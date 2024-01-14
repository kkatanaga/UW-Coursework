/**********************************************************
 Copyright (C) 2001   Daniel Selman

 First distributed with the book "Java 3D Programming"
 by Daniel Selman and published by Manning Publications.
 http://manning.com/selman

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation, version 2.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 The license can be found on the WWW at:
 http://www.fsf.org/copyleft/gpl.html

 Or by writing to:
 Free Software Foundation, Inc.,
 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

 Authors can be contacted at:
 Daniel Selman: daniel@selman.org

 If you make changes you think others would like, please 
 contact one of the authors or someone at the 
 www.j3d.org web site.
 **************************************************************/
package DogFight;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.GraphicsConfigTemplate;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;

import org.jogamp.java3d.Alpha;
import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.AudioDevice;
import org.jogamp.java3d.Background;
import org.jogamp.java3d.Behavior;
import org.jogamp.java3d.BoundingBox;
import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.Bounds;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Canvas3D;
import org.jogamp.java3d.GeometryArray;
import org.jogamp.java3d.GraphicsConfigTemplate3D;
import org.jogamp.java3d.Group;
import org.jogamp.java3d.Locale;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.PhysicalBody;
import org.jogamp.java3d.PhysicalEnvironment;
import org.jogamp.java3d.PointAttributes;
import org.jogamp.java3d.PolygonAttributes;
import org.jogamp.java3d.RotationInterpolator;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Switch;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.TransparencyAttributes;
import org.jogamp.java3d.View;
import org.jogamp.java3d.ViewPlatform;
import org.jogamp.java3d.VirtualUniverse;
import org.jogamp.java3d.WakeupCondition;
import org.jogamp.java3d.WakeupCriterion;
import org.jogamp.java3d.WakeupOnAWTEvent;
import org.jogamp.java3d.WakeupOnElapsedFrames;
import org.jogamp.java3d.WakeupOnElapsedTime;
import org.jogamp.java3d.WakeupOr;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;

import org.jogamp.java3d.audioengines.javasound.JavaSoundMixer;
import org.jogamp.java3d.utils.applet.MainFrame;
import org.jogamp.java3d.utils.geometry.ColorCube;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.image.TextureLoader;

/**
 * Creates five sample behaviors and applies them to an object in a scene:
 * <p>
 * 1. Object Size - displays the size of the geometry in the object 2. Explode -
 * explodes the geometry after 10 seconds 3. Stretch - allows the geometry to be
 * stretched using the spacebar (simple physics) 4. Bounds - displays the bounds
 * of the object 5. FPS - displays frames-per-seconds rendered
 */
