package sliit.pg09.carcare.common.utills;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sliit.pg09.carcare.client.ClientService;
import sliit.pg09.carcare.common.ServiceType;
import sliit.pg09.carcare.completedAppointment.BillingInfo;
import sliit.pg09.carcare.completedAppointment.completedAppointment;
import sliit.pg09.carcare.completedAppointment.completedAppointmentService;
import sliit.pg09.carcare.emergency.EmergencyService;
import sliit.pg09.carcare.vehicle.CarModel.CarModel;
import sliit.pg09.carcare.vehicle.CarModel.CarModelService;
import sliit.pg09.carcare.vehicle.Vehicle;
import sliit.pg09.carcare.vehicle.VehicleService;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.IntStream;

@Component
public class SampleDataLoader implements CommandLineRunner {
    private final Faker faker;
    private final EmergencyService emergencyService;
    private final VehicleService vehicleService;
    private final ClientService clientService;
    private final CarModelService carModelService;
    private final completedAppointmentService completedAppointmentService;

    public SampleDataLoader(
            ClientService clientService,
            CarModelService carModelService,
            VehicleService vehicleService,
            EmergencyService emergencyService, completedAppointmentService completedAppointmentService) {
        this.clientService = clientService;
        this.carModelService = carModelService;
        this.vehicleService = vehicleService;
        this.emergencyService = emergencyService;
        this.completedAppointmentService = completedAppointmentService;
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


    public void completedAppointmentsLoader() {
        String[] licenses = {"BB-001", "BB-002"};
        for (String license : licenses) {
            Vehicle vehicle = vehicleService.getVehicleByLicense(license);
            for (int i = 0; i < 10; i++) {
                LocalDateTime completedTime = LocalDateTime.now().minusDays(faker.number().numberBetween(1, 365));
                Set<ServiceType> services = Set.of(ServiceType.values()[faker.random().nextInt(ServiceType.values().length)]);
                BillingInfo billing = new BillingInfo();
                billing.setLabour(faker.number().randomDouble(2, 1000, 5000));
                billing.setParts(faker.number().randomDouble(2, 500, 2000));
                billing.setAdditional(faker.number().randomDouble(2, 0, 500));
                billing.setDiscount(faker.number().randomDouble(2, 0, 300));
                String notes = faker.lorem().sentence();

                completedAppointment appointment = new completedAppointment();
                appointment.setAppointmentDetails(vehicle, completedTime);
                appointment.setServices(services);
                appointment.setBillingInfo(billing);
                appointment.setNotes(notes);

                completedAppointmentService.insertAppointment(appointment);
            }
        }
    }

    @Override
    public void run(String... args) throws Exception {
//        completedAppointmentsLoader();


//        // Test selection sort and print as table
//        var sorted = completedAppointmentService.getAllAppointmentsSorted("BB-001");
//        System.out.printf("%-20s | %-30s | %-10s\n", "License", "Completed Time", "Notes");
//        System.out.println("--------------------------------------------------------------------------");
//        for (var appt : sorted) {
//            System.out.printf("%-20s | %-30s | %-10s\n",
//                    appt.getVehicle().getLicense(),
//                    appt.getId().getCompletedTime(),
//                    appt.getNotes());
//        }
//        clientsLoader();
//        modelLoader();
//        vehicleLoader();
//        emergencyLoader();
    }
}