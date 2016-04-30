package ist.meic.pa.GenericFunctionsExtended;

import java.lang.reflect.Method;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

public class StandardMethodCombination {

    private Comparator<Entry<GFMethod, Method>> methodComparator = new Comparator<Entry<GFMethod, Method>>() {
	@Override
	public int compare(Entry<GFMethod, Method> m1, Entry<GFMethod, Method> m2) {

	    if (m1.getValue().getParameterTypes().equals(m2.getValue().getParameterTypes()))
		return 0; // should never happen

	    Class<?>[] m1Params = m1.getValue().getParameterTypes();
	    Class<?>[] m2Params = m2.getValue().getParameterTypes();

	    for (int i = 0; i < m1Params.length; i++) {
		if (!m1Params[i].isAssignableFrom(m2Params[i]))
		    return -1;
	    }

	    return 1;
	}
    };

    private Set<Entry<GFMethod, Method>> aroundMethods = new TreeSet<Entry<GFMethod, Method>>(methodComparator);
    private Set<Entry<GFMethod, Method>> primaryMethods = new TreeSet<Entry<GFMethod, Method>>(methodComparator);
    private Set<Entry<GFMethod, Method>> beforeMethods = new TreeSet<Entry<GFMethod, Method>>(methodComparator);
    private Set<Entry<GFMethod, Method>> afterMethods = new TreeSet<Entry<GFMethod, Method>>(methodComparator);

    public void addMethod(GFMethod gfMethod, Method method) {
	this.primaryMethods.add(new SimpleEntry<GFMethod, Method>(gfMethod, method));
    }

    public void addBeforeMethod(GFMethod gfMethod, Method method) {
	this.beforeMethods.add(new SimpleEntry<GFMethod, Method>(gfMethod, method));
    }

    public void addAfterMethod(GFMethod gfMethod, Method method) {
	this.afterMethods.add(new SimpleEntry<GFMethod, Method>(gfMethod, method));
    }

    public void addAroundMethod(GFMethod gfMethod, Method method) {
	this.aroundMethods.add(new SimpleEntry<GFMethod, Method>(gfMethod, method));
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

    private List<Entry<GFMethod, Method>> getApplicableMethods(Set<Entry<GFMethod, Method>> methods, Object[] args,
	    boolean isAll) {

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
		    if (m.getKey().callNextMethod())
			continue;
		    else
			break;
		}
	    }
	}

	return res;
    }

}