package com.fpt.capstone.backend.api.BackEnd.service;

import com.fpt.capstone.backend.api.BackEnd.dto.EvaluationCriteriaDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.FeaturesDTO;
import com.fpt.capstone.backend.api.BackEnd.dto.FeaturesListDTO;
import com.fpt.capstone.backend.api.BackEnd.entity.ResponsePaggingObject;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FeaturesService {
    FeaturesDTO addFeature(FeaturesDTO featuresDTO) throws Exception;

    FeaturesDTO updateFeature(FeaturesDTO featuresDTO) throws Exception;

    FeaturesDTO findById(int id) throws Exception;

    Page<FeaturesDTO> listBy(List<Integer> projectID, String name, String status, int page, int limit, ResponsePaggingObject responsePaggingObject) throws Exception;
    public FeaturesDTO getFeatureDetail(int id);
    public List<FeaturesListDTO> showFeatureList(String status, List<Integer> projectId);
}
