package game;

public class OutofBoundaryException extends Exception {
    public OutofBoundaryException(String errorString) {
        super(errorString);
    }
}
