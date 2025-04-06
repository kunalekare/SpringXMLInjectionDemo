package com.hospital.service;

import com.hospital.entity.Appointment;
import com.hospital.entity.Doctor;
import com.hospital.entity.Nurse;
import com.hospital.entity.Patient;
import com.hospital.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

public class AdminService {
    public void addDoctor(String name, String specialization) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Doctor doctor = new Doctor(name, specialization);
        session.save(doctor);

        tx.commit();
        session.close();
    }

    public void addNurse(String name, String department) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Nurse nurse = new Nurse(name, department);
        session.save(nurse);

        tx.commit();
        session.close();
    }

    public void viewAllDoctorAppointments() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Appointment> appointments = session.createQuery("FROM Appointment", Appointment.class).list();

        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
        } else {
            for (Appointment appointment : appointments) {
                System.out.println("Appointment ID: " + appointment.getId() + ", Doctor: " + appointment.getDoctor().getName() + ", Patient: " + appointment.getPatient().getName() + ", Date: " + appointment.getDate());
            }
        }
        session.close();
    }
}
