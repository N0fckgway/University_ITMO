package Interfaces;
import Exception.CustomSetDeathException;
import People.Persons;

public interface Pain {
    void pain(Persons person) throws CustomSetDeathException;
}