public class BehaviorTest extends Java3dApplet implements ExplosionListener,
    ActionListener {
  private static int m_kWidth = 350;

  private static int m_kHeight = 400;

  private RotationInterpolator m_RotationInterpolator = null;

  private StretchBehavior m_StretchBehavior = null;

  private ObjectSizeBehavior m_SizeBehavior = null;

  private ExplodeBehavior m_ExplodeBehavior = null;

  private FpsBehavior m_FpsBehavior = null;

  private BoundsBehavior m_BoundsBehavior = null;

  public BehaviorTest() {
    initJava3d();

    Panel controlPanel = new Panel();

    Button rotateButton = new Button("Rotate");
    rotateButton.addActionListener(this);
    controlPanel.add(rotateButton);

    Button objSizeButton = new Button("Object Size");
    objSizeButton.addActionListener(this);
    controlPanel.add(objSizeButton);

    Button explodeButton = new Button("Explode");
    explodeButton.addActionListener(this);
    controlPanel.add(explodeButton);

    Button stretchButton = new Button("Stretch");
    stretchButton.addActionListener(this);
    controlPanel.add(stretchButton);

    Button boundsButton = new Button("Bounds");
    boundsButton.addActionListener(this);
    controlPanel.add(boundsButton);

    Button fpsButton = new Button("FPS");
    fpsButton.addActionListener(this);
    controlPanel.add(fpsButton);

    add(controlPanel, BorderLayout.SOUTH);
  }

  // handle event from the GUI components we created
  public void actionPerformed(ActionEvent event) {
    if (event.getActionCommand().equals("Object Size") != false)
      m_SizeBehavior.setEnable(!m_SizeBehavior.getEnable());

    else if (event.getActionCommand().equals("Explode") != false)
      m_ExplodeBehavior.setEnable(!m_ExplodeBehavior.getEnable());

    else if (event.getActionCommand().equals("Stretch") != false)
      m_StretchBehavior.setEnable(!m_StretchBehavior.getEnable());

    else if (event.getActionCommand().equals("Rotate") != false)
      m_RotationInterpolator.setEnable(!m_RotationInterpolator
          .getEnable());

    else if (event.getActionCommand().equals("Bounds") != false)
      m_BoundsBehavior.setEnable(!m_BoundsBehavior.getEnable());

    else if (event.getActionCommand().equals("FPS") != false)
      m_FpsBehavior.setEnable(!m_FpsBehavior.getEnable());
    
  }

  protected Background createBackground() {
    return null;
  }

  protected BranchGroup createSceneBranchGroup() {
    BranchGroup objRoot = super.createSceneBranchGroup();

    // create a TransformGroup to rotate the hand
    TransformGroup objTrans = new TransformGroup();
    objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

    // create a RotationInterpolator behavior to rotate the hand
    Transform3D yAxis = new Transform3D();
    Alpha rotationAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE, 0, 0,
        4000, 0, 0, 0, 0, 0);

    m_RotationInterpolator = new RotationInterpolator(rotationAlpha,
        objTrans, yAxis, 0.0f, (float) Math.PI * 2.0f);
    m_RotationInterpolator.setSchedulingBounds(createApplicationBounds());
    objTrans.addChild(m_RotationInterpolator);

    // create an Appearance and Material
    Appearance app = new Appearance();

    TextureLoader tex = new TextureLoader("src/DogFight/images/earth.png", this);
    app.setTexture(tex.getTexture());

    Sphere sphere = new Sphere(3, Primitive.GENERATE_NORMALS
        | Primitive.GENERATE_TEXTURE_COORDS, 32, app);

    // connect the scenegraph
    objTrans.addChild(sphere);
    objRoot.addChild(objTrans);

    m_FpsBehavior = new FpsBehavior();
    m_FpsBehavior.setSchedulingBounds(getApplicationBounds());
    objRoot.addChild(m_FpsBehavior);

    m_BoundsBehavior = new BoundsBehavior(sphere);
    m_BoundsBehavior.setSchedulingBounds(getApplicationBounds());
    m_BoundsBehavior.addBehaviorToParentGroup(objTrans);

    m_StretchBehavior = new StretchBehavior((GeometryArray) sphere
        .getShape().getGeometry());
    m_StretchBehavior.setSchedulingBounds(getApplicationBounds());
    objRoot.addChild(m_StretchBehavior);
    m_StretchBehavior.setEnable(false);

    m_SizeBehavior = new ObjectSizeBehavior((GeometryArray) sphere
        .getShape().getGeometry());
    m_SizeBehavior.setSchedulingBounds(getApplicationBounds());
    objRoot.addChild(m_SizeBehavior);
    m_SizeBehavior.setEnable(false);

    m_ExplodeBehavior = new ExplodeBehavior(sphere.getShape(), 10000, 20,
        this);
    m_ExplodeBehavior.setSchedulingBounds(getApplicationBounds());
    objRoot.addChild(m_ExplodeBehavior);

    return objRoot;
  }

  public WakeupCondition onExplosionFinished(ExplodeBehavior explodeBehavior,
      Shape3D shape3D) {
    System.out.println("Explosion Finished.");
    return explodeBehavior.restart(shape3D, 10000, 20, this);
  }

  public static void main(String[] args) {
    Applet behaviorTest = new BehaviorTest();
    ((Java3dApplet) behaviorTest).saveCommandLineArguments(args);

    new MainFrame(behaviorTest, m_kWidth, m_kHeight);
  }
}

//this class implements a simple behavior that
//output the rendered Frames Per Second.

class FpsBehavior extends Behavior {
  // the wake up condition for the behavior
  protected WakeupCondition m_WakeupCondition = null;

  protected long m_StartTime = 0;

  private final int m_knReportInterval = 100;

  public FpsBehavior() {
    // save the WakeupCriterion for the behavior
    m_WakeupCondition = new WakeupOnElapsedFrames(m_knReportInterval);
  }

  public void initialize() {
    // apply the initial WakeupCriterion
    wakeupOn(m_WakeupCondition);
  }

  public void processStimulus(java.util.Enumeration criteria) {
    while (criteria.hasMoreElements()) {
      WakeupCriterion wakeUp = (WakeupCriterion) criteria.nextElement();

      // every N frames, update position of the graphic
      if (wakeUp instanceof WakeupOnElapsedFrames) {
        if (m_StartTime > 0) {
          final long interval = System.currentTimeMillis()
              - m_StartTime;
          System.out.println("FPS: " + (m_knReportInterval * 1000)
              / interval);
        }

        m_StartTime = System.currentTimeMillis();
      }
    }

    // assign the next WakeUpCondition, so we are notified again
    wakeupOn(m_WakeupCondition);
  }

@Override
public void processStimulus(Iterator<WakeupCriterion> arg0) {
	// TODO Auto-generated method stub
	
}
}

//this class implements a simple behavior that
//displays a graphical representation of the Bounds
//of a Node.

class BoundsBehavior extends Behavior {
  // the wake up condition for the behavior
  protected WakeupCondition m_WakeupCondition = null;

  // the Node that we are tracking
  protected Node m_Node = null;

