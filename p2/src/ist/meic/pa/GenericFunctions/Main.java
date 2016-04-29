package ist.meic.pa.GenericFunctions;

import java.util.Arrays;

public class Main {

	public static void main(String[] args) {
		try {
			Tests.t1();
		} catch (IllegalArgumentException e) {
			System.err.println(e);
		} finally {
			Tests.t2();
			Tests.t3();
			Tests.t4();

		}
	}

	public static void println(Object obj) {
		if (obj instanceof Object[]) {
			System.out.println(Arrays.deepToString((Object[]) obj));
		} else {
			System.out.println(obj);
		}
	}

}
