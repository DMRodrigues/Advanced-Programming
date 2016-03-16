package pt.ist.ap.lab;

public class CommandClass implements Command {

    @Override
    public void execute(Shell obj, String[] input) throws ClassNotFoundException {
	obj.setObjectClass(Class.forName(input[1]));
	System.out.println(obj.getObjectClass());
    }
}
