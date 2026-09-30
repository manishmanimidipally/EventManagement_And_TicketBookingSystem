package main.com.eventbooking.daoimpl;

import main.com.eventbooking.dao.BookingDAO;
import main.com.eventbooking.model.Booking;
import main.com.eventbooking.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAOImpl implements BookingDAO {

    @Override
    public boolean createBooking(Booking booking) {

        String sql = "INSERT INTO bookings " +
                "(booking_id, user_id, event_id, seat_id, " +
                "booking_date, total_amount, booking_status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, booking.getBookingId());
            ps.setInt(2, booking.getUserId());
            ps.setInt(3, booking.getEventId());
            ps.setInt(4, booking.getSeatId());
            ps.setTimestamp(5,
                    Timestamp.valueOf(booking.getBookingDate()));
            ps.setDouble(6, booking.getTotalAmount());
            ps.setString(7, booking.getBookingStatus());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Booking getBookingById(int bookingId) {

        String sql = "SELECT * FROM bookings WHERE booking_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, bookingId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapBooking(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Booking> getBookingsByUser(int userId) {

        List<Booking> bookings = new ArrayList<>();

        String sql = "SELECT * FROM bookings WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    bookings.add(mapBooking(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookings;
    }

    @Override
    public List<Booking> getBookingsByEvent(int eventId) {

        List<Booking> bookings = new ArrayList<>();

        String sql = "SELECT * FROM bookings WHERE event_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, eventId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    bookings.add(mapBooking(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookings;
    }

    @Override
    public boolean updateBookingStatus(int bookingId, String status) {

        String sql = "UPDATE bookings SET booking_status = ? " +
                "WHERE booking_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, bookingId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean cancelBooking(int bookingId) {

        String sql = "UPDATE bookings SET booking_status = 'CANCELLED' " +
                "WHERE booking_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, bookingId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deleteBooking(int bookingId) {

        String sql = "DELETE FROM bookings WHERE booking_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, bookingId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private Booking mapBooking(ResultSet rs) throws SQLException {

        Booking booking = new Booking();

        booking.setBookingId(rs.getInt("booking_id"));
        booking.setUserId(rs.getInt("user_id"));
        booking.setEventId(rs.getInt("event_id"));
        booking.setSeatId(rs.getInt("seat_id"));

        Timestamp timestamp = rs.getTimestamp("booking_date");

        if (timestamp != null) {
            booking.setBookingDate(timestamp.toLocalDateTime());
        }

        booking.setTotalAmount(rs.getDouble("total_amount"));
        booking.setBookingStatus(rs.getString("booking_status"));

        return booking;
    }
}
