package sliit.pg09.carcare.pendingAppointment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class PendingAppointmentController {
    @Autowired
    PendingAppointmentService pendingAppointmentService;
}
