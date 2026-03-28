# GoldenConnect - Bridging Generations Through Technology

## Project Overview

GoldenConnect is a Java-based desktop application designed to bridge the generational gap by connecting youth volunteers with elderly residents in orphanage homes. The application facilitates companionship, digital literacy lessons, and access to activities and tech-assisted services.

## Features

- **User Management**: Registration and authentication for volunteers, elderly users, and administrators
- **Request System**: Elderly users can request services like companionship, tech help, activities, and health checks
- **Matching Algorithm**: Intelligent matching of volunteers with elderly based on skills and preferences
- **Activity Scheduling**: Create and manage various activities and sessions
- **Video Tutorials**: Educational content library for digital literacy
- **Feedback System**: Rating and feedback mechanism for quality control
- **Reporting**: Analytics and reports for organizational insights
- **Notifications**: Reminder system for scheduled activities and health checks

## Technology Stack

- **Language**: Java 17
- **UI Framework**: JavaFX 17
- **Database**: MySQL 8.0
- **Architecture**: Three-Tier Architecture (Presentation, Application, Data layers)
- **Build Tool**: Maven
- **Version Control**: Git

## Project Structure

```
GoldenConnect/
├─ pom.xml                                    # Maven configuration
└─ src/main/
   ├─ java/za/ac/cput/goldenconnect/
   │   ├─ model/                             # Data entities
   │   │   ├─ User.java                      # User model
   │   │   ├─ Request.java                   # Service request model
   │   │   ├─ Match.java                     # User matching model
   │   │   ├─ Activity.java                  # Activity model
   │   │   ├─ Feedback.java                  # Feedback model
   │   │   └─ VideoTutorial.java            # Video tutorial model
   │   ├─ dao/                               # Data Access Objects
   │   │   ├─ UserDAO.java                   # User data access interface
   │   │   ├─ RequestDAO.java                # Request data access interface
   │   │   ├─ MatchDAO.java                  # Match data access interface
   │   │   ├─ ActivityDAO.java               # Activity data access interface
   │   │   ├─ FeedbackDAO.java               # Feedback data access interface
   │   │   └─ VideoTutorialDAO.java         # Video tutorial data access interface
   │   ├─ dao/impl/                          # DAO implementations
   │   │   ├─ UserDAOImpl.java               # User DAO implementation
   │   │   ├─ RequestDAOImpl.java            # Request DAO implementation
   │   │   ├─ MatchDAOImpl.java              # Match DAO implementation
   │   │   ├─ ActivityDAOImpl.java           # Activity DAO implementation
   │   │   ├─ FeedbackDAOImpl.java           # Feedback DAO implementation
   │   │   └─ VideoTutorialDAOImpl.java     # Video tutorial DAO implementation
   │   ├─ service/                           # Business logic services
   │   │   ├─ AuthService.java               # Authentication service interface
   │   │   ├─ RequestService.java            # Request management service interface
   │   │   ├─ MatchingService.java           # User matching service interface
   │   │   ├─ SchedulingService.java         # Activity scheduling service interface
   │   │   ├─ NotificationService.java       # Notification service interface
   │   │   ├─ ActivityService.java           # Activity management service interface
   │   │   ├─ FeedbackService.java           # Feedback service interface
   │   │   ├─ VideoTutorialService.java      # Video tutorial service interface
   │   │   └─ ReportService.java             # Reporting service interface
   │   ├─ service/impl/                      # Service implementations
   │   │   ├─ AuthServiceImpl.java           # Authentication service implementation
   │   │   ├─ RequestServiceImpl.java        # Request service implementation
   │   │   ├─ MatchingServiceImpl.java       # Matching service implementation
   │   │   ├─ SchedulingServiceImpl.java     # Scheduling service implementation
   │   │   ├─ NotificationServiceImpl.java   # Notification service implementation
   │   │   ├─ ActivityServiceImpl.java       # Activity service implementation
   │   │   ├─ FeedbackServiceImpl.java       # Feedback service implementation
   │   │   ├─ VideoTutorialServiceImpl.java # Video tutorial service implementation
   │   │   └─ ReportServiceImpl.java         # Report service implementation
   │   ├─ ui/                                # User interface components
   │   │   ├─ MainApplication.java           # Main JavaFX application
   │   │   ├─ LoginPanel.java                # Login interface
   │   │   ├─ RegistrationPanel.java         # Registration interface
   │   │   ├─ DashboardPanel.java            # Main dashboard
   │   │   ├─ RequestFormPanel.java          # Request creation form
   │   │   ├─ MyRequestsPanel.java           # User requests view
   │   │   ├─ SchedulePanel.java             # Activity scheduling
   │   │   ├─ ActivitiesPanel.java           # Activity management
   │   │   ├─ FeedbackPanel.java             # Feedback management
   │   │   ├─ TutorialsPanel.java            # Video tutorials
   │   │   ├─ ReportsPanel.java              # Reports and analytics
   │   │   └─ NavigationPanel.java           # Navigation menu
   │   └─ util/                              # Utility classes
   │       ├─ DatabaseConnectionManager.java # Database connection management
   │       ├─ PasswordUtil.java              # Password hashing utilities
   │       ├─ DateTimeUtil.java              # Date/time utilities
   │       └─ NotificationUtil.java          # Notification utilities
   └─ resources/db/                          # Database resources
       ├─ schema.sql                          # Database schema
       └─ data.sql                            # Sample data
```

