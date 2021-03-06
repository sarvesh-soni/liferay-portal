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

package com.liferay.portlet.social.service.persistence;

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
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.test.TransactionalTestRule;
import com.liferay.portal.test.runners.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.tools.DBUpgrader;
import com.liferay.portal.util.test.RandomTestUtil;

import com.liferay.portlet.social.NoSuchActivitySetException;
import com.liferay.portlet.social.model.SocialActivitySet;
import com.liferay.portlet.social.service.SocialActivitySetLocalServiceUtil;

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
public class SocialActivitySetPersistenceTest {
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

		for (ModelListener<SocialActivitySet> modelListener : _modelListeners) {
			_persistence.unregisterListener(modelListener);
		}
	}

	@After
	public void tearDown() throws Exception {
		Iterator<SocialActivitySet> iterator = _socialActivitySets.iterator();

		while (iterator.hasNext()) {
			_persistence.remove(iterator.next());

			iterator.remove();
		}

		for (ModelListener<SocialActivitySet> modelListener : _modelListeners) {
			_persistence.registerListener(modelListener);
		}
	}

	@Test
	public void testCreate() throws Exception {
		long pk = RandomTestUtil.nextLong();

		SocialActivitySet socialActivitySet = _persistence.create(pk);

		Assert.assertNotNull(socialActivitySet);

		Assert.assertEquals(socialActivitySet.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		SocialActivitySet newSocialActivitySet = addSocialActivitySet();

		_persistence.remove(newSocialActivitySet);

		SocialActivitySet existingSocialActivitySet = _persistence.fetchByPrimaryKey(newSocialActivitySet.getPrimaryKey());

		Assert.assertNull(existingSocialActivitySet);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addSocialActivitySet();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = RandomTestUtil.nextLong();

		SocialActivitySet newSocialActivitySet = _persistence.create(pk);

		newSocialActivitySet.setGroupId(RandomTestUtil.nextLong());

		newSocialActivitySet.setCompanyId(RandomTestUtil.nextLong());

		newSocialActivitySet.setUserId(RandomTestUtil.nextLong());

		newSocialActivitySet.setCreateDate(RandomTestUtil.nextLong());

		newSocialActivitySet.setModifiedDate(RandomTestUtil.nextLong());

		newSocialActivitySet.setClassNameId(RandomTestUtil.nextLong());

		newSocialActivitySet.setClassPK(RandomTestUtil.nextLong());

		newSocialActivitySet.setType(RandomTestUtil.nextInt());

		newSocialActivitySet.setExtraData(RandomTestUtil.randomString());

		newSocialActivitySet.setActivityCount(RandomTestUtil.nextInt());

		_socialActivitySets.add(_persistence.update(newSocialActivitySet));

		SocialActivitySet existingSocialActivitySet = _persistence.findByPrimaryKey(newSocialActivitySet.getPrimaryKey());

		Assert.assertEquals(existingSocialActivitySet.getActivitySetId(),
			newSocialActivitySet.getActivitySetId());
		Assert.assertEquals(existingSocialActivitySet.getGroupId(),
			newSocialActivitySet.getGroupId());
		Assert.assertEquals(existingSocialActivitySet.getCompanyId(),
			newSocialActivitySet.getCompanyId());
		Assert.assertEquals(existingSocialActivitySet.getUserId(),
			newSocialActivitySet.getUserId());
		Assert.assertEquals(existingSocialActivitySet.getCreateDate(),
			newSocialActivitySet.getCreateDate());
		Assert.assertEquals(existingSocialActivitySet.getModifiedDate(),
			newSocialActivitySet.getModifiedDate());
		Assert.assertEquals(existingSocialActivitySet.getClassNameId(),
			newSocialActivitySet.getClassNameId());
		Assert.assertEquals(existingSocialActivitySet.getClassPK(),
			newSocialActivitySet.getClassPK());
		Assert.assertEquals(existingSocialActivitySet.getType(),
			newSocialActivitySet.getType());
		Assert.assertEquals(existingSocialActivitySet.getExtraData(),
			newSocialActivitySet.getExtraData());
		Assert.assertEquals(existingSocialActivitySet.getActivityCount(),
			newSocialActivitySet.getActivityCount());
	}

	@Test
	public void testCountByGroupId() {
		try {
			_persistence.countByGroupId(RandomTestUtil.nextLong());

			_persistence.countByGroupId(0L);
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testCountByUserId() {
		try {
			_persistence.countByUserId(RandomTestUtil.nextLong());

			_persistence.countByUserId(0L);
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testCountByG_U_T() {
		try {
			_persistence.countByG_U_T(RandomTestUtil.nextLong(),
				RandomTestUtil.nextLong(), RandomTestUtil.nextInt());

			_persistence.countByG_U_T(0L, 0L, 0);
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testCountByC_C_T() {
		try {
			_persistence.countByC_C_T(RandomTestUtil.nextLong(),
				RandomTestUtil.nextLong(), RandomTestUtil.nextInt());

			_persistence.countByC_C_T(0L, 0L, 0);
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testCountByG_U_C_T() {
		try {
			_persistence.countByG_U_C_T(RandomTestUtil.nextLong(),
				RandomTestUtil.nextLong(), RandomTestUtil.nextLong(),
				RandomTestUtil.nextInt());

			_persistence.countByG_U_C_T(0L, 0L, 0L, 0);
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testCountByU_C_C_T() {
		try {
			_persistence.countByU_C_C_T(RandomTestUtil.nextLong(),
				RandomTestUtil.nextLong(), RandomTestUtil.nextLong(),
				RandomTestUtil.nextInt());

			_persistence.countByU_C_C_T(0L, 0L, 0L, 0);
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		SocialActivitySet newSocialActivitySet = addSocialActivitySet();

		SocialActivitySet existingSocialActivitySet = _persistence.findByPrimaryKey(newSocialActivitySet.getPrimaryKey());

		Assert.assertEquals(existingSocialActivitySet, newSocialActivitySet);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail(
				"Missing entity did not throw NoSuchActivitySetException");
		}
		catch (NoSuchActivitySetException nsee) {
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

	protected OrderByComparator<SocialActivitySet> getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("SocialActivitySet",
			"activitySetId", true, "groupId", true, "companyId", true,
			"userId", true, "createDate", true, "modifiedDate", true,
			"classNameId", true, "classPK", true, "type", true, "extraData",
			true, "activityCount", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		SocialActivitySet newSocialActivitySet = addSocialActivitySet();

		SocialActivitySet existingSocialActivitySet = _persistence.fetchByPrimaryKey(newSocialActivitySet.getPrimaryKey());

		Assert.assertEquals(existingSocialActivitySet, newSocialActivitySet);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		SocialActivitySet missingSocialActivitySet = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingSocialActivitySet);
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereAllPrimaryKeysExist()
		throws Exception {
		SocialActivitySet newSocialActivitySet1 = addSocialActivitySet();
		SocialActivitySet newSocialActivitySet2 = addSocialActivitySet();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newSocialActivitySet1.getPrimaryKey());
		primaryKeys.add(newSocialActivitySet2.getPrimaryKey());

		Map<Serializable, SocialActivitySet> socialActivitySets = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(2, socialActivitySets.size());
		Assert.assertEquals(newSocialActivitySet1,
			socialActivitySets.get(newSocialActivitySet1.getPrimaryKey()));
		Assert.assertEquals(newSocialActivitySet2,
			socialActivitySets.get(newSocialActivitySet2.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereNoPrimaryKeysExist()
		throws Exception {
		long pk1 = RandomTestUtil.nextLong();

		long pk2 = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(pk1);
		primaryKeys.add(pk2);

		Map<Serializable, SocialActivitySet> socialActivitySets = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(socialActivitySets.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereSomePrimaryKeysExist()
		throws Exception {
		SocialActivitySet newSocialActivitySet = addSocialActivitySet();

		long pk = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newSocialActivitySet.getPrimaryKey());
		primaryKeys.add(pk);

		Map<Serializable, SocialActivitySet> socialActivitySets = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, socialActivitySets.size());
		Assert.assertEquals(newSocialActivitySet,
			socialActivitySets.get(newSocialActivitySet.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithNoPrimaryKeys()
		throws Exception {
		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		Map<Serializable, SocialActivitySet> socialActivitySets = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(socialActivitySets.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithOnePrimaryKey()
		throws Exception {
		SocialActivitySet newSocialActivitySet = addSocialActivitySet();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newSocialActivitySet.getPrimaryKey());

		Map<Serializable, SocialActivitySet> socialActivitySets = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, socialActivitySets.size());
		Assert.assertEquals(newSocialActivitySet,
			socialActivitySets.get(newSocialActivitySet.getPrimaryKey()));
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = SocialActivitySetLocalServiceUtil.getActionableDynamicQuery();

		actionableDynamicQuery.setPerformActionMethod(new ActionableDynamicQuery.PerformActionMethod() {
				@Override
				public void performAction(Object object) {
					SocialActivitySet socialActivitySet = (SocialActivitySet)object;

					Assert.assertNotNull(socialActivitySet);

					count.increment();
				}
			});

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		SocialActivitySet newSocialActivitySet = addSocialActivitySet();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(SocialActivitySet.class,
				SocialActivitySet.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("activitySetId",
				newSocialActivitySet.getActivitySetId()));

		List<SocialActivitySet> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		SocialActivitySet existingSocialActivitySet = result.get(0);

		Assert.assertEquals(existingSocialActivitySet, newSocialActivitySet);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(SocialActivitySet.class,
				SocialActivitySet.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("activitySetId",
				RandomTestUtil.nextLong()));

		List<SocialActivitySet> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		SocialActivitySet newSocialActivitySet = addSocialActivitySet();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(SocialActivitySet.class,
				SocialActivitySet.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"activitySetId"));

		Object newActivitySetId = newSocialActivitySet.getActivitySetId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("activitySetId",
				new Object[] { newActivitySetId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingActivitySetId = result.get(0);

		Assert.assertEquals(existingActivitySetId, newActivitySetId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(SocialActivitySet.class,
				SocialActivitySet.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"activitySetId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("activitySetId",
				new Object[] { RandomTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	protected SocialActivitySet addSocialActivitySet()
		throws Exception {
		long pk = RandomTestUtil.nextLong();

		SocialActivitySet socialActivitySet = _persistence.create(pk);

		socialActivitySet.setGroupId(RandomTestUtil.nextLong());

		socialActivitySet.setCompanyId(RandomTestUtil.nextLong());

		socialActivitySet.setUserId(RandomTestUtil.nextLong());

		socialActivitySet.setCreateDate(RandomTestUtil.nextLong());

		socialActivitySet.setModifiedDate(RandomTestUtil.nextLong());

		socialActivitySet.setClassNameId(RandomTestUtil.nextLong());

		socialActivitySet.setClassPK(RandomTestUtil.nextLong());

		socialActivitySet.setType(RandomTestUtil.nextInt());

		socialActivitySet.setExtraData(RandomTestUtil.randomString());

		socialActivitySet.setActivityCount(RandomTestUtil.nextInt());

		_socialActivitySets.add(_persistence.update(socialActivitySet));

		return socialActivitySet;
	}

	private static Log _log = LogFactoryUtil.getLog(SocialActivitySetPersistenceTest.class);
	private List<SocialActivitySet> _socialActivitySets = new ArrayList<SocialActivitySet>();
	private ModelListener<SocialActivitySet>[] _modelListeners;
	private SocialActivitySetPersistence _persistence = SocialActivitySetUtil.getPersistence();
}