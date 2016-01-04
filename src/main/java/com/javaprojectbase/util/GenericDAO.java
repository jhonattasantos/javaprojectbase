package com.javaprojectbase.util;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;

public class GenericDAO<T, ID extends Serializable> implements IGenericDAO<T, ID> {

	private final Class<T> persistentClass;

	@PersistenceContext
	private EntityManager entityManager = Connection.getEntityManager();
	private EntityTransaction transaction = null;

	@SuppressWarnings("unchecked")
	public GenericDAO() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	public GenericDAO(final Class<T> persistentClass) {
		super();
		this.persistentClass = persistentClass;
	}

	@Override
	public Class<T> getEntityClass() {
		return persistentClass;
	}

	@Override
	public T findById(ID id) {
		final T result = getEntityManager().find(persistentClass, id);
		return result;
	}

	@Override
	public List<T> findAll() {
		return findByCriteria();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByExample(T exampleInstance) {
		Session session = (Session) getEntityManager().getDelegate();
		Criteria crit = session.createCriteria(getEntityClass());
		final List<T> result = crit.list();
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByNamedQuery(final String name, Object... params) {
		Query query = getEntityManager().createNamedQuery(name);

		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}

		final List<T> result = (List<T>) query.getResultList();
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByNamedQueryAndNamedParams(String name, Map<String, ? extends Object> params) {
		Query query = getEntityManager().createNamedQuery(name);

		for (final Map.Entry<String, ? extends Object> param : params.entrySet()) {
			query.setParameter(param.getKey(), param.getValue());
		}

		final List<T> result = (List<T>) query.getResultList();

		return result;
	}

	@Override
	public int countAll() {
		return countByCriteria();
	}

	@Override
	public int countByExample(T exampleInstance) {
		Session session = (Session) getEntityManager().getDelegate();
		Criteria crit = session.createCriteria(getEntityClass());
		crit.setProjection(Projections.rowCount());
		crit.add(Example.create(exampleInstance));

		return (Integer) crit.list().get(0);
	}
	
	private void initTransaction() {
		if (transaction == null) {
			transaction = getEntityManager().getTransaction();
		}
		if (!transaction.isActive()) {
			transaction.begin();
		}
	}

	@Override
	public T save(T entity) {
		T savedEntity = null;
		initTransaction();
		try {
			if (getEntityManager().contains(entity)) {
				getEntityManager().merge(entity);
				if (transaction.isActive()) {
					transaction.commit();
				}
				savedEntity = entity;
			} else {
				getEntityManager().persist(entity);
				if (transaction.isActive()) {
					transaction.commit();
				}
				savedEntity = entity;
			}
		} catch (Exception e) {
			if (transaction.isActive()) {
				transaction.setRollbackOnly();
				return null;
			}
		}

		return savedEntity;
	}

	@Override
	public void remove(T entity) {
		initTransaction();
		try {
			getEntityManager().remove(entity);
			if (transaction.isActive()) {
				transaction.commit();
			}
		}catch(Exception e){
			if (transaction.isActive()) {
				transaction.setRollbackOnly();
			}
		}
	}

	@Override
	public void remove(ID id) {
		getEntityManager().remove(this.findById(id));
	}

	/**
	 * Set the JPA entity manager to use.
	 *
	 * @param entityManager
	 */
	@PersistenceContext
	public void setEntityManager(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * Use this inside subclasses as a convenience method.
	 */
	protected List<T> findByCriteria(final Criterion... criterion) {
		return findByCriteria(-1, -1, criterion);
	}

	/**
	 * Use this inside subclasses as a convenience method.
	 */
	@SuppressWarnings("unchecked")
	protected List<T> findByCriteria(final int firstResult, final int maxResults, final Criterion... criterion) {

		Session session = (Session) getEntityManager().getDelegate();
		Criteria crit = session.createCriteria(getEntityClass());

		for (final Criterion c : criterion) {
			crit.add(c);
		}

		if (firstResult > 0) {
			crit.setFirstResult(firstResult);
		}

		if (maxResults > 0) {
			crit.setMaxResults(maxResults);
		}

		final List<T> result = crit.list();
		return result;
	}

	/**
	 * Get counts by Criteria
	 * 
	 * @param criterion
	 * @return int
	 */
	protected int countByCriteria(Criterion... criterion) {

		Session session = (Session) getEntityManager().getDelegate();

		Criteria crit = session.createCriteria(getEntityClass());
		crit.setProjection(Projections.rowCount());

		for (final Criterion c : criterion) {
			crit.add(c);
		}

		return (Integer) crit.list().get(0);
	}

}
