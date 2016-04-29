package ist.meic.pa.GenericFunctionsExtended;

import java.lang.reflect.Method;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

public class StandardMethodCombination {

	private Comparator<Entry<GFMethod, Method>> methodComparator = new Comparator<Entry<GFMethod, Method>>() {
		@Override
		public int compare(Entry<GFMethod, Method> m1, Entry<GFMethod, Method> m2) {

			Class<?>[] m1ParamTypes = m1.getValue().getParameterTypes();
			Class<?>[] m2ParamTypes = m2.getValue().getParameterTypes();

			// ASSUMIR METODOS COM O MESMO NUMERO DE PARAMETROS??
			for (int i = 0; i < m1ParamTypes.length; i++) {
				if (m1ParamTypes[i] != m2ParamTypes[i] && m2ParamTypes[i].isAssignableFrom(m1ParamTypes[i])) {
					return -1;
				}
			}
			return 1;
		}
	};

	private List<Entry<GFMethod, Method>> aroundMethods = new ArrayList<Entry<GFMethod, Method>>();
	private List<Entry<GFMethod, Method>> beforeMethods = new ArrayList<Entry<GFMethod, Method>>();
	private List<Entry<GFMethod, Method>> primaryMethods = new ArrayList<Entry<GFMethod, Method>>();
	private List<Entry<GFMethod, Method>> afterMethods = new ArrayList<Entry<GFMethod, Method>>();

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

		methods.addAll(getNextMethodsCall(this.getApplicableMethods(this.aroundMethods, args)));
		if (!methods.isEmpty())
			return methods;
		
		methods.addAll(this.getApplicableMethods(this.beforeMethods, args));
		methods.addAll(getNextMethodsCall(this.getApplicableMethods(this.primaryMethods, args)));

		// most-specific-last order
		List<Entry<GFMethod, Method>> afterMethods = this.getApplicableMethods(this.afterMethods, args);
		Collections.reverse(afterMethods);
		methods.addAll(afterMethods);

		return methods;
	}

	private List<Entry<GFMethod, Method>> getNextMethodsCall(List<Entry<GFMethod, Method>> methods) {
		List<Entry<GFMethod, Method>> res = new ArrayList<>();
		for (Entry<GFMethod, Method> m : methods) {
			res.add(m);
			if (!m.getKey().callNextMethod())
				break;
		}
		return res;
	}

	private List<Entry<GFMethod, Method>> getApplicableMethods(List<Entry<GFMethod, Method>> methods, Object[] args) {
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
				res.add(m);
			}
		}

		res.sort(methodComparator);
		return res;
	}
}