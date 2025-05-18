package sliit.pg09.carcare.vehicle;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sliit.pg09.carcare.nextService.NextServiceService;
import sliit.pg09.carcare.vehicle.CarModel.CarModel;
import sliit.pg09.carcare.vehicle.CarModel.CarModelRepository;
import sliit.pg09.carcare.vehicle.CarModel.CarModelService;

@Controller
public class VehicleController {

    @Controller
    @RequestMapping("/client")
    public static class ClientVehicleController {
        @Autowired
        private VehicleService vehicleService;
        @Autowired
        private CarModelService carModelService;
        @Autowired
        private NextServiceService nextServiceService;

        @HxRequest
        @GetMapping("/vehicle")
        public String getVehicle(@RequestParam String license, Model model, HttpServletResponse response) {
            try {
                Vehicle vehicle = vehicleService.getVehicleByLicense(license);
                if (vehicle != null) {
                    model.addAttribute("vehicle", vehicle);

                    // Add this block:
                    var nextServiceOpt = nextServiceService.getNextServiceByLicense(vehicle.getLicense());
                    model.addAttribute("nextService", nextServiceOpt.orElse(null));

                    response.setHeader("HX-Trigger", "closeModal");
                    return "Client/Screens/Vehicle-X :: vehicle";
                } else {
                    model.addAttribute("message", "Vehicle not found");
                    return "Components/Error :: error";
                }
            } catch (Exception e) {
                model.addAttribute("message", "An error occurred while loading the vehicle");
                return "Components/Error :: error";
            }
        }

        @HxRequest
        @GetMapping("/vehicles/modal")
        public String getVehicleModal(Model model) {
            model.addAttribute("vehicles", vehicleService.getVehiclesByCurrentClient());
            return "Client/Components/VehicleSelectorModal :: vehicleSelectorModal()";
        }

        @HxRequest
        @PostMapping("/vehicle")
        public String addVehicle(@RequestParam String license, @RequestParam String modelNumber,
                                 Model model, HttpServletResponse response) {
            try {
                Vehicle vehicle = vehicleService.createVehicle(license, modelNumber);
                response.setHeader("HX-Redirect", "/client/");
                return null;
            } catch (IllegalArgumentException | IllegalStateException e) {
                model.addAttribute("message", e.getMessage());
                return "Components/Error :: error";
            } catch (Exception e) {
                model.addAttribute("message", "An unexpected error occurred");
                return "Components/Error :: error";
            }
        }
    }

    @Controller
    @RequestMapping("/admin")
    public static class AdminVehicleController {
        private final VehicleRepository vehicleRepository;
        @Autowired
        private CarModelRepository carModelRepository;
        @Autowired
        private CarModelService carModelService;

        public AdminVehicleController(VehicleRepository vehicleRepository) {
            this.vehicleRepository = vehicleRepository;
        }

        @DeleteMapping
        public ResponseEntity<String> deleteVehicle(@PathVariable String license) {
            vehicleRepository.deleteById(license);
            return ResponseEntity.ok("Vehicle deleted");
        }

        @PostMapping("/vehicle-model")
        public ResponseEntity<String> addModel(@RequestBody CarModel model) {
            carModelRepository.save(model);
            return ResponseEntity.ok("Model added");
        }


        public ResponseEntity<CarModel> getModel(@PathVariable String number) {
            CarModel model = carModelService.getModelByNumber(number);
            return model != null ? ResponseEntity.ok(model) : ResponseEntity.notFound().build();
        }

        @PutMapping("vehicle-model")
        public ResponseEntity<CarModel> updateModel(@RequestBody CarModel carModel) {
            CarModel updated = carModelService.updateModel(carModel);
            return ResponseEntity.ok(updated);
        }

        @DeleteMapping("/vehicle-model/{number}")
        public ResponseEntity<String> deleteModel(@PathVariable String number) {
            carModelRepository.deleteById(number);
            return ResponseEntity.ok("Model deleted");
        }
    }
}
