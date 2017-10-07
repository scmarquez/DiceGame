/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dicegame;

import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Alpha;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.*;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import static java.lang.Math.pow;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Point3f;
import com.sun.j3d.utils.image.TextureLoader;
import javax.vecmath.Color3f;

/**
 *
 * @author sergio
 */
public class Dado {
    //Propiedades del dado
    float size;
    Box geometria;
    Appearance apariencia;
    float px_inicial;
    float py_inicial;
    float pz_inicial;
    //Nodos de la rotación inicial
    BranchGroup bg_rotation;
    TransformGroup tg_rotation;
    Transform3D t3d_rotation;
    
    BranchGroup bg_rotationY;
    TransformGroup tg_rotationY;
    Transform3D t3d_rotationY;
    
    //Nodos de la traslación inicial
    BranchGroup bg_translation;
    TransformGroup tg_translation;
    Transform3D t3d_translation;
    
    //Nodos del interpolador
    BranchGroup bgInterpolator;
    TransformGroup tgInterpolator;
    Transform3D t3dInterpolator;
    
    //Detector de colisiones
    Colision colisionador;
    
    //Variables de anmación
    Alpha alfa;
    Point3f[] posiciones;
    RotPosPathInterpolator interpolador;
    float[] alphas;
    Quat4f[] orientaciones;
    float velocidad_x;
    float velocidad_y; 
    float velocidad_z;
    float max_time;
    //Nodos generales del árbol
    Transform3D t3d_general;
    TransformGroup tg_general;
    BranchGroup bg_general;
    //Luz del dado
    //Índice del dado en el motor de juego
    int cara;
    int option;
    int identificador;
    boolean focused;
    boolean able_to_fight;

    public Dado(float s, int opcion, float x, float y, float z,float vx, float vy,float vz){

        able_to_fight = true;
        max_time = 1.2f;
        velocidad_x = vx;
        velocidad_y = vy;
        velocidad_z = vz;
        posiciones = null;
        size = s;
        px_inicial = x;
        py_inicial = y;
        pz_inicial = z;
        
        option = opcion;
        focused = false;
        
        geometria = new Box(s, s, s, Box.GENERATE_TEXTURE_COORDS+Box.GENERATE_NORMALS+Box.ENABLE_APPEARANCE_MODIFY,new Appearance());
        geometria.setCapability(Box.ALLOW_BOUNDS_WRITE);
        geometria.setCollisionBounds(new BoundingBox(new Point3d((float)-s/2,(float)-s/2,(float)-s/2), new Point3d((float)s/2,(float)s/2,(float)s/2)));
        geometria.setUserData(this);
        geometria.setPickable(true);
        
        if(opcion == 1){
            setBadDice();
        }
        else{
            setGoodDice();
        }
        
        t3d_rotationY = new Transform3D();
        t3d_rotation = new Transform3D();
        cara = (int) (Math.random() * 5);
        setInitFace();
        //Nodos de la rotación
        //Rotación en el eje Y
        tg_rotationY = new TransformGroup();
        tg_rotationY.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        
        
        
       
        tg_rotationY.setTransform(t3d_rotationY);
        tg_rotationY.addChild(geometria);
        bg_rotationY = new BranchGroup();
        bg_rotationY.addChild(tg_rotationY);
        //ROtación en cualquier eje
        
        tg_rotation = new TransformGroup();
        tg_rotation.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        tg_rotation.setTransform(t3d_rotation);
        tg_rotation.addChild(bg_rotationY);
        bg_rotation = new BranchGroup();
        bg_rotation.addChild(tg_rotation);
        
        
        //Nodos de la traslación
        bg_translation = new BranchGroup();
        t3d_translation = new Transform3D();
        t3d_translation.setTranslation(new Vector3f(0f,0f,0.051f));
        tg_translation = new TransformGroup(t3d_translation);
        tg_translation.addChild(bg_rotation);
        //tg_translation.addChild(geometria);
        bg_translation.addChild(tg_translation);
        
        t3dInterpolator = new Transform3D();
        bgInterpolator =new BranchGroup();
        tgInterpolator =new TransformGroup(t3dInterpolator);
        tgInterpolator.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tgInterpolator.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        bgInterpolator.addChild(tgInterpolator);
        tgInterpolator.addChild(bg_translation);
        
        
        
        //Enganche con los nodos generales
        
        t3d_general = new Transform3D();
        t3d_general.setTranslation(new Vector3f(px_inicial,py_inicial,pz_inicial));
        tg_general = new TransformGroup();
        tg_general.setTransform(t3d_general);
        tg_general.addChild(bgInterpolator);
        bg_general = new BranchGroup();
        bg_general.addChild(tg_general);
        
    }
    
