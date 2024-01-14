package DogFight;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.jogamp.java3d.Alpha;
import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.Behavior;
import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.ColoringAttributes;
import org.jogamp.java3d.Geometry;
import org.jogamp.java3d.GeometryArray;
import org.jogamp.java3d.ImageComponent2D;
import org.jogamp.java3d.LineArray;
import org.jogamp.java3d.Material;
import org.jogamp.java3d.OrientedShape3D;
import org.jogamp.java3d.PointArray;
import org.jogamp.java3d.QuadArray;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Switch;
import org.jogamp.java3d.Texture;
import org.jogamp.java3d.Texture2D;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.TransparencyAttributes;
import org.jogamp.java3d.TransparencyInterpolator;
import org.jogamp.java3d.TriangleArray;
import org.jogamp.java3d.WakeupCriterion;
import org.jogamp.java3d.WakeupOnElapsedFrames;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.utils.geometry.Cone;
import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;

public class Fighter {
	private BranchGroup fighterBG;		// Holds everything in the Fighter class; this will be referenced by the DogFight class
	private Switch mainGroup;
	private String fighterName = "insert_name";
	private Plane plane;
	private ExplodingPlane exPlane;
	private Fighter enemy;
	// add more private fields when needed
	
	public Fighter(String fighterName, String design) {
		this.fighterName = fighterName;
		mainGroup = new Switch();
		fighterBG = new BranchGroup();
		plane = new Plane(fighterName, design);
		exPlane = new ExplodingPlane();
		mainGroup.addChild(plane.getRootTG());
		mainGroup.addChild(exPlane.getRootBG());
//		mainGroup.addChild(new Plane(fighterName, "appearance").getRootTG());
		fighterBG.addChild(mainGroup);
		
		mainGroup.setWhichChild(0);
		mainGroup.setCapability(Switch.ALLOW_SWITCH_WRITE);
	}
	public void setEnemy(Fighter enemy) {
		this.enemy = enemy;
		plane.setEnemy(enemy.get_Plane());
	}
	public Switch getFighterGroup() {
		return mainGroup;
	}
	public BranchGroup getFighterBG() {
		return fighterBG;
	}
	public Plane get_Plane() {
		return plane;
	}
	public void explode() {
		exPlane.explode();
		mainGroup.setWhichChild(1);
	}
}

class Plane extends Behavior {
	private Scene s;
	private TransformGroup rootTG;
	private BranchGroup bulletBG;
	private BranchGroup planeBG;
	private int partsCount;
	private String design;
	private Transform3D trfm;
	private Queue<Bullet> unusedBullets;
	private Queue<Bullet> usedBullets;
	private Queue<BranchGroup> unusedMag;
	private Queue<BranchGroup> usedMag;
	
	private Transform3D trfmStep = new Transform3D();
	private WakeupOnElapsedFrames wakeFrame = null;
	private float throttle = 0.1f;
	private int bulletCount = 50;
	private int shotsFired = 0;
	private Bullet bullet_temp = null;
	private BranchGroup mag_temp = null;
	
	private Plane enemy;
	private Vector3f vector = new Vector3f();
	private Transform3D planePos = new Transform3D();
	private Transform3D enemyPos;
	private LineArray tracker;
	private Shape3D shape;
	
	private final int pointCount = 4;
	
