/*****************************************************************************
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Alfonso Castillo Orozco 2022
 alfonso.castillo@ucr.ac.cr
 *****************************************************************************/

// Se importan los paquetes necesarios

package com.aaa.editorapachepdfopenbox;

    import android.content.Intent;
    import android.os.Build;
    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.annotation.RequiresApi;
    import androidx.fragment.app.Fragment;
    import androidx.navigation.Navigation;

    import com.nbsp.materialfilepicker.MaterialFilePicker;
    import com.nbsp.materialfilepicker.ui.FilePickerActivity;

    import java.io.FileInputStream;
    import java.io.IOException;

    // Epublib
    import nl.siegmann.epublib.domain.Author;
    import nl.siegmann.epublib.domain.Book;
    import nl.siegmann.epublib.domain.Metadata;
    import nl.siegmann.epublib.domain.Resource;
    import nl.siegmann.epublib.domain.SpineReference;
    import nl.siegmann.epublib.domain.TOCReference;
    import nl.siegmann.epublib.epub.EpubWriter;
    import nl.siegmann.epublib.epub.EpubReader;

    // WebView
    import android.webkit.WebView;
    import java.util.Calendar;
    import java.util.Collection;
    import java.util.Date;
    import java.text.*;
    import java.util.List;

/****************************************************************************
 *  B6 - Lector de archivos EPUB.
 ***************************************************************************/

///////////////////////////////////////////////////////////////////////////////////////////////////
public class B6_EPUBReader extends Fragment {
    // Inicializa variables
    private Button boton_b6_cargar_epub, boton_b6_leer_epub;    // Botones para seleccionar archivo EPUB y leer archivo
    private TextView txt_path_show;                             // Muestra el path del archivo seleccionado
    public static String pathEPUBselected;                      // Path del archivo PDF seleccionado

    // Variable con datos para desplegar en WebView
    public static String dataB6;

    int RESULT_OK = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.b6_activity_epub_reader, container, false);

        // Selecciona archivo EPUB
        boton_b6_cargar_epub =  view.findViewById(R.id.bt_b6_cargar_epub);
        boton_b6_cargar_epub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filePicker1();
            }
        });

        // Invoca el método para leer el archivo EPUB
        boton_b6_leer_epub =  view.findViewById(R.id.bt_b6_leer_epub);
        txt_path_show = view.findViewById(R.id.txt_path_selected);
        boton_b6_leer_epub.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                try {

                    // Leer EPUB: Abre archivo EPUB
                    leerEPUB(pathEPUBselected);

                    // Se abre nueva Actividad donde se lee el archivo
                    Navigation.findNavController(view).navigate(R.id.activity_6B_web_view);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    // Seleccionar archivo desde el dispositivo
    private void filePicker1() {
        new MaterialFilePicker()
                //.withActivity(getActivity())
                .withSupportFragment(this)
                .withHiddenFiles(true)
                .withRequestCode(1000)
                .start();
    }

    // Responde según las solicitudes en onClick para captar dirección del archivo seleccionado
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Archivo 1
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            pathEPUBselected = filePath;
            txt_path_show.setText(pathEPUBselected);
            displatToast("path: " + pathEPUBselected);
        }
    }

    // Genera el string en un pop-up en pantalla con la dirección del archivo seleccionado
    private void displatToast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    // Leer EPUB: Abre archivo EPUB
    public static void leerEPUB(String pathEPUBselected)throws IOException {

        // Carga datos del EPUB
        EpubReader epubReader = new EpubReader();
        Book book = epubReader.readEpub(new FileInputStream(pathEPUBselected));

        // Manipulación de Datos del EPUB
        List<SpineReference> spine_References = book.getSpine().getSpineReferences();
        Resource r = spine_References.get(0).getResource();
        dataB6 = new String( r.getData() );
        for (int i = 1; i<spine_References.size(); i++) {
            r = spine_References.get(i).getResource();
            dataB6 = dataB6 + "<br>" + new String(r.getData());
        }

    }

}

///////////////////////////////////////////////////////////////////////////////////////////////////