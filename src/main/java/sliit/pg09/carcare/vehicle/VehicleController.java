package sliit.pg09.carcare.vehicle;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sliit.pg09.carcare.vehicle.model.Model;
import sliit.pg09.carcare.vehicle.model.ModelRepository;
import sliit.pg09.carcare.vehicle.model.ModelService;

@Controller
public class VehicleController {

    @Controller
    @RequestMapping("/client")
    public static class ClientVehicleController {
        @Autowired
        private VehicleService vehicleService;

        @HxRequest
        @PostMapping("/vehicle")
        public ResponseEntity<String> addVehicle(@RequestBody Vehicle vehicle) {
            boolean created = vehicleService.createVehicle(vehicle);
            return created ? ResponseEntity.ok("Vehicle created") : ResponseEntity.badRequest().body("Vehicle already exists or invalid");
        }

        @GetMapping("/vehicle/{license}")
        public ResponseEntity<Vehicle> getVehicle(@PathVariable String license) {
            Vehicle vehicle = vehicleService.getVehicleByLicense(license);
            return vehicle != null ? ResponseEntity.ok(vehicle) : ResponseEntity.notFound().build();
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
        public ResponseEntity<String> addModel(@RequestBody Model model) {
            modelRepository.save(model);
            return ResponseEntity.ok("Model added");
        }


        public ResponseEntity<Model> getModel(@PathVariable String number) {
            Model model = modelService.getModelByNumber(number);
            return model != null ? ResponseEntity.ok(model) : ResponseEntity.notFound().build();
        }

        @PutMapping("vehicle-model")
        public ResponseEntity<Model> updateModel(@RequestBody Model model) {
            Model updated = modelService.updateModel(model);
            return ResponseEntity.ok(updated);
        }

        @DeleteMapping("/vehicle-model/{number}")
        public ResponseEntity<String> deleteModel(@PathVariable String number) {
            modelRepository.deleteById(number);
            return ResponseEntity.ok("Model deleted");
        }

    }
}
