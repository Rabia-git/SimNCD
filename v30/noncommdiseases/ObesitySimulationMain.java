package v30.noncommdiseases;

import java.awt.Color;

import v30.noncommdiseases.agent.Child;
import v30.noncommdiseases.agent.CollegeSchool;
import v30.noncommdiseases.agent.CulturalClub;
import v30.noncommdiseases.agent.Home;
import v30.noncommdiseases.agent.Park;
import v30.noncommdiseases.agent.PrimarySchool;
import v30.noncommdiseases.agent.SecondarySchool;
import v30.noncommdiseases.agent.SportClub;

import fr.lifl.jedi.controllersCore.AbstractControlGUIController;
import fr.lifl.jedi.gui.control.graphicalControl.controller.GraphicalSimulationControlGUIController;
import fr.lifl.jedi.gui.display.colorGrid.ColorGridGUIController;
import fr.lifl.jedi.gui.display.colorGrid.view.AgentDisplayer;
import fr.lifl.jedi.gui.display.colorGrid.view.ColorGridGUI;
import fr.lifl.jedi.gui.display.colorGrid.view.agentDisplay.ColorRectangleDisplayer;
import fr.lifl.jedi.gui.display.colorGrid.view.agentDisplay.StaticColorTriangleDisplayer;
import fr.lifl.jedi.model.Environment;

/**
 * @author Rabia AZIZA
 */
public class ObesitySimulationMain {
	/**
	 * Main method of the simulation.
	 */
	public static void main(String[] args) {
		// Dimensions of the environment
		Environment e = new Environment(110, 60);
		e.setXAxisTorus(false);
		e.setYAxisTorus(false);

		AbstractControlGUIController<?> simulationControlGUI = new GraphicalSimulationControlGUIController();

		ObesitySimulationCore core = new ObesitySimulationCore(
				simulationControlGUI, e);

		/* FROM THIS POINT ON, THE SIMULATION CAN BE INITIALIZED AND PERFORMED */

		// Creation of the frame that will display the simulation.
		ColorGridGUI gui = new ColorGridGUI(e, "Obesity Simulation GUI", 6);

		AgentDisplayer disp = new StaticColorTriangleDisplayer(Color.YELLOW, 1, false);
		gui.getGraphicalModel().setHowToDisplay(Child.class, disp);

		disp = new ColorRectangleDisplayer(new Color(0, 206, 209), true);
		gui.getGraphicalModel().setHowToDisplay(PrimarySchool.class, disp);

		disp = new ColorRectangleDisplayer(new Color(30, 144, 255), true);
		gui.getGraphicalModel().setHowToDisplay(CollegeSchool.class, disp);

		disp = new ColorRectangleDisplayer(new Color(65, 105, 225), true);
		gui.getGraphicalModel().setHowToDisplay(SecondarySchool.class, disp);

		disp = new ColorRectangleDisplayer(new Color(255,99,71), true);
		gui.getGraphicalModel().setHowToDisplay(Home.class, disp);

		disp = new ColorRectangleDisplayer(new Color(34, 139, 34), true);
		gui.getGraphicalModel().setHowToDisplay(Park.class, disp);

		disp = new ColorRectangleDisplayer(new Color(218, 112, 214), true);
		gui.getGraphicalModel().setHowToDisplay(CulturalClub.class, disp);

		disp = new ColorRectangleDisplayer(new Color(255, 0, 255), true);
		gui.getGraphicalModel().setHowToDisplay(SportClub.class, disp);

		ColorGridGUIController ctrl = new ColorGridGUIController(gui);
		core.registerGUIControler(ctrl);
	}
}
