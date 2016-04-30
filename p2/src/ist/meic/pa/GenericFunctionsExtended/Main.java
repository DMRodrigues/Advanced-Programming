package ist.meic.pa.GenericFunctionsExtended;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

	try {
	    Tests.t1();
	} catch (IllegalArgumentException e) {
	    System.err.println(e);
	}

	Tests.t2();
	Tests.t3();
	Tests.t4();
	Tests.t5();

	/*
			try {
				Tests2.t1();
			} catch (IllegalArgumentException e) {
				System.err.println(e);
			} finally {
				Tests2.t2();
				Tests2.t3();
				Tests2.t4();
				Tests2.t5();
			}*/

    }

    public static void println(Object obj) {
	if (obj instanceof Object[]) {
	    System.out.println(Arrays.deepToString((Object[]) obj));
	} else {
	    System.out.println(obj);
	}
    }

}
