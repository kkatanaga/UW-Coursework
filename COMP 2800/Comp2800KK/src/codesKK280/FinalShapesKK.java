package codesKK280;

/* Copyright material for students taking COMP-2800 to work on assignment/labs/projects/exam. */

import java.io.FileNotFoundException;

import org.jogamp.java3d.Alpha;
import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.PolygonAttributes;
import org.jogamp.java3d.PositionInterpolator;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.TransparencyAttributes;
import org.jogamp.java3d.loaders.IncorrectFormatException;
import org.jogamp.java3d.loaders.ParsingErrorException;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Vector3d;

/* a super class for the definition of external objects */
public abstract class FinalShapesKK {
	private BranchGroup objBG;                             // load external object to 'objBG'
	private TransformGroup objTG;                          // use 'objTG' to resize an object
	private TransformGroup objPG;							// use 'rotTG' to contain position interpolator
	protected Vector3d scale;                              // use it to change the size of objects
	private Shape3D obj_shape;
	
	private Appearance normlApp;							// Appearance without transparency
	private Appearance clearApp;							// Appearance with transparency
	private boolean clear = false;							// true if object is transparent
	
	private Alpha alpha;									// Use to enable/disable movement
	
	/* a function to attach the current object to 'objTG' and return 'objTG' */ 
	public TransformGroup position_Object() {
		return objPG;
	}

    /* a function to set 'objTG' and load external object to 'objBG' with appearance setting */
	protected void load_Object(String obj_name, Color3f obj_clr) {
		Transform3D scaler = new Transform3D();
		scaler.setScale(scale);
		objPG = new TransformGroup();						// objPG will contain position interpolator and objTG
		objTG = new TransformGroup(scaler);
		objBG = load_Shape(obj_name);                      	// load external object to 'objBG'
		
		objPG.addChild(objTG);								// objPG will rotate objTG
		objTG.addChild(objBG);								// objTG will scale objBG
		
		obj_shape = (Shape3D) objBG.getChild(0);   			// convert to Shape3D to set object's appearance
		normlApp = CommonsKK.obj_Appearance(obj_clr);		// set opaque appearance
		
		clearApp = CommonsKK.obj_Appearance(obj_clr);		// prepare transparent appearance
		TransparencyAttributes ta = new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.3f);	// 30% transparency
		clearApp.setTransparencyAttributes(ta);				// set to transparent
		PolygonAttributes pa = new PolygonAttributes();
		pa.setCullFace(PolygonAttributes.CULL_BACK);
		clearApp.setPolygonAttributes(pa);					// adjust cull face
		
		if (obj_name.equals("cup_base")) {					// cup base uses transparent appearance by default
			obj_shape.setAppearance(clearApp);				// set default appearance as transparent
		}
		else {
			obj_shape.setAppearance(normlApp);				// set default appearance as opaque
			if (!obj_name.equals("cup_wall0"))
				move_Object();								// add movement for all walls except wall0 (innermost wall)
		}
		obj_shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
	}

	/* a function to load and return object shape from the file named 'obj_name' */
	private BranchGroup load_Shape(String obj_name) {
		ObjectFile f = new ObjectFile(ObjectFile.RESIZE, (float) (60 * Math.PI / 180.0));
		Scene s = null;
		try {                                              // load object's definition file to 's'
			s = f.load("src/codesKK280/objects/" + obj_name + ".obj");
		} catch (FileNotFoundException e) {
			System.err.println(e);
			System.exit(1);
		} catch (ParsingErrorException e) {
			System.err.println(e);
			System.exit(1);
		} catch (IncorrectFormatException e) {
			System.err.println(e);
			System.exit(1);
		}
		return s.getSceneGroup();
	}
	
	private void move_Object() {
		objPG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		alpha = new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE, 0, 0, 4000, 2000, 1000, 4000, 2000, 1000);		// save alpha in the class
		
		Transform3D axisPosition = new Transform3D();
		axisPosition.rotZ(Math.PI / 2.0);
		
		PositionInterpolator posInterpol = new PositionInterpolator(alpha, objPG, axisPosition, 0f, 0.375f);
		posInterpol.setSchedulingBounds(CommonsKK.hundredBS);
		
		objPG.addChild(posInterpol);
	}
	
	public boolean flip_Alpha() {
		if (alpha.value() < 1.0)							// don't flip when alpha is not at its highest
			return false;									// flip failed
		
		if (alpha.isPaused()) alpha.resume();				// move if paused
		else alpha.pause();									// pause if moving
		
		return true;										// flip success
	}
	
	public void flip_Transparent() {
		if (clear) obj_shape.setAppearance(normlApp);		// set to opaque if transparent
		else obj_shape.setAppearance(clearApp);				// set to transparent if opaque
		clear = !clear;										// flip boolean
	}
	
	public boolean isClear() {
		return clear;
	}
	public boolean isMoving() {
		return !alpha.isPaused();
	}
}

