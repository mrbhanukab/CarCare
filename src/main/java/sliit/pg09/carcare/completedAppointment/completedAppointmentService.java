package sliit.pg09.carcare.completedAppointment;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class completedAppointmentService {
    private final completedAppointmentRepository completedAppointmentRepository;

    public completedAppointmentService(completedAppointmentRepository completedAppointmentRepository) {
        this.completedAppointmentRepository = completedAppointmentRepository;
    }

    // Get the first appointment for a vehicle (head of the list)
    public completedAppointment getFirstAppointment(String license) {
        return completedAppointmentRepository.findAll().stream().filter(a -> a.getVehicle().getLicense().equals(license) && a.getPreviousAppointment() == null).findFirst().orElse(null);
    }

    // Get appointment by id (license + completedTime)
    public completedAppointment getAppointmentById(String license, LocalDateTime completedTime) {
        return completedAppointmentRepository.findAll().stream().filter(a -> a.getVehicle().getLicense().equals(license) && a.getId().getCompletedTime().equals(completedTime)).findFirst().orElse(null);
    }

    // Insert a new appointment (links are set here)
    public void insertAppointment(completedAppointment appointment) {
        String license = appointment.getVehicle().getLicense();
        completedAppointment first = getFirstAppointment(license);

        if (first == null) {
            // First appointment for this vehicle
            appointment.setPreviousAppointment(null);
            appointment.setNextAppointment(null);
            completedAppointmentRepository.save(appointment);
            return;
        }

        // Find the last node
        completedAppointment last = first;
        while (last.getNextAppointment() != null) {
            last = last.getNextAppointment();
        }

        // Save the new appointment first (so it gets an ID)
        appointment.setPreviousAppointment(last);
        appointment.setNextAppointment(null);
        completedAppointmentRepository.save(appointment);

        // Now update the last node to point to the new appointment
        last.setNextAppointment(appointment);
        completedAppointmentRepository.save(last);
    }

    // Delete an appointment (update links)
    public void deleteAppointment(String license, LocalDateTime completedTime) {
        completedAppointment toDelete = getAppointmentById(license, completedTime);
        if (toDelete == null) return;

        completedAppointment prev = toDelete.getPreviousAppointment();
        completedAppointment next = toDelete.getNextAppointment();

        if (prev != null) {
            prev.setNextAppointment(next);
            completedAppointmentRepository.save(prev);
        }
        if (next != null) {
            next.setPreviousAppointment(prev);
            completedAppointmentRepository.save(next);
        }

        completedAppointmentRepository.delete(toDelete);
    }

    // Get all appointments for a vehicle, sorted by completedTime using selection sort
    public List<completedAppointment> getAllAppointmentsSorted(String license) {
        // Collect all appointments for the vehicle
        List<completedAppointment> all = new ArrayList<>();
        completedAppointment current = getFirstAppointment(license);
        while (current != null) {
            all.add(current);
            current = current.getNextAppointment();
        }

        // Selection sort by completedTime
        int n = all.size();
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (all.get(j).getId().getCompletedTime().isBefore(all.get(minIdx).getId().getCompletedTime())) {
                    minIdx = j;
                }
            }
            if (minIdx != i) {
                completedAppointment temp = all.get(i);
                all.set(i, all.get(minIdx));
                all.set(minIdx, temp);
            }
        }
        return all;
    }
}