  protected TransformGroup m_TransformGroup = null;

  protected Switch m_BoundsSwitch = null;

  protected Transform3D m_Transform3D = null;

  protected Vector3d m_Scale = null;

  protected Vector3d m_Vector3d = null;

  protected Point3d m_Point3d1 = null;

  protected Point3d m_Point3d2 = null;

  private final int m_kSphereBounds = 0;

  private final int m_kBoxBounds = 1;

  public BoundsBehavior(Node node) {
    // save the GeometryArray that we are modifying
    m_Node = node;

    m_Transform3D = new Transform3D();
    m_Scale = new Vector3d();
    m_Vector3d = new Vector3d();
    m_Point3d1 = new Point3d();
    m_Point3d2 = new Point3d();

    // set the capability bits that the behavior requires
    m_Node.setCapability(Node.ALLOW_BOUNDS_READ);

    // save the WakeupCriterion for the behavior
    m_WakeupCondition = new WakeupOnElapsedFrames(10);
  }

  public void addBehaviorToParentGroup(Group nodeParentGroup) {
    nodeParentGroup.addChild(this);

    m_TransformGroup = new TransformGroup();
    m_TransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

    m_BoundsSwitch = new Switch();
    m_BoundsSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);

    Appearance app = new Appearance();

    PolygonAttributes polyAttrbutes = new PolygonAttributes();
    polyAttrbutes.setPolygonMode(PolygonAttributes.POLYGON_LINE);
    polyAttrbutes.setCullFace(PolygonAttributes.CULL_NONE);
    app.setPolygonAttributes(polyAttrbutes);

    m_BoundsSwitch.addChild(new Sphere(1, app));

    ColorCube cube = new ColorCube();
    cube.setAppearance(app);

    Group g = new Group();
    g.addChild(cube);
    m_BoundsSwitch.addChild(g);

    m_BoundsSwitch.setWhichChild(Switch.CHILD_NONE);

    m_TransformGroup.addChild(m_BoundsSwitch);
    nodeParentGroup.addChild(m_TransformGroup);
  }

  public void setEnable(boolean bEnable) {
    super.setEnable(bEnable);

    if (m_BoundsSwitch != null) {
      if (bEnable == false)
        m_BoundsSwitch.setWhichChild(Switch.CHILD_NONE);
    }
  }

  public void initialize() {
    // apply the initial WakeupCriterion
    wakeupOn(m_WakeupCondition);
  }

  public void processStimulus(java.util.Enumeration criteria) {
    while (criteria.hasMoreElements()) {
      WakeupCriterion wakeUp = (WakeupCriterion) criteria.nextElement();

      // every N frames, update position of the graphic
      if (wakeUp instanceof WakeupOnElapsedFrames) {
        if (m_TransformGroup != null) {
          Bounds bounds = m_Node.getBounds();

          int nBoundsType = m_kBoxBounds;
          m_Transform3D.setIdentity();

          if (bounds instanceof BoundingSphere) {
            nBoundsType = m_kSphereBounds;

            ((BoundingSphere) bounds).getCenter(m_Point3d1);

            m_Vector3d.x = m_Point3d1.x;
            m_Vector3d.y = m_Point3d1.y;
            m_Vector3d.z = m_Point3d1.z;

            m_Scale.x = ((BoundingSphere) bounds).getRadius() / 2;
            m_Scale.y = m_Scale.x;
            m_Scale.z = m_Scale.y;

          } else if (bounds instanceof BoundingBox) {
            nBoundsType = m_kBoxBounds;

            ((BoundingBox) bounds).getLower(m_Point3d1);
            ((BoundingBox) bounds).getUpper(m_Point3d2);

            m_Vector3d.x = (m_Point3d1.x + m_Point3d2.x) / 2;
            m_Vector3d.y = (m_Point3d1.y + m_Point3d2.y) / 2;
            m_Vector3d.z = (m_Point3d1.z + m_Point3d2.z) / 2;

            m_Scale.x = Math.abs(m_Point3d1.x - m_Point3d2.x) / 2;
            m_Scale.y = Math.abs(m_Point3d1.y - m_Point3d2.y) / 2;
            m_Scale.z = Math.abs(m_Point3d1.z - m_Point3d2.z) / 2;
          } else
            System.err
                .println("BoundsBehavior found a Bounds it cannot represent: "
                    + bounds);

          m_Transform3D.setScale(m_Scale);
          m_Transform3D.setTranslation(m_Vector3d);

          m_TransformGroup.setTransform(m_Transform3D);

          m_BoundsSwitch.setWhichChild(nBoundsType);
        } else {
          System.err
              .println("Call addBehaviorToParentGroup for BoundsBehavior.");
        }
      }
    }

    // assign the next WakeUpCondition, so we are notified again
    wakeupOn(m_WakeupCondition);
  }

