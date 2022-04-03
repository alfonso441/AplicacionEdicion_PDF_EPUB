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
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.annotation.RequiresApi;
        import androidx.fragment.app.Fragment;

        import com.nbsp.materialfilepicker.MaterialFilePicker;
        import com.nbsp.materialfilepicker.ui.FilePickerActivity;

        import org.apache.commons.io.FilenameUtils;

        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.FileInputStream;
        import java.io.InputStream;
        import java.io.IOException;
        import java.util.List;

        // Epublib
        import nl.siegmann.epublib.domain.Author;
        import nl.siegmann.epublib.domain.Book;
        import nl.siegmann.epublib.domain.Metadata;
        import nl.siegmann.epublib.domain.Resource;
        import nl.siegmann.epublib.domain.Spine;
        import nl.siegmann.epublib.domain.SpineReference;
        import nl.siegmann.epublib.domain.TOCReference;
        import nl.siegmann.epublib.epub.EpubWriter;
        import nl.siegmann.epublib.epub.EpubReader;

/****************************************************************************
 *  B5 - Remover sección de un archivo EPUB.
 ***************************************************************************/

///////////////////////////////////////////////////////////////////////////////////////////////////
public class B5_RemovePagesFromEPUB extends Fragment {
    // Inicializa variables
    private Button bt_b5_seleccionar_paginas, bt_b5_remover_paginas_al_epub;    // Botones para seleccionar página y remover página del archivo EPUB
    private TextView txt_path_show;                                             // Muestra el path del archivo seleccionado
    private TextView txt_rango;                                                 // Muestra el rango de secciones del archivo seleccionado
    private int rango;                                                          // Rango de secciones del archivo seleccionado
    private String pathEPUBselected;                                            // Path del archivo seleccionado
    private EditText SectionNumber;                                             // Ingreso del número de sección a borrar por el usuario
    private int integer_numSection;                                             // Número de sección a eliminar

    int RESULT_OK = -1;

    // Locación donde almacena los archivos de salida
    private static final String OUTPUT_DIR = "/storage/emulated/0/EPUB_Tools";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.b5_activity_remove_pages_from_epub, container, false);
        // Características del EPUB
        txt_rango = view.findViewById(R.id.txt_RANGO_indicaciones_b5);

        // Selecciona archivo EPUB
        txt_path_show = view.findViewById(R.id.txt_path_selected);
        bt_b5_seleccionar_paginas = view.findViewById(R.id.bt_b5_seleccionar_paginas);
        bt_b5_seleccionar_paginas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filePicker1();
            }
        });

        // Invoca el método para remover página del archivo EPUB
        SectionNumber =  view.findViewById(R.id.number_section2remove);
        bt_b5_remover_paginas_al_epub =  view.findViewById(R.id.bt_b5_remover_paginas_al_epub);
        bt_b5_remover_paginas_al_epub.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                try {

                    // Se capta el número de sección a eliminar del usuario
                    integer_numSection = Integer.valueOf(SectionNumber.getText().toString());

                    // Remover página de un archivo EPUB
                    RemovePageEPUB(pathEPUBselected, integer_numSection);
                    txt_path_show.setText("Sección removida del EPUB");

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
            // Rango de secciones del archivo
            try {
                RangoSecciones(pathEPUBselected);
            } catch (IOException e) {
                e.printStackTrace();
            }
            txt_rango.setText("Rango de secciones: 1-"+Integer.toString(rango));
        }
    }

    // Genera el string en un pop-up en pantalla con la dirección del archivo seleccionado
    private void displatToast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    // Rango de secciones de un archivo EPUB
    public void RangoSecciones(String pathEPUBselected) throws IOException {
        // Carga datos del EPUB
        EpubReader epubReader = new EpubReader();
        Book book0 = epubReader.readEpub(new FileInputStream(pathEPUBselected));
        // Se carga la lista de referencias del Spine
        List<SpineReference> spine_References = book0.getSpine().getSpineReferences();
        // Rango de secciones
        rango = spine_References.size();
    }

    // Remover página de un archivo EPUB
    public static void RemovePageEPUB(String pathEPUBselected, Integer integer_numSection)throws IOException {

        // Verificar-Crear Directorio de salida por defecto
        File folder = new File(OUTPUT_DIR);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Carga archivo EPUB y datos para archivo de salida
        File input_1 = new File(pathEPUBselected);                      // Archivo de entrada
        String title1 = input_1.getName();                              // Título del archivo de entrada
        String title_no_ext1 = FilenameUtils.removeExtension(title1);   // Título del archivo de entrada sin extensión
        String name = "5-" + title_no_ext1 + "_SectionRemoved.epub";      // Nombre del archivo de salida

        // Carga datos del EPUB
        EpubReader epubReader = new EpubReader();
        Book book = epubReader.readEpub(new FileInputStream(pathEPUBselected));

        // Se carga la lista de referencias del Spine
        List<SpineReference> spine_References = book.getSpine().getSpineReferences();
        spine_References.remove(integer_numSection-1); // Se remueve la sección

        // Se establece el nuevo spine
        Spine NewSpine = new Spine(spine_References);
        book.setSpine(NewSpine);

        /////////////////////////////////////

        // Crea objeto EpubWriter
        EpubWriter epubWriter = new EpubWriter();

        // Crea archivo EPUB en el dispositivo
        epubWriter.write(book, new FileOutputStream(OUTPUT_DIR + "/" + name));

        ///////////////////////////////////

    }

    // Carga un nuevo recurso
    private static InputStream getResource( String path ) throws IOException {
        //return B2_AddCover2EPUB.class.getResourceAsStream( path );
        InputStream stream = new FileInputStream(path);
        return stream;
    }

    // Crea un nuevo objeto Resource
    private static Resource getResource(String path, String href ) throws IOException {
        //try {
        return  new Resource( getResource( path ), href );
        //} catch (IOException e) {
        //e.printStackTrace();
        //return new Resource( getResource( path ), href );
        //}
    }

}

///////////////////////////////////////////////////////////////////////////////////////////////////