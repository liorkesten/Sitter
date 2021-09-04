package service.sitter.models;

import java.util.UUID;

public class Recommendation {

    private final String uuid;

    public Recommendation() {
        this.uuid = UUID.randomUUID().toString();
    }


    public String getUuid() {
        return uuid;
    }
}
