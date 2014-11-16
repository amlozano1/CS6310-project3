package data.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class SimulationCriteria implements SimulationDAOConstants{
	private StringBuffer whereClause;
	private List<Object> parameters;
	
	public SimulationCriteria() {
		whereClause = new StringBuffer();
		parameters = new ArrayList<Object>();
	}
	
	public void clear(){
		whereClause.setLength(0);
		parameters.clear();
	}
	
	public List<Object> getParameters() {
		return parameters;
	}

	public void setParameters(List<Object> parameters) {
		this.parameters = parameters;
	}
	
	public SimulationCriteria includes(Date date){
		//TODO need to calculate number of months from sim begin to date and make sure 
		prepend().append("LENGTH >= ? ");
		parameters.add(date);
		return this;
	}
	
	public SimulationCriteria withName(String name){
		prepend().append(NAME).append("=? ");
		parameters.add(name);
		
		return this;
	}
	
	public SimulationCriteria withGridSpacing(Integer gridSpacing){
		prepend().append(GRID_SPACING).append("=? ");
		parameters.add(gridSpacing);
		
		return this;
	}
	
	public SimulationCriteria withOrbitalEccentricity(Double orbitalEcc){
		prepend().append(ORBITAL_ECCENTRICITY).append("=? ");
		parameters.add(orbitalEcc);
		
		return this;
	}
	
	public SimulationCriteria withTimeStep(Integer timeStep){
		prepend().append(TIME_STEP).append("=? ");
		parameters.add(timeStep);
		
		return this;
	}
	
	public SimulationCriteria withAxialTilt(Double axialTilt){
		prepend().append(AXIAL_TILT).append("=? ");
		parameters.add(axialTilt);
		
		return this;
	}
	
	private StringBuffer prepend(){
		if(whereClause.length() == 0){
			whereClause.append("WHERE ");
		} else {
			whereClause.append("AND ");
		}
		
		return whereClause;
	}
	
	public String buildWhere(){
		return whereClause == null ? null : whereClause.toString();
	}
}
