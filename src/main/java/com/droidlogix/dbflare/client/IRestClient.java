package com.droidlogix.dbflare.client;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IRestClient {

    <T> IResultProcessor zPost(String url, T payload) throws Exception;

    <T> IResultProcessor zPost(String url, Map<String, String> routeParams, T payload) throws Exception;

    <T> IResultProcessor zPost(String url, Map<String, String> routeParams, Map<String, Object> queryParams, T payload) throws Exception;

    <T> IResultProcessor zPost(String url, List<T> payloads) throws Exception;

    <T> IResultProcessor zPost(String url, Map<String, String> routeParams, List<T> payloads) throws Exception;

    <T> IResultProcessor zPost(String url, Map<String, String> routeParams, Map<String, Object> queryParams, List<T> payloads) throws Exception;

    <T> IResultProcessor zPut(String url, Map<String, String> routeParams, Map<String, Object> queryParams, T payload) throws Exception;

    <T> IResultProcessor zPut(String url, Map<String, String> routeParams, Map<String, Object> queryParams, List<T> payloads) throws Exception;

    <T> IResultProcessor zUpsert(String url, Map<String, String> routeParams, Map<String, Object> queryParams, T payload) throws Exception;

    <T> IResultProcessor zUpsert(String url, Map<String, String> routeParams, Map<String, Object> queryParams, List<T> payloads) throws Exception;

    void zDelete(String url, Map<String, String> routeParams, Map<String, Object> queryParams) throws Exception;

    IResultProcessor zGet(String url) throws Exception;

    IResultProcessor zGet(String url, Map<String, String> routeParams, Map<String, Object> queryParams) throws Exception;

    IResultProcessor zGet(String url, Map<String, String> routeParams, Map<String, Object> queryParams, Map<String, Collection<?>> queryParamsCollection) throws Exception;
}