package v30.noncommdiseases.interaction.functions;

import v30.noncommdiseases.util.DataSharedBetweenAgents;

/**
 * @author Rabia AZIZA
 */
public interface ShareDataTargetFunction {
	/**
	 * Checks if the source is currently practicing the activity.
	 * @param d 
	 * 
	 * @param a
	 *            The child.
	 * @return true if the source is currently practicing the activity.
	 */
	//public boolean isInActivity(Agent a);

	public void addSharedData(DataSharedBetweenAgents d);
}
