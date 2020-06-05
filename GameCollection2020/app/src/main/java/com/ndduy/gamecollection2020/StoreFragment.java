package com.ndduy.gamecollection2020;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class StoreFragment extends Fragment {

    final int CHARACTER_ITEM = 8;
    final int BACKGROUND_ITEM = 9;
    final int ROLL_BACKGROUND_ITEM = 10;
    final int ROLL_CHARACTER_ITEM = 11;
    final int INSUFFICIENT_COIN = 12;
    final int COLLECTION_FULL = 13;

    TextView coin;
    ImageView giftbox1, giftbox2;
    Button rollChar,rollBg;
    ArrayList<Item> bg_item, char_item;
    ArrayList<String> obtained_item = new ArrayList<>();

    public StoreFragment() {
        // Required empty public constructor
    }

    private class GetBackroundResource extends AsyncTask<Void,Void,ArrayList<Item>>
    {
        @Override
        protected ArrayList<Item> doInBackground(Void... voids) {
            ArrayList<Item> buffer = new ArrayList<>();
            XmlParser parser = null;
            try {
                parser = new XmlParser(getResources().openRawResource(R.raw.bg_list));
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
            bg_item = items;
            super.onPostExecute(items);
        }
    }

    private class GetCharacterResource extends AsyncTask<Void,Void,ArrayList<Item>>
    {
        @Override
        protected ArrayList<Item> doInBackground(Void... voids) {
            ArrayList<Item> buffer = new ArrayList<>();
            XmlParser parser = null;
            try {
                parser = new XmlParser(getResources().openRawResource(R.raw.char_list));
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
            char_item = items;
            super.onPostExecute(items);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_store, container, false);

        new GetBackroundResource().execute();
        new GetCharacterResource().execute();

        setUpComponent(rootView);

        return rootView;
    }

    private void setUpComponent(View rootView)
    {
        final Animation anim = AnimationUtils.loadAnimation(getContext(),R.anim.spin_anim);
        coin = rootView.findViewById(R.id.store_coin);
        coin.setInputType(InputType.TYPE_NULL);
        giftbox1 = rootView.findViewById(R.id.giftbox1);
        giftbox2 = rootView.findViewById(R.id.giftbox2);
        rollChar = rootView.findViewById(R.id.characterRollbtn);
        rollBg = rootView.findViewById(R.id.bgRollbtn);

        receiveDatafromLobby();

        rollChar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> temp = new ArrayList<>();
                for (int i = 0; i < char_item.size(); i++)
                    temp.add(char_item.get(i).getName());

                if(Integer.parseInt(coin.getText().toString()) < 1000){
                    setupPopupWindow(v, INSUFFICIENT_COIN);
                }
                else if (obtained_item.containsAll(temp)){
                    setupPopupWindow(v, COLLECTION_FULL);
                }
                else{
                    giftbox1.startAnimation(anim);
                    setUpDialog(ROLL_CHARACTER_ITEM);

                    Bundle success_bundle = new Bundle();
                    success_bundle.putInt("cost_coin", 1000);
                    success_bundle.putString("purchased_item", obtained_item.get(obtained_item.size() - 1));
                    success_bundle.putInt("item_type", CHARACTER_ITEM);
                    getParentFragmentManager().setFragmentResult("purchase_success", success_bundle);
                }
            }
        });

        rollBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> temp = new ArrayList<>();
                for (int i = 0; i < bg_item.size(); i++)
                    temp.add(bg_item.get(i).getName());

                if(Integer.parseInt(coin.getText().toString()) < 1500){
                    setupPopupWindow(v, INSUFFICIENT_COIN);
                }
                else if (obtained_item.containsAll(temp)){
                    setupPopupWindow(v, COLLECTION_FULL);
                }
                else{
                    giftbox2.startAnimation(anim);
                    setUpDialog(ROLL_BACKGROUND_ITEM);

                    Bundle success_bundle = new Bundle();
                    success_bundle.putInt("cost_coin", 1500);
                    success_bundle.putString("purchased_item", obtained_item.get(obtained_item.size() - 1));
                    success_bundle.putInt("item_type", BACKGROUND_ITEM);
                    getParentFragmentManager().setFragmentResult("purchase_success", success_bundle);
                }
            }
        });
    }

    private Item Roll(ArrayList<Item> item_list)
    {
        Random rand = new Random();
        int value = rand.nextInt(item_list.size());
        while (obtained_item.contains(item_list.get(value).getName()))
        {
            value = rand.nextInt(item_list.size());
        }
        return item_list.get(value);
    }

    private void setupPopupWindow(View view, int signal){
        final LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.not_enough_coin, null);
        final PopupWindow window = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        window.showAtLocation(view, Gravity.CENTER, 0,0);

        TextView error_msg = (TextView) popupView.findViewById(R.id.error_title);
        Button confirm = (Button) popupView.findViewById(R.id.error_close_btn);

        if (signal == INSUFFICIENT_COIN)
            error_msg.setText("Insufficient coin");
        else if (signal == COLLECTION_FULL)
            error_msg.setText("You have obtained all item");

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
    }

    private void setUpDialog(int signal)
    {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.gacha_result_dialog);
        setUpDialogComponent(dialog, signal);
        dialog.show();
    }

    private void receiveDatafromLobby(){
        getParentFragmentManager().setFragmentResultListener("lobby_to_store", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                int available_coin = result.getInt("coin", 0);
                coin.setText(Integer.toString(available_coin));
                obtained_item.addAll(result.getStringArrayList("item_list"));
            }
        });

        getParentFragmentManager().setFragmentResultListener("item_list", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
            }
        });
    }

    private void setUpDialogComponent(final Dialog dialog, final int signal)
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
                final Animation anim = AnimationUtils.loadAnimation(getContext(),R.anim.spin_anim);

                if (signal == ROLL_CHARACTER_ITEM) {
                    ArrayList<String> temp = new ArrayList<>();
                    for (int i = 0; i < char_item.size(); i++)
                        temp.add(char_item.get(i).getName());
                    if (Integer.parseInt(coin.getText().toString()) < 0) {
                        setupPopupWindow(v, INSUFFICIENT_COIN);
                    }
                    if (obtained_item.containsAll(temp)) {
                        setupPopupWindow(v, COLLECTION_FULL);
                    }
                    else {
                        giftbox1.startAnimation(anim);
                        setUpDialog(ROLL_CHARACTER_ITEM);
                        Bundle success_bundle = new Bundle();
                        success_bundle.putInt("cost_coin", 0);
                        success_bundle.putString("purchased_item", obtained_item.get(obtained_item.size() - 1));
                        success_bundle.putInt("item_type", CHARACTER_ITEM);
                        getParentFragmentManager().setFragmentResult("purchase_success", success_bundle);
                    }
                }
                else if (signal == ROLL_BACKGROUND_ITEM) {
                    ArrayList<String> temp = new ArrayList<>();
                    for (int i = 0; i < bg_item.size(); i++)
                        temp.add(bg_item.get(i).getName());

                    if (Integer.parseInt(coin.getText().toString()) < 0) {
                        setupPopupWindow(v, INSUFFICIENT_COIN);
                    }
                    else if (obtained_item.containsAll(temp)) {
                        setupPopupWindow(v, COLLECTION_FULL);
                    }
                    else {
                        giftbox2.startAnimation(anim);
                        setUpDialog(ROLL_BACKGROUND_ITEM);
                        Bundle success_bundle = new Bundle();
                        success_bundle.putInt("cost_coin", 0);
                        success_bundle.putString("purchased_item", obtained_item.get(obtained_item.size() - 1));
                        success_bundle.putInt("item_type", BACKGROUND_ITEM);
                        getParentFragmentManager().setFragmentResult("purchase_success", success_bundle);
                    }
                }
                dialog.dismiss();
            }
        });

        if (signal == ROLL_BACKGROUND_ITEM) {
            Item getItem = Roll(bg_item);
            int ResID = getActivity().getResources().getIdentifier(getItem.getImages(),"drawable",getActivity().getPackageName());
            items.setImageResource(ResID);
            textView.setText(getItem.getName());
            if (!obtained_item.contains(getItem.getName()))
                obtained_item.add(getItem.getName());
        }
        else if (signal == ROLL_CHARACTER_ITEM) {
            Item getItem = Roll(char_item);
            int ResID = getActivity().getResources().getIdentifier(getItem.getImages(),"drawable",getActivity().getPackageName());
            items.setImageResource(ResID);
            textView.setText(getItem.getName());
            if (!obtained_item.contains(getItem.getName()))
                obtained_item.add(getItem.getName());
        }

    }
}
