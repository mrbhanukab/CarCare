package sliit.pg09.carcare.common.utills;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sliit.pg09.carcare.client.ClientService;
import sliit.pg09.carcare.emergency.EmergencyService;
import sliit.pg09.carcare.vehicle.CarModel.CarModel;
import sliit.pg09.carcare.vehicle.CarModel.CarModelService;
import sliit.pg09.carcare.vehicle.VehicleService;

import java.util.stream.IntStream;

@Component
public class SampleDataLoader implements CommandLineRunner {
    private final Faker faker;
    private final EmergencyService emergencyService;
    private final VehicleService vehicleService;
    private final ClientService clientService;
    private final CarModelService carModelService;

    public SampleDataLoader(
            ClientService clientService,
            CarModelService carModelService,
            VehicleService vehicleService,
            EmergencyService emergencyService) {
        this.clientService = clientService;
        this.carModelService = carModelService;
        this.vehicleService = vehicleService;
        this.emergencyService = emergencyService;
        this.faker = new Faker();
    }

//    public void clientsLoader() {
//        IntStream.rangeClosed(1, 10).forEach(i -> {
//            Client client = new Client();
//            client.setName(faker.name().fullName());
//            client.setEmail(faker.internet().emailAddress());
//            client.setPhone(faker.phoneNumber().cellPhone());
//            client.setAddress(faker.address().fullAddress());
//            clientService.createClient(client);
//        });
//    }

    public void modelLoader() {
        IntStream.rangeClosed(1, 10).forEach(i -> {
            CarModel model = new CarModel();
            model.setNumber(faker.regexify("[A-Z]{3}-[0-9]{5}"));
            model.setYear(faker.number().numberBetween(2015, 2024));
            model.setColor(faker.options().option(CarModel.color.class));
            model.setImage("https://example.com/car-images/model-" + i + ".jpg");
            carModelService.updateModel(model);
        });
    }
//    public void vehicleLoader() {
//        List<Client> clients = clientService.findAllClients();
//
//        if (!clients.isEmpty()) {
//            IntStream.rangeClosed(1, 20).forEach(i -> {
//                Vehicle vehicle = new Vehicle();
//                vehicle.setLicense(faker.regexify("[A-Z]{2,3}-[0-9]{4}"));
//                vehicle.setClient(clients.get(faker.random().nextInt(clients.size())));
//                vehicleService.createVehicle(vehicle);
//            });
//        }
//    }


//    public void emergencyLoader() {
//        // Get a list of vehicles from the repository
//        List<Vehicle> vehicles = vehicleService.findAll();
//
//        if (!vehicles.isEmpty()) {
//            IntStream.rangeClosed(1, 10).forEach(i -> {
//                // Get a random vehicle
//                Vehicle randomVehicle = vehicles.get(faker.random().nextInt(vehicles.size()));
//
//                // Generate random coordinates within reasonable bounds
//                double latitude = faker.number().randomDouble(6, 6, 10);  // Sri Lanka roughly
//                double longitude = faker.number().randomDouble(6, 79, 82); // Sri Lanka roughly
//
//                // Generate a random time within the last 30 days
//                LocalDateTime emergencyTime = LocalDateTime.ofInstant(
//                        faker.date().past(30, TimeUnit.DAYS).toInstant(),
//                        ZoneId.systemDefault()
//                );
//
//                Emergency emergency = new Emergency(
//                        randomVehicle,
//                        emergencyTime,
//                        new Emergency.Location(latitude, longitude)
//                );
//                emergency.setHandled(faker.random().nextBoolean());
//
//                emergencyService.createEmergency(emergency);
//            });
//        }
//    }

    @Override
    public void run(String... args) throws Exception {
//        clientsLoader();
//        modelLoader();
//        vehicleLoader();
//        emergencyLoader();
    }
}