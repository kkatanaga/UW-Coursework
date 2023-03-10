package codesKK280;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.IncorrectFormatException;
import org.jogamp.java3d.loaders.ParsingErrorException;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;

class CylinderShapes {									// creates 4 cylinders using a shared group
	private TransformGroup objTG;                          // use 'objTG' to position an object
	public CylinderShapes(Transform3D trfm) {
		objTG = new TransformGroup(trfm);
		Vector3f[] pos = {new Vector3f(-0.5f, 0f, 0.5f), new Vector3f(0.5f, 0f, 0.5f),
						  new Vector3f(-0.5f, 0f, -0.5f), new Vector3f(0.5f, 0f, -0.5f)};
		SharedGroup cyldSG = new SharedGroup();
		cyldSG.addChild(create_Object());
		cyldSG.compile();
		
		for (int i = 0; i < 4; ++i) {
			objTG.addChild(linked3D(pos[i], new Link(cyldSG)));		// links an instance of cylinder to 1 of 4 positions
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
		Appearance appearance = CommonsKK.obj_Appearance(CommonsKK.Orange);			// cylinder color
		return new Cylinder(0.5f, 0.2f, Primitive.GENERATE_NORMALS, 30, 30, appearance);
	}
	public TransformGroup position_Object() {
		return objTG;
	}
}


class Ring1Shape {										// loads Ring1.obj
	private Scene s;                          // use 'objTG' to position an object
	
	public Ring1Shape() {
		int flags = ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY;
		ObjectFile f = new ObjectFile(flags, (float) (60 * Math.PI / 180.0));
		String directory = "src/codesKK280/objects/Ring1.obj";			// default directory starting from the Comp2800KK
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
	}
	public BranchGroup position_Object() {
		return s.getSceneGroup();
	}
}

class AxesShape {										// copied from Lab1KK
	private static final int UNIT = 1;					// length of axes
	
	protected Node create_Object() {
		final int AXIS_COUNT = 3;						// number of axes in a 3D space
		
		final int flags = LineArray.COLOR_3 | LineArray.COORDINATES;
		Point3f axis[] = new Point3f[AXIS_COUNT];       // declare 3 points for coordinate axes
		LineArray lineArr = new LineArray(AXIS_COUNT * 2, flags);	// double the number of vertices for tail & head
		
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

class StringShape {											// copied from Lab1KK; changed size, position, and orientation from original
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
		return new Shape3D(text3D, CommonsKK.obj_Appearance(CommonsKK.Cyan));
	}
	public Node position_Object() {
		return objTG;
	}
}

public class Assignment1KK extends JPanel {
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	
	/* a function to build the content branch */
	public static BranchGroup create_Scene() {
		BranchGroup sceneBG = new BranchGroup();           						// create the scene's BranchGroup
		TransformGroup sceneTG = new TransformGroup();     						// create the scene's TransformGroup
		
		Transform3D trfm = new Transform3D();									// prepare location of baseTG
		trfm.setScale(new Vector3d(1.0f, 1.0f, 0.25f));							// scales base by 1/4 in the z dimension
		Transform3D translator = new Transform3D();
		translator.setTranslation(new Vector3f(0.0f, -1.1f, 0.0f));				// moves base to below the ring shape
		trfm.mul(translator);													// combines scale and translation
		
		TransformGroup baseTG = new CylinderShapes(trfm).position_Object();		// creates 4 cylinder shapes with transformation as children of baseTG
		Appearance appearance = CommonsKK.obj_Appearance(CommonsKK.Orange);		// color of boxes; same as cylinders
		baseTG.addChild(new Box(0.5f, 0.1f, 1.0f, appearance));					// 1 x 0.2 x 2 box
		baseTG.addChild(new Box(1.0f, 0.1f, 0.5f, appearance));					// 2 x 0.2 x 1 box
		baseTG.addChild(new StringShape("KK's Assignment 1").position_Object());// stringTG
		
		sceneTG.addChild(baseTG);												// 4 cylinders and 2 boxes
		sceneTG.addChild(new Ring1Shape().position_Object());					// load Ring1.obj
		
		sceneBG.addChild(CommonsKK.add_Lights(CommonsKK.White, 1));				// SceneGraphA1.jpg doesn't include it, but lights are required
		sceneBG.addChild(CommonsKK.rotate_Behavior(7500, sceneTG));	
		sceneBG.addChild(sceneTG);                         						// make 'sceneTG' continuous rotating
		sceneBG.addChild(new AxesShape().position_Object());					// /!\ Part of Lab1KK; creates the x, y, z axes /!\
		return sceneBG;
	}

	/* NOTE: Keep the constructor for each of the labs and assignments */
	public Assignment1KK(BranchGroup sceneBG) {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas = new Canvas3D(config);
		
		SimpleUniverse su = new SimpleUniverse(canvas);    // create a SimpleUniverse
		CommonsKK.define_Viewer(su, new Point3d(4.0d, 1.0d, 1.0d));
		
		sceneBG.addChild(CommonsKK.key_Navigation(su));    // allow key navigation
		sceneBG.compile();		                           // optimize the BranchGroup
		su.addBranchGraph(sceneBG);                        // attach the scene to SimpleUniverse

		setLayout(new BorderLayout());
		add("Center", canvas);
		frame.setSize(800, 800);                           // set the size of the JFrame
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		frame = new JFrame("KK's Lab1");
		frame.getContentPane().add(new Assignment1KK(create_Scene()));  // create an instance of the class
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
