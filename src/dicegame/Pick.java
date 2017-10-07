/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dicegame;

import com.sun.j3d.utils.pickfast.PickCanvas;
import java.awt.AWTEvent;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import javax.media.j3d.Behavior;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Node;
import javax.media.j3d.PickInfo;
import javax.media.j3d.SceneGraphPath;
import javax.media.j3d.WakeupOnAWTEvent;

/**
 *
 * @author afro
 */
public class Pick extends Behavior {
    
    private WakeupOnAWTEvent condition;
    private PickCanvas pickCanvasVistas;
    private Canvas3D canvasVistas;
    private Escena esc;

    
    public Pick(Canvas3D canvasVistas, Canvas3D canvasPlanta) {
        
        this.canvasVistas = canvasVistas;
        condition = new WakeupOnAWTEvent (MouseEvent.MOUSE_CLICKED);
    }
    
    public void setBranchGroup(BranchGroup bg) {
        pickCanvasVistas = new PickCanvas(canvasVistas, bg);
        pickCanvasVistas.setTolerance(0.0f) ;
        pickCanvasVistas.setMode(PickInfo.PICK_GEOMETRY);
        pickCanvasVistas.setFlags(PickInfo.SCENEGRAPHPATH);
        
        
        setEnable(true);
    }

    @Override
    public void initialize() {
        setEnable(true);
        wakeupOn(condition);
    }
    public void LoadScene(Escena scene){
        esc = scene;
    }
    @Override
    public void processStimulus(Enumeration cond) {
        WakeupOnAWTEvent c = (WakeupOnAWTEvent) cond.nextElement();
        AWTEvent[] e = c.getAWTEvent();
        MouseEvent mouse = (MouseEvent) e[0];
        
        PickInfo pi = null;
        Node as = null;
        
        if(mouse.getSource() == canvasVistas) {
            
            pickCanvasVistas.setShapeLocation(mouse);
            pi = pickCanvasVistas.pickClosest();            
        } 
        
        if(pi != null) {
            System.out.println("Has pinchado");
            SceneGraphPath obj = pi.getSceneGraphPath();
            
            as = (Node)obj.getNode(obj.nodeCount() -1);
            if(obj.getNode(obj.nodeCount() -1).getPickable() == true){
                //Lo que quieras hacer
                if(as.getUserData() instanceof Dado){
                    Dado dad = (Dado)as.getUserData();
                    //dad.Rebote();
                    esc.selectDado(dad.getId());
                    System.out.println("ES UN DADO");
                }
                else if(as.getUserData() instanceof Boton){
                    System.out.println("ES UN BOTON");
                    
                    Boton bot = (Boton)as.getUserData();
                    if(bot.GetTipo() == 0){
                        esc.LanzarDados();
                        System.out.println("EL BOTON DE RELANZAR");
                    }
                    else if(bot.GetTipo() == 1){
                        System.out.println("EL BOTON DE PELEA");
                        esc.Lucha();
                    }
                }
                    
            }
        }
        
        wakeupOn(condition) ;
        
    }
    
}