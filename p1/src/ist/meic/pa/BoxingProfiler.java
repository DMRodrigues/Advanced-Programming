package ist.meic.pa;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

public class BoxingProfiler {

	public static void main(String[] args) {

		try {
			ClassPool pool = ClassPool.getDefault();
			CtClass ctClass = pool.get(args[0]);
			CtMethod ctMethod = ctClass.getDeclaredMethod("main");
			String[] restArgs = new String[args.length - 1];
			System.arraycopy(args, 1, restArgs, 0, restArgs.length);

			doSomething(ctClass, ctMethod);

			Class<?> rtClass = ctClass.toClass();
			Method main = rtClass.getMethod("main", args.getClass());
			main.invoke(null, new Object[] { restArgs });

		} catch (NotFoundException | CannotCompileException | NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {

			// TODO Auto-generated catch block
			System.err.println(e); 
		}

	}

	private static void doSomething(CtClass ctClass, CtMethod ctMethod) {
		try {

			String name = ctMethod.getName();
			ctMethod.setName(name + "$original");
			ctMethod = CtNewMethod.copy(ctMethod, name, ctClass, null);
			ctMethod.setBody(
			"{" + 
					"System.out.println(\"BEFORE\");" +
					" " + name + "$original($$);"+  
					"System.out.println(\"AFTER\");" +
			"}");
			ctClass.addMethod(ctMethod);
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			System.err.println(e);
		}

	}
}
