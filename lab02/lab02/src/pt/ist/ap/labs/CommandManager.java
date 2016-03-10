package pt.ist.ap.labs;

import java.util.HashMap;
import java.util.Map;

import pt.ist.ap.labs.commands.Command;
import pt.ist.ap.labs.commands.Generic;

public class CommandManager {

	private Map<String, Command> commands = new HashMap<>();
	private Object result = null;

	public CommandManager(Command... cmds) {

		for (Command cmd : cmds) {
			commands.put(cmd.getClass().getName(), cmd);
		}

	}

	public void execute(String... cmdStr) {
		String cmd = cmdStr[0];
		String[] params = new String[cmdStr.length - 1];
		for (int i = 1; i < cmdStr.length; i++) {
			params[i - 1] = cmdStr[i];
		}

		Command c = getCommand(cmd);
		result = c.execute(result, cmdStr);

	}

	private Command getCommand(String cmdStr) {

		String packageName = Command.class.getPackage().getName();
		String className = packageName + "." + cmdStr;

		Command cmd = commands.get(className);
		if (cmd == null)
			cmd = new Generic();
		
		return cmd;

	}
}
