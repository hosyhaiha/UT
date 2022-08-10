package com.fpt.capstone.backend.api.BackEnd.service.impl;


import com.fpt.capstone.backend.api.BackEnd.dto.AuthorListDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.UserInfoDto;
import com.fpt.capstone.backend.api.BackEnd.dto.UserList;
import com.fpt.capstone.backend.api.BackEnd.dto.UsersDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.Provider;
import com.fpt.capstone.backend.api.BackEnd.entity.Users;
import com.fpt.capstone.backend.api.BackEnd.repository.ClassUserRepository;
import com.fpt.capstone.backend.api.BackEnd.repository.SettingsRepository;
import com.fpt.capstone.backend.api.BackEnd.repository.UserRepository;
import com.fpt.capstone.backend.api.BackEnd.service.MailService;
import com.fpt.capstone.backend.api.BackEnd.service.UserService;
import com.fpt.capstone.backend.api.BackEnd.service.validate.ConstantsRegex;
import com.fpt.capstone.backend.api.BackEnd.service.validate.ConstantsStatus;
import com.fpt.capstone.backend.api.BackEnd.utils.security.JwtUtils;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.AllArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SettingsRepository settingsRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    EntityManager entityManager;
    @Autowired
    ClassUserRepository classUserRepository;

    //private final CommonUtils utils;

    private final PasswordEncoder passwordEncoder;

    // private final MailServiceImpl mailService;

    // private final DepartmentRepository departmentRepository;


    @Override
    public ResponseEntity<?> getUserInformation(String jwtToken) throws Exception {

        String email = jwtUtils.getUserNameFromJwtToken(jwtToken.substring(5));
        Users user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        String role = user.getSettings().getValue();
        return null;

//        UserInforResponse userInforResponse = UserInforResponse.builder().id(user.getId())
//                .email(email).fullName(user.getFullName())
//                .avatarUrl(user.getAvatarImage())
//                .dob(user.getDob()).gender(user.getGender())
//                .star(user.getStar()).roles(roles)
//                .department(department == null ? null
//                        : new UserInforResponse().departmentResponse(department.getId(), department.getName()))
//                .projects(new UserInforResponse().projectResponses(executes)).build();
//
//        return ResponseEntity.ok().body(
//                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
//                        .message(commonProperties.getMESSAGE_SUCCESS())
//                        .data(userInforResponse).build()
//        );
    }


    @Override
    public ResponseEntity<?> saveAvatarLink(String url, String token) throws Exception {
//        String email = jwtUtils.getUserNameFromJwtToken(token.substring(5));
//        User user = userRepository.findByEmail(email).orElse(null);
//        if (user == null) {
//            return ResponseEntity.ok().body(
//                    ApiResponse.builder().code(commonProperties.getCODE_UPDATE_FAILED())
//                            .message("Không thể cập nhật ảnh đại diện").build()
//            );
//        }
//        user.setAvatarImage(url);
//
//        userRepository.save(user);
//
//        return ResponseEntity.ok().body(
//                ApiResponse.builder().code(commonProperties.getCODE_UPDATE_SUCCESS())
//                        .message("Cập nhật avatar thành công").build()
//        );
        return null;
    }

    @Override
    public ResponseEntity<?> getAllUsers(String jwtToken) throws Exception {
        return null;
    }


    @Override
    public Page<UsersDTO> listBy(String email, String fullName, String rollNumber, String status, int page, int per_page) throws Exception {
        Pageable pageable = PageRequest.of(page - 1, per_page);
        if (ObjectUtils.isEmpty(status)) {
            status = null;
        }
        if (!ObjectUtils.isEmpty(status) && !status.matches(ConstantsRegex.STATUS_PATTERN.toString())) {
            throw new Exception("Input status Empty to search all or active/inactive");
        }
        return userRepository.search(email, fullName, rollNumber, status, pageable);

    }

    @Override
    public List<AuthorListDTO> listByAuthor() {
        return userRepository.listAuthor();
    }


    public List<AuthorListDTO> listTrainer(String status) {
        if (ObjectUtils.isEmpty(status)) {
            status = null;
        }
        return userRepository.listTrainer(status);
    }

    @Override
    public List<UserList> listUser(List<Integer> projectId) throws Exception {
        try {
            return userRepository.listUserInFeature(projectId);
        } catch (Exception e) {
            throw new Exception(e);
        }

    }

    @Override
    public UsersDTO findUserById(Integer id) {
        return userRepository.getUserById(id);
    }

    private Users convertToEntity(UsersDTO usersDTO) throws ParseException {
        Users users = modelMapper.map(usersDTO, Users.class);
//        users.setPassword(BCrypt.hashpw(usersDTO.getPassword(), BCrypt.gensalt(12)));
//        Date birthDate = new SimpleDateFormat("yyyy-mm-dd").parse(usersDTO.getBirthday());
//        users.setBirthday(birthDate);
        users.setSettings(settingsRepository.getById(usersDTO.getRoleId()));
        return users;
    }

    @Override
    public void updateByID(UsersDTO userDTO) throws Exception {

        //UserDetailsImpl currentUser= (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Users currentUser = userRepository.findByEmail(userName);
        //check ng dung truyen vo
        Users userDB = userRepository.findByEmail(userDTO.getEmail());
        userDTO.setPassword(userDB.getPassword());
        userDTO.setRole(userDB.getSettings().getValue());
        userDTO.setCreated(userDB.getCreated());
        userDTO.setCreatedBy(userDB.getCreatedBy());
        userDTO.setModified(userDB.getModified());
        userDTO.setModifiedBy(userDB.getModifiedBy());
        userDTO.setAvatarLink(userDB.getAvatarLink());
        //validate.validateUsersEdit(userDTO);
        Users users = convertToEntity(userDTO);
        Date date = new Date();
        users.setCreated(userDB.getCreated());
        users.setCreatedBy(userDB.getCreatedBy());
        users.setModified(date);
        users.setModifiedBy(currentUser.getId());
        users.setProvider(userDB.getProvider());

        if (userDB == null) {
            throw new Exception("User not exsit");
        }
        //check admin
        if (currentUser.getSettings().getId() == 7) {
            userRepository.save(users);
        } else {
            if (!currentUser.getEmail().equals(userDTO.getEmail())) {
                throw new Exception("User don't have permisstion");
            }
            int currentSettingID = currentUser.getSettings().getId();
            int settingIdInsert = userDTO.getRoleId();
            if (settingIdInsert != currentSettingID) {
                throw new Exception("User don't have permisstion to edit role");
            } else {
                userRepository.save(users);
            }

        }

    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        if (file.getSize() > 20000000) {
            throw new IOException("Update has not been successfully uploaded. Requires less than 20 MB ");
        }
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        System.out.println("convert " + file.getOriginalFilename() + " to " + convFile.getName());
        return convFile;
    }

    //ok
    private void checkFileExtension(String fileName) throws ServletException {
        if (fileName != null && !fileName.isEmpty() && fileName.contains(".")) {
            String[] allowedExt = {".jpg", ".jpeg", ".png", ".gif"};
            for (String ext : allowedExt) {
                if (fileName.endsWith(ext)) {
                    return;
                }
            }
            throw new ServletException("File must be an image");
        }
    }

    @Autowired
    ResourceLoader resourceLoader;

    public Users findById(Integer id) {
        return userRepository.getById(id);
    }

    @Override
    public String uploadAva(MultipartFile fileStream) throws Exception {
        try {
            // email+ngayfthangnamhphs
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
            Users users = userRepository.findByEmail(email);
            String bucketName = "slpm";
            File file = convertMultiPartToFile(fileStream);
            checkFileExtension(file.getName());
            String fileName = email + timeStamp;
            String filePath = fileStream.getOriginalFilename();
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream is = classLoader.getResourceAsStream("ancient-ceiling-352503-4ad907700d34.json");
            Credentials credentials = GoogleCredentials.fromStream(is);

            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
            BlobId blobId = BlobId.of(bucketName, fileName);
            BlobInfo blobInfo = storage.create(
                    BlobInfo
                            .newBuilder(blobId)
                            .setContentType("image/png")
                            // .setAcl(new ArrayList<>(Arrays.asList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))))
                            .build(), Files.readAllBytes(Paths.get(filePath))
                    //                     file.openStream()
            );

            System.out.println(blobInfo.getMediaLink());
            System.out.println(blobInfo.getSelfLink());
            System.out.println(blobInfo.getBlobId());

            Integer userId = users.getId();
            String avaLink = blobInfo.getBlobId().getName();
            userRepository.updateAva(userId, avaLink);


            String host = "https://storage.googleapis.com/slpm/";
            UsersDTO usersDTO = modelMapper.map(users, UsersDTO.class);
            usersDTO.setAvatarLink(host + users.getAvatarLink());

            return host + blobInfo.getName();
        } catch (IOException e) {
            throw new Exception(e.getMessage());

        }

    }

    @Override
    public List<UserInfoDto> getUserList(String status, String role) {
        return userRepository.listLabelValueUser(status, role);
    }

    @Autowired
    MailService mailService;

    @Override
    public void createUserImportExcel(String user) throws MessagingException, UnsupportedEncodingException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Users userLogin = userRepository.findByEmail(email);
        Date date = new Date();
        String randomPass = RandomString.make(12);
        Users users = new Users();
        String encodedPassword = passwordEncoder.encode(randomPass);
        users.setEmail(user);
        users.setProvider(Provider.LOCAL);
        users.setPassword(encodedPassword);
        users.setSettings(settingsRepository.getById(10));
        users.setCreated(date);
        users.setModified(date);
        users.setCreatedBy(userLogin.getId());
        users.setModifiedBy(userLogin.getId());
        users.setStatus(ConstantsStatus.active.toString());
        users.setEnabled(true);
        userRepository.save(users);
        mailService.sendMailInvite(user, randomPass);
    }

    @Override
    public Users getUserLogin() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = userRepository.findByEmail(email);
        return user;
    }


}
