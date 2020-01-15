package com.droidlogix.dbflare.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequest;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Future;

/**
 * @author John Pili
 * @since 2016-11-15
 */
public class DbFlareClient implements IDbFlareClient, IResultProcessor
{
	private ObjectMapper objectMapper;
	private String baseUrl;
	private boolean isKeyRequired;
	private String apiKey;
	private Map<String, String> httpMethodMapping;

	//region BUILDER

	public static class Config
	{
		private ObjectMapper objectMapper;
		private String baseUrl;
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
			this.baseUrl = baseUrl;
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

		public Config httpMethodMapping(Map<String, String> httpMethodMapping)
		{
			if (httpMethodMapping != null && !httpMethodMapping.isEmpty())
			{
				this.httpMethodMapping = new HashMap<>();
				this.httpMethodMapping.put(HTTP_METHOD_POST, httpMethodMapping.getOrDefault(HTTP_METHOD_POST, "post")); // Override HTTP Method Mapping
				this.httpMethodMapping.put(HTTP_METHOD_PUT, httpMethodMapping.getOrDefault(HTTP_METHOD_PUT, "put")); // Override HTTP Method Mapping
				this.httpMethodMapping.put(HTTP_METHOD_DELETE, httpMethodMapping.getOrDefault(HTTP_METHOD_DELETE, "delete")); // Override HTTP Method Mapping
				this.httpMethodMapping.put(HTTP_METHOD_GET, httpMethodMapping.getOrDefault(HTTP_METHOD_GET, "get")); // Override HTTP Method Mapping
			}
			else
			{
				this.httpMethodMapping = null;
			}
			return this;
		}

		public DbFlareClient build()
		{
			if (this.httpMethodMapping == null)
			{
				return new DbFlareClient(this.objectMapper, this.baseUrl, this.isKeyRequired, this.apiKey);
			}
			else
			{
				return new DbFlareClient(this.objectMapper, this.baseUrl, this.isKeyRequired, this.apiKey, this.httpMethodMapping);
			}
		}
	}

	//endregion

	//region CONSTRUCTOR

	private DbFlareClient(ObjectMapper objectMapper, String baseUrl, boolean isKeyRequired, String apiKey)
	{
		this.objectMapper = objectMapper;
		this.baseUrl = baseUrl;
		this.isKeyRequired = isKeyRequired;
		this.apiKey = apiKey;
		this.httpMethodMapping = new HashMap<>();
		this.httpMethodMapping.put(HTTP_METHOD_POST, "post"); // Default Mapping
		this.httpMethodMapping.put(HTTP_METHOD_PUT, "put"); // Default Mapping
		this.httpMethodMapping.put(HTTP_METHOD_DELETE, "delete"); // Default Mapping
		this.httpMethodMapping.put(HTTP_METHOD_GET, "get"); // Default Mapping
	}

	private DbFlareClient(ObjectMapper objectMapper, String baseUrl, boolean isKeyRequired, String apiKey, Map<String, String> httpMethodMapping)
	{
		this(objectMapper, baseUrl, isKeyRequired, apiKey);
		if(httpMethodMapping != null)
		{
			this.httpMethodMapping.put(HTTP_METHOD_POST, httpMethodMapping.getOrDefault(HTTP_METHOD_POST, "post")); // Override HTTP Method Mapping
			this.httpMethodMapping.put(HTTP_METHOD_PUT, httpMethodMapping.getOrDefault(HTTP_METHOD_PUT, "put")); // Override HTTP Method Mapping
			this.httpMethodMapping.put(HTTP_METHOD_DELETE, httpMethodMapping.getOrDefault(HTTP_METHOD_DELETE, "delete")); // Override HTTP Method Mapping
			this.httpMethodMapping.put(HTTP_METHOD_GET, httpMethodMapping.getOrDefault(HTTP_METHOD_GET, "get")); // Override HTTP Method Mapping
		}
	}

	//endregion

	//region TRANSACTION

	//region INSERT

	@Override
	public <T> T zinsert(String eid, Map<String, Object> urlParameters, T item, Type typeOfT) throws Exception
	{
		List<T> payload = new ArrayList<>();
		payload.add(item);

		List<T> result = zinsert(eid, urlParameters, payload, typeOfT);
		if (result != null && !result.isEmpty())
		{
			return result.get(0);
		}
		return null;
	}

	@Override
	public <T> Map<String, Object> zinsert(String eid, Map<String, Object> urlParameters, T item) throws Exception
	{
		List<T> payload = new ArrayList<>();
		payload.add(item);

		List<Map<String, Object>> result = zinsert(eid, urlParameters, payload);
		if (result != null && !result.isEmpty())
		{
			return result.get(0);
		}
		return null;
	}

