package com.droidlogix.dbflareclient;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by mrprintedwall on 11/14/16.
 */
public interface RestfulClientInterface
{
	String HTTP_METHOD_POST = "post";
	String HTTP_METHOD_PUT = "put";
	String HTTP_METHOD_DELETE =  "delete";
	String HTTP_METHOD_GET = "get";

	/**
	 * Get result as Object of T
	 * @param eid
	 * @param urlParameters
	 * @param typeOfT
	 * @return Object of T
	 * @throws Exception
	 */
	<T> T zgetSingle(String eid, Map<String, Object> urlParameters, Type typeOfT) throws Exception;

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
	 * Delete a record and expect the deleted record as a result
	 * @param eid
	 * @param urlParameters
	 * @param <T>
	 * @return T result
	 * @throws Exception
	 */
	<T> T zdelete(String eid, Map<String, Object> urlParameters, Type typeOfT) throws Exception;
}
