package com.delcourt.samuel.accio.options_activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.delcourt.samuel.accio.AccueilActivity;
import com.delcourt.samuel.accio.BoxActivity;
import com.delcourt.samuel.accio.ListeBoitesActivity;
import com.delcourt.samuel.accio.R;
import com.delcourt.samuel.accio.RefrigerateurActivity;
import com.delcourt.samuel.accio.structures.Box;
import com.delcourt.samuel.accio.structures.Refrigerateur;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class BoxOptionsActivity extends ActionBarActivity {

    private Box boite = BoxActivity.boite;
    private String boiteID = null;
    private String newName = null;
    private String newNameEnco = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_box_options);

            //Récupère les informations de la boîte pour les afficher :
            TextView textElement = (TextView) findViewById(R.id.boxName_BoxOptionActivity);
            textElement.setText(boite.getName());

            TextView textElement2 = (TextView) findViewById(R.id.frigoName_BoxOptionActivity);
            textElement2.setText("(Réfrigérateur : " + RefrigerateurActivity.refrigerateur.getName() + ")");

            //Affiche l'image du type de la boîte
            //METTRE LES BONNES IMAGES !
            String type = boite.getType();
            ImageView textElement3 = (ImageView) findViewById(R.id.imgTypeBoite_boxOptionActivity);
            if (type.compareTo("Fruits")==0){ textElement3.setImageResource(R.drawable.ic_fruit);}
            else if (type.compareTo("Légumes")==0){textElement3.setImageResource(R.drawable.ic_legume);}
            else if (type.compareTo("Produits laitiers")==0){textElement3.setImageResource(R.drawable.ic_produit_laitier);}
            else if (type.compareTo("Poisson")==0){textElement3.setImageResource(R.drawable.ic_poisson);}
            else if (type.compareTo("Viande")==0){textElement3.setImageResource(R.drawable.ic_viande);}
            else if (type.compareTo("Sauces et condiments")==0){textElement3.setImageResource(R.drawable.ic_condiment);}

        } catch (Exception e){
            Intent intent = new Intent(this,AccueilActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_box_options, menu);
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

    public void sendMessageRename(View view){
        int k = 0;

        EditText editText = (EditText) findViewById(R.id.edit_text_renommer_boite);
        newName = editText.getText().toString();

        //On s'assure qu'aucun frigo du même nom n'a encore été créé
        for (int i=0;i< RefrigerateurActivity.refrigerateur.getBoxes().size();i++){
            if (newName.compareTo(RefrigerateurActivity.refrigerateur.getBoxes().get(i).getName()) == 0){
                k++;
            }
        }

        if(newName.length()==0){
            Toast toast = Toast.makeText(getApplicationContext(), "Vous n'avez pas entré de nouveau nom", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }else if (k > 0){
            Toast toast = Toast.makeText(getApplicationContext(), "Une boîte Accio possédant ce nom existe déjà dans ce réfrigérateur", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
        else{
            //on créé une boite de dialogue
            AlertDialog.Builder adb = new AlertDialog.Builder(BoxOptionsActivity.this);
            //on attribue un titre à notre boite de dialogue
            adb.setTitle("Confirmation");
            //on insère un message à notre boite de dialogue, et ici on affiche le titre de l'item cliqué
            adb.setMessage("Voulez-vous renommer la boîte ''" + boite.getName() + "'' en : ''" + newName + "'' ?");
            //on indique que l'on veut le bouton ok à notre boite de dialogue
            adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    rename(newName);
                }
            });
            //on affiche la boite de dialogue
            adb.show();
        }


    }

    public void sendMessageDelete(View view){
        //on créé une boite de dialogue
        AlertDialog.Builder adb = new AlertDialog.Builder(BoxOptionsActivity.this);
        //on attribue un titre à notre boite de dialogue
        adb.setTitle("Confirmation");
        //on insère un message à notre boite de dialogue, et ici on affiche le titre de l'item cliqué
        adb.setMessage("Voulez-vous vraiment supprimer la boite " + boite.getName()+" ? \nLes informations correspondantes seront perdues");
        //on indique que l'on veut le bouton ok à notre boite de dialogue
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                delete();
            }
        });
        //on affiche la boite de dialogue
        adb.show();
    }

    public void rename(String newName){
        String nameFrigo = RefrigerateurActivity.refrigerateur.getName();

        //On change le nom de la boîte dans la liste dynamique :
        String nameBoite = boite.getName();
        boiteID = boite.getReferenceBdd();
        try {
            newNameEnco = URLEncoder.encode(newName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        new RenameBoite().execute();


        for(int j =0;j<RefrigerateurActivity.refrigerateur.getBoxes().size();j++){
            if(RefrigerateurActivity.refrigerateur.getBoxes().get(j).getName() == nameBoite){
                RefrigerateurActivity.refrigerateur.getBoxes().get(j).setName(newName);
            }
        }
        //On adapte le fichier texte
        try {
            OutputStreamWriter outStream = new OutputStreamWriter(openFileOutput(nameFrigo + "Boxes.txt",MODE_PRIVATE));
            BufferedWriter bw = new BufferedWriter(outStream);
            PrintWriter out2 = new PrintWriter(bw);
            for(int i=0;i<RefrigerateurActivity.refrigerateur.getBoxes().size();i++){
                out2.println(RefrigerateurActivity.refrigerateur.getBoxes().get(i).getReferenceBdd());
                out2.println(RefrigerateurActivity.refrigerateur.getBoxes().get(i).getName());
                out2.println(RefrigerateurActivity.refrigerateur.getBoxes().get(i).getType());
            }
            out2.close();

        } catch (FileNotFoundException e1) {
            Toast.makeText(getApplicationContext(), "problème réécriture liste boîtes", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(this,BoxActivity.class);
        startActivity(intent);
    }


    class RenameBoite extends AsyncTask<String, Void, String> {


        protected String doInBackground(String... urls) {

            String result = "";
            InputStream is = null;

            // Envoi de la requÃªte avec HTTPGet
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet("http://perceval.tk/pact/renameboite.php?newNomBoite=" +newNameEnco+ "&boiteID=" + boiteID);
                HttpResponse response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
            }

            //Conversion de la rÃ©ponse en chaine
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();

                result = sb.toString();
                Toast.makeText(getApplicationContext(), "conversion en chaÃ®ne : ok",
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e("log_tag", "Error converting result " + e.toString());
            }

            //Parsing des donnÃ©es JSON
            try {
                Log.i("tagconvertstr", "[" + result + "]"); // permet de voir ce que retoune le script.
                //JSONArray jArray = new JSONArray(result);
                JSONObject object = new JSONObject(result);
                //Log.i("lol", "COUCOU: "+ object.toString());
                JSONArray array = object.getJSONArray("testData");

                for (int i = 0; i < array.length(); i++) {
                    JSONArray json_data = array.getJSONArray(i);
                    //Met les donnÃ©es ds la liste Ã  afficher
                    result += "\n\t" + array.getString(i);

                }
            } catch (JSONException e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
            }


            return result;
        }

    }



    public void delete(){
        String nameFrigo = RefrigerateurActivity.refrigerateur.getName();
        String nameBoite = boite.getName();
        boiteID = boite.getReferenceBdd();

        new DeleteBoite().execute();

        //On supprime la boîte dans la liste dynamique :
        for(int j =0;j<RefrigerateurActivity.refrigerateur.getBoxes().size();j++){
            if(RefrigerateurActivity.refrigerateur.getBoxes().get(j).getName() == nameBoite){
                RefrigerateurActivity.refrigerateur.getBoxes().remove(j);
            }
        }
        //On adapte le fichier texte
        try {
            OutputStreamWriter outStream = new OutputStreamWriter(openFileOutput(nameFrigo + "Boxes.txt",MODE_PRIVATE));
            BufferedWriter bw = new BufferedWriter(outStream);
            PrintWriter out2 = new PrintWriter(bw);
            for(int i=0;i<RefrigerateurActivity.refrigerateur.getBoxes().size();i++){
                out2.println(RefrigerateurActivity.refrigerateur.getBoxes().get(i).getReferenceBdd());
                out2.println(RefrigerateurActivity.refrigerateur.getBoxes().get(i).getName());
                out2.println(RefrigerateurActivity.refrigerateur.getBoxes().get(i).getType());
            }
            out2.close();

        } catch (FileNotFoundException e1) {
            Toast.makeText(getApplicationContext(), "problème réécriture liste boîtes", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(this,ListeBoitesActivity.class);
        startActivity(intent);
    }

    class DeleteBoite extends AsyncTask<String, Void, String> {


        protected String doInBackground(String... urls) {

            String result = "";
            InputStream is = null;

            // Envoi de la requÃªte avec HTTPGet
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet("http://perceval.tk/pact/deleteboite.php?boiteID=" + boiteID);
                HttpResponse response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
            }

            //Conversion de la rÃ©ponse en chaine
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();

                result = sb.toString();
                Toast.makeText(getApplicationContext(), "conversion en chaÃ®ne : ok",
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e("log_tag", "Error converting result " + e.toString());
            }

            //Parsing des donnÃ©es JSON
            try {
                Log.i("tagconvertstr", "[" + result + "]"); // permet de voir ce que retoune le script.
                //JSONArray jArray = new JSONArray(result);
                JSONObject object = new JSONObject(result);
                //Log.i("lol", "COUCOU: "+ object.toString());
                JSONArray array = object.getJSONArray("testData");

                for (int i = 0; i < array.length(); i++) {
                    JSONArray json_data = array.getJSONArray(i);
                    //Met les donnÃ©es ds la liste Ã  afficher
                    result += "\n\t" + array.getString(i);

                }
            } catch (JSONException e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
            }


            return result;
        }

    }


}
