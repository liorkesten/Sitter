package service.sitter.models;

import java.io.Serializable;
import java.util.UUID;

public class Recommendation implements Serializable {

    private String uuid;
    private Connection connection;
    private String provider;
    private float score;
    private boolean isUsed;
    private String recommendation;

    public Recommendation() {
    }

    public Recommendation(Connection connection, String provider, float score, String recommendation) {
        this.uuid = UUID.randomUUID().toString();
        this.connection = connection;
        this.provider = provider;
        this.score = score;
        this.recommendation = recommendation;
        this.isUsed = false;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }
}
