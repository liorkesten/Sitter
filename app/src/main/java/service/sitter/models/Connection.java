package service.sitter.models;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 *
 */
public class Connection implements Serializable {

    private String uuid;
    private String creationTimestamp;
    private String sideAUId;
    private String sideBUId;

    // Connection is default Ctor. needed for Firestore.
    public Connection() {

    }

    /**
     * Ctor for Connection
     *
     * @param userA
     * @param userB
     */
    public Connection(User userA, User userB) {

        uuid = UUID.randomUUID().toString();

        this.creationTimestamp = Instant.now().toString();
        this.sideAUId = userA.getUuid();
        this.sideBUId = userB.getUuid();
    }

    public String getUuid() {
        return uuid;
    }

    public String getCreationTimestamp() {
        return creationTimestamp;
    }

    public String getSideAUId() {
        return sideAUId;
    }

    public String getSideBUId() {
        return sideBUId;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setCreationTimestamp(String creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public void setSideAUId(String sideAUId) {
        this.sideAUId = sideAUId;
    }

    public void setSideBUId(String sideBUId) {
        this.sideBUId = sideBUId;
    }
}
