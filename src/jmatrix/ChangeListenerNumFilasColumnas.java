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

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;


public class ChangeListenerNumFilasColumnas implements ChangeListener {
    
    private final JMatrix frmOrigen;
    
    public ChangeListenerNumFilasColumnas(JMatrix frmOrigen) {
        
        this.frmOrigen = frmOrigen;
    }
    
    private JTextField crearEntrada(int x, int y) {
        
        JTextField nuevaEntrada = new JTextField("0", 3);
        nuevaEntrada.addFocusListener(frmOrigen.listenerEntradas);
        nuevaEntrada.setHorizontalAlignment(JTextField.CENTER);
        nuevaEntrada.setSize(nuevaEntrada.getPreferredSize());
        nuevaEntrada.setLocation(
            frmOrigen.lblFilas.getX() + x * (nuevaEntrada.getWidth() + 5) + 20,
            frmOrigen.numFilas.getY() + frmOrigen.numFilas.getHeight() * 2 + 20 + y * (nuevaEntrada.getHeight() + 5)
        );
        
        if(x > 0) {
            
            JTextField txtAnterior = frmOrigen.txtEntradas.get(y).get(x - 1);
            txtAnterior.setLocation(txtAnterior.getX() - 10, txtAnterior.getY());
        }
        
        if(y == 0) {
            
            JLabel lblVariable = new JLabel("R", JLabel.CENTER);
            lblVariable.setSize(nuevaEntrada.getSize());
            lblVariable.setLocation(nuevaEntrada.getX(), nuevaEntrada.getY() - lblVariable.getHeight());
            frmOrigen.lblNombresVariables.add(lblVariable);
            frmOrigen.add(lblVariable);
            if(x > 0) {
                
                JLabel lblAnterior = frmOrigen.lblNombresVariables.get(x - 1);
                lblAnterior.setLocation(lblAnterior.getX() - 10, lblAnterior.getY());
                lblAnterior.setText("x" + String.valueOf(x));
            }
        }
        
        return nuevaEntrada;
    }
    
    @Override
    public void stateChanged(ChangeEvent e) {
        
        while(frmOrigen.txtEntradas.size() != (int)frmOrigen.numFilas.getValue()) {
            
            if(frmOrigen.txtEntradas.size() > (int)frmOrigen.numFilas.getValue()) {
                
                for(JTextField entradas : frmOrigen.txtEntradas.get(frmOrigen.txtEntradas.size() - 1)) frmOrigen.remove(entradas);
                frmOrigen.txtEntradas.remove(frmOrigen.txtEntradas.size() - 1);
            }
            else {
                
                int ultimaPos = frmOrigen.txtEntradas.size();
                frmOrigen.txtEntradas.add(new ArrayList<JTextField>());
                for(int c = 0; c < (int)frmOrigen.numColumnas.getValue(); c++) {
                    
                    JTextField nuevaEntrada = crearEntrada(c, ultimaPos);
                    frmOrigen.txtEntradas.get(ultimaPos).add(nuevaEntrada);
                    frmOrigen.add(nuevaEntrada);
                }
            }
        }
        
        while(frmOrigen.txtEntradas.get(0).size() != (int)frmOrigen.numColumnas.getValue()) {            
            
            if(frmOrigen.txtEntradas.get(0).size() > (int)frmOrigen.numColumnas.getValue()) {
                
                for(List<JTextField> entradas : frmOrigen.txtEntradas) {
                    
                    frmOrigen.remove(entradas.get(entradas.size() - 1));
                    entradas.remove(entradas.size() - 1);
                    JTextField txtAnterior = entradas.get(entradas.size() - 1);
                    txtAnterior.setLocation(txtAnterior.getX() + 10, txtAnterior.getY());
                }
                
                frmOrigen.remove(frmOrigen.lblNombresVariables.get(frmOrigen.lblNombresVariables.size() - 1));
                frmOrigen.lblNombresVariables.remove(frmOrigen.lblNombresVariables.size() - 1);
                JLabel lblAnterior = frmOrigen.lblNombresVariables.get(frmOrigen.lblNombresVariables.size() - 1);
                lblAnterior.setLocation(lblAnterior.getX() + 10, lblAnterior.getY());
                lblAnterior.setText("R");
            }
            else {
                
                int ultimaPos = frmOrigen.txtEntradas.get(0).size();
                for (int c = 0; c < frmOrigen.txtEntradas.size(); c++) {
                    
                    JTextField nuevaEntrada = crearEntrada(ultimaPos, c);
                    frmOrigen.txtEntradas.get(c).add(nuevaEntrada);
                    frmOrigen.add(nuevaEntrada);
                }
            }
        }

        JMatrix.actualizarForm(frmOrigen);
        frmOrigen.btnCalcular.setLocation(
            frmOrigen.lblFilas.getX(),
            frmOrigen.txtEntradas.get(frmOrigen.txtEntradas.size() - 1).get(0).getY() + frmOrigen.txtEntradas.get(0).get(0).getHeight() + 20
        );
        frmOrigen.pnlResultados.setSize(frmOrigen.pnlResultados.getWidth(), frmOrigen.btnCalcular.getY());

        int anchoEntradas = frmOrigen.txtEntradas.get(0).get(frmOrigen.txtEntradas.get(0).size() - 1).getX() + frmOrigen.txtEntradas.get(0).get(0).getWidth();
        frmOrigen.pnlResultados.setLocation(anchoEntradas > frmOrigen.numColumnas.getX() + frmOrigen.numColumnas.getWidth() ? anchoEntradas : frmOrigen.numColumnas.getX() + frmOrigen.numColumnas.getWidth(),
            0
        );
        
        frmOrigen.setSize(frmOrigen.pnlResultados.getX() + frmOrigen.pnlResultados.getWidth() + 50, frmOrigen.btnCalcular.getY() + frmOrigen.btnCalcular.getHeight() + 50);
    }
}
