package com.droidlogix.dbflareclient;

public class PagingInformation
{
	private int page;
	private long skip;
	private int pageSize;
	private int total;

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
