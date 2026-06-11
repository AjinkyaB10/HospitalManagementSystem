package com.hospital.controller;

import com.hospital.model.Appointment;
import com.hospital.model.Patient;
import com.hospital.service.AppointmentService;
import com.hospital.service.DoctorService;
import com.hospital.service.PatientService;
import com.hospital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@Controller
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorService doctorService;

    // Patient Dashboard
    @GetMapping("/dashboard")
    public String dashboard(Principal principal, Model model) {
        userService.findByEmail(principal.getName()).ifPresent(user -> {
            patientService.getPatientByUser(user).ifPresent(patient -> {
                model.addAttribute("patient", patient);
                model.addAttribute("appointments",
                    appointmentService.getAppointmentsByPatient(patient));
            });
        });
        return "patient/dashboard";
    }

    // Book appointment form
    @GetMapping("/book-appointment")
    public String bookForm(Model model) {
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("appointment", new Appointment());
        return "patient/book-appointment";
    }

    // Book appointment submit
    @PostMapping("/book-appointment")
    public String bookSubmit(@ModelAttribute Appointment appointment,
                              @RequestParam Long doctorId,
                              Principal principal) {
        userService.findByEmail(principal.getName()).ifPresent(user -> {
            patientService.getPatientByUser(user).ifPresent(patient -> {
                appointment.setPatient(patient);
                doctorService.getDoctorById(doctorId).ifPresent(appointment::setDoctor);
                appointmentService.saveAppointment(appointment);
            });
        });
        return "redirect:/patient/dashboard";
    }

    // View appointments
    @GetMapping("/appointments")
    public String viewAppointments(Principal principal, Model model) {
        userService.findByEmail(principal.getName()).ifPresent(user -> {
            patientService.getPatientByUser(user).ifPresent(patient -> {
                model.addAttribute("appointments",
                    appointmentService.getAppointmentsByPatient(patient));
            });
        });
        return "patient/appointments";
    }

    // Cancel appointment
    @GetMapping("/appointments/cancel/{id}")
    public String cancelAppointment(@PathVariable Long id) {
        appointmentService.updateStatus(id,
            com.hospital.model.AppointmentStatus.CANCELLED);
        return "redirect:/patient/appointments";
    }

    // Update profile form
    @GetMapping("/profile")
    public String profileForm(Principal principal, Model model) {
        userService.findByEmail(principal.getName()).ifPresent(user -> {
            patientService.getPatientByUser(user).ifPresent(patient ->
                model.addAttribute("patient", patient));
        });
        return "patient/profile";
    }

    // Update profile submit
    @PostMapping("/profile")
    public String profileSubmit(@ModelAttribute Patient patient) {
        patientService.savePatient(patient);
        return "redirect:/patient/dashboard";
    }

}	