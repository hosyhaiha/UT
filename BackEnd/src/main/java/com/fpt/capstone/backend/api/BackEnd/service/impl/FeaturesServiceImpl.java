package com.fpt.capstone.backend.api.BackEnd.service.impl;

import com.fpt.capstone.backend.api.BackEnd.dto.FeaturesDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.FeaturesListDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.Features;
import com.fpt.capstone.backend.api.BackEnd.entity.ResponsePaggingObject;
import com.fpt.capstone.backend.api.BackEnd.entity.RoleID;
import com.fpt.capstone.backend.api.BackEnd.entity.Users;
import com.fpt.capstone.backend.api.BackEnd.repository.FeaturesRepository;
import com.fpt.capstone.backend.api.BackEnd.repository.ProjectRepository;
import com.fpt.capstone.backend.api.BackEnd.service.FeaturesService;
import com.fpt.capstone.backend.api.BackEnd.service.UserService;
import com.fpt.capstone.backend.api.BackEnd.service.validate.Validate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FeaturesServiceImpl implements FeaturesService {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private FeaturesRepository featuresRepository;

    @Autowired
    private Validate validate;
    @Autowired
    private UserService userService;

    @Override
    public FeaturesDTO addFeature(FeaturesDTO featuresDTO) throws Exception {
        validate.validateFeature(featuresDTO);
        if (featuresRepository.findByNameAndProjectId(featuresDTO.getName(),featuresDTO.getProjectId()) != null) {
            throw new Exception("Feature name already exist on this project!");
        }
        Features features = modelMapper.map(featuresDTO, Features.class);
//        features.setProject(projectRepository.getById(featuresDTO.getProjectId()));
        featuresRepository.save(features);
        features = entityManager.find(Features.class, features.getId());
        return getFeatureDetail(features.getId());
    }


    @Override
    public FeaturesDTO updateFeature(FeaturesDTO featuresDTO) throws Exception {
        if (ObjectUtils.isEmpty(featuresDTO.getId())) {
            throw new Exception("ID cannot be empty!");
        }
        if (featuresRepository.getById(featuresDTO.getId()) == null) {
            throw new Exception("Feature not found!");
        }
        validate.validateFeature(featuresDTO);
        Features features = featuresRepository.getOne(featuresDTO.getId());
        if (featuresDTO.getName() != features.getName() && featuresDTO.getProjectId() != features.getProjectId()
                && featuresRepository.findByNameAndProjectId(featuresDTO.getName(),featuresDTO.getProjectId() ) != null) {
            throw new Exception("Feature name already exist on this project!");
        }
        features = modelMapper.map(featuresDTO, Features.class);
      //  features.setProject(projectRepository.getById(featuresDTO.getProjectId()));
        featuresRepository.save(features);
        features = entityManager.find(Features.class, features.getId());
        return getFeatureDetail(features.getId());
    }

    @Override
    public FeaturesDTO findById(int id) throws Exception {
        return null;
    }

    @Override
    public Page<FeaturesDTO> listBy(List<Integer> projectID, String name, String status, int page, int limit, ResponsePaggingObject response) throws Exception {
        Pageable pageable = PageRequest.of(page - 1, limit);
        List<Integer> userId = new ArrayList<>();
        Users users = userService.getUserLogin();
        if (Objects.equals(users.getSettings().getId(), RoleID.ADMIN) || Objects.equals(users.getSettings().getId(), RoleID.STUDENT)
                || Objects.equals(users.getSettings().getId(), RoleID.TRAINER)) {
            response.getPermission().setAdd(Boolean.TRUE);
            response.getPermission().setUpdate(Boolean.TRUE);
        }
        if (Objects.equals(users.getSettings().getId(), RoleID.STUDENT)
                || Objects.equals(users.getSettings().getId(), RoleID.TRAINER)) {
            userId.add(users.getId());
            return featuresRepository.search(projectID, userId, name, status, pageable);
        }

        return featuresRepository.search(projectID, userId, name, status, pageable);
    }

    @Override
    public FeaturesDTO getFeatureDetail(int id) {
        return featuresRepository.getFeatureDetail(id);
    }

    @Override
    public List<FeaturesListDTO> showFeatureList(String status, List<Integer> projectId) {
        if (ObjectUtils.isEmpty(status)) {
            status = null;
        }
        List<FeaturesListDTO> featuresListDTOS = featuresRepository.getLabelList(status, projectId);
        return featuresListDTOS;


    }
}