	public Plane(String planeFileName, String design) {
		String directory = "src/DogFight/planes/" + planeFileName + ".obj";
		
		this.design = design;
		s = DFCommons.load_Obj(directory, design.equals("material"));
		
		planeBG = s.getSceneGroup();
		partsCount = planeBG.numChildren();
		setDesign();
		
		trfm = new Transform3D();
		trfm.rotY(Math.PI);
		
		Transform3D translator = new Transform3D();
		translator.setTranslation(new Vector3f(0f,-0.35f,2.5f));
		trfm.mul(translator);
		trfm.setScale(0.5f);
		
		bulletBG = new BranchGroup();
		rootTG = new TransformGroup(trfm);
		rootTG.addChild(planeBG);
		rootTG.addChild(bulletBG);
		rootTG.addChild(this);
		
		rootTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		bulletBG.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		bulletBG.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		
		this.setSchedulingBounds(DogFight.thousandBound);
		
		create_Bullets();
	}
	public void setEnemy(Plane enemy) {
		this.enemy = enemy;
		addTracker(DFCommons.Green);
	}
	public void addTracker(Color3f color) {
//		Point3f planeCoordinates = getPoint(plane.getRootTG());
//		Point3f enemyCoordinates = getPoint(enemy.get_Plane().getRootTG());
		rootTG.getTransform(planePos);
		planePos.get(vector);
		Point3f planeCoordinates = new Point3f();
		vector.get(planeCoordinates);
		
		enemy.getRootTG().getTransform(planePos);
		planePos.get(vector);
		Point3f enemyCoordinates = new Point3f();
		vector.get(enemyCoordinates);
		tracker = new LineArray(pointCount, LineArray.COLOR_3 | LineArray.COORDINATES);
		
		for (int i = 0; i < pointCount / 2; ++i) {
			tracker.setCoordinate(i*2, planeCoordinates);
			tracker.setCoordinate((i*2)+1, enemyCoordinates);
			
			tracker.setColor(i*2, color);
			tracker.setColor((i*2)+1, color);
		}
		tracker.setCapability(LineArray.ALLOW_COORDINATE_READ);
		tracker.setCapability(LineArray.ALLOW_COORDINATE_WRITE);
		
		shape = new Shape3D(tracker, new Appearance());
		shape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
		
//		rootTG.addChild(this);
		rootTG.addChild(shape);
	}
	private Point3f getPoint(TransformGroup tg) {
		tg.getTransform(planePos);
		planePos.get(vector);
		Point3f point = new Point3f();
		vector.get(point);
		return point;
	}
	protected void setDesign() {
		Appearance app = null;
		if (design.equals("material"))
			return;
		
		if (design.equals("texture")) {
			Texture2D texture = getTexture("metal");
			app = new Appearance();
			app.setTexture(texture);
		} else {
			app = getAppearance();
		}
		for (int i = 0; i < partsCount; ++i) {
			((Shape3D) planeBG.getChild(i)).setAppearance(app);
		}
		
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
	public void accelerate(float offset) {
		if (0.01f <= throttle+offset && throttle+offset <= 1f)
			throttle += offset;
	}
	@Override
	public void initialize() {
		wakeFrame = new WakeupOnElapsedFrames(0);
		wakeupOn(wakeFrame);
	}
	@Override
	public void processStimulus(Iterator<WakeupCriterion> arg0) {
		trfmStep.set(new Vector3d(0.0, 0.0, -throttle));
		DogFight.viewTG.getTransform(planePos);
		planePos.mul(trfmStep);
		DogFight.viewTG.setTransform(planePos);
		
		Point3f planeCoord = getPoint(DogFight.viewTG);
		Point3f enemyCoord = getPoint(enemy.getRootTG());
		
		tracker.setCoordinate(0, planeCoord);
		tracker.setCoordinate(3, planeCoord);
		tracker.setCoordinate(1, enemyCoord);
		tracker.setCoordinate(2, enemyCoord);
		shape.setGeometry(tracker);
		
		wakeupOn(wakeFrame);
	}
	public void create_Bullets() {
		unusedBullets = new LinkedList<Bullet>();
		unusedMag = new LinkedList<BranchGroup>();
		bullet_temp = null;
		for (int i = 0; i < bulletCount; ++i) {
			bullet_temp = new Bullet();
			unusedBullets.add(bullet_temp);
			unusedMag.add(bullet_temp.getRootBG());
		}
		usedBullets = new LinkedList<Bullet>();
		usedMag = new LinkedList<BranchGroup>();
	}
	public void shoot() {
		bullet_temp = unusedBullets.poll();
		if (bullet_temp == null) return;
		bulletBG.addChild(bullet_temp.getRootBG());
		bullet_temp.setEnable(true);
		usedBullets.add(bullet_temp);
		
		mag_temp = unusedMag.poll();
		if (mag_temp == null) return;
		usedMag.add(mag_temp);
	
		if (shotsFired == bulletCount-10) reset_First_Bullet();
		else shotsFired++;
	}
	private void reset_First_Bullet() {
		bullet_temp = usedBullets.poll();
		if (bullet_temp == null) return;
		bullet_temp.setEnable(false);
		planePos = new Transform3D();
		DogFight.viewTG.getTransform(planePos);
		bullet_temp.reset_Position(planePos);
		unusedBullets.add(bullet_temp);
		
		mag_temp = usedMag.poll();
		if (mag_temp == null) return;
		mag_temp.detach();
		unusedMag.add(mag_temp);
	}
}

/* A class to for bullet, adjust scaling if necessary */
class Bullet extends Behavior {
	private BranchGroup rootBG;
	private TransformGroup bulletTG;
	private Transform3D initTrfm;
	private WakeupOnElapsedFrames wakeFrame = null;
	private Transform3D trfmStep = new Transform3D();
	private Transform3D trfm = new Transform3D();

