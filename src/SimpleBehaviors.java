//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import simbad.sim.Agent;

public class SimpleBehaviors {
    static double K1 = 5.0;
    static double K2 = 0.8;
    static double K3 = 1.0;
    static double MT = 2.0;
    static double SAFETY = 0.8;
    static double lightThreshold = 1.0E-5;

    public SimpleBehaviors() {
    }

    public static void stop(Agent rob) {
        rob.setRotationalVelocity(0.0);
        rob.setTranslationalVelocity(0.0);
    }

    public static void follow(MyRobot rob, boolean CLOCKWISE) {
        int min = 0;

        for(int i = 1; i < rob.sonars.getNumSensors(); ++i) {
            if (rob.sonars.getMeasurement(i) < rob.sonars.getMeasurement(min)) {
                min = i;
            }
        }

        Point3d p = Tools.getSensedPoint(rob, rob.sonars, min);
        double d = p.distance(new Point3d(0.0, 0.0, 0.0));
        Vector3d v = CLOCKWISE ? new Vector3d(-p.z, 0.0, p.x) : new Vector3d(p.z, 0.0, -p.x);
        double phLin = Math.atan2(v.z, v.x);
        double phRot = Math.atan(K3 * (d - SAFETY));
        if (CLOCKWISE) {
            phRot = -phRot;
        }

        double phRef = Tools.wrapToPi(phLin + phRot);
        rob.setRotationalVelocity(K1 * phRef);
        rob.setTranslationalVelocity(K2 * Math.cos(phRef));
    }

    public static void orientation(MyRobot robot) {
        robot.setRotationalVelocity(1.0);
        robot.setTranslationalVelocity(0.0);
    }

    public static void forward(MyRobot robot) {
        robot.setRotationalVelocity(0.0);
        robot.setTranslationalVelocity(1.0);
    }
}