@Override
public void processStimulus(Iterator<WakeupCriterion> arg0) {
	// TODO Auto-generated method stub
	
}
}

class ExplodeBehavior extends Behavior {
  // the wake up condition for the behavior
  protected WakeupCondition m_InitialWakeupCondition = null;

  protected WakeupCondition m_FrameWakeupCondition = null;

  // the GeometryArray for the Shape3D that we are modifying
  protected Shape3D m_Shape3D = null;

  protected GeometryArray m_GeometryArray = null;

  protected float[] m_CoordinateArray = null;

  protected float[] m_OriginalCoordinateArray = null;

  protected Appearance m_Appearance = null;

  protected TransparencyAttributes m_TransparencyAttributes = null;

  protected int m_nElapsedTime = 0;

  protected int m_nNumFrames = 0;

  protected int m_nFrameNumber = 0;

  protected Vector3f m_Vector = null;

  ExplosionListener m_Listener = null;

  public ExplodeBehavior(Shape3D shape3D, int nElapsedTime, int nNumFrames,
      ExplosionListener listener) {
    // allocate a temporary vector
    m_Vector = new Vector3f();

    m_FrameWakeupCondition = new WakeupOnElapsedFrames(1);

    restart(shape3D, nElapsedTime, nNumFrames, listener);
  }

  public WakeupCondition restart(Shape3D shape3D, int nElapsedTime,
      int nNumFrames, ExplosionListener listener) {
    System.out.println("Will explode after: " + nElapsedTime / 1000
        + " secs.");

    m_Shape3D = shape3D;
    m_nElapsedTime = nElapsedTime;
    m_nNumFrames = nNumFrames;
    m_nFrameNumber = 0;

    // create the WakeupCriterion for the behavior
    m_InitialWakeupCondition = new WakeupOnElapsedTime(m_nElapsedTime);

    m_Listener = listener;

    // save the GeometryArray that we are modifying
    m_GeometryArray = (GeometryArray) m_Shape3D.getGeometry();

    if (m_Shape3D.isLive() == false && m_Shape3D.isCompiled() == false) {
      // set the capability bits that the behavior requires
      m_Shape3D.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
      m_Shape3D.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);

      m_Shape3D.getAppearance().setCapability(
          Appearance.ALLOW_POINT_ATTRIBUTES_WRITE);
      m_Shape3D.getAppearance().setCapability(
          Appearance.ALLOW_POLYGON_ATTRIBUTES_WRITE);
      m_Shape3D.getAppearance().setCapability(
          Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
      m_Shape3D.getAppearance().setCapability(
          Appearance.ALLOW_TEXTURE_WRITE);

      m_GeometryArray.setCapability(GeometryArray.ALLOW_COORDINATE_READ);
      m_GeometryArray.setCapability(GeometryArray.ALLOW_COORDINATE_WRITE);
      m_GeometryArray.setCapability(GeometryArray.ALLOW_COUNT_READ);
    }

    // make a copy of the object's original appearance
    m_Appearance = new Appearance();
    m_Appearance = (Appearance) m_Shape3D.getAppearance()
        .cloneNodeComponent(true);

    // allocate an array for the model coordinates
    m_CoordinateArray = new float[3 * m_GeometryArray.getVertexCount()];

    // make a copy of the models original coordinates
    m_OriginalCoordinateArray = new float[3 * m_GeometryArray
        .getVertexCount()];
    m_GeometryArray.getCoordinates(0, m_OriginalCoordinateArray);

    // start (or restart) the behavior
    setEnable(true);

    return m_InitialWakeupCondition;
  }

  public void initialize() {
    // apply the initial WakeupCriterion
    wakeupOn(m_InitialWakeupCondition);
  }

