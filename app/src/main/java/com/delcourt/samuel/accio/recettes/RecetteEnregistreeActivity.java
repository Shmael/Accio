package com.delcourt.samuel.accio.recettes;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.delcourt.samuel.accio.R;

public class RecetteEnregistreeActivity extends ActionBarActivity {

    protected static String adresseWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recette_favorie);

        WebView webview = (WebView)findViewById(R.id.marmiton_web_view_favori);
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        //on prend le userAgent en desktop pour avoir les option de recherche
        String ua = "Mozilla/5.0 (X11; U; Linux i686; fr-FR; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
        webview.getSettings().setUserAgentString(ua);
        webview.loadUrl(adresseWeb);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recette_favorie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendMessageOptionsRecette(View view){
        Intent intent = new Intent(this,OptionsRecetteEnregistreeActivity.class);
        startActivity(intent);
    }
}
