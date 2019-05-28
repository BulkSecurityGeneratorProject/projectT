package com.mycompany.myapp.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Mark.
 */
@Entity
@Table(name = "mark")
public class Mark implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "x")
    private Integer x;

    @Column(name = "y")
    private Integer y;

    @ManyToOne
    @JsonIgnoreProperties("marks")
    private Board board;

    @ManyToOne
    @JsonIgnoreProperties("marks")
    private Player player;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getX() {
        return x;
    }

    public Mark x(Integer x) {
        this.x = x;
        return this;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public Mark y(Integer y) {
        this.y = y;
        return this;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Board getBoard() {
        return board;
    }

    public Mark board(Board board) {
        this.board = board;
        return this;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Player getPlayer() {
        return player;
    }

    public Mark player(Player player) {
        this.player = player;
        return this;
    }

    public void setPlayer(Player player) {
        this.player = player;
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
        Mark mark = (Mark) o;
        if (mark.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mark.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Mark{" +
            "id=" + getId() +
            ", x=" + getX() +
            ", y=" + getY() +
            "}";
    }
}
