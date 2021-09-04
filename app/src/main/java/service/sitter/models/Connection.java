package service.sitter.models;

import com.google.type.DateTime;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

/**
 *
 */
public class Connection {

    private final String uuid;
    private final Instant creationTimestamp;
    private final String sideAUId;
    private final String sideBUId;

    /**
     * Ctor for connection
     *
     * @param sideAUId
     * @param sideBUId
     */
    public Connection(String sideAUId, String sideBUId) {
        uuid = UUID.randomUUID().toString();

        this.creationTimestamp = Instant.now();
        this.sideAUId = sideAUId;
        this.sideBUId = sideBUId;
    }

    public String getUuid() {
        return uuid;
    }

    public Instant getCreationTimestamp() {
        return creationTimestamp;
    }

    public String getSideAUId() {
        return sideAUId;
    }

    public String getSideBUId() {
        return sideBUId;
    }
}
