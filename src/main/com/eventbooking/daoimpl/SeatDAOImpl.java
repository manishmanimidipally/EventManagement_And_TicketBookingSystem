package main.com.eventbooking.daoimpl;

import main.com.eventbooking.dao.SeatDAO;
import main.com.eventbooking.model.Seat;
import main.com.eventbooking.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatDAOImpl implements SeatDAO {

	@Override
	public boolean addSeat(Seat seat) {

	    String sql = "INSERT INTO seats " +
	            "(seat_id, event_id, seat_number, seat_type, price, available) " +
	            "VALUES (?, ?, ?, ?, ?, ?)";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, seat.getSeatId());
	        ps.setInt(2, seat.getEventId());
	        ps.setString(3, seat.getSeatNumber());
	        ps.setString(4, seat.getSeatType());
	        ps.setDouble(5, seat.getPrice());
	        ps.setBoolean(6, seat.isAvailable());

	        return ps.executeUpdate() > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return false;
	}
    @Override
    public Seat getSeatById(int seatId) {

        String sql = "SELECT * FROM seats WHERE seat_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, seatId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapSeat(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Seat> getSeatsByEvent(int eventId) {

        List<Seat> seats = new ArrayList<>();

        String sql = "SELECT * FROM seats WHERE event_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, eventId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    seats.add(mapSeat(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return seats;
    }

    @Override
    public List<Seat> getAvailableSeats(int eventId) {

        List<Seat> seats = new ArrayList<>();

        String sql = "SELECT * FROM seats " +
                "WHERE event_id = ? AND available = true";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, eventId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    seats.add(mapSeat(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return seats;
    }

    @Override
    public boolean updateSeatAvailability(int seatId, boolean available) {

        String sql = "UPDATE seats SET available = ? WHERE seat_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, available);
            ps.setInt(2, seatId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deleteSeat(int seatId) {

        String sql = "DELETE FROM seats WHERE seat_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, seatId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private Seat mapSeat(ResultSet rs) throws SQLException {

        Seat seat = new Seat();

        seat.setSeatId(rs.getInt("seat_id"));
        seat.setEventId(rs.getInt("event_id"));
        seat.setSeatNumber(rs.getString("seat_number"));
        seat.setSeatType(rs.getString("seat_type"));
        seat.setPrice(rs.getDouble("price"));
        seat.setAvailable(rs.getBoolean("available"));

        return seat;
    }
}
