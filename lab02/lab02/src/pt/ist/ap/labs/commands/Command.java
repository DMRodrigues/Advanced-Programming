package pt.ist.ap.labs.commands;

import java.util.HashMap;
import java.util.Map;

public abstract class Command {
	
	static Map<String, Object> classes = new HashMap<>();
	
	public abstract Object execute(Object clazz, String... param);
	
	
}
