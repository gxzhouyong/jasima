/*******************************************************************************
 * Copyright (c) 2010, 2014 Torsten Hildebrandt and jasima contributors
 *
 * This file is part of jasima, v1.1.
 *
 * jasima is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jasima is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with jasima.  If not, see <http://www.gnu.org/licenses/>.
 *
 * $Id$
 *******************************************************************************/
package jasima_gui;

public class FormTextBuilder {
	protected StringBuilder output = new StringBuilder();
	protected boolean hasLink = false;
	protected boolean paragraphEmpty = true;
	
	public FormTextBuilder() {
		output.append("<form><p>");
	}
	
	public void startParagraph() {
		finishLink();
		if(!paragraphEmpty) {
			output.append("</p><p>");
			paragraphEmpty = true;
		}
	}
	
	public void addText(String text) {
		for(char c : text.toCharArray()) {
			if(c == '<') {
				output.append("&lt;");
			} else if(c == '>') {
				output.append("&gt;");
			} else if(c == '&') {
				output.append("&amp;");
			} else {
				output.append(c);
			}
		}
		paragraphEmpty = false;
	}
	
	public void startLink(String href) {
		finishLink();
		output.append("<a href=\"");
		addText(href);
		output.append("\">");
		hasLink = true;
	}
	
	public void finishLink() {
		if(hasLink) {
			output.append("</a>");
			hasLink = false;
		}
		paragraphEmpty = false;
	}
	
	public String finish() {
		finishLink();
		output.append("</p></form>");
		String retVal = output.toString();
		output = null;
		return retVal;
	}
	
	private boolean isProbablyTag(String tag) {
		int firstSpace = tag.indexOf(' ');
		if(firstSpace == -1) {
			firstSpace = tag.length();
		}
		return firstSpace <= 6;
	}
	
	public String parseBadHtml(String input) {
		int readPtr = 0;
		while(readPtr < input.length()) {
			char c = input.charAt(readPtr++);
			if(c == '<') {
				int tagStart = readPtr;
				int tagEnd = input.substring(tagStart).indexOf('>');
				if(tagEnd == -1) {
					output.append("&lt;");
					paragraphEmpty = false;
				} else {
					String tag = input.substring(tagStart, tagStart + tagEnd);
					String t = tag.trim();
					if(t.equals("p")) {
						startParagraph();
						readPtr += tagEnd + 1;
					} else if(t.equals("/p")) {
						readPtr += tagEnd + 1;
					} else if(t.startsWith("a ")) {
						String href = t.substring(2).trim();
						if(href.startsWith("href")) {
							href = href.substring(4).trim();
							if(href.startsWith("=")) {
								href = href.substring(1).trim();
								if(href.startsWith("'") || href.startsWith("\"")) {
									href = href.substring(1, href.length() - 1);
								}
								startLink(href);
							}
						}
						readPtr += tagEnd + 1;
					} else if(t.equals("/a")) {
						finishLink();
						readPtr += tagEnd + 1;
					} else if(t.equals("dt")) {
						startParagraph();
						readPtr += tagEnd + 1;
					} else if(isProbablyTag(t)) {
						readPtr += tagEnd + 1;
					} else {
						output.append("&lt;");
						paragraphEmpty = false;
					}
				}
			} else if(c == '>') {
				output.append("&gt;");
				paragraphEmpty = false;
			} else {
				if(!paragraphEmpty || !Character.isWhitespace(c)) {
					output.append(c);
					paragraphEmpty = false;
				}
			}
		}
		finishLink();
		return output.toString();
	}
}
