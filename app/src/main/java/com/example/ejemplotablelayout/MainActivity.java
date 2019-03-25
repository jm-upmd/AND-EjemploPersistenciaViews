package com.example.ejemplotablelayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Hashtable;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    TableLayout tableLayout;
    Button botonGrabar;
    Button botonLeer;

    //Hashtable para guardar las referencias a los edittext.
    // Se guarda (idRecurso,edittext)
    Hashtable<Integer, EditText> listaViews = new Hashtable<>();

    //Fichero donde se guardan el contenido de los edittexts
    static final String NOMBRE_FICHERO = "edittexts.data";

    //Caracter de separación utilizado para separar campos id;edittext en el fichero
    private static final String SEPARADOR = ";";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Views del layout
        tableLayout = findViewById(R.id.tableLayout);
        botonGrabar = findViewById(R.id.botonGuardar);
        botonLeer = findViewById(R.id.botonCargar);

        // Listerners botones
        botonGrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grabaEnFichero();
            }
        });

        botonLeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leerDeFichero();
            }
        });

        // Metemos los editText del TableLayout en un HashTable para manejarlos con más comodidad

        creaColeccionDeViews();
    }

    private void leerDeFichero() {

        try {
            // Podemos usar Scanner para leer fichero de texto
            Scanner scanner = new Scanner(openFileInput(NOMBRE_FICHERO));
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                String[] campos = linea.split(SEPARADOR);
                // Actualiza edittext con texto leido del fichero
                // Si algún edittext estaba vacío cuando se guardó se le asigna ""

                listaViews.get(Integer.parseInt(campos[0])).setText(campos.length == 2 ? campos[1] : "");


            }
            scanner.close();

        } catch (FileNotFoundException e) {
            Log.e("xxx", "No se encuentra el fichero");
        } catch (NumberFormatException e) {
            Log.e("xxx", "Error: No se puede convertir a entero el valor leido del fichero");
        }
    }

    private void grabaEnFichero() {
        OutputStreamWriter out;
        final char finLinea = '\n';
        try {
            out = new OutputStreamWriter(openFileOutput(NOMBRE_FICHERO, 0));
            StringBuilder stb = new StringBuilder();
            for (Integer key : listaViews.keySet()) {
                stb.append(key)
                        .append(SEPARADOR)
                        .append(listaViews.get(key).getText().toString())
                        .append(finLinea);
                out.write(stb.toString());
                stb.setLength(0);  // Limpia el stringbuilder
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            Log.e("xxx", "Error de escritura en fichero");
        }

    }

    private void creaColeccionDeViews() {

        for (int fila = 0; fila < tableLayout.getChildCount(); fila++) {
            TableRow rowLayout = (TableRow) tableLayout.getChildAt(fila);
            int numBotonesFila = rowLayout.getChildCount();
            for (int posBoton = 0; posBoton < numBotonesFila; posBoton++) {
                EditText editText = (EditText) rowLayout.getChildAt(posBoton);
                // Añade al hashtable el par clave,valor (id,edittext)

                listaViews.put(editText.getId(), editText);

                /*
                // todos los views tienen un atributo llamado tag que vale para guardar en el
                // información adicional que queramos tener asiciada a la view.

                editText.setTag(0,34);
                editText.setTag(1,"otro dato");

                //Estos datos luego los podemos obtener con getTag
                int numero = (Integer) editText.getTag(0);
                String texto = (String) editText.getTag(1);*/

            }
        }
    }
}
