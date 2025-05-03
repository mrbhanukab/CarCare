package sliit.pg09.carcare.emergency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class EmergencyController {
    @Autowired
    EmergencyService emergencyService;
}
