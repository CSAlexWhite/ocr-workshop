package alexwhitecs.ocr;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class LoadImage extends Component{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -575720947178530668L;	
	
	BufferedImage img;
	
	public void paint(Graphics g){
		
		g.drawImage(img,  0, 0, null);
	}

	public LoadImage(String filename){
		try{
			img = ImageIO.read(new File(filename));
			
		} catch (IOException e){
			
		}
	}
	
	public LoadImage(BufferedImage inImage){
		
		img = inImage;
	}
	
	public Dimension getPreferredSize(){
		if(img == null){
			return new Dimension(100, 100);
		}
		
		else return new Dimension(img.getWidth(null), img.getHeight(null));	
	}
}
