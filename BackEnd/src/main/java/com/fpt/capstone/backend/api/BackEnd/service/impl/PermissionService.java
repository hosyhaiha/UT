package com.fpt.capstone.backend.api.BackEnd.service.impl;

import com.fpt.capstone.backend.api.BackEnd.entity.RoleID;
import com.fpt.capstone.backend.api.BackEnd.entity.Users;
import com.fpt.capstone.backend.api.BackEnd.repository.FunctionsRepository;
import com.fpt.capstone.backend.api.BackEnd.repository.MilestonesRepository;
import com.fpt.capstone.backend.api.BackEnd.repository.ProjectRepository;
import com.fpt.capstone.backend.api.BackEnd.repository.SubjectsRepository;
import com.fpt.capstone.backend.api.BackEnd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PermissionService {
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private FunctionsRepository functionsRepository;
    @Autowired
    private SubjectsRepository subjectsRepository;
    @Autowired
    private MilestonesRepository milestonesRepository;

    public Boolean hasTeamEvaluationPermission(Integer projectId) {
        Users auth = userService.getUserLogin();
        if (Objects.equals(auth.getSettings().getId(), RoleID.ADMIN)) return true;

        return false;
    }

    /**
     * Hàm dùng để lấy danh sách các projectId mà user được thao tác
     *
     * @param auth Users -> mặc định sẽ lấy auth nếu truyền vào null
     * @return
     */
    public List<Integer> getProjectAuthority(Users auth) throws Exception {
        if (auth == null) auth = userService.getUserLogin();

        List<Integer> authority;

        if (Objects.equals(auth.getSettings().getId(), RoleID.ADMIN)) {
            return null;
        }

        if (Objects.equals(auth.getSettings().getId(), RoleID.AUTHOR)) {
            authority = projectRepository.projectAuthority(null, auth.getId());
            return authority.isEmpty() ? new ArrayList<Integer>() {{
                add(-1);
            }} : authority;
        }

        if (Objects.equals(auth.getSettings().getId(), RoleID.TRAINER)) {
            authority = projectRepository.projectAuthority(auth.getId(), null);
            return authority.isEmpty() ? new ArrayList<Integer>() {{
                add(-1);
            }} : authority;
        }
        if (Objects.equals(auth.getSettings().getId(), RoleID.STUDENT)) {
            authority = projectRepository.projectAuthorityStudent(auth.getId());
            return authority.isEmpty() ? new ArrayList<Integer>() {{
                add(-1);
            }} : authority;
        }

        throw new Exception("you do not have permission to access this data!");
    }

    /**
     * Hàm dùng để lấy danh sách các functionId mà user được thao tác
     *
     * @param auth Users -> mặc định sẽ lấy auth nếu truyền vào null
     * @return
     */
    public List<Integer> getFunctionAuthority(Users auth) throws Exception {
        if (auth == null) auth = userService.getUserLogin();
        List<Integer> authority;

        if (Objects.equals(auth.getSettings().getId(), RoleID.ADMIN)) {
            return null;
        }

        if (Objects.equals(auth.getSettings().getId(), RoleID.AUTHOR)) {
            authority = functionsRepository.getFunctionAuthority(null, auth.getId(), null);
            return authority.isEmpty() ? new ArrayList<Integer>() {{
                add(-1);
            }} : authority;
        }

        if (Objects.equals(auth.getSettings().getId(), RoleID.TRAINER)) {
            authority = functionsRepository.getFunctionAuthority(null, null, auth.getId());
            return authority.isEmpty() ? new ArrayList<Integer>() {{
                add(-1);
            }} : authority;
        }

        if (Objects.equals(auth.getSettings().getId(), RoleID.STUDENT)) {
            authority = functionsRepository.getFunctionAuthority(auth.getId(), null, null);
            return authority.isEmpty() ? new ArrayList<Integer>() {{
                add(-1);
            }} : authority;
        }

        throw new Exception("you do not have permission to access this data!");
    }

    public List<Integer> getSubjectAuthority(Users auth)throws Exception {
        if (auth == null) auth = userService.getUserLogin();
        List<Integer> authority;

        if (Objects.equals(auth.getSettings().getId(), RoleID.ADMIN)) {
            return null;
        }

        if (Objects.equals(auth.getSettings().getId(), RoleID.AUTHOR)) {
            authority = subjectsRepository.getSubjectAuthorityRoleAuthor( auth.getId());
            return authority.isEmpty() ? new ArrayList<Integer>() {{
                add(-1);
            }} : authority;
        }

        if (Objects.equals(auth.getSettings().getId(), RoleID.TRAINER)) {
            authority = subjectsRepository.getSubjectAuthorityTrainerId( auth.getId());
            return authority.isEmpty() ? new ArrayList<Integer>() {{
                add(-1);
            }} : authority;
        }

        if (Objects.equals(auth.getSettings().getId(), RoleID.STUDENT)) {
            authority = subjectsRepository.getSubjectStudent(auth.getId());
            return authority.isEmpty() ? new ArrayList<Integer>() {{
                add(-1);
            }} : authority;
        }
        throw new Exception("you do not have permission to access this data!");
    }

    public List<Integer> getMilestoneAuthority(Users auth)throws Exception {
        if (auth == null) auth = userService.getUserLogin();
        List<Integer> authority;

        if (Objects.equals(auth.getSettings().getId(), RoleID.ADMIN)) {
            return null;
        }

        if (Objects.equals(auth.getSettings().getId(), RoleID.AUTHOR)) {
            authority = milestonesRepository.getMilestoneAuthor(auth.getId());
            return authority.isEmpty() ? new ArrayList<Integer>() {{
                add(-1);
            }} : authority;
        }

        if (Objects.equals(auth.getSettings().getId(), RoleID.TRAINER)) {
            authority = milestonesRepository.getMilestoneTrainer(auth.getId());
            return authority.isEmpty() ? new ArrayList<Integer>() {{
                add(-1);
            }} : authority;
        }

        if (Objects.equals(auth.getSettings().getId(), RoleID.STUDENT)) {
            authority = milestonesRepository.getMilestoneStudent(auth.getId());
            return authority.isEmpty() ? new ArrayList<Integer>() {{
                add(-1);
            }} : authority;
        }
        throw new Exception("you do not have permission to access this data!");
    }
}