	public Bullet() {
		rootBG = new BranchGroup();
		Appearance app = new Appearance();
		setTexture(app, "steel");

		Cylinder cylinder = new Cylinder(0.15f, 0.5f, Cylinder.GENERATE_NORMALS | Cylinder.GENERATE_TEXTURE_COORDS, app);
		Cone cone = new Cone(0.15f, 0.5f, Cone.GENERATE_NORMALS | Cone.GENERATE_TEXTURE_COORDS, app);
		
		Transform3D coneTranslator = new Transform3D();
		coneTranslator.setTranslation(new Vector3f(0.0f, 0.5f, 0.0f));
		TransformGroup coneTG = new TransformGroup(coneTranslator);
		coneTG.addChild(cone);
		
		Transform3D scaler = new Transform3D();
		scaler.setScale(0.1f);
		Transform3D rotator = new Transform3D();
		rotator.rotX(Math.PI / 2.0f);
		scaler.mul(rotator);
		TransformGroup orientTG = new TransformGroup(scaler);
		orientTG.addChild(cylinder);
		orientTG.addChild(coneTG);
		
		initTrfm = new Transform3D();
		initTrfm.setTranslation(new Vector3f(0.0f, -0.21f, 0.0f));

		bulletTG = new TransformGroup(initTrfm);
		bulletTG.addChild(orientTG);

		bulletTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		rootBG.setCapability(BranchGroup.ALLOW_DETACH);
		rootBG.addChild(bulletTG);
		rootBG.addChild(this);
		
		this.setSchedulingBounds(DogFight.thousandBound);
		
		this.setEnable(false);
	}
	protected void setTexture(Appearance app, String name) {
		Texture2D texture = getTexture(name);
		app.setTexture(texture);
	}
	private Texture2D getTexture(String fileName) {
		String path = "src/DogFight/images/" + fileName + ".jpg";
		TextureLoader loader = new TextureLoader(path, null);
		ImageComponent2D image = loader.getImage();        // load the image
		if (image == null)
			System.out.println("Cannot open file: " + fileName);

		Texture2D texture = new Texture2D(Texture.BASE_LEVEL,
				Texture.RGBA, image.getWidth(), image.getHeight());
		texture.setImage(0, image);	// set image for the texture
		return texture;
	}
	public void reset_Position(Transform3D planePos) {
		bulletTG.setTransform(initTrfm);
	}
	public BranchGroup getRootBG() {
		return rootBG;
	}
	@Override
	public void initialize() {
		wakeFrame = new WakeupOnElapsedFrames(0);
		wakeupOn(wakeFrame);
	}
	@Override
	public void processStimulus(Iterator<WakeupCriterion> arg0) {
		trfmStep.set(new Vector3d(0.0, 0.0, 1.0));
		bulletTG.getTransform(trfm);
		trfm.mul(trfmStep);
		bulletTG.setTransform(trfm);
		wakeupOn(wakeFrame);
	}
}

class ExplodingPlane {
	private BranchGroup rootBG;
	private BranchGroup[] partBG;
	private TransformGroup[] posTG;
	
	private final int partsCount = 7;
	
	private ExplosionBehavior[] explodingParts;
	
