package com.hospital.service;

import com.hospital.entity.Appointment;
import com.hospital.entity.Doctor;
import com.hospital.entity.Patient;
import com.hospital.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PatientService {


    public void bookAppointment(int doctorId, int patientId, Date date) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        try {

            Date today = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date todayWithoutTime = sdf.parse(sdf.format(today));

            if (date.before(todayWithoutTime)) {
                System.out.println(" Error: Appointment date must be in the future.");
                tx.rollback();
                return;
            }


            Doctor doctor = session.get(Doctor.class, doctorId);
            if (doctor == null) {
                System.out.println("Error: Doctor not found!");
                tx.rollback();
                return;
            }


            Patient patient = session.get(Patient.class, patientId);
            if (patient == null) {
                System.out.println("ℹ️ Patient not found! Adding new patient...");
                patient = new Patient();
                patient.setName("Patient " + patientId);
                session.save(patient); //  Correctly save new patient
                session.flush(); //  Ensure it's saved immediately
                System.out.println(" New patient added: " + patient.getName());
            }



            Query<Appointment> query = session.createQuery(
                    "FROM Appointment a WHERE a.doctor.id = :doctorId AND a.date = :date", Appointment.class);
            query.setParameter("doctorId", doctorId);
            query.setParameter("date", date);
            List<Appointment> existingAppointments = query.list();

            if (!existingAppointments.isEmpty()) {
                System.out.println(" Error: Doctor is already booked on this date!");
                tx.rollback();
                return;
            }


            Appointment appointment = new Appointment(doctor, patient, date);
            session.persist(appointment);
            tx.commit();
            System.out.println("✅ Appointment booked successfully for " + sdf.format(date) + "!");

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println("Error: " + e.getMessage());
        } finally {
            session.close();
        }
    }


    public void cancelAppointment(int appointmentId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        try {
            Appointment appointment = session.get(Appointment.class, appointmentId);
            if (appointment == null) {
                System.out.println(" Error: Appointment not found!");
                tx.rollback();
                return;
            }

            session.remove(appointment);
            tx.commit();
            System.out.println(" Appointment ID " + appointmentId + " canceled successfully!");

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println(" Error: " + e.getMessage());
        } finally {
            session.close();
        }
    }


    public void viewAppointments(int patientId) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            Query<Appointment> query = session.createQuery(
                    "FROM Appointment a WHERE a.patient.id = :patientId", Appointment.class);
            query.setParameter("patientId", patientId);
            List<Appointment> appointments = query.list();

            if (appointments.isEmpty()) {
                System.out.println(" No appointments found for patient ID: " + patientId);
                return;
            }

            System.out.println("Your Appointments:");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (Appointment a : appointments) {
                System.out.println(" Appointment ID: " + a.getId() +
                        " | Doctor: " + a.getDoctor().getName() +
                        " | Specialization: " + a.getDoctor().getSpecialization() +
                        " | Date: " + sdf.format(a.getDate()));
            }
        } catch (Exception e) {
            System.out.println(" Error: " + e.getMessage());
        } finally {
            session.close();
        }
    }
}
