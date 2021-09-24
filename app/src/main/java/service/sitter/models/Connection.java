package service.sitter.models;

import java.time.Instant;
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
     * Ctor for Connection
     *
     * @param userA
     * @param userB
     */
    public Connection(User userA, User userB) {

        uuid = UUID.randomUUID().toString();

        this.creationTimestamp = Instant.now();
        this.sideAUId = userA.getUuid();
        this.sideBUId = userB.getUuid();
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