	@Override
	public <T> List<T> zinsert(String eid, Map<String, Object> urlParameters, List<T> item, Type typeOfT) throws Exception
	{
		Map<String, String> headers = apiKeyCheckpoint();
		headers.put("accept", "application/json;charset=UTF-8");

		Future<HttpResponse<String>> httpResponse = Unirest.post(getBaseUrl() + "zinsert")
				.headers(headers)
				.queryString("eid", eid)
				.queryString(urlParameters)
				.body(objectMapper.writeValueAsString(item))
				.asStringAsync();
		return parseToList(httpResponse, TypeToken.getParameterized(ArrayList.class, typeOfT).getType());
	}

	@Override
	public <T> List<Map<String, Object>> zinsert(String eid, Map<String, Object> urlParameters, List<T> item) throws Exception
	{
		Map<String, String> headers = apiKeyCheckpoint();
		headers.put("accept", "application/json;charset=UTF-8");

		Future<HttpResponse<String>> httpResponse = Unirest.post(getBaseUrl() + "zinsert")
				.headers(headers)
				.queryString("eid", eid)
				.queryString(urlParameters)
				.body(objectMapper.writeValueAsString(item))
				.asStringAsync();
		return parseToListMap(httpResponse);
	}

	//endregion

	//region UPDATE

	@Override
	public <T> T zupdate(String eid, Map<String, Object> urlParameters, T item, Type typeOfT) throws Exception
	{
		if (urlParameters != null)
		{
			if (urlParameters.containsKey("_kb") && ((boolean) urlParameters.getOrDefault("_kb", false)))
			{
				List<T> payload = new ArrayList<>();
				payload.add(item);

				List<T> result = zupdate(eid, urlParameters, payload, typeOfT);
				if (result != null && !result.isEmpty())
				{
					return result.get(0);
				}
			}
		}

		Map<String, String> headers = apiKeyCheckpoint();
		headers.put("accept", "application/json;charset=UTF-8");

		if (this.httpMethodMapping.get(HTTP_METHOD_PUT).equals("put"))
		{
			Future<HttpResponse<String>> httpResponse = Unirest.put(getBaseUrl() + "zupdate")
					.headers(headers)
					.queryString("eid", eid)
					.queryString(urlParameters)
					.body(objectMapper.writeValueAsString(item))
					.asStringAsync();
			return parse(httpResponse, typeOfT);
		}
		else if (this.httpMethodMapping.get(HTTP_METHOD_PUT).equals("post"))
		{
			Future<HttpResponse<String>> httpResponse = Unirest.post(getBaseUrl() + "zupdate")
					.headers(headers)
					.queryString("eid", eid)
					.queryString(urlParameters)
					.body(objectMapper.writeValueAsString(item))
					.asStringAsync();
			return parse(httpResponse, typeOfT);
		}
		else
		{
			throw new Exception("Invalid HTTP METHOD Mapping");
		}
	}

	@Override
	public <T> Map<String, Object> zupdate(String eid, Map<String, Object> urlParameters, T item) throws Exception
	{
		if (urlParameters != null)
		{
			if (urlParameters.containsKey("_kb") && ((boolean) urlParameters.getOrDefault("_kb", false)))
			{
				List<T> payload = new ArrayList<>();
				payload.add(item);

				List<Map<String, Object>> result = zupdate(eid, urlParameters, payload);
				if (result != null && !result.isEmpty())
				{
					return result.get(0);
				}
			}
		}

		Map<String, String> headers = apiKeyCheckpoint();
		headers.put("accept", "application/json;charset=UTF-8");

		if (this.httpMethodMapping.get(HTTP_METHOD_PUT).equals("put"))
		{
			Future<HttpResponse<String>> httpResponse = Unirest.put(getBaseUrl() + "zupdate")
					.headers(headers)
					.queryString("eid", eid)
					.queryString(urlParameters)
					.body(objectMapper.writeValueAsString(item))
					.asStringAsync();
			return parseToMap(httpResponse);
		}
		else if (this.httpMethodMapping.get(HTTP_METHOD_PUT).equals("post"))
		{
			Future<HttpResponse<String>> httpResponse = Unirest.post(getBaseUrl() + "zupdate")
					.headers(headers)
					.queryString("eid", eid)
					.queryString(urlParameters)
					.body(objectMapper.writeValueAsString(item))
					.asStringAsync();
			return parseToMap(httpResponse);
		}
		else
		{
			throw new Exception("Invalid HTTP METHOD Mapping");
		}
	}

	@Override
	public <T> List<T> zupdate(String eid, Map<String, Object> urlParameters, List<T> item, Type typeOfT) throws Exception
	{
		Map<String, String> headers = apiKeyCheckpoint();
		headers.put("accept", "application/json;charset=UTF-8");

		if (this.httpMethodMapping.get(HTTP_METHOD_PUT).equals("put"))
		{
			Future<HttpResponse<String>> httpResponse = Unirest.put(getBaseUrl() + "zupdate")
					.headers(headers)
					.queryString("eid", eid)
					.queryString(urlParameters)
					.body(objectMapper.writeValueAsString(item))
					.asStringAsync();
			return parseToList(httpResponse, TypeToken.getParameterized(ArrayList.class, typeOfT).getType());
		}
		else if (this.httpMethodMapping.get(HTTP_METHOD_PUT).equals("post"))
		{
			Future<HttpResponse<String>> httpResponse = Unirest.post(getBaseUrl() + "zupdate")
					.headers(headers)
					.queryString("eid", eid)
					.queryString(urlParameters)
					.body(objectMapper.writeValueAsString(item))
					.asStringAsync();
			return parseToList(httpResponse, TypeToken.getParameterized(ArrayList.class, typeOfT).getType());
		}
		else
		{
			throw new Exception("Invalid HTTP METHOD Mapping");
		}
	}

