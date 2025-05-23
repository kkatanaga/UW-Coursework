/* Copyright material for the convenience of students working on Lab Exercises */

package codesKK280;

import java.io.FileNotFoundException;

import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.*;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.vecmath.*;

public abstract class Lab8ObjectsKK {
	private Alpha rotationAlpha;                           			// NOTE: keep for future use
	protected BranchGroup objBG;                           			// load external object to 'objBG'
	protected TransformGroup objTG;                        			// use 'objTG' to position an object
	protected TransformGroup objRG;                        			// use 'objRG' to rotate an object
	protected double scale;                                			// use 'scale' to define scaling
	protected Vector3f post;                               			// use 'post' to specify location
	protected int shape_count;										// the number of parts (children) of the loaded object
	protected Material mtl;											// material to set the appearance

	public abstract TransformGroup position_Object();      			// need to be defined in derived classes
	public abstract void add_Child(TransformGroup nextTG);
	
	public Alpha get_Alpha() { return rotationAlpha; };    			// NOTE: keep for future use
	public void set_Alpha(Alpha newRotationAlpha) { rotationAlpha = newRotationAlpha; };

	/* a function to load and return object shape from the file named 'obj_name' */
	private Scene loadShape(String obj_name) {
		ObjectFile f = new ObjectFile(ObjectFile.RESIZE, (float) (60 * Math.PI / 180.0));
		Scene s = null;
		try {                                              			// load object's definition file to 's'
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
		return s;                                          			// return the object shape in 's'
	}
	
	/* function to set 'objTG' and attach object after loading the model from external file */
	protected void transform_Object(String obj_name) {
		Transform3D scaler = new Transform3D();
		scaler.setScale(scale);                            			// set scale for the 4x4 matrix
		scaler.setTranslation(post);                       			// set translations for the 4x4 matrix
		objTG = new TransformGroup(scaler);                			// set the translation BG with the 4x4 matrix
		objBG = loadShape(obj_name).getSceneGroup();       			// load external object to 'objBG'
		
		// /!\ Lab 6 edit /!\
		shape_count = objBG.numChildren();
		for (int i = 0; i < shape_count; ++i) {						// handles any number of children of objBG as long as objBG successfully loads the object
			objBG.getChild(i).setName(obj_name + i);				// use the object name + their index as a child of objBG to identify the object 
		}
		// /!\ End of Lab 6 edit /!\
	}
	
	protected Appearance app = new Appearance();
	private int shine = 32;                                			// specify common values for object's appearance
	protected Color3f[] mtl_clr = {new Color3f(1.000000f, 1.000000f, 1.000000f),
			new Color3f(0.772500f, 0.654900f, 0.000000f),	
			new Color3f(0.175000f, 0.175000f, 0.175000f),
			new Color3f(0.000000f, 0.000000f, 0.000000f)};
	
    /* a function to define object's material and use it to set object's appearance */
	protected void obj_Appearance() {		
		mtl = new Material();                     					// define material's attributes
		mtl.setShininess(shine);
		mtl.setAmbientColor(mtl_clr[0]);                   			// use them to define different materials
		mtl.setDiffuseColor(mtl_clr[1]);
		mtl.setSpecularColor(mtl_clr[2]);
		mtl.setEmissiveColor(mtl_clr[3]);                  			// use it to enlighten a button
		mtl.setLightingEnable(true);

		app.setMaterial(mtl);                              			// set appearance's material
		
		// /!\ Lab 6 edit /!\
		for (int i = 0; i < shape_count; ++i) {						// set every part of the object with the same color
			((Shape3D) objBG.getChild(i)).setAppearance(app);		// set object part's appearance
		}
		// /!\ End of Lab 6 edit /!\
	}
	
}
//===============/!\ Lab 7 shapes /!\===============
class ButtonObject extends Lab8ObjectsKK {
	public ButtonObject(String loc) {
		scale = 0.08d;                                     			// use to scale up/down original size
		post = new Vector3f(0f, -0.68f, -0.8f);            			// use to move object for positioning
		
		if (loc.equals("left"))
			post.add(new Vector3f(-0.125f, 0f, 0f));				// shift to the left of the fan
		else
			post.add(new Vector3f(0.175f, 0f, 0f));            		// shift to the right of the fan
		
		transform_Object("FanButton");                      		// set transformation to 'objTG' and load object file
		mtl_clr[1] = CommonsKK.Red;       							// set "FanButton" to a different color than the common  		                                              
		obj_Appearance();                                  			// set appearance after converting object node to Shape3D
	}
	
