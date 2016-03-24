package ist.meic.pa;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.TreeMap;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class BoxingProfiler {

	private static final String[] typeValue = { "intValue", "longValue", "floatValue", "charValue", "doubleValue", "booleanValue", "shortValue", "byteValue" };

	private static TreeMap<String, TreeMap<String, TreeMap<String, Integer>>> boxingResult = new TreeMap<String, TreeMap<String, TreeMap<String, Integer>>>();

	private static String[] args = null;

	private static CtClass ctClass = null;
	private static String[] ctClassArgs = null;

	public static void main(String[] args) {
		processInput(args);
		//doPrintMethodInfo();
		doMethodEdit();
		executeWork();
		printResult();

	}

	private static void processInput(String[] argts) {
		try {
			args = argts;

			// tratar classpath ou deixar assim ? "ist.meic.pa." + 
			ctClass = ClassPool.getDefault().get(args[0]);
			ctClassArgs = new String[args.length - 1];
			System.arraycopy(args, 1, ctClassArgs, 0, ctClassArgs.length);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void executeWork() {
		try {
			Class<?> rtClass = ctClass.toClass();
			Method main = rtClass.getMethod("main", args.getClass());
			main.invoke(null, new Object[] { ctClassArgs });
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void doPrintMethodInfo() {
		try {
			for (CtMethod m : ctClass.getDeclaredMethods()) {
				System.out.println(m.getLongName());
				m.instrument(new ExprEditor() {
					public void edit(MethodCall mc) throws CannotCompileException {
						System.out.println("	" + mc.getMethodName() + " => " + mc.getSignature());
					}
				});
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void doMethodEdit() {
		try {
			for (final CtMethod m : ctClass.getDeclaredMethods()) {

				boxingResult.put(m.getLongName(), new TreeMap<String, TreeMap<String, Integer>>());

				m.instrument(new ExprEditor() {
					public void edit(MethodCall mc) throws CannotCompileException {

						// boxed operations
						if (mc.getMethodName().equals("valueOf")) {// && getTypeVar(mc.getSignature()) != null) {

							if (boxingResult.get(m.getLongName()).get(mc.getClassName()) == null)
								boxingResult.get(m.getLongName()).put(mc.getClassName(), new TreeMap<String, Integer>());

							boxingResult.get(m.getLongName()).get(mc.getClassName()).put("boxed", 0);
							mc.replace(createBoxedValueOf(m.getLongName(), mc.getClassName()));
						}

						// unboxed operations
						else if (Arrays.asList(typeValue).contains(mc.getMethodName())) {

							if (compareTypeName(mc.getMethodName(), mc.getClassName())) {

								if (boxingResult.get(m.getLongName()).get(mc.getClassName()) == null)
									boxingResult.get(m.getLongName()).put(mc.getClassName(), new TreeMap<String, Integer>());

								boxingResult.get(m.getLongName()).get(mc.getClassName()).put("unboxed", 0);
								mc.replace(createUnboxedValueOf(m.getLongName(), mc.getClassName()));
							}
						}
					}
				});

			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* compare the name of method called to the type of object which is called on
	*  to verify they match and therefore an unbox operation happened
	*/
	protected static boolean compareTypeName(String mthName, String className) {
		//System.out.println(mthName + "===" + className);
		if (className.toLowerCase().regionMatches(10, mthName, 0, 2)) // WARNING may not be enough
			return true;
		return false;
	}

	// maybe not needed
	private static String removePackage(String s) {
		// ist.meic.pa.[12] -> conta a partir daqui
		if (s.startsWith("ist.meic.pa."))
			return s.substring(12, s.length());
		return s;
	}

	private static String createBoxedValueOf(String mthName, String varType) {
		return "{ ist.meic.pa.BoxingProfiler.incValueBoxed(\"" + mthName + "\",\"" + varType + "\");" + "$_ = $proceed($$); }";
	}

	private static String createUnboxedValueOf(String mthName, String varType) {
		return "{ ist.meic.pa.BoxingProfiler.incValueUnboxed(\"" + mthName + "\",\"" + varType + "\");" + "$_ = $proceed($$); }";
	}

	public static void incValueBoxed(String mthName, String varType) {
		int temp = boxingResult.get(mthName).get(varType).get("boxed");
		boxingResult.get(mthName).get(varType).put("boxed", ++temp);
	}

	public static void incValueUnboxed(String mthName, String varType) {
		int temp = boxingResult.get(mthName).get(varType).get("unboxed");
		boxingResult.get(mthName).get(varType).put("unboxed", ++temp);
	}

	private static void printResult() {
		for (String entryType : boxingResult.keySet()) {
			TreeMap<String, TreeMap<String, Integer>> entryMethT = boxingResult.get(entryType);

			for (String entryMeth : entryMethT.keySet()) {
				TreeMap<String, Integer> entryVarT = entryMethT.get(entryMeth);

				for (String entryVar : entryVarT.keySet()) {
					if(entryVarT.get(entryVar) != 0)
						System.out.println(entryType + " " + entryVar + " " + entryVarT.get(entryVar) + " " + entryMeth);
				}
			}
		}
	}

}
