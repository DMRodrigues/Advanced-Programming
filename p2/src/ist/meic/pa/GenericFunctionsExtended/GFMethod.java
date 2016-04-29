package ist.meic.pa.GenericFunctionsExtended;

import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

public class GFMethod {

    private boolean callNextMethod;

    public boolean isCallNextMethod() {
        return this.callNextMethod;
    }

    private Comparator<Map.Entry<GFMethod, Method>> methodComparator = new Comparator<Map.Entry<GFMethod, Method>>() {
	@Override
	public int compare(Entry<GFMethod, Method> m1, Entry<GFMethod, Method> m2) {
	    if (m1.getValue().getParameterTypes().equals(m2.getValue().getParameterTypes()))
		return 0; // should never happen

	    for (Class<?> m1Param : m1.getValue().getParameterTypes()) {
		for (Class<?> m2Param : m2.getValue().getParameterTypes()) {

		    if (!m1Param.isAssignableFrom(m2Param))
			return -1;
		}
	    }
	    return 1;
	}
    };

    private SortedSet<Map.Entry<GFMethod, Method>> aroundMethods = new TreeSet<Map.Entry<GFMethod, Method>>(methodComparator);
    private SortedSet<Map.Entry<GFMethod, Method>> primaryMethods = new TreeSet<Map.Entry<GFMethod, Method>>(methodComparator);
    private SortedSet<Map.Entry<GFMethod, Method>> beforeMethods = new TreeSet<Map.Entry<GFMethod, Method>>(methodComparator);
    private SortedSet<Map.Entry<GFMethod, Method>> afterMethods = new TreeSet<Map.Entry<GFMethod, Method>>(methodComparator);

    public GFMethod() {
	super();
	this.callNextMethod = false;
    }

    public GFMethod(boolean call_next_method) {
	super();
	this.callNextMethod = call_next_method;
    }

    public void addMethod(GFMethod gfMethod, Method method) {
	this.primaryMethods.add(new AbstractMap.SimpleEntry<GFMethod, Method>(gfMethod, method));
    }

    public void addBeforeMethod(GFMethod gfMethod, Method method) {
	this.beforeMethods.add(new AbstractMap.SimpleEntry<GFMethod, Method>(gfMethod, method));
    }

    public void addAfterMethod(GFMethod gfMethod, Method method) {
	this.afterMethods.add(new AbstractMap.SimpleEntry<GFMethod, Method>(gfMethod, method));
    }

    public void addAroundMethod(GFMethod gfMethod, Method method) {
	this.aroundMethods.add(new AbstractMap.SimpleEntry<GFMethod, Method>(gfMethod, method));
    }

    public List<Entry<GFMethod, Method>> getEffectiveMethod(Object[] args) {
	List<Entry<GFMethod, Method>> methods = new ArrayList<>();
	
	methods.addAll(this.getApplicableMethods(this.aroundMethods, args, false));
	methods.addAll(this.getApplicableMethods(this.beforeMethods, args, true));
	methods.addAll(this.getApplicableMethods(this.primaryMethods, args, false));

	// most-specific-last order
	List<Entry<GFMethod, Method>> afterMethods = this.getApplicableMethods(this.afterMethods, args, true);
	Collections.reverse(afterMethods);
	methods.addAll(afterMethods);

	return methods;
    }

    private List<Entry<GFMethod, Method>> getApplicableMethods(SortedSet<Entry<GFMethod, Method>> methods, Object[] args, boolean isAll) {
	List<Entry<GFMethod, Method>> res = new ArrayList<>();

	for (Entry<GFMethod, Method> m : methods) {

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

	    if (match || assignable) {
		res.add(res.size(), m); // preserve order
		
		if (isAll)
		    continue;
		else {
		    if (m.getKey().isCallNextMethod())
			continue;
		    else
			break;
		}
	    }
	}

	return res;
    }

}