	@Override
	public <T> List<Map<String, Object>> zupdate(String eid, Map<String, Object> urlParameters, List<T> item) throws Exception
	{
		Map<String, String> headers = apiKeyCheckpoint();
		headers.put("accept", "application/json;charset=UTF-8");

		if (this.httpMethodMapping.get(HTTP_METHOD_PUT).equals("put"))
		{
			Future<HttpResponse<String>> httpResponse = Unirest.put(getBaseUrl() + "zupdate")
					.headers(headers)
					.queryString("eid", eid)
					.queryString(urlParameters)
					.body(objectMapper.writeValueAsString(item))
					.asStringAsync();
			return parseToListMap(httpResponse);
		}
		else if (this.httpMethodMapping.get(HTTP_METHOD_PUT).equals("post"))
		{
			Future<HttpResponse<String>> httpResponse = Unirest.post(getBaseUrl() + "zupdate")
					.headers(headers)
					.queryString("eid", eid)
					.queryString(urlParameters)
					.body(objectMapper.writeValueAsString(item))
					.asStringAsync();
			return parseToListMap(httpResponse);
		}
		else
		{
			throw new Exception("Invalid HTTP METHOD Mapping");
		}
	}

	//endregion

	//region DELETE

	@Override
	public <T> List<T> zdelete(String eid, Map<String, Object> urlParameters, Type typeOfT) throws Exception
	{
		Map<String, String> headers = apiKeyCheckpoint();
		headers.put("accept", "application/json;charset=UTF-8");

		if (this.httpMethodMapping.get(HTTP_METHOD_DELETE).equals("delete"))
		{
			Future<HttpResponse<String>> httpResponse = Unirest.delete(getBaseUrl() + "zdelete")
					.headers(headers)
					.queryString("eid", eid)
					.queryString(urlParameters)
					.asStringAsync();
			return parseToList(httpResponse, TypeToken.getParameterized(ArrayList.class, typeOfT).getType());
		}
		else if (this.httpMethodMapping.get(HTTP_METHOD_DELETE).equals("get"))
		{
			Future<HttpResponse<String>> httpResponse = Unirest.get(getBaseUrl() + "zdelete")
					.headers(headers)
					.queryString("eid", eid)
					.queryString(urlParameters)
					.asStringAsync();
			return parseToList(httpResponse, TypeToken.getParameterized(ArrayList.class, typeOfT).getType());
		}
		else
		{
			throw new Exception("Invalid HTTP METHOD Mapping");
		}
	}

	@Override
	public List<Map<String, Object>> zdelete(String eid, Map<String, Object> urlParameters) throws Exception
	{
		Map<String, String> headers = apiKeyCheckpoint();
		headers.put("accept", "application/json;charset=UTF-8");

		if (this.httpMethodMapping.get(HTTP_METHOD_DELETE).equals("delete"))
		{
			Future<HttpResponse<String>> httpResponse = Unirest.delete(getBaseUrl() + "zdelete")
					.headers(headers)
					.queryString("eid", eid)
					.queryString(urlParameters)
					.asStringAsync();
			return parseToListMap(httpResponse);
		}
		else if (this.httpMethodMapping.get(HTTP_METHOD_DELETE).equals("get"))
		{
			Future<HttpResponse<String>> httpResponse = Unirest.get(getBaseUrl() + "zdelete")
					.headers(headers)
					.queryString("eid", eid)
					.queryString(urlParameters)
					.asStringAsync();
			return parseToListMap(httpResponse);
		}
		else
		{
			throw new Exception("Invalid HTTP METHOD Mapping");
		}
	}

	//endregion

	//endregion

	//region RETRIEVAL

	//region GET SINGLE

	@Override
	public Map<String, Object> zgetSingle(String eid, Map<String, Object> urlParameters) throws Exception
	{
		Map<String, String> headers = apiKeyCheckpoint();
		headers.put("accept", "application/json;charset=UTF-8");

		Future<HttpResponse<String>> httpResponse = Unirest.get(getBaseUrl() + "zget")
				.headers(headers)
				.queryString("eid", eid)
				.queryString(urlParameters)
				.asStringAsync();
		return parseToMap(httpResponse);
	}

	@Override
	public <T> T zgetSingle(String eid, Map<String, Object> urlParameters, Type typeOfT) throws Exception
	{
		Map<String, String> headers = apiKeyCheckpoint();
		headers.put("accept", "application/json;charset=UTF-8");

		Future<HttpResponse<String>> httpResponse = Unirest.get(getBaseUrl() + "zget")
				.headers(headers)
				.queryString("eid", eid)
				.queryString(urlParameters)
				.asStringAsync();
		return parse(httpResponse, typeOfT);
	}

