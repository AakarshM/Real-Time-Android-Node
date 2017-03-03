package aakarsh.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    private Socket socket;
    TextView tv;
    Button button2;
    Button button;
    Button b4, b3; //toggle, remove
    EditText eText;
    String room = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        eText = (EditText) findViewById(R.id.editText);
        try {
            socket = IO.socket("http://7cb04c83.ngrok.io/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(buttonListener);
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(buttonListener2);
        b4 = (Button) findViewById(R.id.button4);
        b4.setOnClickListener(buttonListener4);
        b3 = (Button) findViewById(R.id.button3);
        b3.setOnClickListener(buttonListener3);
        try {
            meth();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public View.OnClickListener buttonListener3 = new View.OnClickListener() {
        public void onClick (View view){
            String x = returnVal();
            socket.emit("questionRemoveRequest", room
            );

        }};
    public View.OnClickListener buttonListener4 = new View.OnClickListener() {
        public void onClick (View view){
            String x = returnVal();
            socket.emit("questionRequest", room);

        }};
    public View.OnClickListener buttonListener = new View.OnClickListener() {
        public void onClick (View view){
            String x = returnVal();
            socket.emit("createMessage", room);

        }};
    public View.OnClickListener buttonListener2 = new View.OnClickListener() {
        public void onClick (View view){
            socket.emit("join", eText.getText().toString());
            room = eText.getText().toString();
        }};

    public void meth() throws URISyntaxException {
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                socket.emit("createMessage", "CONNECTED");

            }

        }).on("newMessage", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                //System.out.println(args.toString());
                final JSONObject obj = (JSONObject) args[0];
                final String str = obj.toString();
                System.out.println(obj.toString());
                setText(str);
            }

        }).on("newQuestion", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                //System.out.println(args.toString());
                final JSONObject obj = (JSONObject) args[0];
                questionVisible();
            }

        }).on("goneQuestion", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                //System.out.println(args.toString());
                final JSONObject obj = (JSONObject) args[0];
                questionInvisible();
            }

        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            public void rand(){
                System.out.println("You've disconnected");
            }

            @Override
            public void call(Object... args) {}

        });
        socket.connect();
    }

    public String returnVal(){
        return "String gened";
    }

    public void setText(final String string){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv.setText(string);
            }
        });
    }

    public void joinRoom(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("room", "ROOM1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("foo", obj);
    }

    public void questionVisible(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.setVisibility(View.VISIBLE);
                button2.setVisibility(View.VISIBLE);
            }
        });

    }
    public void questionInvisible(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.setVisibility(View.INVISIBLE);
                button2.setVisibility(View.INVISIBLE);
            }
        });

    }

}
