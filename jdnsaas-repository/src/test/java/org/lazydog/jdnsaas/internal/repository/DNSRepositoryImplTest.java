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
import org.lazydog.jdnsaas.spi.repository.DNSRepository;
import org.lazydog.jdnsaas.spi.repository.model.DNSServerEntity;
import org.lazydog.jdnsaas.spi.repository.model.TransactionSignatureAlgorithmEntity;
import org.lazydog.jdnsaas.spi.repository.model.TransactionSignatureEntity;
import org.lazydog.jdnsaas.spi.repository.model.ZoneEntity;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import org.unitils.reflectionassert.ReflectionComparatorMode;

/**
 * DNS repository implementation test.
 * 
 * @author  Ron Rickard
 */
public class DNSRepositoryImplTest {
    
    private static final String TEST_FILE = "dataset.xml";
    private DNSRepository dnsRepository;

    public DNSRepositoryImplTest() throws Exception {
        
        // Ensure the derby.log file is in the target directory.
        System.setProperty("derby.system.home", "./target");
        
        // Create the jdnsaas database.
        DriverManager.getConnection("jdbc:derby:memory:./target/jdnsaas;create=true");
        
        // Get the DNS repository.
        dnsRepository = DNSRepositoryImpl.newInstance("jdnsaasTest");
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
        ((DNSRepositoryImpl)this.dnsRepository).getEntityManager().clear();
        ((DNSRepositoryImpl)this.dnsRepository).getEntityManager().getEntityManagerFactory().getCache().evictAll();
    }
 
    @Test
    public void testFindDnsServer() {
        ZoneEntity zone1 = new ZoneEntity();
        zone1.setId(new Integer(1));
        zone1.setName("testzone1");
        ZoneEntity zone2 = new ZoneEntity();
        zone2.setId(new Integer(2));
        zone2.setName("testzone2");
        List<ZoneEntity> zones = new ArrayList<ZoneEntity>(Arrays.asList(zone1, zone2));
        TransactionSignatureAlgorithmEntity transactionSignatureAlgorithm = new TransactionSignatureAlgorithmEntity();
        transactionSignatureAlgorithm.setId(new Integer(1));
        transactionSignatureAlgorithm.setName("testalgorithm");
        TransactionSignatureEntity transactionSignatureEntity = new TransactionSignatureEntity();
        transactionSignatureEntity.setAlgorithm(transactionSignatureAlgorithm);
        transactionSignatureEntity.setId(new Integer(1));
        transactionSignatureEntity.setName("testname");
        transactionSignatureEntity.setSecret("testsecret");
        DNSServerEntity expectedDnsServer = new DNSServerEntity();
        expectedDnsServer.setId(new Integer(1));
        expectedDnsServer.setName("dns1.testdomain");
        expectedDnsServer.setPort(new Integer(53));
        expectedDnsServer.setTransactionSignature(transactionSignatureEntity);
        expectedDnsServer.setZones(zones);
        assertReflectionEquals(expectedDnsServer, this.dnsRepository.findDnsServer("dns1.testdomain"), ReflectionComparatorMode.LENIENT_ORDER);
        ZoneEntity zone3 = new ZoneEntity();
        zone3.setId(new Integer(3));
        zone3.setName("testzone2");
        ZoneEntity zone4 = new ZoneEntity();
        zone4.setId(new Integer(4));
        zone4.setName("testzone3");
        zones = new ArrayList<ZoneEntity>(Arrays.asList(zone3, zone4));
        expectedDnsServer = new DNSServerEntity();
        expectedDnsServer.setId(new Integer(2));
        expectedDnsServer.setName("dns2.testdomain");
        expectedDnsServer.setPort(new Integer(53));
        expectedDnsServer.setTransactionSignature(transactionSignatureEntity);
        expectedDnsServer.setZones(zones);
        assertReflectionEquals(expectedDnsServer, this.dnsRepository.findDnsServer("dns2.testdomain"), ReflectionComparatorMode.LENIENT_ORDER);
    }
    
    @Test
    public void testFindDnsServers() {
        ZoneEntity zone1 = new ZoneEntity();
        zone1.setId(new Integer(1));
        zone1.setName("testzone1");
        ZoneEntity zone2 = new ZoneEntity();
        zone2.setId(new Integer(2));
        zone2.setName("testzone2");
        List<ZoneEntity> zones1 = new ArrayList<ZoneEntity>(Arrays.asList(zone1, zone2));
        ZoneEntity zone3 = new ZoneEntity();
        zone3.setId(new Integer(3));
        zone3.setName("testzone2");
        ZoneEntity zone4 = new ZoneEntity();
        zone4.setId(new Integer(4));
        zone4.setName("testzone3");
        List<ZoneEntity> zones2 = new ArrayList<ZoneEntity>(Arrays.asList(zone3, zone4));
        TransactionSignatureAlgorithmEntity transactionSignatureAlgorithm = new TransactionSignatureAlgorithmEntity();
        transactionSignatureAlgorithm.setId(new Integer(1));
        transactionSignatureAlgorithm.setName("testalgorithm");
        TransactionSignatureEntity transactionSignatureEntity = new TransactionSignatureEntity();
        transactionSignatureEntity.setAlgorithm(transactionSignatureAlgorithm);
        transactionSignatureEntity.setId(new Integer(1));
        transactionSignatureEntity.setName("testname");
        transactionSignatureEntity.setSecret("testsecret");
        DNSServerEntity dnsServer1 = new DNSServerEntity();
        dnsServer1.setId(new Integer(1));
        dnsServer1.setName("dns1.testdomain");
        dnsServer1.setPort(new Integer(53));
        dnsServer1.setTransactionSignature(transactionSignatureEntity);
        dnsServer1.setZones(zones1);
        DNSServerEntity dnsServer2 = new DNSServerEntity();
        dnsServer2.setId(new Integer(2));
        dnsServer2.setName("dns2.testdomain");
        dnsServer2.setPort(new Integer(53));
        dnsServer2.setTransactionSignature(transactionSignatureEntity);
        dnsServer2.setZones(zones2);
        List<DNSServerEntity> expectedDnsServers = new ArrayList<DNSServerEntity>(Arrays.asList(dnsServer1, dnsServer2));
        assertReflectionEquals(expectedDnsServers, this.dnsRepository.findDnsServers(), ReflectionComparatorMode.LENIENT_ORDER);
    }

    private IDatabaseConnection getDatabaseConnection() throws Exception {
        return new DatabaseConnection(((DNSRepositoryImpl)this.dnsRepository).getConnection());
    }
    
    private static IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(Thread.currentThread().getContextClassLoader().getResourceAsStream(TEST_FILE));
    }
}
