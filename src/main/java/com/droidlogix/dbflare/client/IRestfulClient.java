package com.droidlogix.dbflare.client;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author John Pili
 * @since 2016-11-14
 */

public interface IRestfulClient
{
	String HTTP_METHOD_POST = "post";
	String HTTP_METHOD_PUT = "put";
	String HTTP_METHOD_DELETE =  "delete";
	String HTTP_METHOD_GET = "get";

	/**
	 * Insert a record and expect the generated record as a result
	 * @param eid
	 * @param urlParameters
	 * @param item
	 * @param typeOfT
	 * @param <T>
	 * @return T result
	 * @throws Exception
	 */
	<T> T zinsert(String eid, Map<String, Object> urlParameters, T item, Type typeOfT) throws Exception;

	<T> List<T> zinsert(String eid, Map<String, Object> urlParameters, List<T> item, Type typeOfT) throws Exception;

	/**
	 * Insert a record and expect the generated record as a result
	 * @param eid
	 * @param urlParameters
	 * @param item
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	<T> Map<String, Object> zinsert(String eid, Map<String, Object> urlParameters, T item) throws Exception;

	<T> List<Map<String, Object>> zinsert(String eid, Map<String, Object> urlParameters, List<T> item) throws Exception;

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
	 * Delete a record and expect the deleted record as a result
	 * @param eid
	 * @param urlParameters
	 * @param <T>
	 * @return T result
	 * @throws Exception
	 */
	<T> List<T> zdelete(String eid, Map<String, Object> urlParameters, Type typeOfT) throws Exception;

	/**
	 * Delete a record and expect the deleted record as a result
	 * @param eid
	 * @param urlParameters
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> zdelete(String eid, Map<String, Object> urlParameters) throws Exception;

	/**
	 * Get result as Object of T
	 * @param eid
	 * @param urlParameters
	 * @param typeOfT
	 * @return Object of T
	 * @throws Exception
	 */
	<T> T zgetSingle(String eid, Map<String, Object> urlParameters, Type typeOfT) throws Exception;

	<T> T zgetSingle(String eid, Map<String, Object> urlParameters, IObjectAssembler objectAssembler) throws Exception;

	/**
	 * Get result as List of T
	 * @param eid
	 * @param urlParameters
	 * @param typeOfT
	 * @return List of T
	 * @throws Exception
	 */
	<T> List<T> zgetList(String eid, Map<String, Object> urlParameters, Type typeOfT) throws Exception;

	<T> List<T> zgetList(String eid, Map<String, Object> urlParameters, IObjectAssembler objectAssembler) throws Exception;

	/**
	 * Get result as List of T. PagingInformation used as reference helps in paginating the results
	 * @param eid
	 * @param urlParameters
	 * @param pagingInformation
	 * @param typeOfT
	 * @param <T>
	 * @return List of T
	 * @throws Exception
	 */
	<T> List<T> zgetList(String eid, Map<String, Object> urlParameters, PagingInformation pagingInformation, Type typeOfT) throws Exception;

	<T> List<T> zgetList(String eid, Map<String, Object> urlParameters, PagingInformation pagingInformation, IObjectAssembler objectAssembler) throws Exception;

	/**
	 *  Get result as List of T. urlParameters2 is used for sending query parameters as collection. It is very useful in multiple filter query.
	 * @param eid
	 * @param urlParameters
	 * @param urlParameters2
	 * @param typeOfT
	 * @param <T>
	 * @return List of T
	 * @throws Exception
	 */
	<T> List<T> zgetList(String eid, Map<String, Object> urlParameters,
	                     Map<String, Collection<?>> urlParameters2, Type typeOfT) throws Exception;

	<T> List<T> zgetList(String eid, Map<String, Object> urlParameters,
	                     Map<String, Collection<?>> urlParameters2, IObjectAssembler objectAssembler) throws Exception;

	/**
	 * Get result as List of T. urlParameters2 is used for sending query parameters as collection. It is very useful in multiple filter query.
	 * PagingInformation used as reference helps in paginating the results
	 * @param eid
	 * @param urlParameters
	 * @param urlParameters2
	 * @param pagingInformation
	 * @param typeOfT
	 * @param <T>
	 * @return List of T
	 * @throws Exception
	 */
	<T> List<T> zgetList(String eid, Map<String, Object> urlParameters,
	                     Map<String, Collection<?>> urlParameters2, PagingInformation pagingInformation, Type typeOfT) throws Exception;

	/**
	 * Get the result as JSON String
	 * @param eid
	 * @param urlParameters
	 * @return String
	 * @throws Exception
	 */
	String zgetJSON(String eid, Map<String, Object> urlParameters) throws Exception;

	/**
	 * Get the result as JSON String
	 * @param eid
	 * @param urlParameters
	 * @param urlParameters2
	 * @return String
	 * @throws Exception
	 */
	String zgetJSON(String eid, Map<String, Object> urlParameters,
	                Map<String, Collection<?>> urlParameters2) throws Exception;

	/**
	 * Get the result as String
	 * @param eid
	 * @param urlParameters
	 * @return int
	 * @throws Exception
	 */
	String zgetString(String eid, Map<String, Object> urlParameters) throws Exception;

	/**
	 * Get the result as String
	 * @param eid
	 * @param urlParameters
	 * @return String
	 * @throws Exception
	 */
	long zgetInteger(String eid, Map<String, Object> urlParameters) throws Exception;

	/**
	 * Get the result as long
	 * @param eid
	 * @param urlParameters
	 * @return long
	 * @throws Exception
	 */
	long zgetLong(String eid, Map<String, Object> urlParameters) throws Exception;

	/**
	 * Get the result as double
	 * @param eid
	 * @param urlParameters
	 * @return long
	 * @throws Exception
	 */
	double zgetDouble(String eid, Map<String, Object> urlParameters) throws Exception;
}
