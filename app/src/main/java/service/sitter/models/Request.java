package service.sitter.models;

import android.location.Location;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Request {
    private final String uuid;
    // publisher is the parent that published the request
    private final String publisherId;
    // receiver is the babysitter that approved the request
    private String receiverId;
    // date without time
    private Date date;
    private int startTime;
    private int endTime;
    // TODO Location
    private Location location;
    private List<Child> children;
    private int pricePerHour;
    private String description;
    private RequestStatus status;

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

    public Request(String publisherId, Date date, LocalTime startTime, LocalTime endTime,
                   Location location, List<Child> children, int pricePerHour, String description) {
        this.uuid = UUID.randomUUID().toString();
        this.publisherId = publisherId;
        this.date = date;
        this.startTime = startTime.toSecondOfDay();
        this.endTime = endTime.toSecondOfDay();
        this.location = location;
        this.children = children;
        this.pricePerHour = pricePerHour;
        this.description = description;

        status = RequestStatus.Pending;
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

//    public LocalDate getDate() {
//        return date.toInstant()
//                .atZone(ZoneId.systemDefault())
//                .toLocalDate();
//    }

    public LocalTime getStartTime() {
        return LocalTime.ofSecondOfDay(startTime);
    }

    public LocalTime getEndTime() {
        return LocalTime.ofSecondOfDay(endTime);
    }

    public Location getLocation() {
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
}
