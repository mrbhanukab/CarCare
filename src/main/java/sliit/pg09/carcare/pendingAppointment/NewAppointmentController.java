package sliit.pg09.carcare.pendingAppointment;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sliit.pg09.carcare.client.Client;
import sliit.pg09.carcare.vehicle.Vehicle;
import sliit.pg09.carcare.vehicle.VehicleService;
import sliit.pg09.carcare.vehicle.model.CarModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class NewAppointmentController {
    private final List<NewAppointment> sampleAppointments;

    @Autowired
    NewAppointmentService newAppointmentService;

    @Autowired
    private VehicleService vehicleService;

    public NewAppointmentController() {
        this.sampleAppointments = createSampleAppointments();
    }

    private List<NewAppointment> createSampleAppointments() {
        List<NewAppointment> appointments = new ArrayList<>();

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
        NewAppointment appointment1 = new NewAppointment();
        appointment1.setAppointmentDetails(vehicle1, LocalDateTime.now().minusDays(1));

        NewAppointment appointment2 = new NewAppointment();
        appointment2.setAppointmentDetails(vehicle2, LocalDateTime.now().minusDays(2));

        appointments.add(appointment1);
        appointments.add(appointment2);
        return appointments;
    }

    @Controller
    @RequestMapping("/client")
    public static class ClientPendingAppointment {
        @Autowired
        private NewAppointmentController parentController;

        @Autowired
        private VehicleService vehicleService;
        @Autowired
        private NewAppointmentService newAppointmentService;

        @HxRequest
        @GetMapping("/new-appointment")
        public String getNewAppointmentModal(Model model, @RequestParam String license) {
            model.addAttribute("vehicle", vehicleService.getVehicleByLicense(license));
            model.addAttribute("serviceTypes", ServiceType.values());
            return "Client/Components/NewAppointmentModal :: newAppoinment";
        }

        @HxRequest
        @PostMapping("/new-appointment")
        public String createNewAppointment(
                @RequestParam("license") String license,
                @RequestParam("services") String[] services,
                @RequestParam("preferredDateTime1") String dateTime1,
                @RequestParam("preferredDateTime2") String dateTime2,
                @RequestParam("preferredDateTime3") String dateTime3,
                @RequestParam(value = "notes", required = false) String notes,
                Model model,
                HttpServletResponse response) {

            try {
                Vehicle vehicle = vehicleService.getVehicleByLicense(license);
                if (vehicle == null) {
                    model.addAttribute("message", "Vehicle not found");
                    return "Components/Error :: error";
                }

                NewAppointment appointment = new NewAppointment();
                appointment.setAppointmentDetails(vehicle, LocalDateTime.now());

                Set<ServiceType> serviceTypes = Arrays.stream(services)
                        .map(ServiceType::valueOf)
                        .collect(Collectors.toSet());
                appointment.setServices(serviceTypes);

                List<LocalDateTime> preferredDates = Arrays.asList(
                        LocalDateTime.parse(dateTime1),
                        LocalDateTime.parse(dateTime2),
                        LocalDateTime.parse(dateTime3)
                );
                appointment.setPreferredDateTimes(preferredDates);
                appointment.setNotes(notes);
                newAppointmentService.saveAppointment(appointment);

                // Close the modal upon success
                response.setHeader("HX-Trigger", "closeModal");
                response.setHeader("HX-Redirect", "/client/");
                return null;
            } catch (Exception e) {
                model.addAttribute("message", "Failed to create appointment: " + e.getMessage());
                return "Components/Error :: error";
            }
        }
//
//    @Controller
//    @RequestMapping("/admin")
//    public static class AdminPendingAppointment {
//        @Autowired
//        private NewAppointmentController parentController;
//
//        @GetMapping("/appointments")
//        public String getAppointments(Model model) {
//            model.addAttribute("pendingAppointments",
//                    parentController.sampleAppointments.stream()
//                            .filter(a -> "pending".equals(a.getStatus()))
//                            .collect(Collectors.toList()));
//
//            model.addAttribute("rejectedAppointments",
//                    parentController.sampleAppointments.stream()
//                            .filter(a -> "rejected".equals(a.getStatus()))
//                            .collect(Collectors.toList()));
//
//            return "Admin/Components/Appoinments";
//        }
//
//        @PostMapping("/appointments/reject")
//        @ResponseBody
//        public ResponseEntity<String> rejectAppointment(
//                @RequestParam String license,
//                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdTime) {
//
//            parentController.sampleAppointments.stream()
//                    .filter(a -> a.getId().getLicense().equals(license)
//                            && a.getId().getCreatedTime().equals(createdTime))
//                    .findFirst()
//                    .ifPresent(a -> a.setStatus("rejected"));
//
//            return ResponseEntity.ok()
//                    .header("HX-Trigger", "appointmentUpdated")
//                    .build();
//        }
//    }
    }
}