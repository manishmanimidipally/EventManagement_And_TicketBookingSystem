package main.com.eventbooking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

import main.com.eventbooking.model.Admin;
import main.com.eventbooking.model.Booking;
import main.com.eventbooking.model.Event;
import main.com.eventbooking.model.Payment;
import main.com.eventbooking.model.Seat;
import main.com.eventbooking.model.User;
import main.com.eventbooking.service.AdminService;
import main.com.eventbooking.service.BookingService;
import main.com.eventbooking.service.EventService;
import main.com.eventbooking.service.PaymentService;
import main.com.eventbooking.service.UserService;
import main.com.eventbooking.util.IDGenerator;
import main.com.eventbooking.dao.SeatDAO;
import main.com.eventbooking.daoimpl.SeatDAOImpl;



public class Main {

    private static final Scanner scanner =
            new Scanner(System.in);

    private static final UserService userService =
            new UserService();

    private static final AdminService adminService =
            new AdminService();

    private static final EventService eventService =
            new EventService();

    private static final BookingService bookingService =
            new BookingService();

    private static final PaymentService paymentService =
            new PaymentService();

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("     EVENT TICKET BOOKING SYSTEM");
        System.out.println("======================================");

        while (true) {

            System.out.println("\n--------- MAIN MENU ---------");
            System.out.println("1. User Registration");
            System.out.println("2. User Login");
            System.out.println("3. Admin Login");
            System.out.println("4. View All Events");
            System.out.println("5. View Upcoming Events");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");

            int choice = readInt();

            try {

                switch (choice) {

                    case 1:
                        registerUser();
                        break;

                    case 2:
                        userLogin();
                        break;

                    case 3:
                        adminLogin();
                        break;

                    case 4:
                        viewAllEvents();
                        break;

                    case 5:
                        viewUpcomingEvents();
                        break;

                    case 6:
                        System.out.println(
                                "Thank you for using Event Ticket Booking System!"
                        );

                        scanner.close();
                        return;

                    default:
                        System.out.println(
                                "Invalid choice. Please try again."
                        );
                }

            } catch (Exception e) {

                System.out.println(
                        "Error: " + e.getMessage()
                );
            }
        }
    }

    // =====================================================
    // USER REGISTRATION
    // =====================================================

    private static void registerUser() {

        System.out.println("\n====== USER REGISTRATION ======");

        System.out.print("Enter Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        System.out.print("Enter Phone: ");
        String phone = scanner.nextLine();

        int userId = IDGenerator.generateUserId();

        User user = new User(
                userId,
                name,
                email,
                password,
                phone
        );

        boolean result =
                userService.registerUser(user);

        if (result) {

            System.out.println(
                    "User registered successfully!"
            );

            System.out.println(
                    "Your User ID: " + user.getUserId()
            );

        } else {

            System.out.println(
                    "User registration failed."
            );
        }
    }

    // =====================================================
    // USER LOGIN
    // =====================================================

    private static void userLogin() {

        System.out.println("\n====== USER LOGIN ======");

        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        User user =
                userService.login(email, password);

        System.out.println(
                "\nWelcome, " + user.getName() + "!"
        );

        userMenu(user);
    }

    // =====================================================
    // USER MENU
    // =====================================================

    private static void userMenu(User user) {

        while (true) {

            System.out.println("\n====== USER MENU ======");

            System.out.println("1. View All Events");
            System.out.println("2. View Event");
            System.out.println("3. View Available Seats");
            System.out.println("4. Book Ticket");
            System.out.println("5. Make Payment");
            System.out.println("6. View My Bookings");
            System.out.println("7. Cancel Booking");
            System.out.println("8. Logout");

            System.out.print("Enter your choice: ");

            int choice = readInt();

            try {

                switch (choice) {

                    case 1:
                        viewAllEvents();
                        break;

                    case 2:
                        viewEvent();
                        break;

                    case 3:
                        viewAvailableSeats();
                        break;

                    case 4:
                        bookTicket(user);
                        break;

                    case 5:
                        makePayment();
                        break;

                    case 6:
                        viewMyBookings(user);
                        break;

                    case 7:
                        cancelBooking();
                        break;

                    case 8:
                        System.out.println(
                                "Logged out successfully."
                        );
                        return;

                    default:
                        System.out.println(
                                "Invalid choice."
                        );
                }

            } catch (Exception e) {

                System.out.println(
                        "Error: " + e.getMessage()
                );
            }
        }
    }

    // =====================================================
    // ADMIN LOGIN
    // =====================================================

    private static void adminLogin() {

        System.out.println("\n====== ADMIN LOGIN ======");

        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        Admin admin =
                adminService.login(email, password);

        System.out.println(
                "\nWelcome Admin, " +
                        admin.getName() + "!"
        );

        adminMenu();
    }

    // =====================================================
    // ADMIN MENU
    // =====================================================

    private static void adminMenu() {

        while (true) {

            System.out.println("\n====== ADMIN MENU ======");

            System.out.println("1. Add Event");
            System.out.println("2. View All Events");
            System.out.println("3. View Event");
            System.out.println("4. Update Event");
            System.out.println("5. Delete Event");
            System.out.println("6. Add Seat");
            System.out.println("7. View Event Seats");
            System.out.println("8. View All Payments");
            System.out.println("9. Logout");

            System.out.print("Enter your choice: ");

            int choice = readInt();

            try {

                switch (choice) {

                    case 1:
                        addEvent();
                        break;

                    case 2:
                        viewAllEvents();
                        break;

                    case 3:
                        viewEvent();
                        break;

                    case 4:
                        updateEvent();
                        break;

                    case 5:
                        deleteEvent();
                        break;

                    case 6:
                        addSeat();
                        break;

                    case 7:
                        viewEventSeats();
                        break;

                    case 8:
                        viewAllPayments();
                        break;

                    case 9:
                        System.out.println(
                                "Admin logged out."
                        );
                        return;

                    default:
                        System.out.println(
                                "Invalid choice."
                        );
                }

            } catch (Exception e) {

                System.out.println(
                        "Error: " + e.getMessage()
                );
            }
        }
    }

    // =====================================================
    // VIEW ALL EVENTS
    // =====================================================

    private static void viewAllEvents() {

        System.out.println("\n====== ALL EVENTS ======");

        List<Event> events =
                eventService.getAllEvents();

        if (events.isEmpty()) {

            System.out.println(
                    "No events available."
            );

            return;
        }

        for (Event event : events) {

            printEvent(event);
        }
    }

    // =====================================================
    // VIEW UPCOMING EVENTS
    // =====================================================

    private static void viewUpcomingEvents() {

        System.out.println(
                "\n====== UPCOMING EVENTS ======"
        );

        List<Event> events =
                eventService.getUpcomingEvents();

        if (events.isEmpty()) {

            System.out.println(
                    "No upcoming events."
            );

            return;
        }

        for (Event event : events) {

            printEvent(event);
        }
    }

    // =====================================================
    // VIEW SINGLE EVENT
    // =====================================================

    private static void viewEvent() {

        System.out.print("Enter Event ID: ");

        int eventId = readInt();

        Event event =
                eventService.getEventById(eventId);

        System.out.println("\n====== EVENT DETAILS ======");

        printEvent(event);
    }

    // =====================================================
    // ADD EVENT
    // =====================================================

    private static void addEvent() {

        System.out.println("\n====== ADD EVENT ======");

        System.out.print("Event Name: ");
        String name = scanner.nextLine();

        System.out.print("Event Type: ");
        String type = scanner.nextLine();

        System.out.print("Venue: ");
        String venue = scanner.nextLine();

        System.out.print("Event Date (YYYY-MM-DD): ");
        LocalDate date =
                LocalDate.parse(scanner.nextLine());

        System.out.print("Event Time (HH:MM): ");
        LocalTime time =
                LocalTime.parse(scanner.nextLine());

        System.out.print("Ticket Price: ");
        double price = readDouble();

        System.out.print("Total Seats: ");
        int totalSeats = readInt();

        Event event = new Event(
                name,
                type,
                venue,
                date,
                time,
                price,
                totalSeats
        );

        boolean result =
                eventService.addEvent(event);

        if (result) {

            System.out.println(
                    "Event added successfully!"
            );

            System.out.println(
                    "Event ID: " + event.getEventId()
            );

        } else {

            System.out.println(
                    "Failed to add event."
            );
        }
    }

    // =====================================================
    // UPDATE EVENT
    // =====================================================

    private static void updateEvent() {

        System.out.print("Enter Event ID: ");

        int eventId = readInt();

        Event event =
                eventService.getEventById(eventId);

        System.out.println(
                "\nEnter new event details:"
        );

        System.out.print("Event Name: ");
        event.setEventName(scanner.nextLine());

        System.out.print("Event Type: ");
        event.setEventType(scanner.nextLine());

        System.out.print("Venue: ");
        event.setVenue(scanner.nextLine());

        System.out.print("Event Date (YYYY-MM-DD): ");

        event.setEventDate(
                LocalDate.parse(scanner.nextLine())
        );

        System.out.print("Event Time (HH:MM): ");

        event.setEventTime(
                LocalTime.parse(scanner.nextLine())
        );

        System.out.print("Ticket Price: ");

        event.setTicketPrice(
                readDouble()
        );

        System.out.print("Total Seats: ");

        event.setTotalSeats(
                readInt()
        );

        boolean result =
                eventService.updateEvent(event);

        if (result) {

            System.out.println(
                    "Event updated successfully."
            );

        } else {

            System.out.println(
                    "Event update failed."
            );
        }
    }

    // =====================================================
    // DELETE EVENT
    // =====================================================

    private static void deleteEvent() {

        System.out.print("Enter Event ID: ");

        int eventId = readInt();

        boolean result =
                eventService.deleteEvent(eventId);

        if (result) {

            System.out.println(
                    "Event deleted successfully."
            );

        } else {

            System.out.println(
                    "Event deletion failed."
            );
        }
    }

    // =====================================================
    // ADD SEAT
    // =====================================================
    private static final SeatDAO seatDAO = new SeatDAOImpl();
    private static void addSeat() {

        System.out.println("\n====== ADD SEAT ======");

        System.out.print("Enter Event ID: ");
        int eventId = readInt();

        // Check whether event exists
        Event event = eventService.getEventById(eventId);

        if (event == null) {
            System.out.println("Event not found with ID: " + eventId);
            return;
        }

        System.out.print("Seat Number: ");
        String seatNumber = scanner.nextLine();

        System.out.print("Seat Type: ");
        String seatType = scanner.nextLine();

        System.out.print("Seat Price: ");
        double price = readDouble();

        Seat seat = new Seat(
                eventId,
                seatNumber,
                seatType,
                price,
                true
        );

        seat.setSeatId(IDGenerator.generateSeatId());

        boolean result = seatDAO.addSeat(seat);

        if (result) {
            System.out.println("\nSeat added successfully!");
            System.out.println("Seat ID: " + seat.getSeatId());
        } else {
            System.out.println("\nFailed to add seat.");
        }
    }
    // =====================================================
    // VIEW AVAILABLE SEATS
    // =====================================================

    private static void viewAvailableSeats() {

        System.out.print("Enter Event ID: ");

        int eventId = readInt();

        List<Seat> seats =
                eventService.getAvailableSeats(eventId);

        System.out.println(
                "\n====== AVAILABLE SEATS ======"
        );

        if (seats.isEmpty()) {

            System.out.println(
                    "No seats available."
            );

            return;
        }

        for (Seat seat : seats) {

            System.out.println(
                    "Seat ID: " + seat.getSeatId() +
                    " | Number: " + seat.getSeatNumber() +
                    " | Type: " + seat.getSeatType() +
                    " | Price: ₹" + seat.getPrice()
            );
        }
    }

    // =====================================================
    // VIEW ALL EVENT SEATS
    // =====================================================

    private static void viewEventSeats() {

        System.out.print("Enter Event ID: ");

        int eventId = readInt();

        List<Seat> seats =
                eventService
                        .getAvailableSeats(eventId);

        System.out.println(
                "\n====== AVAILABLE SEATS ======"
        );

        for (Seat seat : seats) {

            System.out.println(
                    seat
            );
        }
    }

    // =====================================================
    // BOOK TICKET
    // =====================================================

    private static void bookTicket(User user) {

        System.out.println("\n====== BOOK TICKET ======");

        System.out.print("Enter Event ID: ");
        int eventId = readInt();

        System.out.print("Enter Seat ID: ");
        int seatId = readInt();

        int bookingId = bookingService.createBooking(
                user.getUserId(),
                eventId,
                seatId
        );

        if (bookingId > 0) {

            System.out.println("\nTicket booked successfully!");

            System.out.println(
                    "Your Booking ID: " + bookingId
            );

            System.out.println(
                    "Please complete payment."
            );

        } else {

            System.out.println(
                    "Ticket booking failed."
            );
        }
    }
    // =====================================================
    // VIEW MY BOOKINGS
    // =====================================================

    private static void viewMyBookings(User user) {

        System.out.println(
                "\n====== MY BOOKINGS ======"
        );

        List<Booking> bookings =
                bookingService.getBookingsByUser(
                        user.getUserId()
                );

        if (bookings.isEmpty()) {

            System.out.println(
                    "You have no bookings."
            );

            return;
        }

        for (Booking booking : bookings) {

            System.out.println(
                    booking
            );
        }
    }

    // =====================================================
    // CANCEL BOOKING
    // =====================================================

    private static void cancelBooking() {

        System.out.print("Enter Booking ID: ");

        int bookingId = readInt();

        boolean result =
                bookingService.cancelBooking(
                        bookingId
                );

        if (result) {

            System.out.println(
                    "Booking cancelled successfully."
            );

        } else {

            System.out.println(
                    "Booking cancellation failed."
            );
        }
    }

    // =====================================================
    // MAKE PAYMENT
    // =====================================================

    private static void makePayment() {

        System.out.println(
                "\n====== MAKE PAYMENT ======"
        );

        System.out.print("Enter Booking ID: ");

        int bookingId = readInt();

        System.out.print(
                "Payment Method (UPI/CARD/CASH): "
        );

        String paymentMethod =
                scanner.nextLine();

        boolean result =
                paymentService.makePayment(
                        bookingId,
                        paymentMethod
                );

        if (result) {

            System.out.println(
                    "Payment successful!"
            );

            Payment payment =
                    paymentService
                            .getPaymentByBookingId(
                                    bookingId
                            );

            System.out.println(
                    "Transaction ID: " +
                            payment.getTransactionId()
            );

        } else {

            System.out.println(
                    "Payment failed."
            );
        }
    }

    // =====================================================
    // VIEW ALL PAYMENTS
    // =====================================================

    private static void viewAllPayments() {

        System.out.println(
                "\n====== ALL PAYMENTS ======"
        );

        List<Payment> payments =
                paymentService.getAllPayments();

        if (payments.isEmpty()) {

            System.out.println(
                    "No payments found."
            );

            return;
        }

        for (Payment payment : payments) {

            System.out.println(
                    payment
            );
        }
    }

    // =====================================================
    // PRINT EVENT
    // =====================================================

    private static void printEvent(Event event) {

        System.out.println(
                "--------------------------------------"
        );

        System.out.println(
                "Event ID      : " +
                        event.getEventId()
        );

        System.out.println(
                "Event Name    : " +
                        event.getEventName()
        );

        System.out.println(
                "Event Type    : " +
                        event.getEventType()
        );

        System.out.println(
                "Venue         : " +
                        event.getVenue()
        );

        System.out.println(
                "Date          : " +
                        event.getEventDate()
        );

        System.out.println(
                "Time          : " +
                        event.getEventTime()
        );

        System.out.println(
                "Ticket Price  : ₹" +
                        event.getTicketPrice()
        );

        System.out.println(
                "Total Seats   : " +
                        event.getTotalSeats()
        );

        System.out.println(
                "--------------------------------------"
        );
    }

    // =====================================================
    // INPUT METHODS
    // =====================================================

    private static int readInt() {

        while (true) {

            try {

                int value =
                        Integer.parseInt(
                                scanner.nextLine()
                        );

                return value;

            } catch (NumberFormatException e) {

                System.out.print(
                        "Please enter a valid number: "
                );
            }
        }
    }

    private static double readDouble() {

        while (true) {

            try {

                double value =
                        Double.parseDouble(
                                scanner.nextLine()
                        );

                return value;

            } catch (NumberFormatException e) {

                System.out.print(
                        "Please enter a valid amount: "
                );
            }
        }
    }
}
