package sliit.pg09.carcare.ongoingAppointment;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sliit.pg09.carcare.common.ServiceType;
import sliit.pg09.carcare.completedAppointment.BillingInfo;
import sliit.pg09.carcare.completedAppointment.completedAppointment;
import sliit.pg09.carcare.completedAppointment.completedAppointmentService;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class OngoingAppointmentController {
    private final OngoingAppointmentService ongoingAppointmentService;
    private final completedAppointmentService completedAppointmentService;

    @HxRequest
    @GetMapping("/finalize-bill")
    public String getFinalizeBill(@RequestParam("license") String license,
                                  @RequestParam("appointmentTime") String appointmentTime,
                                  Model model) {
        LocalDateTime parsedTime = LocalDateTime.parse(appointmentTime);
        OngoingAppointment appointment = ongoingAppointmentService.getAppointmentById(license, parsedTime);
        model.addAttribute("selectedAppointment", appointment);
        return "Admin/Components/FinalizeBill :: finalizeBill";
    }

    @HxRequest
    @PostMapping("/finalize-bill")
    public String finalizeBill(@RequestParam("license") String license,
                               @RequestParam("appointmentTime") String appointmentTime,
                               @RequestParam("laborCost") double laborCost,
                               @RequestParam("partsCost") double partsCost,
                               @RequestParam("additionalFees") double additionalFees,
                               @RequestParam("discount") double discount,
                               @RequestParam("notes") String notes) {
        LocalDateTime parsedTime = LocalDateTime.parse(appointmentTime);

        // Create BillingInfo
        BillingInfo billingInfo = new BillingInfo();
        billingInfo.setLabour(laborCost);
        billingInfo.setParts(partsCost);
        billingInfo.setAdditional(additionalFees);
        billingInfo.setDiscount(discount);

        // Create completedAppointment
        OngoingAppointment ongoingAppointment = ongoingAppointmentService.getAppointmentById(license, parsedTime);
        completedAppointment completed = new completedAppointment();
        completed.setAppointmentDetails(ongoingAppointment.getVehicle(), parsedTime);
        completed.setServices(ongoingAppointment.getServiceNames().stream().map(ServiceType::valueOf).collect(Collectors.toSet()));
        completed.setBillingInfo(billingInfo);
        completed.setNotes(notes);

        // Insert completedAppointment and delete ongoingAppointment
        completedAppointmentService.insertAppointment(completed, billingInfo);
        ongoingAppointmentService.deleteOngoingAppointment(license, parsedTime);

        // Trigger full page reload
        return "redirect:/admin/";
    }
}