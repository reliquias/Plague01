package br.com.inicial.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@SuppressWarnings({"serial", "rawtypes", "unchecked"})
public class XLazyModel extends LazyDataModel<Object>{  

	private List<Object> datasource;

	public XLazyModel(List<Object> datasource) {
		this.datasource = datasource;
		setRowCount(this.datasource.size());
	}

	@Override
	public Object getRowData(String rowKey) {
		for (Object obj : datasource) {
			Class c = obj.getClass();
		    Method method;
			try {
				if(c.getName().equals("br.com.inicial.modelo.Pais")){
					method = c.getMethod("get" + Utils.capitaliza("sigla"));
				}else if(c.getName().equals("br.com.inicial.modelo.TipoDoenca")){
					method = c.getMethod("get" + Utils.capitaliza("doenca"));
				}else{
					method = c.getMethod("get" + Utils.capitaliza("nome"));
				}
				String apelido = "";
			    if(method.invoke(obj) != null){
			    	apelido = (String) method.invoke(obj);
			    }
				if (apelido.equals(rowKey))
					return obj;
			}catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		}
		return null;
	}

	@Override
	public Object getRowKey(Object obj) {
		String apelido = "";
	    try {
	    	Class c = obj.getClass();
		    Method method;
		    if(c.getName().equals("br.com.inicial.modelo.Pais")){
				method = c.getMethod("get" + Utils.capitaliza("sigla"));
		    }else if(c.getName().equals("br.com.inicial.modelo.TipoDoenca")){
				method = c.getMethod("get" + Utils.capitaliza("doenca"));
			}else{
				method = c.getMethod("get" + Utils.capitaliza("nome"));
			}
		    if(method.invoke(obj) != null){
				apelido = (String) method.invoke(obj);
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return apelido;
	}

	@Override
	public List<Object> load(int first, int pageSize, String sortField,
		SortOrder sortOrder, Map<String, Object> filters) {
		List<Object> data = new ArrayList<Object>();

		// filter
		for (Object obj : datasource) {
			boolean match = true;

			for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
				try {
					String filterProperty = it.next();
					String filterValue = (String) filters.get(filterProperty);
					Class c = obj.getClass();
				    Method method = c.getMethod("get" + Utils.capitaliza(filterProperty));
//				    String fieldValue = String.valueOf(agencia.getClass().getField(filterProperty).get(agencia));
				    String fieldValue = "";
				    if(method.invoke(obj) != null){
				    	fieldValue = (String) method.invoke(obj);
				    }
				    
				    if (filterValue == null	|| fieldValue.toLowerCase().contains(filterValue.toLowerCase())) {
						match = true;
					} else {
						match = false;
						break;
					}
				} catch (Exception e) {
					match = false;
				}
			}

			if (match) {
				data.add(obj);
			}
		}
		  

		// sort
		if (sortField != null) {
			Collections.sort(data, new LazySorter(sortField, sortOrder));
		}

		// rowCount
		int dataSize = data.size();
		this.setRowCount(dataSize);
		this.setPageSize(pageSize);

		// paginate
		if (dataSize > pageSize) {
			try {
				return data.subList(first, first + pageSize);
			} catch (IndexOutOfBoundsException e) {
				return data.subList(first, first + (dataSize % pageSize));
			}
		} else {
			return data;
		}
	}
}
