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

    Douglas González Parra 2021
    douglas.gonzalezparra@ucr.ac.cr
*****************************************************************************/

package com.aaa.editorapachepdfopenbox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/****************************************************************************
 *  Pantalla principal de inicio
 *  Se escoge la acción a realizar
 *****************************************************************************/
public class Editor extends Fragment {
    // declara botones
    Button boton_mergerpdf, boton_convertir_pdf_to_img, boton_split_pdf, boton_img_to_pdf;
    Button boton_removepage_pdf, boton_addImageToPdf;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.editor, container, false);

        ////////////////////////////////////////////////////////
        // Agregado por Alfonso Castillo. Versión 2.0.
        /// Permisos para acceder a archivos del dispositivo ///
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
        }
        ////////////////////////////////////////////////////////

        // Unión de archivos PDF
        boton_mergerpdf = view.findViewById(R.id.bt_mergerpdf_editor);
        boton_mergerpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_mergepdf);
            }
        });

        // Convierte cada página del archivo PDF a imagenes
        boton_convertir_pdf_to_img = view.findViewById(R.id.bt_convertir_pdf_to_img_editor);
        boton_convertir_pdf_to_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_extraer_imagenes);
            }
        });

        // Crea un nuevo archivo PDF a partir de la seleccion de unas páginas del archivo PDF original
        boton_split_pdf = view.findViewById(R.id.bt_split_pdf_editor);
        boton_split_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_split_PDF);
            }
        });

        // Convierte imagenes a PDDF
        boton_img_to_pdf = view.findViewById(R.id.bt_img_to_pdf_editor);
        boton_img_to_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_img_to_pdf);
            }
        });

        // Convierte imagenes a PDDF
        boton_removepage_pdf = view.findViewById(R.id.bt_removepage_pdf_editor);
        boton_removepage_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_remove_pages_pdf);
            }
        });

        boton_addImageToPdf = view.findViewById(R.id.bt_addImageToPDF_pdf_editor);
        boton_addImageToPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_add_img_to_pdf);
            }
        });

        return view;
    }

    ////////////////////////////////////////////////////////
    // Agregado por Alfonso Castillo. Versión 2.0.
    /// Permisos para acceder a archivos del dispositivo ///
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1001: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "¡Acceso Permitido!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "¡Acceso Denegado!", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            }
        }

    }
    ////////////////////////////////////////////////////////

}
