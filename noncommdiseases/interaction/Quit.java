package v30.noncommdiseases.interaction;

import v30.noncommdiseases.ObesitySimulationCore;
import v30.noncommdiseases.agent.Activity;
import v30.noncommdiseases.agent.Child;
import v30.noncommdiseases.interaction.functions.QuitActivitySourceFunction;
import v30.noncommdiseases.interaction.functions.QuitActivityTargetFunction;
import fr.lifl.jedi.model.Agent;
import fr.lifl.jedi.model.Environment;
import fr.lifl.jedi.model.interactionDeclaration.SingleTargetInteraction;

/**
 * @author Rabia AZIZA
 */
public class Quit extends SingleTargetInteraction {

	public boolean preconditions(Environment environment, Agent source,
			Agent target) {
		return !((Activity) target).isCurrentlyActive();
	}

	public boolean trigger(Environment environment, Agent source, Agent target) {
		return ((Child) source).isUpdatedI();
	}

	public void perform(Environment environment, Agent source, Agent target) {
		this.setRequiresActivableTarget(false);

		if (ObesitySimulationCore.OUTPUT_CONSOLE_DETAILS)
			System.out.println("Quit (" + source + "," + target + ")");

		((QuitActivitySourceFunction) source).quitActivity();
		((QuitActivityTargetFunction) target).removeChild(source);
		((Child) source).setStatus("goalless");
	}
}
