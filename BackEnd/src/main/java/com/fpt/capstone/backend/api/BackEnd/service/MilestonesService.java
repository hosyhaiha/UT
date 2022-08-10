package com.fpt.capstone.backend.api.BackEnd.service;

import com.fpt.capstone.backend.api.BackEnd.dto.MilestonesListDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.MilestonesSyncDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.milestones.MilestoneInputDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.milestones.MilestonesDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.ResponsePaggingObject;
import org.springframework.data.domain.Page;

import java.util.List;


public interface MilestonesService {
    MilestonesDTO add(MilestoneInputDTO milestoneInput) throws Exception;

    MilestonesDTO showDetail(int id);

    MilestonesDTO edit(MilestoneInputDTO milestoneInput) throws Exception;

    Page<MilestonesDTO> listBy(List<Integer> iterationId, List<Integer> classId, List<Integer> trainerId, String title,
                               String status, int page, int limit, ResponsePaggingObject response) throws Exception;

    List<MilestonesListDTO> listMilestoneByClass(List<Integer> classId, List<Integer> projectId)throws Exception ;

    List<MilestonesSyncDTO> listMilestoneSync(List<Integer> projectId);

    String syncGitLab(List<Integer> classId, List<Integer> projectId) throws Exception;


    List<String> getGitLabMilestones(Integer gitlabId, String token) throws Exception;
}