	public ExplodingPlane() {
		rootBG = new BranchGroup();
		posTG = new TransformGroup[partsCount];
		partBG = new BranchGroup[partsCount];
		explodingParts = new ExplosionBehavior[partsCount];
		
		String directory = "src/DogFight/planes/su-47/";
		String[] partNames = {"body", "left_horizontal", "left_vertical", "left_wing", "right_horizontal", "right_vertical", "right_wing"};
		Transform3D temp1Trfm = new Transform3D();
		Transform3D temp2Trfm = new Transform3D();
		Transform3D temp3Trfm = new Transform3D();
		temp1Trfm.rotY(Math.PI);
		temp2Trfm.setTranslation(new Vector3f(0f,-0.25f,1.65f));
		temp1Trfm.mul(temp2Trfm);
		rootBG.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		for (int i = 0; i < partsCount; ++i) {
			partBG[i] = DFCommons.load_Obj(directory + partNames[i] + ".obj", true).getSceneGroup();
			partBG[i].setCapability(BranchGroup.ALLOW_DETACH);
			posTG[i] = new TransformGroup(temp1Trfm);
			posTG[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			posTG[i].addChild(partBG[i]);
			rootBG.addChild(posTG[i]);
		}
		
		int i = 0;
		// body
		posTG[i].getTransform(temp1Trfm);
		temp2Trfm.setScale(0.5);
		temp1Trfm.mul(temp2Trfm);
		posTG[i].setTransform(temp1Trfm);
		explodingParts[i] = new ExplosionBehavior(posTG[i], 0, -0.001, 0, 0.562);
		
		++i;
		// left_horizontal
		posTG[i].getTransform(temp1Trfm);
		temp2Trfm.setScale(0.1);
		temp3Trfm.setTranslation(new Vector3f(1.6f, -0.5f, 0f));
		temp1Trfm.mul(temp2Trfm);
		temp1Trfm.mul(temp3Trfm);
		posTG[i].setTransform(temp1Trfm);
		explodingParts[i] = new ExplosionBehavior(posTG[i], 0.001, -0.001, 0.001, 0.225);
		
		++i;
		// left_vertical
		posTG[i].getTransform(temp1Trfm);
		temp2Trfm.setScale(0.085);
		temp3Trfm.setTranslation(new Vector3f(1.75f, 0.2f, -4f));
		temp1Trfm.mul(temp2Trfm);
		temp1Trfm.mul(temp3Trfm);
		posTG[i].setTransform(temp1Trfm);
		explodingParts[i] = new ExplosionBehavior(posTG[i], 0.001, 0.001, -0.001, 0.64);
		
		++i;
		// left_wing
		posTG[i].getTransform(temp1Trfm);
		temp2Trfm.setScale(0.18);
		temp3Trfm.setTranslation(new Vector3f(1.8f, -0.3f, -1f));
		temp1Trfm.mul(temp2Trfm);
		temp1Trfm.mul(temp3Trfm);
		posTG[i].setTransform(temp1Trfm);
		explodingParts[i] = new ExplosionBehavior(posTG[i], 0.001, -0.001, 0, 0.19);
		
		++i;
		// right_horizontal
		posTG[i].getTransform(temp1Trfm);
		temp2Trfm.setScale(0.1);
		temp3Trfm.setTranslation(new Vector3f(-1.6f, -0.5f, 0f));
		temp1Trfm.mul(temp2Trfm);
		temp1Trfm.mul(temp3Trfm);
		posTG[i].setTransform(temp1Trfm);
		explodingParts[i] = new ExplosionBehavior(posTG[i], -0.001, -0.001, 0.001, 0.3386);
		
		++i;
		// right_vertical
		posTG[i].getTransform(temp1Trfm);
		temp2Trfm.setScale(0.085);
		temp3Trfm.setTranslation(new Vector3f(-1.75f, 0.5f, -4f));
		temp1Trfm.mul(temp2Trfm);
		temp1Trfm.mul(temp3Trfm);
		posTG[i].setTransform(temp1Trfm);
		explodingParts[i] = new ExplosionBehavior(posTG[i], -0.001, 0.001, -0.001, 0.853);
		
		++i;
		// right_wing
		posTG[i].getTransform(temp1Trfm);
		temp2Trfm.setScale(0.18);
		temp3Trfm.setTranslation(new Vector3f(-1.8f, -0.3f, -1f));
		temp1Trfm.mul(temp2Trfm);
		temp1Trfm.mul(temp3Trfm);
		posTG[i].setTransform(temp1Trfm);
		explodingParts[i] = new ExplosionBehavior(posTG[i], -0.001, -0.001, 0, 0.189);
		
		
	}
	public BranchGroup getRootBG() {
		return rootBG;
	}
	public void explode() {
		for (int i = 0; i < partsCount; ++i) {
			explodingParts[i].setEnable(true);
		}
	}
}

class ExplosionBehavior extends Behavior {
	private TransformGroup rootTG;
	private BranchGroup partBG;
	private WakeupOnElapsedFrames wakeFrame;
	
	private double x;
	private double y;
	private double z;
	
	private double distance;
	private double rate;
	
	private Transform3D trfmStep = new Transform3D();
	private Transform3D trfm = new Transform3D();
	
	public ExplosionBehavior(TransformGroup rootTG, double x, double y, double z, double rate) {
		this.rootTG = rootTG;
		this.x = x;
		this.y = y;
		this.z = z;
		this.rate = rate;
		
		partBG = (BranchGroup) rootTG.getChild(0);
		
		distance = Math.sqrt(x*x + y*y + z*z);
		
		this.setEnable(false);
		
		rootTG.addChild(this);
		
		this.setSchedulingBounds(DogFight.thousandBound);
	}
	@Override
	public void initialize() {
		wakeFrame = new WakeupOnElapsedFrames(0);
		wakeupOn(wakeFrame);
	}

	@Override
	public void processStimulus(Iterator<WakeupCriterion> arg0) {
		trfmStep.set(new Vector3d(rate * x / distance, rate * y / distance, rate * z / distance));
		if (y - 0.1 > 0) {
			y -= 0.1;
		}
		rootTG.getTransform(trfm);
		trfm.mul(trfmStep);
		rootTG.setTransform(trfm);
		wakeupOn(wakeFrame);
	}
}