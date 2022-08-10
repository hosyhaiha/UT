package com.fpt.capstone.backend.api.BackEnd.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModelStatus {
    public final static String STATUS_PENDING = "pending";
    public final static String STATUS_COMMITTED = "committed";
    public final static String STATUS_SUBMITTED = "submitted";
    public final static String STATUS_EVALUATED = "evaluated";
    public final static String STATUS_REJECTED = "rejected";
    public static final String[] SUBMIT_STATUS_HAS_TEAM_EVAL = new String[]{
            STATUS_SUBMITTED,
            STATUS_EVALUATED
    };

    public static final Map<String, String> NEXT_STATUS_STEP = new HashMap<String, String>() {{
        put(STATUS_PENDING, STATUS_COMMITTED);
        put(STATUS_COMMITTED, STATUS_SUBMITTED);
        put(STATUS_SUBMITTED, STATUS_EVALUATED);
    }};

    public final static String STATUS_ACTIVE = "active";
    public final static String STATUS_INACTIVE = "inactive";

    public final static Integer IS_ONGOING = 1;
    public final static Integer IS_PROJECT_LEADER = 1;

    public final static Integer COMPLEXITY_SETTING_TYPE_ID = 11;
    public final static Integer QUALITY_SETTING_TYPE_ID = 12;

    public final static String COMPLEXITY_TITLE = "complexity";
    public final static String QUALITY_TITLE = "quality";

    public final static Map<String, Integer> SETTING_TYPE_MAPPER = Stream.of(new Object[][] {
            { COMPLEXITY_TITLE, COMPLEXITY_SETTING_TYPE_ID },
            { QUALITY_TITLE, QUALITY_SETTING_TYPE_ID },
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (Integer) data[1]));

    // Milestone
    public final static String MILESTONE_STATUS_CANCELLED = "cancelled";
    public final static Integer IS_TEAM_EVALUATION = 1;

    public final static String SUBMIT_STATUS_PENDING = "pending";

}
