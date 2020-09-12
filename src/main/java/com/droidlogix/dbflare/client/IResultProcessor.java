package com.droidlogix.dbflare.client;

import com.droidlogix.dbflare.client.models.Pagination;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.*;
import com.mashape.unirest.http.HttpResponse;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public interface IResultProcessor
{
	JsonElement parse() throws Exception;

	<T> T parse(Type typeOfT) throws Exception;

	<T> T parse(IObjectAssembler objectAssembler) throws Exception;

	Map<String, Object> parseToMap() throws Exception;

	<T> List<T> parseToList(Type typeOfT) throws Exception;

	<T> List<T> parseToList(Type typeOfT, Pagination pagination) throws Exception;

	<T> List<T> parseToList(IObjectAssembler objectAssembler) throws Exception;

	List<Map<String, Object>> parseToListMap() throws Exception;

	List<Map<String, Object>> parseToListMap(Pagination pagination) throws Exception;

	JsonPrimitive parseToJsonPrimitive() throws Exception;

	JsonNode parseToJsonNode() throws Exception;

	String parseToJSONString() throws Exception;

	//region ORIGINAL METHODS

	//Map<String, Object> parseToMap(Future<HttpResponse<String>> httpResponse) throws Exception;

	//<T> List<T> parseToList(Future<HttpResponse<String>> httpResponse, Type typeOfT) throws Exception;

	//<T> List<T> parseToList(Future<HttpResponse<String>> httpResponse, IObjectAssembler objectAssembler) throws Exception;

	//<T> List<T> parseToList(Future<HttpResponse<String>> httpResponse, Pagination pagination, Type typeOfT) throws Exception;

	//<T> List<T> parseToList(Future<HttpResponse<String>> httpResponse, Pagination pagination, IObjectAssembler objectAssembler) throws Exception;

	//List<Map<String, Object>> parseToListMap(Future<HttpResponse<String>> httpResponse) throws Exception;

	//List<Map<String, Object>> parseToListMap(Future<HttpResponse<String>> httpResponse, Pagination pagination) throws Exception;

	//JsonPrimitive parseToJsonPrimitive(Future<HttpResponse<String>> httpResponse) throws Exception;

	//endregion
}
