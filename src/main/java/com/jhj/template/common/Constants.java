package com.jhj.template.common;

public class Constants {

    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";

    public enum RESPONSE {
        SUCCESS("200", "SUCCESS"), FAIL("300", "FAIL");

        private final String code;
        private final String message;

        RESPONSE(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return this.code;
        }

        public String getMessage() {
            return this.message;
        }

    }
}
