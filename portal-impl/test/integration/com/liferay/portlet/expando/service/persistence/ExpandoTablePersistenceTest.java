/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portlet.expando.service.persistence;

import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.template.TemplateException;
import com.liferay.portal.kernel.template.TemplateManagerUtil;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.util.IntegerWrapper;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.test.TransactionalTestRule;
import com.liferay.portal.test.runners.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.tools.DBUpgrader;
import com.liferay.portal.util.PropsValues;
import com.liferay.portal.util.test.RandomTestUtil;

import com.liferay.portlet.expando.NoSuchTableException;
import com.liferay.portlet.expando.model.ExpandoTable;
import com.liferay.portlet.expando.model.impl.ExpandoTableModelImpl;
import com.liferay.portlet.expando.service.ExpandoTableLocalServiceUtil;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import org.junit.runner.RunWith;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @generated
 */
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class ExpandoTablePersistenceTest {
	@ClassRule
	public static TransactionalTestRule transactionalTestRule = new TransactionalTestRule(Propagation.REQUIRED);

	@BeforeClass
	public static void setupClass() throws TemplateException {
		try {
			DBUpgrader.upgrade();
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		TemplateManagerUtil.init();
	}

	@Before
	public void setUp() {
		_modelListeners = _persistence.getListeners();

		for (ModelListener<ExpandoTable> modelListener : _modelListeners) {
			_persistence.unregisterListener(modelListener);
		}
	}

	@After
	public void tearDown() throws Exception {
		Iterator<ExpandoTable> iterator = _expandoTables.iterator();

		while (iterator.hasNext()) {
			_persistence.remove(iterator.next());

			iterator.remove();
		}

		for (ModelListener<ExpandoTable> modelListener : _modelListeners) {
			_persistence.registerListener(modelListener);
		}
	}

	@Test
	public void testCreate() throws Exception {
		long pk = RandomTestUtil.nextLong();

		ExpandoTable expandoTable = _persistence.create(pk);

		Assert.assertNotNull(expandoTable);

		Assert.assertEquals(expandoTable.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		ExpandoTable newExpandoTable = addExpandoTable();

		_persistence.remove(newExpandoTable);

		ExpandoTable existingExpandoTable = _persistence.fetchByPrimaryKey(newExpandoTable.getPrimaryKey());

		Assert.assertNull(existingExpandoTable);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addExpandoTable();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = RandomTestUtil.nextLong();

		ExpandoTable newExpandoTable = _persistence.create(pk);

		newExpandoTable.setCompanyId(RandomTestUtil.nextLong());

		newExpandoTable.setClassNameId(RandomTestUtil.nextLong());

		newExpandoTable.setName(RandomTestUtil.randomString());

		_expandoTables.add(_persistence.update(newExpandoTable));

		ExpandoTable existingExpandoTable = _persistence.findByPrimaryKey(newExpandoTable.getPrimaryKey());

		Assert.assertEquals(existingExpandoTable.getTableId(),
			newExpandoTable.getTableId());
		Assert.assertEquals(existingExpandoTable.getCompanyId(),
			newExpandoTable.getCompanyId());
		Assert.assertEquals(existingExpandoTable.getClassNameId(),
			newExpandoTable.getClassNameId());
		Assert.assertEquals(existingExpandoTable.getName(),
			newExpandoTable.getName());
	}

	@Test
	public void testCountByC_C() {
		try {
			_persistence.countByC_C(RandomTestUtil.nextLong(),
				RandomTestUtil.nextLong());

			_persistence.countByC_C(0L, 0L);
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testCountByC_C_N() {
		try {
			_persistence.countByC_C_N(RandomTestUtil.nextLong(),
				RandomTestUtil.nextLong(), StringPool.BLANK);

			_persistence.countByC_C_N(0L, 0L, StringPool.NULL);

			_persistence.countByC_C_N(0L, 0L, (String)null);
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		ExpandoTable newExpandoTable = addExpandoTable();

		ExpandoTable existingExpandoTable = _persistence.findByPrimaryKey(newExpandoTable.getPrimaryKey());

		Assert.assertEquals(existingExpandoTable, newExpandoTable);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail("Missing entity did not throw NoSuchTableException");
		}
		catch (NoSuchTableException nsee) {
		}
	}

	@Test
	public void testFindAll() throws Exception {
		try {
			_persistence.findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS,
				getOrderByComparator());
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	protected OrderByComparator<ExpandoTable> getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("ExpandoTable", "tableId",
			true, "companyId", true, "classNameId", true, "name", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		ExpandoTable newExpandoTable = addExpandoTable();

		ExpandoTable existingExpandoTable = _persistence.fetchByPrimaryKey(newExpandoTable.getPrimaryKey());

		Assert.assertEquals(existingExpandoTable, newExpandoTable);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		ExpandoTable missingExpandoTable = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingExpandoTable);
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereAllPrimaryKeysExist()
		throws Exception {
		ExpandoTable newExpandoTable1 = addExpandoTable();
		ExpandoTable newExpandoTable2 = addExpandoTable();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newExpandoTable1.getPrimaryKey());
		primaryKeys.add(newExpandoTable2.getPrimaryKey());

		Map<Serializable, ExpandoTable> expandoTables = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(2, expandoTables.size());
		Assert.assertEquals(newExpandoTable1,
			expandoTables.get(newExpandoTable1.getPrimaryKey()));
		Assert.assertEquals(newExpandoTable2,
			expandoTables.get(newExpandoTable2.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereNoPrimaryKeysExist()
		throws Exception {
		long pk1 = RandomTestUtil.nextLong();

		long pk2 = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(pk1);
		primaryKeys.add(pk2);

		Map<Serializable, ExpandoTable> expandoTables = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(expandoTables.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereSomePrimaryKeysExist()
		throws Exception {
		ExpandoTable newExpandoTable = addExpandoTable();

		long pk = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newExpandoTable.getPrimaryKey());
		primaryKeys.add(pk);

		Map<Serializable, ExpandoTable> expandoTables = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, expandoTables.size());
		Assert.assertEquals(newExpandoTable,
			expandoTables.get(newExpandoTable.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithNoPrimaryKeys()
		throws Exception {
		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		Map<Serializable, ExpandoTable> expandoTables = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(expandoTables.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithOnePrimaryKey()
		throws Exception {
		ExpandoTable newExpandoTable = addExpandoTable();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newExpandoTable.getPrimaryKey());

		Map<Serializable, ExpandoTable> expandoTables = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, expandoTables.size());
		Assert.assertEquals(newExpandoTable,
			expandoTables.get(newExpandoTable.getPrimaryKey()));
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = ExpandoTableLocalServiceUtil.getActionableDynamicQuery();

		actionableDynamicQuery.setPerformActionMethod(new ActionableDynamicQuery.PerformActionMethod() {
				@Override
				public void performAction(Object object) {
					ExpandoTable expandoTable = (ExpandoTable)object;

					Assert.assertNotNull(expandoTable);

					count.increment();
				}
			});

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		ExpandoTable newExpandoTable = addExpandoTable();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(ExpandoTable.class,
				ExpandoTable.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("tableId",
				newExpandoTable.getTableId()));

		List<ExpandoTable> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		ExpandoTable existingExpandoTable = result.get(0);

		Assert.assertEquals(existingExpandoTable, newExpandoTable);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(ExpandoTable.class,
				ExpandoTable.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("tableId",
				RandomTestUtil.nextLong()));

		List<ExpandoTable> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		ExpandoTable newExpandoTable = addExpandoTable();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(ExpandoTable.class,
				ExpandoTable.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("tableId"));

		Object newTableId = newExpandoTable.getTableId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("tableId",
				new Object[] { newTableId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingTableId = result.get(0);

		Assert.assertEquals(existingTableId, newTableId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(ExpandoTable.class,
				ExpandoTable.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("tableId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("tableId",
				new Object[] { RandomTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		if (!PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			return;
		}

		ExpandoTable newExpandoTable = addExpandoTable();

		_persistence.clearCache();

		ExpandoTableModelImpl existingExpandoTableModelImpl = (ExpandoTableModelImpl)_persistence.findByPrimaryKey(newExpandoTable.getPrimaryKey());

		Assert.assertEquals(existingExpandoTableModelImpl.getCompanyId(),
			existingExpandoTableModelImpl.getOriginalCompanyId());
		Assert.assertEquals(existingExpandoTableModelImpl.getClassNameId(),
			existingExpandoTableModelImpl.getOriginalClassNameId());
		Assert.assertTrue(Validator.equals(
				existingExpandoTableModelImpl.getName(),
				existingExpandoTableModelImpl.getOriginalName()));
	}

	protected ExpandoTable addExpandoTable() throws Exception {
		long pk = RandomTestUtil.nextLong();

		ExpandoTable expandoTable = _persistence.create(pk);

		expandoTable.setCompanyId(RandomTestUtil.nextLong());

		expandoTable.setClassNameId(RandomTestUtil.nextLong());

		expandoTable.setName(RandomTestUtil.randomString());

		_expandoTables.add(_persistence.update(expandoTable));

		return expandoTable;
	}

	private static Log _log = LogFactoryUtil.getLog(ExpandoTablePersistenceTest.class);
	private List<ExpandoTable> _expandoTables = new ArrayList<ExpandoTable>();
	private ModelListener<ExpandoTable>[] _modelListeners;
	private ExpandoTablePersistence _persistence = ExpandoTableUtil.getPersistence();
}