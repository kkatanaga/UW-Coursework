package DogFight;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;

public class DogFight extends JPanel implements KeyListener, MouseListener {
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private static Fighter f1;
	private static Plane p1;
	
	/* a function to build the content branch */
	public static BranchGroup create_Scene() {
		BranchGroup sceneBG = new BranchGroup();           			// create the scene's BranchGroup
		
		f1 = new Fighter("su-47");
		p1 = f1.get_Plane();
		TransformGroup planeTG = f1.get_FighterTG();
		sceneBG.addChild(planeTG);									// add the Fighter into the scene
		p1.setEnable(false);										// /!\ Remove this to let the plane move /!\

		sceneBG.addChild(new Environment().get_Environment());		// add the Environment into the scene
		
//		sceneBG.addChild(sceneTG);									// add the TG that holds Fighter and Environment (+ other stuff we'll add) into the scene
		
		sceneBG.addChild(DFCommons.add_Lights(DFCommons.White, 1));	// /!\ Remove once the Environment class has its own lighting
//		sceneBG.addChild(DFCommons.rotate_Behavior(17500, planeTG));
		sceneBG.addChild(DFCommons.create_Axes());					// /!\ Remove when finished /!\
		return sceneBG;
	}

	/* NOTE: Keep the constructor for each of the labs and assignments */
	public DogFight(BranchGroup sceneBG) {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas = new Canvas3D(config);	// Hello world
		canvas.addKeyListener(this);
		canvas.addMouseListener(this);
		
		SimpleUniverse su = new SimpleUniverse(canvas);    				// create a SimpleUniverse
		DFCommons.define_Viewer(su, new Point3d(4.0d, 1.0d, 3.0d));
		View view = su.getViewer().getView();
		view.setBackClipDistance(100);
		
		sceneBG.addChild(DFCommons.key_Navigation(su));    				// allow key navigation
		sceneBG.compile();		                           				// optimize the BranchGroup
		su.addBranchGraph(sceneBG);                        				// attach the scene to SimpleUniverse

		setLayout(new BorderLayout());
		add("Center", canvas);
		frame.setSize(800, 800);                      // set the size of the JFrame
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		frame = new JFrame("DogFight");
		frame.getContentPane().add(new DogFight(create_Scene()));  		// create an instance of the class
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		Transform3D newTrfm = new Transform3D();
		Transform3D trfm = new Transform3D();
		
		switch (key) {
		case KeyEvent.VK_W:
			newTrfm.rotX(0.01);			// Pitch down
			break;
		case KeyEvent.VK_S:
			newTrfm.rotX(-0.01);		// Pitch up
			break;
		case KeyEvent.VK_A:
			newTrfm.rotZ(-0.01);		// Roll left
			break;
		case KeyEvent.VK_D:
			newTrfm.rotZ(0.01);			// Roll right
			break;
		case KeyEvent.VK_Q:
			newTrfm.rotY(0.01);			// Yaw left
			break;
		case KeyEvent.VK_E:
			newTrfm.rotY(-0.01);		// Yaw right
			break;
		case KeyEvent.VK_SHIFT:
			p1.accelerate(0.005f);		// Accelerate
			break;
		case KeyEvent.VK_CONTROL:
			p1.accelerate(-0.005f);		// Decelerate
			break;
		case KeyEvent.VK_P:
			p1.explode();				// Explode plane
			break;
		case KeyEvent.VK_SPACE:
			p1.shoot();					// Shoot
			break;
		default:
			return;
		}
		
		p1.getRootTG().getTransform(trfm);
		trfm.mul(newTrfm);
		p1.getRootTG().setTransform(trfm);
	}
	@Override
	public void mouseClicked(MouseEvent e) {
//		int mButton = e.getButton();
//		switch (mButton) {
//		case MouseEvent.BUTTON1:
//			p1.shoot();
//			break;
//		}
	}
	public void mousePressed(MouseEvent e) {}
	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}
