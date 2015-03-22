package com.delcourt.samuel.accio;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.delcourt.samuel.accio.interaction.BDD;
import com.delcourt.samuel.accio.structures.Aliment;
import com.delcourt.samuel.accio.structures.Box;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class FavoriteActivity extends ActionBarActivity {

    private ArrayList<Aliment> listeFavoris;

    static ArrayList<String> listeAlimentsAffichage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoris);

        chargeListeFavoris();

        //new BDD2().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favoris, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_search:
                openSearch();
                return true;
            case R.id.action_settings:
                // on mettra la mÃ©thode openSettings() quand elle sera cree
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openSearch(){
        Uri webpage = Uri.parse("http://www.google.fr/");
        Intent help = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(help);
    }

    public void chargeListeFavoris(){
        for(int i=0; i<RefrigerateurActivity.refrigerateur.getBoxes().size();i++){//On charge toutes les boîtes pas encore chargées
            if(RefrigerateurActivity.refrigerateur.getBoxes().get(i).getConnectedBdd()==false){
                //BDDFavorite.box=RefrigerateurActivity.refrigerateur.getBoxes().get(i);
                //new BDDFavorite().execute();
            }
            else{
                for(int j=0;j<RefrigerateurActivity.refrigerateur.getBoxes().get(i).getListeAliments().size();i++){
                    Aliment aliment = RefrigerateurActivity.refrigerateur.getBoxes().get(i).getListeAliments().get(j);
                    if(aliment.isAlimentFavori()==true){
                        listeFavoris.add(aliment);
                    }
                }
            }
        }
    }

    /*static class BDDFavorite extends AsyncTask<String, Void, String> {

        private static Box box;
        static ArrayList<String> listeNomAliment;


        protected String doInBackground(String... urls) {

            String result = "";

            InputStream is = null;

            // Envoi de la requÃªte avec HTTPGet
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet("http://137.194.8.216/pact/alimrecup.php?boiteid="+box.getReferenceBdd());
                //httpget.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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
            } catch (Exception e) {
                Log.e("log_tag", "Error converting result " + e.toString());
            }

            //Parsing des donnÃ©es JSON
            try {
                Log.i("tagconvertstr", "[" + result + "]"); // permet de voir ce que retoune le script.
                //JSONArray jArray = new JSONArray(result);
                JSONObject object = new JSONObject(result);
                JSONArray array = object.getJSONArray("testData");

                for (int i = 0; i < array.length(); i++) {
                    JSONArray json_data = array.getJSONArray(i);

                    //Met les donnÃ©es ds la liste Ã  afficher
                    // Ici pas besoin d'afficher les données

                    result += "\n\t" + array.getString(i);
                    listeNomAliment.add(json_data.getString(1));


                }
            } catch (JSONException e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
            return result;
        }


        //This Method is called when Network-Request finished

        protected void onPostExecute(String resultat) {


            int nbAliment = listeNomAliment.size();
            for(int k =0; k < nbAliment; k++){

                String nom = listeNomAliment.get(k);
                String marque = null;
                boolean favori = false;
                ArrayList<String> historique = new ArrayList<>();
                //marque = listeMarqueAliment.get(k);

                Aliment aliment = new Aliment(nom,marque, favori, historique,box.getName());
                box.getListeAliments().add(aliment);
            }

            //Affichage des aliments

            int sizeListAliments = box.getListeAliments().size();
            //Toast.makeText(getApplicationContext(), "Nombre d'aliments dans listeNomAliment : "+listeNomAliment.size(),Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(), "Nombre d'aliments à afficher : "+sizeListAliments,Toast.LENGTH_SHORT).show();


            if(sizeListAliments==0){
                TextView textElement = (TextView) findViewById(R.id.message_BoxActivity);
                textElement.setText("Il n'y a aucun aliment dans cette boîte pour l'instant");

                TextView textElement2 = (TextView) findViewById(R.id.resultat2);
                textElement2.setText(" ");

                //PARTIE TEMPORAIRE : on affiche un aliment d'exemple
                ArrayList<String> historique = new ArrayList<>();
                Aliment aliment = new Aliment("Aliment exemple temporaire","marque", true, historique,boite.getName());
                boite.getListeAliments().add(aliment);
                final ListView listViewAliments=(ListView)findViewById(R.id.liste_aliments);
                //Création de la ArrayList qui nous permettra de remplir la listView
                ArrayList<HashMap<String, String>> listItem = new ArrayList<>();

                HashMap<String, String> map;
                map = new HashMap<String, String>();
                map.put("aliment", boite.getListeAliments().get(0).getAlimentName());
                map.put("img", String.valueOf(R.drawable.ic_launcher));
                SimpleAdapter mSchedule = new SimpleAdapter (getApplicationContext(), listItem, R.layout.affichage_aliments,
                        new String[] {"aliment","img"}, new int[] {R.id.nom_aliment_affiche,R.id.imgAlim});
                listItem.add(map);
                listViewAliments.setAdapter(mSchedule);
                listViewAliments.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    // argument position gives the index of item which is clicked

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                        int indexBox = position;
                        sendMessageAlimentSelected(view, indexBox);
                    }
                });

                //FIN PARTIE TEMPORAIRE
            }
            else{
                boite.setConnectedBdd(true);//On indique que la connection a réussi, la prochaine fois on ne se connectera donc pas à la bdd

                TextView textElement = (TextView) findViewById(R.id.resultat2);
                textElement.setText(" ");

                // Get the reference of listViewFrigos (pour l'affichage de la liste)
                final ListView listViewAliments=(ListView)findViewById(R.id.liste_aliments);

                //Création de la ArrayList qui nous permettra de remplir la listView
                ArrayList<HashMap<String, String>> listItem = new ArrayList<>();

                HashMap<String, String> map;

                for (int i =0;i<sizeListAliments;i++){
                    //on insère la référence aux éléments à afficher
                    map = new HashMap<String, String>();
                    map.put("aliment", boite.getListeAliments().get(i).getAlimentName());
                    if(boite.getListeAliments().get(i).isAlimentFavori()==true){
                        map.put("img", String.valueOf(R.drawable.ic_launcher));
                    } else {map.put("img", String.valueOf(R.drawable.ic_launcher));}
                    //enfin on ajoute cette hashMap dans la arrayList
                    listItem.add(map);
                }

                //Création d'un SimpleAdapter qui se chargera de mettre les items présents dans notre list (listItem) dans la vue affichageitem
                SimpleAdapter mSchedule = new SimpleAdapter (getApplicationContext(), listItem, R.layout.affichage_aliments,
                        new String[] {"aliment","img"}, new int[] {R.id.nom_aliment_affiche,R.id.imgAlim});

                //On attribue à notre listView l'adapter que l'on vient de créer
                listViewAliments.setAdapter(mSchedule);


                //register onClickListener to handle click events on each item
                listViewAliments.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    // argument position gives the index of item which is clicked

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                        int indexBox = position;
                        sendMessageAlimentSelected(view, indexBox);
                    }
                });
            }

        }
    }*/

}