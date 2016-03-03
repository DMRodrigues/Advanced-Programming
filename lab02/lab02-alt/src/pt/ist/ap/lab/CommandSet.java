package pt.ist.ap.lab;

public class CommandSet implements Command {

    @Override
    public void execute(Object obj, String[] input) {
	rep.put(input[1], ((Shell) obj).objectClass);
	System.out.println("Saved name for object of type: " + ((Shell) obj).objectClass.getClass());
	System.out.println(((Shell) obj).objectClass);
    }

}
