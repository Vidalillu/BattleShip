package com.company.salvo;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Score {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private Date finishDate;
    private Integer score;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    public Score() { }

    public Score(Player player, Game game, Integer playerScore, Date finishDate) {
        this.player = player;
        this.game = game;
        this.score = playerScore;
        this.finishDate = finishDate;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
