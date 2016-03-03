package pt.ist.ap.lab;

public class CommandGet  implements Command {

    @Override
    public void execute(Object obj, String[] input) {
	((Shell)obj).objectClass = rep.get(input[1]);
	System.out.println("Obtained object of type: " + ((Shell)obj).objectClass);
    }

}