	@Override
	public <T> T zgetSingle(String eid, Map<String, Object> urlParameters, IObjectAssembler objectAssembler) throws Exception
	{
		Map<String, String> headers = apiKeyCheckpoint();
		headers.put("accept", "application/json;charset=UTF-8");

		Future<HttpResponse<String>> httpResponse = Unirest.get(getBaseUrl() + "zget")
				.headers(headers)
				.queryString("eid", eid)
				.queryString(urlParameters)
				.asStringAsync();
		return parse(httpResponse, objectAssembler);
	}

	//endregion

	//region GET LIST

	@Override
	public List<Map<String, Object>> zgetList(String eid, Map<String, Object> urlParameters) throws Exception
	{
		Map<String, String> headers = apiKeyCheckpoint();
		headers.put("accept", "application/json;charset=UTF-8");

		return parseToListMap(Unirest.get(getBaseUrl() + "zget")
				.headers(headers)
				.queryString("eid", eid)
				.queryString(urlParameters)
				.asStringAsync());
	}

	@Override
	public <T> List<T> zgetList(String eid, Map<String, Object> urlParameters, Type typeOfT) throws Exception
	{
		Map<String, String> headers = apiKeyCheckpoint();
		headers.put("accept", "application/json;charset=UTF-8");

		return parseToList(Unirest.get(getBaseUrl() + "zget")
				.headers(headers)
				.queryString("eid", eid)
				.queryString(urlParameters)
				.asStringAsync(), TypeToken.getParameterized(ArrayList.class, typeOfT).getType());
	}

	@Override
	public <T> List<T> zgetList(String eid, Map<String, Object> urlParameters, IObjectAssembler objectAssembler) throws Exception
	{
		Map<String, String> headers = apiKeyCheckpoint();
		headers.put("accept", "application/json;charset=UTF-8");

		return parseToList(Unirest.get(getBaseUrl() + "zget")
				.headers(headers)
				.queryString("eid", eid)
				.queryString(urlParameters)
				.asStringAsync(), objectAssembler);
	}

	@Override
	public <T> List<T> zgetList(String eid, Map<String, Object> urlParameters, PagingInformation pagingInformation, Type typeOfT) throws Exception
	{
		Map<String, String> headers = apiKeyCheckpoint();
		headers.put("accept", "application/json;charset=UTF-8");

		if (pagingInformation == null)
		{
			return parseToList(Unirest.get(getBaseUrl() + "zget")
					.headers(headers)
					.queryString("eid", eid)
					.queryString(urlParameters)
					.asStringAsync(), TypeToken.getParameterized(ArrayList.class, typeOfT).getType());
		}
		else
		{
			return parseToList(Unirest.get(getBaseUrl() + "zget")
					.headers(headers)
					.queryString("eid", eid)
					.queryString(urlParameters)
					.asStringAsync(), pagingInformation, TypeToken.getParameterized(ArrayList.class, typeOfT).getType());
		}
	}

	@Override
	public <T> List<T> zgetList(String eid, Map<String, Object> urlParameters, Map<String, Collection<?>> urlParameters2, Type typeOfT) throws Exception
	{
		Map<String, String> headers = apiKeyCheckpoint();
		headers.put("accept", "application/json;charset=UTF-8");

		HttpRequest request = Unirest.get(getBaseUrl() + "zget")
				.headers(headers)
				.queryString("eid", eid)
				.queryString(urlParameters);
		for (Map.Entry<String, Collection<?>> item : urlParameters2.entrySet())
		{
			request.queryString(item.getKey(), urlParameters2.get(item.getKey()));
		}
		return parseToList(request.asStringAsync(), TypeToken.getParameterized(ArrayList.class, typeOfT).getType());
	}

	@Override
	public <T> List<T> zgetList(String eid, Map<String, Object> urlParameters, Map<String, Collection<?>> urlParameters2, PagingInformation pagingInformation, Type typeOfT) throws Exception
	{
		Map<String, String> headers = apiKeyCheckpoint();
		headers.put("accept", "application/json;charset=UTF-8");

		HttpRequest request = Unirest.get(getBaseUrl() + "zget")
				.headers(headers)
				.queryString("eid", eid)
				.queryString(urlParameters);
		for (Map.Entry<String, Collection<?>> item : urlParameters2.entrySet())
		{
			request.queryString(item.getKey(), urlParameters2.get(item.getKey()));
		}

		if (pagingInformation == null)
		{
			return parseToList(request.asStringAsync(), TypeToken.getParameterized(ArrayList.class, typeOfT).getType());
		}
		else
		{
			return parseToList(request.asStringAsync(), pagingInformation, TypeToken.getParameterized(ArrayList.class, typeOfT).getType());
		}
	}

