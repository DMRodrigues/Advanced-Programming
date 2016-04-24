package ist.meic.pa.GenericFunctionsExtended;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

	Test test1 = new Test() {
	    public void run() {
		final GenericFunction add = new GenericFunction("add");
		add.addMethod(new GFMethod() {
		    Object call(Integer a, Integer b) {
			return a + b;
		    }
		});
		add.addMethod(new GFMethod() {
		    Object call(Object[] a, Object[] b) {
			Object[] r = new Object[a.length];
			for (int i = 0; i < a.length; i++) {
			    r[i] = add.call(a[i], b[i]);
			}
			return r;
		    }
		});
		add.addAroundMethod(new GFMethod() {
		    void call(Integer a, Integer b) {
			System.out.printf("Calculating SUM of %d + %d = ", a, b);
		    }
		});

		Main.println(add.call(1, 3));
	    }
	};
	test1.run();

	System.out.println("-------------------------------");

	Test test2 = new Test() {
	    public void run() {
		final GenericFunction add = new GenericFunction("add");

		add.addMethod(new GFMethod() {
		    Object call(Integer a, Integer b) {
			return a + b;
		    }
		});
		add.addMethod(new GFMethod() {
		    Object call(Object[] a, Object[] b) {
			Object[] r = new Object[a.length];
			for (int i = 0; i < a.length; i++) {
			    r[i] = add.call(a[i], b[i]);
			}
			return r;
		    }
		});
		add.addMethod(new GFMethod() {
		    Object call(Object[] a, Object b) {
			Object[] ba = new Object[a.length];
			Arrays.fill(ba, b);
			return add.call(a, ba);
		    }
		});
		add.addMethod(new GFMethod() {
		    Object call(Object a, Object b[]) {
			Object[] aa = new Object[b.length];
			Arrays.fill(aa, a);
			return add.call(aa, b);
		    }
		});
		add.addMethod(new GFMethod() {
		    Object call(String a, Object b) {
			return add.call(Integer.decode(a), b);
		    }
		});
		add.addMethod(new GFMethod() {
		    Object call(Object a, String b) {
			return add.call(a, Integer.decode(b));
		    }
		});
		add.addMethod(new GFMethod() {
		    Object call(Object[] a, List b) {
			return add.call(a, b.toArray());
		    }
		});

		Main.println(add.call(new Object[] { 1, 2 }, 3));
		Main.println(add.call("12", "34"));
		Main.println(add.call(new Object[] { "123", "4" }, 5));
	    }
	};
	test2.run();

	System.out.println("-------------------------------");

	Test test3 = new Test() {
	    public void run() {
		final GenericFunction explain = new GenericFunction("explain");
		explain.addMethod(new GFMethod() {
		    Object call(Integer entity) {
			System.out.printf("%s is a integer", entity);
			return "";
		    }
		});
		explain.addMethod(new GFMethod() {
		    Object call(Number entity) {
			System.out.printf("%s is a number", entity);
			return "";
		    }
		});
		explain.addMethod(new GFMethod() {
		    Object call(String entity) {
			System.out.printf("%s is a string", entity);
			return "";
		    }
		});
		explain.addAfterMethod(new GFMethod() {
		    void call(Integer entity) {
			System.out.printf(" (in hexadecimal, is %x)", entity);
		    }
		});
		explain.addBeforeMethod(new GFMethod() {
		    void call(Number entity) {
			System.out.printf("The number ");
		    }
		});
		explain.addAroundMethod(new GFMethod() {
		    void call(Number entity) {
			System.out.printf("Number around => ");
		    }
		});
		explain.addAroundMethod(new GFMethod() {
		    void call(String entity) {
			System.out.printf("String around => ");
		    }
		});

		Main.println(explain.call(123));
		Main.println(explain.call("Hi"));
		Main.println(explain.call(3.14159));
	    }
	};
	test3.run();
    }

    public static void println(Object obj) {
	if (obj instanceof Object[]) {
	    System.out.println(Arrays.deepToString((Object[]) obj));
	} else {
	    System.out.println(obj);
	}
    }

    interface Test {
	void run();
    }

}
