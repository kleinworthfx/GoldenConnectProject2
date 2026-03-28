-- GoldenConnect Database Schema
-- Database: goldenconnect

CREATE DATABASE IF NOT EXISTS goldenconnect;
USE goldenconnect;

-- Users table
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role ENUM('VOLUNTEER', 'ADMIN', 'ELDERLY') NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    address TEXT,
    date_of_birth DATETIME,
    profile_picture VARCHAR(255),
    skills TEXT,
    preferences TEXT,
    rating DECIMAL(3,2) DEFAULT 0.00,
    total_sessions INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Requests table
CREATE TABLE requests (
    id INT PRIMARY KEY AUTO_INCREMENT,
    requester_id INT NOT NULL,
    volunteer_id INT,
    request_type ENUM('COMPANIONSHIP', 'TECH_HELP', 'ACTIVITY', 'HEALTH_CHECK') NOT NULL,
    description TEXT NOT NULL,
    status ENUM('PENDING', 'ACCEPTED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED') DEFAULT 'PENDING',
    requested_date DATETIME NOT NULL,
    scheduled_date DATETIME,
    completed_date DATETIME,
    location VARCHAR(100),
    duration INT,
    special_requirements TEXT,
    rating DECIMAL(3,2) DEFAULT 0.00,
    feedback TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (requester_id) REFERENCES users(id),
    FOREIGN KEY (volunteer_id) REFERENCES users(id)
);

-- Matches table
CREATE TABLE matches (
    id INT PRIMARY KEY AUTO_INCREMENT,
    volunteer_id INT NOT NULL,
    elderly_id INT NOT NULL,
    match_type ENUM('COMPANIONSHIP', 'TECH_HELP', 'ACTIVITY', 'HEALTH') NOT NULL,
    status ENUM('ACTIVE', 'INACTIVE', 'PAUSED') DEFAULT 'ACTIVE',
    match_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    last_interaction_date DATETIME,
    total_interactions INT DEFAULT 0,
    compatibility_score DECIMAL(3,2) DEFAULT 0.00,
    notes TEXT,
    is_preferred BOOLEAN DEFAULT FALSE,
    match_reason TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (volunteer_id) REFERENCES users(id),
    FOREIGN KEY (elderly_id) REFERENCES users(id)
);

-- Activities table
CREATE TABLE activities (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    category ENUM('COMPANIONSHIP', 'TECH_HELP', 'LEARNING', 'HEALTH', 'ENTERTAINMENT') NOT NULL,
    type ENUM('INDIVIDUAL', 'GROUP', 'WORKSHOP') NOT NULL,
    duration INT NOT NULL,
    difficulty ENUM('BEGINNER', 'INTERMEDIATE', 'ADVANCED') DEFAULT 'BEGINNER',
    location ENUM('Virtual', 'Physical', 'Hybrid') DEFAULT 'Virtual',
    max_participants INT DEFAULT 1,
    current_participants INT DEFAULT 0,
    scheduled_date DATETIME,
    start_time DATETIME,
    end_time DATETIME,
    status ENUM('SCHEDULED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED') DEFAULT 'SCHEDULED',
    volunteer_id INT,
    materials TEXT,
    instructions TEXT,
    is_recurring BOOLEAN DEFAULT FALSE,
    recurrence_pattern VARCHAR(50),
    rating DECIMAL(3,2) DEFAULT 0.00,
    total_sessions INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (volunteer_id) REFERENCES users(id)
);

