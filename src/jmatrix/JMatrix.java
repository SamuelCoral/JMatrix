/*
 * Copyright (C) 2016 SamuelCoral
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package jmatrix;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;


public class JMatrix extends JFrame {
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JMatrix().setVisible(true);
            }
        });
    }
    
    public JMatrix() {
        inicializarForm();
    }
    
    public JLabel lblFilas;
    public JLabel lblColumnas;
    public JSpinner numFilas;
    public JSpinner numColumnas;
    public List<JLabel> lblNombresVariables;
    public List<List<JTextField>> txtEntradas;
    public JButton btnCalcular;
    public JPanel pnlResultados;
    public JLabel lblResultado;
    public ArrayList<JLabel> lblSoluciones;
    public ChangeListenerNumFilasColumnas listenerNum;
    public FocusListenerEntradas listenerEntradas;
    public ActionListenerBotonResolver listenerBoton;
    
    private void inicializarForm() {
        
        setTitle("Resolver sistemas de ecuaciones lineales");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        
        lblFilas = new JLabel("Filas:");
        lblColumnas = new JLabel("Columnas:");
        numFilas = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        numColumnas = new JSpinner(new SpinnerNumberModel(2, 2, 10, 1));
        lblNombresVariables = new ArrayList<>();
        txtEntradas = new ArrayList<>();
        pnlResultados = new JPanel(null);
        btnCalcular = new JButton("Resolver");
        lblResultado = new JLabel("Soluciones:");
        lblSoluciones = new ArrayList<>();
        listenerNum = new ChangeListenerNumFilasColumnas(this);
        listenerEntradas = new FocusListenerEntradas();
        listenerBoton = new ActionListenerBotonResolver(this);
        
        lblFilas.setLocation(15, 15);
        lblFilas.setSize(lblFilas.getPreferredSize());
        add(lblFilas);
        
        numFilas.setLocation(lblFilas.getX() + lblFilas.getWidth() + 10, lblFilas.getY());
        numFilas.setSize(numFilas.getPreferredSize());
        add(numFilas);
        
        lblColumnas.setLocation(numFilas.getX() + numFilas.getWidth() + 20, numFilas.getY());
        lblColumnas.setSize(lblColumnas.getPreferredSize());
        add(lblColumnas);
        
        numColumnas.setLocation(lblColumnas.getX() + lblColumnas.getWidth() + 10, lblColumnas.getY());
        numColumnas.setSize(numColumnas.getPreferredSize());
        add(numColumnas);
        
        btnCalcular.setSize(btnCalcular.getPreferredSize());
        btnCalcular.addActionListener(listenerBoton);
        add(btnCalcular);
        
        pnlResultados.setSize(200, 0);
        lblResultado.setSize(lblResultado.getPreferredSize());
        lblResultado.setLocation(35, lblFilas.getY());
        pnlResultados.add(lblResultado);
        add(pnlResultados);
        
        numFilas.addChangeListener(listenerNum);
        numFilas.setValue(3);
        numColumnas.addChangeListener(listenerNum);
        numColumnas.setValue(4);
        
        Dimension resPantalla = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((resPantalla.width - getWidth() - 150) / 2, (resPantalla.height - getHeight() - 100) / 2);
    }
    
    public static void actualizarForm(JFrame form) {
        
        form.revalidate();
        form.repaint();
    }
}
