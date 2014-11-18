package data.impl;

import base.Cell;
import base.Simulation;
import base.SimulationParameters;
import base.SimulationResult;

public class TestHelper {
	public static Simulation createSimulation(String simName){
		Simulation simulation = new Simulation();
		simulation.setName(simName);
		
		SimulationParameters parameters = new SimulationParameters();
		parameters.setAxialTilt(23.44d);
		parameters.setGridSpacing((short)15);
		parameters.setLength((short)12);
		parameters.setOrbitalEccentricity(.0167d);
		parameters.setTimeStep(1440);
		parameters.setPrecision((short)10);
		parameters.setGeoPrecision((short)20);
		parameters.setTempPrecision((short)30);
		simulation.setSimulationParameters(parameters);
		
		return simulation;
	}
	
	public static SimulationResult createSimulationResult(){
		Cell[][] resultData = new Cell[10][10];
		for(int x = 1; x<=10; x++){
			for (int y = 1; y <= 10; y++) {
				Cell cell = new Cell();
				cell.setLongitude(x * 100);
				cell.setLatitude(y);
				cell.setTemperature(x*y);
			}
		}
		SimulationResult result = new SimulationResult(resultData);
		result.setSimulationTime(0);
		result.setSunLatitude(45);
		result.setSimulationTime(54);

		return result;
	}
}
