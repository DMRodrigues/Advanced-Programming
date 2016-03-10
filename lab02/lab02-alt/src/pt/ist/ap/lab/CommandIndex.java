package pt.ist.ap.lab;

public class CommandIndex implements Command {

    @Override
    public void execute(Shell obj, String[] input) {

	// verificar se e um array para se poder aceder indices
	if (obj.getObjectClass().getClass().isArray()) {

	    Object[] myArray = (Object[]) obj.getObjectClass(); // para se poder aceder
	    obj.setObjectClass(myArray[Integer.parseInt(input[1])]); // obter o indice pedido
	    System.out.println(obj.getObjectClass());
	}
	else {
	    // se n for array ??
	}
    }
}
