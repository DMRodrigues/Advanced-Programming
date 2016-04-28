package ist.meic.pa.GenericFunctions;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class StandardMethodCombination {

	private Map<GFMethod, Method> primary_methods = new HashMap<>();
	private Map<GFMethod, Method> before_methods = new HashMap<>();
	private Map<GFMethod, Method> after_methods = new HashMap<>();

	public void addMethod(GFMethod gfMethod, Method method) {
		primary_methods.put(gfMethod, method);
	}

	public void addBeforeMethod(GFMethod gfMethod, Method method) {
		before_methods.put(gfMethod, method);
	}

	public void addAfterMethod(GFMethod gfMethod, Method method) {
		after_methods.put(gfMethod, method);
	}

	public List<Entry<GFMethod, Method>> getEffectiveMethod(Object[] args) {
		List<Entry<GFMethod, Method>> methods = new ArrayList<>();
		methods.addAll(getApplicableMethods(before_methods, args));
		methods.addAll(getApplicableMethods(primary_methods, args));
		methods.addAll(getApplicableMethods(after_methods, args));
		return methods;
	}

	private List<Entry<GFMethod, Method>> getApplicableMethods(Map<GFMethod, Method> methods, Object[] args) {
		List<Entry<GFMethod, Method>> res = new ArrayList<>();
		for (Entry<GFMethod, Method> m : methods.entrySet()) {

			Class<?>[] paramsTypes = m.getValue().getParameterTypes();
			if (args.length != paramsTypes.length)
				continue;

			boolean match = true, assignable = true;
			for (int i = 0; i < paramsTypes.length; i++) {
				match = match & paramsTypes[i] == args[i].getClass();
				assignable = assignable & paramsTypes[i].isAssignableFrom(args[i].getClass());
				if (!assignable)
					break;
			}
			if (match) {
				res.add(0, m);
				break;
			} else if (assignable) {
				Entry<GFMethod, Method> method = getMostAssignableMethod(res, m);
				res.add(0, method);
			}
		}
		return res.isEmpty() ? res : res.subList(0, 1);
	}

	private Entry<GFMethod, Method> getMostAssignableMethod(List<Entry<GFMethod, Method>> methods, Entry<GFMethod, Method> m) {
		
		if(methods.isEmpty())
			return m;
		
		Class<?>[] currentMethodParamTypes = m.getValue().getParameterTypes();
		Class<?>[] olderMethodParamTypes = methods.get(0).getValue().getParameterTypes();
		
		for(int i = 0; i < currentMethodParamTypes.length; i++){
			if(olderMethodParamTypes[i] != currentMethodParamTypes[i] && olderMethodParamTypes[i].isAssignableFrom(currentMethodParamTypes[i])){
				return m;
			}
		}
		return methods.get(0);
		
		
	}

}
