package com.itrsa.costeroids.logic.dto.output;

import com.itrsa.costeroids.logic.engine.state.world.Bullet;

import java.io.Serializable;
import java.util.List;


public class StateDTO implements Serializable {
    List<ShipDTO> ships;
    List<BulletDTO> bullets;
    List<String> deaths;
    String winner;

    public StateDTO(List<ShipDTO> ships, List<BulletDTO> bullets, List<String> deaths, String win) {
        this.ships = ships;
        this.bullets = bullets;
        this.deaths = deaths;
        this.winner = win;
    }

    public StateDTO() {
    }

    public List<ShipDTO> getShips() {
        return ships;
    }

    public void setShips(List<ShipDTO> ships) {
        this.ships = ships;
    }

    public List<BulletDTO> getBullets() {
        return bullets;
    }

    public void setBullets(List<BulletDTO> bullets) {
        this.bullets = bullets;
    }

    public List<String> getDeaths() {
        return deaths;
    }

    public void setDeaths(List<String> deaths) {
        this.deaths = deaths;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}
