package service.sitter.utils.calls;

import java.util.Date;

public class Call {
    private final String phNumber;
    String callDate;
    Date callDayTime;
    String callDuration;
    CallType callType;

    public Call(String phNumber, String callDate, Date callDayTime, String callDuration, CallType callType) {
        this.phNumber = phNumber;
        this.callDate = callDate;
        this.callDayTime = callDayTime;
        this.callDuration = callDuration;
        this.callType = callType;
    }

    public String getPhNumber() {
        return phNumber;
    }


    public String getCallDate() {
        return callDate;
    }

    public void setCallDate(String callDate) {
        this.callDate = callDate;
    }

    public Date getCallDayTime() {
        return callDayTime;
    }

    public void setCallDayTime(Date callDayTime) {
        this.callDayTime = callDayTime;
    }

    public String getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }

    public CallType getCallType() {
        return callType;
    }

    public void setCallType(CallType callType) {
        this.callType = callType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Call call = (Call) o;
        return phNumber.equals(call.phNumber);
    }

    @Override
    public int hashCode() {
        return phNumber.hashCode();
    }

    @Override
    public String toString() {
        return "Call{" +
                "phNumber='" + phNumber + '\'' +
                ", callDate='" + callDate + '\'' +
                ", callDayTime=" + callDayTime +
                ", callDuration='" + callDuration + '\'' +
                ", callType=" + callType +
                '}';
    }
}
