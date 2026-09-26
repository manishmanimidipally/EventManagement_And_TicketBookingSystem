# 🎟️ Event Management and Ticket Booking System

A **Core Java-based Event Ticket Booking System** developed using **Java, JDBC, and MySQL**.

This project demonstrates how to build a real-world console-based application using **Object-Oriented Programming, JDBC database connectivity, DAO design pattern, service layer, exception handling, and MySQL**.

---

## 🚀 Technologies Used

* ☕ Java 21
* 🗄️ MySQL
* 🔌 JDBC
* 🧩 Eclipse IDE
* 🌱 Object-Oriented Programming
* 📦 DAO Design Pattern

---

## 📌 Features

### 👤 User Management

* Register a new user
* User login
* View user details
* Update user details
* Delete user
* Change password

### 🎭 Event Management

* Add new events
* View all events
* Search events
* Update event details
* Delete events

### 💺 Seat Management

* View available seats
* Book seats
* Check seat availability
* Update seat status

### 🎫 Booking Management

* Create a booking
* View booking details
* View user bookings
* Cancel booking
* Update booking status

### 💳 Payment Management

* Process payment
* Store payment details
* Check payment status
* Associate payment with booking

---

# 📂 Project Structure

```text
EventTicketBooking
│
├── src
│   └── com.eventbooking
│
│       ├── model
│       │   ├── User.java
│       │   ├── Admin.java
│       │   ├── Event.java
│       │   ├── Seat.java
│       │   ├── Booking.java
│       │   └── Payment.java
│       │
│       ├── dao
│       │   ├── UserDAO.java
│       │   ├── EventDAO.java
│       │   ├── SeatDAO.java
│       │   ├── BookingDAO.java
│       │   └── PaymentDAO.java
│       │
│       ├── dao.impl
│       │   ├── UserDAOImpl.java
│       │   ├── EventDAOImpl.java
│       │   ├── SeatDAOImpl.java
│       │   └── BookingDAOImpl.java
│       │
│       ├── service
│       │   ├── UserService.java
│       │   ├── EventService.java
│       │   └── BookingService.java
│       │
│       ├── exception
│       │   ├── EventNotFoundException.java
│       │   ├── UserNotFoundException.java
│       │   ├── SeatNotAvailableException.java
│       │   └── BookingException.java
│       │
│       ├── util
│       │   └── DBConnection.java
│       │
│       └── main
│           └── EventTicketBookingApplication.java
│
├── .gitignore
├── README.md
└── .classpath
```

---

# 🏗️ Architecture

The application follows a layered architecture:

```text
             User
              │
              ▼
        ┌─────────────┐
        │    Main     │
        │ Application │
        └──────┬──────┘
               │
               ▼
        ┌─────────────┐
        │   Service   │
        │    Layer    │
        └──────┬──────┘
               │
               ▼
        ┌─────────────┐
        │     DAO     │
        │    Layer    │
        └──────┬──────┘
               │
               ▼
        ┌─────────────┐
        │    JDBC     │
        └──────┬──────┘
               │
               ▼
        ┌─────────────┐
        │    MySQL    │
        │  Database   │
        └─────────────┘
```

---

# 📦 Model Package

The `model` package contains Java classes that represent the application's data.

### User

Represents a registered user.

```java
private int userId;
private String name;
private String email;
private String password;
private String phone;
```

### Admin

Represents an administrator who manages events and bookings.

```java
private int adminId;
private String name;
private String email;
private String password;
```

### Event

Represents an event available for booking.

```java
private int eventId;
private String eventName;
private String eventType;
private String venue;
private String eventDate;
private String eventTime;
private double ticketPrice;
```

### Seat

Represents seats available for an event.

```java
private int seatId;
private int eventId;
private String seatNumber;
private String seatType;
private String status;
```

### Booking

Represents a ticket booking made by a user.

```java
private int bookingId;
private int userId;
private int eventId;
private int seatId;
private String bookingDate;
private String bookingStatus;
private double totalAmount;
```

### Payment

Represents payment information for a booking.

```java
private int paymentId;
private int bookingId;
private double amount;
private String paymentMethod;
private String paymentStatus;
```

---

# 🔌 JDBC

This project uses **JDBC (Java Database Connectivity)** to communicate with MySQL.

The basic JDBC flow is:

```text
Java Application
       ↓
JDBC Driver
       ↓
MySQL Database
```

Example:

```java
Connection connection = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/event_booking",
        "root",
        "your_password"
);
```

---

# 🗄️ Database Setup

## 1. Create Database

```sql
CREATE DATABASE event_booking;
```

Select the database:

```sql
USE event_booking;
```

---

# 👤 Users Table

```sql
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(15)
);
```

---

# 👨‍💼 Admin Table

```sql
CREATE TABLE admins (
    admin_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);
```

---

# 🎭 Events Table

```sql
CREATE TABLE events (
    event_id INT PRIMARY KEY AUTO_INCREMENT,
    event_name VARCHAR(150) NOT NULL,
    event_type VARCHAR(100),
    venue VARCHAR(200),
    event_date DATE,
    event_time TIME,
    ticket_price DECIMAL(10,2)
);
```

---

# 💺 Seats Table

