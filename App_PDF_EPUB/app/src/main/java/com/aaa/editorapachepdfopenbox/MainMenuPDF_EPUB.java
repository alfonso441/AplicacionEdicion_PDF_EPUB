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

    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.fragment.app.Fragment;
    import androidx.navigation.Navigation;
    import android.content.Intent;

/****************************************************************************
 *  Pantalla principal de inicio (Menú Principal)
 *  Se escoge la herramienta a utilizar: PDF o EPUB
 ***************************************************************************/

///////////////////////////////////////////////////////////////////////////////////////////////////
public class MainMenuPDF_EPUB extends Fragment{

    // Declaración de botones
    Button boton_herramientas_pdf, boton_herramientas_epub;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_menu_pdf_epub, container, false);

        // Salto a pantalla con herramientas para formato PDF
        boton_herramientas_pdf = view.findViewById(R.id.bt_herramienta_PDF);
        boton_herramientas_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_editor_pdf);
            }
        });

        // Salto a pantalla con herramientas para formato EPUB
        boton_herramientas_epub = view.findViewById(R.id.bt_herramienta_EPUB);
        boton_herramientas_epub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_editor_epub);
            }
        });

        return view;

    }
}