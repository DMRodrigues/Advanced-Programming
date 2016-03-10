package pt.ist.ap.labs.commands;

public class Get extends Command {


	@Override
	public Object execute(Object obj, String... param) {
		
		if(obj == null){
			System.out.println("You need to specify a class to inspect with command Class <fully-qualified-name> !!!!");
			return obj;
		}
		if(param.length < 2){
			System.out.println("You need to specify a label. Expected: Get <label> !!!!");
			return obj;
		}
		Object c = Command.classes.get(param[1]);
		System.out.println(c);
		
		return c;
	}

}
