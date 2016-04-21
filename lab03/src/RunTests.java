public class RunTests {

	public static void main(String[] args) throws SecurityException, ClassNotFoundException {
		
		String[] classes = new String[]{"TestSimple", "TestWithSetup", "TestInheritance"};
		
		for(String className : classes){
			
			Class<?> clazz = Class.forName(className);
			Runner runner = new Runner();
			runner.run(clazz);
			System.out.println("---------------------------------------------------------");
		}
	}

}
