package service.sitter.models;

public class Ranking {
    private int count;
    private int total;

    public float getRank() {
        return count / (float) total;
    }

//    public void AddRank(RankValue rank) {
//        count += 1;
//        total += rank.;
//    }
}
