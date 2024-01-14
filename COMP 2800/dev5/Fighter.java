package DogFight;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.utils.geometry.Cone;
import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;

public class Fighter {
	private BranchGroup fighterBG;		// Holds everything in the Fighter class. This will be referenced by the DogFight class
	private Switch mainGroup;
	private String fighterName = "insert_name";
	private Plane plane;
	private ExplodingPlane exPlane;
	
	private PointSound engineSound;
	private PointSound explosionSound;
	
	public Fighter(String fighterName, String design, String playerType) {
		this.fighterName = fighterName;
		mainGroup = new Switch();
		fighterBG = new BranchGroup();
		plane = new Plane(fighterName, design, playerType);
		exPlane = new ExplodingPlane();
		mainGroup.addChild(plane.getRootTG());
		mainGroup.addChild(exPlane.getRootBG());
		fighterBG.addChild(mainGroup);
		
		mainGroup.setWhichChild(0);
		mainGroup.setCapability(Switch.ALLOW_SWITCH_WRITE);

		engineSound = new PointSound();
		fighterBG.addChild(DFCommons.pointSoundEngine("engine",engineSound));
		
		explosionSound = new PointSound();
		fighterBG.addChild(DFCommons.pointSoundExplosion("explosion", explosionSound));
		explosionSound.setEnable(false);
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
		engineSound.setEnable(false);
		explosionSound.setEnable(true);
	}
}

class Plane extends Behavior {
	private Scene s;
	private TransformGroup rootTG;
	private BranchGroup bulletBG;
	private BranchGroup planeBG;
	private int partsCount;
	private String design;
	private Queue<Bullet> unusedBullets;
	private Queue<Bullet> usedBullets;
	private Bullet[] bullets;
	private String playerType;
	
	private Transform3D planePos = new Transform3D();
	private Transform3D trfmStep = new Transform3D();
	private WakeupOnElapsedFrames wakeFrame = null;
	private float throttle = 0.005f;
	private int bulletCount = 50;
	private int shotsFired = 0;
	private Bullet bullet = null;
	private CollisionDetector cd;
	
	public Plane(String planeFileName, String design, String playerType) {
		this.playerType = playerType;
		
		String directory = "src/DogFight/planes/" + planeFileName + ".obj";
		
		this.design = design;
		s = DFCommons.load_Obj(directory, design.equals("material"));
		
		planeBG = s.getSceneGroup();
		partsCount = planeBG.numChildren();
		setDesign();
		
		planePos = new Transform3D();
		planePos.rotY(Math.PI);
		
		Transform3D translator = new Transform3D();
		translator.setTranslation(new Vector3f(0f,-0.35f,2.5f));
		
		planePos.mul(translator);
		planePos.setScale(0.5f);
		
		bulletBG = new BranchGroup();
		rootTG = new TransformGroup(planePos);
		rootTG.addChild(planeBG);
		rootTG.addChild(bulletBG);
		rootTG.addChild(this);
		rootTG.setCollidable(true);
		
		rootTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		bulletBG.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		bulletBG.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);

		create_Bullets();
		
		cd = new CollisionDetector(rootTG, bulletBG);
		cd.setSchedulingBounds(DogFight.hundredBound);
		rootTG.addChild(cd);
		this.setSchedulingBounds(DogFight.hundredBound);
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
			throttle -= offset;
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

		wakeupOn(wakeFrame);
	}
	public void create_Bullets() {
		unusedBullets = new LinkedList<Bullet>();
		bullets = new Bullet[bulletCount];
		for (int i = 0; i < bulletCount; ++i) {
			bullet = new Bullet(playerType);
			bulletBG.addChild(bullet.getRootGroup());
			unusedBullets.add(bullet);
			bullets[i] = bullet;
		}
		usedBullets = new LinkedList<Bullet>();
	}
	public void shoot() {
		bullet = unusedBullets.poll();
		if (bullet == null) return;
		bullet.enable();
		usedBullets.add(bullet);
	
		if (shotsFired == bulletCount-10) reset_First_Bullet();
		else shotsFired++;
	}
	private void reset_First_Bullet() {
		bullet = usedBullets.poll();
		if (bullet == null) return;
		bullet.disable();
		DogFight.viewTG.getTransform(planePos);
		bullet.reset_Position(planePos);
		unusedBullets.add(bullet);
	}
}

/* A class to for bullet, adjust scaling if necessary */
class Bullet extends Behavior {
	private Switch rootGroup;
	private TransformGroup bulletTG;
	private Transform3D initTrfm;
	private WakeupOnElapsedFrames wakeFrame = null;
	private Transform3D trfmStep = new Transform3D();
	private Transform3D trfm = new Transform3D();

	public Bullet(String playerType) {

		rootGroup = new Switch();
		Appearance app = new Appearance();
		setTexture(app, "brass");

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
		bulletTG.setCollidable(false);
		bulletTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		bulletTG.addChild(this);
		
		rootGroup.setCapability(Switch.ALLOW_SWITCH_WRITE);
		rootGroup.addChild(new BranchGroup());
		rootGroup.addChild(bulletTG);
		
		this.setSchedulingBounds(DogFight.tenBound);
		
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
	public Switch getRootGroup() {
		return rootGroup;
	}
	public void enable() {
		rootGroup.setWhichChild(1);
		this.setEnable(true);
	}
	public void disable() {
		rootGroup.setWhichChild(0);
		this.setEnable(false);
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