  public void processStimulus(java.util.Enumeration criteria) {
    while (criteria.hasMoreElements()) {
      WakeupCriterion wakeUp = (WakeupCriterion) criteria.nextElement();

      if (wakeUp instanceof WakeupOnElapsedTime) {
        // we are starting the explosion, apply the
        // appearance changes we require
        PolygonAttributes polyAttribs = new PolygonAttributes(
            PolygonAttributes.POLYGON_POINT,
            PolygonAttributes.CULL_NONE, 0);
        m_Shape3D.getAppearance().setPolygonAttributes(polyAttribs);

        PointAttributes pointAttribs = new PointAttributes(3, false);
        m_Shape3D.getAppearance().setPointAttributes(pointAttribs);

        m_Shape3D.getAppearance().setTexture(null);

        m_TransparencyAttributes = new TransparencyAttributes(
            TransparencyAttributes.NICEST, 0);
        m_TransparencyAttributes
            .setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
        m_Shape3D.getAppearance().setTransparencyAttributes(
            m_TransparencyAttributes);
      } else {
        // we are mid explosion, modify the GeometryArray
        m_nFrameNumber++;

        m_GeometryArray.getCoordinates(0, m_CoordinateArray);

        m_TransparencyAttributes
            .setTransparency(((float) m_nFrameNumber)
                / ((float) m_nNumFrames));
        m_Shape3D.getAppearance().setTransparencyAttributes(
            m_TransparencyAttributes);

        for (int n = 0; n < m_CoordinateArray.length; n += 3) {
          m_Vector.x = m_CoordinateArray[n];
          m_Vector.y = m_CoordinateArray[n + 1];
          m_Vector.z = m_CoordinateArray[n + 2];

          m_Vector.normalize();

          m_CoordinateArray[n] += m_Vector.x * Math.random()
              + Math.random();
          m_CoordinateArray[n + 1] += m_Vector.y * Math.random()
              + Math.random();
          m_CoordinateArray[n + 2] += m_Vector.z * Math.random()
              + Math.random();
        }

        // assign the new coordinates
        m_GeometryArray.setCoordinates(0, m_CoordinateArray);
      }
    }

    if (m_nFrameNumber < m_nNumFrames) {
      // assign the next WakeUpCondition, so we are notified again
      wakeupOn(m_FrameWakeupCondition);
    } else {
      // we are at the end of the explosion
      // reapply the original appearance and GeometryArray
      // coordinates
      setEnable(false);
      m_Shape3D.setAppearance(m_Appearance);

      m_GeometryArray.setCoordinates(0, m_OriginalCoordinateArray);

      m_OriginalCoordinateArray = null;
      m_GeometryArray = null;
      m_CoordinateArray = null;
      m_TransparencyAttributes = null;

      // if we have a listener notify them that we are done
      if (m_Listener != null)
        wakeupOn(m_Listener.onExplosionFinished(this, m_Shape3D));
    }
  }

@Override
public void processStimulus(Iterator<WakeupCriterion> arg0) {
	// TODO Auto-generated method stub
	
}
}

interface ExplosionListener {
  abstract public WakeupCondition onExplosionFinished(
      ExplodeBehavior explodeBehavior, Shape3D shape3D);
}

//this class implements a simple behavior that
//calculates and prints the size of an object
//based on the vertices in its GeometryArray

class ObjectSizeBehavior extends Behavior {
  // the wake up condition for the behavior
  protected WakeupCondition m_WakeupCondition = null;

  // the GeometryArray for the Shape3D that we are querying
  protected GeometryArray m_GeometryArray = null;

  // cache some information on the model to save reallocation
  protected float[] m_CoordinateArray = null;

  protected BoundingBox m_BoundingBox = null;

  protected Point3d m_Point = null;;

  public ObjectSizeBehavior(GeometryArray geomArray) {
    // save the GeometryArray that we are modifying
    m_GeometryArray = geomArray;

    // set the capability bits that the behavior requires
    m_GeometryArray.setCapability(GeometryArray.ALLOW_COORDINATE_READ);
    m_GeometryArray.setCapability(GeometryArray.ALLOW_COUNT_READ);

    // allocate an array for the coordinates
    m_CoordinateArray = new float[3 * m_GeometryArray.getVertexCount()];

    // create the BoundingBox used to
    // calculate the size of the object
    m_BoundingBox = new BoundingBox();

    // create a temporary point
    m_Point = new Point3d();

    // create the WakeupCriterion for the behavior
    WakeupCriterion criterionArray[] = new WakeupCriterion[1];
    criterionArray[0] = new WakeupOnElapsedFrames(20);

    // save the WakeupCriterion for the behavior
    m_WakeupCondition = new WakeupOr(criterionArray);
  }

  public void initialize() {
    // apply the initial WakeupCriterion
    wakeupOn(m_WakeupCondition);
  }

  public void processStimulus(java.util.Enumeration criteria) {
    while (criteria.hasMoreElements()) {
      WakeupCriterion wakeUp = (WakeupCriterion) criteria.nextElement();

      // every N frames, recalculate the bounds
      // for the points in the GeometryArray
      if (wakeUp instanceof WakeupOnElapsedFrames) {
        // get all the coordinates
        m_GeometryArray.getCoordinates(0, m_CoordinateArray);

        // clear the old BoundingBox
        m_BoundingBox.setLower(0, 0, 0);
        m_BoundingBox.setUpper(0, 0, 0);

        // loop over every vertex and combine with the BoundingBox
        for (int n = 0; n < m_CoordinateArray.length; n += 3) {
          m_Point.x = m_CoordinateArray[n];
          m_Point.y = m_CoordinateArray[n + 1];
          m_Point.z = m_CoordinateArray[n + 2];

          m_BoundingBox.combine(m_Point);
        }

        System.out.println(m_BoundingBox.toString());
      }
    }

    // assign the next WakeUpCondition, so we are notified again
    wakeupOn(m_WakeupCondition);
  }

@Override
public void processStimulus(Iterator<WakeupCriterion> arg0) {
	// TODO Auto-generated method stub
	
}
}

