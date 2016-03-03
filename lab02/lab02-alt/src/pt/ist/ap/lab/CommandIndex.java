package pt.ist.ap.lab;

public class CommandIndex implements Command {

    @Override
    public void execute(Object obj, String[] input) {

	// verificar se e um array para se poder aceder indices
	if (((Shell) obj).objectClass.getClass().isArray()) {

	    Object[] myArray = (Object[]) ((Shell) obj).objectClass; // para se poder aceder
	    ((Shell) obj).objectClass = myArray[Integer.parseInt(input[1])]; // obter o indice pedido
	    System.out.println(((Shell) obj).objectClass.toString());

	} else {
	    // se n for array ??
	}
    }

}
