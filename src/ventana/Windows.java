/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventana;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


/**
 *
 * @author Yovany
 */
public class Windows extends JFrame{
    public Windows()
     {
        super ("Manejo de Archivos");
        this.getContentPane().setLayout(new GridBagLayout());
        setSize(500,500);

        GridBagConstraints constraints = new GridBagConstraints();

        cajaTexto = new JTextArea();
        scrollPane = new JScrollPane(cajaTexto);
        
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.gridheight = 2;
        // El area de texto debe estirarse en ambos sentidos. Ponemos el campo fill.
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1.0;
        this.getContentPane().add (scrollPane, constraints);
        constraints.weighty = 0.0;

        btnAbrir = new JButton ("Abrir");
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weighty = 1.0;
        // El botón 1 debe ocupar la posición NORTH de su celda
        constraints.anchor = GridBagConstraints.NORTH;
        // El botón 1 no debe estirarse. Habíamos cambiado este valor en el
        // area de texto, asi que lo restauramos.
        constraints.fill = GridBagConstraints.NONE;
        this.getContentPane().add (btnAbrir, constraints);
        // Restauramos valores por defecto
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weighty = 0.0;    

        btnBuscar  = new JButton ("Buscar");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        this.getContentPane().add (btnBuscar, constraints);
        
        btnSalir  = new JButton ("Salir");
        constraints.gridx = 2;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        this.getContentPane().add (btnSalir, constraints);

        txtPalabraBuscar = new JTextField ("Palabra a buscar");
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1.0;
        // El campo de texto debe estirarse sólo en horizontal.
        constraints.fill = GridBagConstraints.HORIZONTAL;
        this.getContentPane().add (txtPalabraBuscar, constraints);
                                 
         
         btnSalir.addActionListener(new ManejadorDeEvnetos());
         btnAbrir.addActionListener(new ManejadorDeEvnetos());
         btnBuscar.addActionListener(new ManejadorDeEvnetos());
         btnBuscar.setEnabled(false);
         txtPalabraBuscar.setEnabled(false);
     }
    
    private File getFileOrDirectory()
    {
      // display file dialog, so user can choose file or directory to open
      JFileChooser fileChooser = new JFileChooser();
            
      fileChooser.setFileSelectionMode(
         JFileChooser.FILES_AND_DIRECTORIES );

      int result = fileChooser.showOpenDialog( this );

      // if user clicked Cancel button on dialog, return
      if ( result == JFileChooser.CANCEL_OPTION )
         return null;

      File fileName = fileChooser.getSelectedFile(); // get File

      // display error if invalid
      if ( ( fileName == null ) || ( fileName.getName().equals( "" ) ) )
      {
         JOptionPane.showMessageDialog( this, "Invalid Name",
            "Invalid Name", JOptionPane.ERROR_MESSAGE );
         System.exit( 1 );
      } // end if

      return fileName;
   } // end method getFile
    
    private void showError(File name){
         JOptionPane.showMessageDialog( this, name +
                  " does not exist.", "ERROR", JOptionPane.ERROR_MESSAGE );
    }
    
     private void loadContent(File name){
        palabras = new HashMap();
        palabras = abrirArchivo(name);
        
        for (Map.Entry<String, Integer> entry : palabras.entrySet()) {
            String key = entry.getKey();
            Integer val = entry.getValue();
            System.out.println(key + "->" + val);            
        }
     }
     
     private Map<String , Integer> abrirArchivo(File name){        
        FileReader fr;
        Map<String , Integer> palabras = new HashMap();
        try {
            if( name.exists() ){ 
                fr = new FileReader(name, Charset.forName("UTF-8"));
                BufferedReader br = new BufferedReader(fr);
                String linea;
                StringTokenizer st = null;
                int totalLineas = 0;
                String texto = "";
                while ((linea = br.readLine()) != null) {
                    texto += linea + "\n";
                    st = new StringTokenizer(linea);
                    totalLineas++;
                    while (st.hasMoreTokens()){
                        String palabra = st.nextToken().toLowerCase();
                        if (!palabras.containsKey(palabra)){
                            palabras.put(palabra, 1);
                        }else{
                            palabras.replace(palabra,palabras.get(palabra) + 1);
                        }                    
                    }                    
                }
                palabras.put("totalLineas", totalLineas);
                cajaTexto.setText(texto);
                btnBuscar.setEnabled(true);
                txtPalabraBuscar.setEnabled(true);
                fr.close();                
            }else{
                System.out.println("Arhivo no existe");
            }
                                                           
        } catch (FileNotFoundException ex) {
            System.out.println("Excepcion " + ex.getMessage());
        } catch (IOException e){
            System.out.println("Excepcion " + e.getMessage());
        }
        return palabras;
    }
     
     private void buscarPalabras(){
         //Obtiene la palabra a buscar
         String palabra = txtPalabraBuscar.getText().toLowerCase().trim();
         //Obtiene la primera letra
         char palabraChar=palabra.charAt(0);
         
         //En este caso para verificar si es una sola letra
         if (txtPalabraBuscar.getText().length() == 1){
             int contadorPalabras = 0;
             String listaPalabras = "";
             
            //Este for recorre el map de palabras y lo guarda en "entry"
            for (Map.Entry<String, Integer> entry : palabras.entrySet()) {
                //Si la primera letra es igual a la primera letra de la "key" (Es decir, a la palabra) 
                if (entry.getKey().charAt(0) == palabraChar){
                    //Suma el contador
                    contadorPalabras += entry.getValue();
                    //Añade la palabra a una lista
                    listaPalabras = listaPalabras + entry.getKey() + "\n";
                }
                
            }
            JOptionPane.showMessageDialog(this, "Las palabras que empiezan por " + palabra + " son en total: " + contadorPalabras);
            JOptionPane.showMessageDialog(this, listaPalabras);
                    
         }   
         
         if (cajaTexto.getText().length() >0){
            int total = palabras.get(palabra);
            
            JOptionPane.showMessageDialog(this,"aparece :" + total + " Veces" );
         }else{
             JOptionPane.showMessageDialog(this,"Texto no cargado");
         }
         
     }
    
    private class ManejadorDeEvnetos implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
         if (e.getSource() == btnSalir) {
             dispose();
         }else if(e.getSource() == btnAbrir){
             File name = getFileOrDirectory();
             if(name != null){
                             
                if ( name.exists() ) // if name exists, output information about it                              
                {
                    loadContent(name);
                }else // not file or directory, output error message
                {
                   showError(name);
                } // end else 
             }
         }else if(e.getSource() == btnBuscar){
             buscarPalabras();
         }
        }
    }
           
    JScrollPane scrollPane; 
    JTextArea cajaTexto;
    JButton btnAbrir ;
    JButton btnBuscar;
    JButton btnSalir ;
    JTextField txtPalabraBuscar;
    Map<String , Integer> palabras;
    JLabel lblPalabra;
    JLabel lblLetra;
    JTextField txtPalabras;
    JTextField txtLetras;
}


