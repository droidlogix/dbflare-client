package com.droidlogix.dbflare.client;

import com.google.gson.*;
import com.mashape.unirest.http.HttpResponse;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public interface IResultProcessor
{
	/**
	 * Convert DbFlare result into Object of T
	 *
	 * @param httpResponse
	 * @param typeOfT
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	<T> T processObjectResult(Future<HttpResponse<String>> httpResponse, Type typeOfT) throws Exception;

	/**
	 * Convert DbFlare result to Map
	 *
	 * @param httpResponse
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> processObjectResult(Future<HttpResponse<String>> httpResponse) throws Exception;

	<T> List<T> processListResult(Future<HttpResponse<String>> httpResponse, Type typeOfT) throws Exception;

	<T> List<T> processListResult(Future<HttpResponse<String>> httpResponse, IObjectAssembler objectAssembler) throws Exception;

	<T> List<T> processListResult(Future<HttpResponse<String>> httpResponse, PagingInformation pagingInformation, Type typeOfT) throws Exception;

	<T> List<T> processListResult(Future<HttpResponse<String>> httpResponse, PagingInformation pagingInformation, IObjectAssembler objectAssembler) throws Exception;

	String processJSONResult(Future<HttpResponse<String>> httpResponse) throws Exception;

	JsonPrimitive processJsonPrimitiveResult(Future<HttpResponse<String>> httpResponse) throws Exception;
}
