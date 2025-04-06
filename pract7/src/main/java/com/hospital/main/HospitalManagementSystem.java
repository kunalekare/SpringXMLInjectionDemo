package com.hospital.main;

import com.hospital.service.AdminService;
import com.hospital.service.PatientService;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class HospitalManagementSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AdminService adminService = new AdminService();
        PatientService patientService = new PatientService();

        while (true) {
            System.out.println("\nHospital Management System");
            System.out.println("1. Add Doctor");
            System.out.println("2. Add Nurse");
            System.out.println("3. View All Doctor Appointments (Admin)");
            System.out.println("4. Book Appointment (Patient)");
            System.out.println("5. Cancel Appointment (Patient)");
            System.out.println("6. View My Appointments (Patient)");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter Doctor Name: ");
                    String docName = sc.nextLine();
                    System.out.print("Enter Specialization: ");
                    String specialization = sc.nextLine();
                    adminService.addDoctor(docName, specialization);
                    System.out.println("Doctor added successfully!");
                    break;
                case 2:
                    System.out.print("Enter Nurse Name: ");
                    String nurseName = sc.nextLine();
                    System.out.print("Enter Department: ");
                    String department = sc.nextLine();
                    adminService.addNurse(nurseName, department);
                    System.out.println("Nurse added successfully!");
                    break;
                case 3:
                    adminService.viewAllDoctorAppointments();
                    break;
                case 4:
                    System.out.print("Enter Patient ID: ");
                    int patientId = sc.nextInt();
                    System.out.print("Enter Doctor ID: ");
                    int doctorId = sc.nextInt();
                    sc.nextLine(); // Consume newline
                    System.out.print("Enter Appointment Date (yyyy-MM-dd): ");
                    String dateStr = sc.nextLine();
                    try {
                        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
                        patientService.bookAppointment(doctorId, patientId, date); // ✅ Fixed here
                        System.out.println("Appointment booked successfully!");
                    } catch (Exception e) {
                        System.out.println("Invalid date format! Please use yyyy-MM-dd.");
                    }
                    break;
                case 5:
                    System.out.print("Enter Appointment ID to Cancel: ");
                    int appointmentId = sc.nextInt();
                    patientService.cancelAppointment(appointmentId);
                    System.out.println("Appointment cancelled successfully!");
                    break;
                case 6:
                    System.out.print("Enter Patient ID: ");
                    int patId = sc.nextInt();
                    patientService.viewAppointments(patId);
                    break;
                case 7:
                    System.out.println("Exiting...");
                    sc.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }
}
