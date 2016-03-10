package pt.ist.ap.lab;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandGeneric implements Command {

    @Override
    public void execute(Shell obj, String[] input) throws IllegalAccessException, IllegalArgumentException,
	    InvocationTargetException, ClassNotFoundException, NoSuchMethodException, SecurityException {

	StringBuilder strBuilder = new StringBuilder();
	for (int i = 0; i < input.length; i++)
	    strBuilder.append(input[i] + " ");
	System.out.println("Trying generic command: " + strBuilder.toString());

	// neste caso é preciso dividir a string input
	String params[] = Arrays.copyOfRange(input, 1, input.length);

	/* Maneira mais rapida/eficiente de obter o metodo 
	Class[] argsType = new Class[params.length / 2];
	Object[] argsValue = new Object[params.length / 2];
	for (int a = 0, b = 0, c = 0; a < params.length; a++) {
	    if (a % 2 == 0) {
		argsType[b++] = Class.forName(params[a]);
	    } else {
		argsValue[c++] = params[a];
	    }
	}
	Method t;
	if (input.length == 1) {
	    t = obj.getObjectClass().getClass().getMethod(input[0]);
	    obj.setObjectClass(t.invoke(obj.getObjectClass()));
	} else {
	    t = obj.getObjectClass().getClass().getMethod(input[0], argsType);
	    obj.setObjectClass(t.invoke(obj.getObjectClass(), (Object[]) argsValue));
	}*/

	for (Method m : obj.getObjectClass().getClass().getMethods()) {
	    if (m.getName().equals(input[0])) {

		// se n levar parametros, podia comparar params
		if (input.length == 1) {
		    obj.setObjectClass(m.invoke(obj.getObjectClass()));
		}
		else {
		    // obter tipo dos parametros que o metodo aceita
		    Class<?>[] theTypes = m.getParameterTypes();

		    // testar se levam o msm nr parametros SIMPLES
		    if (theTypes.length == params.length) {

			// executar copia dos parametros
			Object[] finalParams = new Object[params.length];

			for (int i = 0; i < params.length; i++) {
			    finalParams[i] = params[i];
			}

			if (theTypes[0].isArray()) { // se ja for array executar
			    obj.setObjectClass(m.invoke(obj.getObjectClass(), (Object) finalParams));
			}
			else { // se nao converter para array de forma a poder executar/invoke
			    obj.setObjectClass(m.invoke(obj.getObjectClass(), (Object[]) finalParams));
			}
			break; // para pois ja foi obtido melhor/primeiro caso
		    }
		}
	    }
	    else {
		// metodo n encntrado ?? mandar superclass ??
	    }
	}

	// tipo de impressao a fazer caso seja um array ou um objecto mais simples
	if (obj.getObjectClass().getClass().isArray()) {
	    Object[] results = (Object[]) obj.getObjectClass();
	    for (Object iter : results)
		System.out.println(iter);
	}
	else {
	    System.out.println(obj.getObjectClass());
	}
    }

}
