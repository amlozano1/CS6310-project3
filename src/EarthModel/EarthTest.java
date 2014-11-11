/*
 * This code came from Wade Ashby's teams Project 2 code
 */
package EarthModel;

class EarthTest {
	public static void main(String args[]) {
		int gs = 45;
		int timestep = 120;
		int timeElapsed = 0;
		Earth earth = new Earth(gs, timeElapsed);
		earth.displayGrid();
		for (int i=0; i < 5; i++) {
			Earth newEarth = earth.iterate(timestep);
			earth = newEarth;
			earth.displayGrid();
		}
	}
}