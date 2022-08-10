package com.fpt.capstone.backend.api.BackEnd.dto;

import com.fpt.capstone.backend.api.BackEnd.entity.Updates;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrackingRespone{
        private TrackingsDTO trackingsDTO;
        private List<Updates> updatesList;
}
