package com.hospital.controller;

import com.hospital.model.AppointmentStatus;
import com.hospital.model.Doctor;
import com.hospital.service.AppointmentService;
import com.hospital.service.DoctorService;
import com.hospital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentService appointmentService;

    // Doctor Dashboard
    @GetMapping("/dashboard")
    public String dashboard(Principal principal, Model model) {
        userService.findByEmail(principal.getName()).ifPresent(user -> {
            doctorService.getDoctorByUser(user).ifPresent(doctor -> {
                model.addAttribute("doctor", doctor);
                model.addAttribute("appointments",
                    appointmentService.getAppointmentsByDoctor(doctor));
            });
        });
        return "doctor/dashboard";
    }

    // View appointments
    @GetMapping("/appointments")
    public String viewAppointments(Principal principal, Model model) {
        userService.findByEmail(principal.getName()).ifPresent(user -> {
            doctorService.getDoctorByUser(user).ifPresent(doctor -> {
                model.addAttribute("appointments",
                    appointmentService.getAppointmentsByDoctor(doctor));
            });
        });
        return "doctor/appointments";
    }

    // Confirm appointment
    @GetMapping("/appointments/confirm/{id}")
    public String confirmAppointment(@PathVariable Long id) {
        appointmentService.updateStatus(id, AppointmentStatus.CONFIRMED);
        return "redirect:/doctor/appointments";
    }

    // Cancel appointment
    @GetMapping("/appointments/cancel/{id}")
    public String cancelAppointment(@PathVariable Long id) {
        appointmentService.updateStatus(id, AppointmentStatus.CANCELLED);
        return "redirect:/doctor/appointments";
    }

    // Update profile form
    @GetMapping("/profile")
    public String profileForm(Principal principal, Model model) {
        userService.findByEmail(principal.getName()).ifPresent(user -> {
            doctorService.getDoctorByUser(user).ifPresent(doctor -> {
                model.addAttribute("doctor", doctor);
            });
        });
        return "doctor/profile";
    }

    // Update profile submit
    @PostMapping("/profile")
    public String profileSubmit(@ModelAttribute Doctor doctor) {
        doctorService.saveDoctor(doctor);
        return "redirect:/doctor/dashboard";
    }

}
