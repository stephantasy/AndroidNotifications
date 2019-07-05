package com.sby.android.notification.dto;

import java.util.Collections;
import java.util.List;

public class HttpError {
    private Integer Status;
    private String Reason;
    private List<String> Reasons;

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public List<String> getReasons() {
        return Reasons;
    }

    public void setReasons(List<String> reasons) {
        Reasons = reasons;
    }

    public HttpError() {
    }

    public HttpError(Integer status, String reason) {
        Status = status;
        Reason = reason;
        Reasons = Collections.emptyList();
    }

    public HttpError(Integer status, String reason, List<String> reasons) {
        Status = status;
        Reason = reason;
        Reasons = reasons;
    }
}
