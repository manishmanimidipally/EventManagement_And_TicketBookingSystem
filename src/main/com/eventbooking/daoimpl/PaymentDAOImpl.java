package main.com.eventbooking.daoimpl;

import main.com.eventbooking.dao.PaymentDAO;
import main.com.eventbooking.model.Payment;
import main.com.eventbooking.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {

    @Override
    public boolean addPayment(Payment payment) {

        String sql = "INSERT INTO payments " +
                "(payment_id, booking_id, amount, payment_method, " +
                "payment_status, payment_date, transaction_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, payment.getPaymentId());
            ps.setInt(2, payment.getBookingId());
            ps.setDouble(3, payment.getAmount());
            ps.setString(4, payment.getPaymentMethod());
            ps.setString(5, payment.getPaymentStatus());
            ps.setTimestamp(6,
                    Timestamp.valueOf(payment.getPaymentDate()));
            ps.setString(7, payment.getTransactionId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Payment getPaymentById(int paymentId) {

        String sql = "SELECT * FROM payments WHERE payment_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, paymentId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapPayment(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Payment getPaymentByBookingId(int bookingId) {

        String sql = "SELECT * FROM payments WHERE booking_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, bookingId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapPayment(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Payment> getAllPayments() {

        List<Payment> payments = new ArrayList<>();

        String sql = "SELECT * FROM payments";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                payments.add(mapPayment(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payments;
    }

    @Override
    public boolean updatePaymentStatus(int paymentId, String status) {

        String sql = "UPDATE payments SET payment_status = ? " +
                "WHERE payment_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, paymentId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private Payment mapPayment(ResultSet rs) throws SQLException {

        Payment payment = new Payment();

        payment.setPaymentId(rs.getInt("payment_id"));
        payment.setBookingId(rs.getInt("booking_id"));
        payment.setAmount(rs.getDouble("amount"));
        payment.setPaymentMethod(rs.getString("payment_method"));
        payment.setPaymentStatus(rs.getString("payment_status"));

        Timestamp timestamp = rs.getTimestamp("payment_date");

        if (timestamp != null) {
            payment.setPaymentDate(timestamp.toLocalDateTime());
        }

        payment.setTransactionId(rs.getString("transaction_id"));

        return payment;
    }
}
