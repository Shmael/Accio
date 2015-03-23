package com.delcourt.samuel.accio;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.delcourt.samuel.accio.help_activities.AideAliment;
import com.delcourt.samuel.accio.options_activities.BoxOptionsActivity;
import com.delcourt.samuel.accio.structures.Aliment;
import com.delcourt.samuel.accio.structures.Box;


public class AlimentActivity extends ActionBarActivity {

    public static String boiteName;
    public static Aliment aliment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aliment);
        afficheEnTete();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_aliment, menu);
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

    public void afficheEnTete(){
        TextView textElement = (TextView) findViewById(R.id.boxName_AlimentActivity);
        textElement.setText("Boîte : "+boiteName);

        TextView textElement2 = (TextView) findViewById(R.id.frigoName_BoxActivity);
        textElement2.setText("(Réfrigérateur : " + RefrigerateurActivity.refrigerateur.getName() + ")");

        ImageView image = (ImageView) findViewById(R.id.imgAlimentFavori);
        if(aliment.isAlimentFavori()==true){image.setImageResource(R.drawable.fav);
        }else{image.setImageResource(R.drawable.favn);}

        TextView textElement3 = (TextView) findViewById(R.id.nameAliment);
        textElement3.setText(aliment.getAlimentName());
    }

    public void sendMessageHelp(View view){
        Intent intent = new Intent(this,AideAliment.class);
        startActivity(intent);
    }

    public void sendMessageFavori(View view){
        AlertDialog.Builder adb = new AlertDialog.Builder(AlimentActivity.this);
        //on attribue un titre à notre boite de dialogue
        adb.setTitle("Favori");

        if(aliment.isAlimentFavori()==false){
            adb.setMessage("Voulez-vous ajouter l'aliment : "+aliment.getAlimentName()+" à la liste des favoris ?");
            adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    declareFavori();
                }
            });
            adb.show();
        } else{
            adb.setMessage("Voulez-vous retirer l'aliment : "+aliment.getAlimentName()+" de la liste des favoris ?");
            adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    declareNonFavori();
                }
            });
            adb.show();
        }
    }

    public void declareFavori(){
        ImageView image = (ImageView) findViewById(R.id.imgAlimentFavori);
        aliment.setFavori(true);
        image.setImageResource(R.drawable.fav);
        Toast.makeText(getApplicationContext(), "Connecter à la bdd - ajouté aux favoris",Toast.LENGTH_SHORT).show();
    }

    public void declareNonFavori(){
        ImageView image = (ImageView) findViewById(R.id.imgAlimentFavori);
        aliment.setFavori(false);
        image.setImageResource(R.drawable.favn);
        Toast.makeText(getApplicationContext(), "Connecter à la bdd - retiré des favoris",Toast.LENGTH_SHORT).show();
    }

}
