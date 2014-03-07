package net.saga.sync.gameserver.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "MATCHES")
public class Match implements Serializable {

    private static final long serialVersionUID = -6087437982347747043L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_seq")
    @SequenceGenerator(name = "hibernate_seq", sequenceName = "hibernate_seq", allocationSize = 15, initialValue = 32)
    private Long id;

    @Lob
    @Column(name = "NAME")
    private String name;

    @Column(name = "IS_OPEN")
    private boolean open = true;

    @ManyToMany
    @JoinTable(name = "MATCH_PLAYERS", 
               joinColumns = @JoinColumn(name = "MATCH_ID"), 
               inverseJoinColumns = @JoinColumn(name = "PLAYER_ID"))
    private List<Player> players = new ArrayList<>(2);

    
    @Column(name = "IS_STARTED")
    private Boolean started = false;

    @Column(name = "MAX_PLAYERS")
    private Integer maxPlayers = 2;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean getOpen() {
        return this.open;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Boolean isStarted() {
        return started;
    }

    public void setStarted(Boolean started) {
        this.started = started;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

}
