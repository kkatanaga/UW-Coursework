/* Copyright material for students taking COMP-2800 to work on assignment/labs/projects.*/

package DogFight;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jdesktop.j3d.examples.sound.BackgroundSoundBehavior;
import org.jdesktop.j3d.examples.sound.PointSoundBehavior;
import org.jdesktop.j3d.examples.sound.audio.JOALMixer;
import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.IncorrectFormatException;
import org.jogamp.java3d.loaders.ParsingErrorException;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.java3d.utils.universe.Viewer;
import org.jogamp.java3d.utils.universe.ViewingPlatform;
import org.jogamp.vecmath.*;

public class DFCommons extends JPanel {

	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	
	public final static Color3f Red = new Color3f(1.0f, 0.0f, 0.0f);
	public final static Color3f Green = new Color3f(0.0f, 1.0f, 0.0f);
	public final static Color3f Blue = new Color3f(0.0f, 0.0f, 1.0f);
	public final static Color3f Yellow = new Color3f(1.0f, 1.0f, 0.0f);
	public final static Color3f Cyan = new Color3f(0.0f, 1.0f, 1.0f);
	public final static Color3f Orange = new Color3f(1.0f, 0.5f, 0.0f);
	public final static Color3f Magenta = new Color3f(1.0f, 0.0f, 1.0f);
	public final static Color3f White = new Color3f(1.0f, 1.0f, 1.0f);
	public final static Color3f Grey = new Color3f(0.35f, 0.35f, 0.35f);
	public final static Color3f Black = new Color3f(0.0f, 0.0f, 0.0f);
	public final static Color3f[] clr_list = {Blue, Green, Red, Yellow,
			Cyan, Orange, Magenta, Grey};
	public final static int clr_num = 8;
	private static Color3f[] mtl_clrs = {White, Grey, Black};

	public final static BoundingSphere maxBound = new BoundingSphere(new Point3d(0f, 0f, 0f), Double.MAX_VALUE);
	public final static BoundingSphere twentyBS = new BoundingSphere(new Point3d(), 20.0);

	public static Scene load_Obj(String directory, boolean hasMtl) {
		Scene s = null;
		ObjectFile loader = null;
		int flags = ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY;
		
		try {
			if (hasMtl) {
				loader = new ObjectFile(flags);
				File file = new File(directory);
				s = loader.load(file.toURI().toURL());
			} else {
				loader = new ObjectFile(flags, (float) (60 * Math.PI / 180.0));
				s = loader.load(directory);
			}
		} catch (FileNotFoundException e) {
			System.err.println(e);
			System.exit(1);
		} catch (ParsingErrorException e) {
			System.err.println(e);
			System.exit(1);
		} catch (IncorrectFormatException e) {
			System.err.println(e);
			System.exit(1);
		} catch (Exception e) {
			System.err.println(e);
			System.exit(1);
		}
		return s;
	}
    /* A1: function to define object's material and use it to set object's appearance */
	public static Appearance obj_Appearance(Color3f m_clr) {		
		Material mtl = new Material();                     // define material's attributes
		mtl.setShininess(32);
		mtl.setAmbientColor(mtl_clrs[0]);                   // use them to define different materials
		mtl.setDiffuseColor(m_clr);
		mtl.setSpecularColor(mtl_clrs[1]);
		mtl.setEmissiveColor(mtl_clrs[2]);                  // use it to switch button on/off
		mtl.setLightingEnable(true);

		Appearance app = new Appearance();
		app.setMaterial(mtl);                              // set appearance's material
		return app;
	}	
	
	/* a function to create a rotation behavior and refer it to 'my_TG' */
	public static RotationInterpolator rotate_Behavior(int r_num, TransformGroup rotTG) {

		rotTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		Transform3D yAxis = new Transform3D();
		Alpha rotationAlpha = new Alpha(-1, r_num);
		RotationInterpolator rot_beh = new RotationInterpolator(
				rotationAlpha, rotTG, yAxis, 0.0f, (float) Math.PI * 2.0f);
		rot_beh.setSchedulingBounds(DogFight.hundredBound);
		return rot_beh;
	}

	/* a function to enable audio */
	public static void enableAudio(SimpleUniverse su){
		JOALMixer mixer = null;     // create a JOALMixer
		Viewer viewer = su.getViewer();
		viewer.getView().setBackClipDistance(20.0f);    // disappear beyond 20f

		if (mixer == null && viewer.getView().getUserHeadToVworldEnable()){
			mixer = new JOALMixer(viewer.getPhysicalEnvironment());
			if (!mixer.initialize()){   // add audio device
				System.out.println("Open AL failed to init");
				viewer.getPhysicalEnvironment().setAudioDevice(null);
			}
		}
	}

