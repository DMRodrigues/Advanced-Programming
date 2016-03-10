package pt.ist.ap.lab;

public class CommandGet implements Command {

    @Override
    public void execute(Shell obj, String[] input) {
	obj.setObjectClass(rep.get(input[1]));
	System.out.println("Obtained object of type: " + obj.getObjectClass());
    }
}
