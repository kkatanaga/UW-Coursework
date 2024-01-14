/* Copyright material for the convenience of students working on Lab Exercises */

package codesKK280;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;

public class Lab7KK extends JPanel {

	private static final long serialVersionUID = 1L;
	private static JFrame frame;

	private static final int OBJ_NUM = 10;
	private static Lab7ObjectsKK[] object3D = new Lab7ObjectsKK[OBJ_NUM];

	/* a public function to build the base labeled with 'str' */
	public static TransformGroup create_Base(String str) {
		Lab7ShapesKK baseShape = new BaseShape();
		
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
		object3D[3] = new ButtonObject("left");					// create left "FanButton"
		object3D[4] = new ButtonObject("right");				// create right "FanButton"
		object3D[0].add_Child(object3D[1].position_Object());	// attach "FanSwitch" to "FanStand"
		object3D[0].add_Child(object3D[2].position_Object());	// attach "FanShaft" to "FanStand"
		object3D[0].add_Child(object3D[3].position_Object());	// attach left "FanButton" to "FanStand"
		object3D[0].add_Child(object3D[4].position_Object());	// attach right "FanButton" to "FanStand"
		
		object3D[5] = new MotorObject();                 		// create "FanMotor"
		object3D[2].add_Child(object3D[5].position_Object());	// attach "FanMotor" to "FanShaft"
		
		object3D[6] = new BladesObject();                 		// create "FanBlades"
		object3D[7] = new GuardObject();                 		// create "FanGuard"
		object3D[5].add_Child(object3D[6].position_Object());	// attach "FanBlades" to "FanMotor"
		object3D[5].add_Child(object3D[7].position_Object());	// attach "FanGuard" to "FanMotor"
		
		fanTG.addChild(create_Base("KK's Lab7"));          		// create and attach "FanBase" to "FanStand"
		return fanTG;
	}

	/* a function to build the content branch, including the fan and other environmental settings */
	public static BranchGroup create_Scene() {
		BranchGroup sceneBG = new BranchGroup();
		TransformGroup sceneTG = new TransformGroup();	   // make 'sceneTG' continuously rotating
		sceneTG.addChild(CommonsKK.rotate_Behavior(7500, sceneTG));
		
		sceneTG.addChild(create_Fan());                    // add the fan to the rotating 'sceneTG'

		sceneBG.addChild(new AxesShape().position_Object());
		sceneBG.addChild(sceneTG);                         // keep the following stationary
		sceneBG.addChild(CommonsKK.add_Lights(CommonsKK.White, 1));

		return sceneBG;
	}

	/* NOTE: Keep the constructor for each of the labs and assignments */
	public Lab7KK(BranchGroup sceneBG) {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas = new Canvas3D(config);
		
		SimpleUniverse su = new SimpleUniverse(canvas);    // create a SimpleUniverse
		CommonsKK.define_Viewer(su, new Point3d(0.25d, 0.25d, 10.0d));   // set the viewer's location
		
		sceneBG.addChild(CommonsKK.key_Navigation(su));               // allow key navigation
		sceneBG.compile();		                           // optimize the BranchGroup
		su.addBranchGraph(sceneBG);                        // attach the scene to SimpleUniverse

		setLayout(new BorderLayout());
		add("Center", canvas);
		frame.setSize(800, 800);                           // set the size of the JFrame
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		frame = new JFrame("KK's Lab7");                   // NOTE: change KK to student's initials
		frame.getContentPane().add(new Lab7KK(create_Scene()));  // start the program
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

