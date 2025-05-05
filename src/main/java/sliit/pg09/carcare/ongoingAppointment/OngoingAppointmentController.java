package sliit.pg09.carcare.ongoingAppointment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class OngoingAppointmentController {
    @Autowired
    static OngoingAppointmentService ongoingAppointmentService;

    @RequestMapping("/client")
    public static class ClientOngoingAppointment {
        @GetMapping("/ongoing-appointment")
        public ResponseEntity<List<OngoingAppointment>> getOngoingAppointment(
                @RequestParam String vehicle) {
            List<OngoingAppointment> ongoingAppointments = ongoingAppointmentService.getOngoingAppointments(vehicle);
            return ResponseEntity.ok(ongoingAppointments);
        }

    }
}
