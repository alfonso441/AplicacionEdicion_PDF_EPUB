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
 *  B3 - Agrega Capítulo a un archivo EPUB.
 ***************************************************************************/

///////////////////////////////////////////////////////////////////////////////////////////////////
public class B3_AddChapter2EPUB extends Fragment {
    // Inicializa variables
    private Button bt_b3_cargar_capitulo, bt_b3_cargar_epub, bt_b3_agregar_capitulo_a_epub; // Botones para cargar capítulo, cargar EPUB y agregar capítulo al archivo EPUB
    private TextView txt_chapter_show;                                          // Muestra el path del archivo seleccionado (Capítulo)
    private TextView txt_path_show;                                             // Muestra el path del archivo seleccionado (EPUB)
    private String pathChapterSelected;                                         // Path del capítulo seleccionado
    private String pathEPUBselected;                                            // Path del EPUB seleccionado

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
        View view = inflater.inflate(R.layout.b3_activity_add_chapter2epub, container, false);

        // Selecciona capítulo
        txt_chapter_show = view.findViewById(R.id.txt_chapter_selected);
        bt_b3_cargar_capitulo = view.findViewById(R.id.bt_b3_cargar_capitulo);
        bt_b3_cargar_capitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filePicker1();
            }
        });

        // Selecciona archivo EPUB a editar
        txt_path_show = view.findViewById(R.id.txt_path_selected);
        bt_b3_cargar_epub = view.findViewById(R.id.bt_b3_cargar_epub);
        bt_b3_cargar_epub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filePicker2();
            }
        });

        // Invoca el método para agregar capítulo al archivo EPUB
        bt_b3_agregar_capitulo_a_epub =  view.findViewById(R.id.bt_b3_agregar_capitulo_a_epub);
        bt_b3_agregar_capitulo_a_epub.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                try {

                    // Agrega capítulo a un archivo EPUB
                    ChapterIntoEPUB(pathChapterSelected, pathEPUBselected);
                    txt_path_show.setText(" ");
                    txt_chapter_show.setText("Capítulo agregado al EPUB");

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

    // Seleccionar archivo desde el dispositivo
    private void filePicker2() {
        new MaterialFilePicker()
                .withSupportFragment(this)
                .withHiddenFiles(true)
                .withRequestCode(2000)
                .start();
    }

    // Responde según las solicitudes en onClick para captar dirección del archivo seleccionado
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Archivo 1
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            pathChapterSelected = filePath;
            txt_chapter_show.setText(pathChapterSelected);
            displatToast("path: " + pathChapterSelected);
        }
        // Archivo 2
        if (requestCode == 2000 && resultCode == RESULT_OK) {
            String filePath2 = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            pathEPUBselected = filePath2;
            txt_path_show.setText(pathEPUBselected);
            displatToast("path: " + pathEPUBselected);
        }
    }

    // Genera el string en un pop-up en pantalla con la dirección del archivo seleccionado
    private void displatToast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    // Agrega capítulo a un archivo EPUB
    public static void ChapterIntoEPUB(String pathChapterSelected, String pathEPUBselected) throws IOException {

        // Verificar-Crear Directorio de salida por defecto
        File folder = new File(OUTPUT_DIR);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Carga Capítulo y datos para título del capítulo
        File input_0 = new File(pathChapterSelected);                   // Archivo de entrada
        String title0 = input_0.getName();                              // Título del archivo de entrada

        // Carga archivo EPUB y datos para archivo de salida
        File input_1 = new File(pathEPUBselected);                      // Archivo de entrada
        String title1 = input_1.getName();                              // Título del archivo de entrada
        String title_no_ext1 = FilenameUtils.removeExtension(title1);   // Título del archivo de entrada sin extensión
        String name = "3-" + title_no_ext1 + "_ChapterAdded.epub";      // Nombre del archivo de salida

        // Carga datos del EPUB
        EpubReader epubReader = new EpubReader();
        Book book = epubReader.readEpub(new FileInputStream(pathEPUBselected));

        // Se carga como Resource el nuevo capítulo
        Resource NewChapter = getResource(pathChapterSelected, title0);
        SpineReference NewChapter_href = new SpineReference(NewChapter);

        // Se carga la lista de referencias del Spine
        List<SpineReference> spine_References = book.getSpine().getSpineReferences();
        // Se agrega nuevo capítulo al final del Spine
        spine_References.add(NewChapter_href);

        // Se establece el nuevo spine
        Spine NewSpine = new Spine(spine_References);
        book.setSpine(NewSpine);
        book.addResource(NewChapter);

        /////////////////////////////////////

        // Crea objeto EpubWriter
        EpubWriter epubWriter = new EpubWriter();

        // Crea archivo EPUB en el dispositivo
        epubWriter.write(book, new FileOutputStream(OUTPUT_DIR + "/" + name));

        /////////////////////////////////////

    }

    // Carga un nuevo recurso
    private static InputStream getResource( String path ) throws IOException {
        InputStream stream = new FileInputStream(path);
        return stream;
    }

    // Crea un nuevo objeto Resource
    private static Resource getResource(String path, String href ) throws IOException {
        return  new Resource( getResource( path ), href );
    }

}

///////////////////////////////////////////////////////////////////////////////////////////////////