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
	/**
	 * Get result as a single object. The object will be as Object of T
	 *
	 * @param eid
	 * @param urlParameters
	 * @param typeOfT
	 * @return
	 * @throws Exception
	 */
	<T> T zgetSingle(String eid, Map<String, Object> urlParameters, Type typeOfT) throws Exception;

	/**
	 * Get result as list. The list will be as List object of T
	 *
	 * @param eid
	 * @param urlParameters
	 * @param typeOfT
	 * @return
	 * @throws Exception
	 */
	<T> List<T> zgetList(String eid, Map<String, Object> urlParameters, Type typeOfT) throws Exception;

	<T> List<T> zgetList(String eid, Map<String, Object> urlParameters, PagingInformation pagingInformation, Type typeOfT) throws Exception;

	/**
	 *  Get result as List. The list will be as List of object of T.
	 *
	 * @param eid
	 * @param urlParameters
	 * @param urlParameters2
	 * @param typeOfT
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	<T> List<T> zgetList(String eid, Map<String, Object> urlParameters,
	                     Map<String, Collection<?>> urlParameters2, Type typeOfT) throws Exception;

	<T> List<T> zgetList(String eid, Map<String, Object> urlParameters,
	                     Map<String, Collection<?>> urlParameters2, PagingInformation pagingInformation, Type typeOfT) throws Exception;

	/**
	 * Get the result as a paginated object. Putting result on a page will help in pagination of the result
	 *
	 * @param eid
	 * @param urlParameters
	 * @param typeOfT
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	<T> Page<T> zgetPage(String eid, Map<String, Object> urlParameters, Type typeOfT) throws Exception;

	/**
	 * Get the result as a paginated object. Putting result on a page will help in pagination of the result
	 *
	 * @param eid
	 * @param urlParameters
	 * @param urlParameters2
	 * @param typeOfT
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	<T> Page<T> zgetPage(String eid, Map<String, Object> urlParameters,
	                     Map<String, Collection<?>> urlParameters2, Type typeOfT) throws Exception;

	/**
	 * Get the result as JSON
	 *
	 * @param eid
	 * @param urlParameters
	 * @return
	 * @throws Exception
	 */
	String zgetJSON(String eid, Map<String, Object> urlParameters) throws Exception;

	/**
	 * Get the result as JSON
	 *
	 * @param eid
	 * @param urlParameters
	 * @param urlParameters2
	 * @return
	 * @throws Exception
	 */
	String zgetJSON(String eid, Map<String, Object> urlParameters,
	                Map<String, Collection<?>> urlParameters2) throws Exception;

	/**
	 * Send a HTTP GET command and ignore the result from DbFlare
	 *
	 * @param eid
	 * @param urlParameters
	 * @throws Exception
	 */
	void zexecute(String eid, Map<String, Object> urlParameters) throws Exception;

	/**
	 * Send a HTTP GET command and ignore the result from DbFlare
	 *
	 * @param eid
	 * @param urlParameters
	 * @param urlParameters2
	 * @throws Exception
	 */
	void zexecute(String eid, Map<String, Object> urlParameters,
	              Map<String, Collection<?>> urlParameters2) throws Exception;

	/**
	 * Insert via HTTP POST and process as POJO result
	 *
	 * @param eid
	 * @param urlParameters
	 * @param item
	 * @param typeOfT
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	<T> T zinsert(String eid, Map<String, Object> urlParameters, T item, Type typeOfT) throws Exception;

	/**
	 * Update via HTTP POST and process as POJO result
	 *
	 * @param eid
	 * @param urlParameters
	 * @param item
	 * @param typeOfT
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	<T> T zupdate(String eid, Map<String, Object> urlParameters, T item, Type typeOfT) throws Exception;

	/**
	 * Delete via HTTP DELETE and process as POJO result object
	 *
	 * @param eid
	 * @param urlParameters
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	<T> T zdelete(String eid, Map<String, Object> urlParameters, Type typeOfT) throws Exception;
}
