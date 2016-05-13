package ist.meic.pa.GenericFunctionsExtended;

import java.util.Arrays;

public class Main {

	public static void main(String[] args) {
		final GenericFunction explain = new GenericFunction("explain");
		
		explain.addAroundMethod(new GFMethod(true) {
			Object call(Integer entity) {
				System.out.printf("[around-integer]", entity);
				return "";
			}
		});
		explain.addAroundMethod(new GFMethod(true) {
			Object call(Number entity) {
				System.out.printf("[around-number]", entity);
				return "";
			}
		});
		explain.addMethod(new GFMethod(false) {
			Object call(Integer entity) {
				System.out.printf("[integer]", entity);
				return "";
			}
		});
		explain.addMethod(new GFMethod(true) {
			Object call(Number entity) {
				System.out.printf("[number]", entity);
				return "";
			}
		});
		explain.addBeforeMethod(new GFMethod() {
			Object call(Integer entity) {
				System.out.printf("[before-integer]", entity);
				return "";
			}
		});
		explain.addAfterMethod(new GFMethod() {
			Object call(Integer entity) {
				System.out.printf("[after-integer]", entity);
				return "";
			}
		});
		
		println(explain.call(123));
	}

	public static void println(Object obj) {
		if (obj instanceof Object[]) {
			System.out.println(Arrays.deepToString((Object[]) obj));
		} else {
			System.out.println(obj);
		}
	}
}
