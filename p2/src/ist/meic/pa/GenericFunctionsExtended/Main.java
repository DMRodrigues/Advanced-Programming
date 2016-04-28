package ist.meic.pa.GenericFunctionsExtended;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
	/*
		Tests test = new Tests();
		test.t1();
	
		System.out.println("-------------------------------");
		Test test = new Tests();
		test.t2();
	
		System.out.println("-------------------------------");
		Test test = new Tests();
		test.t3();
	*/
	System.out.println("-------------------------------");
	Tests test = new Tests();
	test.t4();

    }

    public static void println(Object obj) {
	if (obj instanceof Object[]) {
	    System.out.println(Arrays.deepToString((Object[]) obj));
	} else {
	    System.out.println(obj);
	}
    }

}
