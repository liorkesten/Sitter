package service.sitter.models;

import java.io.Serializable;
import java.util.UUID;

public class Recommendation implements Serializable {

    private final String uuid;

    public Recommendation() {
        this.uuid = UUID.randomUUID().toString();
    }


    public String getUuid() {
        return uuid;
    }
}
