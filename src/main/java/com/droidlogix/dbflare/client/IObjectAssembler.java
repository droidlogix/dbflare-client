package com.droidlogix.dbflare.client;

import com.droidlogix.dbflare.client.models.Pagination;
import com.google.gson.JsonElement;
import com.mashape.unirest.http.HttpResponse;

import java.util.concurrent.Future;

public interface IObjectAssembler
{
	<T> T assemble(Future<HttpResponse<String>> httpResponse) throws RuntimeException;

	<T> T assemble(JsonElement jsonElement) throws RuntimeException;

	<T> T assemble(JsonElement jsonElement, Pagination pagination) throws RuntimeException;
}
