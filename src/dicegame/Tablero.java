/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dicegame;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.image.TextureLoader;
import java.awt.Container;
import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.vecmath.Color3f;

/**
 *
 * @author sergio
 */
public class Tablero {
    float sizeX;
    float sizeY;
    Box geometria;
    Appearance apariencia;
    BranchGroup bg;
    
    public Tablero(float x,float y,Appearance ap){
        sizeX = x;
        sizeY = y;
        apariencia = ap;
        /*
        geometria = new ColorCube(s,Primitive.GENERATE_NORMALS | 
        Primitive.GENERATE_TEXTURE_COORDS |
        Primitive.ENABLE_APPEARANCE_MODIFY | Primitive.GENERATE_NORMALS, 64, 
        ap);
*/      int paratextura = Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS;
        TextureLoader loader=null;
        Texture texture=null;
        loader = new TextureLoader("Imagenes/tablero2.jpg","INTENSITY", new Container());
        
        texture = loader.getTexture();
        Appearance apa = new Appearance();
        apa.setTexture(texture);
        Material material = new Material(new Color3f(0.5f, 0.5f, 0.5f), new Color3f(0.5f, 0.5f, 0.5f), new Color3f(0.5f, 0.5f, 0.5f), new Color3f(0.5f, 0.5f, 0.5f), 100);
        material.setLightingEnable(true);
        //apa.setMaterial(new Material());
        TextureAttributes ta = new TextureAttributes();
        ta.setTextureMode(TextureAttributes.COMBINE);
        apa.setTextureAttributes(ta);
        
        
        geometria = new Box(sizeX, sizeY, (float)0.001,paratextura, apa);
        geometria.setAppearance(apa);
        geometria.setPickable(false);
        
        bg = new BranchGroup();
        bg.addChild(geometria);
        
    }
    public Node GetNode(){
        return bg;
    }
}
