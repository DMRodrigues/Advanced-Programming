package pt.ist.ap.labs.commands;

public class Class extends Command {


	@Override
	public Object execute(Object obj, String... param) {
		java.lang.Class<?> clazz;
		try {
			clazz = java.lang.Class.forName(param[1]);
			System.out.println(clazz);
			return clazz;
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

}
