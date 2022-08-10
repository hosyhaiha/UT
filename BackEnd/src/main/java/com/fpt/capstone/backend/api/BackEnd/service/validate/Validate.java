package com.fpt.capstone.backend.api.BackEnd.service.validate;

import com.fpt.capstone.backend.api.BackEnd.dto.*;
import com.fpt.capstone.backend.api.BackEnd.dto.class_users.ClassUserInputDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.classes.ClassesInputDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.milestones.MilestoneInputDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.setting.SettingInputDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.subject.SubjectInputDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.subject_setting.SubjectSettingInputDTO;
import com.fpt.capstone.backend.api.BackEnd.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;


@Service
public class Validate {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SettingsRepository settingsRepository;
    @Autowired
    private IterationsRepository iterationsRepository;
    @Autowired
    private FeaturesRepository featuresRepository;
    @Autowired
    private SubjectsRepository subjectsRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ClassesRepository classesRepository;
    @Autowired
    private FunctionsRepository functionsRepository;
    @Autowired
    private ClassSettingsRepository classSettingsRepository;

    @Autowired
    private SubjectSettingsRepository subjectSettingsRepository;

    public boolean validate(String value, String regex) {
        return value.matches(regex);
    }

    public void validateSubject(SubjectInputDTO subjectsInput) throws Exception {
        //ID validate on edit

        //Name
        if (ObjectUtils.isEmpty(subjectsInput.getName())) {
            throw new Exception("Subjects Name cannot be empty!");
        }
        if (!validate(subjectsInput.getName(), String.valueOf(ConstantsRegex.NAME_PATTERN))) {
            throw new Exception("Subject name is not contain special characters and must have 5-255 character!");
        }
        //Code
        if (ObjectUtils.isEmpty(subjectsInput.getCode())) {
            throw new Exception("Subjects Code cannot be empty!");
        }
        if (!validate(subjectsInput.getCode(), String.valueOf(ConstantsRegex.CODE_PATTERN))) {
            throw new Exception("Subject Code must have 3-50 character and don't have special characters!");
        }
        if (ObjectUtils.isEmpty(subjectsInput.getAuthorId())) {
            throw new Exception("Manager ID cannot be empty!");
        }
        if (!subjectsInput.getAuthorId().matches(ConstantsRegex.NUMBER_PATTERN.toString())) {
            throw new Exception("Manager ID must be integer and bigger than 0!");
        }
        if (ObjectUtils.isEmpty(subjectsInput.getStatus())
                || !validate(subjectsInput.getStatus(), String.valueOf(ConstantsRegex.STATUS_PATTERN))) {
            throw new Exception("Subject Status must be active or inactive only!");
        }
    }

    public void validateIterations(IterationsInputDTO iterationsInputDTO) throws Exception {
        if (!subjectsRepository.existsById(Integer.valueOf(iterationsInputDTO.getSubjectId()))) {
            throw new Exception("Subject not found!");
        }
        //Name
        if (ObjectUtils.isEmpty(iterationsInputDTO.getName())) {
            throw new Exception("Iterations name can't be empty!");
        }
        if (!validate(iterationsInputDTO.getName(), String.valueOf(ConstantsRegex.NAME_PATTERN))) {
            throw new Exception("Iterations name is not contain special characters!");
        }
        //evaluationWeight
        if (ObjectUtils.isEmpty(iterationsInputDTO.getEvaluationWeight())) {
            throw new Exception("Evaluation weight can't be empty!");
        }
        if (!validate(iterationsInputDTO.getEvaluationWeight(), String.valueOf(ConstantsRegex.DECIMAL2_PATTERN))
                || Float.valueOf(iterationsInputDTO.getEvaluationWeight()) < 0
                || Float.valueOf(iterationsInputDTO.getEvaluationWeight()) >= 1) {
            throw new Exception("Evaluation weight must be decimal number format 0.xy, bigger than 0, less than 1!");
        }
        //is ongoing
        if(ObjectUtils.isEmpty(iterationsInputDTO.getIsOngoing())){
            iterationsInputDTO.setIsOngoing("0");
        }
    }


