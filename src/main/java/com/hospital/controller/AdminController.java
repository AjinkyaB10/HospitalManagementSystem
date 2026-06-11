package com.hospital.controller;

import com.hospital.model.Doctor;
import com.hospital.model.Role;
import com.hospital.model.User;
import com.hospital.service.AppointmentService;
import com.hospital.service.DoctorService;
import com.hospital.service.PatientService;
import com.hospital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private AppointmentService appointmentService;

    // Admin Dashboard
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalDoctors", doctorService.getAllDoctors().size());
        model.addAttribute("totalPatients", patientService.getAllPatients().size());
        model.addAttribute("totalAppointments", appointmentService.getAllAppointments().size());
        return "admin/dashboard";
    }

    // View all doctors
    @GetMapping("/doctors")
    public String viewDoctors(Model model) {
        model.addAttribute("doctors", doctorService.getAllDoctors());
        return "admin/doctors";
    }

    // Add doctor form
    @GetMapping("/doctors/add")
    public String addDoctorForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("doctor", new Doctor());
        return "admin/add-doctor";
    }

    // Add doctor submit
    @PostMapping("/doctors/add")
    public String addDoctorSubmit(@ModelAttribute User user,
                                   @ModelAttribute Doctor doctor) {
        user.setRole(Role.DOCTOR);
        User savedUser = userService.saveUser(user);
        doctor.setUser(savedUser);
        doctorService.saveDoctor(doctor);
        return "redirect:/admin/doctors";
    }

    // Delete doctor
    @GetMapping("/doctors/delete/{id}")
    public String deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return "redirect:/admin/doctors";
    }

    // View all patients
    @GetMapping("/patients")
    public String viewPatients(Model model) {
        model.addAttribute("patients", patientService.getAllPatients());
        return "admin/patients";
    }

    // View all appointments
    @GetMapping("/appointments")
    public String viewAppointments(Model model) {
        model.addAttribute("appointments", appointmentService.getAllAppointments());
        return "admin/appointments";
    }

}
