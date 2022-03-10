package v30.noncommdiseases.interaction;

import v30.noncommdiseases.ObesitySimulationCore;
import v30.noncommdiseases.agent.Child;
import fr.lifl.jedi.model.Agent;
import fr.lifl.jedi.model.Environment;
import fr.lifl.jedi.model.interactionDeclaration.DegenerateInteraction;

/**
 * @author Rabia AZIZA
 */
public class Move extends DegenerateInteraction {
	public boolean preconditions(Environment environment, Agent source) {
		return ((Child) source).getPlannedNextActivity() != null;
	}

	public boolean trigger(Environment environment, Agent source) {
		return ((Child) source).hasAGoalActivity();
	}

	public void perform(Environment environment, Agent source) {
		environment.moveToPosition(source, ((Child) source)
				.getPlannedNextActivity().getXPosition(), ((Child) source)
				.getPlannedNextActivity().getYPosition());
		((Child) source).setStatus("onGoal");
		if (ObesitySimulationCore.OUTPUT_CONSOLE_DETAILS)
			System.out.println("-Move-" + source
			/*
			 * + source + "(" + ((Child) source).getXPosition() + "," + ((Child)
			 * source).getYPosition() + "), " + ((Child)
			 * source).getPlannedNextActivity() + "(" + ((Child)
			 * source).getPlannedNextActivity() .getXPosition() + "," + ((Child)
			 * source).getPlannedNextActivity() .getYPosition() + ")"
			 */
			);
	}
}
