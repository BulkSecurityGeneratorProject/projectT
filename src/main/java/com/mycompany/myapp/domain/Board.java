package com.mycompany.myapp.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Board.
 */
@Entity
@Table(name = "board")
public class Board implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @OneToMany(mappedBy = "board")
    private Set<Mark> marks = new HashSet<>();
    @OneToMany(mappedBy = "board")
    private Set<Player> players = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWidth() {
        return width;
    }

    public Board width(Integer width) {
        this.width = width;
        return this;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public Board height(Integer height) {
        this.height = height;
        return this;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Set<Mark> getMarks() {
        return marks;
    }

    public Board marks(Set<Mark> marks) {
        this.marks = marks;
        return this;
    }

    public Board addMarks(Mark mark) {
        this.marks.add(mark);
        mark.setBoard(this);
        return this;
    }

    public Board removeMarks(Mark mark) {
        this.marks.remove(mark);
        mark.setBoard(null);
        return this;
    }

    public void setMarks(Set<Mark> marks) {
        this.marks = marks;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public Board players(Set<Player> players) {
        this.players = players;
        return this;
    }

    public Board addPlayer(Player player) {
        this.players.add(player);
        player.setBoard(this);
        return this;
    }

    public Board removePlayer(Player player) {
        this.players.remove(player);
        player.setBoard(null);
        return this;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Board board = (Board) o;
        if (board.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), board.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Board{" +
            "id=" + getId() +
            ", width=" + getWidth() +
            ", height=" + getHeight() +
            "}";
    }
}
