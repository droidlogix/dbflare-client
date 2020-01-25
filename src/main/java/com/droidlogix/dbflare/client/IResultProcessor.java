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
	<T> T parse(Future<HttpResponse<String>> httpResponse, Type typeOfT) throws Exception;

	<T> T parse(Future<HttpResponse<String>> httpResponse, IObjectAssembler objectAssembler) throws Exception;

	Map<String, Object> parseToMap(Future<HttpResponse<String>> httpResponse) throws Exception;

	<T> List<T> parseToList(Future<HttpResponse<String>> httpResponse, Type typeOfT) throws Exception;

	<T> List<T> parseToList(Future<HttpResponse<String>> httpResponse, IObjectAssembler objectAssembler) throws Exception;

	<T> List<T> parseToList(Future<HttpResponse<String>> httpResponse, PagingInformation pagingInformation, Type typeOfT) throws Exception;

	<T> List<T> parseToList(Future<HttpResponse<String>> httpResponse, PagingInformation pagingInformation, IObjectAssembler objectAssembler) throws Exception;

	List<Map<String, Object>> parseToListMap(Future<HttpResponse<String>> httpResponse) throws Exception;

	List<Map<String, Object>> parseToListMap(Future<HttpResponse<String>> httpResponse, PagingInformation pagingInformation) throws Exception;

	String parseToJSONString(Future<HttpResponse<String>> httpResponse) throws Exception;

	JsonPrimitive parseToJsonPrimitive(Future<HttpResponse<String>> httpResponse) throws Exception;
}
