package pdfapp;

import java.awt.geom.Point2D;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class Main {

    /**
     * This will print the documents data.
     *
     * @param args The command line arguments.
     *
     * @throws IOException If there is an error parsing the document.
     */
    public static void main( String[] args ) throws IOException
    {
    	
        if( args.length != 1 )
        {
            usage();
        }
        else
        {
            PDDocument document = null;
            try
            {
                document = PDDocument.load( new File(args[0]) );
                HashMap<Integer, ArrayList<Point2D.Float>> signPositionMap = 
                		new HashMap<Integer, ArrayList<Point2D.Float>>();
                
                for(int pageNum = 1; pageNum <= document.getNumberOfPages(); pageNum++) {
	                PrintTextLocations stripper = new PrintTextLocations();
	                stripper.setSortByPosition( true );
	                stripper.setStartPage(pageNum);
	                stripper.setEndPage(pageNum);
	                
	                stripper.setSearchText("[/SIGN/]");
	
	                Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
	                stripper.writeText(document, dummy);
	                
	                System.out.println("Page " + pageNum);
	                
	                
	                
	                String textWithSearchString = stripper.getText(document);
	                System.out.println(textWithSearchString.replaceAll("(?m)^[ \t]*\r?\n", ""));
	                System.out.println("____________________");
	                
	                PDRectangle pageBounds = document.getPage(pageNum-1).getMediaBox();
	                System.out.println(pageBounds.getHeight());
	                System.out.println(pageBounds.getWidth());
	                ArrayList<Point2D.Float> points = new ArrayList<Point2D.Float>();
	                
	                for(String line : textWithSearchString.split("\n"))
	                {
	                	String values[] = line.split(",");
	                	if(values.length >= 2)
	                	{
	                		float x = Float.parseFloat(values[0]);
	                		System.out.println("X: " + x);
	                		float y = pageBounds.getHeight() - Float.parseFloat(values[1]);
	                		System.out.println("Y: " + y);
	                		points.add(new Point2D.Float(x, y));
	                		
	                	}
	                }
	                
	                signPositionMap.put(pageNum-1, points);
                }
                
                
                //Creating PDImageXObject object
	            PDImageXObject pdImage = PDImageXObject.createFromFile(
	            		"C:/Users/Ian/Documents/E-Sign Test/Signature2.png", document);
                
	            for (int i : signPositionMap.keySet())
	            {
	            	System.out.println("I value: " + i);
	            	//Retrieving the page
		            PDPage page = document.getPage(i);
		            
		            //Get list of x,y coordinates to place signature on currect page
		            ArrayList<Point2D.Float> signPositions = signPositionMap.get(i);
		           
		            //creating the PDPageContentStream object		            
		            PDPageContentStream contents = new PDPageContentStream(document, page, AppendMode.APPEND, true);
		
		            for(Point2D.Float point : signPositions) 
		            {
		            	//Drawing the image in the PDF document
			            contents.drawImage(pdImage, point.x, point.y);
			
			            System.out.println("Image inserted");
		            }
		            
		            //Closing the PDPageContentStream object
		            contents.close();		
	            }
	      		
	            //Saving the document
	            document.save("C:/Users/Ian/Documents/E-Sign Test/output.pdf");
            }
            finally
            {
                if( document != null )
                {
                    document.close();
                }
            }
        }
    }
    
    /**
     * This will print the usage for this document.
     */
    private static void usage()
    {
        System.err.println( "Usage: java " + PrintTextLocations.class.getName() + " <input-pdf>" );
    }
    
    
    
    private class SignaturePositionsOnPage {
    	
		public double pageNumber;
    	public Point2D.Double coords;
    	
    	public SignaturePositionsOnPage(double pageNumber, double xPosition, double yPosition) {
			this.pageNumber = pageNumber;
			this.coords.x = xPosition;
			this.coords.y = yPosition;
		}
    }

}