//this class implements a more complex behavior.
//the behavior modifies the coordinates within a
//GeometryArray based on simulated forces applied to
//the model. Forces are modeled as springs from the origin
//to every node. Every node has a mass, and hence an
//acceleration. Pressing a key will increase the acceleration
//at each node, upsetting the force equilibrium at each vertex.
//The model will then start to oscillate in size as the springs
//have no "damping" effect.
//
//note: this is a very computationally expensive behavior!

class StretchBehavior extends Behavior {
  // the wake up condition for the behavior
  protected WakeupCondition m_WakeupCondition = null;

  // the GeometryArray for the Shape3D that we are modifying
  protected GeometryArray m_GeometryArray = null;

  // cache some information on the model to save reallocation
  protected float[] m_CoordinateArray = null;

  protected float[] m_LengthArray = null;

  protected float[] m_MassArray = null;

  protected float[] m_AccelerationArray = null;

  protected Vector3f m_Vector = null;

  // spring stiffness: Fspring = k.Le
  protected float m_kSpringConstant = 1.3f;

  protected float m_kAccelerationLossFactor = 0.985f;

  public StretchBehavior(GeometryArray geomArray) {
    // save the GeometryArray that we are modifying
    m_GeometryArray = geomArray;

    // set the capability bits that the behavior requires
    m_GeometryArray.setCapability(GeometryArray.ALLOW_COORDINATE_READ);
    m_GeometryArray.setCapability(GeometryArray.ALLOW_COORDINATE_WRITE);
    m_GeometryArray.setCapability(GeometryArray.ALLOW_COUNT_READ);

    // allocate an array for the model coordinates
    m_CoordinateArray = new float[3 * m_GeometryArray.getVertexCount()];

    // retrieve the models original coordinates - this defines
    // the relaxed length of the springs
    m_GeometryArray.getCoordinates(0, m_CoordinateArray);

    // allocate an array to store the relaxed length
    // of the springs from the origin to every vertex
    m_LengthArray = new float[m_GeometryArray.getVertexCount()];

    // allocate an array to store the mass of every vertex
    m_MassArray = new float[m_GeometryArray.getVertexCount()];

    // allocate an array to store the acceleration of every vertex
    m_AccelerationArray = new float[m_GeometryArray.getVertexCount()];

    // allocate a temporary vector
    m_Vector = new Vector3f();

    float x = 0;
    float y = 0;
    float z = 0;

    for (int n = 0; n < m_CoordinateArray.length; n += 3) {
      // calculate and store the relaxed spring length
      x = m_CoordinateArray[n];
      y = m_CoordinateArray[n + 1];
      z = m_CoordinateArray[n + 2];

      m_LengthArray[n / 3] = (x * x) + (y * y) + (z * z);

      // assign the mass for the vertex
      m_MassArray[n / 3] = (float) (50 + (5 * Math.random()));
    }

    // create the WakeupCriterion for the behavior
    WakeupCriterion criterionArray[] = new WakeupCriterion[2];
    criterionArray[0] = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
    criterionArray[1] = new WakeupOnElapsedFrames(1);

    // save the WakeupCriterion for the behavior
    m_WakeupCondition = new WakeupOr(criterionArray);
  }

  public void initialize() {
    // apply the initial WakeupCriterion
    wakeupOn(m_WakeupCondition);
  }

