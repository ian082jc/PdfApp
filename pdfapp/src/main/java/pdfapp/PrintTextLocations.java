/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pdfapp;

import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

/**
 * This is an example on how to get some x/y coordinates of text.
 *
 * @author Ben Litchfield
 */
public class PrintTextLocations extends PDFTextStripper
{
	
	private String searchText;

	/**
     * Instantiate a new PDFTextStripper object.
     *
     * @throws IOException If there is an error loading the properties.
     */
    public PrintTextLocations() throws IOException
    {
    }

    /**
     * Override the default functionality of PDFTextStripper.
     */
    
    @Override
    protected void writeString(String text, List<TextPosition> textPositions) throws IOException{
        
        
        if(text.contains(searchText)) 
        {
        	int searchStringPosition = text.indexOf(searchText); 
        	TextPosition firstProsition = textPositions.get(searchStringPosition);
	        writeString(String.format("%s,%s,%s", firstProsition.getXDirAdj(),
	                firstProsition.getYDirAdj(), text));
        }

    }
    
    public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
}
