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

			// tratar classpath ou deixar assim ?
			ctClass = ClassPool.getDefault().get("ist.meic.pa." + args[0]);
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

				boxingResult.put(removePackage(m.getLongName()), new TreeMap<String, TreeMap<String, Integer>>());

				m.instrument(new ExprEditor() {
					public void edit(MethodCall mc) throws CannotCompileException {

						// boxed operations
						if (mc.getMethodName().equals("valueOf")) {// && getTypeVar(mc.getSignature()) != null) {

							if (boxingResult.get(removePackage(m.getLongName())).get(getTypeVar(mc.getSignature())) == null)
								boxingResult.get(removePackage(m.getLongName())).put(getTypeVar(mc.getSignature()), new TreeMap<String, Integer>());

							boxingResult.get(removePackage(m.getLongName())).get(getTypeVar(mc.getSignature())).put("boxed", 0);
							mc.replace(createBoxedValueOf(removePackage(m.getLongName()), getTypeVar(mc.getSignature())));
						}

						// unboxed operations
						else if (Arrays.asList(typeValue).contains(mc.getMethodName())) {

							if (boxingResult.get(removePackage(m.getLongName())).get(getTypeVar(mc.getMethodName())) == null)
								boxingResult.get(removePackage(m.getLongName())).put(getTypeVar(mc.getMethodName()), new TreeMap<String, Integer>());

							boxingResult.get(removePackage(m.getLongName())).get(getTypeVar(mc.getMethodName())).put("unboxed", 0);
							mc.replace(createUnboxedValueOf(removePackage(m.getLongName()), getTypeVar(mc.getMethodName())));
						}
					}
				});

			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// to improve
	private static String getTypeVar(String t) {
		if (t.contains("Integer") || t.contains(typeValue[0]))
			return new String("java.lang.Integer");
		if (t.contains("Long") || t.contains(typeValue[1]))
			return new String("java.lang.Long");
		if (t.contains("Float") || t.contains(typeValue[2]))
			return new String("java.lang.Float");
		if (t.contains("Character") || t.contains(typeValue[3]))
			return new String("java.lang.Character");
		if (t.contains("Double") || t.contains(typeValue[4]))
			return new String("java.lang.Double");
		if (t.contains("Boolean") || t.contains(typeValue[5]))
			return new String("java.lang.Boolean");
		if (t.contains("Short") || t.contains(typeValue[6]))
			return new String("java.lang.Short");
		if (t.contains("Byte") || t.contains(typeValue[7]))
			return new String("java.lang.Byte");
		return null; // entao n interessa
	}

	private static String removePackage(String s) {
		// ist.meic.pa.[12] -> conta a partir daqui
		return s.substring(12, s.length());
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

				for (String entryVar : entryVarT.keySet())
					System.out.println(entryType + " " + entryVar + " " + entryVarT.get(entryVar) + " " + entryMeth);
			}
		}
	}

}
