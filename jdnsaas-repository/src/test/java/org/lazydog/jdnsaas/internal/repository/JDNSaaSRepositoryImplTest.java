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
import org.lazydog.jdnsaas.model.DNSServer;
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
    public void testFindDNSServers() throws Exception {
        DNSServer dnsServer1 = new DNSServer();
        dnsServer1.setId(new Integer(1));
        dnsServer1.setLocalAddress("testlocaladdress1");
        dnsServer1.setName("testdnsserver1");
        dnsServer1.setPort(new Integer(53));
        DNSServer dnsServer2 = new DNSServer();
        dnsServer2.setId(new Integer(2));
        dnsServer2.setName("testdnsserver2");
        dnsServer2.setPort(new Integer(53));
        List<DNSServer> expectedDNSServers = Arrays.asList(dnsServer1, dnsServer2);
        assertReflectionEquals(expectedDNSServers, this.jdnsaasRepository.findDNSServers(), ReflectionComparatorMode.LENIENT_ORDER);
    }
     
    @Test
    public void testFindTSIGKeys() throws Exception {
        TSIGKey tsigKey1 = new TSIGKey();
        tsigKey1.setAlgorithm(TSIGKeyAlgorithm.HMAC_MD5);
        tsigKey1.setId(new Integer(1));
        tsigKey1.setName("testtsigkeyname1");
        tsigKey1.setValue("testtsigkeyvalue1");
        TSIGKey tsigKey2 = new TSIGKey();
        tsigKey2.setAlgorithm(TSIGKeyAlgorithm.HMAC_SHA256);
        tsigKey2.setId(new Integer(2));
        tsigKey2.setName("testtsigkeyname2");
        tsigKey2.setValue("testtsigkeyvalue2");
        TSIGKey tsigKey3 = new TSIGKey();
        tsigKey3.setAlgorithm(TSIGKeyAlgorithm.HMAC_SHA512);
        tsigKey3.setId(new Integer(3));
        tsigKey3.setName("testtsigkeyname3");
        tsigKey3.setValue("testtsigkeyvalue3");
        List<TSIGKey> expectedTSIGKeys = Arrays.asList(tsigKey1, tsigKey2, tsigKey3);
        assertReflectionEquals(expectedTSIGKeys, this.jdnsaasRepository.findTSIGKeys(), ReflectionComparatorMode.LENIENT_ORDER);
    }
    
    @Test
    public void testFindView() throws Exception {
        DNSServer dnsServer = new DNSServer();
        dnsServer.setId(new Integer(1));
        dnsServer.setLocalAddress("testlocaladdress1");
        dnsServer.setName("testdnsserver1");
        dnsServer.setPort(new Integer(53));
        View expectedView = new View();
        expectedView.setDnsServer(dnsServer);
        expectedView.setId(1);
        expectedView.setName("testview1");
        assertReflectionEquals(expectedView, this.jdnsaasRepository.findView("testview1"));
        dnsServer = new DNSServer();
        dnsServer.setId(new Integer(2));
        dnsServer.setName("testdnsserver2");
        dnsServer.setPort(new Integer(53));
        expectedView = new View();
        expectedView.setDnsServer(dnsServer);
        expectedView.setId(2);
        expectedView.setName("testview2");
        assertReflectionEquals(expectedView, this.jdnsaasRepository.findView("testview2"));
    }

    @Test
    public void testFindViewNames() throws Exception {
        List<String> expectedViewNames = new ArrayList<String>(Arrays.asList("testview1", "testview2"));
        assertReflectionEquals(expectedViewNames, this.jdnsaasRepository.findViewNames(), ReflectionComparatorMode.LENIENT_ORDER);
    }

    @Test
    public void testFindZone() throws Exception {
        TSIGKey queryTSIGKey = new TSIGKey();
        queryTSIGKey.setAlgorithm(TSIGKeyAlgorithm.HMAC_MD5);
        queryTSIGKey.setId(new Integer(1));
        queryTSIGKey.setName("testtsigkeyname1");
        queryTSIGKey.setValue("testtsigkeyvalue1");
        TSIGKey transferTSIGKey = new TSIGKey();
        transferTSIGKey.setAlgorithm(TSIGKeyAlgorithm.HMAC_SHA256);
        transferTSIGKey.setId(new Integer(2));
        transferTSIGKey.setName("testtsigkeyname2");
        transferTSIGKey.setValue("testtsigkeyvalue2");
        TSIGKey updateTSIGKey = new TSIGKey();
        updateTSIGKey.setAlgorithm(TSIGKeyAlgorithm.HMAC_SHA512);
        updateTSIGKey.setId(new Integer(3));
        updateTSIGKey.setName("testtsigkeyname3");
        updateTSIGKey.setValue("testtsigkeyvalue3");
        DNSServer dnsServer = new DNSServer();
        dnsServer.setId(new Integer(1));
        dnsServer.setLocalAddress("testlocaladdress1");
        dnsServer.setName("testdnsserver1");
        dnsServer.setPort(new Integer(53));
        View view = new View();
        view.setDnsServer(dnsServer);
        view.setId(1);
        view.setName("testview1");
        Zone expectedZone = new Zone();
        expectedZone.setId(new Integer(1));
        expectedZone.setName("testzone1");
        expectedZone.setQueryTSIGKey(queryTSIGKey);
        expectedZone.setTransferTSIGKey(transferTSIGKey);
        expectedZone.setUpdateTSIGKey(updateTSIGKey);
        expectedZone.setView(view);
        assertReflectionEquals(expectedZone, this.jdnsaasRepository.findZone("testview1", "testzone1"), ReflectionComparatorMode.LENIENT_ORDER);
        expectedZone = new Zone();
        expectedZone.setId(new Integer(2));
        expectedZone.setName("testzone2");
        expectedZone.setTransferTSIGKey(transferTSIGKey);
        expectedZone.setUpdateTSIGKey(updateTSIGKey);
        expectedZone.setView(view);
        assertReflectionEquals(expectedZone, this.jdnsaasRepository.findZone("testview1", "testzone2"), ReflectionComparatorMode.LENIENT_ORDER);
        dnsServer = new DNSServer();
        dnsServer.setId(new Integer(2));
        dnsServer.setName("testdnsserver2");
        dnsServer.setPort(new Integer(53));
        view = new View();
        view.setDnsServer(dnsServer);
        view.setId(2);
        view.setName("testview2");
        expectedZone = new Zone();
        expectedZone.setId(new Integer(3));
        expectedZone.setName("testzone2");
        expectedZone.setQueryTSIGKey(queryTSIGKey);
        expectedZone.setUpdateTSIGKey(updateTSIGKey);
        expectedZone.setView(view);
        assertReflectionEquals(expectedZone, this.jdnsaasRepository.findZone("testview2", "testzone2"), ReflectionComparatorMode.LENIENT_ORDER);
        expectedZone = new Zone();
        expectedZone.setId(new Integer(4));
        expectedZone.setName("testzone3");
        expectedZone.setQueryTSIGKey(queryTSIGKey);
        expectedZone.setTransferTSIGKey(transferTSIGKey);
        expectedZone.setView(view);
        assertReflectionEquals(expectedZone, this.jdnsaasRepository.findZone("testview2", "testzone3"), ReflectionComparatorMode.LENIENT_ORDER);
    }

    @Test
    public void testFindZoneNames() throws Exception {
        List<String> expectedZoneNames = new ArrayList<String>(Arrays.asList("testzone1", "testzone2"));
        assertReflectionEquals(expectedZoneNames, this.jdnsaasRepository.findZoneNames("testview1"), ReflectionComparatorMode.LENIENT_ORDER);
        expectedZoneNames = new ArrayList<String>(Arrays.asList("testzone2", "testzone3"));
        assertReflectionEquals(expectedZoneNames, this.jdnsaasRepository.findZoneNames("testview2"), ReflectionComparatorMode.LENIENT_ORDER);
    }

    private IDatabaseConnection getDatabaseConnection() throws Exception {
        return new DatabaseConnection(((JDNSaaSRepositoryImpl)this.jdnsaasRepository).getConnection());
    }
    
    private static IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(Thread.currentThread().getContextClassLoader().getResourceAsStream(TEST_FILE));
    }
}
