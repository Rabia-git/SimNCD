package v30.noncommdiseases.interaction.functions;

import v30.noncommdiseases.agent.Activity;

/**
 * @author Rabia AZIZA
 */
public interface QuitActivitySourceFunction {

	public void setStatus(String status);
	
	public boolean isUpdatedI();
	
	public Activity getNextPrePlannedActivity() ;
	
	public void IncrementIndexCurrentActivityInPlan();
	
	/**
	 * Stops practicing the current activity by the source agent and notifies
	 * the activity.
	 */
	public void quitActivity();
}
