package ist.meic.pa.GenericFunctionsExtended;

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
			Tests.t5();
			Tests.t6();
			Tests.t7();
			Tests.t8();
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
