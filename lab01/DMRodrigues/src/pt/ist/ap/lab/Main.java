package pt.ist.ap.lab;

import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

	try {
	    Main main = new Main();
	    main.execute();
	} catch (Exception e) {
	    System.out.println("There were problems mate, sorry!!!");
	}

	System.out.println("All done, thank you!!!");

    }

    public void execute() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
	    NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {

	System.out.print("Nome classe: ");
	Scanner scanner = new Scanner(System.in);
	String nameClass = scanner.next();

	/* seguro por agr ir buscar package assim */
	Class<?> newClass = Class.forName(this.getClass().getPackage().getName() + "." + nameClass);
	Message objectClass = ((Message) newClass.newInstance());

	//newClass.getMethod("say").invoke(objectClass);

	objectClass.say();
    }

}
