package util;

@SuppressWarnings("serial")
public class GeocodeException extends Exception {

    public GeocodeException() {
        super();
    }

    public GeocodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public GeocodeException(String message) {
        super(message);
    }

    public GeocodeException(Throwable cause) {
        super(cause);
    }
}

