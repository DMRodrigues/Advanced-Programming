package pt.ist.ap.labs.commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Generic extends Command {

	@Override
	public Object execute(Object obj, String... param) {
		if (obj == null) {
			System.out.println("You need to specify a class to inspect with command Class <fully-qualified-name> !!!!");
			return null;
		}
		System.out.print("Trying generic command: ");
		String p = Arrays.toString(param).replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\,", "");
		System.out.println(p);
		java.lang.Class<?>[] paramTypes = null;
		Object[] parameters = new Object[(param.length - 1) / 2];
		int k = 0;
		for (int i = 2; i < param.length; i+=2) {
			parameters[k++] = param[i];
		}
		try {
			Method m;
			if (param.length == 1)
				m = obj.getClass().getMethod(param[0]);
			else {
				paramTypes = new java.lang.Class<?>[(param.length - 1) / 2];
				k = 0;
				for (int i = 1; i < param.length; i += 2)
					paramTypes[k++] = java.lang.Class.forName(param[i]);
				m = obj.getClass().getMethod(param[0], paramTypes);
				
				
			}
			Object res = m.invoke(obj, parameters);
			if (res.getClass().isArray()) {
				Object[] result = (Object[]) res;
				for (Object o : result) {
					System.out.println(o);
				}
				return result;
			} else {
				System.out.println(res);
				return res;
			}

		} catch (IllegalArgumentException | InvocationTargetException | SecurityException | IllegalAccessException e) {
			System.out.println(e);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			for (Method m : obj.getClass().getMethods()) {
				if (m.getName().equals(param[0])) {
					for (int i = 0; i < m.getParameterTypes().length; i++) {
						java.lang.Class<?> pt = m.getParameterTypes()[i];
						if (pt != Object[].class)
							break;
					}
					Object res;
					try {
						res = m.invoke(obj, new Object[]{parameters});
						if (res.getClass().isArray()) {
							Object[] result = (Object[]) res;
							for (Object o : result) {
								System.out.println(o);
							}
							return result;
						} else {
							System.out.println(res);
							return res;
						}
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}

		}
		return obj;
	}

}
