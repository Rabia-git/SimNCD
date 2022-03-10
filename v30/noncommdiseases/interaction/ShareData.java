package v30.noncommdiseases.interaction;

import java.util.List;

import v30.noncommdiseases.ObesitySimulationCore;
import v30.noncommdiseases.agent.Child;
import v30.noncommdiseases.interaction.functions.ShareDataSourceFunction;
import v30.noncommdiseases.interaction.functions.ShareDataTargetFunction;
import v30.noncommdiseases.util.DataSharedBetweenAgents;
import fr.lifl.jedi.model.Agent;
import fr.lifl.jedi.model.Environment;
import fr.lifl.jedi.model.interactionDeclaration.MultipleTargetInteraction;

/**
 * @author Rabia AZIZA
 */
public class ShareData extends MultipleTargetInteraction {

	@Override
	public boolean isNeighborMeetingCriterion(Environment environment,
			Agent source, Agent target) {
		return (((Child) target).isUpdatedEPhy() ); //|| ((Child) target)
				//.isDataShared())
				//&& ((Child) target).getCurrentPlannedActivity().equals(
					//	((Child) source).getCurrentPlannedActivity());
	}

	@Override
	public boolean trigger(Environment environment, Agent source,
			List<Agent> targets) {
		return ((Child) source).isUpdatedEPhy();
	}

	@Override
	public boolean preconditions(Environment environment, Agent source,
			List<Agent> targets) {
		return true;
	}

	@Override
	public void perform(Environment environment, Agent source,
			List<Agent> targets) {
		if (ObesitySimulationCore.OUTPUT_CONSOLE_DETAILS)
			System.out.println("ShareData (" + source + ")");
		this.setRequiresActivableTarget(false);

		//// shareDataWithAll(Targets)
//		DataSharedBetweenAgents data = ((ShareDataSourceFunction) source)
//				.dataToShare();
//		for (int i = 0; i < targets.size(); i++)
//			((ShareDataTargetFunction) targets.get(i))
//					.addSharedData(data);
		((Child) source).setStatus("dataShared");
	}
}