	@Override
	public <T> List<T> zgetList(String eid, Map<String, Object> urlParameters, PagingInformation pagingInformation, IObjectAssembler objectAssembler) throws Exception
	{
		Map<String, String> headers = apiKeyCheckpoint();
		headers.put("accept", "application/json;charset=UTF-8");

		HttpRequest request = Unirest.get(getBaseUrl() + "zget")
				.headers(headers)
				.queryString("eid", eid)
				.queryString(urlParameters);

		if (pagingInformation == null)
		{
			return parseToList(request.asStringAsync(), objectAssembler);
		}
		else
		{
			return parseToList(request.asStringAsync(), pagingInformation, objectAssembler);
		}
	}

	@Override
	public <T> List<T> zgetList(String eid, Map<String, Object> urlParameters, Map<String, Collection<?>> urlParameters2, IObjectAssembler objectAssembler) throws Exception
	{
		Map<String, String> headers = apiKeyCheckpoint();
		headers.put("accept", "application/json;charset=UTF-8");

		HttpRequest request = Unirest.get(getBaseUrl() + "zget")
				.headers(headers)
				.queryString("eid", eid)
				.queryString(urlParameters);
		for (Map.Entry<String, Collection<?>> item : urlParameters2.entrySet())
		{
			request.queryString(item.getKey(), urlParameters2.get(item.getKey()));
		}
		return parseToList(request.asStringAsync(), objectAssembler);
	}

	//endregion

	@Override
	public String zgetJSON(String eid, Map<String, Object> urlParameters) throws Exception
	{
		Map<String, String> headers = apiKeyCheckpoint();
		headers.put("accept", "application/json;charset=UTF-8");

		return parseToJSONString(Unirest.get(getBaseUrl() + "zget")
				.headers(headers)
				.queryString("eid", eid)
				.queryString(urlParameters)
				.asStringAsync());
	}

	@Override
	public String zgetJSON(String eid, Map<String, Object> urlParameters, Map<String, Collection<?>> urlParameters2) throws Exception
	{
		Map<String, String> headers = apiKeyCheckpoint();
		headers.put("accept", "application/json;charset=UTF-8");

		HttpRequest request = Unirest.get(getBaseUrl() + "zget")
				.headers(headers)
				.queryString("eid", eid)
				.queryString(urlParameters);
		for (Map.Entry<String, Collection<?>> item : urlParameters2.entrySet())
		{
			request.queryString(item.getKey(), urlParameters2.get(item.getKey()));
		}
		return parseToJSONString(request.asStringAsync());
	}

	@Override
	public String zgetString(String eid, Map<String, Object> urlParameters) throws Exception
	{
		Map<String, String> headers = apiKeyCheckpoint();
		headers.put("accept", "application/json;charset=UTF-8");

		JsonPrimitive jsonPrimitive = parseToJsonPrimitive(Unirest.get(getBaseUrl() + "zget")
				.headers(headers)
				.queryString("eid", eid)
				.queryString(urlParameters)
				.asStringAsync());
		return jsonPrimitive.getAsString();
	}

	@Override
	public long zgetInteger(String eid, Map<String, Object> urlParameters) throws Exception
	{
		Map<String, String> headers = apiKeyCheckpoint();
		headers.put("accept", "application/json;charset=UTF-8");

		JsonPrimitive jsonPrimitive = parseToJsonPrimitive(Unirest.get(getBaseUrl() + "zget")
				.headers(headers)
				.queryString("eid", eid)
				.queryString(urlParameters)
				.asStringAsync());
		return jsonPrimitive.getAsInt();
	}

	@Override
	public long zgetLong(String eid, Map<String, Object> urlParameters) throws Exception
	{
		Map<String, String> headers = apiKeyCheckpoint();
		headers.put("accept", "application/json;charset=UTF-8");

		JsonPrimitive jsonPrimitive = parseToJsonPrimitive(Unirest.get(getBaseUrl() + "zget")
				.headers(headers)
				.queryString("eid", eid)
				.queryString(urlParameters)
				.asStringAsync());
		return jsonPrimitive.getAsLong();
	}

	@Override
	public double zgetDouble(String eid, Map<String, Object> urlParameters) throws Exception
	{
		Map<String, String> headers = apiKeyCheckpoint();
		headers.put("accept", "application/json;charset=UTF-8");

		JsonPrimitive jsonPrimitive = parseToJsonPrimitive(Unirest.get(getBaseUrl() + "zget")
				.headers(headers)
				.queryString("eid", eid)
				.queryString(urlParameters)
				.asStringAsync());
		return jsonPrimitive.getAsDouble();
	}

	//endregion

	//region RESULT PROCESSOR

	//region PARSE OBJECT

