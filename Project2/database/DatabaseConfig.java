public class DatabaseConfig {
    private static final String URL = "jdbc:mysql://localhost:3306/goldenconnect";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            logger.error("Database connection error: " + e.getMessage());
            throw new DatabaseException("Failed to connect to database");
        }
    }
}

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Override
    @Transactional
    public User createUser(UserDTO userDTO) {
        validateUserData(userDTO);
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPasswordHash(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        return userRepository.save(user);
    }
}