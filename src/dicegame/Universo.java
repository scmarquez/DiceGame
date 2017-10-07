/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dicegame;
import GUI.Visualization;
import com.sun.j3d.utils.universe.SimpleUniverse;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.Viewer;
import com.sun.j3d.utils.universe.ViewingPlatform;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Node;
import javax.media.j3d.View;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 *
 * @author sergio
 */
public class Universo {
    private BranchGroup root;
    private Canvas3D canvas;
    private Visualization vis;
    private Viewer viewer;
    private View view;
    private SimpleUniverse simple_universe;
    //private OrbitBehavior orbit;
    Pick pick;
    public Universo(){
        root = new BranchGroup();
    }
    public void AddNode(Node n){
        root.addChild(n);
    }
    public void CreateUniverse(){
        //Se inicializa en Canvas y la visualización
        canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        vis = new Visualization(canvas);
        //Crea el viewingplatform
        ViewingPlatform viewing_platform = new ViewingPlatform();
        //Se crea la trnasformación de vista, dónde está y a dónde mira
        TransformGroup viewTransformGroup = viewing_platform.getViewPlatformTransform();
        Transform3D viewTransform3D = new Transform3D();
        viewTransform3D.lookAt(new Point3d(20,20,20), new Point3d (0,0,0), new Vector3d (0,1,0));
        viewTransform3D.invert();
        viewTransformGroup.setTransform(viewTransform3D);
         
        // El comportamiento, para mover la camara con el raton
        //orbit = new OrbitBehavior(canvas, OrbitBehavior.REVERSE_ALL);
        //orbit.setSchedulingBounds(new BoundingSphere(new Point3d (0.0f, 0.0f, 0.0f), 100.0f));
        //orbit.setZoomFactor (2.0f);
        //viewing_platform.setViewPlatformBehavior(orbit);
        
        // Se establece el angulo de vision a 45 grados y el plano de recorte trasero
        viewer = new Viewer (canvas);
        view = viewer.getView();
        view.setFieldOfView(Math.toRadians(45));
        view.setBackClipDistance(50.0);
        
        pick = new Pick(canvas, null);
        BoundingSphere bs = new BoundingSphere(new Point3d(0, 0, 0), 1000);
        pick.setSchedulingBounds(bs);

        pick.setBranchGroup(root);
        root.addChild(pick);
        
        simple_universe = new SimpleUniverse(viewing_platform,viewer);
        simple_universe.getViewingPlatform().setNominalViewingTransform();
        simple_universe.addBranchGraph(root);
        vis.setVisible(true);
    }
    public void SetScene(Escena scene){
        pick.LoadScene(scene);
    }
}
