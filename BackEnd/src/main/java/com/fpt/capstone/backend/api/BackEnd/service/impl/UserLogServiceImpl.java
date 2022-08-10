package com.fpt.capstone.backend.api.BackEnd.service.impl;

import com.fpt.capstone.backend.api.BackEnd.dto.UpdateLogDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.UserLogsDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.Milestones;
import com.fpt.capstone.backend.api.BackEnd.repository.MilestonesRepository;
import com.fpt.capstone.backend.api.BackEnd.repository.UserLogsRespository;
import com.fpt.capstone.backend.api.BackEnd.service.UserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserLogServiceImpl implements UserLogService {
    @Autowired
    UserLogsRespository userLogsRespository;

    @Override
    public List<UserLogsDTO> getListUserLog(Integer id) {
        List<String> listGroupType = new ArrayList<>();
        listGroupType.add("history");
        listGroupType.add("update");
        List<String> listAction = new ArrayList<>();
        listAction.add("changeData");
        listAction.add("addData");

        List<UserLogsDTO> userLogsDTOS = userLogsRespository.getUserLogsByTrackingId(id, "trackings", listGroupType, listAction);
        return userLogsDTOS;
    }

    @Autowired
    MilestonesRepository milestonesRepository;

    @Override
    public Page<UpdateLogDTO> listBy(Integer functionId, int page, int per_page) throws Exception {

        Pageable pageable = PageRequest.of(page - 1, per_page);

        Page<UpdateLogDTO> userLogsDTOS = userLogsRespository.logList(functionId,pageable);
        userLogsDTOS.forEach(t -> {
            if (t.getRefColumnName().equals("milestone_id")) {
                if (t.getNewValue() != null) {
                    Milestones milestones = new Milestones();
                    milestones = milestonesRepository.getById(Integer.valueOf(t.getNewValue()));
                    t.setMilestonTitle(milestones.getTitle());
                }
            }
        });
        return userLogsDTOS;
//        Page<UpdateLogDTO> pages = new PageImpl<UpdateLogDTO>(userLogsDTOS, pageable, userLogsDTOS.size());
//        return pages;

    }

}
