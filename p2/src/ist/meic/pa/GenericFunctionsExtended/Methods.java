package ist.meic.pa.GenericFunctionsExtended;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Methods {

    private Map<GFMethod, Method> primaryMethods = new HashMap<>();
    private Map<GFMethod, Method> beforeMethods = new HashMap<>();
    private Map<GFMethod, Method> afterMethods = new HashMap<>();
    private Map<GFMethod, Method> aroundMethods = new HashMap<>();

    public void addMethod(GFMethod gfMethod, Method method) {
	this.primaryMethods.put(gfMethod, method);
    }

    public void addBeforeMethod(GFMethod gfMethod, Method method) {
	this.beforeMethods.put(gfMethod, method);
    }

    public void addAfterMethod(GFMethod gfMethod, Method method) {
	this.afterMethods.put(gfMethod, method);
    }

    public void addAroundMethod(GFMethod gfMethod, Method method) {
	this.aroundMethods.put(gfMethod, method);
    }

    public List<Entry<GFMethod, Method>> getEffectiveMethod(Object[] args) {
	List<Entry<GFMethod, Method>> methods = new ArrayList<>();
	methods.addAll(this.getApplicableMethods(this.aroundMethods, args));
	methods.addAll(this.getApplicableMethods(this.beforeMethods, args));
	methods.addAll(this.getApplicableMethods(this.primaryMethods, args));
	methods.addAll(this.getApplicableMethods(this.afterMethods, args));
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
	    } else if (assignable) {
		int lastPosition = res.isEmpty() ? 0 : res.size() - 1;
		res.add(lastPosition, m);
	    }
	}
	return res.isEmpty() ? res : res.subList(0, 1);
    }
}