## Design System

GoldenConnect follows a "Modular Warm Tech" design philosophy:

- **Color Palette**: Warm, accessible colors with high contrast
- **Typography**: Atkinson Hyperlegible font for optimal readability
- **UI Components**: Large, senior-friendly buttons (44×44px minimum)
- **Layout**: Clean, organized interface with clear navigation
- **Accessibility**: WCAG 2.2 AA compliance with high-contrast options

### Color Scheme
- **Core Gold**: #F5B700 (brand warmth)
- **Cobalt Blue**: #2D5BFF (primary actions)
- **Coral Red**: #FF5A4A (alerts)
- **Leaf Green**: #2BAE66 (success)
- **Navy Blue**: #0E2A47 (headers)
- **Warm Cream**: #FFF8EE (backgrounds)

## Getting Started

### Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/goldenconnect.git
   cd goldenconnect
   ```

2. **Set up the database**
   ```bash
   # Create MySQL database and user
   mysql -u root -p
   CREATE DATABASE goldenconnect;
   CREATE USER 'goldenconnect'@'localhost' IDENTIFIED BY 'your_password';
   GRANT ALL PRIVILEGES ON goldenconnect.* TO 'goldenconnect'@'localhost';
   FLUSH PRIVILEGES;
   EXIT;
   
   # Run the schema and sample data
   mysql -u goldenconnect -p goldenconnect < src/main/resources/db/schema.sql
   mysql -u goldenconnect -p goldenconnect < src/main/resources/db/data.sql
   ```

3. **Update database configuration**
   - Edit `src/main/java/za/ac/cput/goldenconnect/util/DatabaseConnectionManager.java`
   - Update database URL, username, and password

4. **Build and run the application**
   ```bash
   mvn clean compile
   mvn javafx:run
   ```

### Default Login Credentials

- **Admin**: admin@goldenconnect.com / admin
- **Volunteer**: john.smith@email.com / password
- **Elderly**: robert.wilson@email.com / password

## Development

### Architecture

GoldenConnect follows a three-tier architecture:

1. **Presentation Layer (UI)**: JavaFX components for user interaction
2. **Application Layer (Services)**: Business logic and application services
3. **Data Layer (DAO)**: Data access and persistence

### Adding New Features

1. **Model**: Create entity classes in the `model` package
2. **DAO**: Define data access interfaces and implementations
3. **Service**: Implement business logic in service classes
4. **UI**: Create JavaFX panels for user interaction

### Code Style

- Follow Java naming conventions
- Use meaningful variable and method names
- Add comprehensive JavaDoc comments
- Implement proper error handling
- Write unit tests for business logic

## Testing

```bash
# Run unit tests
mvn test

# Run with coverage
mvn jacoco:report
```

## Deployment

### Building JAR

```bash
mvn clean package
```

### Distribution

The built JAR file will be in the `target/` directory and can be distributed to users.

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## Team

- **Kenson** - Team Leader
- **Lelethu** - Secretary / Scribe
- **Innocent** - UI/UX Designer
- **Mzuvukile** - Backend Developer
- **Reece** - System Architect / Tester
- **Paul Khumalo** - Project Manager / Developer

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions, please contact the development team or create an issue in the repository.

## Roadmap

- [ ] Implement database connectivity
- [ ] Add user authentication
- [ ] Complete UI components
- [ ] Implement business logic
- [ ] Add reporting features
- [ ] Performance optimization
- [ ] User testing and feedback
- [ ] Production deployment

---

**GoldenConnect** - Simple ways to stay together.
