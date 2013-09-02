package com.paic.sublist;

import java.util.ArrayList;
import java.util.List;

import com.paic.feature.Feature;



public class SublistFeature implements Feature{
	private List<Column> columns=new ArrayList<Column>();

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	
}
