package com.fpt.capstone.backend.api.BackEnd.service;


import com.fpt.capstone.backend.api.BackEnd.dto.AddUpdateLogDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.FunctionTrackingSubmitListDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.TrackingsDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.UpdateLogDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.Trackings;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TrackingsService {
    void addTracking(List<Integer> functionList, Integer milestoneId, Integer assigneeId) throws Exception;

    TrackingsDTO deleteTracking(int id);

    List<TrackingsDTO> showTracking();

    void updateTracking(Integer trackingId, Integer newAssigneeId) throws Exception;

    TrackingsDTO findById(int id) throws Exception;

    Page<TrackingsDTO> listBy(List<Integer> projectId,
                              List<Integer> featureId,
                              List<Integer> milestoneId,
                              List<Integer> assigneeId,
                              List<Integer> assignerId,
                              List<Integer> functionId,
                              List<Integer> classId,
                              String status, int page, int per_page) throws Exception;

    public TrackingsDTO getTrackingDetail(Integer id);

    List<TrackingsDTO> showTrackingList(String status);

    void addLog(Trackings trackings, Integer newAssigneeId) throws Exception;

    void submitTracking(MultipartFile fileStream, List<FunctionTrackingSubmitListDTO> FunctionTrackingSubmitListDTO,Integer milestoneId,Integer projectId) throws Exception;

    String getMilestoneName(Integer functionId);

    String getEvalComment(Integer functionId);

    String getFunctionName(Integer functionId);

    AddUpdateLogDTO addLogUpdate(AddUpdateLogDTO updateLogDTO);

    AddUpdateLogDTO updateLogUpdate(AddUpdateLogDTO updateLogDTO);

    Integer getCurrClass(Integer functionId);
}
