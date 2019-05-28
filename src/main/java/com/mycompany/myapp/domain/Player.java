package com.mycompany.myapp.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.mycompany.myapp.domain.enumeration.PlayerType;

/**
 * A Player.
 */
@Entity
@Table(name = "player")
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private PlayerType type;

    @OneToMany(mappedBy = "player")
    private Set<Mark> marks = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("players")
    private Board board;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Player name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlayerType getType() {
        return type;
    }

    public Player type(PlayerType type) {
        this.type = type;
        return this;
    }

    public void setType(PlayerType type) {
        this.type = type;
    }

    public Set<Mark> getMarks() {
        return marks;
    }

    public Player marks(Set<Mark> marks) {
        this.marks = marks;
        return this;
    }

    public Player addMarks(Mark mark) {
        this.marks.add(mark);
        mark.setPlayer(this);
        return this;
    }

    public Player removeMarks(Mark mark) {
        this.marks.remove(mark);
        mark.setPlayer(null);
        return this;
    }

    public void setMarks(Set<Mark> marks) {
        this.marks = marks;
    }

    public Board getBoard() {
        return board;
    }

    public Player board(Board board) {
        this.board = board;
        return this;
    }

    public void setBoard(Board board) {
        this.board = board;
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
        Player player = (Player) o;
        if (player.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), player.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Player{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
