/* Copyright material for students taking COMP-2800 to work on assignment/labs/projects. */

package codesKK280;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;


public class Lab5KK extends JPanel {

	private static final long serialVersionUID = 1L;
	private static JFrame frame;

	/* a function to build the content branch */
	public static BranchGroup create_Scene() {
		BranchGroup sceneBG = new BranchGroup();           // create the scene' BranchGroup
		
		TransformGroup sceneTG = (TransformGroup) new TurbineShape().position_Object();     // Yaw Drive, Nacelle, Rotor, and Blades with rotation

		sceneTG.addChild(new FoundationShape().position_Object());		// Foundation
		sceneTG.addChild(new TowerShape().position_Object());			// Tower
		
		sceneBG.addChild(CommonsKK.add_Lights(CommonsKK.White, 1));	
//		sceneBG.addChild(CommonsKK.rotate_Behavior(7500, sceneTG));	
		sceneBG.addChild(sceneTG);                         // make 'sceneTG' continuous rotating
		sceneBG.addChild(new AxesShape().position_Object());			// /!\ Part of lab1; creates the x, y, z axes /!\
		return sceneBG;
	}

	/* NOTE: Keep the constructor for each of the labs and assignments */
	public Lab5KK(BranchGroup sceneBG) {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas = new Canvas3D(config);
		
		SimpleUniverse su = new SimpleUniverse(canvas);    // create a SimpleUniverse
		CommonsKK.define_Viewer(su, new Point3d(4.0d, 0.0d, 1.0d));
		
		sceneBG.addChild(CommonsKK.key_Navigation(su));     // allow key navigation
		sceneBG.compile();		                           // optimize the BranchGroup
		su.addBranchGraph(sceneBG);                        // attach the scene to SimpleUniverse

		setLayout(new BorderLayout());
		add("Center", canvas);
		frame.setSize(800, 800);                           // set the size of the JFrame
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		frame = new JFrame("KK's Lab5");
		frame.getContentPane().add(new Lab5KK(create_Scene()));  // create an instance of the class
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