    public void setFace(){
        t3d_rotationY.setIdentity();
        t3d_rotation.setIdentity();
        
        if(cara == 0){ //goblin espada
            t3d_rotationY.rotY(Math.PI);
            t3d_rotation.rotZ(Math.PI);
        }
        else if(cara == 1){ //lobo mago
            t3d_rotationY.rotZ(Math.PI);
            t3d_rotation.rotX(0);
        }
        else if(cara == 2){ //Dragon heroe
            t3d_rotationY.rotY(0);
            t3d_rotation.rotX(-Math.PI/2);
        }
        else if(cara == 3){ //mazo calavera
            t3d_rotationY.rotX(Math.PI);
            t3d_rotation.rotY(Math.PI/2);
        }
        else if(cara == 4){ //ladron cofre
            t3d_rotationY.rotZ(Math.PI);
            t3d_rotation.rotY(Math.PI/2);
        }
        else if(cara == 5){ //pergamino pocion
            t3d_rotationY.rotY(0);
            t3d_rotation.rotX(Math.PI/2);
            System.out.println("Sale el pergamino");
        }
    }
    
    public void setInitFace(){
        t3d_rotationY.setIdentity();
        t3d_rotation.setIdentity();
        
        if(cara == 0){ //goblin espada
            t3d_rotationY.rotY(Math.PI);
            t3d_rotation.rotZ(Math.PI);
        }
        else if(cara == 1){ //lobo mago
            t3d_rotationY.rotY(0);
            t3d_rotation.rotZ(Math.PI);
        }
        else if(cara == 2){ //Dragon heroe
            t3d_rotationY.rotZ(0);
            t3d_rotation.rotX(-Math.PI/2);
        }
        else if(cara == 3){ //mazo calavera
            t3d_rotationY.rotZ(Math.PI);
            t3d_rotation.rotY(-Math.PI/2);
        }
        else if(cara == 4){ //ladron cofre
            t3d_rotationY.rotZ(Math.PI);
            t3d_rotation.rotY(Math.PI/2);
        }
        else if(cara == 5){ //pergamino pocion
            t3d_rotationY.rotY(0);
            t3d_rotation.rotX(Math.PI/2);
            System.out.println("Sale el pergamino");
        }
    }
    
    public void setId(int id){
        identificador = id;
    }
    
    public int getId(){
        return identificador;
    }
    
    public void setBadDice(){
        /////////////////////////////////
        
        TextureAttributes ta = new TextureAttributes();
        ta.setTextureMode(TextureAttributes.COMBINE);
        
        //Colores de los materiales
        Color3f ambiental = new Color3f(1, 1, 1);
        Color3f emisivo = new Color3f(0, 0, 0);
        Color3f difuso = new Color3f(1, 1, 1);
        Color3f especular = new Color3f(1, 1, 1);
        
        Material material = new Material(ambiental, emisivo, difuso, especular, 120);
        material.setLightingEnable(true);

        ////////////////////////////////
        TextureLoader loader;
        Texture texture;
        
        Appearance ap = new Appearance();
        ap.setTextureAttributes(ta);
        loader = new TextureLoader("Imagenes/dragon.jpg", null);
        texture = loader.getTexture();
        ap.setTexture(texture);
        ap.setMaterial(material);
        
        geometria.setAppearance(Box.TOP, ap);
        
        Appearance ap2 = new Appearance();
        ap2.setTextureAttributes(ta);
        loader = new TextureLoader("Imagenes/goblin.jpg", null);
        texture = loader.getTexture();
        ap2.setTexture(texture);
        ap2.setMaterial(material);
        geometria.setAppearance(Box.FRONT, ap2);
        
        Appearance ap3 = new Appearance();
        ap3.setTextureAttributes(ta);
        loader = new TextureLoader("Imagenes/lobo.jpg", null);
        texture = loader.getTexture();
        ap3.setTexture(texture);
        ap3.setMaterial(material);
        geometria.setAppearance(Box.BACK, ap3);
        
        Appearance ap4 = new Appearance();
        ap4.setTextureAttributes(ta);
        loader = new TextureLoader("Imagenes/cofre.jpg", null);
        texture = loader.getTexture();
        ap4.setTexture(texture);
        ap4.setMaterial(material);
        geometria.setAppearance(Box.LEFT, ap4);
        
        Appearance ap5 = new Appearance();
        ap5.setTextureAttributes(ta);
        loader = new TextureLoader("Imagenes/calavera.jpg", null);
        texture = loader.getTexture();
        ap5.setTexture(texture);
        ap5.setMaterial(material);
        geometria.setAppearance(Box.RIGHT, ap5);
        
        Appearance ap6 = new Appearance();
        ap6.setTextureAttributes(ta);
        loader = new TextureLoader("Imagenes/pocion.jpg", null);
        texture = loader.getTexture();
        ap6.setTexture(texture);
        ap6.setMaterial(material);
        geometria.setAppearance(Box.BOTTOM, ap6);
    }
    
