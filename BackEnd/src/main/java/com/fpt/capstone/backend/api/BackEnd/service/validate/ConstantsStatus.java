package com.fpt.capstone.backend.api.BackEnd.service.validate;




public enum ConstantsStatus {
    active("active"),
    inactive("inactive");

    private final String text;
    ConstantsStatus(String s) {
        text=s;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
