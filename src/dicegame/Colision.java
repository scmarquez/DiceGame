/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dicegame;

import javax.media.j3d.Behavior;
import javax.media.j3d.WakeupOnCollisionEntry;
import javax.media.j3d.*;
import java.util.Enumeration;

/**
 *
 * @author sergio
 */
public class Colision extends Behavior{
    private Node n;
    private WakeupOnCollisionEntry condicion_entrda;
    private Dado dado;
    
    Colision(Dado d){
        dado = d;
        n = dado.GetGeometryNode();
        condicion_entrda = new WakeupOnCollisionEntry(n, WakeupOnCollisionEntry.USE_BOUNDS);
        
        this.setSchedulingBounds(dado.getBoundinBox());
        
        this.setBoundsAutoCompute(true);
    }
    @Override
    public void initialize(){
        wakeupOn(condicion_entrda);
    }
    @Override
    public void processStimulus(Enumeration criteria){
        dado.Rebote();        
    }
    
}
