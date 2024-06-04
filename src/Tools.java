//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import javax.media.j3d.Transform3D;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import simbad.sim.Agent;
import simbad.sim.RangeSensorBelt;

public class Tools {
    public Tools() {
    }

    public static Point3d getSensedPoint(Agent rob, RangeSensorBelt sonars, int sonar) {
        double v;
        if (sonars.hasHit(sonar)) {
            v = (double)rob.getRadius() + sonars.getMeasurement(sonar);
        } else {
            v = (double)(rob.getRadius() + sonars.getMaxRange());
        }

        double x = v * Math.cos(sonars.getSensorAngle(sonar));
        double z = v * Math.sin(sonars.getSensorAngle(sonar));
        return new Point3d(x, 0.0, z);
    }

    public static Point3d getGlobalSensedPoint(Agent rob, RangeSensorBelt sonars, int sonar) {
        Point3d r = new Point3d();
        rob.getCoords(r);
        double v;
        if (sonars.hasHit(sonar)) {
            v = (double)rob.getRadius() + sonars.getMeasurement(sonar);
        } else {
            v = (double)(rob.getRadius() + sonars.getMaxRange());
        }

        double th = getAngle(rob);
        double a = th + sonars.getSensorAngle(sonar);
        double x = v * Math.cos(a);
        double z = v * Math.sin(a);
        return new Point3d(r.x + x, 0.0, r.z - z);
    }

    public static Point3d getGlobalCoords(Agent rob, Point3d p) {
        Point3d a = new Point3d();
        Point3d r = new Point3d();
        double th = getAngle(rob);
        rob.getCoords(r);
        double x = p.x;
        double z = p.z;
        a.setX(-x * Math.cos(th) - z * Math.sin(th));
        a.setZ(-z * Math.cos(th) + x * Math.sin(th));
        a.setY(p.y);
        a.x = r.x - a.x;
        a.z = r.z - a.z;
        return a;
    }

    public static Point3d getLocalCoords(Agent rob, Point3d p) {
        Point3d a = new Point3d();
        Point3d r = new Point3d();
        double th = getAngle(rob);
        rob.getCoords(r);
        double x = p.getX() - r.x;
        double z = -p.getZ() + r.z;
        a.setX(x * Math.cos(th) + z * Math.sin(th));
        a.setZ(z * Math.cos(th) - x * Math.sin(th));
        a.setY(p.y);
        return a;
    }

    public static double getAngle(Agent rob) {
        double angle = 0.0;
        Transform3D m_Transform3D = new Transform3D();
        rob.getRotationTransform(m_Transform3D);
        Matrix3d m1 = new Matrix3d();
        m_Transform3D.get(m1);
        double msin = m1.getElement(2, 0);
        double mcos = m1.getElement(0, 0);
        if (msin < 0.0) {
            angle = Math.acos(mcos);
        } else if (mcos < 0.0) {
            angle = 6.283185307179586 - Math.acos(mcos);
        } else {
            angle = -Math.asin(msin);
        }

        while(angle < 0.0) {
            angle += 6.283185307179586;
        }

        return angle;
    }

    public static double min(double a, double b) {
        return a < b ? a : b;
    }

    public static double max(double a, double b) {
        return a > b ? a : b;
    }

    public static double wrapToPi(double a) {
        if (a > Math.PI) {
            return a - 6.283185307179586;
        } else {
            return a <= -3.141592653589793 ? a + 6.283185307179586 : a;
        }
    }

    public static double getGlobalAngleToGoal(Agent rob, Point3d goal) {
        Point3d r = new Point3d();
        rob.getCoords(r);
        return Math.atan2(goal.z - r.z, goal.x - r.x);
    }
}
