package entity;

/*
Exception thrown when the user chooses an inappropriate file format.
 */
public class WrongFormat extends Exception{

    public WrongFormat(String exception){
        super(exception);
    }
}