    public void setGoodDice(){
        TextureAttributes ta = new TextureAttributes();
        ta.setTextureMode(TextureAttributes.COMBINE);
        
        
        
        //Colores de los materiales
        Color3f ambiental = new Color3f(0.9f,0.9f, 0.9f);
        Color3f emisivo = new Color3f(0, 0, 0);
        Color3f difuso = new Color3f(1, 1, 1);
        Color3f especular = new Color3f(1, 1, 1);
        
        Material material = new Material(ambiental, emisivo, difuso, especular, 120);
        material.setLightingEnable(true);
        
        TextureLoader loader;
        Texture texture;
        
        Appearance ap = new Appearance();
        loader = new TextureLoader("Imagenes/heroe.jpg", null);
        texture = loader.getTexture();
        ap.setTexture(texture);
        ap.setMaterial(material);
        ap.setTextureAttributes(ta);
        geometria.setAppearance(Box.TOP, ap);
        
        Appearance ap2 = new Appearance();
        loader = new TextureLoader("Imagenes/guerrero.jpg", null);
        texture = loader.getTexture();
        ap2.setTextureAttributes(ta);
        ap2.setTexture(texture);
        ap2.setMaterial(material);
        geometria.setAppearance(Box.FRONT, ap2);
        
        Appearance ap3 = new Appearance();
        loader = new TextureLoader("Imagenes/mago.jpg", null);
        texture = loader.getTexture();
        ap3.setTextureAttributes(ta);
        ap3.setTexture(texture);
        ap3.setMaterial(material);
        geometria.setAppearance(Box.BACK, ap3);
        
        Appearance ap4 = new Appearance();
        loader = new TextureLoader("Imagenes/ladron.jpg", null);
        texture = loader.getTexture();
        ap4.setTextureAttributes(ta);
        ap4.setTexture(texture);
        ap4.setMaterial(material);
        geometria.setAppearance(Box.LEFT, ap4);
        
        Appearance ap5 = new Appearance();
        loader = new TextureLoader("Imagenes/clerigo.jpg", null);
        texture = loader.getTexture();
        ap5.setTextureAttributes(ta);
        ap5.setTexture(texture);
        ap5.setMaterial(material);
        geometria.setAppearance(Box.RIGHT, ap5);
        
        Appearance ap6 = new Appearance();
        loader = new TextureLoader("Imagenes/pergamino.jpg", null);
        texture = loader.getTexture();
        ap6.setTextureAttributes(ta);
        ap6.setTexture(texture);
        ap6.setMaterial(material);
        geometria.setAppearance(Box.BOTTOM, ap6);
    }
    
    public Alpha GetAlpha(){
        return alfa;
    }
    
    public float GetVX(){
        return velocidad_x;
    }
    
    public float GetVy(){
        return velocidad_y;
    }
    
    public float GetVz(){
        return velocidad_z;
    }
    
    public Node GetNode(){
        return bg_general;   
    }
    
    public Node GetGeometryNode(){
        return geometria;
    }
    
