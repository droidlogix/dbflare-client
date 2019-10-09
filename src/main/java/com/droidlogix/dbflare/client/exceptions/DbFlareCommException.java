package com.droidlogix.dbflare.client.exceptions;

public class DbFlareCommException extends Exception
{
	private int httpCode;
	private String message;

	public DbFlareCommException(int httpCode, String message)
	{
		this.httpCode = httpCode;
		this.message = message;
	}

	@Override
	public String toString()
	{
		return "DbFlareCommException{" +
				"httpCode=" + httpCode +
				", message='" + message + '\'' +
				'}';
	}
}
