package net.saga.sync.gameserver.test;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import net.saga.sync.gameserver.service.MatchService;
import net.saga.sync.gameserver.service.PlayerService;
import net.saga.sync.gameserver.util.jpa.TXOperator;
import net.saga.sync.gameserver.vo.Match;
import net.saga.sync.gameserver.vo.Player;
import org.apache.derby.tools.ij;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.jpa.internal.EntityManagerImpl;
import org.jglue.cdiunit.CdiRunner;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(CdiRunner.class)
public class TestMatchService {

    /**
     * The factory that produces entity manager.
     */
    @Produces
    private static EntityManagerFactory mEmf;
    /**
     * The entity manager that persists and queries the DB.
     */

    private static FlatXmlDataSet mDataset;

    @Inject
    private MatchService matchService;

    @Inject
    private PlayerService playerService;
    
    @BeforeClass
    public static void initTestFixture() throws Exception {
        mEmf = Persistence.createEntityManagerFactory("game_server_datasource");
        
        TXOperator.execute(mEmf, (EntityManager em) -> {
            ((EntityManagerImpl) (em)).getSession().doWork((Connection connection) -> {
                try {
                    ij.runScript(connection,
                            TestMatchService.class.getResourceAsStream("/data/create_db.derby.sql"),
                            "UTF-8", System.out, "UTF-8");

                    //Loads the data set from a file named students-datasets.xml
                    mDataset = new FlatXmlDataSetBuilder().build(Thread.currentThread()
                            .getContextClassLoader()
                            .getResourceAsStream("games-datasets.xml"));

                } catch (UnsupportedEncodingException | DataSetException ex) {
                    Logger.getLogger(TestMatchService.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex);
                }
            });
            return null;
        });

    }

    @Before
    public void init() throws Exception {

        TXOperator.execute(mEmf, (EntityManager em) -> {
            ((EntityManagerImpl) (em)).getSession().doWork((Connection connection) -> {
                try {
                    DatabaseOperation.CLEAN_INSERT.execute(new DatabaseConnection(connection), mDataset);
                } catch (DatabaseUnitException | SQLException ex) {
                    Logger.getLogger(TestMatchService.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex);
                }
            });
            return null;
        });

    }

    /**
     * Cleans up the session.
     */
    @AfterClass
    public static void closeTestFixture() {
        mEmf.close();
    }

    @Test(timeout = 1000L)
    public void testGetMatchs() {
        List<Match> matchs = matchService.findOpenMatchs();
        Assert.assertEquals(1, matchs.size());
        Match match = matchs.get(0);
        Assert.assertNotNull(match.getName());
        Assert.assertTrue(match.getOpen());
    }

    @Test(timeout = 1000L)
    public void testGetMatch() {
        Match match = matchService.getMatch(1L);
        Assert.assertEquals("Match 1", match.getName());
        Assert.assertEquals(1L, (long) match.getId());
    }

    @Test(timeout = 1000L)
    public void testCreateMatch() {
        
        Match match = new Match();
        match.setName("Match 3");
        
        match = matchService.createMatch(match);
        Assert.assertEquals("Match 3", match.getName());
        Assert.assertNotNull(match.getId());
    }

    @Test(timeout = 1000L)
    public void testJoinMatch() {
        Match match = new Match();
        match.setName("Match 3");
        
        match = matchService.createMatch(match);
        Player player1 = playerService.getPlayer(1L);
        match = matchService.joinMatch(match.getId(), player1);
        
        Assert.assertEquals(1, match.getPlayers().size());
        
    }

    
}
