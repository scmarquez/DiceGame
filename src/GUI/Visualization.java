/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import javax.media.j3d.Canvas3D;

/**
 *
 * @author fvelasco
 */
public class Visualization extends javax.swing.JFrame {

  /**
   * Creates new form Visualization
   */
  public Visualization(Canvas3D canvas) {
    initComponents();
    // Varios atributos de la ventana
    setTitle("Java 3D Visualization Window");
    setSize(700, 700);
    setLocation (100, 100);
    // Se le inserta el canvas
    Container contentPane = this.getContentPane();
    contentPane.setLayout(new BorderLayout());
    contentPane.add(canvas, BorderLayout.CENTER);
    repaint();
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 400, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 300, Short.MAX_VALUE)
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents



  // Variables declaration - do not modify//GEN-BEGIN:variables
  // End of variables declaration//GEN-END:variables
}