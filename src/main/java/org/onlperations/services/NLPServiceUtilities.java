package org.onlperations.services;

import java.util.ArrayList;
import java.util.List;

import opennlp.tools.util.Span;

public class NLPServiceUtilities {
	
	public static <T> List<T> getValueAtSpan(Span span, T[] objectArr) {
		
		List<T> spanList = new ArrayList<T>();
		
		//T spanArr[] = new T[span.length()];
		//int spanArrCnt = 0;
		
		for(int cnt = span.getStart(); cnt < span.getEnd(); cnt++) {
			//spanArr[spanArrCnt++] = objectArr[cnt];
			spanList.add(objectArr[cnt]);
		}
		
		return spanList;
		
	}

}
