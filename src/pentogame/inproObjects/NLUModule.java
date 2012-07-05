
/*
 * Wir sammeln ActionIUs in einer Liste, diese haben nur 4 Himmelsrichtungen, 
 * die Pragmatik macht da die Diagonalit�t raus. 
 * Sollte es bei Diagonalit�t aber zu einem "Weiter" kommen m�ssen wir das aber bemerken, mit einer if schleife, die bei jedem "weiter" zwei vorg�nger �berpr�ft
 * Modifikatoren m�ssen bei diagonalit�t �bergeben werden
 * Pausen sind Teil der ActionIUs und werden von den Pragmatikern interpretiert
 * 
 */
package pentogame.inproObjects;

import inpro.incremental.IUModule;
import inpro.incremental.unit.EditMessage;
import inpro.incremental.unit.IU;
import inpro.incremental.unit.WordIU;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * GreifarmActor is the NLU and action management component for the Greifarm prototype
 * 
 * (as the Greifarm prototype is not really a "dialogue system", but rather
 * a speech-controlled application, we avoid the term "dialog manager")
 * 
 * This class consists of the following components, which all live in their 
 * own subclasses:
 * <p>
 * GreifarmActor: the incremental processor (PushBuffer) which handles incoming
 * IUs and initiates the corresponding processing. This class handles the global
 * list of performed actions and of the words yet to process, as well as references
 * to the NLU component, the greifarm handling, and the game score.
 * It also provides a global logger.  
 * <p>
 * NLU: processes incoming words and (depending on what has been done before)
 * initiates actions (in the form of ActionIUs)
 * <p>
 * ActionIU: these {@link inpro.incremental.unit.IU}s are generated,
 * executed and put into the list of performed actions by the NLU. 
 * <p>
 * Greifarm: handles concurrency stuff related to the greifarm GUI (which itself
 * lives in {@link demo.inpro.system.greifarm.gui.GreifArmGUI} 
 * <p>
 * 
 * 
 * @author timo
 *
 */
public class NLUModule extends IUModule {
	
	private static final Logger logger = Logger.getLogger(NLUModule.class);
	private final Deque<ActionIU> performedActions;
	
	/* incrementality/add/revoke related stuff here: */
	/** most recent words that are not yet part of an interpretation */
	private final Deque<WordIU> unusedWords = new ArrayDeque<WordIU>(); 
	/** actions already performed since the last call to processorReset() */
	private final Deque<ActionIU> generatedActions = new ArrayDeque<ActionIU>();
	
	private final NLU nlu = new NLU(unusedWords, generatedActions);
	
	@SuppressWarnings("unchecked")
	@Override
	public void leftBufferUpdate(Collection<? extends IU> ius,
			List<? extends EditMessage<? extends IU>> edits) {
		boolean commitFlag = false; // set to true if there are commit messages
		for (EditMessage<? extends IU> em : edits) {
			logger.debug("received edit message " + em);
			switch (em.getType()) {
			case REVOKE:
				// on revoke, check that this is either the last element of
				// unusedWords, or that unusedWords is empty and
				if (unusedWords.isEmpty()) {
				//	logger.debug("I have to revert an action");
					if (!generatedActions.isEmpty()) {
						ActionIU previousAction = performedActions.peekLast();
						if (!(previousAction instanceof ActionIU.StartActionIU)) { 
							performedActions.pollLast();
							previousAction.revoke();
							unusedWords.addAll((List<WordIU>) previousAction.groundedIn());
						} else {
							logger.warn("something's wrong: " + performedActions + previousAction + unusedWords);
						}
					} else {
						assert false : "Must not revoke when no word has been input.";
					}
				}
				unusedWords.pollLast();
				// check that the correct word was revoked
			//	assert revokedWord.wordEquals((WordIU) em.getIU()) : "Expected " + revokedWord + "\n but I got " + em.getIU();
			//	logger.debug("unused words are now " + unusedWords);
				break;
			case ADD:
				// on add, add this word to unusedWords;
				WordIU addedWord = (WordIU) em.getIU();
				if (addedWord.isSilence() && addedWord.duration() > 0.1) {
					if (!generatedActions.isEmpty())
						generatedActions.getLast().setPrecedesPause(true);
				} else {
					unusedWords.addLast(addedWord);
				}
				break;
			case COMMIT: 
				commitFlag = true;
			}
		}
		nlu.incrementallyUnderstandUnusedWords();
		if (commitFlag) {
			nlu.understandUnusedWordsOnCommit();
			// on commit, we want to notify the corresponding performedAction
			if (!generatedActions.isEmpty())
				generatedActions.getLast().setPrecedesPause(true);
		}
		rightBuffer.setBuffer(generatedActions);
	}
	public void processorReset() {
		unusedWords.clear();
		for (ActionIU aiu : generatedActions) {
			aiu.commit();
		}
		generatedActions.clear();
		//generatedActions.add(new ActionIU.StartActionIU(greifarmController));
	}
}

