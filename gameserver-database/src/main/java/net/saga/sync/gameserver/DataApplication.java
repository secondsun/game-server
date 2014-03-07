/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.saga.sync.gameserver;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author summers
 */
@ApplicationPath("/")
public class DataApplication extends Application {

    public static final String PERSISTENCE_UNIT = "game_server_datasource";

    @javax.enterprise.inject.Produces
    @ApplicationScoped
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);

}
