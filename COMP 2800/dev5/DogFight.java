package DogFight;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;

public class DogFight extends JPanel implements KeyListener {
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	public static Fighter f1;
	private static Plane p1;
	
	private static SimpleUniverse su = null; //simple universe moved to a private global variable
	public static BranchGroup fighterBG = null;
	private static float yawRate = 0.01f;
	public static TransformGroup viewTG = new TransformGroup();
	
	public final static BoundingSphere thousandBound = new BoundingSphere(new Point3d(), 1000.0);
	public final static BoundingSphere hundredBound = new BoundingSphere(new Point3d(), 100.0);
	public final static BoundingSphere tenBound = new BoundingSphere(new Point3d(), 10.0);
	
	public DogFight(BranchGroup sceneBG) {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas = new Canvas3D(config);
		canvas.addKeyListener(this);
		
		su = new SimpleUniverse(canvas);    				// create a SimpleUniverse
		DFCommons.enableAudio(su);

		viewTG = su.getViewingPlatform().getViewPlatformTransform();
		viewTG.addChild(fighterBG);
		viewTG.addChild(DFCommons.add_Lights(DFCommons.White, 2));

		View view = su.getViewer().getView();
		view.setBackClipDistance(500);
		
		sceneBG.compile();		                           				// optimize theBranchGroup
		su.addBranchGraph(sceneBG);                        				// attach the scene to SimpleUniverse
		setLayout(new BorderLayout());
		add("Center", canvas);
		frame.setSize(1200, 720);                      // set the size of the JFrame
		frame.setVisible(true);
	}
	
	/* a function to build the content branch */
	public static BranchGroup create_Scene() {
		BranchGroup sceneBG = new BranchGroup();           			// create the scene's BranchGroup
		
		f1 = new Fighter("su-47", "material", "player");
		p1 = f1.get_Plane();
		fighterBG = f1.getFighterBG();
		
		sceneBG.addChild(new Environment().get_Environment());

		sceneBG.addChild(DFCommons.create_Axes());					// /!\ Remove when finished /!\
		return sceneBG;
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
			newTrfm.rotX(-0.01f);		// Pitch down
			break;
		case KeyEvent.VK_S:
			newTrfm.rotX(0.01f);		// Pitch up
			break;
		case KeyEvent.VK_A:
			newTrfm.rotZ(0.02f);		// Roll left
			break;
		case KeyEvent.VK_D:
			newTrfm.rotZ(-0.02f);		// Roll right
			break;
		case KeyEvent.VK_Q:
			newTrfm.rotY(yawRate);		// Yaw left
			break;
		case KeyEvent.VK_E:
			newTrfm.rotY(-yawRate);		// Yaw right
			break;
		case KeyEvent.VK_SHIFT:
			p1.accelerate(0.005f);		// Accelerate
			break;
		case KeyEvent.VK_CONTROL:
			p1.accelerate(-0.005f);		// Decelerate
			break;
		case KeyEvent.VK_SPACE:
			p1.shoot();					// Shoot
			break;
		case KeyEvent.VK_P:
			f1.explode();
			break;
//		case KeyEvent.VK_B:		// rear view only works once
//			update_Viewer2(new Point3d(0.0d, 0.0d,4.0d),new Point3d(0, 0, 0),new Vector3d(0, 1, 0));
		    
//		    Transform3D t3d = new Transform3D();
//		    combine.getTransform(t3d);
//
//		    Vector3d translation = new Vector3d();
//		    t3d.get(translation);
//		    Point3d position = new Point3d(translation);
//		    
//
//		    combine.getTransform(t3d);
//		    Vector3d orientation = new Vector3d();
//		    Matrix3d matrix = new Matrix3d();
//		    t3d.getRotationScale(matrix);
//		    matrix.getColumn(2, orientation);
//		    orientation.normalize();
//		    
//		    System.out.println("behind"+ position+ orientation);
//		    
//		    
//		    Vector3d offset = new Vector3d(orientation);
//		    offset.scale(1); // scale direction vector by -distance
//
//		  
//		    System.out.println(offset); // print resulting point
//		    Transform3D newTransform = new Transform3D();
//		    newTransform.setTranslation(offset);
//		    viewtrans.setTransform(newTransform);
//		    
//		    viewtrans.getTransform(vtrfm);
		    
		    
		    //
//			break;
//		case KeyEvent.VK_UP:
//			newTrfm.setTranslation(new Vector3f(0f,0f, -0.5f));
//			break;
//		case KeyEvent.VK_DOWN:
//			newTrfm.setTranslation(new Vector3f(0f,0f, 0.5f));
//			break;
//		case KeyEvent.VK_LEFT:
//			newTrfm.setTranslation(new Vector3f(-0.5f,0f, 0f));
//			break;
//		case KeyEvent.VK_RIGHT:
//			newTrfm.setTranslation(new Vector3f(0.5f,0f, 0f));
//			break;
		default:
			return;
		}	
		viewTG.getTransform(trfm);
		trfm.mul(newTrfm);
		viewTG.setTransform(trfm);
	}

	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}

}