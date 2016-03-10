package pt.ist.ap.labs.commands;

public class Index extends Command {


	@Override
	public Object execute(Object obj, String... param) {
		
		int idx = Integer.parseInt(param[1]);
		
		Object[] o = (Object[]) obj;
		if(idx >= 0 && idx < o.length){
			System.out.println(o[idx]);
			return o[idx];
		}
		return obj;
		
		
	}


}
