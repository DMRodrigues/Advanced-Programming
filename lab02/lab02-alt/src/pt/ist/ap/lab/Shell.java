package pt.ist.ap.lab;

import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

public class Shell {

    private Object objectClass = null;

    public static void main(String[] args) {
	try {
	    Shell main = new Shell();
	    main.run();
	}
	catch (Exception e) {
	    // e.printStackTrace();
	    System.out.println("There were problems mate, sorry!!!");
	}
	System.out.println("Bye, thank you!!!");
    }

    private void run() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
	    IllegalArgumentException, InvocationTargetException, NoSuchMethodException {

	String input[] = null;
	Command commandClass = null;
	Class<?> newClass = null;

	while (true) {
	    System.out.print("Command: ");
	    Scanner scanner = new Scanner(System.in);
	    input = scanner.nextLine().split(" ");

	    try {
		newClass = Class.forName(this.getClass().getPackage().getName() + "." + "Command" + input[0]);
	    }
	    catch (ClassNotFoundException e) { // OMG n encotrei so pode ser generica !!!!!!!!
		newClass = Class.forName(this.getClass().getPackage().getName() + "." + "CommandGeneric"); // se n encontrar chapeuuuuu bye bye
	    }

	    commandClass = ((Command) newClass.newInstance());
	    commandClass.execute(this, input);
	}
    }

    public Object getObjectClass() {
	return objectClass;
    }

    public void setObjectClass(Object objectClass) {
	this.objectClass = objectClass;
    }
}