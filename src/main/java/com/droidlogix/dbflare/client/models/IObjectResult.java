package com.droidlogix.dbflare.client.models;

import java.util.List;

public interface IObjectResult<T>
{
	long getTotal();

	void setTotal(long total);

	T getData();

	void setData(T data);

	long getDbExecutionTime();

	void setDbExecutionTime(long delta);

	List<String> getErrors();

	void setErrors(List<String> errors);
}