```sql
CREATE TABLE seats (
    seat_id INT PRIMARY KEY AUTO_INCREMENT,
    event_id INT NOT NULL,
    seat_number VARCHAR(20) NOT NULL,
    seat_type VARCHAR(50),
    status VARCHAR(20) DEFAULT 'AVAILABLE',

    FOREIGN KEY (event_id)
        REFERENCES events(event_id)
);
```

---

# 🎫 Bookings Table

```sql
CREATE TABLE bookings (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    event_id INT NOT NULL,
    seat_id INT NOT NULL,
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    booking_status VARCHAR(30) DEFAULT 'CONFIRMED',
    total_amount DECIMAL(10,2),

    FOREIGN KEY (user_id)
        REFERENCES users(user_id),

    FOREIGN KEY (event_id)
        REFERENCES events(event_id),

    FOREIGN KEY (seat_id)
        REFERENCES seats(seat_id)
);
```

---

# 💳 Payments Table

```sql
CREATE TABLE payments (
    payment_id INT PRIMARY KEY AUTO_INCREMENT,
    booking_id INT NOT NULL,
    amount DECIMAL(10,2),
    payment_method VARCHAR(50),
    payment_status VARCHAR(30),

    FOREIGN KEY (booking_id)
        REFERENCES bookings(booking_id)
);
```

---

# 🔗 Database Relationships

```text
User
 │
 │ 1
 │
 │
 │ *
 ▼
Booking
 │
 ├──────────────► Event
 │
 ├──────────────► Seat
 │
 └──────────────► Payment
```

---

# 🔄 Example Booking Flow

```text
User Registration
       ↓
User Login
       ↓
View Available Events
       ↓
Select Event
       ↓
View Available Seats
       ↓
Select Seat
       ↓
Create Booking
       ↓
Make Payment
       ↓
Booking Confirmed
```

---

# 🧩 DAO Layer

DAO stands for **Data Access Object**.

The DAO layer is responsible for communicating with the database.

Example:

```java
public interface UserDAO {

    void addUser(User user);

    User getUserById(int userId);

    User getUserByEmail(String email);

    List<User> getAllUsers();

    boolean updateUser(User user);

    boolean deleteUser(int userId);
}
```

The implementation is provided by:

```java
UserDAOImpl
```

---

# ⚙️ Service Layer

The service layer contains the application's business logic.

Example:

```java
public class UserService {

    private UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void registerUser(User user) {

        // Business validation

        userDAO.addUser(user);
    }
}
```

---

# 🛠️ Example JDBC DELETE Operation

To delete a user:

```java
String sql = "DELETE FROM users WHERE user_id = ?";

PreparedStatement ps = connection.prepareStatement(sql);

ps.setInt(1, userId);

int rows = ps.executeUpdate();

if (rows > 0) {
    System.out.println("User deleted successfully.");
} else {
    System.out.println("User not found.");
}
```

---

# ✏️ Example JDBC UPDATE Operation

To update a user's password:

```java
String sql = "UPDATE users SET password = ? WHERE user_id = ?";

PreparedStatement ps = connection.prepareStatement(sql);

ps.setString(1, "123456");
ps.setInt(2, 1001);

int rows = ps.executeUpdate();

if (rows > 0) {
    System.out.println("Password updated successfully.");
}
```

---

# 🔍 Example SELECT Operation

```java
String sql = "SELECT * FROM users WHERE user_id = ?";

PreparedStatement ps = connection.prepareStatement(sql);

ps.setInt(1, 1001);

ResultSet rs = ps.executeQuery();

if (rs.next()) {

    System.out.println("User ID: " + rs.getInt("user_id"));
    System.out.println("Name: " + rs.getString("name"));
    System.out.println("Email: " + rs.getString("email"));
}
```

---

# 🔐 Security Note

For learning purposes, the database examples may contain plain-text passwords.

In a production application, passwords should **never be stored as plain text**.

Instead, passwords should be securely hashed using a password-hashing algorithm such as **BCrypt** or **Argon2**.

---

# 📋 Example User Data

```text
+---------+-------+------------------+----------+------------+
| user_id | name  | email            | password | phone      |
+---------+-------+------------------+----------+------------+
| 1001    | mani  | mani@gmail.com   | 123      | 9948492341 |
| 1002    | sunny | sunny@gmail.com  | sunny123 | 9876054321 |
| 1003    | yuva  | yuva@gmail.com   | 12345678 | 9989845321 |
+---------+-------+------------------+----------+------------+
```

---

# 🎯 Learning Objectives

This project helps demonstrate:

* Core Java
* OOP concepts
* Encapsulation
* Interfaces
* Exception handling
* Collections
* JDBC
* SQL
* MySQL
* PreparedStatement
* ResultSet
* DAO Pattern
* Layered Architecture
* CRUD Operations
* Foreign Keys
* Database Relationships
* Business Logic

---

# 🚀 Future Enhancements

* Online payment gateway
* Email booking confirmation
* PDF ticket generation
* Admin dashboard
* Seat layout visualization
* Search and filtering
* Booking history
* Password hashing
* Transaction management
* Connection pooling
* Spring Boot REST API
* Frontend integration

---

# 👨‍💻 Author

**Manish Mamidipally**

Java Developer | Core Java | JDBC | MySQL | Spring

---

## ⭐ If you like this project

Feel free to ⭐ the repository and use the project for learning and practice.
