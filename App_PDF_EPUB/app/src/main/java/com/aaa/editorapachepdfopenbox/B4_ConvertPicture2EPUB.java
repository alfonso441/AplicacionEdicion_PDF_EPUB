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
        import com.tom_roush.pdfbox.util.PDFBoxResourceLoader;

        import org.apache.commons.io.FilenameUtils;

        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.FileInputStream;
        import java.io.InputStream;
        import java.io.IOException;

        // Epublib
        import nl.siegmann.epublib.domain.Author;
        import nl.siegmann.epublib.domain.Book;
        import nl.siegmann.epublib.domain.Metadata;
        import nl.siegmann.epublib.domain.Resource;
        import nl.siegmann.epublib.domain.Resources;
        import nl.siegmann.epublib.domain.Spine;
        import nl.siegmann.epublib.domain.SpineReference;
        import nl.siegmann.epublib.domain.TOCReference;
        import nl.siegmann.epublib.epub.EpubWriter;
        import nl.siegmann.epublib.epub.EpubReader;

/****************************************************************************
 *  B4 - Convierte imagen a EPUB.
 ***************************************************************************/

///////////////////////////////////////////////////////////////////////////////////////////////////
public class B4_ConvertPicture2EPUB extends Fragment {
    // Inicializa variables
    private Button bt_b4_cargar_imagen, bt_b4_convertir_a_epub;     // Botones para seleccionar imagen y convertir a archivo EPUB
    private TextView txt_path_show;                                 // Muestra el path del archivo seleccionado
    private String pathPictureSelected;                             // Path de la imagen seleccionada

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
        View view = inflater.inflate(R.layout.b4_activity_convert_picture2epub, container, false);

        // Selecciona imagen
        txt_path_show = view.findViewById(R.id.txt_path_selected);
        bt_b4_cargar_imagen = view.findViewById(R.id.bt_b4_cargar_imagen);
        bt_b4_cargar_imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filePicker1();
            }
        });

        // Invoca el método para convertir imagen a EPUB
        bt_b4_convertir_a_epub =  view.findViewById(R.id.bt_b4_convertir_a_epub);
        bt_b4_convertir_a_epub.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                try {

                    // Inicializa herramienta intermedia para Imagen a PDF
                    PDFBoxResourceLoader.init(getActivity());
                    // Convierte imagen a EPUB
                    Picture2EPUB(pathPictureSelected);
                    txt_path_show.setText("Imagen convertida a EPUB");

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
            pathPictureSelected = filePath;
            txt_path_show.setText(pathPictureSelected);
            displatToast("path: " + pathPictureSelected);
        }
    }

    // Genera el string en un pop-up en pantalla con la dirección del archivo seleccionado
    private void displatToast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    // Convierte imagen a EPUB
    public static void Picture2EPUB(String pathPictureSelected)throws IOException {

        // Verificar-Crear Directorio de salida por defecto
        File folder = new File(OUTPUT_DIR);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Nombre de archivo de salida
        File image_0 = new File(pathPictureSelected);               // Crea objeto File para extraer nombre del archivo original
        String title = image_0.getName();                           // Título del archivo de entrada
        String title_no_ext = FilenameUtils.removeExtension(title); // Título del archivo de entrada sin extensión

        // Crear nuevo objeto Book
        Book book = new Book();

        // Set cover image
        Resource NewPicture = getResource(pathPictureSelected, title);
        book.setCoverImage(NewPicture);

        // Crea un HTML que referencia la imagen
        String pathHtml = Picture2HTML(OUTPUT_DIR, title, title_no_ext);
        Resource NewHtml = getResource(pathHtml, title_no_ext+".html");
        book.addResource(NewHtml);

        // Primera sección TOC, Resources, Spine
        book.addSection(title, NewHtml);

        /////////////////////////////////////////////////

        // Crea objeto EpubWriter
        EpubWriter epubWriter = new EpubWriter();

        // Crea archivo EPUB en el dispositivo
        String name_output = "4-" + title_no_ext + "_Picture2EPUB";
        epubWriter.write(book, new FileOutputStream(OUTPUT_DIR + "/" + name_output + ".epub"));

        /////////////////////////////////////////////////

        // Borrar HTML externo
        File Html_externo = new File(pathHtml);
        Html_externo.delete();

        /////////////////////////////////////////////////

    }

    // Crea un HTML enlazando una imagen
    private static String Picture2HTML(String Path, String PictureNameWithExtension, String PictureNameWithOUTExtension) {

        String pathHtml = Path + "/" + PictureNameWithOUTExtension + ".html";

        try {
            FileOutputStream HTML = new FileOutputStream(pathHtml, true);
            String datosHtml = "<html> \n <head> \n <title>" + PictureNameWithOUTExtension + "</title> \n </head> \n <body> \n <img src=\"" + PictureNameWithExtension + "\"/> \n </body> \n </html>";
            char caracteres[] = datosHtml.toCharArray();
            for (int i=0; i<datosHtml.length(); i++) {
                HTML.write(caracteres[i]);
            }
            HTML.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // String con dirección del HTML creado
        return pathHtml;

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