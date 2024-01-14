package codesKK280;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.picking.PickResult;
import org.jogamp.java3d.utils.picking.PickTool;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

public class Assignment5KK extends JPanel implements MouseListener, KeyListener {
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private Canvas3D canvas;
	private static PickTool pickTool;
	public static HashMap<String, RotationInterpolator> rotations;						// keeps track of the RotationInterpolator of the rings with the Shape3D names as keys
	private static Switch winner;
	/* a function to build the content branch */
	public static BranchGroup create_Scene() {
		BranchGroup sceneBG = new BranchGroup();										// create the scene's BranchGroup
		
		TransformGroup sceneTG = new TransformGroup();									// create the scene's TransformGroup
		ConcentricRing rings = new ConcentricRing(CommonsKK.Orange, CommonsKK.Black);
		winner = rings.getSwitch();
		sceneTG.addChild(rings.position_Object());
		sceneTG.addChild(new ChangeBackground().position_Object());
		
		sceneBG.addChild(sceneTG);                         								// make 'sceneTG' continuous rotating
		sceneBG.addChild(CommonsKK.add_Lights(CommonsKK.White, 2));
		sceneBG.addChild(new AxesShape().position_Object());							// creates the x, y, z axes
		
		pickTool = new PickTool( sceneBG );                								// allow picking of objects in 'sceneBG'
		pickTool.setMode(PickTool.GEOMETRY);                 							// pick by bounding volume
		
		return sceneBG;
	}

	/* NOTE: Keep the constructor for each of the labs and assignments */
	public Assignment5KK(BranchGroup sceneBG) {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		canvas = new Canvas3D(config);
		canvas.addMouseListener(this);                     								// NOTE: enable mouse clicking
		canvas.addKeyListener(this);                     								// NOTE: enable key input
		
		SimpleUniverse su = new SimpleUniverse(canvas);    								// create a SimpleUniverse
		CommonsKK.define_Viewer(su, new Point3d(4.0d, 1.0d, 3.0d));
		
//		sceneBG.addChild(CommonsKK.key_Navigation(su));    								// allow key navigation
		sceneBG.compile();		                           								// optimize the BranchGroup
		su.addBranchGraph(sceneBG);                        								// attach the scene to SimpleUniverse

		setLayout(new BorderLayout());
		add("Center", canvas);
		frame.setSize(800, 800);                           								// set the size of the JFrame
		frame.setVisible(true);
	}
	public Assignment5KK(Canvas3D canvas3D) {
		canvas = canvas3D; 
	}
	
	public static void main(String[] args) {
		frame = new JFrame("KK's Assignment 5");
		frame.getContentPane().add(new Assignment5KK(create_Scene()));  				// create an instance of the class
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		int x = event.getX(); int y = event.getY();        								// mouse coordinates
		Point3d point3d = new Point3d(), center = new Point3d();
		canvas.getPixelLocationInImagePlate(x, y, point3d);								// obtain AWT pixel in ImagePlate coordinates
		canvas.getCenterEyeInImagePlate(center);           								// obtain eye's position in IP coordinates
		
		Transform3D transform3D = new Transform3D();       								// matrix to relate ImagePlate coordinates~
		canvas.getImagePlateToVworld(transform3D);         								// to Virtual World coordinates
		transform3D.transform(point3d);                    								// transform 'point3d' with 'transform3D'
		transform3D.transform(center);                     								// transform 'center' with 'transform3D'

		Vector3d mouseVec = new Vector3d();
		mouseVec.sub(point3d, center);
		mouseVec.normalize();
		pickTool.setShapeRay(point3d, mouseVec);           								// send a PickRay for intersection
		
		PickResult pickResult = pickTool.pickClosest();									// obtain the closest hit
		if (pickResult == null)
			return;
		
		Node ring = pickResult.getNode(PickResult.SHAPE3D);								// pick a Shape3D, i.e., a ring object
		
		String shapeName = ring.getName();												// Gets the name of the shape (the rings are named, anything else isn't)
		if (shapeName == null)															// ring.getName() returns a null when the PickTool picks a shape without a name
			return;

		Alpha rotAlpha = null;
		if (shapeName.equals("Centr")) {												// center ring is clicked
			Code4Assign4.playSound(3);
			for (String shape: rotations.keySet()) {									// go through all of the rings
				rotAlpha = rotations.get(shape).getAlpha();
				if (!shape.equals("Outer")) {											// pause all rings except Outer
					if (!rotAlpha.isPaused()) rotAlpha.pause();							// call pause() only when the shapes are rotating
				}
				else {																	// keep Outer rotating
					if (rotAlpha.isPaused()) rotAlpha.resume();							// call resume() only when the shapes are paused
				}
			}
			String winnerName = canvas.getName();
			if (winnerName.equals("Player 1")) winner.setWhichChild(1);					// player 1 wins
			else if (winnerName.equals("Player 2")) winner.setWhichChild(2);			// player 2 wins
			Code4Assign4.playWinner();													// play the winner sound
			Code4Assign5.endGame();														// remove mouse access by both players
			return;
		}
		
 		if (rotations.containsKey(shapeName)) {											// check if the shape is one of the rotating rings
 			switch (shapeName) {														// play a sound specific to the rings
 			case "Small":
 				Code4Assign4.playSound(4);
 				break;
 			case "Large":
 				Code4Assign4.playSound(5);
 				break;
 			case "Outer":
 				Code4Assign4.playSound(6);
 				break;
 			}
 			
 			rotAlpha = rotations.get(shapeName).getAlpha();								// Get the Alpha from the RotationInterpolator associated with the shape
 			if (rotAlpha.isPaused())
 				rotAlpha.resume();														// Resume the rotation
 			else
 				rotAlpha.pause();														// Stop the rotation
			
		}
	}
	
	private Transform3D trfm = new Transform3D();
	private Transform3D newTrfm = new Transform3D();
	
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		String player = canvas.getName();
		if (player.equals("Player 1"))
			player = "F-L";
		else if (player.equals("Player 2"))
			player = "B-R";
		
		TransformGroup viewTG = Code4Assign5.viewTGs.get(player);			// get the TG above the viewplatform associated with the player
		
		switch(key) {
		case KeyEvent.VK_Y:
			newTrfm.setTranslation(new Vector3f(0.5f, 0f, 0f));				// move in the positive X axis
			break;
		case KeyEvent.VK_H:
			newTrfm.setTranslation(new Vector3f(0f, 0.5f, 0f));				// move in the positive Y axis
			break;
		case KeyEvent.VK_N:
			newTrfm.setTranslation(new Vector3f(0f, 0f, 0.5f));				// move in the positive Z axis
			break;
		case KeyEvent.VK_T:
			newTrfm.setTranslation(new Vector3f(-0.5f, 0f, 0f));			// move in the negative X axis
			break;
		case KeyEvent.VK_G:
			newTrfm.setTranslation(new Vector3f(0f, -0.5f, 0f));			// move in the negative Y axis
			break;
		case KeyEvent.VK_B:
			newTrfm.setTranslation(new Vector3f(0f, 0f, -0.5f));			// move in the negative Z axis
			break;
		default:
			return;
		}
		
		viewTG.getTransform(trfm);
		trfm.mul(newTrfm);
		viewTG.setTransform(trfm);
		
	}

	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
}
