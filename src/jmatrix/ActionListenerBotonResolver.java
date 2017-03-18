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

import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.*;


public class ActionListenerBotonResolver implements ActionListener {
    
    private final JMatrix frmOrigen;
    
    public ActionListenerBotonResolver(JMatrix frmOrigen) {
        
        this.frmOrigen = frmOrigen;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {

        for(JLabel solucion : frmOrigen.lblSoluciones) frmOrigen.pnlResultados.remove(solucion);
        frmOrigen.lblSoluciones.clear();

        List<List<Double>> numeros = new ArrayList<>();
        try {

            for(List<JTextField> filas : frmOrigen.txtEntradas) {

                List<Double> filaAgregar = new ArrayList<>();
                for(JTextField entrada : filas) filaAgregar.add(Double.parseDouble(entrada.getText()));
                numeros.add(filaAgregar);
            }       
        }
        catch(NumberFormatException ex) {

            frmOrigen.lblResultado.setText("Entradas incorrectas");
            frmOrigen.lblResultado.setSize(frmOrigen.lblResultado.getPreferredSize());
            JMatrix.actualizarForm(frmOrigen);
            return;
        }

        // Toda la magia se compone de este trozito de código
        // Para saber que columnas fueron eliminadas (en caso de que tengan puros 0's)
        List<Integer> variablesEliminadas = new ArrayList<>();
        // Valores de las columnas eliminadas (antes de que quedaran los 0's)
        LinkedList<List<Double>> columnasEliminadas = new LinkedList<>();
        // Contadores genéricos
        int x, y, c;
        // Comenzamos la eliminación
        // Primero el triangular inferior
        for(x = 0; x < numeros.size(); x++) {
            // Intercambiamos filas para que no surjan divisiones entre 0
            for(c = x + 1; c < numeros.size(); c++) {
                
                if(x == numeros.get(x).size() - 1) break;
                if(numeros.get(x).get(x) != 0) break;
                numeros.add(x, numeros.get(c));
                numeros.remove(c + 1);
            }
            // Si en alguna columna quedaron puros 0's la eliminamos de la matriz y pasamos a la siguiente fila
            if(numeros.get(x).get(x) == 0) {
                
                // Recordamos los valores que había en las filas anteriores (servirán para hacer despejes en el resultado)
                columnasEliminadas.add(new ArrayList<Double>());
                for(y = 0; y < numeros.size(); y++) {
                    
                    if(y < x) columnasEliminadas.getLast().add(numeros.get(y).get(x));
                    numeros.get(y).remove(x);
                }
                // Si nos hemos quedado sin variables
                if(numeros.get(x).size() == 1) {
                    
                    frmOrigen.lblResultado.setText("Sin soluciones");
                    frmOrigen.lblResultado.setSize(frmOrigen.lblResultado.getPreferredSize());
                    JMatrix.actualizarForm(frmOrigen);
                    return;
                }
                variablesEliminadas.add(x + variablesEliminadas.size());
                x--;
                continue;
            }
            // Obtenemos los 1's diagonales
            else {
                
                if(numeros.get(x).get(x) != 1) {
                    for(c = x + 1; c < numeros.get(x).size(); c++)
                        numeros.get(x).set(c, numeros.get(x).get(c) / numeros.get(x).get(x));
                    numeros.get(x).set(x, 1d);
                }
                // Ponemos en 0's el resto de la columna justo por debajo del 1 recién transformado
                if(x < numeros.size() - 1) {
                    
                    for(y = x + 1; y < numeros.size(); y++) {
                        for(c = x + 1; c < numeros.get(y).size(); c++)
                            numeros.get(y).set(c, numeros.get(y).get(c) - numeros.get(y).get(x) * numeros.get(x).get(c));
                        numeros.get(y).set(x, 0d);
                    }
                }
            }
            // Comprobamos si no nos han quedado puros 0's en alguna fila
            for(y = x + 1; y < numeros.size(); y++)
                for(c = x + 1; c < numeros.get(y).size(); c++) {
                    // Si toda la fila está llena de 0's ya no nos preocupamos por ella y la borramos
                    if(c == numeros.get(y).size() - 1) {
                        
                        if(numeros.get(y).get(c) != 0) {
                            
                            frmOrigen.lblResultado.setText("Sin soluciones");
                            frmOrigen.lblResultado.setSize(frmOrigen.lblResultado.getPreferredSize());
                            JMatrix.actualizarForm(frmOrigen);
                            return;
                        }
                        numeros.remove(y--);
                    }
                    // Si hay por lo menos un valor diferente de 0 la fila está bien
                    if(numeros.get(y).get(c) != 0) break;
                }
        }
        
        // Ahora el triangular superior 
        for(c = 1; c < numeros.size(); c++) for(y = 0; y < c; y++) {
            
            for(x = c + 1; x < numeros.get(y).size(); x++)
                numeros.get(y).set(x, numeros.get(y).get(x) - numeros.get(y).get(c) * numeros.get(c).get(x));
            numeros.get(y).set(c, 0d);
        }
        
        frmOrigen.lblResultado.setText("Soluciones:");
        frmOrigen.lblResultado.setSize(frmOrigen.lblResultado.getPreferredSize());
        // Construir las etiquetas de las respuestas
        int contColumnasEliminadas = 0;
        for(c = 0; c < numeros.size(); c++) {
            
            JLabel etiquetaRespuesta = new JLabel();
            if(variablesEliminadas.size() > contColumnasEliminadas)
                if(c + contColumnasEliminadas == variablesEliminadas.get(contColumnasEliminadas)) contColumnasEliminadas++;
            String cadenaRespuesta = 'x' + String.valueOf(c + contColumnasEliminadas + 1) + " = ";
            etiquetaRespuesta.setLocation(frmOrigen.lblResultado.getX(), frmOrigen.txtEntradas.get(c).get(0).getY());
            
            String numeroRespuesta = new DecimalFormat("#.##").format(numeros.get(c).get(numeros.get(c).size() - 1));
            cadenaRespuesta += (numeroRespuesta.equals("-0") ? "0" : numeroRespuesta) + ' ';
            // Si eliminamos alguna columna anteriormente, puede que tuviera valores que todavía pueden despejarse en la respuesta
            for(x = 0; x < columnasEliminadas.size(); x++) {
                
                if(columnasEliminadas.get(x).size() <= c) continue;
                if(columnasEliminadas.get(x).get(c) == 0) continue;
                cadenaRespuesta += columnasEliminadas.get(x).get(c) < 0 ? '+' : '-';
                String coeficiente = new DecimalFormat("#.##").format(Math.abs(columnasEliminadas.get(x).get(c)));
                cadenaRespuesta += ' ' + (coeficiente.equals("1") ? "" : coeficiente) + 'x' + String.valueOf(variablesEliminadas.get(x) + 1) + ' ';
            }
            // Si quedaron columnas de mas, el resto de los valores son despejados en la respuesta
            for(x = numeros.size(); x < numeros.get(c).size() - 1; x++) {
                
                if(numeros.get(c).get(x) == 0) continue;
                cadenaRespuesta += numeros.get(c).get(x) < 0 ? '+' : '-';
                String coeficiente = new DecimalFormat("#.##").format(Math.abs(numeros.get(c).get(x)));
                cadenaRespuesta += ' ' + (coeficiente.equals("1") ? "" : coeficiente) + 'x' + String.valueOf(x + contColumnasEliminadas + 1) + ' ';
            }

            etiquetaRespuesta.setText(cadenaRespuesta);
            etiquetaRespuesta.setSize(etiquetaRespuesta.getPreferredSize());
            frmOrigen.lblSoluciones.add(etiquetaRespuesta);
        }
        
        for(c = 0; c < frmOrigen.lblSoluciones.size(); c++) frmOrigen.pnlResultados.add(frmOrigen.lblSoluciones.get(c));
        JMatrix.actualizarForm(frmOrigen);
    }
}
