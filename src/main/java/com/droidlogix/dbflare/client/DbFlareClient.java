package com.droidlogix.dbflare.client;

import com.droidlogix.dbflare.client.models.Pagination;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpMethod;
import kong.unirest.HttpRequest;
import kong.unirest.HttpRequestWithBody;
import kong.unirest.Unirest;

import java.lang.reflect.Type;
import java.util.*;

import static kong.unirest.HttpMethod.*;

/**
 * @author John Pili
 * @since 2016-11-15
 */
public class DbFlareClient implements IDbFlareClient, IRestClient
{
	private ObjectMapper objectMapper;
	private String baseURL;
	private boolean isKeyRequired;
	private String apiKey;

	//region BUILDER

	public static class Config
	{
		private ObjectMapper objectMapper;
		private String baseURL;
		private boolean isKeyRequired;
		private String apiKey;
		private Map<String, String> httpMethodMapping;

		public Config objectMapper(ObjectMapper objectMapper)
		{
			this.objectMapper = objectMapper;
			return this;
		}

		public Config baseUrl(String baseUrl)
		{
			this.baseURL = baseUrl;
			return this;
		}

		public Config isKeyRequired(boolean isKeyRequired)
		{
			this.isKeyRequired = isKeyRequired;
			return this;
		}

		public Config apiKey(String apiKey)
		{
			this.apiKey = apiKey;
			return this;
		}

		public DbFlareClient build()
		{
			if (this.httpMethodMapping == null)
			{
				return new DbFlareClient(this.objectMapper, this.baseURL, this.isKeyRequired, this.apiKey);
			}
			else
			{
				return new DbFlareClient(this.objectMapper, this.baseURL, this.isKeyRequired, this.apiKey, this.httpMethodMapping);
			}
		}
	}

	//endregion

	//region CONSTRUCTOR

	private DbFlareClient(ObjectMapper objectMapper, String baseURL, boolean isKeyRequired, String apiKey)
	{
		this.objectMapper = objectMapper;
		this.baseURL = baseURL;
		this.isKeyRequired = isKeyRequired;
		this.apiKey = apiKey;
	}

	private DbFlareClient(ObjectMapper objectMapper, String baseURL, boolean isKeyRequired, String apiKey, Map<String, String> httpMethodMapping)
	{
		this(objectMapper, baseURL, isKeyRequired, apiKey);
	}

	//endregion

	private HttpRequest prepareHttpRequest(HttpMethod httpMethod, String url, Map<String, String> routeParams, Map<String, Object> queryParams, Map<String, Collection<?>> queryParamsCollection) throws Exception {
		Map<String, String> headers = this.apiKeyCheckpoint();
		headers.put("accept", "application/json;charset=UTF-8");

		switch(httpMethod.name()) {
			case "GET": {
				HttpRequest httpRequest = Unirest.get(this.getBaseURL() + url)
						.headers(headers)
						.queryString(queryParams);
				if(routeParams != null && !routeParams.isEmpty()) {
					for(Map.Entry<String, String> item : routeParams.entrySet()) {
						httpRequest.routeParam(item.getKey(), item.getValue());
					}
				}

				if(queryParamsCollection != null && !queryParamsCollection.isEmpty()) {
					for (Map.Entry<String, Collection<?>> item : queryParamsCollection.entrySet())
					{
						httpRequest.queryString(item.getKey(), queryParamsCollection.get(item.getKey()));
					}
				}
				return httpRequest;
			}
			default: {
				break;
			}
		}
		throw new Exception("Unmapped HTTP Method for prepareHttpRequest");
	}

