package sliit.pg09.carcare.common.utills;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sliit.pg09.carcare.client.Client;
import sliit.pg09.carcare.client.ClientRepository;
import sliit.pg09.carcare.emergency.EmergencyRepository;
import sliit.pg09.carcare.vehicle.Vehicle;
import sliit.pg09.carcare.vehicle.VehicleRepository;
import sliit.pg09.carcare.vehicle.model.Model;
import sliit.pg09.carcare.vehicle.model.ModelRepository;

import java.util.List;
import java.util.stream.IntStream;

@Component
public class SampleDataLoader implements CommandLineRunner {
    private final Faker faker;
    private final ClientRepository clientRepository;
    private final ModelRepository modelRepository;
    private final VehicleRepository vehicleRepository;
    private final EmergencyRepository emergencyRepository;

    public SampleDataLoader(ClientRepository clientRepository, ModelRepository modelRepository, VehicleRepository vehicleRepository, EmergencyRepository emergencyRepository) {
        this.clientRepository = clientRepository;
        this.faker = new Faker();
        this.modelRepository = modelRepository;
        this.vehicleRepository = vehicleRepository;
        this.emergencyRepository = emergencyRepository;
    }

    public void clientsLoader() {
        List<Client> clients = IntStream.rangeClosed(1, 100)
                .mapToObj(client -> new Client(
                        faker.name().fullName(),
                        faker.internet().emailAddress(),
                        "http://yt3.ggpht.com/ytc/AIdro_lBaOkvuCIkdLdhhi78ONhJy57heqBCdiAJH8Nd73tz6wk=s88-c-k-c0x00ffffff-no-rj"
                ))
                .toList();
        clientRepository.saveAll(clients);
    }

    public void modelLoader() {
        List<Model> models = IntStream.rangeClosed(1, 10)
                .mapToObj(model -> new Model(
                        faker.random().hex(), // Random unique identifier
                        faker.options().option(Model.color.values()), // Random color
                        faker.options().option(Model.type.values()), // Random type
                        faker.number().numberBetween(1990, 2024), // Random year
                        faker.number().numberBetween(1, 12), // Random noOfCylinders
                        faker.number().numberBetween(50, 500), // Random power
                        faker.number().randomDouble(2, 1, 10), // Random length
                        faker.number().randomDouble(2, 1, 10), // Random width
                        faker.number().randomDouble(2, 1, 10) // Random wheelBase
                ))
                .toList();
        modelRepository.saveAll(models);
    }

    public void vehicleLoader() {
        List<Vehicle> vehicles = IntStream.rangeClosed(1, 100)
                .mapToObj(vehicle -> {
                    Client client = clientRepository.findById(faker.random().hex()).orElse(null);
                    Model model = modelRepository.findById(faker.random().hex()).orElse(null);
                    return new Vehicle(
                            faker.letterify("??-####"), // Random license plate
                            faker.bothify("??#####"), // Random VIN
                            model,
                            client
                    );
                })
                .toList();
        vehicleRepository.saveAll(vehicles);
    }

    @Override
    public void run(String... args) throws Exception {
        clientsLoader();
        modelLoader();
        vehicleLoader();
    }
}