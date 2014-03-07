/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.saga.sync.gameserver.service;

import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import net.saga.sync.gameserver.util.jpa.NonTXOperator;
import net.saga.sync.gameserver.util.jpa.TXOperator;
import net.saga.sync.gameserver.vo.Match;
import net.saga.sync.gameserver.vo.Player;
import org.jboss.resteasy.annotations.cache.NoCache;

/**
 *
 * @author summers
 */
@Path("/match")
public class MatchService {

    @Inject
    private EntityManagerFactory emf;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @NoCache
    @Path("/open")
    public List<Match> findOpenMatchs() {
        return NonTXOperator.execute(
                emf,
                (EntityManager em) -> em.createQuery("from Match where open = true").getResultList()
        );

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @NoCache
    @Path("/join/{matchId}")
    public Match joinMatch(@PathParam("matchId") Long matchId, Player player) {
        return TXOperator.execute(emf,
                (EntityManager em) -> {
                    Match match = em.find(Match.class, matchId);
                    match.getPlayers().add(player);
                    return em.merge(match);
                });

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @NoCache
    @Path("/{id}")
    public Match getMatch(@PathParam("id") Long id) {
        return NonTXOperator.execute(
                emf,
                (EntityManager em) -> em.find(Match.class, id)
        );

    }

    @POST
    @Path("/")
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    @NoCache
    public Match createMatch(Match match) {
        return TXOperator.execute(
                emf,
                (EntityManager em) -> em.merge(match)
        );

    }

}
