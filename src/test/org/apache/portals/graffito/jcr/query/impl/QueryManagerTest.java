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
package org.apache.portals.graffito.jcr.query.impl;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.portals.graffito.jcr.TestBase;
import org.apache.portals.graffito.jcr.query.Filter;
import org.apache.portals.graffito.jcr.query.Query;
import org.apache.portals.graffito.jcr.query.QueryManager;
import org.apache.portals.graffito.jcr.testmodel.C;


/**
 * Test QueryManagerImpl methods
 *
 * @author <a href="mailto:christophe.lombart@sword-technologies.com">Christophe Lombart</a>
 */
public class QueryManagerTest extends TestBase
{
    private final static Log log = LogFactory.getLog(QueryManagerTest.class);

    /**
     * <p>Defines the test case name for junit.</p>
     * @param testName The test case name.
     */
    public QueryManagerTest(String testName)  throws Exception
    {
        super(testName);
        initPersistenceManager();
    }

    public static Test suite()
    {
        // All methods starting with "test" will be executed in the test suite.
        return new TestSuite(QueryManagerTest.class);
    }

    public void testBuildExpression1()
    {

    	try
    	{
    	      QueryManager queryManager = this.getQueryManager();
    	      Filter filter = queryManager.createFilter(C.class);
    	      filter.addEqualTo("name", "a test value");
    	      filter.addEqualTo("id", new Integer(1));
    	      filter.setScope("/test//");
    	      
    	      Query query = queryManager.createQuery(filter);
    	      String jcrExpression = queryManager.buildJCRExpression(query);
    	      assertNotNull("jcrExpression is null", jcrExpression);
    	      assertTrue("Invalid JcrExpression", jcrExpression.equals("/jcr:root/test//element(*, graffito:C) [@graffito:name = 'a test value' and @graffito:id = 1]"));
    	      
    	      
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Exception occurs during the unit test : " + e);
        }
        
    }

    public void testBuildExpression2()
    {

    	try
    	{
    	      QueryManager queryManager = this.getQueryManager();
    	      Filter filter = queryManager.createFilter(C.class);
    	      filter.addEqualTo("name", "a test value");
    	      filter.addEqualTo("id", new Integer(1));    	      
    	      
    	      Query query = queryManager.createQuery(filter);
    	      String jcrExpression = queryManager.buildJCRExpression(query);
    	      assertNotNull("jcrExpression is null", jcrExpression);
    	      assertTrue("Invalid JcrExpression", jcrExpression.equals("//element(*, graffito:C) [@graffito:name = 'a test value' and @graffito:id = 1]"));
    	      
    	      
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Exception occurs during the unit test : " + e);
        }
        
    }
    

}