	@Override
	public <T> T parse(Future<HttpResponse<String>> httpResponse, Type typeOfT) throws Exception
	{
		Gson gson = getGsonWithSerializerDeserializer();

		JsonElement rootJsonElement = getRootElement(httpResponse.get()); // This will extract the root JSON Element
		if (rootJsonElement == null || rootJsonElement.isJsonNull())
		{
			return null;
		}

		if (rootJsonElement.isJsonPrimitive())
		{
			throw new Exception("Cannot convert JSON Primitive to Object of T");
		}

		if (rootJsonElement.isJsonArray())
		{
			throw new Exception("Cannot convert JSON Array to Object of T");
		}

		if (rootJsonElement.isJsonObject())
		{
			bubbleAnyDbFlareErrorMessages(rootJsonElement);
			JsonObject root = rootJsonElement.getAsJsonObject();
			if (root == null || root.isJsonNull())
			{
				return null;
			}

			if (root.has("data"))
			{
				JsonObject data = root.getAsJsonObject("data");
				return gson.fromJson(data, typeOfT);
			}
			else
			{
				return gson.fromJson(root, typeOfT);
			}
		}
		return null;
	}

	@Override
	public <T> T parse(Future<HttpResponse<String>> httpResponse, IObjectAssembler objectAssembler) throws Exception
	{
		JsonElement rootJsonElement = getRootElement(httpResponse.get()); // This will extract the root JSON Element
		if (rootJsonElement == null || rootJsonElement.isJsonNull())
		{
			return null;
		}

		if (rootJsonElement.isJsonPrimitive())
		{
			throw new Exception("Cannot convert JSON Primitive to Object of T");
		}

		if (rootJsonElement.isJsonArray())
		{
			throw new Exception("Cannot convert JSON Array to Object of T");
		}

		if (rootJsonElement.isJsonObject())
		{
			bubbleAnyDbFlareErrorMessages(rootJsonElement);
			JsonObject root = rootJsonElement.getAsJsonObject();
			if (root == null || root.isJsonNull())
			{
				return null;
			}

			if (root.has("data"))
			{
				JsonObject data = root.getAsJsonObject("data");
				return objectAssembler.assemble(data);
			}
			else
			{
				return objectAssembler.assemble(root);
			}
		}
		return null;
	}

	@Override
	public Map<String, Object> parseToMap(Future<HttpResponse<String>> httpResponse) throws Exception
	{
		Gson gson = getGsonWithSerializerDeserializer();

		JsonElement rootJsonElement = getRootElement(httpResponse.get()); // This will extract the root JSON Element
		if (rootJsonElement == null || rootJsonElement.isJsonNull())
		{
			return null;
		}

		if (rootJsonElement.isJsonPrimitive())
		{
			throw new Exception("Cannot convert JSON Primitive to Map<String, Object>");
		}

		if (rootJsonElement.isJsonArray())
		{
			throw new Exception("Cannot convert JSON Array to Map<String, Object>");
		}

		if (rootJsonElement.isJsonObject())
		{
			bubbleAnyDbFlareErrorMessages(rootJsonElement);
			JsonObject root = rootJsonElement.getAsJsonObject();
			if (root == null || root.isJsonNull())
			{
				return null;
			}

			if (root.has("data"))
			{
				JsonObject data = root.getAsJsonObject("data");
				return gson.fromJson(data, new TypeToken<Map<String, Object>>()
				{
				}.getType());
			}
			else
			{
				return gson.fromJson(root, new TypeToken<Map<String, Object>>()
				{
				}.getType());
			}
		}
		return null;
	}

	//endregion

	//region PARSE TO LIST

	@Override
	public <T> List<T> parseToList(Future<HttpResponse<String>> httpResponse, Type typeOfT) throws Exception
	{
		Gson gson = getGsonWithSerializerDeserializer();

		JsonElement rootJsonElement = getRootElement(httpResponse.get()); // This will extract the root JSON Element
		if (rootJsonElement == null || rootJsonElement.isJsonNull())
		{
			return new ArrayList<>();
		}

		if (rootJsonElement.isJsonPrimitive())
		{
			throw new Exception("Cannot convert JSON Primitive to List<T>");
		}

		if (rootJsonElement.isJsonArray())
		{
			JsonArray root = rootJsonElement.getAsJsonArray();
			return gson.fromJson(root, typeOfT);
		}
		else if (rootJsonElement.isJsonObject())
		{
			bubbleAnyDbFlareErrorMessages(rootJsonElement);
			JsonObject root = rootJsonElement.getAsJsonObject();
			if (root == null || root.isJsonNull())
			{
				return new ArrayList<>();
			}

			if (root.has("data"))
			{
				if(root.get("data").isJsonArray())
				{
					JsonArray data = root.getAsJsonArray("data");
					return gson.fromJson(data, typeOfT);
				}
			}
		}
		return new ArrayList<>();
	}

