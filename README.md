# 💳 Payment Retry & Recovery System
A full-stack **Spring Boot** web application that simulates a real-world payment processing system with automatic retry and recovery mechanisms. The application provides an interactive dashboard to monitor payments, retry failed transactions, and analyze payment statistics in real time.

## 🚀 Features
- 📊 Interactive Dashboard
  - Total Payments
  - Successful Payments
  - Failed Payments
  - Recovered Payments

- 💰 Payment Management
  - Create Payment
  - Delete Payment
  - View All Payments
  - Search Payments
  - Filter by Status

- 🔄 Payment Retry System
  - Retry failed payments
  - Maximum retry limit
  - Automatic recovery after successful retry
  - Retry history tracking

- ⏳ Pending Payment Processing
  - Pending payments are automatically processed after 10 seconds.
  - 70% chance of SUCCESS
  - 30% chance of FAILED with a random failure reason.

- 📈 Analytics
  - Bar Chart (Payment Statistics)
  - Doughnut Chart (Recovery Rate)

- 🎨 Modern UI
  - Responsive Dashboard
  - Toast Notifications
  - Colored Status Badges
  - Status-based Row Colors
  - Hover Effects
  - Sidebar Navigation

## 🛠️ Technologies Used

### Backend
- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- MySQL
- Maven

### Frontend
- HTML5
- CSS3
- JavaScript (ES6)
- Bootstrap 5
- Bootstrap Icons
- Chart.js

### Database
- MySQL

## 📂 Project Structure

```
payment-retry-system
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── payment_retry_system
│   │   │       ├── controller
│   │   │       ├── dto
│   │   │       ├── model
│   │   │       ├── repository
│   │   │       └── service
│   │   │
│   │   └── resources
│   │       └── static
│   │           ├── css
│   │           ├── js
│   │           └── index.html
│   │
│   └── pom.xml
```
## ⚙️ How It Works
### Payment Flow

Create Payment
        │
        ▼
     PENDING
        │
        ▼
 Wait 10 Seconds
        │
        ▼
 ┌───────────────┐
 │               │
 ▼               ▼
SUCCESS       FAILED
                   │
                   ▼
             Retry Payment
                   │
          ┌───────────────┐
          ▼               ▼
     RECOVERED      PERMANENT FAILURE
```

## 📸 Screenshots

### Dashboard

<img width="1920" height="876" alt="whole screen " src="https://github.com/user-attachments/assets/6042b4d8-40a9-4955-bc12-488d8866bdaf" />


### Payment Management

<img width="1567" height="852" alt="payment table " src="https://github.com/user-attachments/assets/b70a1c3e-cde6-4fad-b20c-e0037fb31136" />


### Retry History

<img width="1091" height="695" alt="history " src="https://github.com/user-attachments/assets/b401fbd1-8830-4a98-8cc5-f65aa96bdc25" />

### Analytics

<img width="1507" height="422" alt="charts " src="https://github.com/user-attachments/assets/b0f10f59-ff29-47eb-9b60-1f73e92c53c7" />


## Getting Started

### Clone Repository

```bash
git clone https://github.com/YOUR_USERNAME/payment-retry-system.git
```

### Open Project

```bash
cd payment-retry-system
```

### Configure Database

Update `application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/payment_retry_system
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### Run Application

```bash
mvn spring-boot:run
```

Open

```
http://localhost:8081
```

##  Future Enhancements

- User Authentication
- Email Notifications
- Payment Gateway Integration
- Scheduled Retry Jobs
- Export Reports (CSV/PDF)
- REST API Documentation (Swagger)
- Role-Based Access Control

---

## 👨‍💻 Author

**Nishad Shaikh**

GitHub: https://github.com/nishu949


## 📄 License
This project is developed for educational purposes.
