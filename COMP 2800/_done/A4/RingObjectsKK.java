package codesKK280;

import java.io.FileNotFoundException;
import java.util.HashMap;

import org.jogamp.java3d.Alpha;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.RotationInterpolator;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.loaders.IncorrectFormatException;
import org.jogamp.java3d.loaders.ParsingErrorException;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Vector3f;

public abstract class RingObjectsKK {
	public abstract Node position_Object();
	protected BranchGroup load_Object(String fileName) {				// implemented the method in the abstract class since it does the same thing for each ring
		Scene s = null;
		String directory = "src/codesKK280/objects/" + fileName + ".obj";
		
		int flags = ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY;
		ObjectFile f = new ObjectFile(flags, (float) (60 * Math.PI / 180.0));
		try {
			s = f.load(directory);
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
}

class ConcentricRing extends RingObjectsKK {
	private TransformGroup concentricTG;
	
	public ConcentricRing(Color3f colorOne, Color3f colorTwo) {
		RingShape outerRing = new RingShape(1.0f, new Vector3f(0f, -0.066f, -0.01750f), "Outer", colorOne); 	// Outer ring
		RingShape largeRing = new RingShape(0.9f, new Vector3f(0f,  0.000f, -0.02315f), "Large", colorTwo);		// Large ring
		RingShape smallRing = new RingShape(0.7f, new Vector3f(0f,  0.000f, -0.03441f), "Small", colorOne);		// Small ring
		RingShape centrRing = new RingShape(0.6f, new Vector3f(0f,  0.000f, -0.02110f), "Centr", colorTwo);		// Center ring
		
		// The highest TG has a unique translation passed as Vector3f's as done above. Its child TG has the scale for the next TG
		TransformGroup outerRingTG = outerRing.position_Object(); 
		
		Transform3D yAxis = new Transform3D();
		Transform3D xAxis = new Transform3D();
		
		xAxis.rotZ(-Math.PI / 2.0);												// rotate on x axis
		
		Assignment4KK.rotations = new HashMap<String, RotationInterpolator>();	// Initialize to keep track of the incoming rotation behaviors
		outerRing.add_Rotation(6000, yAxis);									// Add y axis rotation to outer ring
		largeRing.add_Rotation(4500, xAxis);									// Add x axis rotation to large ring
		smallRing.add_Rotation(3000, yAxis);									// Add y axis rotation to small ring
		centrRing.add_Rotation(1500, xAxis);									// Add x axis rotation to center ring
		
		outerRing.set_Next(largeRing);											// Center, Small, and Large ring inherits Outer ring's scale and rotation but not their translation
		largeRing.set_Next(smallRing);											// Center and Small ring inherits Large ring's scale and rotations but not their translation
		smallRing.set_Next(centrRing);											// Center ring inherits small ring's scale and rotations but not their translation
		
		outerRingTG.addChild(new BaseShape(colorOne).position_Object());		// Add base shape to the ring such that it inherits the outer ring rotation
		concentricTG = outerRing.position_Object();								// Reference to ConcentricRing and outer ring are set to be the same
	}
	public TransformGroup position_Object() {
		return concentricTG;
	}
	
}

class RingShape extends RingObjectsKK {												// loads a Ring.obj
	private TransformGroup rootTG;													// holds the rotations and 
	private TransformGroup scaleTG;													// holds the scale of the ring and its smaller rings
	private String ringName;
	
	public RingShape(float scale, Vector3f translate, String ringName, Color3f clr) {
		this.ringName = ringName;
		
		BranchGroup ringBG = load_Object(ringName);										// load ring based on names (Outer, Large, Small, Centr)
		Shape3D ringShape = (Shape3D) ringBG.getChild(0);
		ringShape.setAppearance(CommonsKK.obj_Appearance(clr));
		
		ringShape.setName(ringName);
		
		Transform3D translator = new Transform3D();
		translator.setTranslation(translate);										// translate based on input
		TransformGroup translateTG = new TransformGroup(translator);				// centers the ring
		translateTG.addChild(ringBG);
		
		Transform3D scaler = new Transform3D();
		scaler.setScale(scale);														// scale based on input
		scaleTG = new TransformGroup(scaler);										// scales down the ring before centering
		scaleTG.addChild(translateTG);
		
		rootTG = new TransformGroup();
		rootTG.addChild(scaleTG);													// ringTG will be referenced by a rotation behavior elsewhere
	}
	public TransformGroup position_Object() {
		return rootTG;
	}
	public void set_Next(RingShape nextRing) {
		scaleTG.addChild(nextRing.position_Object());
	}
	protected void add_Rotation(int rot_rate, Transform3D axis) {
		rootTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		rootTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		Alpha rotationAlpha = new Alpha(-1, rot_rate);
		
		RotationInterpolator rot_beh = new RotationInterpolator(rotationAlpha, rootTG, axis, 0.0f, (float) Math.PI * 2.0f);
		rot_beh.setSchedulingBounds(CommonsKK.hundredBS);
		Assignment4KK.rotations.put(ringName, rot_beh);				// /!\ Added in Assignment 4; adds the RotationInterpolator of the shape to the HashMap /!\
		rot_beh.setEnable(true);
		rootTG.addChild(rot_beh);
	}
}