	private HttpRequestWithBody prepareHttpRequestWithBody(HttpMethod httpMethod, String url, Map<String, String> routeParams, Map<String, Object> queryParams) throws Exception {
		Map<String, String> headers = this.apiKeyCheckpoint();
		headers.put("accept", "application/json;charset=UTF-8");

		switch(httpMethod.name()) {
			case "POST": {
				HttpRequestWithBody httpRequestWithBody = Unirest.post(this.getBaseURL() + url)
						.headers(headers)
						.queryString(queryParams);
				if(routeParams != null && !routeParams.isEmpty()) {
					for(Map.Entry<String, String> item : routeParams.entrySet()) {
						httpRequestWithBody.routeParam(item.getKey(), item.getValue());
					}
				}
				return httpRequestWithBody;
			}
			case "PUT": {
				HttpRequestWithBody httpRequestWithBody = Unirest.put(this.getBaseURL() + url)
						.headers(headers)
						.queryString(queryParams);
				if(routeParams != null && !routeParams.isEmpty()) {
					for(Map.Entry<String, String> item : routeParams.entrySet()) {
						httpRequestWithBody.routeParam(item.getKey(), item.getValue());
					}
				}
				return httpRequestWithBody;
			}
			case "DELETE": {
				HttpRequestWithBody httpRequestWithBody = Unirest.delete(this.getBaseURL() + url)
						.headers(headers)
						.queryString(queryParams);
				if(routeParams != null && !routeParams.isEmpty()) {
					for(Map.Entry<String, String> item : routeParams.entrySet()) {
						httpRequestWithBody.routeParam(item.getKey(), item.getValue());
					}
				}
				return httpRequestWithBody;
			}
			default: {
				break;
			}
		}
		throw new Exception("Unmapped HTTP Method for prepareHttpRequestWithBody");
	}

	@Override
	public <T> IResultProcessor zPost(String url, T payload) throws Exception {
		return zPost(url, null, null, payload);
	}

	@Override
	public <T> IResultProcessor zPost(String url, Map<String, String> routeParams, T payload) throws Exception {
		return zPost(url, routeParams, null, payload);
	}

	@Override
	public <T> IResultProcessor zPost(String url, Map<String, String> routeParams, Map<String, Object> queryParams, T payload) throws Exception {
		return new ResultProcessor(prepareHttpRequestWithBody(POST,
				url,
				routeParams,
				queryParams).body(this.objectMapper.writeValueAsString(payload)).asStringAsync());
	}

	@Override
	public <T> IResultProcessor zPost(String url, List<T> payloads) throws Exception {
		return zPost(url, null, null, payloads);
	}

	@Override
	public <T> IResultProcessor zPost(String url, Map<String, String> routeParams, List<T> payloads) throws Exception {
		return zPost(url, routeParams, null, payloads);
	}

	@Override
	public <T> IResultProcessor zPost(String url, Map<String, String> routeParams, Map<String, Object> queryParams, List<T> payloads) throws Exception {
		return new ResultProcessor(prepareHttpRequestWithBody(POST,
				url,
				routeParams,
				queryParams).body(this.objectMapper.writeValueAsString(payloads)).asStringAsync());
	}

	@Override
	public <T> IResultProcessor zPut(String url, Map<String, String> routeParams, Map<String, Object> queryParams, T payload) throws Exception {
		return new ResultProcessor(prepareHttpRequestWithBody(PUT,
				url,
				routeParams,
				queryParams).body(this.objectMapper.writeValueAsString(payload)).asStringAsync());
	}

	@Override
	public <T> IResultProcessor zPut(String url, Map<String, String> routeParams, Map<String, Object> queryParams, List<T> payloads) throws Exception {
		return new ResultProcessor(prepareHttpRequestWithBody(PUT,
				url,
				routeParams,
				queryParams).body(this.objectMapper.writeValueAsString(payloads)).asStringAsync());
	}

	@Override
	public <T> IResultProcessor zUpsert(String url, Map<String, String> routeParams, Map<String, Object> queryParams, T payload) throws Exception {
		return zPost(url, routeParams, queryParams, payload);
	}

	@Override
	public <T> IResultProcessor zUpsert(String url, Map<String, String> routeParams, Map<String, Object> queryParams, List<T> payloads) throws Exception {
		return zPost(url, routeParams, queryParams, payloads);
	}

	@Override
	public void zDelete(String url, Map<String, String> routeParams, Map<String, Object> queryParams) throws Exception {
		new ResultProcessor(prepareHttpRequestWithBody(DELETE,
				url,
				routeParams,
				queryParams).asStringAsync()).parse();
	}

	@Override
	public IResultProcessor zGet(String url) throws Exception {
		return new ResultProcessor(prepareHttpRequest(GET,
				url,
				null,
				null,
				null).asStringAsync());
	}

	@Override
	public IResultProcessor zGet(String url, Map<String, String> routeParams, Map<String, Object> queryParams) throws Exception {
		return new ResultProcessor(prepareHttpRequest(GET,
				url,
				routeParams,
				queryParams,
				null).asStringAsync());
	}

