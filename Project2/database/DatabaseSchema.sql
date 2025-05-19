-- Users and Authentication
CREATE TABLE users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    user_type ENUM('ELDER', 'VOLUNTEER', 'ADMIN') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);

-- User Profiles
CREATE TABLE user_profiles (
    profile_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    date_of_birth DATE,
    photo_url VARCHAR(255),
    medical_info TEXT,
    preferences JSON,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Emergency Contacts
CREATE TABLE emergency_contacts (
    contact_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    relationship VARCHAR(50),
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    is_primary BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Activities and Sessions
CREATE TABLE activities (
    activity_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    category VARCHAR(50),
    duration INT, -- in minutes
    max_participants INT,
    difficulty_level ENUM('EASY', 'MEDIUM', 'HARD'),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE sessions (
    session_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    activity_id BIGINT NOT NULL,
    scheduled_start DATETIME NOT NULL,
    scheduled_end DATETIME NOT NULL,
    status ENUM('SCHEDULED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED'),
    notes TEXT,
    FOREIGN KEY (activity_id) REFERENCES activities(activity_id)
);

-- Session Participants
CREATE TABLE session_participants (
    session_id BIGINT,
    user_id BIGINT,
    role ENUM('PARTICIPANT', 'VOLUNTEER', 'INSTRUCTOR'),
    attendance_status ENUM('CONFIRMED', 'ATTENDED', 'ABSENT'),
    feedback TEXT,
    rating INT,
    PRIMARY KEY (session_id, user_id),
    FOREIGN KEY (session_id) REFERENCES sessions(session_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Health Monitoring
CREATE TABLE health_records (
    record_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    record_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    blood_pressure VARCHAR(20),
    temperature DECIMAL(4,1),
    heart_rate INT,
    mood ENUM('HAPPY', 'NEUTRAL', 'SAD'),
    notes TEXT,
    alert_level ENUM('NORMAL', 'WARNING', 'CRITICAL'),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Notifications
CREATE TABLE notifications (
    notification_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    type ENUM('HEALTH', 'ACTIVITY', 'EMERGENCY', 'SYSTEM'),
    message TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    read_at TIMESTAMP,
    priority ENUM('LOW', 'MEDIUM', 'HIGH', 'URGENT'),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);