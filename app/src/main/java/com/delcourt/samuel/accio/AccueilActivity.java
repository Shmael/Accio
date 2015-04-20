
package com.delcourt.samuel.accio;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.delcourt.samuel.accio.create_new_object_activities.NewFrigoActivity;
import com.delcourt.samuel.accio.options_activities.CreditActivity;
import com.delcourt.samuel.accio.structures.Refrigerateur;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Scanner;

import static android.widget.AdapterView.OnItemClickListener;

//Cette classe gère la gestion des frigos (dans lesquels sont réparties les boîtes).

//Elle n'a pour cela besoin que des noms des frigos : pour cette raison, elle (et les classes directement associées) lit et écrit dans un
//fichier texte liste_frigos_file.txt

public class AccueilActivity extends ActionBarActivity { //Permet la gestion des réfrigérateurs

    private static ArrayList<String> listeFrigosNames = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        makeActionOverflowMenuShown();


        ImageButton fabImageButton = (ImageButton) findViewById(R.id.fab_image_button);
        fabImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageNouveau(v);
            }
        });


        readFiles(); //Lecture des fichiers et récupération des infos sur le frigo

        //Si pas de frigo, on affiche un message
        if(listeFrigosNames.size()==0){
            TextView textElement = (TextView) findViewById(R.id.message_pas_de_frigo);
            textElement.setText("Vous n'avez pour l'instant aucun réfrigérateur");
        }

        //On affiche :
        // Get the reference of listViewFrigos (pour l'affichage de la liste)
        ListView frigoList=(ListView)findViewById(R.id.listViewFrigos);
        // Create The Adapter with passing ArrayList as 3rd parameter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.accueil_listview, listeFrigosNames);
        // Set The Adapter
        frigoList.setAdapter(arrayAdapter);


        //register onClickListener to handle click events on each item
        frigoList.setOnItemClickListener(new OnItemClickListener()
        {
            // argument position gives the index of item which is clicked

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                sendMessageFrigoSelected(position);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_accueil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_credit:
                sendMessageCredit();
                return true;
            case R.id.lien_site:
                sendMessageSite();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void makeActionOverflowMenuShown() {
        //devices with hardware menu button (e.g. Samsung Note) don't show action overflow menu
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), "erreur makeActionOverflowMenuShown",Toast.LENGTH_SHORT).show();
        }
    }

    public static ArrayList<String> getListeFrigosNames(){return listeFrigosNames;}

    public void sendMessageFrigoSelected(int indexName){
        Intent intent = new Intent(this, ListeBoitesActivity.class);//Lance l'activité ListeBoitesActivity, avec le nom du frigo sélectionné en message
        String frigoName = listeFrigosNames.get(indexName);
        Refrigerateur refrigerateur = new Refrigerateur(frigoName);
        ListeBoitesActivity.setRefrigerateur(refrigerateur);
        startActivity(intent);
    }

    public void sendMessageNouveau(View view){
        Intent intent = new Intent(this,NewFrigoActivity.class);
        startActivity(intent);
    }

    public void sendMessageCredit(){
        Intent intent = new Intent(this,CreditActivity.class);
        startActivity(intent);
    }

    public void initialisationFrigoExemple() throws FileNotFoundException {

        //On crée le fichier contenant la liste des boites de Réfrigérateur essai
        OutputStreamWriter outStream = new OutputStreamWriter(openFileOutput("Réfrigérateur essaiBoxes.txt",MODE_APPEND));
        BufferedWriter bw = new BufferedWriter(outStream);
        PrintWriter out2 = new PrintWriter(bw);
        out2.println("3");//Référence vers la bdd
        out2.println("Fruits (exemple)");//Nom de la boite
        out2.println("Fruits");//Catégorie

        out2.println("4");
        out2.println("Légumes (exemple)");
        out2.println("Légumes");

        out2.close();

        //On crée le fichier contenant la liste des favoris, on y ajoute une poire
        OutputStreamWriter outStream2 = new OutputStreamWriter(openFileOutput("Réfrigérateur essaiFavoris.txt",MODE_APPEND));
        BufferedWriter bw2 = new BufferedWriter(outStream2);
        PrintWriter out3 = new PrintWriter(bw2);
        out3.println("Poire");
        out3.close();
    }

    public void readFiles(){
        InputStream instream;
        try {
            instream = openFileInput("frigos_file.txt");
            InputStreamReader inputreader = new InputStreamReader(instream);
            BufferedReader buffreader = new BufferedReader(inputreader);
            Scanner sc = new Scanner(buffreader);
            listeFrigosNames = new ArrayList<>();//réinitialise la liste
            while(sc.hasNextLine() == true){//On recrée la liste des frigos : listeFrigosNames
                String name = sc.nextLine();
                listeFrigosNames.add(name);
            }
            sc.close();
        } catch (FileNotFoundException e) {//A lieu à la première utilisation d'accio. On crée alors le frigo de référence (suite) (utile pour nous)
            try {
                OutputStreamWriter outStream = new OutputStreamWriter(openFileOutput("frigos_file.txt",MODE_APPEND));
                BufferedWriter bw = new BufferedWriter(outStream);
                PrintWriter out2 = new PrintWriter(bw);
                out2.println("Réfrigérateur essai");
                out2.close();

                listeFrigosNames.add("Réfrigérateur essai");//initialise les données locales
                initialisationFrigoExemple();//Permet la suite de l'initialisation du frigo de référence (càd l'exemple)
            } catch (FileNotFoundException e1) {
                e.printStackTrace();
            }

        }
    }

    public void sendMessageSite(){
        try{Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://accio.ml"));
        startActivity(browserIntent);}
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "Impossible de charger la page web", Toast.LENGTH_SHORT).show();
        }
    }

}