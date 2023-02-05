/* Copyright material for students taking COMP-2800 to work on assignment/labs/projects. */

package codesKK280;

import java.awt.Font;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Cone;
import org.jogamp.vecmath.*;

/* List of adjustable numbers. Adjustable means manually changing the value code for a different result before running
 * AxesShape->UNIT 
 * 		- changes the length of the 3 coordinate axes
 * NCircleShape->N 
 * 		- sets the number of vertices and sides of the polygon (higher number makes it circular) */

public abstract class Lab1ShapesKK {
	protected abstract Node create_Object();			// use 'Node' for both Group and Shape3D
	public abstract Node position_Object();
}

class AxesShape extends Lab1ShapesKK {
	private static final int UNIT = 1;					// length of axes
	
	protected Node create_Object() {
		final int AXIS_COUNT = 3;						// number of axes in a 3D space
		
		Point3f axis[] = new Point3f[AXIS_COUNT];       // declare 3 points for coordinate axes
		LineArray lineArr = new LineArray(AXIS_COUNT * 2, LineArray.COLOR_3 | LineArray.COORDINATES);	// double the number of vertices for tail & head
		
		axis[0] = new Point3f(UNIT, 0, 0);				// x coordinate
		axis[1] = new Point3f(0, UNIT, 0);				// y coordinate
		axis[2] = new Point3f(0, 0, UNIT);				// z coordinate
		
		Color3f color[] = new Color3f[AXIS_COUNT];
		color[0] = CommonsKK.Red;						// x axis color
		color[1] = CommonsKK.Green;						// y axis color
		color[2] = CommonsKK.Blue;						// z axis color
		
		for (int i = 0; i < AXIS_COUNT; i++) {
			lineArr.setCoordinate(i * 2 + 1, axis[i]);  // tail coordinate; head is at (0,0,0) by default
			lineArr.setColor(i * 2, color[i]);        	// tail color
			lineArr.setColor(i * 2 + 1, color[i]);		// head color
		}
		
		return new Shape3D(lineArr);                    // create and return a Shape3D
	}
	public Node position_Object() {
		return create_Object();
	}
}

class NCircleShape extends Lab1ShapesKK {
	private static final int N = 15;						// N points for the circle (N-polygon)
	
	protected Node create_Object() {
		float r = 0.6f;										// radius of circle
		float x, y;                              			// vertex coordinates (z is constant)
		float centralAngle = 360.0f / N;					// angle from the center to 1 side
		
		Point3f vertex[] = new Point3f[N];                  // declare N vertices for circular shape
		LineArray lineArr = new LineArray(N * 2, LineArray.COLOR_3 | LineArray.COORDINATES);	// double the number of vertices for tail & head
		
		for (int i = 0; i < N; i++) {
			x = (float) Math.cos(Math.PI / 180 * (90 + centralAngle * i) ) * r;		// x coordinate
			y = (float) Math.sin(Math.PI / 180 * (90 + centralAngle * i) ) * r;		// y coordinate
			vertex[i] = new Point3f(x, y, -0.6f);            // vertex constructed with x and y, with z = -0.6f
		}
		
		for (int i = 0; i < N; i++) {
			lineArr.setCoordinate(i * 2, vertex[i]);         		// tail coordinate
			lineArr.setCoordinate(i * 2 + 1, vertex[(i + 1) % N]);	// head coordinate
			lineArr.setColor(i * 2, CommonsKK.Red);        			// tail color
			lineArr.setColor(i * 2 + 1, CommonsKK.Green);			// head color
		}
		
		return new Shape3D(lineArr);                        // create and return a Shape3D
	}
	public Node position_Object() {
		return create_Object();
	}
}

class StarShape extends Lab1ShapesKK {
	protected Node create_Object() {
		float r = 0.6f, x, y;                              // vertices at 0.6 away from origin
		Point3f coor[] = new Point3f[5];                   // declare 5 points for star shape
		LineArray lineArr = new LineArray(10, LineArray.COLOR_3 | LineArray.COORDINATES);
		for (int i = 0; i <= 4; i++) {                     // define coordinates for star shape
			x = (float) Math.cos(Math.PI / 180 * (90 + 72 * i)) * r;
			y = (float) Math.sin(Math.PI / 180 * (90 + 72 * i)) * r;
			coor[i] = new Point3f(x, y, -0.6f);            // use z-value to position star shape
		}
		for (int i = 0; i <= 4; i++) {
			lineArr.setCoordinate(i * 2, coor[i]);         // define point pairs for each line
			lineArr.setCoordinate(i * 2 + 1, coor[(i + 2) % 5]);
			lineArr.setColor(i * 2, CommonsKK.Red);        // specify color for each pair of points
			lineArr.setColor(i * 2 + 1, CommonsKK.Green);
		}
		return new Shape3D(lineArr);                        // create and return a Shape3D
	}
	public Node position_Object() {
		return create_Object();
	}
}

class ConeShape extends Lab1ShapesKK {
	private TransformGroup objTG;                          // use 'objTG' to position an object
	public ConeShape() {
		Transform3D translator = new Transform3D();        // 4x4 matrix for translation
		translator.setTranslation(new Vector3f(0f, 0f, 0.3f));
		Transform3D rotator = new Transform3D();           // 4x4 matrix for rotation
		rotator.rotX(Math.PI / -2);
		Transform3D trfm = new Transform3D();              // 4x4 matrix for composition
		trfm.mul(translator);                              // apply translation next
		trfm.mul(rotator);                                 // apply rotation first
		objTG = new TransformGroup(trfm);                  // set the combined transformation
		
		objTG.addChild(create_Object());
	}
	protected Node create_Object() {
		return new Cone(0.6f, 0.6f, CommonsKK.obj_Appearance(CommonsKK.Orange));
	}
	public Node position_Object() {
		return objTG;
	}
}

class StringShape extends Lab1ShapesKK {
	private TransformGroup objTG;                              // use 'objTG' to position an object
	private String str;
	public StringShape(String str_ltrs) {
		str = str_ltrs;		
		Transform3D scaler = new Transform3D();
		scaler.setScale(0.2);                              // scaling 4x4 matrix 
		objTG = new TransformGroup(scaler);
		objTG.addChild(create_Object());		   // apply scaling to change the string's size
	}
	protected Node create_Object() {
		Font my2DFont = new Font("Arial", Font.PLAIN, 1);  // font's name, style, size
		FontExtrusion myExtrude = new FontExtrusion();
		Font3D font3D = new Font3D(my2DFont, myExtrude);		

		Point3f pos = new Point3f(-str.length()/4f, 0, 3f);// position for the string 
		Text3D text3D = new Text3D(font3D, str, pos);      // create a text3D object
		return new Shape3D(text3D);
	}
	public Node position_Object() {
		return objTG;
	}
}
