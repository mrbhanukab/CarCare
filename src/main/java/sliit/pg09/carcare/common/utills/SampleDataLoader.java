package sliit.pg09.carcare.common.utills;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sliit.pg09.carcare.client.Client;
import sliit.pg09.carcare.client.ClientService;
import sliit.pg09.carcare.emergency.Emergency;
import sliit.pg09.carcare.emergency.EmergencyService;
import sliit.pg09.carcare.vehicle.Vehicle;
import sliit.pg09.carcare.vehicle.VehicleService;
import sliit.pg09.carcare.vehicle.model.CarModel;
import sliit.pg09.carcare.vehicle.model.ModelService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Component
public class SampleDataLoader implements CommandLineRunner {
    private final Faker faker;
    private final EmergencyService emergencyService;
    private final VehicleService vehicleService;
    private final ClientService clientService;
    private final ModelService modelService;

    public SampleDataLoader(
            ClientService clientService,
            ModelService modelService,
            VehicleService vehicleService,
            EmergencyService emergencyService) {
        this.clientService = clientService;
        this.modelService = modelService;
        this.vehicleService = vehicleService;
        this.emergencyService = emergencyService;
        this.faker = new Faker();
    }

    public void clientsLoader() {
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Client client = new Client();
            client.setName(faker.name().fullName());
            client.setEmail(faker.internet().emailAddress());
            client.setPhone(faker.phoneNumber().cellPhone());
            client.setAddress(faker.address().fullAddress());
            clientService.createClient(client);
        });
    }

    public void modelLoader() {
        List<String> brands = List.of("Toyota", "Honda", "BMW", "Mercedes", "Audi");

        brands.forEach(brand -> {
            for (CarModel.type type : CarModel.type.values()) {
                CarModel model = new CarModel();
                model.setNumber(faker.regexify(brand.substring(0, 3).toUpperCase() + "-[0-9]{5}"));
                model.setMake(brand);
                model.setType(type);
                model.setYear(faker.number().numberBetween(2015, 2024));
                model.setColor(faker.options().option(CarModel.color.class));
                model.setNoOfCylinders(faker.number().numberBetween(3, 8));
                model.setPower(faker.number().numberBetween(100, 500));
                model.setLength(faker.number().randomDouble(2, 400, 600));
                model.setWidth(faker.number().randomDouble(2, 170, 200));
                model.setWheelBase(faker.number().randomDouble(2, 250, 300));
                // You might want to add actual image URLs here
                model.setImage("https://example.com/car-images/" + brand.toLowerCase() + ".jpg");

                modelService.updateModel(model);
            }
        });
    }

    public void vehicleLoader() {
        List<Client> clients = clientService.findAllClients();

        if (!clients.isEmpty()) {
            IntStream.rangeClosed(1, 20).forEach(i -> {
                Vehicle vehicle = new Vehicle();
                vehicle.setLicense(faker.regexify("[A-Z]{2,3}-[0-9]{4}"));
                vehicle.setClient(clients.get(faker.random().nextInt(clients.size())));
                vehicleService.createVehicle(vehicle);
            });
        }
    }

//    public void clientsLoader() {
//        List<Client> clients = IntStream.rangeClosed(1, 100)
//                .mapToObj(client -> new Client(
//                        faker.name().fullName(),
//                        faker.internet().emailAddress(),
//                        "http://yt3.ggpht.com/ytc/AIdro_lBaOkvuCIkdLdhhi78ONhJy57heqBCdiAJH8Nd73tz6wk=s88-c-k-c0x00ffffff-no-rj"
//                ))
//                .toList();
//        clientRepository.saveAll(clients);
//    }


    public void emergencyLoader() {
        // Get a list of vehicles from the repository
        List<Vehicle> vehicles = vehicleService.findAll();

        if (!vehicles.isEmpty()) {
            IntStream.rangeClosed(1, 10).forEach(i -> {
                // Get a random vehicle
                Vehicle randomVehicle = vehicles.get(faker.random().nextInt(vehicles.size()));

                // Generate random coordinates within reasonable bounds
                double latitude = faker.number().randomDouble(6, 6, 10);  // Sri Lanka roughly
                double longitude = faker.number().randomDouble(6, 79, 82); // Sri Lanka roughly

                // Generate a random time within the last 30 days
                LocalDateTime emergencyTime = LocalDateTime.ofInstant(
                        faker.date().past(30, TimeUnit.DAYS).toInstant(),
                        ZoneId.systemDefault()
                );

                Emergency emergency = new Emergency(
                        randomVehicle,
                        emergencyTime,
                        new Emergency.Location(latitude, longitude)
                );
                emergency.setHandled(faker.random().nextBoolean());

                emergencyService.createEmergency(emergency);
            });
        }
    }

    @Override
    public void run(String... args) throws Exception {
//        clientsLoader();
//        modelLoader();
//        vehicleLoader();
//        emergencyLoader();
    }
}