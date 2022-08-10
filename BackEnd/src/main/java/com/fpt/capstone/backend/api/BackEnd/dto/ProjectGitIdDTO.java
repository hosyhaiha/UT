package com.fpt.capstone.backend.api.BackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectGitIdDTO {
    private Integer projectId;
    private Integer classId;
    private Integer gitId;
    private String token;
    private List<MilestonesSyncDTO> milestones;
    private List<String> gitLabMilestones;

    public ProjectGitIdDTO(Integer projectId, Integer classId, Integer gitId, String token) {
        this.projectId = projectId;
        this.classId = classId;
        this.gitId = gitId;
        this.token=token;
    }
}
