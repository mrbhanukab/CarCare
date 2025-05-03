package sliit.pg09.carcare.ongoingAppointment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class OngoingAppointmentController {
    @Autowired
    OngoingAppointmentService ongoingAppointmentService;
}
