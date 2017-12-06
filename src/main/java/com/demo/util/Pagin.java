package com.demo.util;

import java.util.Collection;

public class Pagin {
	public static ThreadLocal TR_PAGENUMBER = new ThreadLocal();
	public static ThreadLocal TR_PAGESIZE = new ThreadLocal();
	public static ThreadLocal TR_SORT = new ThreadLocal();
	public static ThreadLocal TR_ORDER = new ThreadLocal();
	private Integer total;
	private Collection rows;
	private Collection footer;

	public static Long getPageNumber() {
		return (Long) TR_PAGENUMBER.get();
	}

	public static void setPageNumber(Long pageNumber) {
		TR_PAGENUMBER.set(pageNumber);
	}

	public static Long getPageSize() {
		return (Long) TR_PAGESIZE.get();
	}

	public static void setPageSize(Long pageSize) {
		TR_PAGESIZE.set(pageSize);
	}

	public static String getSort() {
		return (String) TR_SORT.get();
	}

	public static void setSort(String sort) {
		TR_SORT.set(sort);
	}

	public static String getOrder() {
		return (String) TR_ORDER.get();
	}

	public static void setOrder(String order) {
		TR_ORDER.set(order);
	}

	public Collection getFooter() {
		return this.footer;
	}

	public void setFooter(Collection footer) {
		this.footer = footer;
	}

	public Collection getRows() {
		return this.rows;
	}

	public void setRows(Collection rows) {
		this.rows = rows;
	}

	public Integer getTotal() {
		return this.total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}
}
