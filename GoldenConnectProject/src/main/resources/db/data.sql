-- GoldenConnect Sample Data
-- Insert sample data for testing and development

USE goldenconnect;

-- Insert sample users
INSERT INTO users (name, email, role, password_hash, phone_number, skills, preferences, rating, total_sessions) VALUES
('John Smith', 'john.smith@email.com', 'VOLUNTEER', '$2a$10$hashedpassword123', '+27123456789', 'Technology, Communication, Gardening', NULL, 4.8, 15),
('Mary Johnson', 'mary.johnson@email.com', 'VOLUNTEER', '$2a$10$hashedpassword123', '+27123456790', 'Cooking, Music, Art', NULL, 4.9, 22),
('Robert Wilson', 'robert.wilson@email.com', 'ELDERLY', '$2a$10$hashedpassword123', '+27123456791', NULL, 'Technology help, Companionship, Reading', 4.7, 8),
('Sarah Davis', 'sarah.davis@email.com', 'ELDERLY', '$2a$10$hashedpassword123', '+27123456792', NULL, 'Gardening, Music, Social activities', 4.6, 12),
('Admin User', 'admin@goldenconnect.com', 'ADMIN', '$2a$10$hashedpassword123', '+27123456793', 'System administration, User management', NULL, 5.0, 0);

-- Insert sample requests
INSERT INTO requests (requester_id, request_type, description, requested_date, location, duration, special_requirements) VALUES
(3, 'TECH_HELP', 'Need help setting up video calls with my family', NOW(), 'Virtual', 60, 'Patient teacher preferred'),
(4, 'COMPANIONSHIP', 'Would like someone to chat with and share stories', NOW(), 'Virtual', 45, 'Someone who enjoys music'),
(3, 'ACTIVITY', 'Interested in learning basic computer skills', NOW() + INTERVAL 1 DAY, 'Virtual', 90, 'Beginner-friendly approach'),
(4, 'HEALTH_CHECK', 'Regular check-in and wellness conversation', NOW() + INTERVAL 2 DAY, 'Virtual', 30, 'Gentle and caring approach');

-- Insert sample matches
INSERT INTO matches (volunteer_id, elderly_id, match_type, compatibility_score, match_reason) VALUES
(1, 3, 'TECH_HELP', 4.8, 'Strong technology skills match with elderly tech needs'),
(2, 4, 'COMPANIONSHIP', 4.9, 'Shared interests in music and social activities'),
(1, 4, 'ACTIVITY', 4.7, 'Good balance of skills and interests');

-- Insert sample activities
INSERT INTO activities (name, description, category, type, duration, difficulty, location, max_participants, volunteer_id) VALUES
('Basic Computer Skills Workshop', 'Learn fundamental computer operations and internet safety', 'LEARNING', 'WORKSHOP', 90, 'BEGINNER', 'Virtual', 5, 1),
('Gardening Chat Session', 'Virtual gardening discussion and plant care tips', 'COMPANIONSHIP', 'GROUP', 60, 'BEGINNER', 'Virtual', 8, 2),
('Music Appreciation Hour', 'Share favorite music and discuss musical memories', 'ENTERTAINMENT', 'GROUP', 45, 'BEGINNER', 'Virtual', 10, 2),
('Technology Q&A Session', 'Answer questions about smartphones and apps', 'TECH_HELP', 'INDIVIDUAL', 30, 'BEGINNER', 'Virtual', 1, 1);

-- Insert sample feedback
INSERT INTO feedback (user_id, target_id, target_type, rating, comment, category, status) VALUES
(3, 1, 'USER', 5, 'Excellent teacher, very patient and helpful', 'TECH_HELP', 'APPROVED'),
(4, 2, 'USER', 5, 'Wonderful companion, great conversation', 'COMPANIONSHIP', 'APPROVED'),
(3, 1, 'ACTIVITY', 4, 'Great workshop, learned a lot', 'TECH_HELP', 'APPROVED'),
(4, 3, 'ACTIVITY', 5, 'Loved the music session, very enjoyable', 'ACTIVITY', 'APPROVED');

-- Insert sample video tutorials
INSERT INTO video_tutorials (title, description, category, difficulty, video_url, duration, instructor, instructor_id, status, target_audience) VALUES
('Getting Started with Video Calls', 'Learn how to make video calls using popular apps', 'TECH_BASICS', 'BEGINNER', 'https://example.com/video1.mp4', 300, 'John Smith', 1, 'PUBLISHED', 'ELDERLY'),
('Smartphone Basics', 'Essential smartphone operations for beginners', 'TECH_BASICS', 'BEGINNER', 'https://example.com/video2.mp4', 450, 'John Smith', 1, 'PUBLISHED', 'ELDERLY'),
('Online Banking Safety', 'Stay safe while banking online', 'BANKING', 'INTERMEDIATE', 'https://example.com/video3.mp4', 600, 'John Smith', 1, 'PUBLISHED', 'ELDERLY'),
('Social Media for Seniors', 'Connect with family on social media platforms', 'SOCIAL_MEDIA', 'BEGINNER', 'https://example.com/video4.mp4', 480, 'Mary Johnson', 2, 'PUBLISHED', 'ELDERLY'),
('Digital Photo Sharing', 'Share photos with family and friends', 'COMMUNICATION', 'BEGINNER', 'https://example.com/video5.mp4', 360, 'Mary Johnson', 2, 'PUBLISHED', 'ELDERLY');

