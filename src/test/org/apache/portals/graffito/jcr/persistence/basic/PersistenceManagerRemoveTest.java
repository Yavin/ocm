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
package org.apache.portals.graffito.jcr.persistence.basic;

import java.io.ByteArrayInputStream;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.portals.graffito.jcr.RepositoryLifecycleTestSetup;
import org.apache.portals.graffito.jcr.TestBase;
import org.apache.portals.graffito.jcr.persistence.PersistenceManager;
import org.apache.portals.graffito.jcr.query.Filter;
import org.apache.portals.graffito.jcr.query.Query;
import org.apache.portals.graffito.jcr.query.QueryManager;
import org.apache.portals.graffito.jcr.testmodel.Atomic;
import org.apache.portals.graffito.jcr.testmodel.MultiValue;

/**
 * Test Query on atomic fields
 *
 * @author <a href="mailto:christophe.lombart@sword-technologies.com">Christophe Lombart</a>
 */
public class PersistenceManagerRemoveTest extends TestBase
{
	private final static Log log = LogFactory.getLog(PersistenceManagerRemoveTest.class);
	private Date date = new Date();
	/**
	 * <p>Defines the test case name for junit.</p>
	 * @param testName The test case name.
	 */
	public PersistenceManagerRemoveTest(String testName) throws Exception
	{
		super(testName);

	}

	public static Test suite()
	{
		// All methods starting with "test" will be executed in the test suite.
		return new RepositoryLifecycleTestSetup(
                new TestSuite(PersistenceManagerRemoveTest.class));
	}

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
		this.importData(date);
        
    }
	
	public void tearDown() throws Exception
	{

		for (int i = 1; i <= 10; i++)
		{
			if (getPersistenceManager().objectExists("/test" + i))
			{
				getPersistenceManager().remove("/test" + i);
			}

		}
		getPersistenceManager().save();

		super.tearDown();
	}

	public void testRemove()
	{

		try
		{
			
			PersistenceManager persistenceManager = this.getPersistenceManager();
			persistenceManager.remove("/test5");
			persistenceManager.save();

			assertFalse("Test5 has not been removed", persistenceManager.objectExists("/test5"));

			QueryManager queryManager = this.getQueryManager();
			Filter filter = queryManager.createFilter(Atomic.class);
			filter.addEqualTo("booleanObject" , new Boolean(false));
			Query query = queryManager.createQuery(filter);
			persistenceManager.remove(query);
			persistenceManager.save();

			filter = queryManager.createFilter(Atomic.class);
			filter.setScope("//");
			query = queryManager.createQuery(filter);			
			Collection result = persistenceManager.getObjects(query);
			assertEquals("Invalid number of objects", 5, result.size());

		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}

	}

	private void importData(Date date)
	{
		try
		{

			PersistenceManager persistenceManager = getPersistenceManager();

			for (int i = 1; i <= 10; i++)
			{
				Atomic a = new Atomic();
				a.setPath("/test" + i);
				a.setBooleanObject(new Boolean(i % 2 == 0));
				a.setBooleanPrimitive(true);
				a.setIntegerObject(new Integer(100 * i));
				a.setIntPrimitive(200 + i);
				a.setString("Test String " + i);
				a.setDate(date);
				Calendar calendar = Calendar.getInstance();
				calendar.set(1976, 4, 20, 15, 40);
				a.setCalendar(calendar);
				a.setDoubleObject(new Double(2.12 + i));
				a.setDoublePrimitive(1.23 + i);
				long now = System.currentTimeMillis();
				a.setTimestamp(new Timestamp(now));
				if ((i % 2) == 0)
				{
					a.setByteArray("This is small object stored in a JCR repository".getBytes());
					a.setInputStream(new ByteArrayInputStream("Test inputstream".getBytes()));
				}
				else
				{
					a.setByteArray("This is small object stored in a Graffito repository".getBytes());
					a.setInputStream(new ByteArrayInputStream("Another Stream".getBytes()));
				}
				persistenceManager.insert(a);

			}
			persistenceManager.save();

		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("Exception occurs during the unit test : " + e);
		}

	}

}