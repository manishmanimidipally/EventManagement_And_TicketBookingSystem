package main.com.eventbooking.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import main.com.eventbooking.dao.BookingDAO;
import main.com.eventbooking.dao.PaymentDAO;
import main.com.eventbooking.daoimpl.BookingDAOImpl;
import main.com.eventbooking.daoimpl.PaymentDAOImpl;
import main.com.eventbooking.exception.BookingNotFoundException;
import main.com.eventbooking.exception.PaymentException;
import main.com.eventbooking.model.Booking;
import main.com.eventbooking.model.Payment;
import main.com.eventbooking.util.IDGenerator;

public class PaymentService {

    private final PaymentDAO paymentDAO;
    private final BookingDAO bookingDAO;

    public PaymentService() {
        this.paymentDAO = new PaymentDAOImpl();
        this.bookingDAO = new BookingDAOImpl();
    }

    public boolean makePayment(
            int bookingId,
            String paymentMethod) {

        Booking booking =
                bookingDAO.getBookingById(bookingId);

        if (booking == null) {
            throw new BookingNotFoundException(
                    "Booking not found with ID: " + bookingId
            );
        }

        if ("CANCELLED".equalsIgnoreCase(
                booking.getBookingStatus())) {

            throw new PaymentException(
                    "Payment cannot be made for a cancelled booking"
            );
        }

        Payment existingPayment =
                paymentDAO.getPaymentByBookingId(bookingId);

        if (existingPayment != null) {
            throw new PaymentException("Payment already exists for this booking");
        }

        if (paymentMethod == null ||
                paymentMethod.trim().isEmpty()) {

            throw new PaymentException(
                    "Payment method cannot be empty"
            );
        }

        Payment payment = new Payment();

        payment.setPaymentId(
                IDGenerator.generatePaymentId()
        );

        payment.setBookingId(bookingId);

        payment.setAmount(
                booking.getTotalAmount()
        );

        payment.setPaymentMethod(paymentMethod);

        payment.setPaymentStatus("SUCCESS");

        payment.setPaymentDate(
                LocalDateTime.now()
        );

        payment.setTransactionId(
                generateTransactionId()
        );

        return paymentDAO.addPayment(payment);
    }

    public Payment getPaymentById(int paymentId) {

        Payment payment =
                paymentDAO.getPaymentById(paymentId);

        if (payment == null) {
            throw new PaymentException(
                    "Payment not found with ID: " + paymentId
            );
        }

        return payment;
    }

    public Payment getPaymentByBookingId(int bookingId) {

        Payment payment =
                paymentDAO.getPaymentByBookingId(bookingId);

        if (payment == null) {
            throw new PaymentException(
                    "Payment not found for booking ID: "
                    + bookingId
            );
        }

        return payment;
    }

    public List<Payment> getAllPayments() {
        return paymentDAO.getAllPayments();
    }

    public boolean updatePaymentStatus(
            int paymentId,
            String status) {

        getPaymentById(paymentId);

        if (status == null || status.trim().isEmpty()) {
            throw new PaymentException(
                    "Payment status cannot be empty"
            );
        }

        return paymentDAO.updatePaymentStatus(
                paymentId,
                status
        );
    }

    private String generateTransactionId() {

        return "TXN-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }
}
