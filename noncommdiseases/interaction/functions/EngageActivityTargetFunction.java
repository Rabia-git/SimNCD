package v30.noncommdiseases.interaction.functions;

import fr.lifl.jedi.model.Agent;

/**
 * @author Rabia AZIZA
 */
public interface EngageActivityTargetFunction {
	/**
	 * Checks if the source is eligible to join the activity.
	 * 
	 * @param a
	 *            The child.
	 * @return true if the source is eligible to join the activity.
	 */
	public boolean checkEligibility(Agent a);
	
	public boolean isCurrentlyActive();

	/**
	 * Adds the source as an agent who's currently practicing the activity.
	 * 
	 * @param a
	 *            The child.
	 * @return true if the source is successfully added.
	 */
	public boolean addChild(Agent a);
}
