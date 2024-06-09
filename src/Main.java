//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.IOException;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import simbad.gui.Simbad;
import simbad.sim.Box;
import simbad.sim.EnvironmentDescription;

public class Main {
    private static State initial;

    public Main() {
    }

    public static void main(String[] args) throws IOException {
        EnvironmentDescription enviroment = new EnvironmentDescription();

        spheres(enviroment);

        enviroment.light1IsOn = true;
        enviroment.light1Position = new Vector3d(-1.0, 2.0, -8.0);
        enviroment.light2IsOn = false;
        enviroment.ambientLightColor = new Color3f(250.0F, 250.0F, 250.0F);
        MyRobot rob = new MyRobot(new Vector3d(0, 0.0, 8.0), "robot1");
        enviroment.add(rob);
        new Simbad(enviroment, false);
    }

    static void spheres(EnvironmentDescription environment) {
        environment.add(new Box(new Vector3d(0.0, 0.0, 3.0), new Vector3f(5.0F, 1.0F, 5.0F), environment));
        environment.add(new Box(new Vector3d(2.75, 0.0, 3.0), new Vector3f(0.5F, 1.0F, 4.0F), environment));
        environment.add(new Box(new Vector3d(3.25, 0.0, 3.0), new Vector3f(0.5F, 1.0F, 3.0F), environment));
        environment.add(new Box(new Vector3d(-2.75, 0.0, 3.0), new Vector3f(0.5F, 1.0F, 4.0F), environment));
        environment.add(new Box(new Vector3d(-3.25, 0.0, 3.0), new Vector3f(0.5F, 1.0F, 3.0F), environment));
        // environment.add(new Box(new Vector3d(0.0, 0.0, -4.0), new Vector3f(2.0F, 1.0F, 2.0F), environment));

        environment.add(new Box(new Vector3d(0.0, 0.0, -2.5), new Vector3f(4.0F, 1.0F, 1.0F), environment));
    }

    static void box(EnvironmentDescription environment) {
        environment.add(new Box(new Vector3d(0.0, 0.0, 0.0), new Vector3f(5.0F, 1.0F, 5.0F), environment));
    }

    static void bottle(EnvironmentDescription environment) {
        environment.add(new Box(new Vector3d(0.0, 0.0, 3.0), new Vector3f(5.0F, 1.0F, 5.0F), environment));
        environment.add(new Box(new Vector3d(0.0, 0.0, -0.5), new Vector3f(2.0F, 1.0F, 2.0F), environment));
    }

    static void spiral(EnvironmentDescription environment) {
        environment.add(new Box(new Vector3d(0.0, 0.0, 7.0), new Vector3f(14.0F, 1.0F, 1.0F), environment));
        environment.add(new Box(new Vector3d(-2.0, 0.0, 3.0), new Vector3f(12.0F, 1.0F, 1.0F), environment));
        environment.add(new Box(new Vector3d(0.0, 0.0, -9.0), new Vector3f(16.0F, 1.0F, 1.0F), environment));
        environment.add(new Box(new Vector3d(-7.5, 0.0, -2.5), new Vector3f(1.0F, 1.0F, 12.0F), environment));
        environment.add(new Box(new Vector3d(7.5, 0.0, -0.5), new Vector3f(1.0F, 1.0F, 16.0F), environment));
        environment.add(new Box(new Vector3d(3.5, 0.0, -1.0), new Vector3f(1.0F, 1.0F, 7.0F), environment));
        environment.add(new Box(new Vector3d(0.0, 0.0, -5.0), new Vector3f(7.0F, 1.0F, 1.0F), environment));
        environment.add(new Box(new Vector3d(-3.5, 0.0, -2.5), new Vector3f(1.0F, 1.0F, 4.0F), environment));
        environment.add(new Box(new Vector3d(-1.5, 0.0, -1.0), new Vector3f(3.0F, 1.0F, 1.0F), environment));
    }
}
