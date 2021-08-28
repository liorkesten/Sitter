package service.sitter.models;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Request {
    private final String requestId;
    // publisher is the parent that published the request
    private String publisherId;
    // receiver is the babysitter that approved the request
    private String receiverId;
    private Date date;
    private Date startTime;
    private Date endTime;
    private String location; // TODO Location
    private List<Child> children;
    private int pricePerHour;
    private String description;
    private RequestStatus status;

    public Request(String publisherId, String receiverId, Date startTime, Date endTime,
                   String location, List<Child> children, int pricePerHour, String description) {
        this.requestId = UUID.randomUUID().toString();
        this.publisherId = publisherId;
        this.receiverId = receiverId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.children = children;
        this.pricePerHour = pricePerHour;
        this.description = description;
    }

    public String getUUID() {
        return requestId;
    }
}
