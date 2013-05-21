/**
 * Copyright 2013 lazydog.org.
 *
 * This file is part of JDNSaaS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lazydog.jdnsaas.internal.repository;

import java.sql.DriverManager;
import java.sql.SQLNonTransientConnectionException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.lazydog.jdnsaas.model.Resolver;
import org.lazydog.jdnsaas.model.TSIGKey;
import org.lazydog.jdnsaas.model.TSIGKeyAlgorithm;
import org.lazydog.jdnsaas.model.View;
import org.lazydog.jdnsaas.model.Zone;
import org.lazydog.jdnsaas.spi.repository.JDNSaaSRepository;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import org.unitils.reflectionassert.ReflectionComparatorMode;

/**
 * Java DNS as a Service (JDNSaaS) repository implementation test.
 * 
 * @author  Ron Rickard
 */
public class JDNSaaSRepositoryImplTest {
    
    private static final String TEST_FILE = "dataset.xml";
    private JDNSaaSRepository jdnsaasRepository;

    public JDNSaaSRepositoryImplTest() throws Exception {
        
        // Ensure the derby.log file is in the target directory.
        System.setProperty("derby.system.home", "./target");
        
        // Create the JDNSaaS database.
        DriverManager.getConnection("jdbc:derby:memory:./target/jdnsaas;create=true");
        
        // Get the JDNSaaS repository.
        jdnsaasRepository = JDNSaaSRepositoryImpl.newInstance("jdnsaasTest");
    }

    @AfterClass
    public static void afterClass() throws Exception {

        // Drop the jdnsaas database.
        try {
            DriverManager.getConnection("jdbc:derby:memory:./target/jdnsaas;drop=true");
        } catch (SQLNonTransientConnectionException e) {
            // Ignore.
        }
    }

    @Before
    public void beforeTest() throws Exception {

        // Get the database connection.
        IDatabaseConnection databaseConnection = this.getDatabaseConnection();
        
        // Refresh the dataset in the database.
        DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, getDataSet());
        
        // Close the database connection.
        databaseConnection.close();

