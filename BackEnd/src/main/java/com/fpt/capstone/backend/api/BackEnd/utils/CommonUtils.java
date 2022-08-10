package com.fpt.capstone.backend.api.BackEnd.utils;


import com.fpt.capstone.backend.api.BackEnd.entity.Settings;
import com.fpt.capstone.backend.api.BackEnd.repository.SettingsRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@AllArgsConstructor
public class CommonUtils {

    private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    public static final String PATTERN_ddMMyyyy = "dd/MM/yyyy";

    private final SettingsRepository settingsRepository;

    public static final String ROLE_STUDENT = "Student";

    public Date stringToDate(String dateStr, String pattern) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.parse(dateStr);
    }

    public Set<Settings> getUserRoles(Set<String> strRoles) {
        Set<Settings> settings = new HashSet<>();

        if (strRoles == null) {
            Settings setting = settingsRepository.findRoleByValue(CommonUtils.ROLE_STUDENT).get();
            logger.info("add role USER");
            settings.add(setting);
        } else {
            strRoles.forEach(role -> {
                Settings settingDB = settingsRepository.findRoleByValue(role).orElse(null);
                if (settingDB != null) {
                    logger.info("add role " + role);
                    settings.add(settingDB);
                }
            });
        }
        return settings;
    }

    public String generateRandomCode(int codeSize) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(codeSize);

        for (int i = 0; i < codeSize; i++) {
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }

    public ArrayList<Long> stringToArray(String string) {
        ArrayList<Long> longArray = new ArrayList<>();
        if (string != null && !string.trim().isEmpty()) {
            String[] array = string.split(",");
            for (String item : array) {
                if (!item.trim().isEmpty()) {
                    longArray.add(Long.parseLong(item.trim()));
                }
            }
        }
        return longArray;
    }

    public String arrayToString(List<Long> longArray) {
        StringBuilder string = new StringBuilder(",");
        if(longArray == null || longArray.size() == 0){
            return string.toString();
        }
        for (Long along : longArray) {
            if(along != null) {
                string.append(along).append(",");
            }
        }
        return string.toString();
    }

    public Map<String,Integer> paging(Page<?> page,int currentPage){

        Map<String,Integer> meta = new HashMap<>();
        meta.put("totalItems", (int) page.getTotalElements());
        meta.put("totalPages",page.getTotalPages());
        meta.put("currentPage",currentPage);

        return meta;
    }
}
