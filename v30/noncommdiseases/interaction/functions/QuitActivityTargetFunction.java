package v30.noncommdiseases.interaction.functions;

import fr.lifl.jedi.model.Agent;

/**
 * @author Rabia AZIZA
 */
public interface QuitActivityTargetFunction {

	public boolean isCurrentlyActive();

	/**
	 * Removes the source from the current agents currently practicing the
	 * activity.
	 * 
	 * @param a
	 *            The child.
	 * @return true if the source is successfully removed.
	 */
	public void removeChild(Agent a);
}
