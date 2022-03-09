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

        import android.Manifest;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.net.Uri;
        import android.os.Build;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.activity.result.ActivityResultCallback;
        import androidx.activity.result.ActivityResultLauncher;
        import androidx.activity.result.contract.ActivityResultContracts;
        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.annotation.RequiresApi;
        import androidx.core.app.ActivityCompat;
        import androidx.core.content.ContextCompat;
        import androidx.fragment.app.Fragment;

        import com.nbsp.materialfilepicker.MaterialFilePicker;
        import com.nbsp.materialfilepicker.ui.FilePickerActivity;
        import com.tom_roush.pdfbox.cos.COSName;
        import com.tom_roush.pdfbox.cos.COSStream;
        import com.tom_roush.pdfbox.pdmodel.PDDocument;
        import com.tom_roush.pdfbox.pdmodel.PDPage;
        import com.tom_roush.pdfbox.pdmodel.PDPageTree;
        import com.tom_roush.pdfbox.pdmodel.PDResources;
        import com.tom_roush.pdfbox.pdmodel.graphics.PDXObject;
        import com.tom_roush.pdfbox.pdmodel.graphics.image.PDImageXObject;
        import com.tom_roush.pdfbox.rendering.PDFRenderer;
        import com.tom_roush.pdfbox.util.PDFBoxResourceLoader;

        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.FileInputStream;
        import java.io.InputStream;
        import java.io.IOException;

        // Epublib
        import nl.siegmann.epublib.domain.Author;
        import nl.siegmann.epublib.domain.Book;
        import nl.siegmann.epublib.domain.Metadata;
        import nl.siegmann.epublib.domain.Resource;
        import nl.siegmann.epublib.domain.TOCReference;
        import nl.siegmann.epublib.epub.EpubWriter;
        import nl.siegmann.epublib.epub.EpubReader;

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
    private static final String OUTPUT_DIR = "/storage/emulated/0/Documents";

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
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            // Path PDF 1
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
    public static void PDF2EPUB(String pathPDFselected)throws IOException {
        PdfConverter.convert(new File(pathPDFselected)).intoEpub("PDF_Converted_2_EPUB", new File("PDF_Converted_2_EPUB.epub"));
    }

}

///////////////////////////////////////////////////////////////////////////////////////////////////