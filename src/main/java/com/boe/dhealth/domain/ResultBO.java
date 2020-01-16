package com.boe.dhealth.domain;




import java.util.List;

public class ResultBO {
    private String used_time;
    private String det_num;
    private String message;
    private String status;
    private String request_id;
    private List<DetInfoBO> det_info;

    public String getUsed_time() {
        return used_time;
    }

    public void setUsed_time(String used_time) {
        this.used_time = used_time;
    }

    public String getDet_num() {
        return det_num;
    }

    public void setDet_num(String det_num) {
        this.det_num = det_num;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public List<DetInfoBO> getDet_info() {
        return det_info;
    }

    public void setDet_info(List<DetInfoBO> det_info) {
        this.det_info = det_info;
    }
}
