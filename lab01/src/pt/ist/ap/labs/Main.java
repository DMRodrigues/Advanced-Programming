package pt.ist.ap.labs;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		String className = null;
		Scanner sc = new Scanner(System.in);
		try {

			System.out.println("Class-Name? ");
			String packageName = Message.class.getPackage().getName();
			String packagePath = packageName.replace('.', '/');
			
			Enumeration<URL> resources = ClassLoader.getSystemResources(packagePath);
			List<File> dirs = new ArrayList<File>();
			while (resources.hasMoreElements()) {
				URL resource = resources.nextElement();
				dirs.add(new File(resource.getFile()));
			}
			ArrayList<Class> classes = new ArrayList<Class>();
		    for (File directory : dirs) {
		        classes.addAll(findClasses(directory, packageName));
		    }
		    classes.forEach(System.out::println);
			className = sc.nextLine();

			Class clazz = Class.forName(packageName + "." + className);
			Message m = (Message) clazz.newInstance();

			m.say();

		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IOException e) {
			System.out.println("Class " + className + " not found!");
		}

	}

	private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class> classes = new ArrayList<Class>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory() && file.getName().contains(".")) {
					classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				Class clazz = Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
				if(Message.class.isAssignableFrom(clazz) && Message.class != clazz)
					classes.add(clazz);
						
			}
		}
		return classes;
	}
}
