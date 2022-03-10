package v30.noncommdiseases.interaction;

import v30.noncommdiseases.ObesitySimulationCore;
import v30.noncommdiseases.agent.Activity;
import v30.noncommdiseases.agent.Child;
import v30.noncommdiseases.interaction.functions.EngageActivitySourceFunction;
import v30.noncommdiseases.interaction.functions.EngageActivityTargetFunction;
import fr.lifl.jedi.model.Agent;
import fr.lifl.jedi.model.Environment;
import fr.lifl.jedi.model.interactionDeclaration.SingleTargetInteraction;

/**
 * @author Rabia AZIZA
 */
public class Engage extends SingleTargetInteraction {

	public boolean preconditions(Environment environment, Agent source,
			Agent target) {
		return ((Activity) target).isCurrentlyActive() ;
	}

	public boolean trigger(Environment environment, Agent source, Agent target) {
		return ((Child) source).isOnGoal();
	}

	public void perform(Environment environment, Agent source, Agent target) {
		if (ObesitySimulationCore.OUTPUT_CONSOLE_DETAILS)
			System.out.println("Engage (" + source + "," + target + ")");
		this.setRequiresActivableTarget(false);

		if (((EngageActivityTargetFunction) target).addChild(source)) {
			((EngageActivitySourceFunction) source).engageActivity((Activity)target);
			((Child) source).setStatus("achievingGoal");
		} else
			((Child) source).setStatus("goalless");
	}
}
