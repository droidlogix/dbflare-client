package com.droidlogix.dbflare.client;

import com.droidlogix.dbflare.client.models.Pagination;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author John Pili
 * @since 2016-11-14
 */

public interface IDbFlareClient
{
	/**
	 * Insert a record and expect the generated record as a result.
	 * @param eid
	 * @param item
	 * @param typeOfT
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	<T> T zinsert(String eid, T item, Type typeOfT) throws Exception;

	/**
	 * Insert a record and expect the generated record as a result.
	 * @param eid
	 * @param item
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	<T> Map<String, Object> zinsert(String eid, T item) throws Exception;

	/**
	 * Insert a record and expect the generated record as a result. This support bulk insert
	 * @param eid
	 * @param item
	 * @param typeOfT
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	<T> List<T> zinsert(String eid, List<T> item, Type typeOfT) throws Exception;

	/**
	 * Insert a record and expect the generated record as a result. This support bulk insert
	 * @param eid
	 * @param item
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	<T> List<Map<String, Object>> zinsert(String eid, List<T> item) throws Exception;

	/**
	 * Update a record and expect the updated record as a result.
	 * @param eid
	 * @param urlParameters
	 * @param item
	 * @param typeOfT
	 * @param <T>
	 * @return T result
	 * @throws Exception
	 */
	<T> T zupdate(String eid, Map<String, Object> urlParameters, T item, Type typeOfT) throws Exception;

	/**
	 * Update a record and expect the updated record as a result.
	 * @param eid
	 * @param urlParameters
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	<T> Map<String, Object> zupdate(String eid, Map<String, Object> urlParameters, T item) throws Exception;

	/**
	 * Update a record and expect the updated record as a result. This support bulk update
	 * @param eid
	 * @param urlParameters
	 * @param item
	 * @param typeOfT
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	<T> List<T> zupdate(String eid, Map<String, Object> urlParameters, List<T> item, Type typeOfT) throws Exception;

	/**
	 * Update a record and expect the updated record as a result. This support bulk update
	 * @param eid
	 * @param urlParameters
	 * @param item
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	<T> List<Map<String, Object>> zupdate(String eid, Map<String, Object> urlParameters, List<T> item) throws Exception;

	/**
	 * Delete a record and expect the deleted record as a result
	 * @param eid
	 * @param queryParams
	 * @throws Exception
	 */
	void zdelete(String eid, Map<String, Object> queryParams) throws Exception;

	/**
	 * Get result as Map
	 * @param eid
	 * @param queryParams
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> zgetOne(String eid, Map<String, Object> queryParams) throws Exception;

	/**
	 * Get result as Object of T
	 * @param eid
	 * @param queryParams
	 * @param typeOfT
	 * @return Object of T
	 * @throws Exception
	 */
	<T> T zgetOne(String eid, Map<String, Object> queryParams, Type typeOfT) throws Exception;

	/**
	 * Get result as Object of T. Provide an IObjectAssembler for custom object generation and mapping
	 * @param eid
	 * @param queryParams
	 * @param objectAssembler
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	<T> T zgetOne(String eid, Map<String, Object> queryParams, IObjectAssembler objectAssembler) throws Exception;

	/**
	 * Get result as List Map
	 * @param eid
	 * @param urlParameters
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> zgetList(String eid, Map<String, Object> urlParameters) throws Exception;

	List<Map<String, Object>> zgetList(String eid, Map<String, Object> urlParameters, Pagination pagination) throws Exception;

	/**
	 * Get result as List of T
	 * @param eid
	 * @param urlParameters
	 * @param typeOfT
	 * @return List of T
	 * @throws Exception
	 */
	<T> List<T> zgetList(String eid, Map<String, Object> urlParameters, Type typeOfT) throws Exception;

	/**
	 * Get result as List object. Provide an IObjectAssembler for custom object generation and mapping
	 * @param eid
	 * @param urlParameters
	 * @param objectAssembler
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	<T> List<T> zgetList(String eid, Map<String, Object> urlParameters, IObjectAssembler objectAssembler) throws Exception;

	/**
	 * Get result as List of T. PagingInformation used as reference helps in paginating the results
	 * @param eid
	 * @param urlParameters
	 * @param pagination
	 * @param typeOfT
	 * @param <T>
	 * @return List of T
	 * @throws Exception
	 */
	<T> List<T> zgetList(String eid, Map<String, Object> urlParameters, Pagination pagination, Type typeOfT) throws Exception;

	<T> List<T> zgetList(String eid, Map<String, Object> urlParameters, Pagination pagination, IObjectAssembler objectAssembler) throws Exception;

	/**
	 *  Get result as List of T. urlParameters2 is used for sending query parameters as collection. It is very useful in multiple filter query.
	 * @param eid
	 * @param urlParameters
	 * @param urlParameters2 used for Collection values
	 * @param typeOfT
	 * @param <T>
	 * @return List of T
	 * @throws Exception
	 */
	<T> List<T> zgetList(String eid, Map<String, Object> urlParameters,
	                     Map<String, Collection<?>> urlParameters2, Type typeOfT) throws Exception;

	<T> List<T> zgetList(String eid, Map<String, Object> urlParameters,
	                     Map<String, Collection<?>> urlParameters2, IObjectAssembler objectAssembler) throws Exception;

	List<Map<String, Object>> zgetList(String eid, Map<String, Object> urlParameters, Map<String, Collection<?>> queryParamsCollection) throws Exception;

	/**
	 * Get result as List of T. queryParamsCollection is used for sending query parameters as collection. It is very useful in multiple filter query.
	 * PagingInformation used as reference helps in paginating the results
	 * @param eid
	 * @param urlParameters
	 * @param queryParamsCollection used for Collection values
	 * @param pagination
	 * @param typeOfT
	 * @param <T>
	 * @return List of T
	 * @throws Exception
	 */
	<T> List<T> zgetList(String eid, Map<String, Object> urlParameters,
	                     Map<String, Collection<?>> queryParamsCollection, Pagination pagination, Type typeOfT) throws Exception;

	List<Map<String, Object>> zgetList(String eid, Map<String, Object> queryParams, Map<String, Collection<?>> queryParamsCollection, Pagination pagination) throws Exception;


	String zgetJSONString(String eid, Map<String, Object> queryParams) throws Exception;

	String zgetJSONString(String eid, Map<String, Object> queryParams, Map<String, Collection<?>> queryParamsCollection) throws Exception;

	/**
	 * Get the result as String
	 * @param eid
	 * @param queryParams
	 * @return int
	 * @throws Exception
	 */
	String zgetString(String eid, Map<String, Object> queryParams) throws Exception;

	/**
	 * Get the result as String
	 * @param eid
	 * @param queryParams
	 * @return String
	 * @throws Exception
	 */
	long zgetInteger(String eid, Map<String, Object> queryParams) throws Exception;

	/**
	 * Get the result as long
	 * @param eid
	 * @param queryParams
	 * @return long
	 * @throws Exception
	 */
	long zgetLong(String eid, Map<String, Object> queryParams) throws Exception;

	/**
	 * Get the result as double
	 * @param eid
	 * @param queryParams
	 * @return long
	 * @throws Exception
	 */
	double zgetDouble(String eid, Map<String, Object> queryParams) throws Exception;

	/**
	 * Execute a non select native query
	 * @param eid
	 * @param queryParams
	 * @return String
	 * @throws Exception
	 */
	String zexecuteJSON(String eid, Map<String, Object> queryParams) throws Exception;
}