  public void processStimulus(java.util.Enumeration criteria) {
    // update the positions of the vertices - regardless of criteria
    float elongation = 0;
    float force_spring = 0;
    float force_mass = 0;
    float force_sum = 0;
    float timeFactor = 0.1f;
    float accel_sum = 0;

    // loop over every vertex and calculate its new position
    // based on the sum of forces due to acceleration and the
    // spring.
    for (int n = 0; n < m_CoordinateArray.length; n += 3) {
      m_Vector.x = m_CoordinateArray[n];
      m_Vector.y = m_CoordinateArray[n + 1];
      m_Vector.z = m_CoordinateArray[n + 2];

      // use squared lengths, as sqrt is costly
      elongation = m_LengthArray[n / 3] - m_Vector.lengthSquared();

      // Fspring = k.Le
      force_spring = m_kSpringConstant * elongation;
      force_mass = m_AccelerationArray[n / 3] * m_MassArray[n / 3];

      // calculate resultant force
      force_sum = force_mass + force_spring;

      // a = F/m
      m_AccelerationArray[n / 3] = (force_sum / m_MassArray[n / 3])
          * m_kAccelerationLossFactor;
      accel_sum += m_AccelerationArray[n / 3];

      m_Vector.normalize();

      // apply a portion of the acceleration as change in coordinate.
      // based on the normalized vector from the origin to the vertex
      m_CoordinateArray[n] += m_Vector.x * timeFactor
          * m_AccelerationArray[n / 3];
      m_CoordinateArray[n + 1] += m_Vector.y * timeFactor
          * m_AccelerationArray[n / 3];
      m_CoordinateArray[n + 2] += m_Vector.z * timeFactor
          * m_AccelerationArray[n / 3];
    }

    // assign the new coordinates
    m_GeometryArray.setCoordinates(0, m_CoordinateArray);

    while (criteria.hasMoreElements()) {
      WakeupCriterion wakeUp = (WakeupCriterion) criteria.nextElement();

      // if a key was pressed increase the acceleration at the
      // vertices a little to upset the equiblibrium
      if (wakeUp instanceof WakeupOnAWTEvent) {
        for (int n = 0; n < m_AccelerationArray.length; n++)
          m_AccelerationArray[n] += 0.3f;
      } else {
        // otherwise, print the average acceleration
        System.out.print("Average acceleration:\t" + accel_sum
            / m_AccelerationArray.length + "\n");
      }
    }

    // assign the next WakeUpCondition, so we are notified again
    wakeupOn(m_WakeupCondition);
  }

@Override
public void processStimulus(Iterator<WakeupCriterion> arg0) {
	// TODO Auto-generated method stub
	
}
}
/*******************************************************************************
 * Copyright (C) 2001 Daniel Selman
 * 
 * First distributed with the book "Java 3D Programming" by Daniel Selman and
 * published by Manning Publications. http://manning.com/selman
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, version 2.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * The license can be found on the WWW at: http://www.fsf.org/copyleft/gpl.html
 * 
 * Or by writing to: Free Software Foundation, Inc., 59 Temple Place - Suite
 * 330, Boston, MA 02111-1307, USA.
 * 
 * Authors can be contacted at: Daniel Selman: daniel@selman.org
 * 
 * If you make changes you think others would like, please contact one of the
 * authors or someone at the www.j3d.org web site.
 ******************************************************************************/

//*****************************************************************************
/**
 * Java3dApplet
 * 
 * Base class for defining a Java 3D applet. Contains some useful methods for
 * defining views and scenegraphs etc.
 * 
 * @author Daniel Selman
 * @version 1.0
 */
//*****************************************************************************

abstract class Java3dApplet extends Applet {
  public static int m_kWidth = 300;

  public static int m_kHeight = 300;

  protected String[] m_szCommandLineArray = null;

  protected VirtualUniverse m_Universe = null;

  protected BranchGroup m_SceneBranchGroup = null;

  protected Bounds m_ApplicationBounds = null;

  //  protected com.tornadolabs.j3dtree.Java3dTree m_Java3dTree = null;

  public Java3dApplet() {
  }

  public boolean isApplet() {
    try {
      System.getProperty("user.dir");
      System.out.println("Running as Application.");
      return false;
    } catch (Exception e) {
    }

    System.out.println("Running as Applet.");
    return true;
  }

  public URL getWorkingDirectory() throws java.net.MalformedURLException {
    URL url = null;

    try {
      File file = new File(System.getProperty("user.dir"));
      System.out.println("Running as Application:");
      System.out.println("   " + file.toURI().toURL());
      return file.toURI().toURL();
    } catch (Exception e) {
    }

    System.out.println("Running as Applet:");
    System.out.println("   " + getCodeBase());

    return getCodeBase();
  }

  public VirtualUniverse getVirtualUniverse() {
    return m_Universe;
  }

  //public com.tornadolabs.j3dtree.Java3dTree getJ3dTree() {
  //return m_Java3dTree;
  //  }

  public Locale getFirstLocale() {
    java.util.Enumeration e = (Enumeration) m_Universe.getAllLocales();

    if (e.hasMoreElements() != false)
      return (Locale) e.nextElement();

    return null;
  }

  protected Bounds getApplicationBounds() {
    if (m_ApplicationBounds == null)
      m_ApplicationBounds = createApplicationBounds();

    return m_ApplicationBounds;
  }

