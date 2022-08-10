package com.fpt.capstone.backend.api.BackEnd.service;

import com.fpt.capstone.backend.api.BackEnd.dto.IterationListSearchDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.IterationsDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.IterationsInputDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.ResponsePaggingObject;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InterationsService {
    IterationsDTO addIterations(IterationsInputDTO iterationsInputDTO) throws Exception;

    IterationsDTO deleteIterations(int id) throws Exception;

    List<IterationsDTO> showIterationsList();

    IterationsDTO updateIterations(IterationsInputDTO iterationsInputDTO) throws Exception;

    Page<IterationsDTO> listBy(List<Integer> id, String name, List<Integer> manageId, String status, int page, int per_page, ResponsePaggingObject responsePaggingObject) throws Exception;

    public IterationsDTO getIterationDetail(int id) throws Exception;

    public List<IterationsDTO> showIterationList();

    public List<IterationListSearchDTO> listIteration(String status);
}
