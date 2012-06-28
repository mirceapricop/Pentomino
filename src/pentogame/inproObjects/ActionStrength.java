package pentogame.inproObjects;

public enum ActionStrength {
	NONE, WEAK, NORMAL, STRONG, MAX;
	
	double getDistance() {
		switch (this) {
		case NONE: return 0.0;
		case WEAK: return 1.0;
		case NORMAL: return 3.0;
		case STRONG: return 10.0;
		case MAX: return 99999.0; // this prints more favourably than Double.MAX_VALUE
		default: return 0.0;
		}
	}
	
	/** a distance that is neither 0 nor infinity */
	boolean isNormalDistance() {
		return this != ActionStrength.NONE && this != ActionStrength.MAX;
	}
}
