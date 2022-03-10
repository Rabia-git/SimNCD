package v30.noncommdiseases.util;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import v30.noncommdiseases.ObesitySimulationCore;
import v30.noncommdiseases.agent.Child;
import v30.noncommdiseases.agent.CulturalClub;
import v30.noncommdiseases.agent.Home;
import v30.noncommdiseases.agent.Park;
import v30.noncommdiseases.agent.SportClub;
import v30.noncommdiseases.factors.Age;
import v30.noncommdiseases.factors.BMI;
import v30.noncommdiseases.factors.Gender;
import v30.noncommdiseases.factors.MVPA;

public class Output {
	public static void insertToDB_ChildData(Child child) {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdfD = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdfT = new SimpleDateFormat("HH:mm:ss");
		try {
			Statement stmt = ObesitySimulationCore.connection.createStatement();

			stmt.executeUpdate("INSERT INTO \""
					+ ObesitySimulationCore.OUTPUT_TableUpdates
					+ "\" (child_name, gender, sim_y, age_y, mvpa, b_mvpa, d_mvpa,"
					+ "bmi_status, bmi, b_bmi, d_bmi, sys_date, sys_time,"
					+ "nb_sportclub, nb_park, nb_culturalclub, nb_home) VALUES ("
					+ "'"
					+ child
					+ "', "
					+ ((Gender) child.factors.get("gender")).value
					+ ", "
					+ ObesitySimulationCore.year
					+ ", "
					+ ((Age) child.factors.get("age")).year
					+ ", "
					+ 0 // v29 deleted: ((MVPA)
						// child.factors.get("mvpa")).annualMVPAold
					+ ", "
					+ ((MVPA) child.factors.get("mvpa")).basicMVPA
					+ ", "
					+ 0 // v29 deleted: (((MVPA)
						// child.factors.get("mvpa")).annualMVPAold - ((MVPA)
						// child.factors.get("mvpa")).basicMVPA)
					+ ", '"
					+ ((BMI) child.factors.get("bmi")).term
					+ "', "
					+ (((BMI) child.factors.get("bmi")).basicBMI + ((BMI) child.factors
							.get("bmi")).dBMI) + ", "
					+ ((BMI) child.factors.get("bmi")).basicBMI + ", "
					+ ((BMI) child.factors.get("bmi")).dBMI + ", '"
					+ sdfD.format(date) + "', '" + sdfT.format(date) + "',"
					+ child.ifActivityIsPerceived(SportClub.class) + ", "
					+ child.ifActivityIsPerceived(Park.class) + ", "
					+ child.ifActivityIsPerceived(CulturalClub.class) + ", "
					+ child.ifActivityIsPerceived(Home.class) + ");");
			if (ObesitySimulationCore.COGNITIVE_REASONING) {
				for (int i = 0; i < child.preferences.size(); i++)
					stmt.executeUpdate("INSERT INTO \""
							+ ObesitySimulationCore.OUTPUT_TablePreferences
							+ "\" (child_name, criterion, sub_criterion, preference) VALUES ("
							+ "'"
							+ child
							+ "', '"
							+ child.preferences.get(i).subCriterion.criterion.label
							+ "', '"
							+ child.preferences.get(i).subCriterion.label
							+ "', " + child.preferences.get(i).lingTerm + ");");

				stmt.executeUpdate("INSERT INTO \""
						+ ObesitySimulationCore.OUTPUT_TableSatisfactions
						+ "\" (child_name, satisf_home, satisf_park, satisf_sportclub) VALUES ("
						+ "'" + child + "', "
						+ child.getRelativeProximity(Home.class) + ", "
						+ child.getRelativeProximity(Park.class) + ", "
						+ child.getRelativeProximity(SportClub.class) + ");");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void insertToDB_ChildData_Annual(Child child) {
		try {
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat sdfD = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat sdfT = new SimpleDateFormat("HH:mm:ss");
			Statement stmt = ObesitySimulationCore.connection.createStatement();
			stmt.executeUpdate("INSERT INTO \""
					+ ObesitySimulationCore.OUTPUT_TableUpdates
					+ "\" (child_name, gender, sim_y, age_y, mvpa, b_mvpa, d_mvpa,"
					+ "bmi_status, bmi, b_bmi, d_bmi, sys_date, sys_time,"
					+ "nb_sportclub, nb_park, nb_culturalclub, nb_home, contribmvpa, satisfaction) VALUES ("
					+ "'"
					+ child
					+ "', "
					+ ((Gender) child.factors.get("gender")).value
					+ ", "
					+ (ObesitySimulationCore.year + 1)
					+ ", "
					+ ((Age) child.factors.get("age")).year
					+ ", "
					+ ((MVPA) child.factors.get("mvpa")).getAverageAnnualMVPA()
					+ ", "
					+ ((MVPA) child.factors.get("mvpa")).basicMVPA
					+ ", "
					+ (((MVPA) child.factors.get("mvpa"))
							.getAverageAnnualMVPA() - ((MVPA) child.factors
							.get("mvpa")).basicMVPA) + ", " + "'"
					+ ((BMI) child.factors.get("bmi")).term + "', "
					+ (BMI) child.factors.get("bmi") + ", "
					+ ((BMI) child.factors.get("bmi")).basicBMI + ", "
					+ ((BMI) child.factors.get("bmi")).dBMI + ", '"
					+ sdfD.format(date) + "', '" + sdfT.format(date) + "',"
					+ child.nb_sc + ", " + child.nb_p + ", " + child.nb_cc
					+ ", " + child.nb_h + " ," + child.yearlyContribMVPA + ", "
					+ child.yearlySatisfactionAPL + ");");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void prepareDB(String sysMS) {
		ObesitySimulationCore.OUTPUT_TableUpdates = "Li_5";
							// sysMS;//*******************
		ObesitySimulationCore.OUTPUT_TablePreferences = ObesitySimulationCore.OUTPUT_TableUpdates
				+ "_P";
		ObesitySimulationCore.OUTPUT_TableSatisfactions = ObesitySimulationCore.OUTPUT_TableUpdates
				+ "_S";
		try {
			Class.forName("org.postgresql.Driver");
			ObesitySimulationCore.connection = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/simulation_v30",
					"postgres", "admin");
			Statement stmt = ObesitySimulationCore.connection.createStatement();
			stmt.executeUpdate("create table \""
					+ ObesitySimulationCore.OUTPUT_TableUpdates
					+ "\" as select * from public.\"MAS\";");
			if (ObesitySimulationCore.COGNITIVE_REASONING) {
				stmt.executeUpdate("create table \""
						+ ObesitySimulationCore.OUTPUT_TablePreferences
						+ "\" as select * from public.\"Preferences\";");
				stmt.executeUpdate("create table \""
						+ ObesitySimulationCore.OUTPUT_TableSatisfactions
						+ "\" as select * from public.\"Satisfactions\";");
			}
			stmt.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
}