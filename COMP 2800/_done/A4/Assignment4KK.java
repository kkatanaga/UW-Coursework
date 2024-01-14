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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

public class Assignment4KK extends JPanel implements MouseListener {
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private Canvas3D canvas;
	private static PickTool pickTool;
	public static HashMap<String, RotationInterpolator> rotations;						// keeps track of the RotationInterpolator of the rings with the Shape3D names as keys
	
	/* a function to build the content branch */
	public static BranchGroup create_Scene() {
		BranchGroup sceneBG = new BranchGroup();										// create the scene's BranchGroup
		
		TransformGroup sceneTG = new TransformGroup();									// create the scene's TransformGroup
		sceneTG.addChild(new ConcentricRing(CommonsKK.Orange, CommonsKK.Black).position_Object());
		sceneTG.addChild(new ChangeBackground().position_Object());
		
		sceneBG.addChild(sceneTG);                         								// make 'sceneTG' continuous rotating
		sceneBG.addChild(CommonsKK.add_Lights(CommonsKK.White, 1));
		sceneBG.addChild(new AxesShape().position_Object());							// creates the x, y, z axes
		
		pickTool = new PickTool( sceneBG );                								// allow picking of objects in 'sceneBG'
		pickTool.setMode(PickTool.GEOMETRY);                 							// pick by bounding volume
		
		return sceneBG;
	}

	/* NOTE: Keep the constructor for each of the labs and assignments */
	public Assignment4KK(BranchGroup sceneBG) {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		canvas = new Canvas3D(config);
		canvas.addMouseListener(this);                     								// NOTE: enable mouse clicking
		
		SimpleUniverse su = new SimpleUniverse(canvas);    								// create a SimpleUniverse
		CommonsKK.define_Viewer(su, new Point3d(4.0d, 1.0d, 3.0d));
		
		sceneBG.addChild(CommonsKK.key_Navigation(su));    								// allow key navigation
		sceneBG.compile();		                           								// optimize the BranchGroup
		su.addBranchGraph(sceneBG);                        								// attach the scene to SimpleUniverse

		setLayout(new BorderLayout());
		add("Center", canvas);
		frame.setSize(800, 800);                           								// set the size of the JFrame
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		frame = new JFrame("KK's Assignment 4");
		frame.getContentPane().add(new Assignment4KK(create_Scene()));  				// create an instance of the class
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

 		if (rotations.containsKey(shapeName)) {											// check if the shape is one of the rotating rings
			Alpha rotAlpha = rotations.get(shapeName).getAlpha();						// Get the Alpha from the RotationInterpolator associated with the shape
			if (rotAlpha.isPaused())
				rotAlpha.resume();														// Resume the rotation
			else
				rotAlpha.pause();														// Stop the rotation
		}
	}

	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}

}
