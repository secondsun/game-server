package net.saga.sync.gameserver.service;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import net.saga.sync.gameserver.util.jpa.NonTXOperator;
import net.saga.sync.gameserver.util.jpa.TXOperator;
import net.saga.sync.gameserver.vo.Player;
import org.jboss.resteasy.spi.HttpRequest;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.IDToken;

@Path("/player")
public class PlayerService {

    @Inject
    private EntityManagerFactory emf;

    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{playerId}")
    public Player getPlayer(@PathParam("playerId") Long id) {
        return NonTXOperator.execute(
                emf,
                (EntityManager em) -> em.find(Player.class, id)
        );
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @Path("")
    public Player createPlayer(@Context HttpRequest req, Player player) {
        KeycloakSecurityContext session = (KeycloakSecurityContext) req.getAttribute(KeycloakSecurityContext.class.getName());
        IDToken token = session.getIdToken();
        if (player.getName() == null || player.getName().isEmpty()) {
            player.setName(token.getName());
        }
        return TXOperator.execute(emf, 
                                 ((EntityManager em) -> em.merge(player)
        ));

    }

}
