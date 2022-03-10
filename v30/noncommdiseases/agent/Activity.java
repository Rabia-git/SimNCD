package v30.noncommdiseases.agent;

import java.util.ArrayList;

import v30.noncommdiseases.ObesitySimulationCore;
import v30.noncommdiseases.agent.Child;
import v30.noncommdiseases.cognitive.Characteristic;
import v30.noncommdiseases.interaction.functions.EngageActivityTargetFunction;
import v30.noncommdiseases.interaction.functions.QuitActivityTargetFunction;
import v30.noncommdiseases.util.IntervalConstraint;
import fr.lifl.jedi.model.Agent;

/**
 * The java class that represents the activity agent family in the simulation of
 * childhood obesity.
 * 
 * @author Rabia AZIZA
 */
public abstract class Activity extends Agent implements
		EngageActivityTargetFunction, QuitActivityTargetFunction {

	public ArrayList<Child> currentParticipants = new ArrayList<Child>();
	public ArrayList<IntervalConstraint> hourIntervals;
	public ArrayList<Integer> dayConstrains;
	public ArrayList<IntervalConstraint> yearIntervals;

	// Cognitive
	public ArrayList<Characteristic> characteristics;

	/**
	 * Constructor of the Activity agent family.
	 */
	// public Activity() {super(1, 1, null);}

	public Activity(ArrayList<IntervalConstraint> yearIntervals,
			ArrayList<Integer> dayConstrains,
			ArrayList<IntervalConstraint> hourIntervals,
			ArrayList<Characteristic> characteristics) {
		super(1, 1, null);
		this.dayConstrains = dayConstrains;
		this.hourIntervals = hourIntervals;
		this.yearIntervals = yearIntervals;
		this.characteristics = characteristics;
	}

	public void update() {
	}

	@Override
	public boolean checkEligibility(Agent a) {
		return (a instanceof Child);
	}

	@Override
	public boolean isCurrentlyActive() {
		for (int i = 0; i < this.dayConstrains.size(); i++)
			if (ObesitySimulationCore.day % 5 == this.dayConstrains.get(i) - 1) {
				for (int j = 0; j < this.hourIntervals.size(); j++)
					if (ObesitySimulationCore.hour >= this.hourIntervals.get(j).start)
						if (ObesitySimulationCore.hour < this.hourIntervals
								.get(j).ending)
							return true;
				return false;
			}
		return false;
	}

	public boolean isActiveAtHour(int hour) {
		for (int i = 0; i < this.hourIntervals.size(); i++)
			if (hour >= this.hourIntervals.get(i).start)
				if (hour < this.hourIntervals.get(i).ending)
					return true;
		return false;
	}

	// public IntervalConstraint getCurrentHourInterval() {
	// if (this.isCurrentlyActive())
	// for (int i = 0; i < this.hourIntervals.size(); i++)
	// if (ObesitySimulationCore.hour >= this.hourIntervals.get(i).start)
	// if (ObesitySimulationCore.hour < this.hourIntervals.get(i).ending)
	// return this.hourIntervals.get(i);
	// return null;
	// }

	// public boolean isPastHalfTime() {
	// if ((this.getCurrentHourInterval() != null)
	// && (ObesitySimulationCore.hour >= this.getCurrentHourInterval().start
	// + this.getCurrentHourInterval().duration()/2))
	// return true;
	// return false;
	// }

	@Override
	public boolean addChild(Agent a) {
		// if (this.checkEligibility(a)) {
		// for (int i = 0; i < this.currentParticipants.size(); i++) {
		// if (this.currentParticipants.get(i).equals(a))
		// return true;
		// }
		// *** We do not have a maximum number of participants
		return this.currentParticipants.add((Child) a);
		// return true;
		// }
		// return false;
	}

	@Override
	public void removeChild(Agent a) {
		this.currentParticipants.remove(a);
	}

	public String listParticipants() {
		String s = this.toString();
		for (int i = 0; i < this.currentParticipants.size(); i++)
			s += " + " + this.currentParticipants.get(i);
		return s;
	}

	public abstract double getContribMVPA();
}