	@Override
	public IResultProcessor zGet(String url, Map<String, String> routeParams, Map<String, Object> queryParams, Map<String, Collection<?>> queryParamsCollection) throws Exception {
		return new ResultProcessor(prepareHttpRequest(GET,
				url,
				routeParams,
				queryParams,
				queryParamsCollection).asStringAsync());
	}

	//region TRANSACTION

	//region INSERT

	@Override
	public <T> T zinsert(String eid, T item, Type typeOfT) throws Exception
	{
		List<T> payload = new ArrayList<>();
		payload.add(item);

		List<T> result = zinsert(eid, payload, typeOfT);
		if (result != null && !result.isEmpty())
		{
			return result.get(0);
		}
		return null;
	}

	@Override
	public <T> Map<String, Object> zinsert(String eid, T item) throws Exception
	{
		List<T> payload = new ArrayList<>();
		payload.add(item);

		List<Map<String, Object>> result = zinsert(eid, payload);
		if (result != null && !result.isEmpty())
		{
			return result.get(0);
		}
		return null;
	}

	@Override
	public <T> List<T> zinsert(String eid, List<T> item, Type typeOfT) throws Exception
	{
		Map<String, String> routeParams = new HashMap<>();
		routeParams.put("eid", eid);
		return zPost("/zinsert/{eid}", routeParams, item).parseToList(typeOfT);
	}

	@Override
	public <T> List<Map<String, Object>> zinsert(String eid, List<T> item) throws Exception
	{
		Map<String, String> routeParams = new HashMap<>();
		routeParams.put("eid", eid);
		return zPost("/zinsert/{eid}", routeParams, item).parseToListMap();
	}

	//endregion

	//region UPDATE

	@Override
	public <T> T zupdate(String eid, Map<String, Object> queryParams, T item, Type typeOfT) throws Exception
	{
		if (queryParams != null)
		{
			if (queryParams.containsKey("_kb") && ((boolean) queryParams.getOrDefault("_kb", false)))
			{
				List<T> payload = new ArrayList<>();
				payload.add(item);

				List<T> result = zupdate(eid, queryParams, payload, typeOfT);
				if (result != null && !result.isEmpty())
				{
					return result.get(0);
				}
			}
		}
		Map<String, String> routeParams = new HashMap<>();
		routeParams.put("eid", eid);
		return zPut("/zupdate/{eid}", routeParams, queryParams, item).parse(typeOfT);
	}

	@Override
	public <T> Map<String, Object> zupdate(String eid, Map<String, Object> queryParams, T item) throws Exception
	{
		if (queryParams != null)
		{
			if (queryParams.containsKey("_kb") && ((boolean) queryParams.getOrDefault("_kb", false)))
			{
				List<T> payload = new ArrayList<>();
				payload.add(item);

				List<Map<String, Object>> result = zupdate(eid, queryParams, payload);
				if (result != null && !result.isEmpty())
				{
					return result.get(0);
				}
			}
		}

		Map<String, String> routeParams = new HashMap<>();
		routeParams.put("eid", eid);
		return zPut("/zupdate/{eid}", routeParams, queryParams, item).parseToMap();
	}

	@Override
	public <T> List<T> zupdate(String eid, Map<String, Object> queryParams, List<T> item, Type typeOfT) throws Exception
	{
		Map<String, String> routeParams = new HashMap<>();
		routeParams.put("eid", eid);
		return zPut("/zupdate/{eid}", routeParams, queryParams, item).parseToList(typeOfT);
	}

	@Override
	public <T> List<Map<String, Object>> zupdate(String eid, Map<String, Object> queryParams, List<T> item) throws Exception
	{
		Map<String, String> routeParams = new HashMap<>();
		routeParams.put("eid", eid);
		return zPut("/zupdate/{eid}", routeParams, queryParams, item).parseToListMap();
	}

	//endregion

	//region DELETE

	@Override
	public void zdelete(String eid, Map<String, Object> queryParams) throws Exception
	{
		Map<String, String> routeParams = new HashMap<>();
		routeParams.put("eid", eid);
		zDelete("/zdelete/{eid}", routeParams, queryParams);
	}

	//endregion

	//endregion

	//region RETRIEVAL

