import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Runner {

	private int passed = 0, failed = 0;

	public void run(Class<?> clazz) {
		Class<?> superClazz = clazz.getSuperclass();
		if (superClazz != null && superClazz != Object.class) {
			run(superClazz);
		}

		for (Method m : clazz.getDeclaredMethods()) {
			if (!Modifier.isStatic(m.getModifiers()) || m.getParameterCount() > 0 || m.getReturnType() != void.class)
				continue;

			if (m.isAnnotationPresent(Test.class) || m.isAnnotationPresent(org.junit.Test.class)) {
				try {
					if (m.getAnnotation(Test.class) != null) {
						String[] value = m.getAnnotation(Test.class).value();
						for (String setupName : value) {
							invokeSetup(setupName, clazz);

						}
					}
					m.setAccessible(true);
					m.invoke(null);
					System.out.printf("Test %s OK!\n", m);
					passed++;
				} catch (Throwable ex) {
					System.out.printf("Test %s failed!\n", m);
					failed++;
				}
			}
		}

		System.out.printf("Passed: %d, Failed: %d%n", passed, failed);

	}

	private void invokeSetup(String name, Class<?> clazz)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		Class<?> superClazz = clazz.getSuperclass();
		if (superClazz != null && superClazz != Object.class) {
			invokeSetup(name, superClazz);
		}

		for (Method setupMethod : clazz.getDeclaredMethods()) {
			Setup setupAnnotation = setupMethod.getAnnotation(Setup.class);
			if (setupAnnotation != null) {
				String setupAnnotationValue = setupAnnotation.value();
				if (name.equals("*") || setupAnnotationValue.equals(name)) {
					setupMethod.setAccessible(true);
					setupMethod.invoke(null);
				}
			}

		}
	}
}