        // Since the database was modified outside of the entity manager control, clear the entities and the cache.
        ((JDNSaaSRepositoryImpl)this.jdnsaasRepository).getEntityManager().clear();
        ((JDNSaaSRepositoryImpl)this.jdnsaasRepository).getEntityManager().getEntityManagerFactory().getCache().evictAll();
    }
 
    @Test
    public void testFindResolvers() throws Exception {
        TSIGKey tsigKey1 = new TSIGKey();
        tsigKey1.setAlgorithm(TSIGKeyAlgorithm.HMAC_MD5);
        tsigKey1.setId(new Integer(1));
        tsigKey1.setName("tsigkeyname1");
        tsigKey1.setValue("tsigkeyvalue1");
        Resolver resolver1 = new Resolver();
        resolver1.setHostName("hostname1");
        resolver1.setId(new Integer(1));
        resolver1.setLocalHostName("localhostname1");
        resolver1.setPort(new Integer(53));
        resolver1.setTSIGKey(tsigKey1);
        Resolver resolver2 = new Resolver();
        resolver2.setHostName("hostname2");
        resolver2.setId(new Integer(2));
        resolver2.setLocalHostName("localhostname2");
        resolver2.setPort(new Integer(53));
        Resolver resolver3 = new Resolver();
        resolver3.setHostName("hostname3");
        resolver3.setId(new Integer(3));
        resolver3.setPort(new Integer(53));
        List<Resolver> expectedResolvers = Arrays.asList(resolver1, resolver2, resolver3);
        assertReflectionEquals(expectedResolvers, this.jdnsaasRepository.findResolvers(), ReflectionComparatorMode.LENIENT_ORDER);
    }
     
    @Test
    public void testFindTSIGKeys() throws Exception {
        TSIGKey tsigKey1 = new TSIGKey();
        tsigKey1.setAlgorithm(TSIGKeyAlgorithm.HMAC_MD5);
        tsigKey1.setId(new Integer(1));
        tsigKey1.setName("tsigkeyname1");
        tsigKey1.setValue("tsigkeyvalue1");
        TSIGKey tsigKey2 = new TSIGKey();
        tsigKey2.setAlgorithm(TSIGKeyAlgorithm.HMAC_SHA256);
        tsigKey2.setId(new Integer(2));
        tsigKey2.setName("tsigkeyname2");
        tsigKey2.setValue("tsigkeyvalue2");
        TSIGKey tsigKey3 = new TSIGKey();
        tsigKey3.setAlgorithm(TSIGKeyAlgorithm.HMAC_SHA512);
        tsigKey3.setId(new Integer(3));
        tsigKey3.setName("tsigkeyname3");
        tsigKey3.setValue("tsigkeyvalue3");
        List<TSIGKey> expectedTSIGKeys = Arrays.asList(tsigKey1, tsigKey2, tsigKey3);
        assertReflectionEquals(expectedTSIGKeys, this.jdnsaasRepository.findTSIGKeys(), ReflectionComparatorMode.LENIENT_ORDER);
    }

    @Test
    public void testFindView() throws Exception {
        TSIGKey tsigKey1 = new TSIGKey();
        tsigKey1.setAlgorithm(TSIGKeyAlgorithm.HMAC_MD5);
        tsigKey1.setId(new Integer(1));
        tsigKey1.setName("tsigkeyname1");
        tsigKey1.setValue("tsigkeyvalue1");
        Resolver resolver1 = new Resolver();
        resolver1.setHostName("hostname1");
        resolver1.setId(new Integer(1));
        resolver1.setLocalHostName("localhostname1");
        resolver1.setPort(new Integer(53));
        resolver1.setTSIGKey(tsigKey1);
        Resolver resolver2 = new Resolver();
        resolver2.setHostName("hostname2");
        resolver2.setId(new Integer(2));
        resolver2.setLocalHostName("localhostname2");
        resolver2.setPort(new Integer(53));
        Resolver resolver3 = new Resolver();
        resolver3.setHostName("hostname3");
        resolver3.setId(new Integer(3));
        resolver3.setPort(new Integer(53));
        View expectedView = new View();
        expectedView.setId(1);
        expectedView.setName("view1");
        expectedView.setResolvers(Arrays.asList(resolver1, resolver2, resolver3));
        assertReflectionEquals(expectedView, this.jdnsaasRepository.findView("view1"), ReflectionComparatorMode.LENIENT_ORDER);
        expectedView = new View();
        expectedView.setId(2);
        expectedView.setName("view2");
        expectedView.setResolvers(Arrays.asList(resolver1));
        assertReflectionEquals(expectedView, this.jdnsaasRepository.findView("view2"), ReflectionComparatorMode.LENIENT_ORDER);
    }

    @Test
    public void testFindViewNames() throws Exception {
        List<String> expectedViewNames = new ArrayList<String>(Arrays.asList("view1", "view2"));
        assertReflectionEquals(expectedViewNames, this.jdnsaasRepository.findViewNames(), ReflectionComparatorMode.LENIENT_ORDER);
    }

    @Test
    public void testFindZone() throws Exception {
        TSIGKey queryTSIGKey = new TSIGKey();
        queryTSIGKey.setAlgorithm(TSIGKeyAlgorithm.HMAC_MD5);
        queryTSIGKey.setId(new Integer(1));
        queryTSIGKey.setName("tsigkeyname1");
        queryTSIGKey.setValue("tsigkeyvalue1");
        TSIGKey transferTSIGKey = new TSIGKey();
        transferTSIGKey.setAlgorithm(TSIGKeyAlgorithm.HMAC_SHA256);
        transferTSIGKey.setId(new Integer(2));
        transferTSIGKey.setName("tsigkeyname2");
        transferTSIGKey.setValue("tsigkeyvalue2");
        TSIGKey updateTSIGKey = new TSIGKey();
        updateTSIGKey.setAlgorithm(TSIGKeyAlgorithm.HMAC_SHA512);
        updateTSIGKey.setId(new Integer(3));
        updateTSIGKey.setName("tsigkeyname3");
        updateTSIGKey.setValue("tsigkeyvalue3");
        Resolver resolver1 = new Resolver();
        resolver1.setHostName("hostname1");
        resolver1.setId(new Integer(1));
        resolver1.setLocalHostName("localhostname1");
        resolver1.setPort(new Integer(53));
        resolver1.setTSIGKey(queryTSIGKey);
        Resolver resolver2 = new Resolver();
        resolver2.setHostName("hostname2");
        resolver2.setId(new Integer(2));
        resolver2.setLocalHostName("localhostname2");
        resolver2.setPort(new Integer(53));
        Resolver resolver3 = new Resolver();
        resolver3.setHostName("hostname3");
        resolver3.setId(new Integer(3));
        resolver3.setPort(new Integer(53));
        View view = new View();
        view.setId(1);
        view.setName("view1");
        view.setResolvers(Arrays.asList(resolver1, resolver2, resolver3));
        Zone expectedZone = new Zone();
        expectedZone.setId(new Integer(1));
        expectedZone.setName("zone1");
        expectedZone.setQueryTSIGKey(queryTSIGKey);
        expectedZone.setTransferTSIGKey(transferTSIGKey);
        expectedZone.setUpdateTSIGKey(updateTSIGKey);
        expectedZone.setView(view);
        assertReflectionEquals(expectedZone, this.jdnsaasRepository.findZone("view1", "zone1"), ReflectionComparatorMode.LENIENT_ORDER);
        expectedZone = new Zone();
        expectedZone.setId(new Integer(2));
        expectedZone.setName("zone2");
        expectedZone.setTransferTSIGKey(transferTSIGKey);
        expectedZone.setUpdateTSIGKey(updateTSIGKey);
        expectedZone.setView(view);
        assertReflectionEquals(expectedZone, this.jdnsaasRepository.findZone("view1", "zone2"), ReflectionComparatorMode.LENIENT_ORDER);
        view = new View();
        view.setId(2);
        view.setName("view2");
        view.setResolvers(Arrays.asList(resolver1));
        expectedZone = new Zone();
        expectedZone.setId(new Integer(3));
        expectedZone.setName("zone2");
        expectedZone.setQueryTSIGKey(queryTSIGKey);
        expectedZone.setUpdateTSIGKey(updateTSIGKey);
        expectedZone.setView(view);
        assertReflectionEquals(expectedZone, this.jdnsaasRepository.findZone("view2", "zone2"), ReflectionComparatorMode.LENIENT_ORDER);
        expectedZone = new Zone();
        expectedZone.setId(new Integer(4));
        expectedZone.setName("zone3");
        expectedZone.setQueryTSIGKey(queryTSIGKey);
        expectedZone.setTransferTSIGKey(transferTSIGKey);
        expectedZone.setView(view);
        assertReflectionEquals(expectedZone, this.jdnsaasRepository.findZone("view2", "zone3"), ReflectionComparatorMode.LENIENT_ORDER);
    }

    @Test
    public void testFindZoneNames() throws Exception {
        List<String> expectedZoneNames = new ArrayList<String>(Arrays.asList("zone1", "zone2"));
        assertReflectionEquals(expectedZoneNames, this.jdnsaasRepository.findZoneNames("view1"), ReflectionComparatorMode.LENIENT_ORDER);
        expectedZoneNames = new ArrayList<String>(Arrays.asList("zone2", "zone3"));
        assertReflectionEquals(expectedZoneNames, this.jdnsaasRepository.findZoneNames("view2"), ReflectionComparatorMode.LENIENT_ORDER);
    }

    private IDatabaseConnection getDatabaseConnection() throws Exception {
        return new DatabaseConnection(((JDNSaaSRepositoryImpl)this.jdnsaasRepository).getConnection());
    }
    
    private static IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(Thread.currentThread().getContextClassLoader().getResourceAsStream(TEST_FILE));
    }
}
