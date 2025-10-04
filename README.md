# Bangladesh Police Management System

A comprehensive police station management system built with JavaFX for the Bangladesh Police Department. This system provides real-time communication, file sharing, criminal records management, and officer tracking capabilities.

## Features

### 🔐 Authentication System
- **Admin Login**: Username and password-based authentication for administrators
- **Officer Login**: Police ID and password-based authentication for officers
- **Session Management**: Secure user sessions with automatic timeout

### 👨‍💼 Admin Dashboard
- **Real-time Statistics**: Active rooms, total officers, pending reports, completed missions
- **Chat Room Management**: Create, manage, and monitor mission communication rooms
- **Officer Management**: Add, edit, and manage police officers
- **File Management**: View and organize all uploaded files across rooms
- **Criminal Records**: Search and manage criminal database records

### 👮‍♂️ Officer Dashboard
- **Mission Overview**: View assigned missions and their status
- **Chat Rooms**: Access assigned communication rooms
- **Criminal Records**: Search and view criminal records
- **Live Map**: Real-time officer location tracking
- **File Sharing**: Upload and share files within missions

### 💬 Chat Room Features
- **Real-time Messaging**: Text messaging with auto-delete after 10 minutes
- **File Sharing**: Upload images, videos, documents, and audio files
- **Voice Calls**: Walkie-talkie style group voice communication
- **Participant Management**: View all room members and their status
- **File History**: Access previously uploaded files with verification

### 🗺️ Interactive Map
- **Real-time Tracking**: Live officer location monitoring
- **Room-based Filtering**: View locations of officers in specific missions
- **Status Indicators**: Online, away, and offline status visualization
- **Interactive Controls**: Zoom, pan, and officer selection

### 🗂️ Criminal Records Management
- **Advanced Search**: Filter by name, age, gender, hometown, National ID
- **Detailed Records**: Complete criminal history with crime details
- **Status Tracking**: Active, resolved, and under investigation cases

### 📁 File Management System
- **Multi-format Support**: Images, videos, documents, and audio files
- **Room Organization**: Files organized by mission/chat room
- **Upload History**: Track file uploads and access patterns
- **Secure Access**: Verification required for sensitive files

## Technology Stack

- **Frontend**: JavaFX with FXML for UI design
- **Backend**: Java with JDBC for database operations
- **Database**: SQLite for local data storage
- **Styling**: Custom CSS with police-themed color scheme
- **Build Tool**: Maven for dependency management
- **IDE Support**: IntelliJ IDEA, Eclipse, VS Code

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   ├── com/bangladeshpolice/
│   │   │   ├── DatabaseManager.java          # Database operations
│   │   │   ├── PoliceManagementSystem.java   # Main application class
│   │   │   ├── SampleDataLoader.java         # Demo data loader
│   │   │   ├── controller/                   # FXML Controllers
│   │   │   │   ├── AdminDashboardController.java
│   │   │   │   ├── ChatRoomController.java
│   │   │   │   ├── CriminalRecordsController.java
│   │   │   │   ├── FileManagerController.java
│   │   │   │   ├── LandingPageController.java
│   │   │   │   ├── OfficerDashboardController.java
│   │   │   │   └── PoliceMapController.java
│   │   │   ├── model/                        # Data models
│   │   │   │   ├── ChatRoom.java
│   │   │   │   ├── Crime.java
│   │   │   │   ├── CriminalRecord.java
│   │   │   │   ├── Message.java
│   │   │   │   ├── OfficerLocation.java
│   │   │   │   ├── PoliceFile.java
│   │   │   │   └── User.java
│   │   │   └── module-info.java
│   ├── resources/
│   │   ├── css/
│   │   │   └── styles.css                    # Application styling
│   │   ├── fxml/
│   │   │   ├── AdminDashboard.fxml
│   │   │   ├── ChatRoom.fxml
│   │   │   ├── CriminalRecords.fxml
│   │   │   ├── FileManager.fxml
│   │   │   ├── LandingPage.fxml
│   │   │   ├── OfficerDashboard.fxml
│   │   │   └── PoliceMap.fxml
│   │   └── images/                           # Icon resources
│   │       ├── police-badge.png
│   │       ├── user-icon.png
│   │       ├── lock-icon.png
│   │       └── [other icons]
└── test/                                     # Test files
```

## Installation and Setup

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- JavaFX SDK 17+

### Build Instructions

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd bangladesh-police-management-system
   ```

2. **Build the project**
   ```bash
   mvn clean compile
   ```

3. **Run the application**
   ```bash
   mvn javafx:run
   ```

   Or create a runnable JAR:
   ```bash
   mvn clean package
   java -jar target/police-management-system-1.0.0.jar
   ```

### Development Setup

1. **IDE Configuration**
   - Import as Maven project
   - Set JavaFX SDK path in run configurations
   - Configure module path for JavaFX libraries

2. **Database Setup**
   - SQLite database is created automatically on first run
   - Sample data is loaded for demonstration
   - Database file: `police_management.db`

## Usage Guide

### Admin Login
- **Username**: `admin`
- **Password**: `admin123`
- Access full administrative features

### Officer Login
- **Police ID**: `BD001` (or any BD### format)
- **Password**: `officer123`
- Access officer-specific features

### Key Features Demo

1. **Create Chat Rooms**
   - Navigate to Admin Dashboard → Chat Rooms
   - Click "Create Room" and assign officers

2. **File Sharing**
   - Upload files in chat rooms
   - Access files with police ID verification

3. **Criminal Records Search**
   - Use search and filter options
   - View detailed criminal records

4. **Map Tracking**
   - View real-time officer locations
   - Filter by mission/room

## Security Features

- **Authentication**: Username/password for admins, Police ID/password for officers
- **Authorization**: Role-based access control
- **File Access Control**: Police ID verification for sensitive files
- **Audit Logging**: All actions are logged for security monitoring
- **Auto-delete Messages**: Text messages deleted after 10 minutes
- **Session Management**: Secure user sessions

## Mobile Responsiveness

- **Responsive Design**: Optimized for desktop and tablet screens
- **Touch-friendly**: Large touch targets for mobile interaction
- **Adaptive Layout**: Adjusts to different screen sizes
- **Mobile-first**: Designed with mobile usability in mind

## Color Scheme

- **Primary Blue**: `#1e40af` - Police authority color
- **Dark Blue**: `#1e3a8a` - Headers and important elements
- **Light Blue**: `#3b82f6` - Interactive elements
- **Green**: `#22c55e` - Online/active status
- **Orange**: `#f59e0b` - Away/busy status
- **Red**: `#dc2626` - Alerts and danger states
- **Gray Scale**: Various shades for backgrounds and text

## Database Schema

### Core Tables
- **users**: Officer and admin accounts
- **chat_rooms**: Mission communication rooms
- **room_participants**: Officer-room assignments
- **messages**: Chat messages and file uploads
- **files**: File metadata and storage info
- **criminal_records**: Criminal database records
- **crimes**: Individual crime records
- **officer_locations**: Real-time location tracking
- **audit_log**: Security and access logging

## Future Enhancements

- **Real-time WebSocket Communication**: Live message updates
- **Advanced Map Features**: GPS integration, route planning
- **Report Generation**: PDF reports and analytics
- **Multi-language Support**: Bengali language interface
- **Cloud Integration**: File storage and backup
- **API Integration**: External system connectivity

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is developed for the Bangladesh Police Department and is not licensed for public use.

## Support

For technical support and questions:
- Email: support@bdpolice.gov.bd
- Phone: +880-2-123456789

---

**Bangladesh Police Management System v1.0.0**
*Secure • Professional • Efficient*