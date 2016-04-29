package ist.meic.pa.GenericFunctionsExtended;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

	Tests test1 = new Tests();
	test1.t1();

	System.out.println("-------------------------------");
	Tests test2 = new Tests();
	test2.t2();

	System.out.println("-------------------------------");
	Tests test3 = new Tests();
	test3.t3();

	System.out.println("-------------------------------");
	Tests test4 = new Tests();
	test4.t4();

    }

    public static void println(Object obj) {
	if (obj instanceof Object[]) {
	    System.out.println(Arrays.deepToString((Object[]) obj));
	} else {
	    System.out.println(obj);
	}
    }

}
