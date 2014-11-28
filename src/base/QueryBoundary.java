package base;

import exceptions.QueryBoundaryException;

public class QueryBoundary {
	
	private Double eastBoundary, westBoundary, southBoundary, northBoundary;
	
	public QueryBoundary(Double north, Double south, Double east, Double west) throws QueryBoundaryException{
		
		checkPair(north,south);
		checkPair(east, west);
		
		checkValue(north, 90.0);
		checkValue(south, 90.0);
		checkValue(east, 180.0);
		checkValue(west, 180.0);
		
		
		northBoundary = (north == null) ?  90.0 : north;
		southBoundary = (south == null) ? -90.0 : south;
		eastBoundary = (east == null)  ?  180.0 : east;
		westBoundary = (west == null)  ? -180.0 : west;
		
	}
	
	private void checkPair(Double a, Double b) throws QueryBoundaryException{
		if((a==null) != (b==null))
			throw new QueryBoundaryException("One value in pair ("+a+","+b+") is null. Both or neither must be null");
		
		if(a.equals(b))
			throw new QueryBoundaryException("The longitude or latitude values cannot match other similar boundary.");
	}
	
	private void checkValue(Double bound, double limit) throws QueryBoundaryException{
		if(bound != null){
			if(Math.abs(bound) > limit)
				throw new QueryBoundaryException("The value "+bound+" does not reside within the range of -"+limit+" to "+limit+".");
		}
	}
	
	
	public Double getEast(){
		return eastBoundary;
	}
	
	public Double getWest(){
		return westBoundary;
	}
	
	public Double getNorth(){
		return northBoundary;
	}
	
	public Double getSouth(){
		return southBoundary;
	}
}
