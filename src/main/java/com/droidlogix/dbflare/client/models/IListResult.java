package com.droidlogix.dbflare.client.models;

import java.util.List;

public interface IListResult<T>
{
	long getTotal();

	void setTotal(long total);

	List<T> getData();

	void setData(List<T> data);

	long getDbExecutionTime();

	void setDbExecutionTime(long delta);

	List<String> getErrors();

	void setErrors(List<String> errors);
}
