package com.droidlogix.dbflare.client.models;

import java.util.List;

public class ObjectResult<T> implements IObjectResult<T>
{
	private long total;
	private T data;
	private long dbExecutionTime;
	private List<String> errors;

	@Override
	public long getTotal()
	{
		return this.total;
	}

	@Override
	public void setTotal(long total)
	{
		this.total = total;
	}

	@Override
	public T getData()
	{
		return this.data;
	}

	@Override
	public void setData(T data)
	{
		this.data = data;
	}

	@Override
	public long getDbExecutionTime()
	{
		return this.dbExecutionTime;
	}

	@Override
	public void setDbExecutionTime(long delta)
	{
		this.dbExecutionTime = delta;
	}

	@Override
	public List<String> getErrors()
	{
		return this.errors;
	}

	@Override
	public void setErrors(List<String> errors)
	{
		this.errors = errors;
	}
}
