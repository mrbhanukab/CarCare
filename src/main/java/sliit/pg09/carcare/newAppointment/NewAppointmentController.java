package sliit.pg09.carcare.newAppointment;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sliit.pg09.carcare.common.ServiceType;
import sliit.pg09.carcare.ongoingAppointment.OngoingAppointmentService;
import sliit.pg09.carcare.vehicle.Vehicle;
import sliit.pg09.carcare.vehicle.VehicleService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class NewAppointmentController {
    private final NewAppointmentService newAppointmentService;
    private final VehicleService vehicleService;

    @Controller
    @RequestMapping("/client")
    @RequiredArgsConstructor
    public static class ClientPendingAppointment {
        private final VehicleService vehicleService;
        private final NewAppointmentService newAppointmentService;

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

                response.setHeader("HX-Trigger", "closeModal");
                response.setHeader("HX-Redirect", "/client/");
                return null;
            } catch (Exception e) {
                model.addAttribute("message", "Failed to create appointment: " + e.getMessage());
                return "Components/Error :: error";
            }
        }

        @Controller
        @RequestMapping("/admin")
        @RequiredArgsConstructor
        public static class AdminPendingAppointment {
            private final NewAppointmentService newAppointmentService;
            private final OngoingAppointmentService ongoingAppointmentService;

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

            @HxRequest
            @PostMapping("/appointments/select-date")
            public String selectPreferredDate(
                    @RequestParam String license,
                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime createdTime,
                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime selectedDate,
                    Model model) {
                try {
                    NewAppointment.PendingAppointmentId id = new NewAppointment.PendingAppointmentId();
                    id.setLicense(license);
                    id.setCreatedTime(createdTime);
                    NewAppointment newAppointment = newAppointmentService.getNewAppointmentsByVehicle(license)
                            .stream()
                            .filter(a -> a.getId().equals(id))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

                    ongoingAppointmentService.createOngoingAppointment(license, selectedDate, newAppointment.getServiceNames());

                    newAppointmentService.deleteAppointmentById(id);

                    List<NewAppointment> remainingAppointments = newAppointmentService.getNewAppointments();
                    model.addAttribute("activeCount", remainingAppointments.size());
                    return "Admin/Components/AppointmentCounter :: requestCounterElement";
                } catch (Exception e) {
                    model.addAttribute("message", "Failed to select date: " + e.getMessage());
                    return "Components/Error :: error";
                }
            }
        }
    }
}