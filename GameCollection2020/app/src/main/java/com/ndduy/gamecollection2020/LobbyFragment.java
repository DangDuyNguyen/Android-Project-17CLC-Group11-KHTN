package com.ndduy.gamecollection2020;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.internal.$Gson$Preconditions;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import io.opencensus.tags.Tag;

import static android.app.Activity.RESULT_OK;

public class LobbyFragment extends Fragment {

    private static final String TAG = "MyActivity";
    final int CHARACTER_ITEM = 8;
    final int BACKGROUND_ITEM = 9;
    final int LOGIN_REQUEST = 98;

    //current user state
    User currentUser = new User();
    ArrayList<String> obtained_item;
    ArrayList<Item> store_char_list, store_bg_list;

    //game UI components
    private TextView hungry_text, flatter_text, mood_text, sleepy_text;
    private Button hungriness, flattering, mood, sleepiness;
    private EditText name;
    private EditText coin;
    private Button setting_btn;
    private Button inventory_btn;
    private ImageView character_img;
    private ImageView sleep_icon;
    private Animation sleep1;
    Boolean isSleeping = Boolean.FALSE;
    Handler sleepingHandler;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    SettingClass settingClass = new SettingClass();

    public SettingClass getSettingClass() {
        return settingClass;
    }

    public LobbyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_lobby, container, false);

        //get UI components id
        readVarUI(rootView);
        XmlParser parser = null;
        try {
            parser = new XmlParser(getResources().openRawResource(R.raw.char_list));
            store_char_list = parser.readFile();
            parser = new XmlParser(getResources().openRawResource(R.raw.bg_list));
            store_bg_list = parser.readFile();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }
        getResourceStoreItem(store_char_list, store_bg_list);

        //handle all request from other object
        manageFragmentManagerRequest();

        //initialize game value
        initValue();

        //load UI from the user data
        refresh();
        //loadUI(currentUser);

        updateCoin();

        //update Status
        updateStatus();

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RenameClass editName = new RenameClass();
                editName.showPopupWindow(v, currentUser);
            }
        });

        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingClass.showPopupWindow(v, currentUser.getUsername(), getParentFragmentManager());
            }
        });

        hungriness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseHungryStatus(20);
            }
        });

        flattering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseFlatterStatus();
            }
        });

        sleepiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseSleepyStatus(1);
            }
        });

        inventory_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(v.getContext().LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.custom_inventory, null);
                PopupWindow inventory = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                ListView char_container = (ListView) popupView.findViewById(R.id.char_container);
                ListView bg_container = (ListView) popupView.findViewById(R.id.bg_container);
                Button inventory_close = (Button) popupView.findViewById(R.id.inventory_closebtn);
                ArrayList<Item> character = new ArrayList<>();
                Item char_default = new Item();
                char_default.setName("Chicken");
                char_default.setImageResource(R.drawable.char_chicken);
                character.add(char_default);

                ArrayList<Item> background = new ArrayList<>();
                Item bg_default = new Item();
                bg_default.setName("Bedroom");
                bg_default.setImageResource(R.drawable.bg1);
                background.add(bg_default);

                for (int i = 0; i < currentUser.getChar_list().size(); i++){
                    for (int j = 0; j < store_char_list.size(); j++){
                        if (currentUser.getChar_list().get(i).equals(store_char_list.get(j).getName()))
                            character.add(store_char_list.get(j));
                    }
                }

                for (int i = 0; i < currentUser.getBg_list().size(); i++){
                    for (int j = 0; j < store_bg_list.size(); j++){
                        if (currentUser.getBg_list().get(i).equals(store_bg_list.get(j).getName()))
                            background.add(store_bg_list.get(j));
                    }
                }

                StoreItemAdapter Charadapter = new StoreItemAdapter(character);
                char_container.setAdapter(Charadapter);
                StoreItemAdapter Bgadapter = new StoreItemAdapter(background);
                bg_container.setAdapter(Bgadapter);

                inventory.showAtLocation(v, Gravity.CENTER, 0,0);

                char_container.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        character_img.setBackgroundResource(character.get(position).getImageResource());
                    }
                });

                bg_container.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //rootView.setBackgroundResource(background.get(position).getImageResource());
                        getParentFragmentManager().getFragments().get(0).getView().setBackgroundResource(background.get(position).getImageResource());
                        getParentFragmentManager().getFragments().get(1).getView().setBackgroundResource(background.get(position).getImageResource());
                        getParentFragmentManager().getFragments().get(2).getView().setBackgroundResource(background.get(position).getImageResource());
                    }
                });

                inventory_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inventory.dismiss();
                    }
                });
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST) {
            if (data != null) {
                String username = data.getStringExtra("username");
                currentUser.setUsername(username);
                pullUserFromFirebase(username);
                settingClass.setOnLogin(setting_btn.getRootView(), username, "LOGOUT");
            }
        }
    }

    public void getResourceStoreItem(ArrayList<Item> character, ArrayList<Item> background){
        for(int i = 0; i < character.size(); i++){
            if(character.get(i).getName().equals("Tiger"))
                character.get(i).setImageResource(R.drawable.char_tiger);
            else if(character.get(i).getName().equals("Bear"))
                character.get(i).setImageResource(R.drawable.char_bear);
            else if(character.get(i).getName().equals("Cow"))
                character.get(i).setImageResource(R.drawable.char_cow);
            else if(character.get(i).getName().equals("Pig"))
                character.get(i).setImageResource(R.drawable.char_pig);
            else if(character.get(i).getName().equals("Buffalo"))
                character.get(i).setImageResource(R.drawable.char_buffalo);
        }

        for(int i = 0; i < background.size(); i++){
            if(background.get(i).getName().equals("Gym"))
                background.get(i).setImageResource(R.drawable.bg4);
            else if(background.get(i).getName().equals("Club"))
                background.get(i).setImageResource(R.drawable.bg6);
            else if(background.get(i).getName().equals("Garden"))
                background.get(i).setImageResource(R.drawable.bg2);
            else if(background.get(i).getName().equals("Park"))
                background.get(i).setImageResource(R.drawable.bg3);
            else if(background.get(i).getName().equals("Living Room"))
                background.get(i).setImageResource(R.drawable.bg5);
        }
    }

    public void pullUserFromFirebase(String username){
        db.collection("user").whereEqualTo("username", username).get()
        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                DocumentSnapshot document =  queryDocumentSnapshots.getDocuments().get(0);
                currentUser.setUsername(Objects.requireNonNull(document.getData().get("username")).toString());
                currentUser.setName(Objects.requireNonNull(document.getData().get("name")).toString());
                currentUser.setCoin(Objects.requireNonNull(document.getData().get("coin")).toString());
                currentUser.getHungriness().setPercentage(Integer.parseInt(Objects.requireNonNull(document.getData().get("hungriness")).toString()));
                currentUser.getFlattering().setPercentage(Integer.parseInt(Objects.requireNonNull(document.getData().get("flattering")).toString()));
                currentUser.getSleepiness().setPercentage(Integer.parseInt(Objects.requireNonNull(document.getData().get("sleepiness")).toString()));
                currentUser.getMood().setPercentage(Integer.parseInt(Objects.requireNonNull(document.getData().get("mood")).toString()));
                ArrayList<String> temp_char = new ArrayList<>();
                ArrayList<String> temp_bg = new ArrayList<>();
                currentUser.setChar_list(temp_char);
                currentUser.setBg_list(temp_bg);
                currentUser.getChar_list().addAll((ArrayList<String>) document.getData().get("character"));
                currentUser.getBg_list().addAll((ArrayList<String>) document.getData().get("background"));
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error getting data!!!", Toast.LENGTH_LONG).show();
            }
        });

        loadUI(currentUser);
    }

    public void readVarUI (View rootView) {
        hungriness = (Button) rootView.findViewById(R.id.hungry_status);
        flattering = (Button) rootView.findViewById(R.id.bathroom_status);
        sleepiness = (Button) rootView.findViewById(R.id.sleepy_status);
        mood = (Button) rootView.findViewById(R.id.mood_status);

        hungry_text = (TextView) rootView.findViewById(R.id.hungry_stat);
        flatter_text = (TextView) rootView.findViewById(R.id.flatter_stat);
        mood_text = (TextView) rootView.findViewById(R.id.mood_stat);
        sleepy_text = (TextView) rootView.findViewById(R.id.sleepy_stat);
        character_img = (ImageView) rootView.findViewById(R.id.character);

        name = (EditText) rootView.findViewById(R.id.name);
        coin = (EditText) rootView.findViewById(R.id.coin);

        inventory_btn = (Button) rootView.findViewById(R.id.inventory_btn);
        setting_btn = (Button) rootView.findViewById(R.id.setting_btn);
        name.setInputType(InputType.TYPE_NULL);
        coin.setInputType(InputType.TYPE_NULL);

        sleep1 = AnimationUtils.loadAnimation(getContext(),R.anim.anim_sleep);
        sleep_icon = (ImageView)rootView.findViewById(R.id.sleep_icon);

    }

    public void initValue() {
        obtained_item = new ArrayList<>();

        obtained_item.addAll(currentUser.getChar_list());
        obtained_item.addAll(currentUser.getBg_list());
    }

    public void loadUI (User user){
        name.setText(user.getName());
        coin.setText(user.getCoin());

        hungry_text.setText(String.format("%s%%", user.getHungriness().getPercentage()));
        flatter_text.setText(String.format("%s%%", user.getFlattering().getPercentage()));
        sleepy_text.setText(String.format("%s%%", user.getSleepiness().getPercentage()));
        mood_text.setText(String.format("%s%%", user.getMood().getPercentage()));

        if (user.getHungriness().getPercentage() > 70)
            user.getHungriness().setImage(R.drawable.green_hungry_status_button);
        else if (user.getHungriness().getPercentage() > 40)
            user.getHungriness().setImage(R.drawable.yellow_hungry_status_button);
        else
            user.getHungriness().setImage(R.drawable.red_hungry_status_button);

        if (user.getFlattering().getPercentage() > 70)
            user.getFlattering().setImage(R.drawable.green_bathroom_status_button);
        else if (user.getFlattering().getPercentage() > 40)
            user.getFlattering().setImage(R.drawable.yellow_bathroom_status_button);
        else
            user.getFlattering().setImage(R.drawable.red_bathroom_status_button);

        if (user.getSleepiness().getPercentage() > 70)
            user.getSleepiness().setImage(R.drawable.green_sleepy_status_button);
        else if (user.getSleepiness().getPercentage() > 40)
            user.getSleepiness().setImage(R.drawable.yellow_sleepy_status_button);
        else
            user.getSleepiness().setImage(R.drawable.red_sleepy_status_button);

        if (user.getMood().getPercentage() > 70)
            user.getMood().setImage(R.drawable.green_mood_status_button);
        else if (user.getMood().getPercentage() > 40)
            user.getMood().setImage(R.drawable.yellow_mood_status_button);
        else
            user.getMood().setImage(R.drawable.red_mood_status_button);


        hungriness.setBackgroundResource(user.getHungriness().getImage());
        flattering.setBackgroundResource(user.getFlattering().getImage());
        sleepiness.setBackgroundResource(user.getSleepiness().getImage());
        mood.setBackgroundResource(user.getMood().getImage());
    }

    private void manageFragmentManagerRequest(){
        getParentFragmentManager().setFragmentResultListener("AR_coin", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                coin.setText(Integer.toString( result.getInt("coin")));
                currentUser.setCoin(coin.getText().toString());
                currentUser.getMood().setPercentage(currentUser.getMood().getPercentage() + result.getInt("mood_increase"));
            }
        });

        getParentFragmentManager().setFragmentResultListener("Game_coin", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                int extraCoin = result.getInt("coin");
                coin.setText(Integer.toString(Integer.parseInt(coin.getText().toString()) + extraCoin));
                currentUser.setCoin(coin.getText().toString());
                currentUser.getMood().setPercentage(currentUser.getMood().getPercentage() + result.getInt("mood_increase"));
            }
        });

        getParentFragmentManager().setFragmentResultListener("request_save_data", getActivity(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (result.getBoolean("request")){
                    Bundle savedata = new Bundle();
                    savedata.putString("user_username", currentUser.getUsername());
                    savedata.putString("user_name", currentUser.getName());
                    savedata.putString("user_coin", currentUser.getCoin());
                    savedata.putInt("user_hungry_status", currentUser.getHungriness().getPercentage());
                    savedata.putInt("user_flatter_status", currentUser.getFlattering().getPercentage());
                    savedata.putInt("user_sleep_status", currentUser.getSleepiness().getPercentage());
                    savedata.putInt("user_mood_status", currentUser.getMood().getPercentage());
                    savedata.putStringArrayList("user_char_list", currentUser.getChar_list());
                    savedata.putStringArrayList("user_bg_list", currentUser.getBg_list());
                    getParentFragmentManager().setFragmentResult("savedata", savedata);
                }
            }
        });
        getParentFragmentManager().setFragmentResultListener("request_sync_data", getActivity(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (result.getBoolean("request")){
                    Bundle savedata = new Bundle();
                    savedata.putString("user_username", currentUser.getUsername());
                    savedata.putString("user_name", currentUser.getName());
                    savedata.putString("user_coin", currentUser.getCoin());
                    savedata.putInt("user_hungry_status", currentUser.getHungriness().getPercentage());
                    savedata.putInt("user_flatter_status", currentUser.getFlattering().getPercentage());
                    savedata.putInt("user_sleep_status", currentUser.getSleepiness().getPercentage());
                    savedata.putInt("user_mood_status", currentUser.getMood().getPercentage());
                    savedata.putStringArrayList("user_char_list", currentUser.getChar_list());
                    savedata.putStringArrayList("user_bg_list", currentUser.getBg_list());
                    getParentFragmentManager().setFragmentResult("syncdata", savedata);
                }
            }
        });

        getParentFragmentManager().setFragmentResultListener("pass_coin_please", getActivity(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Bundle bundle = new Bundle();
                bundle.putString("currentCoin", currentUser.getCoin());
                getParentFragmentManager().setFragmentResult("pass_coin",bundle );
            }
        });

        getParentFragmentManager().setFragmentResultListener("loaduserdata", getActivity(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                currentUser.setUsername(result.getString("user_username"));
                currentUser.setName(result.getString("user_name"));
                currentUser.setCoin(result.getString("user_coin"));
                currentUser.getHungriness().setPercentage(result.getInt("user_hungry_status"));
                currentUser.getFlattering().setPercentage(result.getInt("user_flatter_status"));
                currentUser.getSleepiness().setPercentage(result.getInt("user_sleep_status"));
                currentUser.getMood().setPercentage(result.getInt("user_mood_status"));
                ArrayList<String> temp_char = new ArrayList<>();
                ArrayList<String> temp_bg = new ArrayList<>();
                currentUser.setChar_list(temp_char);
                currentUser.setBg_list(temp_bg);
                currentUser.getChar_list().addAll(result.getStringArrayList("user_char_list"));
                currentUser.getBg_list().addAll(result.getStringArrayList("user_bg_list"));

                if (currentUser.getHungriness().getPercentage() > 70)
                    currentUser.getHungriness().setImage(R.drawable.green_hungry_status_button);
                else if (currentUser.getHungriness().getPercentage() > 40)
                    currentUser.getHungriness().setImage(R.drawable.yellow_hungry_status_button);
                else
                    currentUser.getHungriness().setImage(R.drawable.red_hungry_status_button);

                if (currentUser.getFlattering().getPercentage() > 70)
                    currentUser.getFlattering().setImage(R.drawable.green_bathroom_status_button);
                else if (currentUser.getFlattering().getPercentage() > 40)
                    currentUser.getFlattering().setImage(R.drawable.yellow_bathroom_status_button);
                else
                    currentUser.getFlattering().setImage(R.drawable.red_bathroom_status_button);

                if (currentUser.getSleepiness().getPercentage() > 70)
                    currentUser.getSleepiness().setImage(R.drawable.green_sleepy_status_button);
                else if (currentUser.getSleepiness().getPercentage() > 40)
                    currentUser.getSleepiness().setImage(R.drawable.yellow_sleepy_status_button);
                else
                    currentUser.getSleepiness().setImage(R.drawable.red_sleepy_status_button);

                if (currentUser.getMood().getPercentage() > 70)
                    currentUser.getMood().setImage(R.drawable.green_mood_status_button);
                else if (currentUser.getMood().getPercentage() > 40)
                    currentUser.getMood().setImage(R.drawable.yellow_mood_status_button);
                else
                    currentUser.getMood().setImage(R.drawable.red_mood_status_button);

            }
        });


        getParentFragmentManager().setFragmentResultListener("login_request", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                if (result.getBoolean("islogin")) {
                    startActivityForResult(intent, LOGIN_REQUEST);
                }
                else{
                    currentUser.setUsername("");
                    settingClass.setOnLogout(setting_btn.getRootView());
                }
            }
        });

        getParentFragmentManager().setFragmentResultListener("purchase_success", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                String item = result.getString("purchased_item", null);
                obtained_item.add(item);
                if (result.getInt("item_type", 0) == CHARACTER_ITEM) {
                    currentUser.getChar_list().add(item);
                }
                else if (result.getInt("item_type", 0) == BACKGROUND_ITEM) {
                    currentUser.getBg_list().add(item);
                }

                int cost = result.getInt("cost_coin", 0);
                int new_value = Integer.parseInt(coin.getText().toString()) - cost;
                coin.setText(Integer.toString(new_value));
                currentUser.setCoin(Integer.toString(new_value));
                sendDatatoStore();
            }
        });

    }

    public void updateStatus() {
        final Handler hungry_update = new Handler();
        hungry_update.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentUser.getHungriness().getPercentage() > 0)
                    currentUser.getHungriness().setPercentage(currentUser.getHungriness().getPercentage() - 1);

                if (currentUser.getHungriness().getPercentage() > 70)
                    currentUser.getHungriness().setImage(R.drawable.green_hungry_status_button);
                else if (currentUser.getHungriness().getPercentage() > 40)
                    currentUser.getHungriness().setImage(R.drawable.yellow_hungry_status_button);
                else
                    currentUser.getHungriness().setImage(R.drawable.red_hungry_status_button);

                hungry_text.setText(String.format("%s%%", currentUser.getHungriness().getPercentage()));
                hungriness.setBackgroundResource(currentUser.getHungriness().getImage());

                hungry_update.postDelayed(this, 10000);
            }
        }, 10000); //after 10s

        final Handler flatter_update = new Handler();
        flatter_update.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentUser.getFlattering().getPercentage() > 0)
                    currentUser.getFlattering().setPercentage(currentUser.getFlattering().getPercentage() - 1);

                if (currentUser.getFlattering().getPercentage() > 70)
                    currentUser.getFlattering().setImage(R.drawable.green_bathroom_status_button);
                else if (currentUser.getFlattering().getPercentage() > 40)
                    currentUser.getFlattering().setImage(R.drawable.yellow_bathroom_status_button);
                else
                    currentUser.getFlattering().setImage(R.drawable.red_bathroom_status_button);

                flatter_text.setText(String.format("%s%%", currentUser.getFlattering().getPercentage()));
                flattering.setBackgroundResource(currentUser.getFlattering().getImage());

                flatter_update.postDelayed(this, 12000);
            }
        }, 12000); //after 12s

        final Handler sleepy_update = new Handler();
        sleepy_update.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentUser.getSleepiness().getPercentage() > 0)
                    currentUser.getSleepiness().setPercentage(currentUser.getSleepiness().getPercentage() - 1);

                if (currentUser.getSleepiness().getPercentage() > 70)
                    currentUser.getSleepiness().setImage(R.drawable.green_sleepy_status_button);
                else if (currentUser.getSleepiness().getPercentage() > 40)
                    currentUser.getSleepiness().setImage(R.drawable.yellow_sleepy_status_button);
                else
                    currentUser.getSleepiness().setImage(R.drawable.red_sleepy_status_button);

                sleepy_text.setText(String.format("%s%%", currentUser.getSleepiness().getPercentage()));
                sleepiness.setBackgroundResource(currentUser.getSleepiness().getImage());

                sleepy_update.postDelayed(this, 20000);
            }
        }, 20000); //after 20s

        final Handler mood_update = new Handler();
        mood_update.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentUser.getMood().getPercentage() > 0)
                    currentUser.getMood().setPercentage(currentUser.getMood().getPercentage() - 1);

                if (currentUser.getMood().getPercentage() > 70)
                    currentUser.getMood().setImage(R.drawable.green_mood_status_button);
                else if (currentUser.getMood().getPercentage() > 40)
                    currentUser.getMood().setImage(R.drawable.yellow_mood_status_button);
                else
                    currentUser.getMood().setImage(R.drawable.red_mood_status_button);

                mood_text.setText(String.format("%s%%", currentUser.getMood().getPercentage()));
                mood.setBackgroundResource(currentUser.getMood().getImage());

                mood_update.postDelayed(this, 15000);
            }
        }, 15000); //after 15s

    }

    public void sendDatatoStore(){
        Bundle bundle = new Bundle();
        bundle.putInt("coin", Integer.parseInt(coin.getText().toString()));
        bundle.putStringArrayList("item_list", obtained_item);
        getParentFragmentManager().setFragmentResult("lobby_to_store", bundle);
    }

    public void updateCoin(){
        final Handler update_coin_handler = new Handler();
        update_coin_handler.post(new Runnable() {
            @Override
            public void run() {
                sendDatatoStore();
                update_coin_handler.post(this);
            }
        });
    }

    public void refresh(){
        final Handler refresh = new Handler();
        refresh.post(new Runnable() {
            @Override
            public void run() {
                loadUI(currentUser);
                refresh.post(this);
            }
        });

    }

    public void increaseHungryStatus(int range){
        int old_status =Integer.parseInt(hungry_text.getText().toString().substring(0, hungry_text.getText().toString().indexOf('%')));
        int new_coin = Integer.parseInt(currentUser.getCoin()) - 5;

        int new_status = old_status + range;
        if(new_coin<0)
        {
            Toast.makeText(getActivity(),"Coin not enough!",Toast.LENGTH_LONG).show();
        }
        if(old_status<90 && new_coin >= 0) {
            if (new_status < 100) {
                hungry_text.setText(new_status + "%");
                currentUser.getHungriness().setPercentage(new_status);
            } else {
                hungry_text.setText("100%");
                currentUser.getHungriness().setPercentage(100);
            }
            if (new_status < 100 + range) {
                currentUser.setCoin(Integer.toString(new_coin));
                coin.setText(Integer.toString(new_coin));
            }

            int new_flatter = Integer.parseInt(flatter_text.getText().toString().substring(0, flatter_text.getText().toString().indexOf('%')));
            if (new_flatter - 5 <= 0) {
                currentUser.getFlattering().setPercentage(0);
                flatter_text.setText("0%");
            } else {
                currentUser.getFlattering().setPercentage(new_flatter - 5);
                flatter_text.setText((new_flatter - 5) + "%");
            }

            if (currentUser.getHungriness().getPercentage() > 70)
                currentUser.getHungriness().setImage(R.drawable.green_hungry_status_button);
            else if (currentUser.getHungriness().getPercentage() > 40)
                currentUser.getHungriness().setImage(R.drawable.yellow_hungry_status_button);
            else
                currentUser.getHungriness().setImage(R.drawable.red_hungry_status_button);

            hungriness.setBackgroundResource(currentUser.getHungriness().getImage());

            if (currentUser.getFlattering().getPercentage() > 70)
                currentUser.getFlattering().setImage(R.drawable.green_bathroom_status_button);
            else if (currentUser.getFlattering().getPercentage() > 40)
                currentUser.getFlattering().setImage(R.drawable.yellow_bathroom_status_button);
            else
                currentUser.getFlattering().setImage(R.drawable.red_bathroom_status_button);

            flattering.setBackgroundResource(currentUser.getFlattering().getImage());
        }
    }

    public void increaseFlatterStatus(){

        int status = Integer.parseInt(flatter_text.getText().toString().substring(0, flatter_text.getText().toString().indexOf('%')));
        int new_coin = Integer.parseInt(currentUser.getCoin()) - 5;
        if(new_coin<0)
        {
            Toast.makeText(getActivity(),"Coin not enough!",Toast.LENGTH_LONG).show();
        }
        if (status < 70 &&new_coin>=0) {
            flatter_text.setText("100%");
            currentUser.getFlattering().setPercentage(100);


            currentUser.setCoin(Integer.toString(new_coin));
            coin.setText(Integer.toString(new_coin));

            if (currentUser.getFlattering().getPercentage() > 70)
                currentUser.getFlattering().setImage(R.drawable.green_bathroom_status_button);
            else if (currentUser.getFlattering().getPercentage() > 40)
                currentUser.getFlattering().setImage(R.drawable.yellow_bathroom_status_button);
            else
                currentUser.getFlattering().setImage(R.drawable.red_bathroom_status_button);

            flattering.setBackgroundResource(currentUser.getFlattering().getImage());
        }
    }

    public void increaseSleepyStatus(int range){

        Runnable sleep = new Runnable() {
            @Override
            public void run() {

                int new_status = Integer.parseInt(sleepy_text.getText().toString().substring(0, sleepy_text.getText().toString().indexOf('%'))) + 1;
                if (new_status < 100) {
                    sleepy_text.setText(new_status + "%");
                    currentUser.getSleepiness().setPercentage(new_status);
                } else {
                    sleepy_text.setText("100%");
                    currentUser.getSleepiness().setPercentage(100);
                }

                if (currentUser.getSleepiness().getPercentage() > 70)
                    currentUser.getSleepiness().setImage(R.drawable.green_sleepy_status_button);
                else if (currentUser.getSleepiness().getPercentage() > 40)
                    currentUser.getSleepiness().setImage(R.drawable.yellow_sleepy_status_button);
                else
                    currentUser.getSleepiness().setImage(R.drawable.red_sleepy_status_button);

                sleepiness.setBackgroundResource(currentUser.getSleepiness().getImage());
                if (isSleeping) {
                    sleepingHandler.postDelayed(this, 10000);
                    sleep_icon.startAnimation(sleep1);
                }
            }
        };



        if (!isSleeping) {
            hungriness.setClickable(false);
            mood.setClickable(false);
            flattering.setClickable(false);
            isSleeping = true;
            sleep_icon.setVisibility(View.VISIBLE);
            if (sleepingHandler == null)
                sleepingHandler = new Handler();
            sleepingHandler.postDelayed(sleep,10000);
        }
        else
        {
            hungriness.setClickable(true);
            mood.setClickable(true);
            flattering.setClickable(true);
            sleep_icon.setVisibility(View.INVISIBLE);
            isSleeping = false;
        }
    }

}


