package sliit.pg09.carcare.pendingAppointment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PendingAppointmentController {
    PendingAppointmentService pendingAppointmentService;


    @RequestMapping("/client")
    public static class ClientPendingAppointment {
        @Autowired
        private PendingAppointmentService pendingAppointmentService;

        @GetMapping("/pending-appointment")
        public ResponseEntity<List<PendingAppointment>> getPendingAppointment(
                @RequestParam String vehicle) {
            List<PendingAppointment> pendingAppointments = pendingAppointmentService.getPendingAppointments(vehicle);
            return ResponseEntity.ok(pendingAppointments);
        }

    }
}
