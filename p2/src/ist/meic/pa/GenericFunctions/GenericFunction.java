package ist.meic.pa.GenericFunctions;

import java.lang.reflect.Method;

public class GenericFunction {

	private final String name;

	public GenericFunction(String name) {
		this.name = name;
	}

	public void addMethod(GFMethod method) {

		try {
			Class<GFMethod> c = (Class<GFMethod>) method.getClass();
			System.out.println(c);
			Method[] m = c.getDeclaredMethods();
			if(m.length > 0 && m[0].getName().equals("call"))
				System.out.println(m[0]);
			System.out.println("------------------------------------");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ClassPool pool = ClassPool.getDefault();
		// CtClass ctClass;
		// try {
		// ctClass = pool.get("ist.meic.pa.GenericFunctions.GFMethod");
		// String name = "call";
		// CtMethod ctMethod = ctClass.getDeclaredMethod(name);
		// ctMethod.setName(name + "$original");
		// ctMethod = CtNewMethod.copy(ctMethod, name, ctClass, null);
		//// ctMethod.setBody("{" +
		//// " Object result = cachedResults.get($1);" +
		//// " if (result == null) {" +
		//// " result = " + name + "$original($$);" +
		//// " cachedResults.put($1, result);" +
		//// " }" +
		//// " return ($r)result;" +
		//// "}");
		// ctClass.addMethod(ctMethod);
		// } catch (NotFoundException | CannotCompileException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	public Object call(Object... args) {
		Object result = null;
		return result;
	}

}
