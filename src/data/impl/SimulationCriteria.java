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
	
	public SimulationCriteria between(Date from, Date to){
		after(from);
		before(to);
		return this;
	}
	
	public SimulationCriteria before(Date date){
		prepend().append("RUNDATE <= ? ");
		parameters.add(date);
		return this;
	}
	
	public SimulationCriteria after(Date date){
		prepend().append("RUNDATE >= ? ");
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
	
	public static void main(String[] args) {
		SimulationCriteria criteria = new SimulationCriteria();
		criteria.between(new Date(0), new Date(System.currentTimeMillis()));
		criteria.withAxialTilt(100d).withGridSpacing(10).withName("SOMENAME").withOrbitalEccentricity(.5d).withTimeStep(5);
		
		System.out.println(criteria.buildWhere());
	}
}