	public TransformGroup position_Object() {              			// attach object BranchGroup "FanButton" to 'objTG'
		Transform3D r_axis = new Transform3D();            			// default: rotate around Y-axis
		r_axis.rotY(Math.PI);                              			// rotate around y-axis for 180 degrees
		objRG = new TransformGroup(r_axis);                			// allow "FanButton" to rotate
		objTG.addChild(objRG);                             			// position "FanButton" by attaching 'objRG' to 'objTG'
		objRG.addChild(objBG);                             			// rotate "FanButton" by attaching 'objBG' to 'objRG'
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objRG.addChild(nextTG);                            			// attach the next transformGroup to 'objTG'
	}
}
//===========/!\ End of Lab 7 shapes /!\============

//===============/!\ Lab 6 shapes /!\===============
class ShaftObject extends Lab8ObjectsKK {
	public ShaftObject() {
		scale = 0.18d;                                     			// use to scale up/down original size
		post = new Vector3f(0f, 1.0f, 0.6f);               			// use to move object for positioning
		transform_Object("FanShaft");                      			// set transformation to 'objTG' and load object file
		mtl_clr[1] = new Color3f(0.3f, 0.15f, 0.3f);       			// set "FanShaft" to a different color than the common  		                                              
		obj_Appearance();                                  			// set appearance after converting object node to Shape3D
	}

	public TransformGroup position_Object() {              			// attach object BranchGroup "FanShaft" to 'objTG'
		Transform3D r_axis = new Transform3D();            			// default: rotate around Y-axis
		r_axis.rotY(Math.PI);                              			// rotate around y-axis for 180 degrees
		objRG = new TransformGroup(r_axis);                			// allow "FanShaft" to rotate
		objTG.addChild(objRG);                             			// position "FanShaft" by attaching 'objRG' to 'objTG'
		rot_Object(r_axis);											// objRG is referenced by and holds a rotation behavior
		objRG.addChild(objBG);                             			// rotate "FanShaft" by attaching 'objBG' to 'objRG'
		return objTG;                                      
	}

	/* a function that modifies objRG to be referenced by and hold a rotation behavior */
	public void rot_Object(Transform3D axis) {
		objRG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		Alpha newRotationAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE, 0, 0, 5000, 2500, 200, 5000, 2500, 200);
		set_Alpha(newRotationAlpha);
		
		float startAngle = (float) (Math.PI / 2.0);					// define where the fan motor starts rotating from
		float endAngle = (float) ((3.0 * Math.PI) / 2.0);			// define where the fan motor ends before returning
		RotationInterpolator rot_beh = new RotationInterpolator(newRotationAlpha, objRG, axis, startAngle, endAngle);
		rot_beh.setSchedulingBounds(CommonsKK.hundredBS);
		objRG.addChild(rot_beh);;
	}
	
	public void add_Child(TransformGroup nextTG) {
		objRG.addChild(nextTG);                            			// attach the next transformGroup to 'objTG'
	}
}

class MotorObject extends Lab8ObjectsKK {
	public MotorObject() {
		scale = 3.5d;                                      			// use to scale up/down original size
		post = new Vector3f(0f, 2.5f, 0f);               			// use to move object for positioning
		transform_Object("FanMotor");                      			// set transformation to 'objTG' and load object file
		mtl_clr[1] = new Color3f(0.58f, 0.69f, 0.11f);     			// set "FanMotor" to a different color than the common 		                                              
		obj_Appearance();                                  			// set appearance after converting object node to Shape3D
	}

	public TransformGroup position_Object() {              			// attach object BranchGroup "FanMotor" to 'objTG'
		Transform3D r_axis = new Transform3D();            			// default: rotate around Y-axis
		r_axis.rotY(Math.PI);                              			// rotate around y-axis for 180 degrees
		objRG = new TransformGroup(r_axis);                			// allow "FanMotor" to rotate
		objTG.addChild(objRG);                             			// position "FanMotor" by attaching 'objRG' to 'objTG'
		objRG.addChild(objBG);                             			// rotate "FanMotor" by attaching 'objBG' to 'objRG'
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objRG.addChild(nextTG);                            			// attach the next transformGroup to 'objTG'
	}
}

class BladesObject extends Lab8ObjectsKK {
	public BladesObject() {
		scale = 2.9d;                                      			// use to scale up/down original size
		post = new Vector3f(0f, 0f, -1.2f);                			// use to move object for positioning
		transform_Object("FanBlades");                     			// set transformation to 'objTG' and load object file
		mtl_clr[1] = new Color3f(0.58f, 0.69f, 0.11f);     			// set "FanBlades" to a different color than the common  		                                              
		obj_Appearance();                                  			// set appearance after converting object node to Shape3D
	}

	public TransformGroup position_Object() {              			// attach object BranchGroup "FanBlades" to 'objTG'
		Transform3D r_axis = new Transform3D();            			// default: rotate around Y-axis
		r_axis.rotX(-Math.PI / 2.0f);                      			// rotate around z-axis for -90 degrees
		objRG = new TransformGroup();                				// allow "FanBlades" to rotate
		objTG.addChild(objRG);                             			// position "FanBlades" by attaching 'objRG' to 'objTG'
		rot_Object(500, r_axis);									// objRG is referenced by and holds a rotation behavior
		objRG.addChild(objBG);                             			// rotate "FanBlades" by attaching 'objBG' to 'objRG'
		return objTG;                                      
	}

