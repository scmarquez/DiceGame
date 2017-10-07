/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dicegame;

import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.*;
import javax.vecmath.Vector3f;
import com.sun.j3d.utils.image.TextureLoader;
import javax.vecmath.Color3f;
/**
 *
 * @author sergio
 */
public class Boton {
    Box geometry;
    
    BranchGroup bg;
    TransformGroup tg;
    Transform3D t3d;
    Appearance apariencia;
    float px_inicial;
    float py_inicial;
    float pz_inicial;
    int tipo;
    
    public Boton(float x, float y, float z, Appearance apariencia,String text, int type){
        px_inicial = x;
        py_inicial = y;
        pz_inicial = z;
        
        this.apariencia = apariencia;
        geometry = new Box(0.1f, 0.05f, 0.1f,Box.GENERATE_TEXTURE_COORDS ,apariencia);
        geometry.setUserData(this);
        setText(text);
        
        t3d = new Transform3D();
        t3d.setTranslation(new Vector3f(px_inicial,py_inicial,pz_inicial));
        
        tg = new TransformGroup(t3d);
        tg.addChild(geometry);
        
        bg = new BranchGroup();
        bg.addChild(tg);
        
        tipo = type;
        
    }
    public void Click(){
        //Lanzar los dados de nuevo.
        System.out.println("Lanzando dados de nuevo...");
    }
    public Node GetNode(){
        return bg;
    }
    public void setText(String text){
        /////////////////////////////////
        //Colores de los materiales
        Color3f ambiental = new Color3f(1, 1, 1);
        Color3f emisivo = new Color3f(1, 1, 1);
        Color3f difuso = new Color3f(1, 1, 1);
        Color3f especular = new Color3f(1, 1, 1);
        ////////////////////////////////
        TextureLoader loader;
        Texture texture;
        
        Appearance ap = new Appearance();
        loader = new TextureLoader(text, null);
        texture = loader.getTexture();
        ap.setTexture(texture);
        ap.setMaterial(new Material(ambiental, emisivo, difuso, especular, 17));
        
        geometry.setAppearance(Box.FRONT, ap);  
    }
    public int GetTipo(){
        return tipo;
    }
}
