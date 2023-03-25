package entities;

import sharedRegions.*;

public class CommonThief extends Thread {
	/**
	 * 
	 */
	private int commonThiefId;
	
	/**
	 * 
	 */
	private int commonThiefState;

	/**
	 * 
	 */
	private int maximumDisplacement;
	
	/**
	 * 
	 */
	private boolean holdingACanvas;

	private int assaultParty;

	/**
	 * 
	 */
	private final AssaultParty[] assPart;
	
	/**
	 * 
	 */
	private final ControlSite contSite;
	
	/**
	 * 
	 */
	private final ConcentrationSite concSite;
	
	/**
	 * 
	 */
	private final Museum museum;
	
	/**
	 * 
	 */
	private final GeneralRepository genRep;
	
	public CommonThief(int commonThiefId, AssaultParty[] assPart, ControlSite contSite, ConcentrationSite concSite,
			Museum museum, GeneralRepository genRep) {
		this.commonThiefId = commonThiefId;
		this.commonThiefState = 0;
		this.assPart = assPart;
		this.contSite = contSite;
		this.concSite = concSite;
		this.museum = museum;
		this.genRep = genRep;
	}

	/**
	 * @return the commonThiefState
	 */
	public int getCommonThiefState() {
		return commonThiefState;
	}

	/**
	 * @param commonThiefState the commonThiefState to set
	 */
	public void setCommonThiefState(int commonThiefState) {
		this.commonThiefState = commonThiefState;
	}

	/**
	 * @return the commonThiefId
	 */
	public int getCommonThiefId() {
		return commonThiefId;
	}

	public int getMaximumDisplacement() {
		return maximumDisplacement;
	}

	public void setHoldingACanvas() {
		holdingACanvas = true;
	}

	public void unsetHoldingACanvas() {
		holdingACanvas = false;
	}

	public boolean getHoldingACanvas() {
		return holdingACanvas;
	}

	public void setMaximumDisplacement(int maximumDisplacement) {
		this.maximumDisplacement = maximumDisplacement;
	}

	public int getAssaultParty() {
		return assaultParty;
	}

	@Override
	public void run() {
		while(concSite.amINeeded() != 'E') {
			assaultParty = concSite.prepareExcursion();
			while(assPart[assaultParty].crawlIn());
			museum.rollACanvas();
			assPart[assaultParty].reverseDirection();
			while(assPart[assaultParty].crawlOut());
			contSite.handACanvas();
		}
	}
}
