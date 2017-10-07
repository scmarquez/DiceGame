/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dicegame;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

/**
 *
 * @author sergio
 */
public class Escena {
    Universo universo;
  
    AmbientLight ambient_light;
    DirectionalLight luz_direccional;
    GameEngine motor_de_juego;
    
    public Escena(){
        universo = new Universo();
        ambient_light = new AmbientLight(true, new Color3f(0.1f, 0.1f, 0.1f));
        ambient_light.setInfluencingBounds(new BoundingSphere(new Point3d(0, 0, 0),1000));
        
        luz_direccional = new DirectionalLight(true, new Color3f(0.9f, 0.9f, 0.9f), new Vector3f(0.5f, -0, -1));
        luz_direccional.setInfluencingBounds(new BoundingSphere(new Point3d(0, 0, 0), 1000));
        
        motor_de_juego = new GameEngine();
        
        
    }
    public void Draw(){
        
        //De universo se cuelgan las luces
        universo.AddNode(ambient_light);
        universo.AddNode(luz_direccional);
        //Se añaden los dados
        universo.AddNode(motor_de_juego.GetDados()[0].GetNode());
        universo.AddNode(motor_de_juego.GetDados()[1].GetNode());
        universo.AddNode(motor_de_juego.GetDados()[2].GetNode());
        universo.AddNode(motor_de_juego.GetDados()[3].GetNode());
        //Se añade el tablero
        universo.AddNode(motor_de_juego.GetTablero().GetNode());
        //Se añaden los botones
        universo.AddNode(motor_de_juego.GetMulligan().GetNode());
        universo.AddNode(motor_de_juego.GetFight().GetNode());
        //Inicializa el juego
        motor_de_juego.LanzarDados();
        //Inicialización del universo
        universo.CreateUniverse();
        universo.SetScene(this);    
    }
    public void LanzarDados(){
        motor_de_juego.Mulligan();
        
    }
    public void GameStart(){
        motor_de_juego.LanzarDados();
    }
    
    public void selectDado(int id){
        motor_de_juego.selectDado(id);
    }
    
    public void Lucha(){
        motor_de_juego.Lucha();
    }

}
