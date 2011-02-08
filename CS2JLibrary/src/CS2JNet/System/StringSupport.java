/*
   Copyright 2007,2008,2009,2010 Rustici Software, LLC
   Copyright 2010,2011 Kevin Glynn (kevin.glynn@twigletsoftware.com)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

   Author(s):

   Kevin Glynn (kevin.glynn@twigletsoftware.com)
*/

package CS2JNet.System;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class StringSupport {

	private static boolean isIn (char c, char[] cs)
	{
		for (char f : cs)
			if (f == c)
				return true;

		return false;
	}
	
	public static boolean equals(String s1, String s2)
	{
		if (s1 == null)
			return s2 == null;
		else
			return s1.equals(s2);
	}
	
	public static String charAltsToRegex(char[] cs)
	{
		StringBuilder buf = new StringBuilder();
		
		for (char c : cs)
		{
			buf.append("(\\Q" + c + "\\E)|");
		}
		if (buf.length() > 0)
			// remove final '|'
			buf.deleteCharAt(buf.length() - 1);
		return buf.toString();
	}

	public static String strAltsToRegex(String[] strs)
	{
		StringBuilder buf = new StringBuilder();
		
		for (String s : strs)
		{
			buf.append("(\\Q" + s + "\\E)|");
		}
		if (buf.length() > 0)
			// remove final '|'
			buf.deleteCharAt(buf.length() - 1);
		return buf.toString();
	}
	
	public static int Compare(String s1, String s2) {
		return Compare(s1, s2, false);
	}
	
	public static int Compare(String s1, String s2, boolean ignoreCase)
	{
		// Handele nulls, null is smaller than a real string
		if (s1 == null) {
			if (s2 == null) {
				return 0;
			}
			else {
				return -1;
			}
		}
		if (s2 == null) {
			return 1;
		}
		
		if (ignoreCase) {
			return s1.compareToIgnoreCase(s2);
		}
		else {
			return s1.compareTo(s2);
		}
	}

	public static String Trim(String in, char[] filters, boolean trimStart, boolean trimEnd)
	{
		// Locate first non-trimmable index
		int firstIdx = 0;
		if (trimStart) {
			while (firstIdx < in.length())
			{
				if (isIn(in.charAt(firstIdx),filters))
				    firstIdx++;
				else
					break;
			}
		}
		
		int lastIdx = in.length();		
		if (trimEnd) {
			while (lastIdx > firstIdx)
			{
				if (isIn(in.charAt(lastIdx-1),filters))
					lastIdx--;
				else
					break;
			}
		}
		
		// firstIdx is start of new string, lastIdx is end of new string
		return in.substring(firstIdx, lastIdx);
	}
	
	// These are listed in the remarks for String.Trim() documentation
	private static char[] wschars = {
			(char) 0x9,
			(char) 0xA,
			(char) 0xB,
			(char) 0xC,
			(char) 0xD,
			// #if NET_2_0
			// (char) 0x85, (char) 0x1680, (char) 0x2028, (char) 0x2029,
			// #endif
			(char) 0x20, (char) 0xA0, (char) 0x2000, (char) 0x2001,
			(char) 0x2002, (char) 0x2003, (char) 0x2004, (char) 0x2005,
			(char) 0x2006, (char) 0x2007, (char) 0x2008, (char) 0x2009,
			(char) 0x200A, (char) 0x200B, (char) 0x3000, (char) 0xFEFF };
	
	public static String Trim(String in)
	{
		return Trim(in, wschars, true, true);
	}

	public static String Trim(String in, char[] filters)
	{
		return Trim(in, filters, true, true);
	}
	
	public static String TrimStart(String in, char[] filters)
	{
		return Trim(in, (filters == null ? wschars : filters), true, false);
	}

	public static String TrimEnd(String in, char[] filters)
	{
		return Trim(in, (filters == null ? wschars : filters), false, true);
	}
	
	public static String PadLeft(Object inVal, int totalWidth, char paddingChar)
	{
		String inValStr = inVal.toString();
		int inValLen = inValStr.length();
		if (inValLen >= totalWidth)
			return inValStr;
		else
		{
			char[] padder = new char[totalWidth-inValLen];
			Arrays.fill(padder, paddingChar);
			return new String(padder)+inValStr;
		}
	}

	public static String PadLeft(Object inVal, int totalWidth)
	{
		return PadLeft(inVal,totalWidth, ' ');
 	}

	public static String PadRight(Object inVal, int totalWidth, char paddingChar)
	{
		String inValStr = inVal.toString();
		int inValLen = inValStr.length();
		if (inValLen >= totalWidth)
			return inValStr;
		else
		{
			char[] padder = new char[totalWidth-inValLen];
			Arrays.fill(padder, paddingChar);
			return inValStr + new String(padder);
		}
	}

	public static String PadRight(Object inVal, int totalWidth)
	{
		return PadRight(inVal,totalWidth, ' ');
 	}

	// encodes special characters with their html character entity name
	// for now we only encode HTML 2.0's (4.0 has loads of them!)
    public static final String encodeHTML(String str) {
        if (str == null || str == "") return str;
 
        StringBuilder sb = new StringBuilder();
        char c;
        for (int i = 0; i < str.length(); i++) {
        	c = str.charAt(i);
            switch (c) {
            case '<': 
                sb.append("&lt;");
                break;
            case '>': 
                sb.append("&gt;");
                break;
            case '&': 
                sb.append("&amp;");
                break;
            case '\"': 
                sb.append("&quot;");
                break;
            default: 
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static final String[] Split(String str, char c1) {
        
    	String[] rets = str.split("\\Q" + c1 + "\\E");
    	if (str.endsWith(c1+""))
    	{
    		// Add empty string for .Net
    		ArrayList<String> ret_al = new ArrayList<String>(Arrays.asList(rets));
    		ret_al.add("");
    		rets = ret_al.toArray(new String [ret_al.size()]);
    	}
    	return rets;
    }
    		
    public static final String[] Split(String str, char c1, char c2) {
        
    	String[] rets = str.split("\\Q" + c1 + "\\E)|(\\Q" + c2 + "\\E");
    	if (str.endsWith(c1+"") || str.endsWith(c2+""))
    	{
    		// Add empty string for .Net
    		ArrayList<String> ret_al = new ArrayList<String>(Arrays.asList(rets));
    		ret_al.add("");
    		rets = ret_al.toArray(new String [ret_al.size()]);
    	}
    	return rets;
    }
    	
    
   public static final String[] Split(String str, char[] cs, StringSplitOptions options) {
        
    	String[] rets = str.split(charAltsToRegex(cs));
    	
    	for (char c : cs)
    	{ 
    		if (options != StringSplitOptions.RemoveEmptyEntries && str.endsWith(c+""))
    		{
    			// Add empty string for .Net
    			ArrayList<String> ret_al = new ArrayList<String>(Arrays.asList(rets));
    			ret_al.add("");
    			rets = ret_al.toArray(new String [ret_al.size()]);
    			break;
    		}
    	}
    	
    	if (options == StringSplitOptions.RemoveEmptyEntries)
    	{
			ArrayList<String> ret_al = new ArrayList<String>();
			for (String p : rets)
			{
				if (!p.equals(""))
				{
					ret_al.add(p);
				}
			}
			rets = ret_al.toArray(new String [ret_al.size()]);
    	}
    	
    	return rets;
    }

   public static final String[] Split(String str, String[] strs, StringSplitOptions options) {
       
   	String[] rets = str.split(strAltsToRegex(strs));
   	
   	for (String s : strs)
   	{ 
   		if (options != StringSplitOptions.RemoveEmptyEntries && str.endsWith(s))
   		{
   			// Add empty string for .Net
   			ArrayList<String> ret_al = new ArrayList<String>(Arrays.asList(rets));
   			ret_al.add("");
   			rets = ret_al.toArray(new String [ret_al.size()]);
   			break;
   		}
   	}
   	
   	if (options == StringSplitOptions.RemoveEmptyEntries)
   	{
			ArrayList<String> ret_al = new ArrayList<String>();
			for (String p : rets)
			{
				if (!p.equals(""))
				{
					ret_al.add(p);
				}
			}
			rets = ret_al.toArray(new String [ret_al.size()]);
   	}
   	
   	return rets;
   }

    public static final boolean isNullOrEmpty(String str) {
        
    	return (str == null || "".equals(str));
    }
    
    public static final boolean IsNullOrEmpty(String str){
    	return isNullOrEmpty(str);
    }
    
    public static final boolean IsEmptyOrBlank(String str){
		// Locate first non-trimmable index
		int firstIdx = 0;
		while (firstIdx < str.length())
		{
			if (isIn(str.charAt(firstIdx),wschars))
				firstIdx++;
			else
				break;
		}
		return firstIdx == str.length();
    }
    		
    public static final int lastIndexOfAny(String str, char[] anyOf)
    {
    	int index = -1;
    	for (char test : anyOf)
    	{
    		index = Math.max(index, str.lastIndexOf(test));
    	}
    	return index;
    }
	
    // in C# new String('x',50)
    public static String mkString(char c, int count) {
        char[] chars = new char[count];
        for (int i = 0; i < count; i++) {
        	chars[i] = c;
        }
        return new String(chars);
	}
    
	public static void Testmain(String[] args)
	{
		System.out.println("**" + Trim("") + "**");
		System.out.println("**" + Trim("kevin") + "**");
		System.out.println("**" + Trim("    ") + "**");
		
		for (Object o : new char[] {'h', 'e', 'l', 'l', 'o'})
		{
			char c = (Character) o;
			System.out.print(c);
		}
		System.out.println();
		
		System.out.println("5L = **" + PadLeft(56, 5, '0') + "**");
		System.out.println("1L = **" + PadLeft(56, 1, '0') + "**");
		System.out.println("2L = **" + PadLeft(56, 2, '0') + "**");
		System.out.println("5L = **" + PadLeft(-56, 5, '0') + "**");
		System.out.println("1L = **" + PadLeft(-56, 1, '0') + "**");
		System.out.println("2L = **" + PadLeft(-56, 2, '0') + "**");

		System.out.println("5R = **" + PadRight(56, 5, '0') + "**");
		System.out.println("1R = **" + PadRight(56, 1, '0') + "**");
		System.out.println("2R = **" + PadRight(56, 2, '0') + "**");
		System.out.println("5R = **" + PadRight(-56, 5, '0') + "**");
		System.out.println("1R = **" + PadRight(-56, 1, '0') + "**");
		System.out.println("2R = **" + PadRight(-56, 2, '0') + "**");
		
		String[] splitFred = Split("fred=",'=');
		System.out.println("Split(\"fred=\", '=') = [\"" + splitFred[0] + "\", \"" + splitFred[1] + "\"]");
		splitFred = Split("fred=5",'=');
		System.out.println("Split(\"fred=5\", '=') = [\"" + splitFred[0] + "\", \"" + splitFred[1] + "\"]");
		splitFred = Split("=fred",'=');
		System.out.println("Split(\"=fred\", '=') = [\"" + splitFred[0] + "\", \"" + splitFred[1] + "\"]");
		
}


}
