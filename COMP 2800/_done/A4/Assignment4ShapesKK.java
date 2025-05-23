/* Copyright material for students taking COMP-2800 to work on assignment/labs/projects. */

package codesKK280;

import java.awt.Font;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.*;

/* List of adjustable numbers. Adjustable means manually changing the value code for a different result before running
 * AxesShape->UNIT 
 * 		- changes the length of the 3 coordinate axes */

public abstract class Assignment4ShapesKK {
	protected abstract Node create_Object();									// use 'Node' for both Group and Shape3D
	public abstract Node position_Object();
}

class BaseShape extends Assignment4ShapesKK {
	private TransformGroup baseTG;
	private Color3f baseColor;
	public BaseShape(Color3f baseColor) {
		this.baseColor = baseColor;
		baseTG = (TransformGroup) create_Object();
	}
	protected Node create_Object() {
		TransformGroup newTG = new CylinderShapes(baseColor).position_Object();					// creates 4 cylinder shapes with transformation as children of baseTG
		Appearance appearance = CommonsKK.obj_Appearance(baseColor);							// color of boxes; same as cylinders
		newTG.addChild(new Box(0.5f, 0.1f, 1.0f, Primitive.GENERATE_NORMALS, appearance));		// 1 x 0.2 x 2 box
		newTG.addChild(new Box(1.0f, 0.1f, 0.5f, Primitive.GENERATE_NORMALS, appearance));		// 2 x 0.2 x 1 box
		newTG.addChild(new StringShape("KK's Assignment 4").position_Object());	// stringTG
		return newTG;
	}
	public Node position_Object() {
		return baseTG;
	}
}

class ChangeBackground extends Assignment4ShapesKK {
	protected Node create_Object() {
		Background bg = new Background();
		BoundingSphere bound = new BoundingSphere(new Point3d(0f, 0f, 0f), Double.MAX_VALUE);
		bg.setImage(new TextureLoader("src/codesKK280/images/room.jpg", null).getImage());
		bg.setImageScaleMode(Background.SCALE_FIT_MAX);
		bg.setApplicationBounds(bound);
		return bg;
	}
	public Node position_Object() {
		return create_Object();
	}
	
}

class CylinderShapes extends Assignment4ShapesKK {								// creates 4 cylinders using a shared group
	private TransformGroup objTG;                          						// use 'objTG' to position an object
	private Color3f color;
	public CylinderShapes(Color3f clr) {
		Transform3D trfm = new Transform3D();									// prepare location of baseTG
		trfm.setScale(new Vector3d(1.0f, 1.0f, 0.25f));							// scales base by 1/4 in the z dimension
		Transform3D translator = new Transform3D();
		translator.setTranslation(new Vector3f(0.0f, -1.166f, 0.0f));				// moves base to below the ring shape
		trfm.mul(translator);													// combines scale and translation
		
		color = clr;
		objTG = new TransformGroup(trfm);
		Vector3f[] pos = {new Vector3f(-0.5f, 0f, 0.5f), new Vector3f(0.5f, 0f, 0.5f),
						  new Vector3f(-0.5f, 0f, -0.5f), new Vector3f(0.5f, 0f, -0.5f)};
		SharedGroup cyldSG = new SharedGroup();
		cyldSG.addChild(create_Object());
		cyldSG.compile();
		
		for (int i = 0; i < 4; ++i) {
			objTG.addChild(linked3D(pos[i], new Link(cyldSG)));					// links an instance of cylinder to 1 of 4 positions
		}
	}
	private TransformGroup linked3D(Vector3f pos, Link link) {
		Transform3D position = new Transform3D();
		position.setTranslation(pos);
		TransformGroup posTG = new TransformGroup(position);
		posTG.addChild(link);
		return posTG;
	}
	protected Cylinder create_Object() {
		Appearance appearance = CommonsKK.obj_Appearance(color);		// cylinder color
		Cylinder cyl = new Cylinder(0.5f, 0.2f, Primitive.GENERATE_NORMALS | Primitive.ENABLE_APPEARANCE_MODIFY, 30, 30, appearance);
		return cyl;
	}
	public TransformGroup position_Object() {
		return objTG;
	}
}

class AxesShape extends Assignment4ShapesKK {				// copied from Lab1KK
	private static final int UNIT = 1;						// length of axes
	
	protected Node create_Object() {
		final int AXIS_COUNT = 3;							// number of axes in a 3D space
		
		final int flags = LineArray.COLOR_3 | LineArray.COORDINATES;
		Point3f axis[] = new Point3f[AXIS_COUNT];       	// declare 3 points for coordinate axes
		LineArray lineArr = new LineArray(AXIS_COUNT * 2, flags);	// double the number of vertices for tail & head
		
		axis[0] = new Point3f(UNIT, 0, 0);					// x coordinate
		axis[1] = new Point3f(0, UNIT, 0);					// y coordinate
		axis[2] = new Point3f(0, 0, UNIT);					// z coordinate
		
		Color3f color[] = new Color3f[AXIS_COUNT];
		color[0] = CommonsKK.Red;							// x axis color
		color[1] = CommonsKK.Green;							// y axis color
		color[2] = CommonsKK.Blue;							// z axis color
		
		for (int i = 0; i < AXIS_COUNT; i++) {
			lineArr.setCoordinate(i * 2 + 1, axis[i]);  	// tail coordinate; head is at (0,0,0) by default
			lineArr.setColor(i * 2, color[i]);        		// tail color
			lineArr.setColor(i * 2 + 1, color[i]);			// head color
		}
		
		return new Shape3D(lineArr);                    	// create and return a Shape3D
	}
	public Node position_Object() {
		return create_Object();
	}
}

class StringShape extends Assignment4ShapesKK {				// copied from Lab1KK; changed size, position, and orientation from original
	private final float scale = 0.15f;						// scale ratio of the string to add on top of baseTG's scale down
	
	private TransformGroup objTG;                           // use 'objTG' to position an object
	private String str;
	public StringShape(String str_ltrs) {
		str = str_ltrs;		
		Transform3D trfm = new Transform3D();
		trfm.setScale(scale);                              	// scaling 4x4 matrix
		Transform3D rotation = new Transform3D();
		rotation.rotY(Math.PI);								// rotates the text 180 degrees by the y-axis
		trfm.mul(rotation);
		objTG = new TransformGroup(trfm);					// apply scaling & rotation before string is created
		objTG.addChild(create_Object());		   			// string is translated after the previous transformation
	}
	protected Node create_Object() {
		Font my2DFont = new Font("Arial", Font.PLAIN, 1);  	// font's name, style, size
		FontExtrusion myExtrude = new FontExtrusion();
		Font3D font3D = new Font3D(my2DFont, myExtrude);		

		Point3f pos = new Point3f(-str.length()/4f, -(0.05f / scale), 1.0f / scale);	// position for the string
								// height is lowered a bit, and z-coordinate sits 1.0 (0.5 + 0.5) unit in front of base
		Text3D text3D = new Text3D(font3D, str, pos);      // create a text3D object
		return new Shape3D(text3D, CommonsKK.obj_Appearance(CommonsKK.Black));
	}
	public Node position_Object() {
		return objTG;
	}
}