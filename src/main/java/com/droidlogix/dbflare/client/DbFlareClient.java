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
public class DbFlareClient implements IRestfulClient, IResultProcessor
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
			if (httpMethodMapping != null)
			{
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
		this.httpMethodMapping.put(HTTP_METHOD_POST, httpMethodMapping.getOrDefault(HTTP_METHOD_POST, "post")); // Override HTTP Method Mapping
		this.httpMethodMapping.put(HTTP_METHOD_PUT, httpMethodMapping.getOrDefault(HTTP_METHOD_PUT, "put")); // Override HTTP Method Mapping
		this.httpMethodMapping.put(HTTP_METHOD_DELETE, httpMethodMapping.getOrDefault(HTTP_METHOD_DELETE, "delete")); // Override HTTP Method Mapping
		this.httpMethodMapping.put(HTTP_METHOD_GET, httpMethodMapping.getOrDefault(HTTP_METHOD_GET, "get")); // Override HTTP Method Mapping
	}

	//endregion

	//region TRANSACTION


	@Override
	public <T> T zinsert(String eid, Map<String, Object> urlParameters, T item, Type typeOfT) throws Exception
	{
		List<T> payload = new ArrayList<>();
		payload.add(item);

		List<T> result = zinsert(eid, urlParameters, payload, TypeToken.getParameterized(ArrayList.class, typeOfT).getType());
		if (result != null && !result.isEmpty())
		{
			return result.get(0);
		}
		return null;
	}

	@Override
	public <T> List<T> zinsert(String eid, Map<String, Object> urlParameters, List<T> item, Type typeOfT) throws Exception
	{
		Map<String, String> headers = new HashMap<>();
		if (isKeyRequired)
		{
			headers.put("Authorization", this.apiKey);
		}
		headers.put("accept", "application/json;charset=UTF-8");

		//ObjectMapper objectMapper = new ObjectMapper().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		Future<HttpResponse<String>> httpResponse = Unirest.post(getBaseUrl() + "zinsert")
				.headers(headers)
				.queryString("eid", eid)
				.queryString(urlParameters)
				.body(objectMapper.writeValueAsString(item))
				.asStringAsync();
		return processListResult(httpResponse, typeOfT);
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
	public <T> List<Map<String, Object>> zinsert(String eid, Map<String, Object> urlParameters, List<T> item) throws Exception
	{
		Map<String, String> headers = new HashMap<>();
		if (isKeyRequired)
		{
			headers.put("Authorization", this.apiKey);
		}
		headers.put("accept", "application/json;charset=UTF-8");

		//ObjectMapper objectMapper = new ObjectMapper().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		Future<HttpResponse<String>> httpResponse = Unirest.post(getBaseUrl() + "zinsert")
				.headers(headers)
				.queryString("eid", eid)
				.queryString(urlParameters)
				.body(objectMapper.writeValueAsString(item))
				.asStringAsync();
		return processListResult(httpResponse, new TypeToken<List<Map<String, Object>>>()
		{
		}.getType());
	}

	@Override
	public <T> T zupdate(String eid, Map<String, Object> urlParameters, T item, Type typeOfT) throws Exception
	{
		Map<String, String> headers = new HashMap<>();
		if (isKeyRequired)
		{
			headers.put("Authorization", this.apiKey);
		}
		headers.put("accept", "application/json;charset=UTF-8");

		if (this.httpMethodMapping.get(HTTP_METHOD_PUT).equals("put"))
		{
			//ObjectMapper objectMapper = new ObjectMapper().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
			Future<HttpResponse<String>> httpResponse = Unirest.put(getBaseUrl() + "zupdate")
					.headers(headers)
					.queryString("eid", eid)
					.queryString(urlParameters)
					.body(objectMapper.writeValueAsString(item))
					.asStringAsync();
			return processObjectResult(httpResponse, typeOfT);
		}
		else if (this.httpMethodMapping.get(HTTP_METHOD_PUT).equals("post"))
		{
			//ObjectMapper objectMapper = new ObjectMapper().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
			Future<HttpResponse<String>> httpResponse = Unirest.post(getBaseUrl() + "zupdate")
					.headers(headers)
					.queryString("eid", eid)
					.queryString(urlParameters)
					.body(objectMapper.writeValueAsString(item))
					.asStringAsync();
			return processObjectResult(httpResponse, typeOfT);
		}
		else
		{
			throw new Exception("Invalid HTTP METHOD Mapping");
		}
	}

	@Override
	public <T> Map<String, Object> zupdate(String eid, Map<String, Object> urlParameters, T item) throws Exception
	{
		Map<String, String> headers = new HashMap<>();
		if (isKeyRequired)
		{
			headers.put("Authorization", this.apiKey);
		}
		headers.put("accept", "application/json;charset=UTF-8");

		if (this.httpMethodMapping.get(HTTP_METHOD_PUT).equals("put"))
		{
			//ObjectMapper objectMapper = new ObjectMapper().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
			Future<HttpResponse<String>> httpResponse = Unirest.put(getBaseUrl() + "zupdate")
					.headers(headers)
					.queryString("eid", eid)
					.queryString(urlParameters)
					.body(objectMapper.writeValueAsString(item))
					.asStringAsync();
			return processObjectResult(httpResponse);
		}
		else if (this.httpMethodMapping.get(HTTP_METHOD_PUT).equals("post"))
		{
			//ObjectMapper objectMapper = new ObjectMapper().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
			Future<HttpResponse<String>> httpResponse = Unirest.post(getBaseUrl() + "zupdate")
					.headers(headers)
					.queryString("eid", eid)
					.queryString(urlParameters)
					.body(objectMapper.writeValueAsString(item))
					.asStringAsync();
			return processObjectResult(httpResponse);
		}
		else
		{
			throw new Exception("Invalid HTTP METHOD Mapping");
		}
	}

	@Override
	public <T> List<T> zdelete(String eid, Map<String, Object> urlParameters, Type typeOfT) throws Exception
	{
		Map<String, String> headers = new HashMap<>();
		if (isKeyRequired)
		{
			headers.put("Authorization", this.apiKey);
		}
		headers.put("accept", "application/json;charset=UTF-8");

		if (this.httpMethodMapping.get(HTTP_METHOD_DELETE).equals("delete"))
		{
			Future<HttpResponse<String>> httpResponse = Unirest.delete(getBaseUrl() + "zdelete")
					.headers(headers)
					.queryString("eid", eid)
					.queryString(urlParameters)
					.asStringAsync();
			return processListResult(httpResponse, TypeToken.getParameterized(ArrayList.class, typeOfT).getType());
		}
		else if (this.httpMethodMapping.get(HTTP_METHOD_DELETE).equals("get"))
		{
			Future<HttpResponse<String>> httpResponse = Unirest.get(getBaseUrl() + "zdelete")
					.headers(headers)
					.queryString("eid", eid)
					.queryString(urlParameters)
					.asStringAsync();
			return processListResult(httpResponse, TypeToken.getParameterized(ArrayList.class, typeOfT).getType());
		}
		else
		{
			throw new Exception("Invalid HTTP METHOD Mapping");
		}
	}

	@Override
	public List<Map<String, Object>> zdelete(String eid, Map<String, Object> urlParameters) throws Exception
	{
		Map<String, String> headers = new HashMap<>();
		if (isKeyRequired)
		{
			headers.put("Authorization", this.apiKey);
		}
		headers.put("accept", "application/json;charset=UTF-8");

		if (this.httpMethodMapping.get(HTTP_METHOD_DELETE).equals("delete"))
		{
			Future<HttpResponse<String>> httpResponse = Unirest.delete(getBaseUrl() + "zdelete")
					.headers(headers)
					.queryString("eid", eid)
					.queryString(urlParameters)
					.asStringAsync();
			return processListResult(httpResponse, new TypeToken<List<Map<String, Object>>>()
			{
			}.getType());
		}
		else if (this.httpMethodMapping.get(HTTP_METHOD_DELETE).equals("get"))
		{
			Future<HttpResponse<String>> httpResponse = Unirest.get(getBaseUrl() + "zdelete")
					.headers(headers)
					.queryString("eid", eid)
					.queryString(urlParameters)
					.asStringAsync();
			return processListResult(httpResponse, new TypeToken<List<Map<String, Object>>>()
			{
			}.getType());
		}
		else
		{
			throw new Exception("Invalid HTTP METHOD Mapping");
		}
	}

	//endregion

	//region RETRIEVAL

	@Override
	public <T> T zgetSingle(String eid, Map<String, Object> urlParameters, Type typeOfT) throws Exception
	{
		Map<String, String> headers = new HashMap<>();
		if (isKeyRequired)
		{
			headers.put("Authorization", this.apiKey);
		}
		headers.put("accept", "application/json;charset=UTF-8");

		Future<HttpResponse<String>> httpResponse = Unirest.get(getBaseUrl() + "zget")
				.headers(headers)
				.queryString("eid", eid)
				.queryString(urlParameters)
				.asStringAsync();
		return processObjectResult(httpResponse, typeOfT);
	}

	@Override
	public <T> T zgetSingle(String eid, Map<String, Object> urlParameters, IObjectAssembler objectAssembler) throws Exception
	{
		return null;
	}

	@Override
	public <T> List<T> zgetList(String eid, Map<String, Object> urlParameters, Type typeOfT) throws Exception
	{
		Map<String, String> headers = new HashMap<>();
		if (isKeyRequired)
		{
			headers.put("Authorization", this.apiKey);
		}
		headers.put("accept", "application/json;charset=UTF-8");

		return processListResult(Unirest.get(getBaseUrl() + "zget")
				.headers(headers)
				.queryString("eid", eid)
				.queryString(urlParameters)
				.asStringAsync(), typeOfT);
	}

	@Override
	public <T> List<T> zgetList(String eid, Map<String, Object> urlParameters, IObjectAssembler objectAssembler) throws Exception
	{
		Map<String, String> headers = new HashMap<>();
		if (isKeyRequired)
		{
			headers.put("Authorization", this.apiKey);
		}
		headers.put("accept", "application/json;charset=UTF-8");

		return processListResult(Unirest.get(getBaseUrl() + "zget")
				.headers(headers)
				.queryString("eid", eid)
				.queryString(urlParameters)
				.asStringAsync(), objectAssembler);
	}

	@Override
	public <T> List<T> zgetList(String eid, Map<String, Object> urlParameters, PagingInformation pagingInformation, Type typeOfT) throws Exception
	{
		Map<String, String> headers = new HashMap<>();
		if (isKeyRequired)
		{
			headers.put("Authorization", this.apiKey);
		}
		headers.put("accept", "application/json;charset=UTF-8");

		if (pagingInformation == null)
		{
			return processListResult(Unirest.get(getBaseUrl() + "zget")
					.headers(headers)
					.queryString("eid", eid)
					.queryString(urlParameters)
					.asStringAsync(), typeOfT);
		}
		else
		{
			return processListResult(Unirest.get(getBaseUrl() + "zget")
					.headers(headers)
					.queryString("eid", eid)
					.queryString(urlParameters)
					.asStringAsync(), pagingInformation, typeOfT);
		}
	}

	@Override
	public <T> List<T> zgetList(String eid, Map<String, Object> urlParameters, Map<String, Collection<?>> urlParameters2, Type typeOfT) throws Exception
	{
		Map<String, String> headers = new HashMap<>();
		if (isKeyRequired)
		{
			headers.put("Authorization", this.apiKey);
		}
		headers.put("accept", "application/json;charset=UTF-8");

		HttpRequest request = Unirest.get(getBaseUrl() + "zget")
				.headers(headers)
				.queryString("eid", eid)
				.queryString(urlParameters);
		for (Map.Entry<String, Collection<?>> item : urlParameters2.entrySet())
		{
			request.queryString(item.getKey(), urlParameters2.get(item.getKey()));
		}
		return processListResult(request.asStringAsync(), typeOfT);
	}

	@Override
	public <T> List<T> zgetList(String eid, Map<String, Object> urlParameters, Map<String, Collection<?>> urlParameters2, PagingInformation pagingInformation, Type typeOfT) throws Exception
	{
		Map<String, String> headers = new HashMap<>();
		if (isKeyRequired)
		{
			headers.put("Authorization", this.apiKey);
		}
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
			return processListResult(request.asStringAsync(), typeOfT);
		}
		else
		{
			return processListResult(request.asStringAsync(), pagingInformation, typeOfT);
		}
	}

	@Override
	public <T> List<T> zgetList(String eid, Map<String, Object> urlParameters, PagingInformation pagingInformation, IObjectAssembler objectAssembler) throws Exception
	{
		throw new Exception("Not implement method");
	}

	@Override
	public <T> List<T> zgetList(String eid, Map<String, Object> urlParameters, Map<String, Collection<?>> urlParameters2, IObjectAssembler objectAssembler) throws Exception
	{
		throw new Exception("Not implement method");
	}

	@Override
	public String zgetJSON(String eid, Map<String, Object> urlParameters) throws Exception
	{
		Map<String, String> headers = new HashMap<>();
		if (isKeyRequired)
		{
			headers.put("Authorization", this.apiKey);
		}
		headers.put("accept", "application/json;charset=UTF-8");
		return processJSONResult(Unirest.get(getBaseUrl() + "zget")
				.headers(headers)
				.queryString("eid", eid)
				.queryString(urlParameters)
				.asStringAsync());
	}

	@Override
	public String zgetJSON(String eid, Map<String, Object> urlParameters, Map<String, Collection<?>> urlParameters2) throws Exception
	{
		Map<String, String> headers = new HashMap<>();
		if (isKeyRequired)
		{
			headers.put("Authorization", this.apiKey);
		}
		headers.put("accept", "application/json;charset=UTF-8");
		HttpRequest request = Unirest.get(getBaseUrl() + "zget")
				.headers(headers)
				.queryString("eid", eid)
				.queryString(urlParameters);
		for (Map.Entry<String, Collection<?>> item : urlParameters2.entrySet())
		{
			request.queryString(item.getKey(), urlParameters2.get(item.getKey()));
		}
		return processJSONResult(request.asStringAsync());
	}

	@Override
	public String zgetString(String eid, Map<String, Object> urlParameters) throws Exception
	{
		Map<String, String> headers = new HashMap<>();
		if (isKeyRequired)
		{
			headers.put("Authorization", this.apiKey);
		}
		headers.put("accept", "application/json;charset=UTF-8");
		JsonPrimitive jsonPrimitive = processJsonPrimitiveResult(Unirest.get(getBaseUrl() + "zget")
				.headers(headers)
				.queryString("eid", eid)
				.queryString(urlParameters)
				.asStringAsync());
		return jsonPrimitive.getAsString();
	}

	@Override
	public long zgetInteger(String eid, Map<String, Object> urlParameters) throws Exception
	{
		Map<String, String> headers = new HashMap<>();
		if (isKeyRequired)
		{
			headers.put("Authorization", this.apiKey);
		}
		headers.put("accept", "application/json;charset=UTF-8");
		JsonPrimitive jsonPrimitive = processJsonPrimitiveResult(Unirest.get(getBaseUrl() + "zget")
				.headers(headers)
				.queryString("eid", eid)
				.queryString(urlParameters)
				.asStringAsync());
		return jsonPrimitive.getAsInt();
	}

	@Override
	public long zgetLong(String eid, Map<String, Object> urlParameters) throws Exception
	{
		Map<String, String> headers = new HashMap<>();
		if (isKeyRequired)
		{
			headers.put("Authorization", this.apiKey);
		}
		headers.put("accept", "application/json;charset=UTF-8");
		JsonPrimitive jsonPrimitive = processJsonPrimitiveResult(Unirest.get(getBaseUrl() + "zget")
				.headers(headers)
				.queryString("eid", eid)
				.queryString(urlParameters)
				.asStringAsync());
		return jsonPrimitive.getAsLong();
	}

	@Override
	public double zgetDouble(String eid, Map<String, Object> urlParameters) throws Exception
	{
		Map<String, String> headers = new HashMap<>();
		if (isKeyRequired)
		{
			headers.put("Authorization", this.apiKey);
		}
		headers.put("accept", "application/json;charset=UTF-8");
		JsonPrimitive jsonPrimitive = processJsonPrimitiveResult(Unirest.get(getBaseUrl() + "zget")
				.headers(headers)
				.queryString("eid", eid)
				.queryString(urlParameters)
				.asStringAsync());
		return jsonPrimitive.getAsDouble();
	}

	//endregion

	//region RESULT PROCESSOR

	@Override
	public <T> T processObjectResult(Future<HttpResponse<String>> httpResponse, Type typeOfT) throws Exception
	{
		HttpResponse<String> result = httpResponse.get();
		if (result.getStatus() >= 200 && result.getStatus() <= 299)
		{
			Gson gson = getGsonWithSerializerDeserializer();
			String jsonString = result.getBody();
			JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);
			if (!jsonString.equalsIgnoreCase("null") && !jsonString.contains("\"data\":null") && jsonElement != null)
			{
				JsonObject resultObject = jsonElement.getAsJsonObject();
				JsonElement dataMember = resultObject.getAsJsonObject("data");
				if (dataMember != null && !dataMember.isJsonNull())
				{
					return gson.fromJson(dataMember, typeOfT);
				}
				else
				{
					return gson.fromJson(resultObject, typeOfT);
				}
			}
			return null;
		}
		throw new Exception("Error processing your request");
	}

	private JsonElement getJsonElement(HttpResponse<String> result) throws Exception
	{
		if (result.getStatus() >= 200 && result.getStatus() <= 299)
		{
			Gson gson = getGsonWithSerializerDeserializer();
			return gson.fromJson(result.getBody(), JsonElement.class);
		}
		throw new Exception("Invalid HTTP Response Code");
	}

	private JsonObject getJsonObject(JsonElement jsonElement) throws Exception
	{
		if (jsonElement == null)
		{
			throw new Exception("Result JsonElement is null");
		}

		if (!jsonElement.isJsonObject())
		{
			throw new Exception("Result is not JsonObject");
		}
		return jsonElement.getAsJsonObject();
	}

	@Override
	public <T> T processObjectResult(Future<HttpResponse<String>> httpResponse, IObjectAssembler objectAssembler) throws Exception
	{
		throw new Exception("Not implemented method");
	}

	@Override
	public Map<String, Object> processObjectResult(Future<HttpResponse<String>> httpResponse) throws Exception
	{
		try
		{
			HttpResponse<String> result = httpResponse.get();
			if (result.getStatus() >= 200 && result.getStatus() <= 299)
			{
				Gson gson = getGsonWithSerializerDeserializer();
				String jsonString = result.getBody();
				JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);
				if (!jsonString.equalsIgnoreCase("null") && !jsonString.contains("\"data\":null") && jsonElement != null)
				{
					JsonObject resultObject = jsonElement.getAsJsonObject();
					JsonElement dataMember = resultObject.getAsJsonObject("data");
					if (dataMember != null && !dataMember.isJsonNull())
					{
						return gson.fromJson(dataMember, new TypeToken<Map<String, Object>>()
						{
						}.getType());
					}
					else
					{
						return gson.fromJson(resultObject, new TypeToken<Map<String, Object>>()
						{
						}.getType());
					}
				}
				return null;
			}
		}
		catch (Exception exception)
		{
			throw exception;
		}
		throw new Exception("Error processing your request");
	}

	@Override
	public <T> List<T> processListResult(Future<HttpResponse<String>> httpResponse, Type typeOfT) throws Exception
	{
		try
		{
			HttpResponse<String> result = httpResponse.get();
			if (result.getStatus() >= 200 && result.getStatus() <= 299)
			{
				Gson gson = getGsonWithSerializerDeserializer();
				JsonElement jsonElement = gson.fromJson(result.getBody(), JsonElement.class);
				if (jsonElement != null && !jsonElement.isJsonNull())
				{
					if (jsonElement.isJsonObject())
					{
						JsonObject resultObject = jsonElement.getAsJsonObject();
						JsonArray dataMember = resultObject.getAsJsonArray("data");
						if (!dataMember.isJsonNull())
						{
							if (dataMember.isJsonArray())
							{
								return gson.fromJson(dataMember, typeOfT);
							}
						}
					}
					else if (jsonElement.isJsonArray())
					{
						return gson.fromJson(jsonElement.getAsJsonArray(), typeOfT);
					}
					else
					{
						throw new Exception("Invalid result format");
					}
				}
				return new ArrayList<>();
			}
		}
		catch (Exception exception)
		{
			throw new Exception(exception.getMessage(), exception.getCause());
		}
		throw new Exception("Error processing your request");
	}

	@Override
	public <T> List<T> processListResult(Future<HttpResponse<String>> httpResponse, IObjectAssembler objectAssembler) throws Exception
	{
		try
		{
			HttpResponse<String> result = httpResponse.get();
			if (result.getStatus() >= 200 && result.getStatus() <= 299)
			{
				Gson gson = getGsonWithSerializerDeserializer();
				JsonElement jsonElement = gson.fromJson(result.getBody(), JsonElement.class);
				if (jsonElement != null && !jsonElement.isJsonNull())
				{
					if (jsonElement.isJsonObject())
					{
						JsonObject resultObject = jsonElement.getAsJsonObject();
						JsonArray dataMember = resultObject.getAsJsonArray("data");
						if (!dataMember.isJsonNull())
						{
							List<T> tmpList = new ArrayList<>();
							while (dataMember.iterator().hasNext())
							{
								tmpList.add(objectAssembler.assemble(dataMember.iterator().next()));
							}
							return tmpList;
						}
					}
					else if (jsonElement.isJsonArray())
					{
						List<T> tmpList = new ArrayList<>();
						JsonArray jsonArray = jsonElement.getAsJsonArray();
						while (jsonArray.iterator().hasNext())
						{
							tmpList.add(objectAssembler.assemble(jsonArray.iterator().next()));
						}
						return tmpList;
					}
					else
					{
						throw new Exception("Invalid result format");
					}
				}
				return new ArrayList<>();
			}
		}
		catch (Exception exception)
		{
			throw new Exception(exception.getMessage(), exception.getCause());
		}
		throw new Exception("Error processing your request");
	}

	@Override
	public <T> List<T> processListResult(Future<HttpResponse<String>> httpResponse, PagingInformation pagingInformation, Type typeOfT) throws Exception
	{
		try
		{
			HttpResponse<String> result = httpResponse.get();
			if (result.getStatus() >= 200 && result.getStatus() <= 299)
			{
				Gson gson = getGsonWithSerializerDeserializer();
				JsonElement jsonElement = gson.fromJson(result.getBody(), JsonElement.class);
				if (jsonElement != null && !jsonElement.isJsonNull())
				{
					if (jsonElement.isJsonObject())
					{
						JsonObject resultObject = jsonElement.getAsJsonObject();
						JsonArray dataMember = resultObject.getAsJsonArray("data");
						pagingInformation.setTotal(resultObject.getAsJsonPrimitive("total").getAsInt());
						if (!dataMember.isJsonNull() && dataMember.isJsonArray())
						{
							return gson.fromJson(dataMember, typeOfT);
						}
					}
					else if (jsonElement.isJsonArray())
					{
						return gson.fromJson(jsonElement.getAsJsonArray(), typeOfT);
					}
					else
					{
						throw new Exception("Invalid result format");
					}
				}

				return new ArrayList<>();
			}
		}
		catch (Exception exception)
		{
			throw exception;
		}
		throw new Exception("Error processing your request");
	}

	@Override
	public <T> List<T> processListResult(Future<HttpResponse<String>> httpResponse, PagingInformation pagingInformation, IObjectAssembler objectAssembler) throws Exception
	{
		try
		{
			HttpResponse<String> result = httpResponse.get();
			if (result.getStatus() >= 200 && result.getStatus() <= 299)
			{
				Gson gson = getGsonWithSerializerDeserializer();
				JsonElement jsonElement = gson.fromJson(result.getBody(), JsonElement.class);
				if (jsonElement != null && !jsonElement.isJsonNull())
				{
					if (jsonElement.isJsonObject())
					{
						JsonObject resultObject = jsonElement.getAsJsonObject();
						JsonArray dataMember = resultObject.getAsJsonArray("data");
						pagingInformation.setTotal(resultObject.getAsJsonPrimitive("total").getAsInt());
						if (!dataMember.isJsonNull() && dataMember.isJsonArray())
						{
							List<T> tmpList = new ArrayList<>();
							while (dataMember.iterator().hasNext())
							{
								tmpList.add(objectAssembler.assemble(dataMember.iterator().next()));
							}
							return tmpList;
						}
					}
					else if (jsonElement.isJsonArray())
					{
						List<T> tmpList = new ArrayList<>();
						JsonArray jsonArray = jsonElement.getAsJsonArray();
						while (jsonArray.iterator().hasNext())
						{
							tmpList.add(objectAssembler.assemble(jsonArray.iterator().next()));
						}
						return tmpList;
					}
					else
					{
						throw new Exception("Invalid result format");
					}
				}
				return new ArrayList<>();
			}
		}
		catch (Exception exception)
		{
			throw exception;
		}
		throw new Exception("Error processing your request");
	}

	@Override
	public String processJSONResult(Future<HttpResponse<String>> httpResponse) throws Exception
	{
		try
		{
			HttpResponse<String> result = httpResponse.get();
			if (result.getStatus() >= 200 && result.getStatus() <= 299)
			{
				Gson gson = getGsonWithSerializerDeserializer();
				JsonElement jsonElement = gson.fromJson(result.getBody(), JsonElement.class);
				if (jsonElement.isJsonArray())
				{
					JsonArray mainJsonObject = jsonElement.getAsJsonArray();
					return mainJsonObject.toString();
				}
				else if (jsonElement.isJsonObject())
				{
					JsonObject mainJsonObject = jsonElement.getAsJsonObject();
					return mainJsonObject.toString();
				}
				else if (jsonElement.isJsonPrimitive())
				{
					JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
					return jsonPrimitive.getAsString();
				}
			}
		}
		catch (Exception exception)
		{
			throw new Exception(exception.getMessage(), exception.getCause());
		}
		throw new Exception("Error processing your request ");
	}

	@Override
	public JsonPrimitive processJsonPrimitiveResult(Future<HttpResponse<String>> httpResponse) throws Exception
	{
		HttpResponse<String> result = httpResponse.get();
		if (result.getStatus() >= 200 && result.getStatus() <= 299)
		{
			Gson gson = getGsonWithSerializerDeserializer();
			JsonElement jsonElement = gson.fromJson(result.getBody(), JsonElement.class);
			if (jsonElement.isJsonPrimitive())
			{
				return jsonElement.getAsJsonPrimitive();
			}
			else
			{
				throw new Exception("Result is not primitive");
			}
		}
		throw new Exception("Error processing your request");
	}

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
}
