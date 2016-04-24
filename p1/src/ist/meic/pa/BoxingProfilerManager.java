package ist.meic.pa;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

import javassist.CannotCompileException;
import javassist.ClassPool;
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

	private static final String BOX_TEMPLATE = "{ "
			+ "ist.meic.pa.BoxingProfilerManager.inc(\"%s\", \"%s\", \"%s\");"
			+ "$_ = $proceed($$); " + "}";
	private static final String UNBOX_TEMPLATE = ""
            + "{ "
			+       "ist.meic.pa.BoxingProfilerManager.inc(\"%s\", \"%s\", \"%s\");"
<<<<<<< HEAD
			+       "$_ = $proceed(); "
            + "}";

	//TreeMap para garantir ordem natural das chaves
    
	private static Map<String, Map<String, Map<String, Integer>>> resultMap =
    
        new TreeMap<String, Map<String, Map<String, Integer>>>();

=======
			+       "$_ = $proceed(); " + "}";

	// TreeMap para garantir ordem natural das chaves
	private static Map<String, Map<String, Map<String, Integer>>> resultMap = new TreeMap<String, Map<String, Map<String, Integer>>>();
>>>>>>> b4c2ab74997f1da75860fc7fe20b72554f969061

	public BoxingProfilerManager(String[] args) throws NotFoundException {
		this.ctClass = ClassPool.getDefault().get(args[0]);
		String[] restArgs = new String[args.length - 1];
		System.arraycopy(args, 1, restArgs, 0, restArgs.length);
		this.args = restArgs;
		this.argsClass = args.getClass();
	}

	public void profile() {
		for (CtBehavior ctBehavior : ctClass.getDeclaredBehaviors()) {
			
			// adicionar ao mapa um novo método
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
						
						// Operações box chamam sempre o método "valueOf" independentemente da classe
						if (mc.getMethodName().equals("valueOf")) {
							if (!m.containsKey(className)) {
								m.put(className, new TreeMap<String, Integer>());
							}
							m.get(className).put(BOXED, 0);
							//adiciona comportamento(incrementar o contador) antes da invocação do respetivo método
							mc.replace(String.format(BOX_TEMPLATE, methodName, className, BOXED));

						}
						//Operações unbox chamam sempre o método "<tipo>Value"
						else if (mc.getMethodName().equals("intValue") || mc.getMethodName().equals("longValue")
								|| mc.getMethodName().equals("doubleValue") || mc.getMethodName().equals("floatValue")
								|| mc.getMethodName().equals("booleanValue") || mc.getMethodName().equals("byteValue")
								|| mc.getMethodName().equals("charValue") || mc.getMethodName().equals("shortValue")) {
							try {
								String wrapperType = ((CtPrimitiveType) mc.getMethod().getReturnType()).getWrapperName();
								//verifica se o wrapper type do método chamado é igual ao type da classe
								if (wrapperType.equals(className)) {

									if (!m.containsKey(className)) {
										m.put(className, new TreeMap<String, Integer>());
									}
									m.get(className).put(UNBOXED, 0);
									//adiciona comportamento(incrementar o contador) antes da invocação do respetivo método
									mc.replace(String.format(UNBOX_TEMPLATE, methodName, className, UNBOXED));
								}
							}
							catch (NotFoundException e) {
								throw new RuntimeException(e);
							}
						}
					}

				});
			}
			catch (CannotCompileException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	public void run() {
		try {

			// converte a compile-time class (ctClass) num objeto java.lang.Class
			Class<?> rtClass = ctClass.toClass();
			Method main = rtClass.getMethod("main", argsClass);
			main.invoke(null, new Object[] { args });

		} catch (CannotCompileException | NoSuchMethodException | SecurityException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
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
<<<<<<< HEAD
                    if(counter > 0)
                    	System.err.println(methodName + " " + operation + " " + counter + " " + className);
=======
					if (counter > 0)
						System.err.println(methodName + " " + operation + " " + counter + " " + className);
>>>>>>> b4c2ab74997f1da75860fc7fe20b72554f969061
				}
			}
		}
	}

}
