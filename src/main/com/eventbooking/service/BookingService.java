package main.com.eventbooking.service;

import java.time.LocalDateTime;
import java.util.List;

import main.com.eventbooking.dao.BookingDAO;
import main.com.eventbooking.dao.EventDAO;
import main.com.eventbooking.dao.SeatDAO;
import main.com.eventbooking.daoimpl.BookingDAOImpl;
import main.com.eventbooking.daoimpl.EventDAOImpl;
import main.com.eventbooking.daoimpl.SeatDAOImpl;
import main.com.eventbooking.exception.BookingNotFoundException;
import main.com.eventbooking.exception.EventNotFoundException;
import main.com.eventbooking.exception.SeatNotAvailableException;
import main.com.eventbooking.model.Booking;
import main.com.eventbooking.model.Event;
import main.com.eventbooking.model.Seat;
import main.com.eventbooking.util.IDGenerator;

public class BookingService {

    private final BookingDAO bookingDAO;
    private final EventDAO eventDAO;
    private final SeatDAO seatDAO;

    public BookingService() {
        this.bookingDAO = new BookingDAOImpl();
        this.eventDAO = new EventDAOImpl();
        this.seatDAO = new SeatDAOImpl();
    }

    public int createBooking(int userId, int eventId, int seatId) {

        Event event = eventDAO.getEventById(eventId);

        if (event == null) {
            throw new EventNotFoundException(
                    "Event not found with ID: " + eventId
            );
        }

        Seat seat = seatDAO.getSeatById(seatId);

        if (seat == null) {
            throw new IllegalArgumentException(
                    "Seat not found with ID: " + seatId
            );
        }

        if (seat.getEventId() != eventId) {
            throw new IllegalArgumentException(
                    "Seat does not belong to this event"
            );
        }

        if (!seat.isAvailable()) {
            throw new SeatNotAvailableException(
                    "Seat " + seat.getSeatNumber() +
                    " is not available"
            );
        }

        // Create booking
        Booking booking = new Booking();

        // Generate Booking ID
        int bookingId = IDGenerator.generateBookingId();

        booking.setBookingId(bookingId);
        booking.setUserId(userId);
        booking.setEventId(eventId);
        booking.setSeatId(seatId);
        booking.setBookingDate(LocalDateTime.now());
        booking.setTotalAmount(seat.getPrice());
        booking.setBookingStatus("CONFIRMED");

        // Save booking
        boolean bookingCreated = bookingDAO.createBooking(booking);

        if (bookingCreated) {

            // Make seat unavailable
            boolean seatUpdated =
                    seatDAO.updateSeatAvailability(
                            seatId,
                            false
                    );

            if (!seatUpdated) {
                throw new IllegalStateException(
                        "Booking created but seat status could not be updated"
                );
            }

            // Return Booking ID
            return bookingId;
        }

        // Booking failed
        return -1;
    }
    public Booking getBookingById(int bookingId) {

        Booking booking =
                bookingDAO.getBookingById(bookingId);

        if (booking == null) {
            throw new BookingNotFoundException(
                    "Booking not found with ID: " + bookingId
            );
        }

        return booking;
    }

    public List<Booking> getBookingsByUser(int userId) {
        return bookingDAO.getBookingsByUser(userId);
    }

    public List<Booking> getBookingsByEvent(int eventId) {

        Event event = eventDAO.getEventById(eventId);

        if (event == null) {
            throw new EventNotFoundException(
                    "Event not found with ID: " + eventId
            );
        }

        return bookingDAO.getBookingsByEvent(eventId);
    }

    public boolean cancelBooking(int bookingId) {

        Booking booking = getBookingById(bookingId);

        if ("CANCELLED".equalsIgnoreCase(
                booking.getBookingStatus())) {

            throw new IllegalStateException(
                    "Booking is already cancelled"
            );
        }

        boolean cancelled =
                bookingDAO.cancelBooking(bookingId);

        if (cancelled) {

            seatDAO.updateSeatAvailability(
                    booking.getSeatId(),
                    true
            );

            return true;
        }

        return false;
    }

    public boolean updateBookingStatus(
            int bookingId,
            String status) {

        getBookingById(bookingId);

        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Booking status cannot be empty"
            );
        }

        return bookingDAO.updateBookingStatus(
                bookingId,
                status
        );
    }

	
}
