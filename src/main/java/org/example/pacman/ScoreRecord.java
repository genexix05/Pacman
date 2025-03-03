package org.example.pacman;

import java.time.LocalDateTime;

public class ScoreRecord implements Comparable<ScoreRecord> {
    private String name;
    private int score;
    private LocalDateTime dateTime;

    public ScoreRecord(String name, int score, LocalDateTime dateTime) {
        this.name = name;
        this.score = score;
        this.dateTime = dateTime;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    // Orden descendente por score
    @Override
    public int compareTo(ScoreRecord other) {
        return Integer.compare(other.score, this.score);
    }
}
