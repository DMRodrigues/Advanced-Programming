package ist.meic.pa.GenericFunctionsExtended;

public class GFMethod {

    private final boolean callNextMethod;

    public boolean callNextMethod() {
	return this.callNextMethod;
    }

    public GFMethod(boolean callNextMethod) {
	this.callNextMethod = callNextMethod;
    }

    public GFMethod() {
	this(false);
    }

}