class Cup {
	private FinalShapesKK[] walls;
	private TransformGroup rootTG;
	private boolean baseClear = false;
	
	public Cup() {
		walls = new FinalShapesKK[5];
		TransformGroup[] tgs = new TransformGroup[5];
		walls[0] = new CupWall0(); 
		walls[1] = new CupWall1(); 
		walls[2] = new CupWall2();  
		walls[3] = new CupWall3(); 
		walls[4] = new CupWall4();
		
		for (int i = 0; i < 5; ++i) {
			tgs[i] = walls[i].position_Object();
			if (i > 0)
				tgs[i-1].addChild(tgs[i]);				// Set nested transform groups (i.e., wall0 is a parent of wall1, wall1 is a parent of wall2, etc.)
		}
		
		Transform3D trfm = new Transform3D();
		trfm.setTranslation(new Vector3d(0, -0.5, 0));	// Set cups lower
		rootTG = new TransformGroup(trfm);
		rootTG.addChild(tgs[0]);
	}
	public TransformGroup getTG() {
		return rootTG;
	}
	public void flip1() {
		if (walls[1].isMoving()) {					// Lock out any movement/transparency modifications if the wall is stopped
			if (!walls[1].flip_Alpha())	return;		// If moving, stop only if extended completely. If stopped, move.
			walls[1].flip_Transparent();			// If transparent, set to non-transparent. If non-transparent, set to transparent.
		}
		checkTransparency();						// Check if all except the base are transparent
	}
	public void flip2() {
		if (walls[2].isMoving()) {
			if (!walls[2].flip_Alpha()) return;
			walls[2].flip_Transparent();
		}
		checkTransparency();
	}
	public void flip3() {
		if (walls[3].isMoving()) {
			if (!walls[3].flip_Alpha()) return;
			walls[3].flip_Transparent();
		}
		checkTransparency();
	}
	public void flip4() {
		if (walls[4].isMoving()) {
			if (!walls[4].flip_Alpha()) return;
			walls[4].flip_Transparent();
		}
		checkTransparency();
	}
	
	private void checkTransparency() {
		if (baseClear) {													// if base is transparent
			for (int i = 0; i < 5; ++i) {
				if (i > 0 && !walls[i].isMoving()) walls[i].flip_Alpha();	// set the wall moving unless it's the base or is already moving
				if (walls[i].isClear()) walls[i].flip_Transparent();		// set the wall opaque unless it's already opaque
			}
			baseClear = false;												// base is opaque
			return;
		} 
		
		boolean setTransparency = true;
		for (int i = 1; i < 5; ++i) {
			setTransparency = setTransparency && walls[i].isClear();		// false if at least 1 of the walls is opaque; true if all walls are transparent
		}
		if (setTransparency) {												// if we need to set base as transparent
			walls[0].flip_Transparent();
			baseClear = true;												// base is clear
		}
	}
}

/* a derived class for the cup's base object */
class CupBase extends FinalShapesKK {
	public CupBase() {
		scale = new Vector3d(0.8, 0.8, 0.8);               // set scaling for transformation
		load_Object("cup_base", new Color3f(0.35f, 0.35f, 0.35f)); // light grey
	}
}

/* a derived class for the innermost segment of cup wells */
class CupWall0 extends FinalShapesKK {
	public CupWall0() {
		scale = new Vector3d(0.42, 0.42, 0.42);            // set scaling for transformation
		load_Object("cup_wall0", new Color3f(0.35f, 0.0f, 0.35f)); // light magenta
	}
}

/* a derived class for the 1st segment of cup wells */
class CupWall1 extends FinalShapesKK {
	public CupWall1() {
		scale = new Vector3d(0.4662, 0.4662, 0.4662);      // set scaling for transformation
		load_Object("cup_wall1", new Color3f(0.35f, 0.15f, 0.0f)); // light orange
	}
}

/* a derived class for the 2nd segment of cup wells */
class CupWall2 extends FinalShapesKK {
	public CupWall2() {
		scale = new Vector3d(0.5175, 0.5175, 0.5175);      // set scaling for transformation
		load_Object("cup_wall2", new Color3f(0.35f, 0.35f, 0.0f)); // light yellow
	}
}

/* a derived class for the 3rd segment of cup wells */
class CupWall3 extends FinalShapesKK {
	public CupWall3() {
		scale = new Vector3d(0.5744, 0.5744, 0.5744);      // set scaling for transformation
		load_Object("cup_wall3", new Color3f(0.0f, 0.35f, 0.0f)); // light green
	}
}

/* a derived class for the 4th segment of cup wells */
class CupWall4 extends FinalShapesKK {
	public CupWall4() {
		scale = new Vector3d(0.6376, 0.6376, 0.6376);      // set scaling for transformation
		load_Object("cup_wall4", new Color3f(0.0f, 0.0f, 0.35f)); // light blue
	}
}