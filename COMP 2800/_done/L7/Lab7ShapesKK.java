/* Copyright material for the convenience of GA/TAs to help students working on Lab Exercises,
 * but NOT to be shown or distributed to the students. */

package codesKK280;

import java.awt.Font;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.*;

/* an abstract super class for create different objects */
public abstract class Lab7ShapesKK {
	protected TransformGroup objTG = new TransformGroup(); // use 'objTG' to position an object

	protected abstract Node create_Object();               // allow derived classes to create different objects
	
	public TransformGroup position_Object() {	           // retrieve 'objTG' to which 'obj_shape' is attached
		return objTG;   
	}
	
	protected Appearance app;                              // allow each object to define its own appearance
	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // A3: attach the next transformGroup to 'objTG'
	}
}

class AxesShape extends Lab7ShapesKK {					// (Lab1) creates coordinate axes x, y, and z
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
	public TransformGroup position_Object() {
		objTG = new TransformGroup();
		objTG.addChild(create_Object());
		return objTG;
	}
}

class BaseShape extends Lab7ShapesKK {
	public BaseShape() {
		Transform3D translator = new Transform3D();
		translator.setTranslation(new Vector3d(0.0, -0.54, 0));
		objTG = new TransformGroup(translator);            // down half of the tower and base's heights

		objTG.addChild(create_Object());                   // attach the object to 'objTG'
	}
	
	protected Node create_Object() {
		app = CommonsKK.obj_Appearance(CommonsKK.White);   // set the appearance for the base
		app.setTexture(textured_App("MarbleTexture"));     // set texture for the base
		TransparencyAttributes ta =                        // value: FASTEST NICEST SCREEN_DOOR BLENDED NONE
				new TransparencyAttributes(TransparencyAttributes.SCREEN_DOOR, 0.5f);
		app.setTransparencyAttributes(ta);                 // set transparency for the base
		return new Box(0.5f, 0.04f, 0.5f, Box.GENERATE_NORMALS | Box.GENERATE_TEXTURE_COORDS, app);
	}
	
	private static Texture textured_App(String name) {
		String filename = "src/codesKK280/images/" + name + ".jpg";       // tell the folder of the image
		TextureLoader loader = new TextureLoader(filename, null);
		ImageComponent2D image = loader.getImage();        // load the image
		if (image == null)
			System.out.println("Cannot load file: " + filename);

		Texture2D texture = new Texture2D(Texture.BASE_LEVEL,
				Texture.RGBA, image.getWidth(), image.getHeight());
		texture.setImage(0, image);                        // set image for the texture

		return texture;
	}
}

/* a derived class to create a string label and place it to the bottom of the self-made cone */
class ColorString extends Lab7ShapesKK {
	String str;
	Color3f clr;
	double scl;
	Point3f pos;                                           // make the label adjustable with parameters
	public ColorString(String str_ltrs, Color3f str_clr, double s, Point3f p) {
		str = str_ltrs;	
		clr = str_clr;
		scl = s;
		pos = p;

		Transform3D scaler = new Transform3D();
		scaler.setScale(scl);                              // scaling 4x4 matrix 
		Transform3D rotator = new Transform3D();           // 4x4 matrix for rotation
		rotator.rotY(Math.PI);
		Transform3D trfm = new Transform3D();              // 4x4 matrix for composition
		trfm.mul(rotator);                                 // apply rotation second
		trfm.mul(scaler);                                  // apply scaling first
		objTG = new TransformGroup(trfm);                  // set the combined transformation
		objTG.addChild(create_Object());                   // attach the object to 'objTG'		
	}
	protected Node create_Object() {
		Font my2DFont = new Font("Arial", Font.PLAIN, 1);  // font's name, style, size
		FontExtrusion myExtrude = new FontExtrusion();
		Font3D font3D = new Font3D(my2DFont, myExtrude);	
		Text3D text3D = new Text3D(font3D, str, pos);      // create 'text3D' for 'str' at position of 'pos'
		
		Appearance app = CommonsKK.obj_Appearance(clr);    // use appearance to specify the string color
		return new Shape3D(text3D, app);                   // return a string label with the appearance
	}
}