-- Update some ratings based on feedback
UPDATE users SET rating = 4.8, total_sessions = 15 WHERE id = 1;
UPDATE users SET rating = 4.9, total_sessions = 22 WHERE id = 2;
UPDATE users SET rating = 4.7, total_sessions = 8 WHERE id = 3;
UPDATE users SET rating = 4.6, total_sessions = 12 WHERE id = 4;

-- Update video tutorial ratings
UPDATE video_tutorials SET rating = 4.8, view_count = 25, total_ratings = 5 WHERE id = 1;
UPDATE video_tutorials SET rating = 4.7, view_count = 18, total_ratings = 4 WHERE id = 2;
UPDATE video_tutorials SET rating = 4.9, view_count = 32, total_ratings = 6 WHERE id = 3;
UPDATE video_tutorials SET rating = 4.6, view_count = 15, total_ratings = 3 WHERE id = 4;
UPDATE video_tutorials SET rating = 4.8, view_count = 28, total_ratings = 5 WHERE id = 5;

-- Insert sample elderly profiles
INSERT INTO elderly_profiles (full_name, age, medical_condition, retirement_village, address, phone_number, emergency_contact, emergency_contact_phone, preferences, notes, total_sessions, average_rating) VALUES
('Robert Wilson', 72, 'Mild arthritis, good mobility', 'Sunset Gardens Retirement Village', '123 Sunset Drive, Cape Town', '+27123456791', 'Jane Wilson', '+27123456794', 'Technology help, Companionship, Reading', 'Enjoys learning new things, very patient', 8, 4.7),
('Sarah Davis', 68, 'Diabetes, well managed', 'Golden Years Home', '456 Golden Street, Johannesburg', '+27123456792', 'Michael Davis', '+27123456795', 'Gardening, Music, Social activities', 'Loves classical music and gardening', 12, 4.6),
('Margaret Thompson', 75, 'Hypertension, stable', 'Serenity Lodge', '789 Peace Avenue, Durban', '+27123456796', 'David Thompson', '+27123456797', 'Cooking, Art, Storytelling', 'Former teacher, loves sharing stories', 5, 4.8),
('James Brown', 70, 'Mild hearing loss', 'Harmony House', '321 Harmony Road, Pretoria', '+27123456798', 'Elizabeth Brown', '+27123456799', 'Technology, Chess, Walking', 'Very active, enjoys strategic games', 10, 4.5),
('Dorothy White', 73, 'Good health, minor vision issues', 'Comfort Care Village', '654 Comfort Lane, Port Elizabeth', '+27123456800', 'Richard White', '+27123456801', 'Reading, Music, Social interaction', 'Loves reading and classical music', 7, 4.9);

-- Insert sample volunteers
INSERT INTO volunteers (full_name, email, phone_number, address, date_of_birth, skills, interests, availability, emergency_contact, emergency_contact_phone, background_check, training_completed, preferences, notes, total_sessions_completed, average_rating, total_hours, certification, experience_level) VALUES
('John Smith', 'john.smith@email.com', '+27123456789', '123 Volunteer Street, Cape Town', '1990-05-15 00:00:00', 'Technology, Communication, Gardening', 'Computers, Nature, Helping others', 'Weekdays 9-5, Weekends 10-3', 'Lisa Smith', '+27123456802', 'Completed', 'Basic Training, Safety Course', 'Prefers technology and companionship sessions', 'Very reliable and patient with elderly', 15, 4.8, 22, 'Volunteer Certification', 'ADVANCED'),
('Mary Johnson', 'mary.johnson@email.com', '+27123456790', '456 Helper Avenue, Johannesburg', '1988-08-22 00:00:00', 'Cooking, Music, Art', 'Creative activities, Music, Social work', 'Weekends 9-6, Weekdays 6-9 PM', 'Tom Johnson', '+27123456803', 'Completed', 'Advanced Training, First Aid', 'Enjoys creative and social activities', 'Great with group activities and music sessions', 22, 4.9, 35, 'Advanced Volunteer Certification', 'EXPERT'),
('David Wilson', 'david.wilson@email.com', '+27123456804', '789 Service Road, Durban', '1992-03-10 00:00:00', 'Technology, Sports, Communication', 'Sports, Technology, Mentoring', 'Weekdays 5-9 PM, Weekends 8-4', 'Sarah Wilson', '+27123456805', 'Completed', 'Basic Training', 'Prefers technology and activity sessions', 'Good with younger elderly who are active', 8, 4.6, 12, 'Volunteer Certification', 'INTERMEDIATE'),
('Lisa Anderson', 'lisa.anderson@email.com', '+27123456806', '321 Care Street, Pretoria', '1985-12-05 00:00:00', 'Nursing, Companionship, Art', 'Healthcare, Art therapy, Social work', 'Weekdays 9-3, Weekends flexible', 'Mark Anderson', '+27123456807', 'Completed', 'Nursing Background, Advanced Training', 'Specializes in health-related companionship', 'Has nursing background, very caring', 18, 4.9, 28, 'Nursing Certification, Volunteer Certification', 'EXPERT'),
('Michael Chen', 'michael.chen@email.com', '+27123456808', '654 Support Lane, Port Elizabeth', '1995-07-18 00:00:00', 'Technology, Languages, Music', 'Programming, Languages, Music', 'Weekends 10-6, Weekdays 7-9 PM', 'Anna Chen', '+27123456809', 'In Progress', 'Basic Training', 'Prefers technology and language sessions', 'Young volunteer, very enthusiastic', 5, 4.4, 8, 'Basic Certification', 'BEGINNER');
