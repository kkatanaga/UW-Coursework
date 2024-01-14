/* *********************************************************
 * For use by students to work on assignments and project.
 * Permission required material. Contact: xyuan@uwindsor.ca 
 **********************************************************/
package codesKK280;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.picking.PickResult;
import org.jogamp.java3d.utils.picking.PickTool;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;

public class Code4Assign4 extends JPanel implements MouseListener {
	private static final long serialVersionUID = 1L;
	private static JFrame frame;

	private Canvas3D canvas;
	private static PickTool pickTool;

	private static SoundUtilityJOAL soundJOAL;               // for A5: needed for sound

	/* a function to set user-defined values for a rotating box and attach it to 'scene' */
	private static TransformGroup one_Box(float x, float y, float z) {
		Appearance app = CommonsKK.obj_Appearance(CommonsKK.Green);
		Box box = new Box(x, y, z,                         // create an appearance-modifiable box
				Primitive.GENERATE_NORMALS | Primitive.ENABLE_APPEARANCE_MODIFY, app);
		box.setUserData(0);                                // 'UserData' retrievable at picking
//		box.setName("box");                                // NOTE: 'Name' is also retrievable
		
		TransformGroup sceneTG = new TransformGroup();
		sceneTG.addChild(box);                             // add 'box' to the 'sceneTG'
		return sceneTG;
	}

	/* a function to create and return the scene BranchGroup */
	public static BranchGroup create_Scene() {
		BranchGroup sceneBG = new BranchGroup();
		
		TransformGroup sceneTG = one_Box(0.5f, 0.5f, 0.5f);  
		sceneBG.addChild(CommonsKK.rotate_Behavior(5000, sceneTG)); 
		sceneBG.addChild(sceneTG);                         // make 'sceneTG' continuously rotating
		pickTool = new PickTool( sceneBG );                // allow picking of objects in 'sceneBG'
		pickTool.setMode(PickTool.BOUNDS);                 // pick by bounding volume
		return sceneBG;
	}

	/* a constructor to set up and run the application */
	public Code4Assign4(BranchGroup sceneBG) {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		canvas = new Canvas3D(config);                        
		canvas.addMouseListener(this);                     // NOTE: enable mouse clicking 

		SimpleUniverse su = new SimpleUniverse(canvas);    // create a SimpleUniverse
		CommonsKK.define_Viewer(su, new Point3d(1.0d, 1.0d, 4.0d));  // set the viewer's location
		
		sceneBG.addChild(CommonsKK.add_Lights(CommonsKK.White, 1));
		sceneBG.addChild(CommonsKK.key_Navigation(su));     // allow key navigation
		sceneBG.compile();
		su.addBranchGraph(sceneBG);                        // attach the scene to SimpleUniverse
		
		setLayout(new BorderLayout());
		add("Center", canvas);		
		frame.setSize(800, 800);                           // set the size of the JFrame
		frame.setVisible(true);
	}

	/* for A5: a constructor to initialize canvas */
	public Code4Assign4(Canvas3D canvas3D) {
		canvas = canvas3D; 
	}

	public static void main(String[] args) {
		frame = new JFrame("Code for Assignment 4");
		frame.getContentPane().add(new Code4Assign4(create_Scene())); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}	

	@Override
	public void mouseClicked(MouseEvent event) {
		int x = event.getX(); int y = event.getY();        // mouse coordinates
		Point3d point3d = new Point3d(), center = new Point3d();
		canvas.getPixelLocationInImagePlate(x, y, point3d);// obtain AWT pixel in ImagePlate coordinates
		canvas.getCenterEyeInImagePlate(center);           // obtain eye's position in IP coordinates
		
		Transform3D transform3D = new Transform3D();       // matrix to relate ImagePlate coordinates~
		canvas.getImagePlateToVworld(transform3D);         // to Virtual World coordinates
		transform3D.transform(point3d);                    // transform 'point3d' with 'transform3D'
		transform3D.transform(center);                     // transform 'center' with 'transform3D'

		Vector3d mouseVec = new Vector3d();
		mouseVec.sub(point3d, center);
		mouseVec.normalize();
		pickTool.setShapeRay(point3d, mouseVec);           // send a PickRay for intersection
		
		
		if (pickTool.pickClosest() != null) {
			PickResult pickResult = pickTool.pickClosest();// obtain the closest hit
			Box box = (Box)pickResult.getNode(PickResult.PRIMITIVE);
			Appearance app = new Appearance();             // originally a PRIMITIVE as a box
			if ((int) box.getUserData() == 0) {            // retrieve 'UserData'
				app = CommonsKK.obj_Appearance(CommonsKK.Green);
				box.setUserData(1);                        // set 'UserData' to a new value
			}
			else {                                         // use 'UserData' as flag to switch color
				app = CommonsKK.obj_Appearance(CommonsKK.Red);
				box.setUserData(0);                        // reset 'UserData'
			}
			box.setAppearance(app);                        // change box's appearance
		} 
	}

	public void mouseEntered(MouseEvent arg0) { }
	public void mouseExited(MouseEvent arg0) { }
	public void mousePressed(MouseEvent e) { }
	public void mouseReleased(MouseEvent e) { }
	
	/* for A5: a function to initialize for playing sound */
	public static void initialSound() {
		soundJOAL = new SoundUtilityJOAL();
		if (!soundJOAL.load("player1", 0f, 0f, 10f, true))
			System.out.println("Could not load " + "player1");	
		if (!soundJOAL.load("player2", 0f, 0f, 10f, true))
			System.out.println("Could not load " + "player2");
		if (!soundJOAL.load("do", 0f, 0f, 10f, true))
			System.out.println("Could not load " + "do");
		if (!soundJOAL.load("re", 0f, 0f, 10f, true))
			System.out.println("Could not load " + "re");
		if (!soundJOAL.load("mi", 0f, 0f, 10f, true))
			System.out.println("Could not load " + "mi");
		if (!soundJOAL.load("fa", 0f, 0f, 10f, true))
			System.out.println("Could not load " + "fa");
		if (!soundJOAL.load("winner", 0f, 0f, 10f, true))
			System.out.println("Could not load " + "winner");	
		}

	/* for A5: a function to play different sound according to key (user) */
	public static void playSound(int key) {
		String snd_pt = "";
		switch (key) {
		case 1:
			snd_pt = "player1";
			break;
		case 2:
			snd_pt = "player2";
			break;
		case 3:
			snd_pt = "do";
			break;
		case 4:
			snd_pt = "re";
			break;
		case 5:
			snd_pt = "mi";
			break;
		case 6:
			snd_pt = "fa";
			break;
		}
		soundJOAL.play(snd_pt);
		try {
			Thread.sleep(1000); // sleep for 1 secs
		} catch (InterruptedException ex) {}
		soundJOAL.stop(snd_pt);
	}
	public static void playWinner() {
		soundJOAL.play("winner");
		try {
			Thread.sleep(10000); // sleep for 10 secs
		} catch (InterruptedException ex) {}
		soundJOAL.stop("winner");
	}
}
