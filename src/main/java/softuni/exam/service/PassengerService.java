package softuni.exam.service;



import softuni.exam.models.entities.Passenger;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface PassengerService {

    boolean areImported();

    String readPassengersFileContent() throws IOException;
	
	String importPassengers() throws IOException;

	String getPassengersOrderByTicketsCountDescendingThenByEmail() throws FileNotFoundException;

	Passenger findByEmail(String email);
}
