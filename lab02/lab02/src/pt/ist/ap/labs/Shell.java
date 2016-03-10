package pt.ist.ap.labs;

import java.util.Scanner;

public class Shell {

	public static void main(String[] args){

		CommandManager cm = new CommandManager(
				new pt.ist.ap.labs.commands.Class(),
				new pt.ist.ap.labs.commands.Set(),
				new pt.ist.ap.labs.commands.Get(),
				new pt.ist.ap.labs.commands.Index(),
				new pt.ist.ap.labs.commands.Generic()
				);
		
		Scanner sc = new Scanner(System.in);
		do {
			System.out.print("Command:> ");
			String[] line = sc.nextLine().split(" ");

			if(line[0].equals("exit"))
				break;
			
			if (line.length < 1) {
				System.out.println("Error: expected <command> <parameter>");
				continue;
			}

			cm.execute(line);

		} while (true);
		sc.close();
	}

	
}