-- Feedback table
CREATE TABLE feedback (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    target_id INT NOT NULL,
    target_type ENUM('USER', 'ACTIVITY', 'SERVICE', 'SYSTEM') NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    category ENUM('COMPANIONSHIP', 'TECH_HELP', 'ACTIVITY', 'OVERALL'),
    feedback_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    is_anonymous BOOLEAN DEFAULT FALSE,
    response TEXT,
    is_resolved BOOLEAN DEFAULT FALSE,
    tags VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Video Tutorials table
CREATE TABLE video_tutorials (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    category ENUM('TECH_BASICS', 'SOCIAL_MEDIA', 'BANKING', 'SHOPPING', 'COMMUNICATION') NOT NULL,
    difficulty ENUM('BEGINNER', 'INTERMEDIATE', 'ADVANCED') DEFAULT 'BEGINNER',
    video_url VARCHAR(500) NOT NULL,
    thumbnail_url VARCHAR(500),
    duration INT NOT NULL,
    language VARCHAR(50) DEFAULT 'English',
    instructor VARCHAR(100),
    instructor_id INT,
    rating DECIMAL(3,2) DEFAULT 0.00,
    view_count INT DEFAULT 0,
    total_ratings INT DEFAULT 0,
    tags VARCHAR(255),
    transcript TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    status ENUM('DRAFT', 'PUBLISHED', 'ARCHIVED') DEFAULT 'DRAFT',
    target_audience ENUM('ELDERLY', 'VOLUNTEER', 'ALL') DEFAULT 'ALL',
    prerequisites TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (instructor_id) REFERENCES users(id)
);

-- Elderly Profiles table
CREATE TABLE elderly_profiles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(255) NOT NULL,
    age INT NOT NULL CHECK (age >= 65 AND age <= 120),
    medical_condition TEXT NOT NULL,
    retirement_village VARCHAR(255) NOT NULL,
    address TEXT,
    phone_number VARCHAR(20),
    emergency_contact VARCHAR(255),
    emergency_contact_phone VARCHAR(20),
    preferences TEXT,
    notes TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    total_sessions INT DEFAULT 0,
    average_rating DECIMAL(3,2) DEFAULT 0.00,
    UNIQUE KEY unique_full_name (full_name)
);

-- Volunteers table
CREATE TABLE volunteers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    address TEXT,
    date_of_birth DATETIME,
    skills TEXT,
    interests TEXT,
    availability TEXT,
    emergency_contact VARCHAR(255),
    emergency_contact_phone VARCHAR(20),
    background_check VARCHAR(100),
    training_completed VARCHAR(100),
    preferences TEXT,
    notes TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    registration_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    last_active_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    total_sessions_completed INT DEFAULT 0,
    average_rating DECIMAL(3,2) DEFAULT 0.00,
    total_hours INT DEFAULT 0,
    certification VARCHAR(255),
    experience_level ENUM('BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT') DEFAULT 'BEGINNER'
);

-- Create indexes for better performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_requests_requester ON requests(requester_id);
CREATE INDEX idx_requests_volunteer ON requests(volunteer_id);
CREATE INDEX idx_requests_status ON requests(status);
CREATE INDEX idx_matches_volunteer ON matches(volunteer_id);
CREATE INDEX idx_matches_elderly ON matches(elderly_id);
CREATE INDEX idx_activities_volunteer ON activities(volunteer_id);
CREATE INDEX idx_activities_status ON activities(status);
CREATE INDEX idx_feedback_user ON feedback(user_id);
CREATE INDEX idx_feedback_target ON feedback(target_id, target_type);
CREATE INDEX idx_tutorials_category ON video_tutorials(category);
CREATE INDEX idx_tutorials_difficulty ON video_tutorials(difficulty);
CREATE INDEX idx_elderly_profiles_name ON elderly_profiles(full_name);
CREATE INDEX idx_elderly_profiles_village ON elderly_profiles(retirement_village);
CREATE INDEX idx_elderly_profiles_active ON elderly_profiles(is_active);
CREATE INDEX idx_volunteers_email ON volunteers(email);
CREATE INDEX idx_volunteers_name ON volunteers(full_name);
CREATE INDEX idx_volunteers_active ON volunteers(is_active);
CREATE INDEX idx_volunteers_registration_date ON volunteers(registration_date);
CREATE INDEX idx_volunteers_experience_level ON volunteers(experience_level);