    public Point3f[] CalculaTrayectoria(int max_move,float x,float y,float z){
        //Se tiene que el sistema solo presenta la aceleración gravitatoria
        
        //Vector de posiciones de la trayectoria
        Point3f[] vector_posiciones = new Point3f[max_move];
        //Vector de tiempos asociado a cada posición.
        float[] tiempos = new float[max_move]; //en segundos
        /*
        Para calcular cada punto de la trayectoria es necesario saber en que tiempo t
        se produce. Primeramente se rellena el vector de tiempos, que indica el momento
        concreto en el que el objeto alcanza la posición i-ésima
        */
        for(int i = 0; i < max_move; i++){
            tiempos[i] = max_time/(float)max_move * i;
        }
        //Una vez creado el vector de tiempos se crea el de posiciones en función de la velocidad de cada eje
        for(int i = 0; i < max_move; i++){
            /*Como en este caso la perspectiva es distinta, se usa el eje Z del sistema do corrodenadas como
            eje Y de un sistema real. Por eso la aceleración propia de la gravedad se usa en el eje Z
            Eje Z: Eje perpendicular al plano de la pantalla(representa al eje Y de un sistema real)
            Eje X: Eje que va de izquierda a derecha de la pantalla(representa al eje X de un sistema real)
            Eje Y: Eje que va de la parte superior a la inferior de la pantalla(Representa al eje Z de un sistema real)
            */
            float xi,yi,zi;
            //Posición de X(movimiento rectilineo uniforme)
            xi = x + velocidad_x*tiempos[i];
            //Posición de Y(movimiento rectilineo uniforme)
            yi = z + velocidad_z*tiempos[i];
            //Posición de Z(movimiento rectilineo uniformemente acelerado)
            zi = y + velocidad_y*tiempos[i] + (float)1/2*(float)-9.8*(float)pow(tiempos[i],2);
            
            //Insertar los valores en el vector de puntos
            vector_posiciones[i] = new Point3f(xi, yi, zi);

        }
        return vector_posiciones;
    }
    
    public void Interpola(Point3f[] pos,int particiones,float delay){
        colisionador = new Colision(this);
        
        tg_rotationY.addChild(colisionador);  
        
        colisionador.setEnable(true);
        ///////////////////////////////////////////////////////////////////////
        alfa = new Alpha(1,Alpha.INCREASING_ENABLE,(long)delay,0,(int)(max_time*1000),0, 0, 0, 0, 0);
        
        //Vector de alfas
        alphas = new float[particiones];
        for(int i =0 ; i < particiones; i++){
            alphas[i] = (float)1/(float)(particiones-1)*i;
        }
        posiciones = pos;

        orientaciones = new Quat4f[particiones];
        
        for(int i  =0; i < particiones; i++){
            orientaciones[i] = new Quat4f();
            if(i%2 == 0){
                if(i%3 == 0){
                    orientaciones[i].set(1,1,0, -(float)Math.PI);
                }
                else{
                    orientaciones[i].set(1,0,0, -(float)Math.PI);
                }
                
            }
            else{
                if(i%3 == 0){
                    orientaciones[i].set(1,1,0, -(float)Math.PI);
                }
                else{
                    orientaciones[i].set(new AxisAngle4f(1f,0f,0f,-(float)(Math.PI)));
                }
            }   
        }
        
        interpolador = new RotPosPathInterpolator(alfa, tgInterpolator, t3dInterpolator, alphas, orientaciones, posiciones);
        interpolador.setSchedulingBounds(new BoundingSphere(new Point3d(0, 0, 0), 10000));
        interpolador.setEnable(true);
        tgInterpolator.addChild(interpolador);
  
    }
    
    public void CorrigePuntos(float px, float py, float pz, float vx, float vy, float vz,
                              int tamanio, float tiempo_total,Point3f[] puntos){
        int colisiones = 0;
        float tiempo = 0;
        float intervalo_temporal = (float)tiempo_total/(float)tamanio;
        float p0x = px;
        float p0z = py;
        float p0y = pz;
        System.out.println("PUntos de colision:"+" x0 = "+px+" py = "+py+"pz= "+pz);
        //Los ejes Y y Z están cambiados
        for(int i = 0; i < tamanio; i++){
            puntos[i].x = p0x + vx*tiempo;
            puntos[i].y = p0y + vz*tiempo;
            puntos[i].z = p0z + vy*tiempo+(float)1/(float)2*(-9.8f)*(float)pow(tiempo,2);
            if(puntos[i].z < 0){
                puntos[i].z = 0;
                vy*=0.85;
                vz*=0.85;
                vy*=0.85;
                
                p0x = puntos[i].x;
                p0y = puntos[i].y;
                p0z = puntos[i].z;
                
                tiempo = 0;
            }
            System.out.println("px"+i+"= "+puntos[i].x);
            System.out.println("py"+i+"= "+puntos[i].y);
            System.out.println("pz"+i+"= "+puntos[i].z);
            tiempo+=intervalo_temporal;
        }
        //Se asegura que acaba el dado en el suelo
        puntos[tamanio-1].z = -0.04f;
    }
    