	@Override
	public <T> List<T> parseToList(Future<HttpResponse<String>> httpResponse, IObjectAssembler objectAssembler) throws Exception
	{
		JsonElement rootJsonElement = getRootElement(httpResponse.get()); // This will extract the root JSON Element
		if (rootJsonElement == null || rootJsonElement.isJsonNull())
		{
			return new ArrayList<>();
		}

		if (rootJsonElement.isJsonPrimitive())
		{
			throw new Exception("Cannot convert JSON Primitive to List<T>");
		}

		if (rootJsonElement.isJsonArray())
		{
			JsonArray data = rootJsonElement.getAsJsonArray();
			List<T> tmpList = new ArrayList<>();
			while (data.iterator().hasNext())
			{
				tmpList.add(objectAssembler.assemble(data.iterator().next()));
			}
			return tmpList;
		}
		else if (rootJsonElement.isJsonObject())
		{
			bubbleAnyDbFlareErrorMessages(rootJsonElement);
			JsonObject root = rootJsonElement.getAsJsonObject();
			if (root == null || root.isJsonNull())
			{
				return new ArrayList<>();
			}

			if (root.has("data"))
			{
				if(root.get("data").isJsonArray())
				{
					JsonArray data = root.getAsJsonArray("data");
					List<T> tmpList = new ArrayList<>();
					while (data.iterator().hasNext())
					{
						tmpList.add(objectAssembler.assemble(data.iterator().next()));
					}
					return tmpList;
				}
			}
		}
		return new ArrayList<>();
	}

	@Override
	public <T> List<T> parseToList(Future<HttpResponse<String>> httpResponse, PagingInformation pagingInformation, Type typeOfT) throws Exception
	{
		Gson gson = getGsonWithSerializerDeserializer();

		JsonElement rootJsonElement = getRootElement(httpResponse.get()); // This will extract the root JSON Element
		if (rootJsonElement == null || rootJsonElement.isJsonNull())
		{
			pagingInformation.setTotal(0);
			return new ArrayList<>();
		}

		if (rootJsonElement.isJsonPrimitive())
		{
			throw new Exception("Cannot convert JSON Primitive to List<T>");
		}

		if (rootJsonElement.isJsonArray())
		{
			JsonArray root = rootJsonElement.getAsJsonArray();
			if (root != null && !root.isJsonNull())
			{
				pagingInformation.setTotal(root.size());
				return gson.fromJson(root, typeOfT);
			}
		}
		else if (rootJsonElement.isJsonObject())
		{
			bubbleAnyDbFlareErrorMessages(rootJsonElement);
			JsonObject root = rootJsonElement.getAsJsonObject();
			if (root == null || root.isJsonNull())
			{
				pagingInformation.setTotal(0);
				return new ArrayList<>();
			}

			if (root.has("data"))
			{
				if(root.get("data").isJsonArray())
				{
					JsonArray data = root.getAsJsonArray("data");
					pagingInformation.setTotal(root.getAsJsonPrimitive("total").getAsInt());
					return gson.fromJson(data, typeOfT);
				}
			}
		}
		return new ArrayList<>();
	}

	@Override
	public <T> List<T> parseToList(Future<HttpResponse<String>> httpResponse, PagingInformation pagingInformation, IObjectAssembler objectAssembler) throws Exception
	{
		JsonElement rootJsonElement = getRootElement(httpResponse.get()); // This will extract the root JSON Element
		if (rootJsonElement == null || rootJsonElement.isJsonNull())
		{
			pagingInformation.setTotal(0);
			return new ArrayList<>();
		}

		if (rootJsonElement.isJsonPrimitive())
		{
			throw new Exception("Cannot convert JSON Primitive to List<T>");
		}

		if (rootJsonElement.isJsonArray())
		{
			JsonArray data = rootJsonElement.getAsJsonArray();
			List<T> tmpList = new ArrayList<>();
			while (data.iterator().hasNext())
			{
				tmpList.add(objectAssembler.assemble(data.iterator().next()));
			}
			pagingInformation.setTotal(tmpList.size());
			return tmpList;
		}
		else if (rootJsonElement.isJsonObject())
		{
			bubbleAnyDbFlareErrorMessages(rootJsonElement);
			JsonObject root = rootJsonElement.getAsJsonObject();
			if (root == null || root.isJsonNull())
			{
				pagingInformation.setTotal(0);
				return new ArrayList<>();
			}

			if (root.has("data"))
			{
				if(root.get("data").isJsonArray())
				{
					JsonArray data = root.getAsJsonArray("data");
					pagingInformation.setTotal(root.getAsJsonPrimitive("total").getAsInt());
					List<T> tmpList = new ArrayList<>();
					while (data.iterator().hasNext())
					{
						tmpList.add(objectAssembler.assemble(data.iterator().next()));
					}
					return tmpList;
				}
			}
		}
		pagingInformation.setTotal(0);
		return new ArrayList<>();
	}

