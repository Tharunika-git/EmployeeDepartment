package org.example;

import org.example.entity.Department;
import org.example.entity.Employee;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {

    public static void main(String[] args) {

        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("companyPU");

        EntityManager em = emf.createEntityManager();

        try {

            // ==========================================
            // 1. INSERT DATA
            // ==========================================

            em.getTransaction().begin();

            Department it = new Department("IT");
            Department hr = new Department("HR");

            Employee e1 = new Employee("Rahul", 50000);
            Employee e2 = new Employee("Priya", 60000);
            Employee e3 = new Employee("Amit", 55000);

            // Add employees to IT department
            it.addEmployee(e1);
            it.addEmployee(e2);
            it.addEmployee(e3);

            // Save departments
            em.persist(it);
            em.persist(hr);

            // Save employees
            em.persist(e1);
            em.persist(e2);
            em.persist(e3);

            em.getTransaction().commit();

            System.out.println("=================================");
            System.out.println("DATA SAVED SUCCESSFULLY");
            System.out.println("=================================");


            // ==========================================
            // 2. READ EMPLOYEE -> DEPARTMENT
            // ==========================================

            Employee rahul =
                    em.find(Employee.class, e1.getId());

            System.out.println("\n--- Employee to Department ---");

            System.out.println(
                    rahul.getName()
                            + " works in "
                            + rahul.getDepartment().getName()
            );


            // ==========================================
            // 3. READ DEPARTMENT -> EMPLOYEES
            // ==========================================

            Department itDepartment =
                    em.find(Department.class, it.getId());

            System.out.println("\n--- Department to Employees ---");

            for (Employee employee :
                    itDepartment.getEmployees()) {

                System.out.println(
                        employee.getName()
                                + " - Salary: "
                                + employee.getSalary()
                );
            }


            // ==========================================
            // 4. UPDATE EMPLOYEE'S DEPARTMENT
            // Rahul: IT -> HR
            // ==========================================

            em.getTransaction().begin();

            Department hrDepartment =
                    em.find(Department.class, hr.getId());

            rahul.setDepartment(hrDepartment);

            em.getTransaction().commit();

            System.out.println(
                    "\nRahul's department updated from IT to HR."
            );


            // ==========================================
            // 5. UPDATE EMPLOYEE DETAILS
            // ==========================================

            em.getTransaction().begin();

            Employee priya =
                    em.find(Employee.class, e2.getId());

            priya.setName("Priya Sharma");
            priya.setSalary(65000);

            em.getTransaction().commit();

            System.out.println(
                    "Priya's details updated successfully."
            );


            // ==========================================
            // 6. DELETE EMPLOYEE
            // Delete Amit
            // ==========================================

            em.getTransaction().begin();

            Employee amit =
                    em.find(Employee.class, e3.getId());

            em.remove(amit);

            em.getTransaction().commit();

            System.out.println(
                    "Amit deleted successfully."
            );


            // ==========================================
            // FINAL MESSAGE
            // ==========================================

            System.out.println("\n=================================");
            System.out.println("ALL OPERATIONS COMPLETED");
            System.out.println("=================================");

        } catch (Exception e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            e.printStackTrace();

        } finally {

            em.close();
            emf.close();
        }
    }
}