    public void Rebote(){
       
        alfa.pause();
        alfa.setTriggerTime(0);
        alfa.setPhaseDelayDuration(0);
        //Se obtiene el punto dónde el dado toca el tablero.
        Transform3D trans;
        trans = new Transform3D();
        tgInterpolator.getTransform(trans);
        Vector3d colision = new Vector3d();
        trans.get(colision);
        
        //Se recalculan las posiciones del vector
        CorrigePuntos((float)colision.x, (float)colision.y, (float)colision.z, (float)velocidad_x, 3.5f, (float)velocidad_z,30, (float)max_time, posiciones);

        interpolador.setPathArrays(alphas, orientaciones, posiciones);
        alfa.setStartTime(System.currentTimeMillis());
        alfa.resume();       
    }
    
    public void Relanzar(long delay){
        clean();
        able_to_fight = true;
        cara = (int) ((Math.random() * 5));
        setFace();
        tg_rotationY.setTransform(t3d_rotationY);
        tg_rotation.setTransform(t3d_rotation);
        alfa.setStartTime(System.currentTimeMillis()+delay);
    }
    
    public Bounds getBoundinBox(){
        return geometria.getCollisionBounds();
    }
    
    public void setBadDiceFocus(){
        /////////////////////////////////
        
        TextureAttributes ta = new TextureAttributes();
        ta.setTextureMode(TextureAttributes.COMBINE);
        
        
        //Colores de los materiales
        Color3f ambiental = new Color3f(1, 1, 1);
        Color3f emisivo = new Color3f(1, 1, 1);
        Color3f difuso = new Color3f(1, 1, 1);
        Color3f especular = new Color3f(1, 1, 1);
        ////////////////////////////////
        TextureLoader loader;
        Texture texture;
        
        Appearance ap = new Appearance();
        ap.setTextureAttributes(ta);
        loader = new TextureLoader("Imagenes/dragonfocus.jpg", null);
        texture = loader.getTexture();
        ap.setTexture(texture);
        ap.setMaterial(new Material(ambiental, emisivo, difuso, especular, 17));
        
        geometria.setAppearance(Box.TOP, ap);
        
        Appearance ap2 = new Appearance();
        ap2.setTextureAttributes(ta);
        loader = new TextureLoader("Imagenes/goblinfocus.jpg", null);
        texture = loader.getTexture();
        ap2.setTexture(texture);
        ap2.setMaterial(new Material(ambiental, emisivo, difuso, especular, 1));
        geometria.setAppearance(Box.FRONT, ap2);
        
        Appearance ap3 = new Appearance();
        ap3.setTextureAttributes(ta);
        loader = new TextureLoader("Imagenes/lobofocus.jpg", null);
        texture = loader.getTexture();
        ap3.setTexture(texture);
        ap3.setMaterial(new Material(ambiental, emisivo, difuso, especular, 17));
        geometria.setAppearance(Box.BACK, ap3);
        
        Appearance ap4 = new Appearance();
        ap4.setTextureAttributes(ta);
        loader = new TextureLoader("Imagenes/cofrefocus.jpg", null);
        texture = loader.getTexture();
        ap4.setTexture(texture);
        ap4.setMaterial(new Material(ambiental, emisivo, difuso, especular, 17));
        geometria.setAppearance(Box.LEFT, ap4);
        
        Appearance ap5 = new Appearance();
        ap5.setTextureAttributes(ta);
        loader = new TextureLoader("Imagenes/calaverafocus.jpg", null);
        texture = loader.getTexture();
        ap5.setTexture(texture);
        ap5.setMaterial(new Material(ambiental, emisivo, difuso, especular, 17));
        geometria.setAppearance(Box.RIGHT, ap5);
        
        Appearance ap6 = new Appearance();
        ap6.setTextureAttributes(ta);
        loader = new TextureLoader("Imagenes/pocionfocus.jpg", null);
        texture = loader.getTexture();
        ap6.setTexture(texture);
        ap6.setMaterial(new Material(ambiental, emisivo, difuso, especular, 17));
        geometria.setAppearance(Box.BOTTOM, ap6);
    }
    
