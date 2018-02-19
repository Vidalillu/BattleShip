package com.company.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Email;

import java.util.*;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import static java.util.stream.Collectors.*;


@Entity
public class Player {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private String userName;
    private String email;
    private String password;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setPlayer(this);
        gamePlayers.add(gamePlayer);
    }

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<Score> scores;

    public void addScore(Score score) {
        score.setPlayer(this);
        scores.add(score);
    }

    public Player() { }

    public Player(String user, String mail, String pass) {
        userName = user;
        email = mail;
        password = pass;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String toString() {
        return userName;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Score getScores(Game game) {
        return scores.stream().filter(score -> score.getGame().equals(game)).findFirst().orElse(null);
    }

    public int totalScores(Player player) {
        return scores.stream().collect(summingInt(Score::getScore));
    }

    public Long wonScores(Player player) {
        return scores.stream().filter(score -> score.getScore().equals(3)).collect(counting());
    }

    public Long lostScores(Player player) {
        return scores.stream().filter(score -> score.getScore().equals(0)).collect(counting());
    }

    public Long tiedScores(Player player) {
        return scores.stream().filter(score -> score.getScore().equals(1)).collect(counting());
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }
}
