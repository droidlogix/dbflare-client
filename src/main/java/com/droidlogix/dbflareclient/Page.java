package com.droidlogix.dbflareclient;

import java.util.ArrayList;
import java.util.List;

public class Page<T>
{
	public int total;
	public List<T> data;

	public Page()
	{
		this.total = 0;
		this.data = new ArrayList<>();
	}

	public Page(List<T> data, int total)
	{
		this.total = total;
		this.data = data;
	}

	public int maxPage(int pageSize)
	{
		if(this.total == 0 || this.total == 1)
		{
			return 1;
		}
		else if(this.total < pageSize)
		{
			return 1;
		}
		else
		{
			return (this.total % pageSize) == 0 ? (this.total / pageSize) : (this.total / pageSize) + 1;
		}
	}
}