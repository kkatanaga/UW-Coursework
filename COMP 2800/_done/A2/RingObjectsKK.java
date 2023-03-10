package codesKK280;

import java.io.FileNotFoundException;

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Node;
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

class Ring1Shape extends RingObjectsKK {								// loads Ring1.obj
	private TransformGroup objTG;                          				// use 'objTG' to position an object
	public Ring1Shape(Color3f clr) {
		BranchGroup ringBG = load_Object("Ring1");
		Shape3D ringShape = (Shape3D) ringBG.getChild(0);
		ringShape.setAppearance(CommonsKK.obj_Appearance(clr));
		
		objTG = new TransformGroup();
		objTG.addChild(ringBG);
	}
	public TransformGroup position_Object() {
		return objTG;
	}
}

class Ring2Shape extends RingObjectsKK {								// loads Ring2.obj
	private TransformGroup objTG;                          				// use 'objTG' to position an object
	public Ring2Shape(Color3f clr) {
		BranchGroup ringBG = load_Object("Ring2");
		Shape3D ringShape = (Shape3D) ringBG.getChild(0);
		ringShape.setAppearance(CommonsKK.obj_Appearance(clr));
		
		Transform3D scaler = new Transform3D();
		scaler.setScale(0.9f);
		Transform3D trfm = new Transform3D();
		trfm.setTranslation(new Vector3f(0f, 0.065f, 0f));
		trfm.mul(scaler);
		objTG = new TransformGroup(trfm);
		objTG.addChild(ringBG);
	}
	public TransformGroup position_Object() {
		return objTG;
	}
}

class Ring3Shape extends RingObjectsKK {								// loads Ring3.obj
	private TransformGroup objTG;                          				// use 'objTG' to position an object
	public Ring3Shape(Color3f clr) {
		BranchGroup ringBG = load_Object("Ring3");
		Shape3D ringShape = (Shape3D) ringBG.getChild(0);
		ringShape.setAppearance(CommonsKK.obj_Appearance(clr));
		
		Transform3D scaler = new Transform3D();
		scaler.setScale(0.7f);
		objTG = new TransformGroup(scaler);
		objTG.addChild(ringBG);
	}
	public TransformGroup position_Object() {
		return objTG;
	}
}

class Ring4Shape extends RingObjectsKK {								// loads Ring4.obj
	private TransformGroup objTG;                          				// use 'objTG' to position an object
	public Ring4Shape(Color3f clr) {
		BranchGroup ringBG = load_Object("Ring4");
		Shape3D ringShape = (Shape3D) ringBG.getChild(0);
		ringShape.setAppearance(CommonsKK.obj_Appearance(clr));
		
		Transform3D scaler = new Transform3D();
		scaler.setScale(0.6f);
		objTG = new TransformGroup(scaler);
		objTG.addChild(ringBG);
	}
	public TransformGroup position_Object() {
		return objTG;
	}
}
