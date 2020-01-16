
package com.boe.dhealth.domain;



import java.util.List;

public class JsonRootBean{

    private int person_num;
    private List<PersonInfoBO> person_info;
    private long log_id;

    public int getPerson_num() {
        return person_num;
    }

    public void setPerson_num(int person_num) {
        this.person_num = person_num;
    }

    public List<PersonInfoBO> getPerson_info() {
        return person_info;
    }

    public void setPerson_info(List<PersonInfoBO> person_info) {
        this.person_info = person_info;
    }

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }
}