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

	private static TreeMap<String, TreeMap<String, TreeMap<String, Integer>>> res = new TreeMap<String, TreeMap<String, TreeMap<String, Integer>>>();
	private static final String[] typeValue = { "intValue", "longValue", "floatValue", "charValue", "doubleValue", "booleanValue", "shortValue", "byteValue" };
	private static CtClass ctClass = null;
	private static String[] args = null;
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
			
			// tratar classpath
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

				m.instrument(new ExprEditor() {
					public void edit(MethodCall mc) throws CannotCompileException {

						// boxed operations
						if (mc.getMethodName().equals("valueOf")) {// && getTypeVar(mc.getSignature()) != null) {

							checkTreeMapValues(removePackage(m.getLongName()), getTypeVar(mc.getSignature()));

							res.get(removePackage(m.getLongName())).get(getTypeVar(mc.getSignature())).put("boxed", 0);
							mc.replace(createBoxedValueOf(removePackage(m.getLongName()), getTypeVar(mc.getSignature())));
						}

						// unboxed operations
						else if (Arrays.asList(typeValue).contains(mc.getMethodName())) {

							checkTreeMapValues(removePackage(m.getLongName()), getTypeVar(mc.getMethodName()));

							res.get(removePackage(m.getLongName())).get(getTypeVar(mc.getMethodName())).put("unboxed", 0);
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

	private static String removePackage(String t) {
		// ist.meic.pa.[12] -> 10
		return t.substring(12, t.length());
	}

	private static String createBoxedValueOf(String mname, String msig) {
		return "{ ist.meic.pa.BoxingProfiler.incValueBoxed(\"" + mname + "\",\"" + msig + "\");" + "$_ = $proceed($$); }";
	}

	private static String createUnboxedValueOf(String mname, String msig) {
		return "{ ist.meic.pa.BoxingProfiler.incValueUnboxed(\"" + mname + "\",\"" + msig + "\");" + "$_ = $proceed($$); }";
	}

	public static void incValueBoxed(String n, String t) {
		int temp = res.get(n).get(t).get("boxed");
		res.get(n).get(t).put("boxed", ++temp);
	}

	public static void incValueUnboxed(String n, String t) {
		int temp = res.get(n).get(t).get("unboxed");
		res.get(n).get(t).put("unboxed", ++temp);
	}

	private static void printResult() {
		for (String entryType : res.keySet()) {
			TreeMap<String, TreeMap<String, Integer>> entryMethT = res.get(entryType);

			for (String entryMeth : entryMethT.keySet()) {
				TreeMap<String, Integer> entryVarT = entryMethT.get(entryMeth);

				for (String entryVar : entryVarT.keySet())
					System.out.println(entryType + " " + entryVar + " " + entryVarT.get(entryVar) + " " + entryMeth);
			}
		}
	}

	private static void checkTreeMapValues(String mthName, String varType) {
		// se ainda n vimos metodo criar
		if (res.get(mthName) == null)
			res.put(mthName, new TreeMap<String, TreeMap<String, Integer>>());
		if (res.get(mthName).get(varType) == null)
			res.get(mthName).put(varType, new TreeMap<String, Integer>());
	}

}
