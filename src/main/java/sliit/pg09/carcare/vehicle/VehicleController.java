package sliit.pg09.carcare.vehicle;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sliit.pg09.carcare.vehicle.model.CarModel;
import sliit.pg09.carcare.vehicle.model.ModelRepository;
import sliit.pg09.carcare.vehicle.model.ModelService;

@Controller
public class VehicleController {

    @Controller
    @RequestMapping("/client")
    public static class ClientVehicleController {
        @Autowired
        private VehicleService vehicleService;
        @Autowired
        private ModelService modelService;

        @GetMapping("/vehicle/{license}")
        public ResponseEntity<Vehicle> getVehicle(@PathVariable String license) {
            Vehicle vehicle = vehicleService.getVehicleByLicense(license);
            return vehicle != null ? ResponseEntity.ok(vehicle) : ResponseEntity.notFound().build();
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
        private ModelRepository modelRepository;
        @Autowired
        private ModelService modelService;

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
            modelRepository.save(model);
            return ResponseEntity.ok("Model added");
        }


        public ResponseEntity<CarModel> getModel(@PathVariable String number) {
            CarModel model = modelService.getModelByNumber(number);
            return model != null ? ResponseEntity.ok(model) : ResponseEntity.notFound().build();
        }

        @PutMapping("vehicle-model")
        public ResponseEntity<CarModel> updateModel(@RequestBody CarModel carModel) {
            CarModel updated = modelService.updateModel(carModel);
            return ResponseEntity.ok(updated);
        }

        @DeleteMapping("/vehicle-model/{number}")
        public ResponseEntity<String> deleteModel(@PathVariable String number) {
            modelRepository.deleteById(number);
            return ResponseEntity.ok("Model deleted");
        }
    }
}