    public void setGoodDiceFocus(){
        TextureLoader loader;
        Texture texture;
        TextureAttributes ta = new TextureAttributes();
        ta.setTextureMode(TextureAttributes.COMBINE);
        //Colores de los materiales
        Color3f ambiental = new Color3f(1, 1, 1);
        Color3f emisivo = new Color3f(1, 1, 1);
        Color3f difuso = new Color3f(1, 1, 1);
        Color3f especular = new Color3f(1, 1, 1);
        Material material= new Material(ambiental, emisivo, difuso, especular, 100);
        
        Appearance ap = new Appearance();
        loader = new TextureLoader("Imagenes/heroefocus.jpg", null);
        texture = loader.getTexture();
        ap.setTexture(texture);
        ap.setTextureAttributes(ta);
        ap.setMaterial(material);
        geometria.setAppearance(Box.TOP, ap);
        
        Appearance ap2 = new Appearance();
        loader = new TextureLoader("Imagenes/guerrerofoucs.jpg", null);
        texture = loader.getTexture();
        ap2.setTexture(texture);
        ap2.setTextureAttributes(ta);
        ap2.setMaterial(material);
        geometria.setAppearance(Box.FRONT, ap2);
        
        Appearance ap3 = new Appearance();
        loader = new TextureLoader("Imagenes/magofocus.jpg", null);
        texture = loader.getTexture();
        ap3.setTexture(texture);
        ap3.setTextureAttributes(ta);
        ap3.setMaterial(material);
        geometria.setAppearance(Box.BACK, ap3);
        
        Appearance ap4 = new Appearance();
        loader = new TextureLoader("Imagenes/ladronfocus.jpg", null);
        texture = loader.getTexture();
        ap4.setTexture(texture);
        ap4.setTextureAttributes(ta);
        ap4.setMaterial(material);
        geometria.setAppearance(Box.LEFT, ap4);
        
        Appearance ap5 = new Appearance();
        loader = new TextureLoader("Imagenes/clerigofocus.jpg", null);
        texture = loader.getTexture();
        ap5.setTexture(texture);
        ap5.setTextureAttributes(ta);
        ap5.setMaterial(material);
        geometria.setAppearance(Box.RIGHT, ap5);
        
        Appearance ap6 = new Appearance();
        loader = new TextureLoader("Imagenes/pergaminofocus.jpg", null);
        texture = loader.getTexture();
        ap6.setTexture(texture);
        ap6.setTextureAttributes(ta);
        ap6.setMaterial(material);
        geometria.setAppearance(Box.BOTTOM, ap6);
    }
    
    public void Focous(){
        if(focused){
            if(option == 1){
                setBadDice();
            }
            else{
                setGoodDice();
            }
            focused = false;
            
        }
        else{
            if(option == 1){
                setBadDiceFocus();
            }
            else{
                setGoodDiceFocus();
            }
            focused = true;  
        }
        if(!able_to_fight){
            Die();
        }
    }
    
    public int getOpt(){
        return option;
    }
    
    public boolean getFocused(){
        return focused;
    }
    
    public int getCara(){
        return cara;
    }
    
    public boolean getAbleToFight(){
        return able_to_fight;
    }
    
    public void Fight(){
        able_to_fight = false;
        Die();
    }
    
    public void clean(){
        if(option == 1){
            setBadDice();
        }
        else{
            setGoodDice();
        }
        focused = false;
    }
    
    public void Die(){
        TextureLoader loader;
        Texture texture;
        TextureAttributes ta = new TextureAttributes();
        ta.setTextureMode(TextureAttributes.COMBINE);
        //Colores de los materiales
        Color3f ambiental = new Color3f(1, 1, 1);
        Color3f emisivo = new Color3f(1, 1, 1);
        Color3f difuso = new Color3f(1, 1, 1);
        Color3f especular = new Color3f(1, 1, 1);
        Material material= new Material(ambiental, emisivo, difuso, especular, 100);
        
        Appearance apn = new Appearance();
        if(option ==1)
            loader = new TextureLoader("Imagenes/badCross.jpg", null);
        else
            loader = new TextureLoader("Imagenes/goodCross.jpg", null);
        texture = loader.getTexture();
        apn.setTexture(texture);
        apn.setTextureAttributes(ta);
        apn.setMaterial(material);
        
        if(cara == 0){ //goblin
            geometria.setAppearance(Box.FRONT, apn);
        }
        else if(cara == 1){ //lobo
            geometria.setAppearance(Box.BACK, apn);
        }
        else if(cara == 2){ //dragon
            geometria.setAppearance(Box.TOP, apn);
        }
        else if(cara == 3){ //calavera
            geometria.setAppearance(Box.RIGHT, apn);
        }
        else if(cara == 4){ //cofre
            geometria.setAppearance(Box.LEFT, apn);
        }
        else if(cara == 5){ //pocion
            geometria.setAppearance(Box.BOTTOM, apn);
        }
    }
}
