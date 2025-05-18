package sliit.pg09.carcare.emergency;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sliit.pg09.carcare.client.Client;
import sliit.pg09.carcare.vehicle.CarModel.CarModel;
import sliit.pg09.carcare.vehicle.Vehicle;
import sliit.pg09.carcare.vehicle.VehicleService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class EmergencyController {
    private final List<Emergency> emergencies;
    @Autowired
    EmergencyService emergencyService;

    public EmergencyController() {
        this.emergencies = createSampleEmergencies();
    }

    private List<Emergency> createSampleEmergencies() {
        List<Emergency> sampleEmergencies = new ArrayList<>();

        // Create sample clients
        Client client1 = new Client();
        client1.setName("John Smith");
        client1.setPhone("+1 (555) 123-4567");
        client1.setImageUrl("https://lh3.googleusercontent.com/a/ACg8ocIazPzNQsfrx16bSitYr5Xn7uGxcvtIIbm-jCA5-lhg_KNGDtqS=s96-c");

        // Create sample models
        CarModel model1 = new CarModel();
        model1.setNumber("1234");
        model1.setColor(CarModel.color.WHITE);
        model1.setYear(2019);
        model1.setImage("https://images-porsche.imgix.net/-/media/5D0BB7E042BD4C9DBEF84B5E70482520_73AA748306934B0C9CE20E32231DFCE2_CZ25W01IX0011911-carrera-front?w=750&q=85&auto=format");

        // Create sample vehicles
        Vehicle vehicle1 = new Vehicle();
        vehicle1.setLicense("ABC-1234");
        vehicle1.setModel(model1);
        vehicle1.setClient(client1);

        // Create emergencies
        Emergency emergency1 = new Emergency();
        emergency1.setId(new Emergency.EmergencyId());
        emergency1.getId().setVehicleLicense(vehicle1.getLicense());
        emergency1.getId().setEmergencyTime(LocalDateTime.now().minusMinutes(10));
        emergency1.setLocation(new Emergency.Location(6.777703649096581, 79.91003484541915));
        emergency1.setVehicle(vehicle1);
        emergency1.setHandled(false);

        sampleEmergencies.add(emergency1);
        return sampleEmergencies;
    }

    @Controller
    @RequestMapping("/client")
    public static class AClientEmergency {
        @Autowired
        private EmergencyController emergencyController;
        @Autowired
        private EmergencyService emergencyService;
        @Autowired
        private VehicleService vehicleService;

        @HxRequest
        @GetMapping("/emergency")
        public String getEmergencies(@RequestParam String license, Model model) {
            Vehicle vehicle = vehicleService.getVehicleByLicense(license);
            model.addAttribute("vehicle", vehicle);
            return "Client/Components/EmergencyConfirmation";
        }

        @HxRequest
        @PostMapping("/emergency")
        public String createEmergency(
                @RequestParam String license,
                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime timestamp,
                @RequestParam double latitude,
                @RequestParam double longitude,
                Model model, HttpServletResponse response) {

            Vehicle vehicle = vehicleService.getVehicleByLicense(license);
            if (vehicle == null) {
                model.addAttribute("message", "Vehicle not found");
                return "Components/Error :: error";
            }

            emergencyService.createEmergency(vehicle, timestamp, latitude, longitude);

            response.setHeader("HX-Trigger", "closeModal");
            return null;
        }
    }

    @Controller
    @RequestMapping("/admin")
    public static class AdminEmergency {
        @Autowired
        private EmergencyController emergencyController;
        @Autowired
        private EmergencyService emergencyService;

        @PostMapping("/emergency/handle")
        public String markAsHandled(
                @RequestParam String license,
                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime time,
                Model model) {

            boolean success = emergencyService.markRequestAsHandled(license, time);

            if (!success) {
                model.addAttribute("message", "Emergency request not found or already handled");
                return "Components/Error :: error";
            }

            model.addAttribute("activeCount", emergencyService.getActiveEmergencies().size());
            return "Admin/Components/EmergencyCounter :: counterElement";
        }
    }
}