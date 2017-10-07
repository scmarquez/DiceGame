/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dicegame;

import javax.media.j3d.Appearance;
import java.util.Random;
import javax.media.j3d.Material;
import javax.swing.JOptionPane;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

/**
 *
 * @author sergio
 */
public class GameEngine {
    Dado[] dados;
    Tablero tablero;
    Boton mulligan;
    Boton fight;
    int aliado;
    int enemigo;
    int puntos;
    
    public GameEngine(){
        puntos = 10;
        aliado = -1;
        enemigo = -1;
        mulligan = new Boton(-0.2f, 0.89f, -0.09f, new Appearance(), "Imagenes/mulligan.jpg", 0);
        fight = new Boton(0.2f, 0.89f, -0.09f, new Appearance(), "Imagenes/fight.jpg", 1);
        dados = new Dado[4];
        //Dados aliados
        //La segunda coordenada representa el eje y del mundo real
        dados[0] = new Dado((float) 0.1, 0,-0.5f,0.5f,0.2f,1,0,-1);
        dados[1] = new Dado((float) 0.1, 0,0.5f,0.5f,0.2f,-1,0,-1);
        dados[2] = new Dado((float) 0.1, 1,-0.5f,-0f,0.2f,1,0,0);
        dados[3] = new Dado((float) 0.1, 1,0.5f,-0f,0.2f,-1,0,0);
        dados[0].setId(0);
        dados[1].setId(1);
        dados[2].setId(2);
        dados[3].setId(3);
        
        Appearance apariencia_tablero = new Appearance();
        Material material_tablero = new Material(new Color3f(1.0f, 0.0f, 0.0f), 
                new Color3f(1f, 1f, 0f), 
                new Color3f(1.0f, 1f, 0f), 
                new Color3f(1.0f, 1f, 0f), 80);
        apariencia_tablero.setMaterial(material_tablero);
        
        tablero = new Tablero(1,1,apariencia_tablero);
    }
    public void LanzarDados(){
        //La tercera coordenada representa al eje z ficticio(eje y real)
        Point3f[]posiciones_iniciales = dados[0].CalculaTrayectoria(30, -0.5f, 0.5f, 0.2f);
        Point3f[]posiciones_iniciales2 = dados[1].CalculaTrayectoria(30, 0.3f, 0.5f, 0.2f);
        Point3f[]posiciones_iniciales_enemigo1 = dados[2].CalculaTrayectoria(30, -0.5f, 0.2f, -0f);
        Point3f[]posiciones_iniciales_enemigo2 = dados[3].CalculaTrayectoria(30, 0.5f, 0.2f, -0f);
        
        dados[0].Interpola(posiciones_iniciales,30,8000);
        dados[1].Interpola(posiciones_iniciales2, 30, 7000);
        dados[2].Interpola(posiciones_iniciales_enemigo1, 30, 9000);
        dados[3].Interpola(posiciones_iniciales_enemigo2, 30, 10000);
    }
    public void Mulligan(){
        if(puntos > 0 && puntos < 100){
            //Se lanzan los dados de forma secuencial.
            dados[0].Relanzar(5000);
            dados[1].Relanzar(6000);
            dados[2].Relanzar(3000);
            dados[3].Relanzar(4000);
            puntos -=2;
            enemigo = -1;
            aliado = -1;
        }
        else{
            JOptionPane.showMessageDialog(null, "Eres prisionero de la mazmorra.", "DERROTA", JOptionPane.INFORMATION_MESSAGE);
        }
        
        
    }
    public Dado[] GetDados(){
        return dados;
    }
    public Tablero GetTablero(){
        return tablero;
    }
    public Boton GetMulligan(){
        return mulligan;
    }
    public Boton GetFight(){
        return fight;
    }
    
    public void selectDado(int id){
        dados[id].Focous();
        int tipo = dados[id].getOpt();
        for(int i = 0; i < 4; i++){
            if(dados[i].getId() != id && dados[i].getOpt() == tipo && dados[i].getFocused()){
                dados[i].Focous();
            }
        }
        if(dados[id].getOpt() == 0){
            aliado = id;
        }
        else{
            enemigo = id;
        }
        
        
    }
    
    public void Lucha(){
        //Una vez que se han seleccionado 2 dados se procede a la lucha y se muestra el resultado
        if(aliado != -1 && enemigo != -1 && dados[aliado].getAbleToFight() && dados[enemigo].getAbleToFight()){
            dados[aliado].Fight();
            dados[enemigo].Fight();
            //Si hay seleccionados dos dados se procede al combate
            if(dados[aliado].getCara() == dados[enemigo].getCara() ){
                //En este caso gana siempre
                puntos += 5;
                JOptionPane.showMessageDialog(null, "puntuacion: "+puntos, "GANAS!!", JOptionPane.INFORMATION_MESSAGE);
                if(puntos >= 100){
                    JOptionPane.showMessageDialog(null, "Eres el nuevo amo de la mazmorra", "TRIUNFO", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            else{
                //En este caso puede ganar o perder en función de los puntos o ganar si llevas al heroe
                Random rmd = new Random();
                float next_rmd = rmd.nextFloat();
                System.out.println("aleatorio: "+next_rmd);
                if(dados[aliado].getCara() == 2){
                    //ganas el duelo por ser el heroe
                    puntos += 6;
                    JOptionPane.showMessageDialog(null, "puntuacion: "+puntos, "BONUS DE HEROE!!!", JOptionPane.INFORMATION_MESSAGE);
                    if(puntos >= 100){
                        JOptionPane.showMessageDialog(null, "Eres el nuevo amo de la mazmorra", "TRIUNFO", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                else{
                    /////////////////////////
                    if(next_rmd > (float)puntos/100){
                        if(dados[enemigo].getCara() == 2){
                            //Te mata el dragón
                            puntos = 10;
                            JOptionPane.showMessageDialog(null, "puntuacion: "+puntos, "DEVORADO", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else{
                            //pierdes el duelo
                            puntos -= 4;
                            JOptionPane.showMessageDialog(null, "puntuacion: "+puntos, "PIERDES!!", JOptionPane.INFORMATION_MESSAGE);
                        }
                        
                    }
                    else{
                        //ganas el duelo matando al dragón
                        if(dados[enemigo].getCara() == 2){
                            puntos += 8;
                            JOptionPane.showMessageDialog(null, "puntuacion: "+puntos, "MATADRAGONES", JOptionPane.INFORMATION_MESSAGE);
                            if(puntos >= 100){
                                JOptionPane.showMessageDialog(null, "Eres el nuevo amo de la mazmorra", "TRIUNFO", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                        else{
                            puntos += 6;
                            JOptionPane.showMessageDialog(null, "puntuacion: "+puntos, "VALENTÍA!!", JOptionPane.INFORMATION_MESSAGE);
                            if(puntos >= 100){
                                JOptionPane.showMessageDialog(null, "Eres el nuevo amo de la mazmorra", "TRIUNFO", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                        
                    }
                }
                
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "puntuacion: "+puntos, "HAZ UNA SELECCION", JOptionPane.INFORMATION_MESSAGE);
        }
        
    }
    
}
