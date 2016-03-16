package pt.ist.ap.lab;

public class CommandSet implements Command {

    @Override
    public void execute(Shell obj, String[] input) {
	rep.put(input[1], obj.getObjectClass());
	System.out.println("Saved name for object of type: " + obj.getObjectClass().getClass());
	System.out.println(obj.getObjectClass());
    }
}
