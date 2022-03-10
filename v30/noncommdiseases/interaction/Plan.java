package v30.noncommdiseases.interaction;

import v30.noncommdiseases.ObesitySimulationCore;
import v30.noncommdiseases.agent.Activity;
import v30.noncommdiseases.agent.Child;
import v30.noncommdiseases.factors.Age;
import fr.lifl.jedi.model.Agent;
import fr.lifl.jedi.model.Environment;
import fr.lifl.jedi.model.interactionDeclaration.DegenerateInteraction;

/**
 * @author Rabia AZIZA
 * 
 *         This interaction prepares the next activity to practice
 */
public class Plan extends DegenerateInteraction {

	public boolean preconditions(Environment environment, Agent source) {
		return (((Age) ((Child) source).factors.get("age")).year <= 18);
		// true; if children continue when adults
	}

	public boolean trigger(Environment environment, Agent source) {
		return ((Child) source).status == "goalless";
	}

	public void perform(Environment environment, Agent source) {
		Activity act = ((Child) source).plan();
		if (ObesitySimulationCore.OUTPUT_CONSOLE_DETAILS)
			System.out.println("Plan (" + source + "," + act + ")");
		((Child) source).setStatus("hasGoal");
	}
}
