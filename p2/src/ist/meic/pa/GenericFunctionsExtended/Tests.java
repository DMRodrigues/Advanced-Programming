package ist.meic.pa.GenericFunctionsExtended;

import java.util.Arrays;
import java.util.List;

public class Tests {

	public static void t1() {
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

		System.out.println("----------------------TEST 1-----------------------");
		Main.println(add.call(1, 3));
		Main.println(add.call(new Object[] { 1, 2, 3 }, new Object[] { 4, 5, 6 }));
		Main.println(add.call(new Object[] { new Object[] { 1, 2 }, 3 }, new Object[] { new Object[] { 3, 4 }, 5 }));
		Main.println(add.call(new Object[] { 1, 2 }, 3));
	}

	public static void t2() {
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

		System.out.println("----------------------TEST 2-----------------------");
		Main.println(add.call(new Object[] { 1, 2 }, 3));
		Main.println(add.call(1, new Object[][] { { 1, 2 }, { 3, 4 } }));
		Main.println(add.call("12", "34"));
		Main.println(add.call(new Object[] { "123", "4" }, 5));
		Main.println(add.call(new Object[] { 1, 2, 3 }, Arrays.asList(4, 5, 6)));
	}

	public static void t3() {
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
				System.out.printf("The number ", entity);
			}
		});

		System.out.println("----------------------TEST 3-----------------------");
		Main.println(explain.call(123));
		Main.println(explain.call("Hi"));
		Main.println(explain.call(3.14159));
	}

	public static void t4() {
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
		explain.addMethod(new GFMethod() {
			Object call(Long entity) {
				System.out.printf("%s is a long", entity);
				return "";
			}
		});
		// -----------------------------------------------------------------------------------------
		explain.addAfterMethod(new GFMethod() {
			void call(Integer entity) {
				System.out.printf(" INTEGER ", entity);
			}
		});
		explain.addAfterMethod(new GFMethod() {
			void call(Object entity) {
				System.out.printf(" OBJECT ", entity);
			}
		});
		explain.addAfterMethod(new GFMethod() {
			void call(Number entity) {
				System.out.printf(" NUMBER ", entity);
			}
		});
		explain.addAfterMethod(new GFMethod() {
			void call(String entity) {
				System.out.printf(" STRING ", entity);
			}
		});
		explain.addAfterMethod(new GFMethod() {
			void call(Long entity) {
				System.out.printf(" LONG ", entity);
			}
		});
		// -----------------------------------------------------------------------------------------
		explain.addBeforeMethod(new GFMethod() {
			void call(Number entity) {
				System.out.printf("The number ");
			}
		});
		// -----------------------------------------------------------------------------------------
		explain.addAroundMethod(new GFMethod(true) {
			void call(Integer entity) {
				System.out.printf("Around Integer |");
			}
		});
		explain.addAroundMethod(new GFMethod(true) {
			void call(Object entity) {
				System.out.printf("Around Object |");
			}
		});
		explain.addAroundMethod(new GFMethod(true) {
			void call(Object[] entity) {
				System.out.printf("Around Object[] |");
			}
		});
		explain.addAroundMethod(new GFMethod(true) {
			void call(Number entity) {
				System.out.printf("Around Number |");
			}
		});
		explain.addAroundMethod(new GFMethod(true) {
			void call(String entity) {
				System.out.printf("Around String |");
			}
		});
		explain.addAroundMethod(new GFMethod(true) {
			void call(Double entity) {
				System.out.printf("Around Double |");
			}
		});
		explain.addAroundMethod(new GFMethod(true) {
			void call(Float entity) {
				System.out.printf("Around Float |");
			}
		});
		explain.addAroundMethod(new GFMethod(true) {
			void call(Character entity) {
				System.out.printf("Around Character |");
			}
		});

		System.out.println("----------------------TEST 4-----------------------");
		Main.println(explain.call(123));
		Main.println(explain.call("Hi"));
		Main.println(explain.call(3.14159));
		Main.println(explain.call(new Character('a')));
		Main.println(explain.call(new Long(1)));
	}

	public static void t5() {
		final GenericFunction explain = new GenericFunction("explain");

		explain.addMethod(new GFMethod() {
			Object call(Object entity) {
				System.out.printf("|primary-object|");
				return "";
			}
		});
		explain.addMethod(new GFMethod() {
			Object call(Integer entity) {
				System.out.printf("|primary-integer|");
				return "";
			}
		});
		explain.addMethod(new GFMethod() {
			Object call(Number entity) {
				System.out.printf("|primary-number|");
				return "";
			}
		});
		explain.addMethod(new GFMethod() {
			Object call(String entity) {
				System.out.printf("|primary-string|");
				return "";
			}
		});
		explain.addMethod(new GFMethod() {
			Object call(Long entity) {
				System.out.printf("|primary-long|");
				return "";
			}
		});
		// -----------------------------------------------------------------------------------------
		explain.addAfterMethod(new GFMethod() {
			void call(Object entity) {
				System.out.printf("|after-object|");
			}
		});
		explain.addAfterMethod(new GFMethod() {
			void call(Integer entity) {
				System.out.printf("|after-integer|");
			}
		});
		explain.addAfterMethod(new GFMethod() {
			void call(String entity) {
				System.out.printf("|after-string|");
			}
		});
		// -----------------------------------------------------------------------------------------
		explain.addBeforeMethod(new GFMethod() {
			void call(Number entity) {
				System.out.printf("|before-number|");
			}
		});
		explain.addBeforeMethod(new GFMethod() {
			void call(String entity) {
				System.out.printf("|before-string|");
			}
		});
		explain.addBeforeMethod(new GFMethod() {
			void call(Object entity) {
				System.out.printf("|before-object|");
			}
		});
		explain.addBeforeMethod(new GFMethod() {
			void call(Integer entity) {
				System.out.printf("|before-integer|");
			}
		});
		// -----------------------------------------------------------------------------------------
		explain.addAroundMethod(new GFMethod() {
			void call(Integer entity) {
				System.out.printf("|around-integer|");
			}
		});
		explain.addAroundMethod(new GFMethod() {
			void call(Object entity) {
				System.out.printf("|around-object|");
			}
		});
		explain.addAroundMethod(new GFMethod(true) {
			void call(Number entity) {
				System.out.printf("|around-number|");
			}
		});

		System.out.println("----------------------TEST 5-----------------------");
		Main.println(explain.call(123));
		Main.println(explain.call(new Long(1)));
	}
	
	public static void t6() {
		final GenericFunction explain = new GenericFunction("explain");
		
		explain.addMethod(new GFMethod() {
			Object call(Object entity) {
				System.out.printf("|primary-object|");
				return "";
			}
		});
		explain.addMethod(new GFMethod(true) {
			Object call(Integer entity) {
				System.out.printf("|primary-integer|");
				return "";
			}
		});
		explain.addMethod(new GFMethod() {
			Object call(Number entity) {
				System.out.printf("|primary-number|");
				return "";
			}
		});
		explain.addMethod(new GFMethod() {
			Object call(String entity) {
				System.out.printf("|primary-string|");
				return "";
			}
		});
		explain.addAfterMethod(new GFMethod() {
			void call(Object entity) {
				System.out.printf("|after-object|");
			}
		});
		explain.addAfterMethod(new GFMethod() {
			void call(Integer entity) {
				System.out.printf("|after-integer|");
			}
		});
		explain.addBeforeMethod(new GFMethod() {
			void call(Number entity) {
				System.out.printf("|before-number|");
			}
		});
		explain.addBeforeMethod(new GFMethod() {
			void call(String entity) {
				System.out.printf("|before-string|");
			}
		});
		explain.addBeforeMethod(new GFMethod() {
			void call(Object entity) {
				System.out.printf("|before-object|");
			}
		});
		explain.addBeforeMethod(new GFMethod() {
			void call(Integer entity) {
				System.out.printf("|before-integer|");
			}
		});

		System.out.println("----------------------TEST 6-----------------------");
		Main.println(explain.call(123));
		Main.println(explain.call(new Long(1)));
	}
	public static void t7() {
		final GenericFunction explain = new GenericFunction("explain");
		
		explain.addMethod(new GFMethod() {
			Object call(Object entity) {
				System.out.printf("|primary-object|");
				return "OBJECT";
			}
		});
		explain.addMethod(new GFMethod(true) {
			Object call(Integer entity) {
				System.out.printf("|primary-integer|");
				return "INTEGER";
			}
		});
		explain.addMethod(new GFMethod() {
			Object call(Number entity) {
				System.out.printf("|primary-number|");
				return "NUMBER";
			}
		});
		explain.addMethod(new GFMethod() {
			Object call(String entity) {
				System.out.printf("|primary-string|");
				return "";
			}
		});
		explain.addAfterMethod(new GFMethod() {
			void call(Object entity) {
				System.out.printf("|after-object|");
			}
		});
		explain.addAfterMethod(new GFMethod() {
			void call(Integer entity) {
				System.out.printf("|after-integer|");
			}
		});
		explain.addBeforeMethod(new GFMethod() {
			void call(Number entity) {
				System.out.printf("|before-number|");
			}
		});
		explain.addBeforeMethod(new GFMethod() {
			void call(String entity) {
				System.out.printf("|before-string|");
			}
		});
		explain.addBeforeMethod(new GFMethod() {
			void call(Object entity) {
				System.out.printf("|before-object|");
			}
		});
		explain.addBeforeMethod(new GFMethod() {
			void call(Integer entity) {
				System.out.printf("|before-integer|");
			}
		});

		System.out.println("----------------------TEST 7-----------------------");
		Main.println(explain.call(123));
		Main.println(explain.call(new Long(1)));
	}
	
	public static void t8(){
		final GenericFunction explain = new GenericFunction("explain");
		explain.addBeforeMethod(new GFMethod(true) {
			void call(A entity) {
				System.out.printf("|before-A|");
			}
		});
		explain.addBeforeMethod(new GFMethod(true) {
			void call(C entity) {
				System.out.printf("|before-C|");
			}
		});
		explain.addBeforeMethod(new GFMethod(true) {
			void call(C entity) {
				System.out.printf("|before-D|");
			}
		});
		explain.addBeforeMethod(new GFMethod(true) {
			void call(B entity) {
				System.out.printf("|before-B|");
			}
		});
		explain.addAfterMethod(new GFMethod(true) {
			void call(A entity) {
				System.out.printf("|after-A|");
			}
		});
		explain.addAfterMethod(new GFMethod(true) {
			void call(C entity) {
				System.out.printf("|after-C|");
			}
		});
		explain.addAfterMethod(new GFMethod(true) {
			void call(C entity) {
				System.out.printf("|after-D|");
			}
		});
		explain.addAfterMethod(new GFMethod(true) {
			void call(B entity) {
				System.out.printf("|after-B|");
			}
		});
		explain.addMethod(new GFMethod(true) {
			Object call(A entity) {
				System.out.printf("|A|");
				return "A";
			}
		});
		explain.addMethod(new GFMethod(true) {
			Object call(B entity) {
				System.out.printf("|B|");
				return "B";
			}
		});
		explain.addMethod(new GFMethod(true) {
			Object call(C entity) {
				System.out.printf("|C|");
				return "C";
			}
		});
		explain.addMethod(new GFMethod(true) {
			Object call(C entity) {
				System.out.printf("|D|");
				return "D";
			}
		});
		System.out.println("----------------------TEST 8-----------------------");
		Main.println(explain.call(new A()));
		Main.println(explain.call(new B()));
	}

}
