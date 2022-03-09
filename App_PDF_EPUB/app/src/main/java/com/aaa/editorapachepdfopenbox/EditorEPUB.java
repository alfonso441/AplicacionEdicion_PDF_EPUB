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

    import androidx.appcompat.app.AppCompatActivity;
    import android.os.Bundle;
    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.fragment.app.Fragment;
    import androidx.navigation.Navigation;
    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;

/****************************************************************************
 *  Menú de Herramientas EPUB.
 ***************************************************************************/

///////////////////////////////////////////////////////////////////////////////////////////////////
public class EditorEPUB extends Fragment {

    // Declaración de botones
    Button boton_1B_convert_pdf2epub, boton_2B_add_cover2epub, boton_3B_add_chapter2epub;
    Button boton_4B_convert_picture2epub, boton_5B_remove_pages_from_epub, boton_6B_epub_reader;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_editor_epub, container, false);

        // 1B-Convierte archivos PDF a EPUB
        boton_1B_convert_pdf2epub = view.findViewById(R.id.bt_1B_convert_pdf2epub);
        boton_1B_convert_pdf2epub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_1B_convert_pdf2epub);
            }
        });

        // 2B-Agrega portada al archivo EPUB
        boton_2B_add_cover2epub = view.findViewById(R.id.bt_2B_add_cover2epub);
        boton_2B_add_cover2epub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_2B_add_cover2epub);
            }
        });

        // 3B-Agrega capítulo al archivo EPUB
        boton_3B_add_chapter2epub = view.findViewById(R.id.bt_3B_add_chapter2epub);
        boton_3B_add_chapter2epub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_3B_add_chapter2epub);
            }
        });

        // 4B-Convierte imagen a EPUB
        boton_4B_convert_picture2epub = view.findViewById(R.id.bt_4B_convert_picture2epub);
        boton_4B_convert_picture2epub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_4B_convert_picture2epub);
            }
        });

        // 5B-Remueve páginas del EPUB
        boton_5B_remove_pages_from_epub = view.findViewById(R.id.bt_5B_remove_pages_from_epub);
        boton_5B_remove_pages_from_epub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_5B_remove_pages_from_epub);
            }
        });

        // 6B-Lector de archivos EPUB
        boton_6B_epub_reader = view.findViewById(R.id.bt_6B_epub_reader);
        boton_6B_epub_reader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_6B_epub_reader);
            }
        });

        return view;
    }
}