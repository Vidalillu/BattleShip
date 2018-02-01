package com.company.salvo;

import java.util.*;
import javax.persistence.*;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private String shipType;

    @ElementCollection
    @Column(name = "location")
    private List<String> location = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    public Ship() { }

    public Ship(String type, List<String> cells, GamePlayer gamePlayer) {
        shipType = type;
        location = cells;
        this.gamePlayer = gamePlayer;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public List<String> getLocation() {
        return location;
    }

    public void setLocation(List<String> location) {
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

}
