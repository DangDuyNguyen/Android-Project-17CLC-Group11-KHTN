package com.example.storegacha;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {
    TextView coins_Display;
    ImageView item;
    Button spin,back;
    ArrayList<Item> list_item;
    int player_coins = 9999;

    private class BackgroundLoading extends AsyncTask<Void,Void,ArrayList<Item>>
    {

        @Override
        protected ArrayList<Item> doInBackground(Void... voids) {
            ArrayList<Item> buffer = new ArrayList<>();
            XmlParser parser = null;
            try {
                parser = new XmlParser(getResources().openRawResource(R.raw.items_list));
                buffer = parser.readFile();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException | SAXException e) {
                e.printStackTrace();
            }
            return buffer;
        }

        @Override
        protected void onPostExecute(ArrayList<Item> items) {
            list_item = items;
            Toast.makeText(MainActivity.this, list_item.toString(), Toast.LENGTH_SHORT).show();
            super.onPostExecute(items);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new BackgroundLoading().execute();
        setUpComponent();
    }

    private void setUpComponent()
    {
        final Animation anim = AnimationUtils.loadAnimation(this,R.anim.spin_anim);
        coins_Display = findViewById(R.id.coins);
        item = findViewById(R.id.item);
        spin = findViewById(R.id.SpinButton);
        spin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.startAnimation(anim);
                setUpDialog();
            }
        });
        coins_Display.setText("Coins: "+player_coins);
    }

    private Item Roll()
    {
        Random rand = new Random();
        int value = rand.nextInt(list_item.size());
        return list_item.get(value);
    }

    private void setUpDialog()
    {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        setUpDialogComponent(dialog);
        dialog.show();
    }


    private void setUpDialogComponent(final Dialog dialog)
    {
        ImageView items = dialog.findViewById(R.id.itemsGot);
        TextView textView = dialog.findViewById(R.id.itemsGotName);
        Button returns = dialog.findViewById(R.id.OKButton);
        Button SpinAgain = dialog.findViewById(R.id.SpinAgainButton);

        returns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        SpinAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpDialog();
                dialog.dismiss();
            }
        });
        Item getItem = Roll();
        int ResID = this.getResources().getIdentifier(getItem.getImages(),"drawable",getPackageName());
        items.setImageResource(ResID);
        textView.setText(getItem.getName());
    }
}
