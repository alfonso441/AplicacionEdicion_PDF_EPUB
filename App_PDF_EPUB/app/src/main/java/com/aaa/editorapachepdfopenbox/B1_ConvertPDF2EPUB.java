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

    import java.io.File;
    import java.io.FileOutputStream;
    import java.io.FileInputStream;
    import java.io.InputStream;
    import java.io.IOException;

    import org.apache.commons.io.FilenameUtils;
    import org.apache.commons.io.IOUtils;

    // Pdf-Converter
    import pdf.converter.PdfConverter;

/****************************************************************************
 *  B1 - Convierte archivos PDF a EPUB.
 ***************************************************************************/

///////////////////////////////////////////////////////////////////////////////////////////////////
public class B1_ConvertPDF2EPUB extends Fragment {
    // Inicializa variables
    private Button bt_b1_cargar_pdf, bt_b1_convertir_a_epub;    // Botones para seleccionar archivo PDF y convertir a archivo EPUB
    private TextView txt_path_show;                             // Muestra el path del archivo seleccionado
    private String pathPDFselected;                             // Path del archivo PDF seleccionado

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
        View view = inflater.inflate(R.layout.b1_activity_convert_pdf2epub, container, false);

        // Selecciona archivo PDF
        txt_path_show = view.findViewById(R.id.txt_path_selected);
        bt_b1_cargar_pdf = view.findViewById(R.id.bt_b1_cargar_pdf);
        bt_b1_cargar_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filePicker1();
            }
        });

        // Invoca el método para convertir el archivo PDF a EPUB
        bt_b1_convertir_a_epub =  view.findViewById(R.id.bt_b1_convertir_a_epub);
        bt_b1_convertir_a_epub.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                try {

                    // Convierte archivos PDF a EPUB
                    PDF2EPUB(pathPDFselected);
                    txt_path_show.setText("PDF convertido a EPUB");

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
            pathPDFselected = filePath;
            txt_path_show.setText(pathPDFselected);
            displatToast("path: " + pathPDFselected);
        }
    }

    // Genera el string en un pop-up en pantalla con la dirección del archivo seleccionado
    private void displatToast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    // Convierte archivos PDF a EPUB
    public void PDF2EPUB(String pathPDFselected)throws IOException {

        // Verificar-Crear Directorio de salida por defecto
        File folder = new File(OUTPUT_DIR);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Carga del archivo como stream
        InputStream stream = new FileInputStream(pathPDFselected);

        // Título de archivo de salida
        File input_0 = new File(pathPDFselected);                   // Archivo de entrada
        String title = input_0.getName();                           // Título del archivo de entrada
        String title_no_ext = FilenameUtils.removeExtension(title); // Título del archivo de entrada sin extensión
        String name = "1-" + title_no_ext + "_PDF2EPUB";            // Nombre del archivo de salida

        // Archivos auxiliares
        File input = File.createTempFile(title_no_ext, ".pdf", folder);
        File output = new File(OUTPUT_DIR + "/" + name + ".epub");
        IOUtils.copy(stream, new FileOutputStream(input));

        // Convierte PDF a EPUB
        PdfConverter.convert(input).intoEpub(title_no_ext, output);
        output.createNewFile(); // Crea el archivo de salida en el dispositivo
        input.delete();         // Borra el archivo input temporal

    }

}

///////////////////////////////////////////////////////////////////////////////////////////////////