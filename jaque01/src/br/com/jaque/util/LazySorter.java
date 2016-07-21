package br.com.jaque.util;

import java.lang.reflect.Method;
import java.util.Comparator;

import org.primefaces.model.SortOrder;

@SuppressWarnings("rawtypes")
public class LazySorter implements Comparator {

    private String sortField;
    
    private SortOrder sortOrder;
    
    public LazySorter(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

	@SuppressWarnings("unchecked")
	@Override
	public int compare(Object o1, Object o2) {
		try {
        	Class c1 = o1.getClass();
        	Class c2 = o2.getClass();
        	
		    Method method1 = c1.getMethod("get" + Utils.capitaliza(this.sortField));
		    Method method2 = c2.getMethod("get" + Utils.capitaliza(this.sortField));
		    
            Object value1 = method1.invoke(o1);
            Object value2 = method2.invoke(o2);

            int value = ((Comparable)Utils.substituiNuloPorObject(value1)).compareTo(Utils.substituiNuloPorObject(value2));
            
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(Exception e) {
            throw new RuntimeException();
        }
	}
}