	/* a function to create point sound effect, use fileName and PointSound as parameters, use ps to control sound pause and resume */
	public static PointSound pointSoundCrash(String fileName, PointSound ps){
		Point2f[] distanceGain = {new Point2f(10.0f, 6.0f), // Full volume
				new Point2f(20.0f, 4.0f), // Half volume
				new Point2f(30.0f, 3.0f), // Quarter volume
				new Point2f(50.0f, 0.0f) // Zero volume
		};

		URL url = null;
		String filePath = "src/DogFight/sounds/" + fileName + ".wav";
		try {
			url = new URL("file", "localhost", filePath);
		} catch (Exception e) {
			System.out.println("Can't open " + filePath);
		}

		// PointSound ps = new PointSound();   // create point sound
		//ps.setInitialGain(6.0f);    // set initial gain
		ps.setLoop(-1);     // 0: once, 1: loop
		ps.setDistanceGain(distanceGain); // Set sound distance
		//ps.setPosition(new Point3f(0.0f, 0.0f, 0.0f));  // set sound position
		// create and position a point sound
		PointSoundBehavior pointSoundBehavior = new PointSoundBehavior(ps, url, new Point3f(0.0f, 0.0f, 0.0f));
		pointSoundBehavior.setSchedulingBounds(DogFight.hundredBound); // set scheduling
		return ps;
	}

	public static PointSound pointSoundExplosion(String fileName, PointSound ps){
		Point2f[] distanceGain = {new Point2f(10.0f, 6.0f), // Full volume
				new Point2f(20.0f, 4.0f), // Half volume
				new Point2f(30.0f, 3.0f), // Quarter volume
				new Point2f(50.0f, 0.0f) // Zero volume
		};

		URL url = null;
		String filePath = "src/DogFight/sounds/" + fileName + ".wav";
		try {
			url = new URL("file", "localhost", filePath);
		} catch (Exception e) {
			System.out.println("Can't open " + filePath);
		}
		ps.setCapability(PointSound.ALLOW_ENABLE_WRITE);
		//PointSound ps = new PointSound();   // create point sound
		//ps.setInitialGain(6.0f);    // set initial gain
		ps.setLoop(1);     // 0: once, 1: loop
		ps.setDistanceGain(distanceGain); // Set sound distance
		//ps.setPosition(new Point3f(0.0f, 0.0f, 0.0f));  // set sound position
		// create and position a point sound
		PointSoundBehavior pointSoundBehavior = new PointSoundBehavior(ps, url, new Point3f(0.0f, 0.0f, 0.0f));
		pointSoundBehavior.setSchedulingBounds(DogFight.hundredBound); // set scheduling
		return ps;
	}

	public static PointSound pointSoundShot(String fileName, PointSound ps){
		Point2f[] distanceGain = {new Point2f(10.0f, 6.0f), // Full volume
				new Point2f(20.0f, 4.0f), // Half volume
				new Point2f(30.0f, 3.0f), // Quarter volume
				new Point2f(50.0f, 0.0f) // Zero volume
		};

		URL url = null;
		String filePath = "src/DogFight/sounds/" + fileName + ".wav";
		try {
			url = new URL("file", "localhost", filePath);
		} catch (Exception e) {
			System.out.println("Can't open " + filePath);
		}

		//PointSound ps = new PointSound();   // create point sound
		//ps.setInitialGain(6.0f);    // set initial gain
		ps.setLoop(-1);     // 0: once, 1: loop
		ps.setDistanceGain(distanceGain); // Set sound distance
		//ps.setPosition(new Point3f(0.0f, 0.0f, 0.0f));  // set sound position
		// create and position a point sound
		PointSoundBehavior pointSoundBehavior = new PointSoundBehavior(ps, url, new Point3f(0.0f, 0.0f, 0.0f));
		pointSoundBehavior.setSchedulingBounds(DogFight.hundredBound); // set scheduling
		return ps;
	}

	public static PointSound pointSoundEngine(String fileName, PointSound ps){
		Point2f[] distanceGain = {new Point2f(10.0f, 6.0f), // Full volume
				new Point2f(20.0f, 4.0f), // Half volume
				new Point2f(30.0f, 3.0f), // Quarter volume
				new Point2f(50.0f, 0.0f) // Zero volume

		};
		ps.setCapability(PointSound.ALLOW_ENABLE_WRITE);

		URL url = null;
		String filePath = "src/DogFight/sounds/" + fileName + ".wav";
		try {
			url = new URL("file", "localhost", filePath);
		} catch (Exception e) {
			System.out.println("Can't open " + filePath);
		}

		//PointSound ps = new PointSound();   // create point sound
		//ps.setInitialGain(6.0f);    // set initial gain
		ps.setLoop(1);     // 0: once, 1: loop
		ps.setDistanceGain(distanceGain); // Set sound distance
		//ps.setPosition(new Point3f(0.0f, 0.0f, 0.0f));  // set sound position
		// create and position a point sound
		PointSoundBehavior pointSoundBehavior = new PointSoundBehavior(ps, url, new Point3f(0.0f, 0.0f, 0.0f));
		pointSoundBehavior.setSchedulingBounds(DogFight.hundredBound); // set scheduling
		return ps;
	}

