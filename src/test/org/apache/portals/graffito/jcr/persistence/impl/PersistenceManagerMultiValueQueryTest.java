/* ========================================================================
 * Copyright 2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ========================================================================
 */
package org.apache.portals.graffito.jcr.persistence.impl;

import java.util.ArrayList;
import java.util.Collection;

import javax.jcr.Node;
import javax.jcr.Session;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.portals.graffito.jcr.TestBase;
import org.apache.portals.graffito.jcr.persistence.PersistenceManager;
import org.apache.portals.graffito.jcr.query.Filter;
import org.apache.portals.graffito.jcr.query.Query;
import org.apache.portals.graffito.jcr.query.QueryManager;
import org.apache.portals.graffito.jcr.testmodel.MultiValue;

/**
 * Test NTCollectionConverterImpl
 *
 * @author <a href="mailto:christophe.lombart@sword-technologies.com">Christophe Lombart</a>
 */
public class PersistenceManagerMultiValueQueryTest extends TestBase
{
    private final static Log log = LogFactory.getLog(PersistenceManagerMultiValueQueryTest.class);

    /**
     * <p>Defines the test case name for junit.</p>
     * @param testName The test case name.
     */
    public PersistenceManagerMultiValueQueryTest(String testName)  throws Exception
    {
        super(testName);
    }

    public static Test suite()
    {
        // All methods starting with "test" will be executed in the test suite.
        return new TestSuite(PersistenceManagerMultiValueQueryTest.class);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
		this.importData();
        
    }
    
    public void tearDown() throws Exception
    {
        if (getPersistenceManager().objectExists("/test"))
        {
            getPersistenceManager().remove("/test");
            
        }    
        getPersistenceManager().save();
        super.tearDown();
    }	
    
    public void testMultiValueSearch()
    {
        try
        {
        	
  	      QueryManager queryManager = this.getQueryManager();
	      Filter filter = queryManager.createFilter(MultiValue.class);    
	      filter.addEqualTo("multiValues", "Value1");
	      Query query = queryManager.createQuery(filter);    	      
	      PersistenceManager persistenceManager = this.getPersistenceManager();
	      Collection result = persistenceManager.getObjects(query);
	      assertTrue("Invalid number of objects - should be = 3", result.size() == 3);            
          
  	      queryManager = this.getQueryManager();
	      filter = queryManager.createFilter(MultiValue.class);    
	      filter.addEqualTo("multiValues", "Value9");
	      query = queryManager.createQuery(filter);    	      
	      persistenceManager = this.getPersistenceManager();
	      result = persistenceManager.getObjects(query);
	      assertTrue("Invalid number of objects - should be = 1", result.size() == 1);
	      MultiValue multiValue = (MultiValue)result.iterator().next();
	      assertTrue("Incorrect MultiValue found ", multiValue.getName().equals("m3"));
	      
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Exception occurs during the unit test : " + e);
        }
        
    }


    public void importData()
    {
        try
        {
        	PersistenceManager persistenceManager = getPersistenceManager();

			PersistenceManagerImpl persistenceManagerImpl = (PersistenceManagerImpl) persistenceManager;
			
			Session session = persistenceManagerImpl.getSession();
			Node root = session.getRootNode();
			root.addNode("test");

            MultiValue multiValue = new MultiValue();
            multiValue.setPath("/test/m1");
            multiValue.setName("m1");
            ArrayList values = new ArrayList();
            values.add("Value1");
            values.add("Value2");
            values.add("Value3");
            values.add("Value4");
            multiValue.setMultiValues(values);
            persistenceManager.insert(multiValue);
            
            multiValue = new MultiValue();
            multiValue.setPath("/test/m2");
            multiValue.setName("m2");
            values = new ArrayList();
            values.add("Value1");
            values.add("Value5");
            values.add("Value6");
            values.add("Value7");            
            multiValue.setMultiValues(values);
            persistenceManager.insert(multiValue);
            
            multiValue = new MultiValue();
            multiValue.setPath("/test/m3");
            multiValue.setName("m3");
            values = new ArrayList();
            values.add("Value1");
            values.add("Value2");
            values.add("Value8");
            values.add("Value9");
            
            multiValue.setMultiValues(values);
            persistenceManager.insert(multiValue);
                                   
            persistenceManager.save();
            
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	fail("Impossible to insert objects");
        }
    	
    }
}