  protected Bounds createApplicationBounds() {
    m_ApplicationBounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
        100.0);
    return m_ApplicationBounds;
  }

  protected Background createBackground() {
    Background back = new Background(new Color3f(0.9f, 0.9f, 0.9f));
    back.setApplicationBounds(createApplicationBounds());
    return back;
  }

  public void initJava3d() {
    //  m_Java3dTree = new com.tornadolabs.j3dtree.Java3dTree();
    m_Universe = createVirtualUniverse();

    Locale locale = createLocale(m_Universe);

    BranchGroup sceneBranchGroup = createSceneBranchGroup();

    ViewPlatform vp = createViewPlatform();
    BranchGroup viewBranchGroup = createViewBranchGroup(
        getViewTransformGroupArray(), vp);

    createView(vp);

    Background background = createBackground();

    if (background != null)
      sceneBranchGroup.addChild(background);

    //    m_Java3dTree.recursiveApplyCapability(sceneBranchGroup);
    //  m_Java3dTree.recursiveApplyCapability(viewBranchGroup);

    locale.addBranchGraph(sceneBranchGroup);
    addViewBranchGroup(locale, viewBranchGroup);

    onDoneInit();
  }

  protected void onDoneInit() {
    //  m_Java3dTree.updateNodes(m_Universe);
  }

  protected double getScale() {
    return 1.0;
  }

  public TransformGroup[] getViewTransformGroupArray() {
    TransformGroup[] tgArray = new TransformGroup[1];
    tgArray[0] = new TransformGroup();

    // move the camera BACK a little...
    // note that we have to invert the matrix as
    // we are moving the viewer
    Transform3D t3d = new Transform3D();
    t3d.setScale(getScale());
    t3d.setTranslation(new Vector3d(0.0, 0.0, -20.0));
    t3d.invert();
    tgArray[0].setTransform(t3d);

    return tgArray;
  }

  protected void addViewBranchGroup(Locale locale, BranchGroup bg) {
    locale.addBranchGraph(bg);
  }

  protected Locale createLocale(VirtualUniverse u) {
    return new Locale(u);
  }

  protected BranchGroup createSceneBranchGroup() {
    m_SceneBranchGroup = new BranchGroup();
    return m_SceneBranchGroup;
  }

  protected View createView(ViewPlatform vp) {
    View view = new View();

    PhysicalBody pb = createPhysicalBody();
    PhysicalEnvironment pe = createPhysicalEnvironment();

    AudioDevice audioDevice = createAudioDevice(pe);

    if (audioDevice != null) {
      pe.setAudioDevice(audioDevice);
      audioDevice.initialize();
    }

    view.setPhysicalEnvironment(pe);
    view.setPhysicalBody(pb);

    if (vp != null)
      view.attachViewPlatform(vp);

    view.setBackClipDistance(getBackClipDistance());
    view.setFrontClipDistance(getFrontClipDistance());

    Canvas3D c3d = createCanvas3D();
    view.addCanvas3D(c3d);
    addCanvas3D(c3d);

    return view;
  }

  protected PhysicalBody createPhysicalBody() {
    return new PhysicalBody();
  }

  protected AudioDevice createAudioDevice(PhysicalEnvironment pe) {
    JavaSoundMixer javaSoundMixer = new JavaSoundMixer(pe);

    if (javaSoundMixer == null)
      System.out.println("create of audiodevice failed");

    return javaSoundMixer;
  }

  protected PhysicalEnvironment createPhysicalEnvironment() {
    return new PhysicalEnvironment();
  }

  protected float getViewPlatformActivationRadius() {
    return 100;
  }

  protected ViewPlatform createViewPlatform() {
    ViewPlatform vp = new ViewPlatform();
    vp.setViewAttachPolicy(View.RELATIVE_TO_FIELD_OF_VIEW);
    vp.setActivationRadius(getViewPlatformActivationRadius());

    return vp;
  }

  protected Canvas3D createCanvas3D() {
    GraphicsConfigTemplate3D gc3D = new GraphicsConfigTemplate3D();
    gc3D.setSceneAntialiasing(GraphicsConfigTemplate.PREFERRED);
    GraphicsDevice gd[] = GraphicsEnvironment.getLocalGraphicsEnvironment()
        .getScreenDevices();

    Canvas3D c3d = new Canvas3D(gd[0].getBestConfiguration(gc3D));
    c3d.setSize(getCanvas3dWidth(c3d), getCanvas3dHeight(c3d));

    return c3d;
  }

  protected int getCanvas3dWidth(Canvas3D c3d) {
    return m_kWidth;
  }

  protected int getCanvas3dHeight(Canvas3D c3d) {
    return m_kHeight;
  }

  protected double getBackClipDistance() {
    return 100.0;
  }

  protected double getFrontClipDistance() {
    return 1.0;
  }

  protected BranchGroup createViewBranchGroup(TransformGroup[] tgArray,
      ViewPlatform vp) {
    BranchGroup vpBranchGroup = new BranchGroup();

    if (tgArray != null && tgArray.length > 0) {
      Group parentGroup = vpBranchGroup;
      TransformGroup curTg = null;

      for (int n = 0; n < tgArray.length; n++) {
        curTg = tgArray[n];
        parentGroup.addChild(curTg);
        parentGroup = curTg;
      }

      tgArray[tgArray.length - 1].addChild(vp);
    } else
      vpBranchGroup.addChild(vp);

    return vpBranchGroup;
  }

  protected void addCanvas3D(Canvas3D c3d) {
    setLayout(new BorderLayout());
    add(c3d, BorderLayout.CENTER);
    doLayout();
  }

  protected VirtualUniverse createVirtualUniverse() {
    return new VirtualUniverse();
  }

  protected void saveCommandLineArguments(String[] szArgs) {
    m_szCommandLineArray = szArgs;
  }

  protected String[] getCommandLineArguments() {
    return m_szCommandLineArray;
  }
}