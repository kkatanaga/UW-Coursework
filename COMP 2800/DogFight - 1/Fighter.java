package DogFight;

import java.io.*;

import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.ImageComponent2D;
import org.jogamp.java3d.Material;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Texture;
import org.jogamp.java3d.Texture2D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.loaders.IncorrectFormatException;
import org.jogamp.java3d.loaders.ParsingErrorException;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.Color3f;

public class Fighter {
	private TransformGroup mainTG;		// Holds everything in the Fighter class; this will be referenced by the DogFight class
	
	private String fighterName = "insert_name";
	private BranchGroup planeBG;
	// add more private fields when needed
	
	public Fighter() {
		// add transformations such as translations and scales and pass into the following TransformGroup() if needed
		mainTG = new TransformGroup();
		
		// Load plane object  /!\ Change "plane" to whatever the plane .obj name is
		Plane fighter = new Plane("su-47", false);
		planeBG = fighter.getShapeBG();		// Holds the plane. Plane could be made up of just 1 Shape3D, or multiple.
		mainTG.addChild(fighter.getRootTG());
		
//		Sphere sphere = new Sphere( 3, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, 32, app );
//
//		ExplodeBehavior m_ExplodeBehavior = new ObjectSizeBehavior( (GeometryArray) sphere.getShape().getGeometry() );
//		m_ExplodeBehavior.setSchedulingBounds( getApplicationBounds() );
//		objRoot.addChild( m_SizeBehavior );
		
	}
	// Loads a .obj file; copied from the assignments
	
	public TransformGroup get_Fighter() {
		return mainTG;
	}
}

class Plane {
	private Scene s;
	private BranchGroup planeBG;
	private TransformGroup rootTG;
	private boolean textured;
	private int partsCount;
	
	public Plane(String planeFileName, boolean textured) {
		String directory = "src/DogFight/planes/" + planeFileName + ".obj";
		
//		int flags = ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY;
//		ObjectFile f = new ObjectFile(flags, (float) (60 * Math.PI / 180.0));
		File file = new File(directory);
		ObjectFile loader = new ObjectFile();
		
		try {
			s = loader.load(file.toURI().toURL());
		} catch (FileNotFoundException e) {
			System.err.println(e);
			System.exit(1);
		} catch (ParsingErrorException e) {
			System.err.println(e);
			System.exit(1);
		} catch (IncorrectFormatException e) {
			System.err.println(e);
			System.exit(1);
		} catch (Exception e) {
			System.err.println(e);
			System.exit(1);
		}
		
		planeBG = s.getSceneGroup();
		this.textured = textured;
		setAppearance(textured);
		
		rootTG = new TransformGroup();
		rootTG.addChild(planeBG);
	}
	public Plane() {
		this("Harrier", false);
	}
	protected void setAppearance(boolean textured) {
		Appearance app = getAppearance();
		
		if (textured) {
			Texture2D texture = getTexture("metal");
			app.setTexture(texture);
		}
		
		partsCount = planeBG.numChildren();
		System.out.println("Shape count: " + partsCount);
		((Shape3D) planeBG.getChild(2)).setAppearance(app);
	}
	private Appearance getAppearance() {
		Material mtl = new Material();
		Appearance app = new Appearance();
		int shine = 32;
		mtl.setShininess(shine);
		mtl.setAmbientColor(new Color3f(1f, 1f, 1f));                   			// use them to define different materials
		mtl.setDiffuseColor(new Color3f(0f, 0f, 0f));
		mtl.setSpecularColor(new Color3f(0.175000f, 0.175000f, 0.175000f));
		mtl.setEmissiveColor(new Color3f(0f, 0f, 0f));                  			// use it to enlighten a button
		mtl.setLightingEnable(true);

		app.setMaterial(mtl);                              			// set appearance's material
		return app;
	}
	private Texture2D getTexture(String name) {
		String filename = "src/DogFight/images/" + name + ".jpg";
		TextureLoader loader = new TextureLoader(filename, null);
		ImageComponent2D image = loader.getImage();
		if (image == null)
			System.out.println("Cannot open file: " + filename);
		
		Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, image.getWidth(), image.getHeight());
		texture.setImage(0, image);
		return texture;
	}
	public Scene getPlaneScene() {
		return s;
	}
	public BranchGroup getShapeBG() {
		return planeBG;
	}
	public TransformGroup getRootTG() {
		return rootTG;
	}
	public void explode() {
		Shape3D[] parts = new Shape3D[partsCount];
		TransformGroup[] partsMovement = new TransformGroup[partsCount];
		
		for (int i = 0; i < partsCount; ++i) {						// set every part of the object with the same color/texture
			parts[i] = (Shape3D) planeBG.getChild(i);
			
			partsMovement[i] = new TransformGroup();
			partsMovement[i].setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
			partsMovement[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			
			partsMovement[i].addChild(parts[i]);
			
			planeBG.removeChild(i);
			planeBG.addChild(partsMovement[i]);
		}
		
		
	}
}




