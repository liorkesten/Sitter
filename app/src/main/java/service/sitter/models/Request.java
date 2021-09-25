package service.sitter.models;

import com.google.android.libraries.places.api.model.Place;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class Request implements Serializable {

    private String uuid;
    // publisher is the parent that published the request
    private String publisherId;
    // receiver is the babysitter that approved the request
    private String receiverId;
    // date without time
    private String date;
    private String startTime;
    private String endTime;
    // TODO Location
    private Place location;
    private List<Child> children;
    private int pricePerHour;
    private String description;
    private RequestStatus status;

    // Request is default Ctor. needed for Firestore.
    public Request() {

    }

    public Request(String publisherId, LocalDate date, String startTime, String endTime,
                   Place location, List<Child> children, int pricePerHour, String description) {
        this.uuid = UUID.randomUUID().toString();
        this.publisherId = publisherId;
        this.date = (date != null) ? date.toString() : "";
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.children = children;
        this.pricePerHour = pricePerHour;
        this.description = description;

        status = RequestStatus.Pending;
    }


    @Override
    public String toString() {
        return "Request{" +
                "uuid='" + uuid + '\'' +
                ", publisherId='" + publisherId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", date=" + date +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", location=" + location +
                ", children=" + children +
                ", pricePerHour=" + pricePerHour +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }


    public String getUuid() {
        return uuid;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public String getReceiverId() {
        return receiverId;
    }


    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public Place getLocation() {
        return location;
    }

    public List<Child> getChildren() {
        return children;
    }

    public int getPricePerHour() {
        return pricePerHour;
    }

    public String getDescription() {
        return description;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setLocation(Place location) {
        this.location = location;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }

    public void setPricePerHour(int pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }
}
