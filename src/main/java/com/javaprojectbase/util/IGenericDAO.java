package com.javaprojectbase.util;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface IGenericDAO<T, PK extends Serializable> {

	 /**
     * Get the Class of the entity.
     *
     * @return the class
     */
    Class<T> getEntityClass();

    /**
     * Find an entity by its primary key
     *
     * @param id the primary key
     * @return the entity
     */
    T findById(final PK id);

    /**
     * Load all entities.
     *
     * @return the list of entities
     */
    List<T> findAll();

    /**
     * Find entities based on an example.
     *
     * @param exampleInstance the example
     * @return the list of entities
     */
    List<T> findByExample(final T exampleInstance);

    /**
     * Find using a named query.
     *
     * Note that Named Queries are configured in the Entities and look
     * like this:
     *
     * <pre>
     *
     * {@literal @}Entity
     * {@literal @}NamedQuery(name="findSalaryForNameAndDepartment",
     *   query="SELECT e.salary " +
     *         "FROM Employee e " +
     *         "WHERE e.department.name = :deptName AND " +
     *         "      e.name = :empName")
     *  public class Employee {
     *  ...
     * </pre>
     *
     * @param queryName the name of the query
     * @param params the query parameters
     *
     * @return the list of entities
     */
    List<T> findByNamedQuery(final String queryName, Object... params);

    /**
     * Find using a named query.
     *
     * @param queryName the name of the query
     * @param params the query parameters
     *
     * @return the list of entities
     */
    List<T> findByNamedQueryAndNamedParams(final String queryName, final Map<String, ?extends Object> params);

    /**
     * Count all entities.
     *
     * @return the number of entities
     */
    int countAll();

    /**
     * Count entities based on an example.
     *
     * @param exampleInstance the search criteria
     * @return the number of entities
     */
    int countByExample(final T exampleInstance);
 
    /**
     * Save an entity. This is an "INSERT or UPDATE" operation based on
     * the existence of an entity in the current persistence context. This
     * is an approximation of the Hibernate "Session.saveOrUpdate()" method.
     * 
     * @param entity the entity to save
     * 
     * @return the saved entity
     */
    T save(final T entity);

    /**
     * Delete an entity from the persistence store
     * 
     * @param entity the entity to delete�?
     */
    void remove(final T entity);
    
    /**
     * Delete an entity from the persistence store by Primary Key ID
     * 
     * @param id
     */
    void remove(final PK id);
    
    /**	
     * Setting for EntityManager DI
     *
     * @param entityManager An instance of an EntityManager
     */
    void setEntityManager(EntityManager entityManager);
}
