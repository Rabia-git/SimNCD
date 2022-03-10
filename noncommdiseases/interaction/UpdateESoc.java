package v30.noncommdiseases.interaction;

import v30.noncommdiseases.ObesitySimulationCore;
import v30.noncommdiseases.agent.Child;
import fr.lifl.jedi.model.Agent;
import fr.lifl.jedi.model.Environment;
import fr.lifl.jedi.model.interactionDeclaration.DegenerateInteraction;

/**
 * @author Rabia AZIZA
 */
public class UpdateESoc extends DegenerateInteraction {
	public boolean preconditions(Environment environment, Agent source) {
		return true;
	}

	public boolean trigger(Environment environment, Agent source) {
		return ((Child) source).isDataShared();
	}

	public void perform(Environment environment, Agent source) {
		if (ObesitySimulationCore.OUTPUT_CONSOLE_DETAILS)
			System.out.println("UpdateESoc-" + source);
//		((Child) source).updateESoc();

		((Child) source).setStatus("updatedESoc");
	}
}
