package com.boe.dhealth.domain;

public class JdjsonBO {
    private String code;
    private String charge;
    private String remain;
    private String remainTimes;
    private String remainSeconds;
    private String msg;
    private ResultBO result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getRemain() {
        return remain;
    }

    public void setRemain(String remain) {
        this.remain = remain;
    }

    public String getRemainTimes() {
        return remainTimes;
    }

    public void setRemainTimes(String remainTimes) {
        this.remainTimes = remainTimes;
    }

    public String getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(String remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultBO getResult() {
        return result;
    }

    public void setResult(ResultBO result) {
        this.result = result;
    }
}
