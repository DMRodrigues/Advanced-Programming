package ist.meic.pa;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class BoxingProfilerManager {

	private CtClass ctClass;

	static final String TEMPLATE = "{ " + 
			"ist.meic.pa.BoxingProfilerManager.inc(\"%s\", \"%s\", \"%s\");"
			+ "$_ = $proceed($$); " + "}";

	static Map<String, Map<String, Map<String, Integer>>> resultMap = new TreeMap<String, Map<String, Map<String, Integer>>>();

	private String[] args;
	private Class<? extends String[]> argsClass;

	public BoxingProfilerManager(CtClass ctClass, String[] args) {
		this.ctClass = ctClass;
		String[] restArgs = new String[args.length - 1];
		System.arraycopy(args, 1, restArgs, 0, restArgs.length);
		this.args = restArgs;
		this.argsClass = args.getClass();
	}

	public void profile(){
		for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {

			if (!resultMap.containsKey(ctMethod.getLongName())) {
				resultMap.put(ctMethod.getLongName(), new TreeMap<String, Map<String, Integer>>());
			}
			
			try {
				ctMethod.instrument(new ExprEditor() {

					@Override
					public void edit(MethodCall mc) throws CannotCompileException {

						String methodName = mc.where().getLongName();
						String className = mc.getClassName();

						Map<String, Map<String, Integer>> m = resultMap.get(methodName);
						if (mc.getMethodName().equals("valueOf")) {
							if (!m.containsKey(className)) {
								m.put(className, new TreeMap<String, Integer>());
							}
							Map<String, Integer> mm = m.get(className);
							mm.put("boxed", 0);
							mc.replace(String.format(TEMPLATE, methodName, className, "boxed"));

						} else if (mc.getMethodName().equals("intValue") || mc.getMethodName().equals("longValue")
								|| mc.getMethodName().equals("doubleValue") || mc.getMethodName().equals("floatValue")
								|| mc.getMethodName().equals("booleanValue") || mc.getMethodName().equals("byteValue")
								|| mc.getMethodName().equals("charValue") || mc.getMethodName().equals("shortValue")) {

							if (!m.containsKey(className)) {
								m.put(className, new TreeMap<String, Integer>());
							}
							Map<String, Integer> mm = m.get(className);
							mm.put("unboxed", 0);
							mc.replace(String.format(TEMPLATE, methodName, className, "unboxed"));
						}

					}

				});
			} catch (CannotCompileException e) {
				System.err.println(e);
			}
		}

		try {
		
			Class<?> rtClass = ctClass.toClass();
			Method main = rtClass.getMethod("main", argsClass);
			main.invoke(null, new Object[] { args });
			
		} catch (CannotCompileException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			System.err.println(e);
		}
	}

	public static void inc(String methodName, String className, String operation) {
		Map<String, Integer> map = resultMap.get(methodName).get(className);
		Integer counter = new Integer(map.get(operation) + 1);
		map.put(operation, counter);
	}

	public void printResults() {

		for (String methodName : resultMap.keySet()) {
			for (String className : resultMap.get(methodName).keySet()) {
				for (String operation : resultMap.get(methodName).get(className).keySet()) {
					Integer mci = resultMap.get(methodName).get(className).get(operation);
					System.err.println(methodName + " " + operation + " " + mci + " " + className);
				}
			}
		}

	}

}
