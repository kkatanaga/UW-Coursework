/* Copyright material for students taking COMP-2800 to work on assignment/labs/projects. */

package DogFight;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.*;

public class Environment {
	private TransformGroup mainTG;		// Holds everything in the Environment class; this will be referenced by the DogFight class
	// add more private fields when needed
	
	public Environment() {
		// add transformations such as translations and scales and pass into the following TransformGroup() if needed
		mainTG = new TransformGroup();
		
		// Loads a background image; copied from the assignments
		Background bg = new Background();
		BoundingSphere bound = new BoundingSphere(new Point3d(0f, 0f, 0f), Double.MAX_VALUE);
		bg.setImage(new TextureLoader("src/DogFight/images/room.jpg", null).getImage());			// /!\ Change "room.jpg" to whatever your image name is
		bg.setImageScaleMode(Background.SCALE_FIT_MAX);
		bg.setApplicationBounds(bound);
		
		mainTG.addChild(bg);
	}
	public TransformGroup get_Environment() {
		return mainTG;					// Returns the reference to Environment class
	}
}