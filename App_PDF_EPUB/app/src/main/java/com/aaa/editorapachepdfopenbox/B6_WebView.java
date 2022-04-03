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
    import android.webkit.WebView;

public class B6_WebView extends AppCompatActivity {

    private WebView WebView_obj;
    private String pathEPUBselected = B6_EPUBReader.pathEPUBselected;
    private String dataB6 = B6_EPUBReader.dataB6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b6_activity_web_view);

        WebView_obj = (WebView) findViewById(R.id.WebView);
        WebView_obj.loadDataWithBaseURL(pathEPUBselected, dataB6, "text/html", "utf-8", null);
    }

}

///////////////////////////////////////////////////////////////////////////////////////////////////