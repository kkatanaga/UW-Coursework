/* Copyright material for the convenience of students working on Lab Exercises */

package codesKK280;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jdesktop.j3d.examples.sound.audio.JOALMixer;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.java3d.utils.universe.Viewer;
import org.jogamp.vecmath.*;

public class Lab9KK extends JPanel implements KeyListener {

	private static final long serialVersionUID = 1L;
	private static JFrame frame;

	private static final int OBJ_NUM = 5;
	private static Lab9ObjectsKK[] object3D = new Lab9ObjectsKK[OBJ_NUM];
	
	private static Alpha shaftAlpha;
	private static Alpha bladeAlpha;
	
	private static ButtonObjects buttons;
	private static BackgroundSound bgs;

	/* a public function to build the base labeled with 'str' */
	public static TransformGroup create_Base(String str) {
		Lab9ShapesKK baseShape = new BaseShape();
		
		Transform3D scaler = new Transform3D();
		scaler.setScale(new Vector3d(4d, 2d, 4d));         // set scale for the 4x4 matrix
		TransformGroup baseTG = new TransformGroup(scaler); 
		baseTG.addChild(baseShape.position_Object());

		ColorString clr_str = new ColorString(str, CommonsKK.Red, 0.06, 
				new Point3f(-str.length() / 4f, -9.4f, 8.2f));
		Transform3D r_axis = new Transform3D();            // default: rotate around Y-axis
		r_axis.rotY(Math.PI);                              
		TransformGroup objRG = new TransformGroup(r_axis); 
		objRG.addChild(clr_str.position_Object());         // move string to baseShape's other side
		baseTG.addChild(objRG);

		return baseTG;
	}
	
	/* a function to create the desk fan */
	private static TransformGroup create_Fan() {
		TransformGroup fanTG = new TransformGroup();

		object3D[0] = new StandObject();                  		// create "FanStand"
		fanTG = object3D[0].position_Object();             		// set 'fan_baseTG' to FanStand's 'objTG'
		
		object3D[1] = new SwitchObject();                 		// create "FanSwitch"
		object3D[2] = new ShaftObject();                 		// create "FanShaft"
		
		buttons = new ButtonObjects();							// create left & right buttons + red & green states
		
		object3D[0].add_Child(object3D[1].position_Object());	// attach "FanSwitch" to "FanStand"
		object3D[0].add_Child(object3D[2].position_Object());	// attach "FanShaft" to "FanStand"
		object3D[0].add_Child(buttons.getSwitchTG());			// attach "FanButton"s to "FanStand"
		
		object3D[3] = new MotorObject();                 		// create "FanMotor"
		object3D[2].add_Child(object3D[3].position_Object());	// attach "FanMotor" to "FanShaft"
		
		BladesObject blades = new BladesObject();               // create "FanBlades"
		object3D[4] = new GuardObject();                 		// create "FanGuard"
		object3D[3].add_Child(blades.position_Object());		// attach "FanBlades" to "FanMotor"
		object3D[3].add_Child(object3D[4].position_Object());	// attach "FanGuard" to "FanMotor"
		
		shaftAlpha = object3D[2].get_Alpha();					// save the reference to the Alpha for the shaft rotation
		bladeAlpha = blades.get_Alpha();						// save the reference to the Alpha for the blade rotation
		
		bgs = blades.get_BGS();									// save the reference to the BackgroundSound for pausing and resuming
		
		fanTG.addChild(create_Base("KK's Lab9"));          		// create and attach "FanBase" to "FanStand"
		return fanTG;
	}

	/* a function to build the content branch, including the fan and other environmental settings */
	public static BranchGroup create_Scene() {
		BranchGroup sceneBG = new BranchGroup();
		TransformGroup sceneTG = new TransformGroup();	   // make 'sceneTG' continuously rotating
		
		sceneTG.addChild(create_Fan());                    // add the fan to the rotating 'sceneTG'

		sceneBG.addChild(new AxesShape().position_Object());
		sceneBG.addChild(sceneTG);                         // keep the following stationary
		sceneBG.addChild(CommonsKK.add_Lights(CommonsKK.White, 1));

		return sceneBG;
	}
	
	public Lab9KK(BranchGroup sceneBG) {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas = new Canvas3D(config);
		
		canvas.addKeyListener(this);									// enables the use of KeyListener
		
		SimpleUniverse su = new SimpleUniverse(canvas);   				// create a SimpleUniverse
		enableAudio(su);
		CommonsKK.define_Viewer(su, new Point3d(0.25d, 0.25d, 10.0d));  // set the viewer's location
		
		sceneBG.addChild(CommonsKK.key_Navigation(su));               	// allow key navigation
		sceneBG.compile();		                           				// optimize the BranchGroup
		su.addBranchGraph(sceneBG);                        				// attach the scene to SimpleUniverse

		setLayout(new BorderLayout());
		add("Center", canvas);
		frame.setSize(800, 800);                           				// set the size of the JFrame
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		frame = new JFrame("KK's Lab9");
		frame.getContentPane().add(new Lab9KK(create_Scene()));  		// start the program
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/* a function to enable audio device via JOAL */
	private void enableAudio(SimpleUniverse simple_U) {
		JOALMixer mixer = null;		                         			// create a null mixer as a joalmixer
		Viewer viewer = simple_U.getViewer();
		viewer.getView().setBackClipDistance(20.0f);         			// make object(s) disappear beyond 20f 

		if (mixer == null && viewer.getView().getUserHeadToVworldEnable()) {			                                                 
			mixer = new JOALMixer(viewer.getPhysicalEnvironment());
			if (!mixer.initialize()) {                       			// add mixer as audio device if successful
				System.out.println("Open AL failed to init");
				viewer.getPhysicalEnvironment().setAudioDevice(null);
			}
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		char key = Character.toLowerCase(e.getKeyChar());
		if (key == 'z') {
			if (bladeAlpha.isPaused())
				return;							// Do nothing if blades are stopped
			
			if (shaftAlpha.isPaused()) {
				shaftAlpha.resume();			// Start fan motor when it's stopped
				buttons.setRightSwitch(0);		// Set right button to green
			} else {
				shaftAlpha.pause();				// Stop fan motor when it's rotating
				buttons.setRightSwitch(1);		// Set right button to red
			}
		} else if (key == 'x') {
			if (bladeAlpha.isPaused()) {
				bladeAlpha.resume();			// Start fan blades when it's stopped
				buttons.setLeftSwitch(0);		// Set left button to green
				bgs.setEnable(true);			// Turn on fan hum
				if (shaftAlpha.isPaused())
					shaftAlpha.resume();		// Run resume() start on fan motor only when it's stopped
					
			} else {
				bladeAlpha.pause();				// Stop fan blades when it's rotating
				buttons.setLeftSwitch(1);		// Set left button to red
				bgs.setEnable(false);			// Turn off fan hum
				if (!shaftAlpha.isPaused())
					shaftAlpha.pause();			// Run pause() on fan motor only when it's rotating
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}
}

