package softuni.exam.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import softuni.exam.service.PassengerService;

import java.io.FileNotFoundException;

@Controller
@RequestMapping("/export")
public class ExportController extends BaseController {


    private final PassengerService passengerService;

    @Autowired
    public ExportController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }


    @GetMapping("/passengers-by-tickets")
    public ModelAndView exportCarsByPictures() throws FileNotFoundException {
        String passengers = this.passengerService
                .getPassengersOrderByTicketsCountDescendingThenByEmail();

        return super.view("export/export-passengers-by-tickets.html","passengers", passengers);
    }
}