	@Override
	public List<Map<String, Object>> parseToListMap(Future<HttpResponse<String>> httpResponse) throws Exception
	{
		Gson gson = getGsonWithSerializerDeserializer();

		JsonElement rootJsonElement = getRootElement(httpResponse.get()); // This will extract the root JSON Element
		if (rootJsonElement == null || rootJsonElement.isJsonNull())
		{
			return new ArrayList<>();
		}

		if (rootJsonElement.isJsonPrimitive())
		{
			throw new Exception("Cannot convert JSON Primitive to List<Map<String, Object>>");
		}

		if (rootJsonElement.isJsonArray())
		{
			JsonArray data = rootJsonElement.getAsJsonArray();
			return gson.fromJson(data, new TypeToken<List<Map<String, Object>>>()
			{
			}.getType());
		}
		else if (rootJsonElement.isJsonObject())
		{
			bubbleAnyDbFlareErrorMessages(rootJsonElement);
			JsonObject root = rootJsonElement.getAsJsonObject();
			if (root == null || root.isJsonNull())
			{
				return new ArrayList<>();
			}

			if (root.has("data"))
			{
				if(root.get("data").isJsonArray())
				{
					JsonArray data = root.getAsJsonArray("data");
					return gson.fromJson(data, new TypeToken<List<Map<String, Object>>>()
					{
					}.getType());
				}
			}
		}
		return new ArrayList<>();
	}

	//endregion

	//region JSON AND PRIMITIVE

	@Override
	public String parseToJSONString(Future<HttpResponse<String>> httpResponse) throws Exception
	{
		JsonElement rootJsonElement = getRootElement(httpResponse.get()); // This will extract the root JSON Element
		if (rootJsonElement == null || rootJsonElement.isJsonNull())
		{
			return null;
		}

		if (rootJsonElement.isJsonArray())
		{
			return rootJsonElement.getAsJsonArray().toString();
		}
		else if (rootJsonElement.isJsonObject())
		{
			return rootJsonElement.getAsJsonObject().toString();
		}
		else if (rootJsonElement.isJsonPrimitive())
		{
			return rootJsonElement.getAsJsonPrimitive().toString();
		}
		return null;
	}

	@Override
	public JsonPrimitive parseToJsonPrimitive(Future<HttpResponse<String>> httpResponse) throws Exception
	{
		JsonElement rootJsonElement = getRootElement(httpResponse.get()); // This will extract the root JSON Element
		if (rootJsonElement == null || rootJsonElement.isJsonNull())
		{
			return null;
		}

		if (rootJsonElement.isJsonArray())
		{
			throw new Exception("Cannot convert JSON Array to JsonPrimitive");
		}

		if (rootJsonElement.isJsonPrimitive())
		{
			return rootJsonElement.getAsJsonPrimitive();
		}
		else if (rootJsonElement.isJsonObject())
		{
			bubbleAnyDbFlareErrorMessages(rootJsonElement);
			JsonObject root = rootJsonElement.getAsJsonObject();
			if (root == null || root.isJsonNull())
			{
				return null;
			}

			if (root.has("data"))
			{
				return root.getAsJsonObject("data").getAsJsonPrimitive();
			}
		}
		return null;
	}

	//endregion

	//endregion

	private Gson getGsonWithSerializerDeserializer()
	{
		GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping();
		gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>()
		{
			public Date deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
			{
				String dateString = json.getAsString();
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSZ");
				try
				{
					if (StringUtils.isBlank(dateString))
					{
						return null;
					}
					else
					{
						return df.parse(dateString);
					}
				}
				catch (ParseException parseException)
				{
					System.out.println(parseException.getMessage());
					return null;
				}
			}
		}).registerTypeHierarchyAdapter(byte[].class, new ByteArrayToBase64TypeAdapter());
		return gsonBuilder.create();
	}

	private JsonElement getRootElement(HttpResponse<String> response) throws Exception
	{
		if (response == null)
		{
			throw new NullPointerException("response is null");
		}

		if (response.getStatus() >= 200 && response.getStatus() <= 299)
		{
			Gson gson = getGsonWithSerializerDeserializer();
			return gson.fromJson(response.getBody(), JsonElement.class);
		}
		else if(response.getStatus() == 401)
		{
			throw new Exception("Unauthorized");
		}
		StringBuilder sb = new StringBuilder("Invalid HTTP Response Code\n");
		sb.append(response.getStatusText());
		sb.append(response.getBody());
		throw new Exception(sb.toString());
	}

	private void bubbleAnyDbFlareErrorMessages(JsonElement rootElement) throws Exception
	{
		if (rootElement != null)
		{
			if (!rootElement.isJsonNull() && rootElement.isJsonObject())
			{
				JsonObject root = rootElement.getAsJsonObject();
				if (root.has("errors"))
				{
					JsonArray errors = root.getAsJsonArray("errors");
					while (errors.iterator().hasNext())
					{
						JsonElement e = errors.iterator().next();
						throw new Exception(e.getAsString());
					}
				}
			}
		}
	}

	//CODE FROM or BASED: https://gist.github.com/orip/3635246
	private static class ByteArrayToBase64TypeAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]>
	{
		public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			return Base64.getDecoder().decode(json.getAsString());
		}

		public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context)
		{
			return new JsonPrimitive(Base64.getEncoder().encodeToString(src));
		}
	}

	public String getBaseUrl()
	{
		if (baseUrl != null)
		{
			if (!baseUrl.endsWith("/"))
				return baseUrl + "/";
		}
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl)
	{
		this.baseUrl = baseUrl;
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
}
