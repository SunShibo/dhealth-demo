package com.boe.dhealth.domain;


public class ValueBO {
    private Integer id; //value
    private Integer head; //头部侧倾
    private Integer shoulder; //高低肩
    private Integer spine; //脊柱侧弯
    private Integer hip; //盆骨侧倾
    private Integer leftLeg; //
    private Integer rightLeg; //
    private Integer headForward;//头部前倾
    private Integer kneeHyperextension;//盆骨前倾
    private Integer hunchback;//备用字段

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHead() {
        return head;
    }

    public void setHead(Integer head) {
        this.head = head;
    }

    public Integer getShoulder() {
        return shoulder;
    }

    public void setShoulder(Integer shoulder) {
        this.shoulder = shoulder;
    }

    public Integer getSpine() {
        return spine;
    }

    public void setSpine(Integer spine) {
        this.spine = spine;
    }

    public Integer getHip() {
        return hip;
    }

    public void setHip(Integer hip) {
        this.hip = hip;
    }

    public Integer getLeftLeg() {
        return leftLeg;
    }

    public void setLeftLeg(Integer leftLeg) {
        this.leftLeg = leftLeg;
    }

    public Integer getRightLeg() {
        return rightLeg;
    }

    public void setRightLeg(Integer rightLeg) {
        this.rightLeg = rightLeg;
    }

    public Integer getHeadForward() {
        return headForward;
    }

    public void setHeadForward(Integer headForward) {
        this.headForward = headForward;
    }

    public Integer getKneeHyperextension() {
        return kneeHyperextension;
    }

    public void setKneeHyperextension(Integer kneeHyperextension) {
        this.kneeHyperextension = kneeHyperextension;
    }

    public Integer getHunchback() {
        return hunchback;
    }

    public void setHunchback(Integer hunchback) {
        this.hunchback = hunchback;
    }
}
