package sliit.pg09.carcare.completedAppointment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class completedAppointmentService {
    private final completedAppointmentRepository completedAppointmentRepository;

    // Get the first appointment for a vehicle (head of the list)
    public completedAppointment getFirstAppointment(String license) {
        return completedAppointmentRepository.findAll().stream()
                .filter(a -> a.getVehicle().getLicense().equals(license) && a.getPreviousAppointment() == null)
                .findFirst()
                .orElse(null);
    }

    // Get appointment by id (license + completedTime)
    public completedAppointment getAppointmentById(String license, LocalDateTime completedTime) {
        return completedAppointmentRepository.findAll().stream()
                .filter(a -> a.getVehicle().getLicense().equals(license) && a.getId().getCompletedTime().equals(completedTime))
                .findFirst()
                .orElse(null);
    }

    // Insert a new appointment (links are set here)
    public void insertAppointment(completedAppointment appointment, BillingInfo billingInfo) {
        String license = appointment.getVehicle().getLicense();
        completedAppointment first = getFirstAppointment(license);

        if (first == null) {
            appointment.setPreviousAppointment(null);
            appointment.setNextAppointment(null);
            completedAppointmentRepository.save(appointment);
            return;
        }

        completedAppointment last = first;
        while (last.getNextAppointment() != null) {
            last = last.getNextAppointment();
        }

        appointment.setPreviousAppointment(last);
        appointment.setNextAppointment(null);
        appointment.setBillingInfo(billingInfo);
        completedAppointmentRepository.save(appointment);

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
        List<completedAppointment> all = new ArrayList<>();
        completedAppointment current = getFirstAppointment(license);
        while (current != null) {
            all.add(current);
            current = current.getNextAppointment();
        }

        int n = all.size();
        for (int i = 0; i < n - 1; i++) {
            int maxIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (all.get(j).getId().getCompletedTime().isAfter(all.get(maxIdx).getId().getCompletedTime())) {
                    maxIdx = j;
                }
            }
            if (maxIdx != i) {
                completedAppointment temp = all.get(i);
                all.set(i, all.get(maxIdx));
                all.set(maxIdx, temp);
            }
        }
        return all;
    }

    public List<completedAppointment> getAllAppointments() {
        return completedAppointmentRepository.findAll();
    }

    public List<completedAppointment> getLatest3Appointments(String license) {
        List<completedAppointment> sorted = getAllAppointmentsSorted(license);
        return sorted.subList(0, Math.min(3, sorted.size()));
    }

    public List<completedAppointment> searchAppointments(String search) {
        return completedAppointmentRepository.findAll().stream()
                .filter(appointment -> appointment.getVehicle().getClient().getName().toLowerCase().contains(search.toLowerCase())
                        || appointment.getVehicle().getLicense().toLowerCase().contains(search.toLowerCase())
                        || appointment.getServiceNames().stream().anyMatch(service -> service.toLowerCase().contains(search.toLowerCase()))
                        || appointment.getId().getCompletedTime().toString().contains(search))
                .collect(Collectors.toList());
    }
}