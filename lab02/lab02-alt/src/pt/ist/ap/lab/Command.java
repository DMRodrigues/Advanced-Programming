package pt.ist.ap.lab;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public interface Command {

    // aqui fica melhor, ja q se pode !?
    HashMap<String, Object> rep = new HashMap<String, Object>();

    abstract void execute(Shell obj, String[] input) throws ClassNotFoundException, IllegalAccessException,
	    IllegalArgumentException, InvocationTargetException, NoSuchMethodException;
}
