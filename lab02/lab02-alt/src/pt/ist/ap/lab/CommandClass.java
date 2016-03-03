package pt.ist.ap.lab;

public class CommandClass implements Command {

    @Override
    public void execute(Object obj, String[] input) throws ClassNotFoundException {
	
	((Shell) obj).objectClass = Class.forName(input[1]);
	System.out.println(((Shell) obj).objectClass);
    }
}
