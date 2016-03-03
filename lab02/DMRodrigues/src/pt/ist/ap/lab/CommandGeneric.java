package pt.ist.ap.lab;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class CommandGeneric implements Command {

    @Override
    public void execute(Object obj, String[] input)
	    throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

	StringBuilder strBuilder = new StringBuilder();
	for (int i = 0; i < input.length; i++)
	    strBuilder.append(input[i] + " ");
	System.out.println("Trying generic command: " + strBuilder.toString());

	// neste caso é preciso dividir a string input
	String params[] = Arrays.copyOfRange(input, 1, input.length);

	for (Method m : ((Shell) obj).objectClass.getClass().getMethods()) {
	    if (m.getName().equals(input[0])) {

		// se n levar parametros, podia comparar params
		if (input.length == 1) {
		    ((Shell) obj).objectClass = m.invoke(((Shell) obj).objectClass);
		} else {
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
			    ((Shell) obj).objectClass = m.invoke(((Shell) obj).objectClass, (Object) finalParams);

			} else { // se nao converter para array de forma a poder executar/invoke
			    ((Shell) obj).objectClass = m.invoke(((Shell) obj).objectClass, (Object[]) finalParams);
			}
			break; // para pois ja foi obtido melhor/primeiro caso
		    }
		}

	    } else {
		// metodo n encntrado ??
		// mandar superclass ??
	    }
	}

	// tipo de impressao a fazer caso seja um array ou um objecto mais simples
	if (((Shell) obj).objectClass.getClass().isArray()) {
	    Object[] results = (Object[]) ((Shell) obj).objectClass;
	    for (Object iter : results)
		System.out.println(iter);
	} else {
	    System.out.println(((Shell) obj).objectClass);
	}
    }

}
