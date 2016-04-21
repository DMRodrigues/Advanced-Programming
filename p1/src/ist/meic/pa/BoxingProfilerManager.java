package ist.meic.pa;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtPrimitiveType;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class BoxingProfilerManager {

	private CtClass ctClass;
	private String[] args;
	private Class<? extends String[]> argsClass;

	private static final String BOXED = "boxed";
	private static final String UNBOXED = "unboxed";

<<<<<<< HEAD
	private static final String BOX_TEMPLATE = "{ "
			+ "ist.meic.pa.BoxingProfilerManager.inc(\"%s\", \"%s\", \"%s\");"
			+ "$_ = $proceed($$); " + "}";
	private static final String UNBOX_TEMPLATE = ""
            + "{ "
			+       "ist.meic.pa.BoxingProfilerManager.inc(\"%s\", \"%s\", \"%s\");"
			+       "$_ = $proceed(); "
            + "}";

	//TreeMap para garantir ordem natural das chaves
    
	private static Map<String, Map<String, Map<String, Integer>>> resultMap =
    
        new TreeMap<String, Map<String, Map<String, Integer>>>();
=======
	private static final String BOX_TEMPLATE = "{ " + "ist.meic.pa.BoxingProfilerManager.inc(\"%s\", \"%s\", \"%s\");" + "$_ = $proceed($$); " + "}";
	private static final String UNBOX_TEMPLATE = "{ " + "ist.meic.pa.BoxingProfilerManager.inc(\"%s\", \"%s\", \"%s\");" + "$_ = $proceed(); " + "}";

	private static Map<String, Map<String, Map<String, Integer>>> resultMap = new TreeMap<String, Map<String, Map<String, Integer>>>();
>>>>>>> 42690032c1de9983d7b4b32a652a37c6a7c89ac0

	public BoxingProfilerManager(CtClass ctClass, String[] args) {
		this.ctClass = ctClass;
		String[] restArgs = new String[args.length - 1];
		System.arraycopy(args, 1, restArgs, 0, restArgs.length);
		this.args = restArgs;
		this.argsClass = args.getClass();
	}

	public void profile() {

		for (CtBehavior ctBehavior : ctClass.getDeclaredBehaviors()) {

			if (!resultMap.containsKey(ctBehavior.getLongName())) {
				resultMap.put(ctBehavior.getLongName(), new TreeMap<String, Map<String, Integer>>());
			}

			try {
				ctBehavior.instrument(new ExprEditor() {

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
							mm.put(BOXED, 0);
							mc.replace(String.format(BOX_TEMPLATE, methodName, className, BOXED));

						}
						else if (mc.getMethodName().equals("intValue") || mc.getMethodName().equals("longValue") || mc.getMethodName().equals("doubleValue") || mc.getMethodName().equals("floatValue")
								|| mc.getMethodName().equals("booleanValue") || mc.getMethodName().equals("byteValue") || mc.getMethodName().equals("charValue")
								|| mc.getMethodName().equals("shortValue")) {
							try {
								String wrapperType = ((CtPrimitiveType) mc.getMethod().getReturnType()).getWrapperName();
								if (wrapperType.equals(className)) {

									if (!m.containsKey(className)) {
										m.put(className, new TreeMap<String, Integer>());
									}
									Map<String, Integer> mm = m.get(className);
									mm.put(UNBOXED, 0);
									mc.replace(String.format(UNBOX_TEMPLATE, methodName, className, UNBOXED));
								}
							}
							catch (NotFoundException e) {
								System.err.println(e);
							}
						}

					}

				});
			}
			catch (CannotCompileException e) {
				System.err.println(e);
			}
		}

		try {

			Class<?> rtClass = ctClass.toClass();
			Method main = rtClass.getMethod("main", argsClass);
			main.invoke(null, new Object[] { args });

		}
		catch (CannotCompileException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
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
                    
					Integer counter = resultMap.get(methodName).get(className).get(operation);
                    
                    System.err.println(methodName + " " + operation + " " + counter + " " + className);
				}
			}
		}

	}

}