	/* a function to create background sound effect */
	public static BackgroundSound backgroundSound(String fileName, BackgroundSound bgs){
		URL url = null;
		String filePath = "src/DogFight/sounds/" + fileName + ".wav";
		try {
			url = new URL("file", "localhost", filePath);
		} catch (Exception e) {
			System.out.println("Can't open " + filePath);
		}

		//BackgroundSound bgs = new BackgroundSound();    // create a background sound
		bgs.setInitialGain(0.4f);  // lower its volume
		BackgroundSoundBehavior player = new BackgroundSoundBehavior(bgs, url); // create the sound behavior
		player.setSchedulingBounds(DogFight.hundredBound);  // set scheduling bound

		return bgs;
	}
	
	/* a function to place one light or two lights at opposite locations */
	public static BranchGroup add_Lights(Color3f clr, int p_num) {
		BranchGroup lightBG = new BranchGroup();
		Point3f atn = new Point3f(0.5f, 0.0f, 0.0f);
		PointLight ptLight;
		float adjt = 1f;
		for (int i = 0; (i < p_num) && (i < 2); i++) {
			if (i > 0) 
				adjt = -1f; 
			ptLight = new PointLight(clr, new Point3f(3.0f * adjt, 1.0f, 3.0f  * adjt), atn);
			ptLight.setInfluencingBounds(DogFight.hundredBound);
			lightBG.addChild(ptLight);
		}
		return lightBG;
	}

	/* a function to position viewer to 'eye' location */
	public static void define_Viewer(SimpleUniverse simple_U, Point3d eye) {

	    TransformGroup viewTransform = simple_U.getViewingPlatform().getViewPlatformTransform();
		Point3d center = new Point3d(0, 0, 0);             // define the point where the eye looks at
		Vector3d up = new Vector3d(0, 1, 0);               // define camera's up direction
		Transform3D view_TM = new Transform3D();
		view_TM.lookAt(eye, center, up);
		view_TM.invert();
	    viewTransform.setTransform(view_TM);               // set the TransformGroup of ViewingPlatform
	}

	/* a function to allow key navigation with the ViewingPlateform */
	public static KeyNavigatorBehavior key_Navigation(SimpleUniverse simple_U) {
		ViewingPlatform view_platfm = simple_U.getViewingPlatform();
		TransformGroup view_TG = view_platfm.getViewPlatformTransform();
		KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(view_TG);
		keyNavBeh.setSchedulingBounds(twentyBS);
		return keyNavBeh;
	}

	/* a function to build the content branch and attach to 'scene' */
	public static BranchGroup create_Scene() {
		BranchGroup sceneBG = new BranchGroup();
		TransformGroup sceneTG = new TransformGroup();
		sceneTG.addChild(new Box(0.5f, 0.5f, 0.5f, obj_Appearance(Orange) ));
		sceneBG.addChild(rotate_Behavior(7500, sceneTG));
		
		sceneBG.addChild(sceneTG);
		return sceneBG;
	}
	
	public static Shape3D create_Axes() {					// copied from Lab1
		final int UNIT = 3;									// length of axes
		final int AXIS_COUNT = 3;							// number of axes in a 3D space
		final int flags = LineArray.COLOR_3 | LineArray.COORDINATES;
		
		Point3f axis[] = new Point3f[AXIS_COUNT];       	// declare 3 points for coordinate axes
		LineArray lineArr = new LineArray(AXIS_COUNT * 2, flags);	// double the number of vertices for tail & head
		
		axis[0] = new Point3f(UNIT, 0, 0);					// x coordinate
		axis[1] = new Point3f(0, UNIT, 0);					// y coordinate
		axis[2] = new Point3f(0, 0, UNIT);					// z coordinate
		
		Color3f color[] = new Color3f[AXIS_COUNT];
		color[0] = Red;										// x axis color
		color[1] = Green;									// y axis color
		color[2] = Blue;									// z axis color
		
		for (int i = 0; i < AXIS_COUNT; i++) {
			lineArr.setCoordinate(i * 2 + 1, axis[i]);  	// tail coordinate; head is at (0,0,0) by default
			lineArr.setColor(i * 2, color[i]);        		// tail color
			lineArr.setColor(i * 2 + 1, color[i]);			// head color
		}
		
		return new Shape3D(lineArr);                    	// create and return a Shape3D
	}
	
	/* a constructor to set up for the application */
	public DFCommons(BranchGroup sceneBG) {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas = new Canvas3D(config);
		
		SimpleUniverse su = new SimpleUniverse(canvas);    // create a SimpleUniverse
		define_Viewer(su, new Point3d(1.0d, 1.0d, 4.0d));  // set the viewer's location
		
		sceneBG.addChild(add_Lights(White, 1));	
		sceneBG.addChild(key_Navigation(su));              // allow key navigation
		sceneBG.compile();		                           // optimize the BranchGroup
		su.addBranchGraph(sceneBG);                        // attach the scene to SimpleUniverse

		setLayout(new BorderLayout());
		add("Center", canvas);
		frame.setSize(800, 800);                           // set the size of the JFrame
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		frame = new JFrame("KK's Common File");
		frame.getContentPane().add(new DFCommons(create_Scene()));  // create an instance of the class
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