    public void validateRegisterUser(UserRegisterDTO userDTO) throws Exception {
        String email = userDTO.getEmail();
        String[] parts = email.split("@");

        if (!validate(userDTO.getEmail(), String.valueOf(ConstantsRegex.EMAIL_PATTERN))) {
            throw new Exception("Email must be format abc@fpt.edu.vn");
        }
        if (!parts[1].equals("fpt.edu.vn")) {
            throw new Exception("Email must be @fpt.edu.vn!");
        }
//        if (!validate(userDTO.getFullName(), String.valueOf(ConstantsRegex.FULLNAME_PATTERN))) {
//            throw new Exception("Full name cannot be contain special character and number");
//        }
        if (ObjectUtils.isEmpty(userDTO.getBirthday())) {
            throw new Exception("Birthday cannot be empty!");
        }
        if (ObjectUtils.isEmpty(userDTO.getFullName().trim())) {
            throw new Exception("Full name cannot be empty!");
        }
        if (ObjectUtils.isEmpty(userDTO.getPassword())) {
            throw new Exception("Password cannot be empty!");
        }
        if (!validate(userDTO.getPassword(), String.valueOf(ConstantsRegex.PASSWORD_PATTERN))) {
            throw new Exception("Password must have minimum eight characters, at least one letter and one number!");
        }
        if (ObjectUtils.isEmpty(userDTO.getGender())
                || !validate(userDTO.getGender(), String.valueOf(ConstantsRegex.GENDER_PATTERN))) {
            throw new Exception("Gender  must be Male or Female only!");
        }
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new Exception("Username is already exist");
        }
    }

    public void validateUsersEdit(UsersDTO userDTO) throws Exception {

//        if (!validate(userDTO.getUsername(), String.valueOf(ConstantsRegex.USERNAME_PATTERN))) {
//            throw new Exception("Username must have 5-20 character and not contain special characters");
//        }
//        //cần check nếu trùng với hiện tại thì bỏ qua check duplicate
////        if(userRepository.findByUsername(userDTO.getUsername())!=null){
////            throw new Exception("Username is duplicate");
////        }
////        if (!validate(userDTO.getPassword(), String.valueOf(ConstantsRegex.PASSWORD_PATTERN))) {
////            throw new Exception("Password must have 8-20 character," +
////                    " must have uppercase, lowercase, number and special character ");
////        }
//        if (!StringUtils.isEmpty(userDTO.getFullName()) && (!validate(userDTO.getFullName(), String.valueOf(ConstantsRegex.FULLNAME_PATTERN)))) {
//            throw new Exception("Full name cannot be contain special character and number");
//        }
////        if (!StringUtils.isEmpty(userDTO.getBirthday()) && (!validate(userDTO.getBirthday(), String.valueOf(ConstantsRegex.DATE_PATTERN)))) {
////            throw new Exception("Birth day must be format ĐD-MM-YYYY");
////        }
//        if (!StringUtils.isEmpty(userDTO.getTel()) && (!validate(userDTO.getTel(), String.valueOf(ConstantsRegex.PHONE_PATTERN)))) {
//            throw new Exception("Phone number must have 10 character");
//        }
//        if (!StringUtils.isEmpty(userDTO.getEmail()) &&(!validate(userDTO.getEmail(), String.valueOf(ConstantsRegex.EMAIL_PATTERN)))) {
//            throw new Exception("Email must be format abc@xyz.edg");
//        }
//        if (!StringUtils.isEmpty(userDTO.getAvatarLink()) &&(!validate(userDTO.getAvatarLink(), String.valueOf(ConstantsRegex.LINK_PATTERN)))) {
//            throw new Exception("Wrong link");
//        }
//        if (!StringUtils.isEmpty(userDTO.getFacebookLink()) &&(!validate(userDTO.getFacebookLink(), String.valueOf(ConstantsRegex.LINK_PATTERN)))) {
//            throw new Exception("Wrong link");
//        }
//        //check if role=user => check setting id => loc tim ra nhung truong xua
//
//        if (userDTO.getSettingsId() != null &&(!validate(userDTO.getSettingsId().toString(), String.valueOf(ConstantsRegex.NUMBER_PATTERN)))) {
//            throw new Exception("Id must be a integer");
//        }
//        if (userDTO.getSettingsId() != null && !settingsRepository.existsById(userDTO.getSettingsId())) {
//            throw new Exception("Id of setting not found");
//        }
//        if (!StringUtils.isEmpty(userDTO.getStatus()) &&(!validate(userDTO.getStatus(), String.valueOf(ConstantsRegex.STATUS_PATTERN)))) {
//            throw new Exception("Status must be active or inactive only!");
//        }

    }

    public void validateSetting(SettingInputDTO settingsDTO) throws Exception {
        if (ObjectUtils.isEmpty(settingsDTO.getTitle())) {
            throw new Exception("Title cannot be empty!");
        }
        if (!validate(settingsDTO.getTitle(), String.valueOf(ConstantsRegex.FULLNAME_PATTERN))) {
            throw new Exception("Title cannot be contain special character and number!");
        }
        if (ObjectUtils.isEmpty(settingsDTO.getValue())) {
            throw new Exception("Value cannot be empty!");
        }
        if (ObjectUtils.isEmpty(settingsDTO.getTypeId())) {
            throw new Exception("TypeID cannot be empty!");
        }
        if (ObjectUtils.isEmpty(settingsDTO.getDisplayOrder())) {
            throw new Exception("DisplayOrder cannot be empty!");
        }
        if (ObjectUtils.isEmpty(settingsDTO.getStatus())
                || !validate(settingsDTO.getStatus(), String.valueOf(ConstantsRegex.STATUS_PATTERN))) {
            throw new Exception("Status must be active or inactive only!");
        }
    }

    public void validateSubjectSetting(SubjectSettingInputDTO subjectSettingInputDTO) throws Exception {
        //SubjectID
        if (ObjectUtils.isEmpty(subjectSettingInputDTO.getSubjectId())) {
            throw new Exception("SubjectId cannot be empty!");
        }
        if (!subjectSettingInputDTO.getSubjectId().matches(ConstantsRegex.NUMBER_PATTERN.toString())
                &&Integer.valueOf(subjectSettingInputDTO.getSubjectId())==0) {
            throw new Exception("SubjectId must be integer and bigger than 0!");
        }
        if (subjectsRepository.findBySubjectId(Integer.parseInt(subjectSettingInputDTO.getSubjectId())) == 0) {
            throw new Exception("Subject does not exist!");
        }
        //typeID
        if (ObjectUtils.isEmpty(subjectSettingInputDTO.getTypeId())) {
            throw new Exception("TypeId cannot be empty!");
        }
        if (!subjectSettingInputDTO.getTypeId().matches(ConstantsRegex.NUMBER_PATTERN.toString())) {
            throw new Exception("TypeId must be integer and bigger than 0!");
        }
        if (subjectSettingsRepository.findSubjectSettingsByTypeId(Integer.parseInt(subjectSettingInputDTO.getTypeId())) == 0) {
            throw new Exception("TypeId does not exist!");
        }
        //title
        if (ObjectUtils.isEmpty(subjectSettingInputDTO.getTitle())) {
            throw new Exception("Title cannot be empty!");
        }
        if (subjectSettingInputDTO.getTitle().length() > 255) {
            throw new Exception("Title too long!");
        }
        if (subjectSettingInputDTO.getTitle().length() < 4) {
            throw new Exception("Title too sort!");
        }
        //value
        if (ObjectUtils.isEmpty(subjectSettingInputDTO.getValue())) {
            throw new Exception("Value cannot be empty!");
        }
        if (subjectSettingInputDTO.getValue().length() > 255) {
            throw new Exception("Value too long!");
        }
        if (subjectSettingInputDTO.getValue().length() < 4) {
            throw new Exception("Value too sort!");
        }
        //display order
        if (ObjectUtils.isEmpty(subjectSettingInputDTO.getDisplayOrder())) {
            throw new Exception("Display order cannot be empty!");
        }
        if (!subjectSettingInputDTO.getDisplayOrder().matches(ConstantsRegex.NUMBER_PATTERN.toString())) {
            throw new Exception("Display order must be integer!");
        }
        //display order check trùng ở edit và add riêng
        if (ObjectUtils.isEmpty(subjectSettingInputDTO.getStatus())
                || !validate(subjectSettingInputDTO.getStatus(), String.valueOf(ConstantsRegex.STATUS_PATTERN))) {
            throw new Exception("Status must be active or inactive only! only!");
        }
    }

    public void validateChangePassword(UserChangePWDTO userChangePWDTO) throws Exception {
        if (ObjectUtils.isEmpty(userChangePWDTO.getOldPassword())) {
            throw new Exception("Old Password cannot be empty!");
        }
        if (ObjectUtils.isEmpty(userChangePWDTO.getNewPassword())) {
            throw new Exception("New Password cannot be empty!");
        }
        if (!userChangePWDTO.getNewPassword().matches(ConstantsRegex.PASSWORD_PATTERN.toString())) {
            throw new Exception("New Password must have minimum eight characters, at least one letter and one number!");
        }
        if (ObjectUtils.isEmpty(userChangePWDTO.getComfirmNewPassword())) {
            throw new Exception("Confirm New Password cannot be empty!");
        }
        if (!userChangePWDTO.getNewPassword().equals(userChangePWDTO.getComfirmNewPassword())) {
            throw new Exception("Try again, Confirm New Password not match NewPassword!");
        }
    }

    public void validateClasses(ClassesInputDTO classesInput) throws Exception {
        //Code
        if (ObjectUtils.isEmpty(classesInput.getCode())) {
            throw new Exception("Class code cannot be empty!");
        }
        if (!classesInput.getCode().matches(ConstantsRegex.CODE_PATTERN.toString())) {
            throw new Exception("Class code must have 3-50 character and do not have special character!");
        }
        //trainerId
        if (ObjectUtils.isEmpty(classesInput.getTrainerId())) {
            throw new Exception("Trainer id cannot be empty!");
        }
        if (!classesInput.getTrainerId().matches(ConstantsRegex.NUMBER_PATTERN.toString())) {
            throw new Exception("Trainer id must be integer and bigger than 0!");
        }
        if (userRepository.findTrainerById(Integer.parseInt(classesInput.getTrainerId())) == null) {
            throw new Exception("Trainer not found!");
        }
        //subjectID;
        if (ObjectUtils.isEmpty(classesInput.getSubjectId())) {
            throw new Exception("Subject Id cannot be empty!");
        }
        if (!classesInput.getSubjectId().matches(ConstantsRegex.NUMBER_PATTERN.toString())) {
            throw new Exception("Subject ID must be integer and bigger than 0!");
        }
        if (!subjectsRepository.existsById(Integer.valueOf(classesInput.getSubjectId()))) {
            throw new Exception("Subject not found!");
        }
        // Year
        if (ObjectUtils.isEmpty(classesInput.getYear())) {
            throw new Exception("Class year cannot be empty!");
        }
        if (!classesInput.getYear().matches(ConstantsRegex.NUMBER_PATTERN.toString())
                || Integer.valueOf(classesInput.getYear()) < 2006 || Integer.valueOf(classesInput.getYear()) > 2099) {
            throw new Exception("Class year must be integer and between 2006 -2099!");
        }
        // Term
        if (ObjectUtils.isEmpty(classesInput.getTerm())) {
            throw new Exception("Class term cannot be empty!");
        }
        if (!classesInput.getTerm().matches(ConstantsRegex.TERM_PATTERN.toString())) {
            throw new Exception("Class term must be spring, summer or winter only!");
        }
        //status
        if (ObjectUtils.isEmpty(classesInput.getStatus())) {
            throw new Exception("Class status cannot be empty!");
        }
        if (!classesInput.getStatus().matches(ConstantsRegex.STATUS02_PATTERN.toString())) {
            throw new Exception("Class status must be active, closed or cancelled!");
        }
        //block5Class
//        if (ObjectUtils.isEmpty(classesInput.getBlock5Class())) {
//            throw new Exception("Block5 Class cannot be empty!");
//        }
//        if (!classesInput.getBlock5Class().matches(ConstantsRegex.BLOCK5_PATTERN.toString())) {
//            throw new Exception("Block5 Class must be yes/no only!");
//        }
    }

    public void validateCriteria(EvaluationCriteriaDTO evaluationCriteriaDTO) throws Exception {
        if (!iterationsRepository.existsById(Integer.valueOf(evaluationCriteriaDTO.getIterationId()))) {
            throw new Exception("Iteration not found!");
        }
        if (ObjectUtils.isEmpty(evaluationCriteriaDTO.getEvaluationWeight())) {
            throw new Exception("Evaluation weight cannot be empty!");
        }
        if (evaluationCriteriaDTO.getEvaluationWeight().floatValue() < 0 && evaluationCriteriaDTO.getEvaluationWeight().floatValue() > 1) {
            throw new Exception("Evaluation weight in range of 0%-100%!");
        }
        if (evaluationCriteriaDTO.getTeamEvaluation() != 0 && evaluationCriteriaDTO.getTeamEvaluation() != 1) {
            throw new Exception("Choose yes/no for team evaluation!");
        }
        if (!validate(evaluationCriteriaDTO.getMaxLoc().toString(), String.valueOf(ConstantsRegex.NUMBER_PATTERN))) {
            throw new Exception("Max loc must be positive number!");
        }
        if (evaluationCriteriaDTO.getMaxLoc() < 0) {
            throw new Exception("Max loc must be more than 0!");
        }
    }

    public void validateProject(ProjectsDTO projectsDTO) throws Exception {
        if (ObjectUtils.isEmpty(projectsDTO.getCode())) {
            throw new Exception("Project code cannot be empty!");
        }
        if (ObjectUtils.isEmpty(projectsDTO.getName())) {
            throw new Exception("Project name cannot be empty!");
        }
        if (ObjectUtils.isEmpty(projectsDTO.getStatus())) {
            throw new Exception("Project status cannot be empty!");
        }
        if (ObjectUtils.isEmpty(projectsDTO.getGitlabUrl())) {
            throw new Exception("Please input git lab url!");
        }
        if (ObjectUtils.isEmpty(projectsDTO.getGitlabUrl())) {
            throw new Exception("Please input git token!");
        }

        if (!classesRepository.existsById(Integer.valueOf(projectsDTO.getClassId()))) {
            throw new Exception("Class not found!");
        }
        if (ObjectUtils.isEmpty(projectsDTO.getStatus())
                || !validate(projectsDTO.getStatus(), String.valueOf(ConstantsRegex.STATUS_PATTERN))) {
            throw new Exception("Status must be active or inactive only!");
        }
    }

    public void validateFunction(FunctionsDTO functionsDTO) throws Exception {
        if (ObjectUtils.isEmpty(functionsDTO.getName())) {
            throw new Exception("Function name cannot be empty!");
        }
//        if (!projectRepository.existsById(Integer.valueOf(functionsDTO.getProjectId()))) {
//            throw new Exception("Project not found!");
//        }
        if (!featuresRepository.existsById(Integer.valueOf(functionsDTO.getFeatureId()))) {
            throw new Exception("Feature not found!");
        }
//        if (!userRepository.existsById(Integer.valueOf(functionsDTO.getOwnerId()))) {
//            throw new Exception("User not found!");
//        }
        if (ObjectUtils.isEmpty(functionsDTO.getStatus())
                || !validate(functionsDTO.getStatus(), ConstantsRegex.FUNCTION_STATUS_PATTERN.toString())) {
            throw new Exception("Status must be pending/committed/evaluated/rejected/done only!");
        }
//        if (ObjectUtils.isEmpty(functionsDTO.getComplexity())
//                || !validate(functionsDTO.getComplexity(), ConstantsRegex.COMPLEXITY_STATUS_PATTERN.toString())){
//            throw new Exception("Complex must be complex/medium/simple only!");
//        }
        if (ObjectUtils.isEmpty(functionsDTO.getPriority())
                || !validate(functionsDTO.getPriority(), ConstantsRegex.PRIORITY_STATUS_PATTERN.toString())) {
            throw new Exception("Priority must be urgent/high/medium/low/lowest only!");
        }
    }

    public void validateClassUser(ClassUserInputDTO classUserInput) throws Exception {
        //ClassID
        if (ObjectUtils.isEmpty(classUserInput.getClassId())) {
            throw new Exception("Class id cannot be empty!");
        }
        if (!classUserInput.getClassId().matches(ConstantsRegex.NUMBER_PATTERN.toString())) {
            throw new Exception("Class id must be number!");
        }
        if (classesRepository.getById(Integer.valueOf(classUserInput.getClassId())) == null) {
            throw new Exception("Class does not exist!");
        }
        //ProjectID
        if (ObjectUtils.isEmpty(classUserInput.getProjectId())) {
            throw new Exception("ProjectId cannot be empty!");
        }
        if (!classUserInput.getProjectId().matches(ConstantsRegex.NUMBER_PATTERN.toString())) {
            throw new Exception("Project id must be number!");
        }
        if (projectRepository.getById(Integer.valueOf(classUserInput.getProjectId())) == null) {
            throw new Exception("Project does does not exist!");
        }
        //userID
        if (ObjectUtils.isEmpty(classUserInput.getUserId())) {
            throw new Exception("Student cannot be empty!");
        }
        if (!classUserInput.getUserId().matches(ConstantsRegex.NUMBER_PATTERN.toString())) {
            throw new Exception("User id must be number!");
        }
        if (userRepository.getById(Integer.valueOf(classUserInput.getUserId())) == null) {
            throw new Exception("Student does does not exist!");
        }
        //projectLeader
        if (ObjectUtils.isEmpty(classUserInput.getProjectLeader())) {
            throw new Exception("Project Leader cannot be empty!");
        }
        if (!classUserInput.getProjectLeader().matches(ConstantsRegex.PROJECTLEADER_PATTERN.toString())) {
            throw new Exception("Project Leader cannot be true or false only!");
        }
        //dropoutDate
//        if (ObjectUtils.isEmpty(classUserInput.getDropoutDate())) {
//            throw new Exception("Dropout Date cannot be empty!");
//        }
        if (!ObjectUtils.isEmpty(classUserInput.getDropoutDate()) && !classUserInput.getDropoutDate().matches(ConstantsRegex.DATE_PATTERN.toString())) {
            throw new Exception("Dropout Date must be format dd-MM-yyyy!");
        }
        // Note
        if (classUserInput.getNote() != null && classUserInput.getNote().length() > 255) {
            throw new Exception("The length of the note cannot be more than 255 characters!");
        }
        // ongoingEval
        if (!ObjectUtils.isEmpty(classUserInput.getOngoingEval())
                && !classUserInput.getOngoingEval().matches(ConstantsRegex.DECIMAL_PATTERN.toString())
                || Float.valueOf(classUserInput.getOngoingEval()) > 10) {
            throw new Exception("Ongoing Evaluation must be decimal number, the value must be between 0 and 10!");
        }
        // finalPresentEval
        if (!ObjectUtils.isEmpty(classUserInput.getFinalPresentEval())
                && !classUserInput.getFinalPresentEval().matches(ConstantsRegex.DECIMAL_PATTERN.toString())
                || Float.valueOf(classUserInput.getFinalPresentEval()) > 10) {
            throw new Exception("Final Present Evaluation must be number decimal, the value must be between 0 and 10!");
        }
        // finalTopicEval
        if (!ObjectUtils.isEmpty(classUserInput.getFinalTopicEval())
                && !classUserInput.getFinalTopicEval().matches(ConstantsRegex.DECIMAL_PATTERN.toString())
                || Float.valueOf(classUserInput.getFinalTopicEval()) > 10) {
            throw new Exception("Final Topic Evaluation must be number decimal, the value must be between 0 and 10!");
        }
        //status
        if (ObjectUtils.isEmpty(classUserInput.getStatus())
                || !classUserInput.getStatus().matches(ConstantsRegex.STATUS_PATTERN.toString())) {
            throw new Exception("Status must be active or inactive!");
        }
    }

    public String convertDDMMYYtoYYMMDD(String dateIn) throws ParseException {
        if (dateIn.isEmpty()) return null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        return sdf2.format(sdf.parse(dateIn));
    }

    public void validateFeature(FeaturesDTO featuresDTO) throws Exception {
        if (ObjectUtils.isEmpty(featuresDTO.getProjectId())) {
            throw new Exception("Project ID cannot be empty!");
        }
        if (projectRepository.getById(featuresDTO.getProjectId()) == null) {
            throw new Exception("Project not found!");
        }
        if (ObjectUtils.isEmpty(featuresDTO.getName())) {
            throw new Exception("Feature name cannot be empty!");
        }
        if (!featuresDTO.getName().matches(ConstantsRegex.NAME_PATTERN.toString())) {
            throw new Exception("Feature name is not contain special characters and must have 5-255 character!");
        }
        if (!featuresDTO.getStatus().matches(ConstantsRegex.STATUS_PATTERN.toString())) {
            throw new Exception("Status must be active or inactive!");
        }
    }

    public void validateMilestone(MilestoneInputDTO milestone) throws Exception {
        //iterationId
        if (ObjectUtils.isEmpty(milestone.getIterationId())) {
            throw new Exception("Iteration ID cannot be empty!");
        }
        if (iterationsRepository.getById(Integer.valueOf(milestone.getIterationId())) == null) {
            throw new Exception("Iteration not found!");
        }
        //classId
        if (ObjectUtils.isEmpty(milestone.getClassId())) {
            throw new Exception("Class ID cannot be empty!");
        }
        if (classesRepository.getById(Integer.valueOf(milestone.getClassId())) == null) {
            throw new Exception("Class not found!");
        }
        //title
        if (ObjectUtils.isEmpty(milestone.getTitle())) {
            throw new Exception("Title cannot be empty!");
        }
        if (!milestone.getTitle().matches(ConstantsRegex.NAME_PATTERN.toString())) {
            throw new Exception("Title cannot be contain special character and max length is 255 character!");
        }
        //description
        if (!ObjectUtils.isEmpty(milestone.getDescription())
                && milestone.getDescription().length() > 255) {
            throw new Exception("Description have max length is 255 character!");
        }
        //from
        if (ObjectUtils.isEmpty(milestone.getFrom())) {
            throw new Exception("Date from cannot be empty!");
        }
        if (!milestone.getFrom().matches(ConstantsRegex.DATE_PATTERN.toString())) {
            throw new Exception("Date from must be format dd-MM-yyyy!");
        }
        //to
        if (ObjectUtils.isEmpty(milestone.getTo())) {
            throw new Exception("Date to cannot be empty!");
        }
        if (!milestone.getTo().matches(ConstantsRegex.DATE_PATTERN.toString())) {
            throw new Exception("Date to must be format dd-MM-yyyy!");
        }
        //status
        if (ObjectUtils.isEmpty(milestone.getStatus())) {
            throw new Exception("Status cannot be empty!");
        }
        if (!milestone.getStatus().matches(ConstantsRegex.STATUS03_PATTERN.toString())) {
            throw new Exception("Status must be open, closed, cancelled!");
        }
    }

    public void validateClassSettings(ClassSettingsDTO classSettingsDTO)throws Exception {

        if (ObjectUtils.isEmpty(classSettingsDTO.getClassId())) {
            throw new Exception("Class cannot be empty!");
        }
        if (!classesRepository.findById(classSettingsDTO.getClassId()).isPresent()) {
            throw new Exception("Class not found!");
        }
        if (ObjectUtils.isEmpty(classSettingsDTO.getTypeId())) {
            throw new Exception("Type cannot be empty!");
        }
        if (ObjectUtils.isEmpty(classSettingsDTO.getTitle())) {
            throw new Exception("Title cannot be empty!");
        }

        if (ObjectUtils.isEmpty(classSettingsDTO.getValue())) {
            throw new Exception("Value cannot be empty!");
        }
        if (!classSettingsDTO.getStatus().matches(ConstantsRegex.STATUS_PATTERN.toString())) {
            throw new Exception("Status must be active or inactive!");
        }
    }
}
