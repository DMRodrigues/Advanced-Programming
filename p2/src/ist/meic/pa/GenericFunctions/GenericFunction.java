package ist.meic.pa.GenericFunctions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class GenericFunction {

	private final String name;
	private StandardMethodCombination methods = new StandardMethodCombination();

	public GenericFunction(String name) {
		this.name = name;
	}

	public void addMethod(GFMethod method) {
		try {
			Method[] m = method.getClass().getDeclaredMethods();
			if (m.length > 0 && m[0].getName().equals("call"))
				methods.addMethod(method, m[0]);
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	public void addAfterMethod(GFMethod method) {
		try {
			Method[] m = method.getClass().getDeclaredMethods();
			if (m.length > 0 && m[0].getName().equals("call"))
				methods.addAfterMethod(method, m[0]);
		} catch (SecurityException e) {
			e.printStackTrace();
		}

	}

	public void addBeforeMethod(GFMethod method) {
		try {
			Method[] m = method.getClass().getDeclaredMethods();
			if (m.length > 0 && m[0].getName().equals("call"))
				methods.addBeforeMethod(method, m[0]);
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
	public Object call(Object... args) {
		Object result = null;
		Iterator<Entry<GFMethod, Method>> it = null;
		try {
			List<Entry<GFMethod, Method>> effectiveMethod = methods.getEffectiveMethod(args);
			if (effectiveMethod.isEmpty()) {
				throw new IllegalArgumentException(this.getCause(args));
			}
			for (it = effectiveMethod.iterator(); it.hasNext();) {
				Entry<GFMethod, Method> m = it.next();
				Method value = m.getValue();
				GFMethod key = m.getKey();
				if (value.getReturnType() == Object.class)
					result = value.invoke(key, args);
				else
					value.invoke(key, args);
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

		if (result == null)
			result = "";

		return result;
	}

	private String getCause(Object... args) {
		StringBuilder cause = new StringBuilder(
				"\nNo methods for generic function " + name + " with args " + Arrays.deepToString((Object[]) args));
		cause.append("\nof classes [");
		for (Object arg : args)
			cause.append(arg.getClass()).append(", ");
		int length = cause.length();
		cause.replace(length - 2, length, "]");
		return cause.toString();
	}

}
