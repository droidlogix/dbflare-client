package com.droidlogix.dbflare.client;

import com.droidlogix.dbflare.client.exceptions.DbFlareException;
import com.droidlogix.dbflare.client.models.Pagination;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import kong.unirest.HttpResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Future;

public class ResultProcessor implements IResultProcessor {
    private static final Logger logger = LoggerFactory.getLogger(ResultProcessor.class);

    private Future<HttpResponse<String>> httpResponse;

    public ResultProcessor(Future<HttpResponse<String>> httpResponse) {
        this.httpResponse = httpResponse;
    }

    @Override
    public JsonElement parse() throws Exception {
        return getRootElement(httpResponse.get()); // This will extract the root JSON Element
    }

    @Override
    public <T> T parse(Type typeOfT) throws Exception {
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

            if (root.has("result") && typeOfT != null)
            {
                JsonElement delta = root.get("result");
                if(!delta.isJsonNull()) {
                    JsonObject data = root.getAsJsonObject("result");
                    return gson.fromJson(data, typeOfT);
                }
                return null;
            }
            else
            {
                return gson.fromJson(root, typeOfT);
            }
        }
        return null;
    }

    @Override
    public <T> T parse(IObjectAssembler objectAssembler) throws Exception {
        return (T)objectAssembler.assemble(getRootElement(httpResponse.get()));
    }

    @Override
    public Map<String, Object> parseToMap() throws Exception {
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

            if (root.has("result"))
            {
                JsonObject data = root.getAsJsonObject("result");
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

    @Override
    public <T> List<T> parseToList(Type typeOfT) throws Exception {
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

        if (rootJsonElement.isJsonObject())
        {
            bubbleAnyDbFlareErrorMessages(rootJsonElement);
            JsonObject root = rootJsonElement.getAsJsonObject();
            if (root == null || root.isJsonNull())
            {
                return new ArrayList<>();
            }

            if (root.has("result"))
            {
                if(root.get("result").isJsonArray())
                {
                    JsonArray data = root.getAsJsonArray("result");
                    return gson.fromJson(data, TypeToken.getParameterized(ArrayList.class, typeOfT).getType());
                }
            }
        }
        return new ArrayList<>();
    }

    @Override
    public <T> List<T> parseToList(Type typeOfT, Pagination pagination) throws Exception {
        Gson gson = getGsonWithSerializerDeserializer();

        JsonElement rootJsonElement = getRootElement(httpResponse.get()); // This will extract the root JSON Element
        if (rootJsonElement == null || rootJsonElement.isJsonNull())
        {
            pagination.setTotal(0);
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
                pagination.setTotal(root.size());
                return gson.fromJson(root, typeOfT);
            }
        }

        if (rootJsonElement.isJsonObject())
        {
            bubbleAnyDbFlareErrorMessages(rootJsonElement);
            JsonObject root = rootJsonElement.getAsJsonObject();
            if (root == null || root.isJsonNull())
            {
                pagination.setTotal(0);
                return new ArrayList<>();
            }

            if (root.has("result"))
            {
                if(root.get("result").isJsonArray())
                {
                    JsonArray data = root.getAsJsonArray("result");
                    if(pagination != null) {
                        pagination.setTotal(root.getAsJsonPrimitive("total").getAsInt());
                    }
                    return gson.fromJson(data, TypeToken.getParameterized(ArrayList.class, typeOfT).getType());
                }
            }
        }
        return new ArrayList<>();
    }

    @Override
    public <T> List<T> parseToList(IObjectAssembler objectAssembler) throws Exception {
        return (List<T>)objectAssembler.assemble(getRootElement(httpResponse.get()));
    }

    @Override
    public <T> List<T> parseToList(IObjectAssembler objectAssembler, Pagination pagination) throws Exception {
        return (List<T>)objectAssembler.assemble(getRootElement(httpResponse.get()), pagination);
    }

    @Override
    public List<Map<String, Object>> parseToListMap() throws Exception {
        return parseToListMap(null);
    }

    @Override
    public List<Map<String, Object>> parseToListMap(Pagination pagination) throws Exception {
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

            if (root.has("result"))
            {
                if(root.get("result").isJsonArray())
                {
                    JsonArray data = root.getAsJsonArray("result");
                    if(pagination != null) {
                        pagination.setTotal(root.getAsJsonPrimitive("total").getAsInt());
                    }
                    return gson.fromJson(data, new TypeToken<List<Map<String, Object>>>()
                    {
                    }.getType());
                }
            }
        }
        return new ArrayList<>();
    }

    @Override
    public JsonPrimitive parseToJsonPrimitive() throws Exception {
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

            if (root.has("result"))
            {
                return root.getAsJsonPrimitive("result");
            } else {
                return root.getAsJsonPrimitive();
            }
        }
        return null;
    }

    @Override
    public JsonNode parseToJsonNode() throws Exception {
        return null;
    }

    @Override
    public String parseToJSONString() throws Exception {
        JsonElement rootJsonElement = getRootElement(httpResponse.get()); // This will extract the root JSON Element
        if (rootJsonElement == null || rootJsonElement.isJsonNull())
        {
            return null;
        }

        if (rootJsonElement.isJsonArray())
        {
            return rootJsonElement.getAsJsonArray().toString();
        }

        if (rootJsonElement.isJsonObject())
        {
            return rootJsonElement.getAsJsonObject().toString();
        }

        if (rootJsonElement.isJsonPrimitive())
        {
            return rootJsonElement.getAsJsonPrimitive().toString();
        }
        return null;
    }

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
                    //System.out.println(parseException.getMessage());
                    return null;
                }
            }
        }).registerTypeHierarchyAdapter(byte[].class, new ByteArrayToBase64TypeAdapter());
        return gsonBuilder.create();
    }

    private JsonElement getRootElement(HttpResponse<String> response) throws Exception
    {
        Gson gson = getGsonWithSerializerDeserializer();

        if (response == null)
        {
            throw new NullPointerException("response is null");
        }

        if (response.getStatus() >= 200 && response.getStatus() <= 299)
        {
            return gson.fromJson(response.getBody(), JsonElement.class);
        }

        bubbleAnyDbFlareErrorMessages(gson.fromJson(response.getBody(), JsonElement.class));
        //StringBuilder sb = new StringBuilder("Expecting response code 200; got " + response.getStatus() + "\n\n");
        //sb.append(response.getBody());
        //sb.append("\n\n");
        //logger.error(sb.toString());
        //throw new Exception(sb.toString());
        //bubbleAnyDbFlareErrorMessages(gson.fromJson(response.getBody(), JsonElement.class));
        return null;
    }

    private void bubbleAnyDbFlareErrorMessages(JsonElement rootElement) throws DbFlareException
    {
        if (rootElement != null)
        {
            if (!rootElement.isJsonNull() && rootElement.isJsonObject())
            {
                JsonObject root = rootElement.getAsJsonObject();
                if (root.has("errors"))
                {
                    JsonArray jsonErrors = root.getAsJsonArray("errors").deepCopy();
                    StringBuilder sb = new StringBuilder();
                    List<String> errors = new ArrayList<>();
                    for(int i = 0; i < jsonErrors.size(); i++) {
                        if(!jsonErrors.get(i).isJsonNull()) {
                            sb.append(jsonErrors.get(i).getAsString());
                            sb.append("\n");
                            errors.add(jsonErrors.get(i).getAsString());
                        }
                    }

                    if(sb.length() > 0) {
                        throw new DbFlareException(sb.toString(), errors);
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

    /*private static class BooleanTypeAdapter implements JsonSerializable, JsonDeserializer<Boolean> {

        @Override
        public void serialize(JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        }

        @Override
        public void serializeWithType(JsonGenerator jsonGenerator, SerializerProvider serializerProvider, TypeSerializer typeSerializer) throws IOException {

        }

        @Override
        public Boolean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if(jsonElement != null) {
                jsonElement.getAs
            }
            return false;
        }
    }*/
}
