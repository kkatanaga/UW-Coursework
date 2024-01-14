package DogFight;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;

public class DogFight extends JPanel {
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	
	/* a function to build the content branch */
	public static BranchGroup create_Scene() {
		BranchGroup sceneBG = new BranchGroup();           						// create the scene's BranchGroup
		
		Fighter f1 = new Fighter();
		TransformGroup planeTG = f1.get_Fighter();
		sceneBG.addChild(planeTG);				// add the Fighter into the scene
		
//		sceneBG.addChild(new Environment().get_Environment());		// add the Environment into the scene
		
//		sceneBG.addChild(sceneTG);									// add the TG that holds Fighter and Environment (+ other stuff we'll add) into the scene
		
		sceneBG.addChild(DFCommons.add_Lights(DFCommons.White, 1));	// /!\ Remove once the Environment class has its own lighting
		sceneBG.addChild(DFCommons.rotate_Behavior(17500, planeTG));
		sceneBG.addChild(DFCommons.create_Axes());					// /!\ Remove when finished /!\
		return sceneBG;
	}

	/* NOTE: Keep the constructor for each of the labs and assignments */
	public DogFight(BranchGroup sceneBG) {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas = new Canvas3D(config);
		
		SimpleUniverse su = new SimpleUniverse(canvas);    // create a SimpleUniverse
		DFCommons.define_Viewer(su, new Point3d(4.0d, 1.0d, 3.0d));
		
		sceneBG.addChild(DFCommons.key_Navigation(su));    // allow key navigation
		sceneBG.compile();		                           // optimize the BranchGroup
		su.addBranchGraph(sceneBG);                        // attach the scene to SimpleUniverse

		setLayout(new BorderLayout());
		add("Center", canvas);
		frame.setSize(800, 800);                           // set the size of the JFrame
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		frame = new JFrame("DogFight Project");
		frame.getContentPane().add(new DogFight(create_Scene()));  // create an instance of the class
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
