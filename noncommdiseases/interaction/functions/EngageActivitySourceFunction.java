package v30.noncommdiseases.interaction.functions;

import v30.noncommdiseases.agent.Activity;

/**
 * @author Rabia AZIZA
 */
public interface EngageActivitySourceFunction {
	

	public void setStatus(String status);
	
	public boolean isOnGoal();
	
	
	
	/**
	 * Asks to join the activity then Starts it and adds it to the history of
	 * the source agent.
	 * 
	 * @param a
	 *            The activity to practice.
	 */
	public void engageActivity(Activity a);
}
