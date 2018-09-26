package org.techtown.share_chat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonElement;
import com.google.gson.Gson;

import org.techtown.share_chat.databinding.ActivityChatroomBinding;
import org.techtown.share_chat.databinding.ItemChatBinding;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import ai.api.AIServiceException;
import ai.api.RequestExtras;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.android.GsonFactory;
import ai.api.model.AIContext;
import ai.api.model.AIError;
import ai.api.model.AIEvent;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Metadata;
import ai.api.model.Result;
import ai.api.model.Status;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    ActivityChatroomBinding binding;
    ItemChatBinding ic;
    private static final int CHATROOM = R.layout.activity_chatroom;

    private Vector<Chat> chats;
    private ChatAdapter adapter;
    private AIDataService aiDataService;
    private Gson gson = GsonFactory.getGson();

    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;

    private GPSTracker gps;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,  R.layout.activity_chatroom);

        final AIConfiguration config = new AIConfiguration("f5dfc697c01c47b7bab5215704793918",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiDataService = new AIDataService(this, config);
        final AIRequest aiRequest = new AIRequest();

        //int img = getIntent().getIntExtra("fImg", 0);
        chats = new Vector<>();
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.chatRoomListView.setLayoutManager(manager);
        //sendRequest("hello");
        //binding.chatRoomFriendNameTxtView.setText(fName);
       /* switch (fName) {
            case "근육몬":
                chats.add(new Chat("근육몬", "안녕",0, 0));
                chats.add(new Chat("근육몬", "안녕~",1, R.drawable.chat_bot));
                chats.add(new Chat("근육몬", "반가워 나는 알통몬이야.",0, 0));
                chats.add(new Chat("근육몬", "나도 반가워 난 근육몬이야 ㅎㅎ",1,R.drawable.chat_bot));
                chats.add(new Chat("근육몬", "안드로이드 알아?",0,0));
                chats.add(new Chat("근육몬", "알긴 아는데 잘 몰라 ㅎ",1,R.drawable.chat_bot));
                chats.add(new Chat("근육몬", "애국가는?",0,0));
                chats.add(new Chat("근육몬", "1절만 알아",1,R.drawable.chat_bot));
                chats.add(new Chat("근육몬", "알려줘",0,0));
                chats.add(new Chat("근육몬", "동해물과 백두산이 마르고 닳도록 하나님이 보우하사 우리나라만세 무궁화 삼천리 화려강산 대한사람 대한으로 길이 보전하세",1,R.drawable.chat_bot));
                break;
            case "괴력몬":
                chats.add(new Chat("괴력몬", "안녕하세요",0 ,0));
                chats.add(new Chat("괴력몬", "안녕하세요~",1,R.drawable.chat_bot));
                chats.add(new Chat("괴력몬", "반가워여 저는 알통몬입니다.",0,0));
                chats.add(new Chat("괴력몬", "반가워요 저는 괴력몬이에요~~",1,R.drawable.chat_bot));
                chats.add(new Chat("괴력몬", "안드로이드 아세요?",0,0));
                chats.add(new Chat("괴력몬", "알긴 아는데 잘 몰라요^^",1,R.drawable.chat_bot));
                chats.add(new Chat("괴력몬", "애국가는요??",0,0));
                chats.add(new Chat("괴력몬", "1절만 알아여",1,R.drawable.chat_bot));
                chats.add(new Chat("괴력몬", "알려줘요",0,0));
                chats.add(new Chat("괴력몬", "동해물과 백두산이 마르고 닳도록 하나님이 보우하사 우리나라만세 무궁화 삼천리 화려강산 대한사람 대한으로 길이 보전하세",1,R.drawable.chat_bot));
                break;
        }*/
        adapter = new ChatAdapter(chats, this);
        binding.chatRoomListView.setAdapter(adapter);
        /*binding.backChatRoomListImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });*/


        /*binding.sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps = new GPSTracker(MainActivity.this);
                Log.i("gps enable", "gps enable");
                // GPS 사용유무 가져오기

                double latitude = 0;
                double longitude = 0;

                if (gps.isGetLocation()) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    Log.i("gps 1", "gps 1");

                } else {
                    // GPS 를 사용할수 없으므로
                    gps.showSettingsAlert();
                    Log.i("gps n", "gps n");
                }
                String msg = binding.msgEditText.getText().toString();
                chats.add(new Chat(msg, 0, 0));
                sendRequest( msg + " \\ " + latitude + " \\ " + longitude);
                adapter.notifyDataSetChanged();
                binding.chatRoomListView.scrollToPosition(chats.size()-1);
                binding.msgEditText.setText("");
            }
        });*/
    }



    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_ACCESS_FINE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            isAccessFineLocation = true;

        } else if (requestCode == PERMISSIONS_ACCESS_COARSE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            isAccessCoarseLocation = true;
        }

        if (isAccessFineLocation && isAccessCoarseLocation) {
            isPermission = true;
        }
    }

    public void onClick(View v) {
        gps = new GPSTracker(MainActivity.this);
        Log.i("gps enable", "gps enable");
        // GPS 사용유무 가져오기

        double latitude = 0;
        double longitude = 0;

        if (gps.isGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            Log.i("gps 1", "gps 1");

        } else {
            // GPS 를 사용할수 없으므로
            gps.showSettingsAlert();
            Log.i("gps n", "gps n");
        }
        //new message
        String msg = binding.msgEditText.getText().toString();
        chats.add(new Chat(msg, 0, 0));
        sendRequest( msg + " \\ " + latitude + " \\ " + longitude);
        adapter.notifyDataSetChanged();
        binding.chatRoomListView.scrollToPosition(chats.size()-1);
        binding.msgEditText.setText("");
    }

    private void sendRequest(String text) {
        Log.d(TAG, text);

        final String queryString = String.valueOf(text);
        final String eventString = null;
        final String contextString = null;


        if (TextUtils.isEmpty(queryString) && TextUtils.isEmpty(eventString)) {
            //onError(new AIError(getString(R.string.non_empty_query)));
            return;
        }

        new AiTask().execute(queryString, eventString, contextString);
    }

    public class AiTask extends AsyncTask<String, Void, AIResponse> {
        private AIError aiError;

        @Override
        protected AIResponse doInBackground(final String... params) {
            final AIRequest request = new AIRequest();
            String query = params[0];
            String event = params[1];
            String context = params[2];

            if (!TextUtils.isEmpty(query)){
                request.setQuery(query);
            }

            if (!TextUtils.isEmpty(event)){
                request.setEvent(new AIEvent(event));
            }

            RequestExtras requestExtras = null;
            if (!TextUtils.isEmpty(context)) {
                final List<AIContext> contexts = Collections.singletonList(new AIContext(context));
                requestExtras = new RequestExtras(contexts, null);
            }

            try {
                return aiDataService.request(request, requestExtras);
            } catch (final AIServiceException e) {
                aiError = new AIError(e);
                return null;
            }
        }
        @Override
        protected void onPostExecute(final AIResponse response) {
            if (response != null) {
                onResult(response);
            } else {
                onError(aiError);
            }
        }
    }

    private void onResult(final AIResponse response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Variables
                gson.toJson(response);
                final Status status = response.getStatus();
                final Result result = response.getResult();
                final String speech = result.getFulfillment().getSpeech();
                boolean layout = speech.contains("map");
                final Metadata metadata = result.getMetadata();
                final HashMap<String, JsonElement> params = result.getParameters();

                // Logging
                Log.d(TAG, "onResult");
                Log.i(TAG, "Received success response");
                Log.i(TAG, "Status code: " + status.getCode());
                Log.i(TAG, "Status type: " + status.getErrorType());
                Log.i(TAG, "Resolved query: " + result.getResolvedQuery());
                Log.i(TAG, "Action: " + result.getAction());
                Log.i(TAG, "Speech: " + speech);

                if (metadata != null) {
                    Log.i(TAG, "Intent id: " + metadata.getIntentId());
                    Log.i(TAG, "Intent name: " + metadata.getIntentName());
                }

                if (params != null && !params.isEmpty()) {
                    Log.i(TAG, "Parameters: ");
                    for (final Map.Entry<String, JsonElement> entry : params.entrySet()) {
                        Log.i(TAG, String.format("%s: %s",
                                entry.getKey(), entry.getValue().toString()));
                    }
                }

                //Update view to bot says
                /*final Message receivedMessage = new Message.Builder()
                        .setUser(droidKaigiBot)
                        .setRightMessage(false)
                        .setMessageText(speech)
                        .build();
                chatView.receive(receivedMessage);*/

                if(layout){
                    MapViewFragmentNaver fragment1 = new MapViewFragmentNaver();
                    fragment1.setArguments(new Bundle());
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.add(R.id.MapHere, fragment1);
                    fragmentTransaction.commit();
                }else {
                    chats.add(new Chat(speech, 1, R.drawable.chat_bot));
                }
            }
        });
    }
    private void onError(final AIError error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG,error.toString());
            }
        });
    }
    /*@Override
    public void onBackPressed() {
        Intent intent = new Intent(ChatRoomActivity.this, MainActivity.class);
        intent.putExtra("position", 1);
        startActivity(intent);
        finish();
    }*/
}