	/* a function that modifies objRG to be referenced by and hold a rotation behavior */
	public void rot_Object(int rot_rate, Transform3D axis) {
		objRG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		objRG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		Alpha newRotationAlpha = new Alpha(-1, rot_rate);
		set_Alpha(newRotationAlpha);
		
		float startAngle = 0.0f;									// define the starting point of the fan rotation
		float endAngle = (float) Math.PI * 2.0f;					// define where the rotation ends before starting from startAngle again
		RotationInterpolator rot_beh = new RotationInterpolator(newRotationAlpha, objRG, axis, startAngle, endAngle);
		rot_beh.setSchedulingBounds(CommonsKK.hundredBS);
		objRG.addChild(rot_beh);;
	}
	
	public void add_Child(TransformGroup nextTG) {
		objRG.addChild(nextTG);                            			// attach the next transformGroup to 'objTG'
	}
	
	@Override
	protected void obj_Appearance() {
		super.obj_Appearance();										// apply initial appearance before modifying
		Appearance new_app = new Appearance();						// define "FanBlades" handle's material attributes
		Material new_mtl = (Material) mtl.cloneNodeComponent(true);
		
		new_mtl.setDiffuseColor(new Color3f(0.3f, 0.15f, 0.3f));	// use a different color
		
		new_app.setMaterial(new_mtl);                              	// set new appearance's material
		
		((Shape3D) objBG.getChild(5)).setAppearance(new_app);		// set "FanBlades" handle to a new appearance
	}	
}

class GuardObject extends Lab8ObjectsKK {
	public GuardObject() {
		scale = 3.3d;                                        		// use to scale up/down original size
		post = new Vector3f(0f, 0f, -1.6f);                   		// use to move object for positioning
		transform_Object("FanGuard");                     			// set transformation to 'objTG' and load object file
		mtl_clr[1] = new Color3f(0.58f, 0.69f, 0.11f);     			// set "FanGuard" to a different color than the common  		                                              
		obj_Appearance();                                  			// set appearance after converting object node to Shape3D
	}

	public TransformGroup position_Object() {              			// attach object BranchGroup "FanGuard" to 'objTG'
		Transform3D r_axis = new Transform3D();            			// default: rotate around Y-axis
		r_axis.rotY(Math.PI);                              			// rotate around y-axis for 180 degrees
		objRG = new TransformGroup(r_axis);                			// allow "FanGuard" to rotate
		objTG.addChild(objRG);                             			// position "FanGuard" by attaching 'objRG' to 'objTG'
		objRG.addChild(objBG);                             			// rotate "FanGuard" by attaching 'objBG' to 'objRG'
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objRG.addChild(nextTG);                            			// attach the next transformGroup to 'objTG'
	}
}

//===========/!\ End of Lab 6 shapes /!\============

class StandObject extends Lab8ObjectsKK {
	public StandObject() {
		scale = 1d;                                        			// use to scale up/down original size
		post = new Vector3f(0f, -1.0f, 0f);                   		// use to move object for positioning
		transform_Object("FanStand");                      			// set transformation to 'objTG' and load object file
		mtl_clr[1] = new Color3f(0.58f, 0.69f, 0.11f);     			// set "FanStand" to a different color than the common  		                                              
		obj_Appearance();                                  			// set appearance after converting object node to Shape3D
	}

	public TransformGroup position_Object() {              			// attach object BranchGroup "FanStand" to 'objTG'
		Transform3D r_axis = new Transform3D();            			// default: rotate around Y-axis
		r_axis.rotY(Math.PI);                              			// rotate around y-axis for 180 degrees
		objRG = new TransformGroup(r_axis);                			// allow "FanStand" to rotate
		objTG.addChild(objRG);                            			// position "FanStand" by attaching 'objRG' to 'objTG'
		objRG.addChild(objBG);                             			// rotate "FanStand" by attaching 'objBG' to 'objRG'
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objRG.addChild(nextTG);                            			// attach the next transformGroup to 'objTG'
	}
}

class SwitchObject extends Lab8ObjectsKK {
	public SwitchObject() {
		scale = 0.3d;                                      			// actual scale is 0.3 = 1.0 x 0.3
		post = new Vector3f(0.02f, -0.77f, -0.8f);         			// location to connect "FanSwitch" with "FanStand"
		transform_Object("FanSwitch");                     			// set transformation to 'objTG' and load object file
		obj_Appearance();                                  			// set appearance after converting object node to Shape3D
	}

	public TransformGroup position_Object() {
		objTG.addChild(objBG);                             			// attach "FanSwitch" to 'objTG'
		return objTG;                                      			// use 'objTG' to attach "FanSwitch" to the previous TG
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            			// attach the next transformGroup to 'objTG'
	}
}

