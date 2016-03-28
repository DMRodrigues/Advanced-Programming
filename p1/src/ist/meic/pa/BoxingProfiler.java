package ist.meic.pa;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class BoxingProfiler {

	public static void main(String[] args) {
		
		try {
			CtClass ctClass = ClassPool.getDefault().get(args[0]);

			BoxingProfilerManager profilerManager = new BoxingProfilerManager(ctClass, args);
			profilerManager.profile();
			profilerManager.run();
			profilerManager.printResults();
			
		} catch (NotFoundException e) {
			e.printStackTrace();
		}

	}

}
