package com.droidlogix.dbflareclient;

/**
 * This class is used as reference for result pagination
 */
public class PagingInformation
{
	private int page; // Indicator of current page
	private long skip; // SKip record indicator
	private int pageSize; // Number of records to return as a list
	private int total; // Total is indicator how much total record it has in the database

	public PagingInformation(int page, long skip, int pageSize)
	{
		this.page = page;
		this.skip = skip;
		this.pageSize = pageSize;
	}

	public PagingInformation(int page, long skip, int pageSize, int total)
	{
		this.page = page;
		this.skip = skip;
		this.pageSize = pageSize;
		this.total = total;
	}

	public int getPage()
	{
		return page;
	}

	public void setPage(int page)
	{
		this.page = page;
	}

	public long getSkip()
	{
		return skip;
	}

	public void setSkip(long skip)
	{
		this.skip = skip;
	}

	public int getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}

	public int getTotal()
	{
		return total;
	}

	public void setTotal(int total)
	{
		this.total = total;
	}
}