	//region GET SINGLE

	@Override
	public Map<String, Object> zgetOne(String eid, Map<String, Object> queryParams) throws Exception
	{
		Map<String, String> routeParams = new HashMap<>();
		routeParams.put("eid", eid);
		return zGet("/zget/{eid}",routeParams, queryParams).parseToMap();
	}

	@Override
	public <T> T zgetOne(String eid, Map<String, Object> queryParams, Type typeOfT) throws Exception
	{
		Map<String, String> routeParams = new HashMap<>();
		routeParams.put("eid", eid);
		return zGet("/zget/{eid}",routeParams, queryParams).parse(typeOfT);
	}

	@Override
	public <T> T zgetOne(String eid, Map<String, Object> queryParams, IObjectAssembler objectAssembler) throws Exception
	{
		Map<String, String> routeParams = new HashMap<>();
		routeParams.put("eid", eid);
		return zGet("/zget/{eid}",routeParams, queryParams).parse(objectAssembler);
	}

	//endregion

	//region GET LIST

	@Override
	public List<Map<String, Object>> zgetList(String eid, Map<String, Object> queryParams) throws Exception
	{
		Map<String, String> routeParams = new HashMap<>();
		routeParams.put("eid", eid);
		return zGet("/zget/{eid}", routeParams, queryParams).parseToListMap();
	}

	@Override
	public List<Map<String, Object>> zgetList(String eid, Map<String, Object> queryParams, Pagination pagination) throws Exception
	{
		Map<String, String> routeParams = new HashMap<>();
		routeParams.put("eid", eid);
		return zGet("/zget/{eid}", routeParams, queryParams).parseToListMap(pagination);
	}

	@Override
	public <T> List<T> zgetList(String eid, Map<String, Object> queryParams, Type typeOfT) throws Exception
	{
		Map<String, String> routeParams = new HashMap<>();
		routeParams.put("eid", eid);
		return zGet("/zget/{eid}", routeParams, queryParams).parseToList(typeOfT);
	}

	@Override
	public <T> List<T> zgetList(String eid, Map<String, Object> queryParams, Pagination pagination, Type typeOfT) throws Exception
	{
		Map<String, String> routeParams = new HashMap<>();
		routeParams.put("eid", eid);
		return zGet("/zget/{eid}", routeParams, queryParams).parseToList(typeOfT, pagination);
	}

	@Override
	public <T> List<T> zgetList(String eid, Map<String, Object> queryParams, IObjectAssembler objectAssembler) throws Exception
	{
		Map<String, String> routeParams = new HashMap<>();
		routeParams.put("eid", eid);
		return zGet("/zget/{eid}", routeParams, queryParams).parseToList(objectAssembler);
	}

	@Override
	public <T> List<T> zgetList(String eid, Map<String, Object> queryParams, Map<String, Collection<?>> queryParamsCollection, Type typeOfT) throws Exception
	{
		Map<String, String> routeParams = new HashMap<>();
		routeParams.put("eid", eid);
		return zGet("/zget/{eid}", routeParams, queryParams, queryParamsCollection).parseToList(typeOfT);
	}

	@Override
	public <T> List<T> zgetList(String eid, Map<String, Object> queryParams, Map<String, Collection<?>> queryParamsCollection, Pagination pagination, Type typeOfT) throws Exception
	{
		Map<String, String> routeParams = new HashMap<>();
		routeParams.put("eid", eid);
		return zGet("/zget/{eid}", routeParams, queryParams, queryParamsCollection).parseToList(typeOfT, pagination);
	}

	@Override
	public <T> List<T> zgetList(String eid, Map<String, Object> queryParams, Pagination pagination, IObjectAssembler objectAssembler) throws Exception
	{
		Map<String, String> routeParams = new HashMap<>();
		routeParams.put("eid", eid);
		return zGet("/zget/{eid}", routeParams, queryParams, null).parseToList(objectAssembler, pagination);
	}

	@Override
	public <T> List<T> zgetList(String eid, Map<String, Object> queryParams, Map<String, Collection<?>> queryParamsCollection, IObjectAssembler objectAssembler) throws Exception
	{
		Map<String, String> routeParams = new HashMap<>();
		routeParams.put("eid", eid);
		return zGet("/zget/{eid}", routeParams, queryParams, queryParamsCollection).parseToList(objectAssembler);
	}

