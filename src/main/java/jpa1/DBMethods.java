package jpa1;

import javax.persistence.*;
import java.util.List;
import java.util.Scanner;

public class DBMethods {
    Scanner sc = new Scanner(System.in);
    private EntityManager em;

    public DBMethods() {
    }

    public void start() {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPATest");
            em = emf.createEntityManager();
            try {
                while (true) {
                    System.out.println("1: add apartment");
                    System.out.println("2: add random apartments");
                    System.out.println("3: search apartment");
                    System.out.println("4: exit");
                    System.out.print("-> ");

                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                            addApartment();
                            break;
                        case "2":
                            fillRandomApartments();
                            break;
                        case "3":
                            viewApartments();
                            break;
                        case "4":
                            return;
                        default:
                            System.out.println("Incorrect data, try again.");
                            break;
                    }
                }
            } finally {
                sc.close();
                em.close();
                emf.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addApartment() {
        System.out.print("Enter district: ");
        String district = sc.nextLine();
        System.out.print("Enter address: ");
        String address = sc.nextLine();
        System.out.print("Enter apartment area: ");
        double area = Double.parseDouble(sc.nextLine());
        System.out.print("Enter number of rooms: ");
        int rooms = Integer.parseInt(sc.nextLine());
        System.out.print("Enter apartment price: ");
        double price = Double.parseDouble(sc.nextLine());

        em.getTransaction().begin();
        try {
            Apartment a = new Apartment(district, address, area, rooms, price);
            em.persist(a);
            em.getTransaction().commit();

            System.out.println("Success! Your apartment ID is: " + a.getId());
        } catch (Exception ex) {
            em.getTransaction().rollback();
            System.out.println("Error! Try again.");
        }
    }

    private void fillRandomApartments() {
        System.out.println("Enter apartments count: ");
        int count = Integer.parseInt(sc.nextLine());
        RandomValue r = new RandomValue();
        em.getTransaction().begin();
        try {
            for (int i = 0; i < count; i++) {
                Apartment a = new Apartment(
                        r.randomDistrict(), r.randomAddress(), r.randomArea(), r.randomRooms(), r.randomPrice());
                em.persist(a);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Error! Try again.");
        }
    }

    private void viewApartments() {
        showParam();
        String select = sc.nextLine();
        switch (select) {
            case "1" -> {
                System.out.println("Enter district: ");
                String district = sc.nextLine();
                searchApartments("district", district);
            }
            case "2" -> {
                System.out.println("Enter address: ");
                String address = sc.nextLine();
                searchApartments("address", address);
            }
            case "3" -> {
                System.out.println("Enter minimum area of room: ");
                double minArea = Double.parseDouble(sc.nextLine());
                System.out.println("Enter maximum area of room: ");
                double maxArea = Double.parseDouble(sc.nextLine());
                searchApartments("area", minArea, maxArea);
            }
            case "4" -> {
                System.out.println("Enter minimum rooms: ");
                int minRooms = Integer.parseInt(sc.nextLine());
                System.out.println("Enter maximum rooms: ");
                int maxRooms = Integer.parseInt(sc.nextLine());
                searchApartments("rooms", minRooms, maxRooms);
            }
            case "5" -> {
                System.out.println("Enter minimum price: ");
                double minPrice = Double.parseDouble(sc.nextLine());
                System.out.println("Enter maximum price: ");
                double maxPrice = Double.parseDouble(sc.nextLine());
                searchApartments("price", minPrice, maxPrice);
            }
            case "6" -> searchApartments();
        }


    }

    private void searchApartments(Object... args) {
        Query query;
        if (args.length == 0) {
            query = em.createQuery("SELECT a FROM Apartment a", Apartment.class);
            List<Apartment> list = (List<Apartment>) query.getResultList();

            for (Apartment a : list)
                System.out.println(a);
        } else if (args.length == 2) {
            query = em.createQuery(
                    "SELECT a FROM Apartment a WHERE a." + args[0].toString() + " = :value", Apartment.class);
            query.setParameter("value", args[1]);
            List<Apartment> list = (List<Apartment>) query.getResultList();

            for (Apartment a : list)
                System.out.println(a);
        } else if (args.length == 3) {
            query = em.createQuery(
                    "SELECT a FROM Apartment a WHERE a." + args[0].toString() + " BETWEEN :start AND :end", Apartment.class);
            query.setParameter("start", args[1]);
            query.setParameter("end", args[2]);
            List<Apartment> list = (List<Apartment>) query.getResultList();

            for (Apartment a : list)
                System.out.println(a);
        } else {
            System.out.println("Incorrect data. Try again");
        }

    }

    private void showParam() {
        System.out.println("""
                Choose your search options:
                1. By district
                2. By address
                3. By area
                4. By rooms
                5. By price
                6. View all apartments
                ->\s""");
    }
}
