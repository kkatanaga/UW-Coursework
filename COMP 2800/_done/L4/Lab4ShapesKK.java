/* Copyright material for students taking COMP-2800 to work on assignment/labs/projects. */

package codesKK280;

import java.awt.Font;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.*;

/* List of adjustable numbers. Adjustable means manually changing the value code for a different result before running
 * TriangleConeShape->n (not N)
 * 		- sets the number of sides of the pyramid. Higher number means more circular cone
 * AxesShape->UNIT 
 * 		- changes the length of the 3 coordinate axes */

public abstract class Lab4ShapesKK {
	protected abstract Node create_Object();			// use 'Node' for both Group and Shape3D
	public abstract Node position_Object();
}

//===============/!\ Lab 3 shapes /!\===============
class TowerShape extends Lab4ShapesKK {
	protected Node create_Object() {
		return new Cylinder(0.12f, 1.0f, Primitive.GENERATE_NORMALS, 30, 30, CommonsKK.obj_Appearance(CommonsKK.Orange));
	}
	public Node position_Object() {
		return create_Object();
	}
}

class YawDriveShape extends Lab4ShapesKK {
	 private TransformGroup objTG;
	 public YawDriveShape() {
		 Transform3D translate = new Transform3D();
		 translate.setTranslation(new Vector3f(0f, 0.5f, 0f));	// move center of sphere to the top of the tower which leaves half of the sphere exposed
		 objTG = new TransformGroup(translate);
		 
		 objTG.addChild(create_Object());
	 }
	 protected Node create_Object() {
		return new Sphere(0.12f, Primitive.GENERATE_NORMALS, CommonsKK.obj_Appearance(CommonsKK.Red));
	}
	public Node position_Object() {
		return objTG;
	}
 }
 
class NacelleShape extends Lab4ShapesKK {
	 private static float boxHalfLength = 0.26f;
	 private TransformGroup objTG;
	 public NacelleShape() {
		 Transform3D translate = new Transform3D();					
		 translate.setTranslation(new Vector3f(boxHalfLength-0.12f, 0.68f, 0f));	// x = half of nacelle length - yaw drive radius
		 objTG = new TransformGroup(translate);										// y = origin to top of cylinder + radius of yaw drive + half of nacelle height
		 
		 objTG.addChild(create_Object());
	 }
	 protected Node create_Object() {
		return new Box(boxHalfLength, 0.06f, 0.12f, Primitive.GENERATE_NORMALS, CommonsKK.obj_Appearance(CommonsKK.Cyan));	// 0.52 x 0.12 x 0.24 box
	}
	public Node position_Object() {
		return objTG;
	}
}
// ===========/!\ End of Lab 3 shapes /!\============

// ===============/!\ Lab 4 shapes /!\===============
class RotorShape extends Lab4ShapesKK {
	 private TransformGroup objTG;
	 public RotorShape() {
		 Transform3D translate = new Transform3D();
		 translate.setTranslation(new Vector3f(0.40f, 0.68f, 0f));	// x = full nacelle length - yaw drive radius
		 															// y = half of tower height + yaw drive radius + half of nacelle height
		 objTG = new TransformGroup(translate);
		 
		 objTG.addChild(create_Object());
	 }
	 protected Node create_Object() {
		return new Sphere(0.06f, Primitive.GENERATE_NORMALS, CommonsKK.obj_Appearance(CommonsKK.Red));
	}
	public Node position_Object() {
		return objTG;
	}
}

class BladeShape extends Lab4ShapesKK {
	 private TransformGroup objTG;
	 public BladeShape() {
		 Transform3D translate = new Transform3D();
		 translate.setTranslation(new Vector3f(0.46f, 0.68f, 0f));	// x = (full nacelle length - yaw drive radius) + rotor radius
		 															// y = origin to top of tower + radius of yaw drive + half of nacelle height
		 objTG = new TransformGroup(translate);
		 
		 objTG.addChild(create_Object());
	 }
	 protected Node create_Object() {
		return new Box(0.01f, 0.06f, 0.5f, Primitive.GENERATE_NORMALS, CommonsKK.obj_Appearance(CommonsKK.Magenta));	// 0.02 x 0.12 x 1.0 box
	}
	public Node position_Object() {
		return objTG;
	}
}

class FoundationShape extends Lab4ShapesKK {
	 private TransformGroup objTG;
	 public FoundationShape() {
		 Transform3D translate = new Transform3D();
		 translate.setTranslation(new Vector3f(0f, -0.54f, 0f));	// y = -(origin to bottom of tower + half of foundation height)
		 objTG = new TransformGroup(translate);
		 
		 objTG.addChild(create_Object());
	 }
	 protected Node create_Object() {
		 Appearance app = new Appearance();
		 app.setTexture(texturedApp());
		 return new Box(0.5f, 0.04f, 0.5f, Primitive.GENERATE_TEXTURE_COORDS, app);	// 1.0 x 0.08 x 1.0 box
	}
	public Node position_Object() {
		return objTG;
	}
	public Texture texturedApp() {
		String filename = "src/codesKK280/images/eyebrow.jpg";
		TextureLoader loader = new TextureLoader(filename, null);
		ImageComponent2D image = loader.getImage();
		if (image == null)
			System.out.println("Cannot open file: " + filename);
		
		Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, image.getWidth(), image.getHeight());
		texture.setImage(0, image);
		return texture;
	}
}
// ===========/!\ End of Lab 4 shapes /!\============

class AxesShape extends Lab4ShapesKK {					// (Lab1) creates coordinate axes x, y, and z
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

class StringShape extends Lab4ShapesKK {
	private TransformGroup objTG;                              		// use 'objTG' to position an object
	private String str;
	public StringShape(String str_ltrs) {
		str = str_ltrs;		
		Transform3D scaler = new Transform3D();
		scaler.setScale(0.11);                              // scaling 4x4 matrix 
		
		Transform3D rotator = new Transform3D();
		rotator.rotY(Math.PI);										// rotate string by 180 degrees
		
		Transform3D trfm = new Transform3D();
		trfm.setTranslation(new Vector3f(0.14f, 0.64f, -0.12f));	// x = half of nacelle length - yaw drive radius; same as nacelle position
																	// y = distance from origin to tower top + yaw drive radius + 0.02f
																	// z = half of nacelle width
		trfm.mul(rotator);
		trfm.mul(scaler);
		objTG = new TransformGroup(trfm);
		objTG.addChild(create_Object());		   					// apply scaling to change the string's size and position
	}
	protected Node create_Object() {
		Font my2DFont = new Font("Arial", Font.PLAIN, 1);  // font's name, style, size
		FontExtrusion myExtrude = new FontExtrusion();
		Font3D font3D = new Font3D(my2DFont, myExtrude);

		Point3f pos = new Point3f(-str.length()/4f, 0, 0f);// initial position for the string
		Text3D text3D = new Text3D(font3D, str, pos);      // create a text3D object
		return new Shape3D(text3D, CommonsKK.obj_Appearance(CommonsKK.White));	// /!\ (Lab2) added obj_Appearance() so text doesn't use pyramid base color
	}
	public Node position_Object() {
		return objTG;
	}
}
