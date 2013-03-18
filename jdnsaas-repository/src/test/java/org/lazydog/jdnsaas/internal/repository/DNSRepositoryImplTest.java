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
    public void testFindDnsServer() throws Exception {
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
        assertReflectionEquals(expectedDnsServer, this.dnsRepository.findDnsServer("dns1.testdomain"), ReflectionComparatorMode.LENIENT_ORDER);
        expectedDnsServer = new DNSServerEntity();
        expectedDnsServer.setId(new Integer(2));
        expectedDnsServer.setName("dns2.testdomain");
        expectedDnsServer.setPort(new Integer(53));
        expectedDnsServer.setTransactionSignature(transactionSignatureEntity);
        assertReflectionEquals(expectedDnsServer, this.dnsRepository.findDnsServer("dns2.testdomain"), ReflectionComparatorMode.LENIENT_ORDER);
    }
     
    @Test
    public void testFindZone() throws Exception {
        TransactionSignatureAlgorithmEntity transactionSignatureAlgorithm = new TransactionSignatureAlgorithmEntity();
        transactionSignatureAlgorithm.setId(new Integer(1));
        transactionSignatureAlgorithm.setName("testalgorithm");
        TransactionSignatureEntity transactionSignatureEntity = new TransactionSignatureEntity();
        transactionSignatureEntity.setAlgorithm(transactionSignatureAlgorithm);
        transactionSignatureEntity.setId(new Integer(1));
        transactionSignatureEntity.setName("testname");
        transactionSignatureEntity.setSecret("testsecret");
        DNSServerEntity dnsServerEntity = new DNSServerEntity();
        dnsServerEntity.setId(new Integer(1));
        dnsServerEntity.setName("dns1.testdomain");
        dnsServerEntity.setPort(new Integer(53));
        dnsServerEntity.setTransactionSignature(transactionSignatureEntity);
        ZoneEntity expectedZone = new ZoneEntity();
        expectedZone.setDnsServerEntity(dnsServerEntity);
        expectedZone.setId(new Integer(1));
        expectedZone.setName("testzone1");
        assertReflectionEquals(expectedZone, this.dnsRepository.findZone("dns1.testdomain", "testzone1"), ReflectionComparatorMode.LENIENT_ORDER);
        expectedZone = new ZoneEntity();
        expectedZone.setDnsServerEntity(dnsServerEntity);
        expectedZone.setId(new Integer(2));
        expectedZone.setName("testzone2");
        assertReflectionEquals(expectedZone, this.dnsRepository.findZone("dns1.testdomain", "testzone2"), ReflectionComparatorMode.LENIENT_ORDER);
        dnsServerEntity = new DNSServerEntity();
        dnsServerEntity.setId(new Integer(2));
        dnsServerEntity.setName("dns2.testdomain");
        dnsServerEntity.setPort(new Integer(53));
        dnsServerEntity.setTransactionSignature(transactionSignatureEntity);
        expectedZone = new ZoneEntity();
        expectedZone.setDnsServerEntity(dnsServerEntity);
        expectedZone.setId(new Integer(3));
        expectedZone.setName("testzone2");
        assertReflectionEquals(expectedZone, this.dnsRepository.findZone("dns2.testdomain", "testzone2"), ReflectionComparatorMode.LENIENT_ORDER);
        expectedZone = new ZoneEntity();
        expectedZone.setDnsServerEntity(dnsServerEntity);
        expectedZone.setId(new Integer(4));
        expectedZone.setName("testzone3");
        assertReflectionEquals(expectedZone, this.dnsRepository.findZone("dns2.testdomain", "testzone3"), ReflectionComparatorMode.LENIENT_ORDER);
    }
    
    @Test
    public void testFindDnsServerNames() throws Exception {
        List<String> expectedDnsServerNames = new ArrayList<String>(Arrays.asList("dns1.testdomain", "dns2.testdomain"));
        assertReflectionEquals(expectedDnsServerNames, this.dnsRepository.findDnsServerNames(), ReflectionComparatorMode.LENIENT_ORDER);
    }

    @Test
    public void testFindZoneNames() throws Exception {
        List<String> expectedZoneNames = new ArrayList<String>(Arrays.asList("testzone1", "testzone2"));
        assertReflectionEquals(expectedZoneNames, this.dnsRepository.findZoneNames("dns1.testdomain"), ReflectionComparatorMode.LENIENT_ORDER);
        expectedZoneNames = new ArrayList<String>(Arrays.asList("testzone2", "testzone3"));
        assertReflectionEquals(expectedZoneNames, this.dnsRepository.findZoneNames("dns2.testdomain"), ReflectionComparatorMode.LENIENT_ORDER);
    }
    
    private IDatabaseConnection getDatabaseConnection() throws Exception {
        return new DatabaseConnection(((DNSRepositoryImpl)this.dnsRepository).getConnection());
    }
    
    private static IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(Thread.currentThread().getContextClassLoader().getResourceAsStream(TEST_FILE));
    }
}