	@Override
	public List<Map<String, Object>> zgetList(String eid, Map<String, Object> queryParams, Map<String, Collection<?>> queryParamsCollection) throws Exception
	{
		Map<String, String> routeParams = new HashMap<>();
		routeParams.put("eid", eid);
		return zGet("/zget/{eid}", routeParams, queryParams, queryParamsCollection).parseToListMap();
	}

	@Override
	public List<Map<String, Object>> zgetList(String eid, Map<String, Object> queryParams, Map<String, Collection<?>> queryParamsCollection, Pagination pagination) throws Exception
	{
		Map<String, String> routeParams = new HashMap<>();
		routeParams.put("eid", eid);
		return zGet("/zget/{eid}", routeParams, queryParams, queryParamsCollection).parseToListMap(pagination);
	}

	//endregion

	@Override
	public String zgetJSONString(String eid, Map<String, Object> queryParams) throws Exception
	{
		Map<String, String> routeParams = new HashMap<>();
		routeParams.put("eid", eid);
		return zGet("/zget/{eid}", routeParams, queryParams).parseToJSONString();
	}

	@Override
	public String zgetJSONString(String eid, Map<String, Object> queryParams, Map<String, Collection<?>> queryParamsCollection) throws Exception
	{
		Map<String, String> routeParams = new HashMap<>();
		routeParams.put("eid", eid);
		return zGet("/zget/{eid}", routeParams, queryParams, queryParamsCollection).parseToJSONString();
	}

	@Override
	public String zgetString(String eid, Map<String, Object> queryParams) throws Exception
	{
		Map<String, String> routeParams = new HashMap<>();
		routeParams.put("eid", eid);
		return zGet("/zget/{eid}", routeParams, queryParams, null).parseToJsonPrimitive().getAsString();
	}

	@Override
	public long zgetInteger(String eid, Map<String, Object> queryParams) throws Exception
	{
		Map<String, String> routeParams = new HashMap<>();
		routeParams.put("eid", eid);
		return zGet("/zget/{eid}", routeParams, queryParams, null).parseToJsonPrimitive().getAsInt();
	}

	@Override
	public long zgetLong(String eid, Map<String, Object> queryParams) throws Exception
	{
		Map<String, String> routeParams = new HashMap<>();
		routeParams.put("eid", eid);
		return zGet("/zget/{eid}", routeParams, queryParams, null).parseToJsonPrimitive().getAsLong();
	}

	@Override
	public double zgetDouble(String eid, Map<String, Object> queryParams) throws Exception
	{
		Map<String, String> routeParams = new HashMap<>();
		routeParams.put("eid", eid);
		return zGet("/zget/{eid}", routeParams, queryParams, null).parseToJsonPrimitive().getAsDouble();
	}

	@Override
	public String zexecuteJSON(String eid, Map<String, Object> queryParams) throws Exception {
		Map<String, String> routeParams = new HashMap<>();
		routeParams.put("eid", eid);
		return zGet("/zget/{eid}", routeParams, queryParams, null).parseToJSONString();
	}


	//endregion

	//region GETTER/SETTER

	public String getBaseURL()
	{
		/*if (baseURL != null)
		{
			if (!baseURL.endsWith("/"))
				return baseURL + "/";
		}*/
		return baseURL;
	}

	public void setBaseURL(String baseUrl)
	{
		this.baseURL = baseUrl;
	}

	public boolean isKeyRequired()
	{
		return isKeyRequired;
	}

	public void setKeyRequired(boolean keyRequired)
	{
		isKeyRequired = keyRequired;
	}

	public String getApiKey()
	{
		return apiKey;
	}

	public void setApiKey(String apiKey)
	{
		this.apiKey = apiKey;
	}

	public static <T> T get(List<T> result, int pos)
	{
		if (result != null && !result.isEmpty())
		{
			return result.get(pos);
		}
		return null;
	}

	public Map<String, String> apiKeyCheckpoint() throws Exception
	{
		Map<String, String> headers = new HashMap<>();
		if (isKeyRequired)
		{
			if (this.apiKey == null || this.apiKey.trim().isEmpty())
			{
				throw new Exception("API Key is null or empty");
			}
			headers.put("Authorization", this.apiKey);
		}
		return headers;
	}

	//endregion
}
