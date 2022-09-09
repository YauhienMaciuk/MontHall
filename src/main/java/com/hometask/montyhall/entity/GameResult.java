package com.hometask.montyhall.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class GameResult {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Game game;
    @NotNull
    private Boolean pickedBoxWasChanged;
    @NotNull
    private Boolean win;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Boolean getPickedBoxWasChanged() {
        return pickedBoxWasChanged;
    }

    public void setPickedBoxWasChanged(Boolean pickedBoxWasChanged) {
        this.pickedBoxWasChanged = pickedBoxWasChanged;
    }

    public Boolean getWin() {
        return win;
    }

    public void setWin(Boolean win) {
        this.win = win;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameResult that = (GameResult) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(game, that.game) &&
                Objects.equals(pickedBoxWasChanged, that.pickedBoxWasChanged) &&
                Objects.equals(win, that.win);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, game, pickedBoxWasChanged, win);
    }

    @Override
    public String toString() {
        return "GameResult{" +
                "id=" + id +
                ", game=" + game +
                ", pickedBoxWasChanged=" + pickedBoxWasChanged +
                ", win=" + win +
                '}';
    }
}
