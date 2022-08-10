package com.fpt.capstone.backend.api.BackEnd.service.impl;


import org.springframework.stereotype.Service;

/*
JWTUserDetailsService implements the Spring Security UserDetailsService interface.
It overrides the loadUserByUsername for fetching user details from the database using the username.
The Spring Security Authentication Manager calls this method for getting the user details from the database
when authenticating the user details provided by the user. Here we are getting the user details from a hardcoded
User List. In the next tutorial we will be adding the DAO implementation for fetching User Details from the Database.
Also the password for a user is stored in encrypted format using BCrypt.
Previously we have seen Spring Boot Security - Password Encoding Using Bcrypt.
Here using the Online Bcrypt Generator you can generate the Bcrypt for a password.
 */
@Service
public class JwtUserDetailsService {

//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private SettingsRepository settingsRepository;
//
//
//    @Autowired
//    private ModelMapper modelMapper;
//    @Autowired
//    private PasswordEncoder bcryptEncoder;
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    public boolean validateInput(String value, String regex) {
//        return value.matches(regex);
//    }
//
//    public CustomUserDetails loadUserByEmail(String email) throws Exception {
//        Users user = userRepository.findByEmail(email);
//        if (user == null) {
//            throw new Exception("User not found with email: " + email);
//        }
//        final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//        authorities.add((GrantedAuthority) new SimpleGrantedAuthority(user.getSettings().getValue()));
//        //return new User(user.getUsername(), user.getPassword(), authorities);
//        CustomUserDetails customUserDetails = new CustomUserDetails(user);
//        return customUserDetails;
//    }
//
//    public Integer loadUserIDByUsername(String email) throws UsernameNotFoundException {
//        Users user = userRepository.findByEmail(email);
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found with username: " + email);
//        }
////        final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
////        authorities.add((GrantedAuthority) new SimpleGrantedAuthority(user.getSettings().getValue()));
////        return new User(user.getUsername(), user.getPassword(), authorities);
//        CustomUserDetails customUserDetails = new CustomUserDetails(user);
//        return customUserDetails.getUsers().getId();
//    }
//    public Users createUser(UsersDTO usersDTO) throws Exception {
////        System.out.println("ok1");
//        if (checkDuplicateUsername(usersDTO.getEmail())) {
//            throw new Exception( "User is already exist");
//        }
//        if (!validateInput(usersDTO.getPassword(), ConstantsRegex.pass.toString())) {
//            throw new Exception("Invalid password");
//        }
////        if (!validate.validateEmail(usersDTO.getEmail())) {
////            throw new Exception("Invalid email");
////        }
//        Users users = modelMapper.map(usersDTO,Users.class);
//       // users.setUsername(usersDTO.getUsername());
//        users.setPassword(bcryptEncoder.encode(usersDTO.getPassword()));
//        //Setting =1 ->ROLE_STUDENT
//        users.setSettings(settingsRepository.getById(10));
//        return userRepository.save(users);
//    }
//
//    public void updateUser(UsersDTO usersDTO) {
////        Users users = mapper.map(usersDTO, Users.class);
////        users.setRole(getRole(usersDTO.getRoleId()));
////
//        java.sql.Timestamp date = new java.sql.Timestamp(System.currentTimeMillis());
//
////        userRepository.updateUser(usersDTO.getFullName(), usersDTO.getBirthday(), usersDTO.getTel(),
////                usersDTO.getEmail(), usersDTO.getAvatarLink(), usersDTO.get, date, 0, usersDTO.getId());
//    }
//
//
//    public void deleteUser(int id) {
//        userRepository.deleteById(id);
//    }
//
//
//    public boolean checkDuplicateUsername(String email) {
//        return userRepository.findByEmail(email) != null;
//    }


}
