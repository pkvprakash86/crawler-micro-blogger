package com.tags.extract;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.tokenattributes.*;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.*;


public class StopWordRemovalAndStemming {

	public String process(String input) throws IOException   {
		Set<String> stopWords = new HashSet<String>();
	    stopWords.add("a");
	    stopWords.add("I");
	    stopWords.add("the");
	    stopWords.add("in");
	    stopWords.add("is");
	    stopWords.add("tabular");
	    stopWords.add("and");
	    stopWords.add("am");
	    stopWords.add("all");
	    stopWords.add("of");
	    stopWords.add("on");
	    stopWords.add("our");
	    stopWords.add("with");
	    stopWords.add("are");
	    stopWords.add("div");
	    stopWords.add("class");
	    stopWords.add("span");
	    stopWords.add("link");
	    stopWords.add("href");
	    stopWords.add("http");
	    stopWords.add("rel");
	    stopWords.add("li");
	    stopWords.add("p");
	    stopWords.add("h2");
	    stopWords.add("h6");
	    stopWords.add("meta");
	    stopWords.add("property");
	    stopWords.add("article");
	    stopWords.add("content");
	    stopWords.add("amp");
	    
	    

	    TokenStream tokenStream = new StandardTokenizer(
	            Version.LUCENE_30, new StringReader(input));
	    tokenStream = new StopFilter(true, tokenStream, stopWords);
	    tokenStream = new PorterStemFilter(tokenStream);

	    StringBuilder sb = new StringBuilder();
	    TermAttribute termAttr = tokenStream.getAttribute(TermAttribute.class);
	    while (tokenStream.incrementToken()) {
	        if (sb.length() > 0) {
	            sb.append(" ");
	        }
	        sb.append(termAttr.term());
	    }
	    return sb.toString();
	}
}