package pt.ist.ap.labs.commands;

public class Set extends Command {

	@Override
	public Object execute(Object obj, String... param) {
		
		if(obj == null){
			System.out.println("You need to specify a class to inspect with command Class <fully-qualified-name> !!!!");
			return obj;
		}
		if(param.length < 2){
			System.out.println("You need to specify a label. Expected: Set <label> !!!!");
			return obj;
		}
		Command.classes.put(param[1], obj);
		System.out.println("Saved name for object of type: " + obj.getClass()); 
		System.out.println(obj);
		
		return obj;
	}

}
