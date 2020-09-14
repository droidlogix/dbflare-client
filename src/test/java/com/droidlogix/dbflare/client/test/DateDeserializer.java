package com.droidlogix.dbflare.client.test;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.Date;

public class DateDeserializer implements JsonDeserializer<Object>
{
    private static final Logger logger = LoggerFactory.getLogger(DateDeserializer.class);

    @Override
    public Object deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
    {
        try
        {
            return new Date(jsonElement.getAsJsonPrimitive().getAsLong());
        }
        catch (Exception exception)
        {
            logger.error(exception.getMessage());
            return null;
        }
    }
}