package transformer; 
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import constants.GEConstants;
import constants.GEConstants.EAnchorTypes;
import shapes.GEShape; 

public class GEResizer extends GETransformer{  
	private Point resizeAnchor;
	private Point previousP;	
	
	// GEResizer의 생성자
	public GEResizer(GEShape shape){ 
		super(shape);  
		previousP = new Point(); 
	} 

	// 초기 지점을 지정
	public void init(Point p) {  
		previousP = p;
		resizeAnchor = getResizeAnchor(); 
		shape.moveReverse(resizeAnchor); 
	}  

	// 크기를 조정인 이후 다시 그림
	public void transformer(Graphics2D g2D, Point p) {  
		g2D.setXORMode(GEConstants.BACKGROUND_COLOR ); 
		g2D.setStroke(dashedLineStroke);  
		Point2D resizeFactor = computeResizeFactor(previousP, p); 
		AffineTransform tempAffine = g2D.getTransform();  
		g2D.translate(resizeAnchor.getX(), resizeAnchor.getY());  
		shape.draw(g2D);  
		shape.resizeCoordinate(resizeFactor);
		shape.draw(g2D); 	
		g2D.setTransform(tempAffine); 
		previousP = p; 
	}  

	// 그림의 크기를 조정함
	private Double computeResizeFactor(Point previousP, Point currentP){ 
		double deltaW = 0;   
		double deltaH = 0; 

		if (shape.getSelectedAnchor() == EAnchorTypes. NW ) {   
			deltaW=-(currentP.x-previousP.x);     
			deltaH=-(currentP.y-previousP.y);  
		} 
		else if (shape.getSelectedAnchor() == EAnchorTypes. NN ) {  
			deltaW=  0;      
			deltaH=-(currentP.y-previousP.y);  
		} 

		else if (shape.getSelectedAnchor() == EAnchorTypes. NE ) {  
			deltaW=  currentP.x-previousP.x;    
			deltaH=-(currentP.y-previousP.y);  
		}
		else if (shape.getSelectedAnchor() == EAnchorTypes. WW ) {
			deltaW=-(currentP.x-previousP.x);    
			deltaH=  0;   
		}
		else if (shape.getSelectedAnchor() == EAnchorTypes. EE ) {   
			deltaW=  currentP.x-previousP.x;   
			deltaH=  0;   
		}
		else if (shape.getSelectedAnchor() == EAnchorTypes. SW ) {  
			deltaW=-(currentP.x-previousP.x);   
			deltaH=  currentP.y-previousP.y;   
		} 
		else if (shape.getSelectedAnchor() == EAnchorTypes. SS ) { 
			deltaW=  0;     
			deltaH=  currentP.y-previousP.y; 
		} else if (shape.getSelectedAnchor() == EAnchorTypes. SE ) {  
			deltaW=  currentP.x-previousP.x;    
			deltaH=  currentP.y-previousP.y; 
		}  
		double currentW = shape.getBounds().getWidth();
		double currentH = shape.getBounds().getHeight(); 
		double xFactor = 1.0;   

		if (currentW > 0.0)  
			xFactor = (1.0+deltaW/currentW);

		double yFactor = 1.0;   

		if (currentH > 0.0)   
			yFactor = (1.0+deltaH/currentH);

		return new Point.Double(xFactor, yFactor);  
	}  

	// 그림을 얼마나 움직여야 할지를 계산함
	private Point getResizeAnchor() {  
		Point resizeAnchor = new Point();
		if (shape.getSelectedAnchor() == EAnchorTypes. NW ) { 
			resizeAnchor.setLocation((shape.getAnchorList().getAnchors().get(EAnchorTypes. SE .ordinal())).getX(),      
					(shape.getAnchorList().getAnchors().get(EAnchorTypes. SE .ordinal())).getY()); 
		} else if (shape.getSelectedAnchor() == EAnchorTypes. NN ) { 
			resizeAnchor.setLocation(0, (shape.getAnchorList().getAnchors().get(EAnchorTypes. SS .ordinal())).getY()); 
		} else if (shape.getSelectedAnchor() == EAnchorTypes. NE ) {  
			resizeAnchor.setLocation((shape.getAnchorList().getAnchors().get(     
					EAnchorTypes. SW .ordinal())).getX(),(shape.getAnchorList().getAnchors().get(EAnchorTypes. SW .ordinal())).getY());  
		} else if (shape.getSelectedAnchor() == EAnchorTypes. WW ) {  
			resizeAnchor.setLocation((shape.getAnchorList().getAnchors().get(EAnchorTypes. EE .ordinal())).getX(),0); 
		} else if (shape.getSelectedAnchor() == EAnchorTypes. EE ) {  
			resizeAnchor.setLocation(( shape.getAnchorList().getAnchors().get(EAnchorTypes. WW .ordinal())).getX(), 0);  
		} else if (shape.getSelectedAnchor() == EAnchorTypes. SW ) {   
			resizeAnchor.setLocation(( shape.getAnchorList().getAnchors().get(EAnchorTypes. NE .ordinal())).getX(),
					( shape.getAnchorList().getAnchors().get(EAnchorTypes. NE .ordinal())).getY());   
		} else if (shape.getSelectedAnchor() == EAnchorTypes. SS ) {   
			resizeAnchor.setLocation(0, (shape.getAnchorList().getAnchors().get(EAnchorTypes. NN .ordinal())).getY());   
		} else if (shape.getSelectedAnchor() == EAnchorTypes. SE ) { 
			resizeAnchor.setLocation(( shape.getAnchorList().getAnchors().get(    
					EAnchorTypes. NW .ordinal())).getX(),(shape.getAnchorList().getAnchors().get(EAnchorTypes. NW .ordinal())).getY()); 
		}   
		return resizeAnchor;  
	} 
	
	// 행렬의 기본행연산을 통해 해당 도형의 크기를 조절함
	public void finalize(Point point) { 
		shape.move(resizeAnchor);  
	}  					
} 