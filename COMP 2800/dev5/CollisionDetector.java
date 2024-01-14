package DogFight;

import org.jogamp.java3d.*;
import org.jogamp.vecmath.Vector3d;

import java.util.Enumeration;
import java.util.Iterator;

class CollisionDetector extends Behavior {
    public static boolean inCollision = false;
    private TransformGroup shape;
    private BranchGroup bullets;
    private ColoringAttributes shapeColoring;
    private Appearance shapeApp;
    private WakeupOnCollisionEntry wEnter;
    private WakeupOnCollisionExit wExit;

    public CollisionDetector(TransformGroup s, BranchGroup bullets) { // Corrected: gp
        
        shape = s;
        this.bullets = bullets;

        inCollision = false;
    }

    public void initialize() {
        wEnter = new WakeupOnCollisionEntry(shape,WakeupOnCollisionEntry.USE_BOUNDS);
        wExit = new WakeupOnCollisionExit(shape, WakeupOnCollisionExit.USE_BOUNDS);
        wakeupOn(wEnter);
    }


    public void processStimulus(Iterator<WakeupCriterion> iterator) {
        //System.out.println("inCollision");
        inCollision = !inCollision;
        if (inCollision) {
        	
//        	if (!bullets.getBounds().intersect(wEnter.getTriggeringBounds())) {
        	if (!shape.getBounds().intersect(bullets.getBounds())) {
        		DogFight.f1.explode();
        	}
            wakeupOn(wExit);



        } else {
            //System.out.println("notInCollision");

            wakeupOn(wEnter);
        }
    }
}

class BulletCollision extends Behavior {
	public static boolean inCollision = false;
    private BranchGroup bulletBG;
    private WakeupOnCollisionEntry wEnter;
    private WakeupOnCollisionExit wExit;
    private Plane plane;

	public BulletCollision(BranchGroup bg, Plane plane) {
		bulletBG = bg;
		inCollision = false;
		this.plane = plane;
	}

	@Override
	public void initialize() {
		wEnter = new WakeupOnCollisionEntry(bulletBG,WakeupOnCollisionEntry.USE_BOUNDS);
        wExit = new WakeupOnCollisionExit(bulletBG, WakeupOnCollisionExit.USE_BOUNDS);
        wakeupOn(wEnter);
	}

	@Override
	public void processStimulus(Iterator<WakeupCriterion> arg0) {
		inCollision = !inCollision;
        if (inCollision) {
			wEnter.getTriggeringBounds();
//    		DogFight.f1.explode();
            wakeupOn(wExit);
        } else {
            wakeupOn(wEnter);
        }
	}
	
}