package sliit.pg09.carcare.pendingAppointment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sliit.pg09.carcare.client.Client;
import sliit.pg09.carcare.vehicle.Vehicle;
import sliit.pg09.carcare.vehicle.model.CarModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PendingAppointmentController {
    private final List<PendingAppointment> sampleAppointments;
    @Autowired
    private PendingAppointmentService pendingAppointmentService;

    public PendingAppointmentController() {
        this.sampleAppointments = createSampleAppointments();
    }

    private List<PendingAppointment> createSampleAppointments() {
        List<PendingAppointment> appointments = new ArrayList<>();

        // Create sample clients
        Client client1 = new Client();
        client1.setName("John Smith");
        client1.setPhone("+1 (555) 123-4567");
        client1.setImageUrl("https://lh3.googleusercontent.com/a/ACg8ocIazPzNQsfrx16bSitYr5Xn7uGxcvtIIbm-jCA5-lhg_KNGDtqS=s96-c");

        Client client2 = new Client();
        client2.setName("Emma Davis");
        client2.setPhone("+1 (555) 987-6543");
        client2.setImageUrl("https://lh3.googleusercontent.com/a/ACg8ocIazPzNQsfrx16bSitYr5Xn7uGxcvtIIbm-jCA5-lhg_KNGDtqS=s96-c");

        // Create sample models
        CarModel model1 = new CarModel();
        model1.setNumber("M1");
        model1.setMake("Porsche");
        model1.setYear(2023);
        model1.setImage("https://images-porsche.imgix.net/-/media/5D0BB7E042BD4C9DBEF84B5E70482520_73AA748306934B0C9CE20E32231DFCE2_CZ25W01IX0011911-carrera-front?w=750&q=85&auto=format");

        CarModel model2 = new CarModel();
        model2.setNumber("M2");
        model2.setMake("BMW");
        model2.setYear(2022);
        model2.setImage("https://images-porsche.imgix.net/-/media/5D0BB7E042BD4C9DBEF84B5E70482520_73AA748306934B0C9CE20E32231DFCE2_CZ25W01IX0011911-carrera-front?w=750&q=85&auto=format");

        // Create sample vehicles
        Vehicle vehicle1 = new Vehicle();
        vehicle1.setLicense("ABC-1234");
        vehicle1.setModel(model1);
        vehicle1.setClient(client1);

        Vehicle vehicle2 = new Vehicle();
        vehicle2.setLicense("XYZ-9876");
        vehicle2.setModel(model2);
        vehicle2.setClient(client2);

        // Create pending appointments
        PendingAppointment appointment1 = new PendingAppointment();
        appointment1.setAppointmentDetails(vehicle1, LocalDateTime.now().minusDays(1));
        appointment1.setDescription("Regular maintenance and oil change");
        appointment1.setStatus("pending");
        appointment1.setScheduledTime(LocalDateTime.now().plusDays(2));

        PendingAppointment appointment2 = new PendingAppointment();
        appointment2.setAppointmentDetails(vehicle2, LocalDateTime.now().minusDays(2));
        appointment2.setDescription("Brake inspection and tire rotation");
        appointment2.setStatus("rejected");
        appointment2.setScheduledTime(LocalDateTime.now().plusDays(3));

        appointments.add(appointment1);
        appointments.add(appointment2);
        return appointments;
    }

    @RequestMapping("/client")
    public static class ClientPendingAppointment {
        private PendingAppointmentController parentController;

        @GetMapping("/pending-appointment")
        public ResponseEntity<List<PendingAppointment>> getPendingAppointment(
                @RequestParam String vehicle) {
            return ResponseEntity.ok(parentController.sampleAppointments.stream()
                    .filter(a -> a.getVehicle().getLicense().equals(vehicle))
                    .collect(Collectors.toList()));
        }
    }

    @Controller
    @RequestMapping("/admin")
    public static class AdminPendingAppointment {
        @Autowired
        private PendingAppointmentController parentController;

        @GetMapping("/appointments")
        public String getAppointments(Model model) {
            model.addAttribute("pendingAppointments",
                    parentController.sampleAppointments.stream()
                            .filter(a -> "pending".equals(a.getStatus()))
                            .collect(Collectors.toList()));

            model.addAttribute("rejectedAppointments",
                    parentController.sampleAppointments.stream()
                            .filter(a -> "rejected".equals(a.getStatus()))
                            .collect(Collectors.toList()));

            return "Admin/Components/Appoinments";
        }

        @PostMapping("/appointments/reject")
        @ResponseBody
        public ResponseEntity<String> rejectAppointment(
                @RequestParam String license,
                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdTime) {

            parentController.sampleAppointments.stream()
                    .filter(a -> a.getId().getLicense().equals(license)
                            && a.getId().getCreatedTime().equals(createdTime))
                    .findFirst()
                    .ifPresent(a -> a.setStatus("rejected"));

            return ResponseEntity.ok()
                    .header("HX-Trigger", "appointmentUpdated")
                    .build();
        }
    }
}