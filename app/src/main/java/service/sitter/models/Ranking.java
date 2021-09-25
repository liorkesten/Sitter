package service.sitter.models;

import java.io.Serializable;

public class Ranking implements Serializable {
    private int count;
    private int total;

    public float getRank() {
        return count / (float) total;
    }

    public void AddRank(RankValue rank) {
        count += 1;
        total += rank.ordinal();
    }
}
