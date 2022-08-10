package com.fpt.capstone.backend.api.BackEnd.service.impl;

import com.fpt.capstone.backend.api.BackEnd.dto.IterationListSearchDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.IterationsDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.IterationsInputDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.subject.SubjectsListDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.Iterations;
import com.fpt.capstone.backend.api.BackEnd.entity.ResponsePaggingObject;
import com.fpt.capstone.backend.api.BackEnd.entity.RoleID;
import com.fpt.capstone.backend.api.BackEnd.entity.Users;
import com.fpt.capstone.backend.api.BackEnd.repository.IterationsRepository;
import com.fpt.capstone.backend.api.BackEnd.repository.SubjectsRepository;
import com.fpt.capstone.backend.api.BackEnd.service.InterationsService;
import com.fpt.capstone.backend.api.BackEnd.service.UserService;
import com.fpt.capstone.backend.api.BackEnd.service.validate.ConstantsRegex;
import com.fpt.capstone.backend.api.BackEnd.service.validate.ConstantsStatus;
import com.fpt.capstone.backend.api.BackEnd.service.validate.Validate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class IterationsServiceImpl implements InterationsService {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private IterationsRepository iterationsRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SubjectsRepository subjectsRepository;
    @Autowired
    private Validate validate;
    @Autowired
    private UserService userService;

    @Override
    public IterationsDTO getIterationDetail(int id) throws Exception {
        UserDetails u = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (u.getAuthorities().stream().findFirst().get().toString().equals("Author")) {
            List<SubjectsListDTO> subjectsListDTOS = subjectsRepository
                    .searchSubjectsByAuthor(u.getUsername());
            boolean isAccessDenied = true;
            for (SubjectsListDTO subjects : subjectsListDTOS) {
                if (id == subjects.getId()) {
                    isAccessDenied = false;
                    return iterationsRepository.getIterationsDetail(id);
                }
            }
            if (isAccessDenied) {
                throw new Exception("You don't have permission");
            }
        }
        return iterationsRepository.getIterationsDetail(id);
    }

    @Override
    public IterationsDTO addIterations(IterationsInputDTO iterationsInputDTO) throws Exception {
        validate.validateIterations(iterationsInputDTO);
        if (iterationsRepository.findByIterationsName(iterationsInputDTO.getName()) > 0) {
            throw new Exception("Iterations Name already exist!");
        }
        Iterations iterations = modelMapper.map(iterationsInputDTO, Iterations.class);
        //   iterations.setSubject(subjectsRepository.getById(Integer.valueOf(iterationsInputDTO.getSubjectId())));
        iterations.setStatus(ConstantsStatus.active.toString());
        iterationsRepository.save(iterations);
        iterations = entityManager.find(Iterations.class, iterations.getId());
        return getIterationDetail(iterations.getId());
    }

    @Override
    public IterationsDTO deleteIterations(int id) throws Exception {
        Iterations iterations = iterationsRepository.getOne(id);

        UserDetails u = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (u.getAuthorities().stream().findFirst().get().toString().equals("Author")) {
            List<SubjectsListDTO> subjectsListDTOS = subjectsRepository
                    .searchSubjectsByAuthor(u.getUsername());
            boolean isAccessDenied = true;
            for (SubjectsListDTO subjects : subjectsListDTOS) {
                if (id == subjects.getId()) {
                    isAccessDenied = false;
                    modelMapper.map(iterations, IterationsDTO.class);
                    iterations.setStatus(ConstantsStatus.inactive.toString());
                    iterationsRepository.save(iterations);
                    iterations = entityManager.find(Iterations.class, iterations.getId());
                    return getIterationDetail(iterations.getId());
                }
            }
            if (isAccessDenied) {
                throw new Exception("You don't have permission");
            }
        }
        modelMapper.map(iterations, IterationsDTO.class);
        iterations.setStatus(ConstantsStatus.inactive.toString());
        iterationsRepository.save(iterations);
        iterations = entityManager.find(Iterations.class, iterations.getId());
        return getIterationDetail(iterations.getId());
    }

    @Override
    public List<IterationsDTO> showIterationsList() {
        List<Iterations> iterations = iterationsRepository.findAll();
        List<IterationsDTO> iterationsDTOS = iterations.stream()
                .map(iteration -> modelMapper.map(iteration, IterationsDTO.class))
                .collect(Collectors.toList());
        return iterationsDTOS;
    }

    @Override
    public IterationsDTO updateIterations(IterationsInputDTO iterationsInputDTO) throws Exception {
        validate.validateIterations(iterationsInputDTO);
        if (ObjectUtils.isEmpty(iterationsInputDTO.getId())) {
            throw new Exception("ID can't be empty");
        }
        if (!iterationsInputDTO.getId().matches(ConstantsRegex.NUMBER_PATTERN.toString())) {
            throw new Exception("ID must be integer");
        }
        if (iterationsRepository.getIterationsDetail(Integer.valueOf(iterationsInputDTO.getId())) == null) {
            throw new Exception("Iteration not exist");
        }
        Iterations iterations = iterationsRepository.getOne(Integer.valueOf(iterationsInputDTO.getId()));
        if (!iterations.getName().equals(iterationsInputDTO.getName())) {
            if (iterationsRepository.findByIterationsName(iterationsInputDTO.getName()) > 0) {
                throw new Exception("Iteration Name already exist ");
            } else {
                iterations.setName(iterationsInputDTO.getName());
            }
        }
        iterations = modelMapper.map(iterationsInputDTO, Iterations.class);
        //iterations.setSubject(subjectsRepository.getById(Integer.valueOf(iterationsInputDTO.getSubjectId())));
        iterationsRepository.save(iterations);
        iterations = entityManager.find(Iterations.class, iterations.getId());
        return getIterationDetail(iterations.getId());
    }


    @Override
    public Page<IterationsDTO> listBy(List<Integer> id, String name, List<Integer> manageId, String status, int page, int per_page, ResponsePaggingObject response) throws Exception {
        Pageable pageable = PageRequest.of(page - 1, per_page);
        if (!ObjectUtils.isEmpty(status) && !status.matches(ConstantsRegex.STATUS_PATTERN.toString())) {
            throw new Exception("Input status Empty to search all or active/inactive");
        }
        Users users = userService.getUserLogin();


        if (Objects.equals(users.getSettings().getId(), RoleID.ADMIN) || Objects.equals(users.getSettings().getId(), RoleID.AUTHOR)) {
            response.getPermission().setAdd(Boolean.TRUE);
            response.getPermission().setUpdate(Boolean.TRUE);
        }
        if (Objects.equals(users.getSettings().getId(), RoleID.AUTHOR)) {
            manageId = new ArrayList<>();
            manageId.add(users.getId());
            return iterationsRepository.search(id, name, manageId, status, pageable);
        }

        return iterationsRepository.search(id, name, manageId, status, pageable);
    }

    public List<IterationsDTO> showIterationList() {
        List<Iterations> iterations = iterationsRepository.findAll();
        List<IterationsDTO> iterationsDTOS = iterations.stream()
                .map(iterations1 -> modelMapper.map(iterations1, IterationsDTO.class))
                .collect(Collectors.toList());

        return iterationsDTOS;
    }

    @Override
    public List<IterationListSearchDTO> listIteration(String status) {
        List<IterationListSearchDTO> iterationListSearchDTOS = iterationsRepository.showAll(status);

        return iterationListSearchDTOS;
    }

}
