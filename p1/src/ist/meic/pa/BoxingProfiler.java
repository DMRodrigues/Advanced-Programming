package ist.meic.pa;

import javassist.NotFoundException;

public class BoxingProfiler {

	public static void main(String[] args) {
		
		try {
			BoxingProfilerManager profilerManager = new BoxingProfilerManager(args);
			profilerManager.profile();
			profilerManager.run();
			profilerManager.printResults();
			
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}

	}

}
