package sliit.pg09.carcare.newAppointment;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sliit.pg09.carcare.common.ServiceType;
import sliit.pg09.carcare.vehicle.Vehicle;
import sliit.pg09.carcare.vehicle.VehicleService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class NewAppointmentController {
    @Autowired
    NewAppointmentService newAppointmentService;

    @Autowired
    private VehicleService vehicleService;

    public NewAppointmentController() {
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
        public String createNewAppointment(@RequestParam("license") String license, @RequestParam("services") String[] services, @RequestParam("preferredDateTime1") String dateTime1, @RequestParam("preferredDateTime2") String dateTime2, @RequestParam("preferredDateTime3") String dateTime3, @RequestParam(value = "notes", required = false) String notes, Model model, HttpServletResponse response) {

            try {
                Vehicle vehicle = vehicleService.getVehicleByLicense(license);
                if (vehicle == null) {
                    model.addAttribute("message", "Vehicle not found");
                    return "Components/Error :: error";
                }

                NewAppointment appointment = new NewAppointment();
                appointment.setAppointmentDetails(vehicle, LocalDateTime.now());

                Set<ServiceType> serviceTypes = Arrays.stream(services).map(ServiceType::valueOf).collect(Collectors.toSet());
                appointment.setServices(serviceTypes);

                List<LocalDateTime> preferredDates = Arrays.asList(LocalDateTime.parse(dateTime1), LocalDateTime.parse(dateTime2), LocalDateTime.parse(dateTime3));
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
        @Controller
        @RequestMapping("/admin")
        public static class AdminPendingAppointment {
            @Autowired
            private NewAppointmentController parentController;
            @Autowired
            private NewAppointmentService newAppointmentService;

            @HxRequest
            @DeleteMapping("/appointments/reject")
            public String rejectAppointment(@RequestParam String license, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime createdTime, Model model) {
                try {
                    NewAppointment.PendingAppointmentId id = new NewAppointment.PendingAppointmentId();
                    id.setLicense(license);
                    id.setCreatedTime(createdTime);

                    newAppointmentService.deleteAppointmentById(id);

                    List<NewAppointment> remainingAppointments = newAppointmentService.getNewAppointments();
                    model.addAttribute("activeCount", remainingAppointments.size());
                    return "Admin/Components/AppointmentCounter :: requestCounterElement";
                } catch (Exception e) {
                    model.addAttribute("message", "Failed to reject appointment: " + e.getMessage());
                    return "Components/Error :: error";
                }
            }
        }
    }
}