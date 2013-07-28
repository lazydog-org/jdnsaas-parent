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
import org.jboss.weld.environment.se.Weld;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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
    private static JDNSaaSRepository jdnsaasRepository;

    @BeforeClass
    public static void beforeClass() throws Exception {
        
        // Ensure the derby.log file is in the target directory.
        System.setProperty("derby.system.home", "./target");
        
        // Create the JDNSaaS database.
        DriverManager.getConnection("jdbc:derby:memory:./target/jdnsaas;create=true");

        // Get the JDNSaaS repository.
        jdnsaasRepository = new Weld().initialize().instance().select(JDNSaaSRepository.class).get();
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

        // Since the database was modified outside of the entity manager control, clear the entities and the cache.
        ((JDNSaaSRepositoryImpl)jdnsaasRepository).getEntityManager().clear();
        ((JDNSaaSRepositoryImpl)jdnsaasRepository).getEntityManager().getEntityManagerFactory().getCache().evictAll();
    }
 
    @Test
    public void testFindResolvers() {
        TSIGKey tsigKey1 = new TSIGKey();
        tsigKey1.setAlgorithm(TSIGKeyAlgorithm.HMAC_MD5);
        tsigKey1.setId(1);
        tsigKey1.setName("tsigkeyname1");
        tsigKey1.setValue("tsigkeyvalue1");
        Resolver resolver1 = new Resolver();
        resolver1.setAddress("10.0.0.1");
        resolver1.setId(1);
        resolver1.setLocalAddress("172.16.0.1");
        resolver1.setPort(53);
        Resolver resolver2 = new Resolver();
        resolver2.setAddress("10.0.0.2");
        resolver2.setId(2);
        resolver2.setPort(53);
        List<Resolver> expectedResolvers = Arrays.asList(resolver1, resolver2);
        assertReflectionEquals(expectedResolvers, jdnsaasRepository.findResolvers(), ReflectionComparatorMode.LENIENT_ORDER);
    }
     
    @Test
    public void testFindTSIGKeys() {
        TSIGKey tsigKey1 = new TSIGKey();
        tsigKey1.setAlgorithm(TSIGKeyAlgorithm.HMAC_MD5);
        tsigKey1.setId(1);
        tsigKey1.setName("tsigkeyname1");
        tsigKey1.setValue("tsigkeyvalue1");
        TSIGKey tsigKey2 = new TSIGKey();
        tsigKey2.setAlgorithm(TSIGKeyAlgorithm.HMAC_SHA256);
        tsigKey2.setId(2);
        tsigKey2.setName("tsigkeyname2");
        tsigKey2.setValue("tsigkeyvalue2");
        TSIGKey tsigKey3 = new TSIGKey();
        tsigKey3.setAlgorithm(TSIGKeyAlgorithm.HMAC_SHA512);
        tsigKey3.setId(3);
        tsigKey3.setName("tsigkeyname3");
        tsigKey3.setValue("tsigkeyvalue3");
        List<TSIGKey> expectedTSIGKeys = Arrays.asList(tsigKey1, tsigKey2, tsigKey3);
        assertReflectionEquals(expectedTSIGKeys, jdnsaasRepository.findTSIGKeys(), ReflectionComparatorMode.LENIENT_ORDER);
    }

    @Test
    public void testFindView() {
        TSIGKey tsigKey1 = new TSIGKey();
        tsigKey1.setAlgorithm(TSIGKeyAlgorithm.HMAC_MD5);
        tsigKey1.setId(1);
        tsigKey1.setName("tsigkeyname1");
        tsigKey1.setValue("tsigkeyvalue1");
        Resolver resolver1 = new Resolver();
        resolver1.setAddress("10.0.0.1");
        resolver1.setId(1);
        resolver1.setLocalAddress("172.16.0.1");
        resolver1.setPort(53);
        Resolver resolver2 = new Resolver();
        resolver2.setAddress("10.0.0.2");
        resolver2.setId(2);
        resolver2.setPort(53);
        View expectedView = new View();
        expectedView.setId(1);
        expectedView.setName("view1");
        expectedView.setResolvers(Arrays.asList(resolver1, resolver2));
        assertReflectionEquals(expectedView, jdnsaasRepository.findView("view1"), ReflectionComparatorMode.LENIENT_ORDER);
        expectedView = new View();
        expectedView.setId(2);
        expectedView.setName("view2");
        expectedView.setResolvers(Arrays.asList(resolver1));
        assertReflectionEquals(expectedView, jdnsaasRepository.findView("view2"), ReflectionComparatorMode.LENIENT_ORDER);
    }

    @Test
    public void testFindViewNames() {
        List<String> expectedViewNames = new ArrayList<String>(Arrays.asList("view1", "view2"));
        assertReflectionEquals(expectedViewNames, jdnsaasRepository.findViewNames(), ReflectionComparatorMode.LENIENT_ORDER);
    }

    @Test
    public void testFindZone() {
        TSIGKey queryTSIGKey = new TSIGKey();
        queryTSIGKey.setAlgorithm(TSIGKeyAlgorithm.HMAC_MD5);
        queryTSIGKey.setId(1);
        queryTSIGKey.setName("tsigkeyname1");
        queryTSIGKey.setValue("tsigkeyvalue1");
        TSIGKey transferTSIGKey = new TSIGKey();
        transferTSIGKey.setAlgorithm(TSIGKeyAlgorithm.HMAC_SHA256);
        transferTSIGKey.setId(2);
        transferTSIGKey.setName("tsigkeyname2");
        transferTSIGKey.setValue("tsigkeyvalue2");
        TSIGKey updateTSIGKey = new TSIGKey();
        updateTSIGKey.setAlgorithm(TSIGKeyAlgorithm.HMAC_SHA512);
        updateTSIGKey.setId(3);
        updateTSIGKey.setName("tsigkeyname3");
        updateTSIGKey.setValue("tsigkeyvalue3");
        Resolver resolver1 = new Resolver();
        resolver1.setAddress("10.0.0.1");
        resolver1.setId(1);
        resolver1.setLocalAddress("172.16.0.1");
        resolver1.setPort(53);
        Resolver resolver2 = new Resolver();
        resolver2.setAddress("10.0.0.2");
        resolver2.setId(2);
        resolver2.setPort(53);
        View view = new View();
        view.setId(1);
        view.setName("view1");
        view.setResolvers(Arrays.asList(resolver1, resolver2));
        Zone expectedZone = new Zone();
        expectedZone.setId(1);
        expectedZone.setName("zone1");
        expectedZone.setQueryTSIGKey(queryTSIGKey);
        expectedZone.setTransferTSIGKey(transferTSIGKey);
        expectedZone.setUpdateTSIGKey(updateTSIGKey);
        expectedZone.setView(view);
        assertReflectionEquals(expectedZone, jdnsaasRepository.findZone("view1", "zone1"), ReflectionComparatorMode.LENIENT_ORDER);
        expectedZone = new Zone();
        expectedZone.setId(2);
        expectedZone.setName("zone2");
        expectedZone.setTransferTSIGKey(transferTSIGKey);
        expectedZone.setUpdateTSIGKey(updateTSIGKey);
        expectedZone.setView(view);
        assertReflectionEquals(expectedZone, jdnsaasRepository.findZone("view1", "zone2"), ReflectionComparatorMode.LENIENT_ORDER);
        view = new View();
        view.setId(2);
        view.setName("view2");
        view.setResolvers(Arrays.asList(resolver1));
        expectedZone = new Zone();
        expectedZone.setId(3);
        expectedZone.setName("zone2");
        expectedZone.setQueryTSIGKey(queryTSIGKey);
        expectedZone.setUpdateTSIGKey(updateTSIGKey);
        expectedZone.setView(view);
        assertReflectionEquals(expectedZone, jdnsaasRepository.findZone("view2", "zone2"), ReflectionComparatorMode.LENIENT_ORDER);
        expectedZone = new Zone();
        expectedZone.setId(4);
        expectedZone.setName("zone3");
        expectedZone.setQueryTSIGKey(queryTSIGKey);
        expectedZone.setTransferTSIGKey(transferTSIGKey);
        expectedZone.setView(view);
        assertReflectionEquals(expectedZone, jdnsaasRepository.findZone("view2", "zone3"), ReflectionComparatorMode.LENIENT_ORDER);
    }

    @Test
    public void testFindZoneNames() {
        List<String> expectedZoneNames = new ArrayList<String>(Arrays.asList("zone1", "zone2"));
        assertReflectionEquals(expectedZoneNames, jdnsaasRepository.findZoneNames("view1"), ReflectionComparatorMode.LENIENT_ORDER);
        expectedZoneNames = new ArrayList<String>(Arrays.asList("zone2", "zone3"));
        assertReflectionEquals(expectedZoneNames, jdnsaasRepository.findZoneNames("view2"), ReflectionComparatorMode.LENIENT_ORDER);
    }

    @Test
    public void testFindZones() {
        TSIGKey queryTSIGKey = new TSIGKey();
        queryTSIGKey.setAlgorithm(TSIGKeyAlgorithm.HMAC_MD5);
        queryTSIGKey.setId(1);
        queryTSIGKey.setName("tsigkeyname1");
        queryTSIGKey.setValue("tsigkeyvalue1");
        TSIGKey transferTSIGKey = new TSIGKey();
        transferTSIGKey.setAlgorithm(TSIGKeyAlgorithm.HMAC_SHA256);
        transferTSIGKey.setId(2);
        transferTSIGKey.setName("tsigkeyname2");
        transferTSIGKey.setValue("tsigkeyvalue2");
        TSIGKey updateTSIGKey = new TSIGKey();
        updateTSIGKey.setAlgorithm(TSIGKeyAlgorithm.HMAC_SHA512);
        updateTSIGKey.setId(3);
        updateTSIGKey.setName("tsigkeyname3");
        updateTSIGKey.setValue("tsigkeyvalue3");
        Resolver resolver1 = new Resolver();
        resolver1.setAddress("10.0.0.1");
        resolver1.setId(1);
        resolver1.setLocalAddress("172.16.0.1");
        resolver1.setPort(53);
        Resolver resolver2 = new Resolver();
        resolver2.setAddress("10.0.0.2");
        resolver2.setId(2);
        resolver2.setPort(53);
        View view = new View();
        view.setId(1);
        view.setName("view1");
        view.setResolvers(Arrays.asList(resolver1, resolver2));
        Zone zone1 = new Zone();
        zone1.setId(1);
        zone1.setName("zone1");
        zone1.setQueryTSIGKey(queryTSIGKey);
        zone1.setTransferTSIGKey(transferTSIGKey);
        zone1.setUpdateTSIGKey(updateTSIGKey);
        zone1.setView(view);
        Zone zone2 = new Zone();
        zone2.setId(2);
        zone2.setName("zone2");
        zone2.setTransferTSIGKey(transferTSIGKey);
        zone2.setUpdateTSIGKey(updateTSIGKey);
        zone2.setView(view);
        view = new View();
        view.setId(2);
        view.setName("view2");
        view.setResolvers(Arrays.asList(resolver1));
        Zone zone3 = new Zone();
        zone3.setId(3);
        zone3.setName("zone2");
        zone3.setQueryTSIGKey(queryTSIGKey);
        zone3.setUpdateTSIGKey(updateTSIGKey);
        zone3.setView(view);
        Zone zone4 = new Zone();
        zone4.setId(4);
        zone4.setName("zone3");
        zone4.setQueryTSIGKey(queryTSIGKey);
        zone4.setTransferTSIGKey(transferTSIGKey);
        zone4.setView(view);
        List<Zone> expectedZones = Arrays.asList(zone1, zone2, zone3, zone4);
        assertReflectionEquals(expectedZones, jdnsaasRepository.findZones(), ReflectionComparatorMode.LENIENT_ORDER);
    }

    private IDatabaseConnection getDatabaseConnection() throws Exception {
        return new DatabaseConnection(((JDNSaaSRepositoryImpl)jdnsaasRepository).getConnection());
    }
    
    private static IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(Thread.currentThread().getContextClassLoader().getResourceAsStream(TEST_FILE));
    }
}
