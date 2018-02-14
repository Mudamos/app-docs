package tagview.com.br.mudamosexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.HashMap;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private TextView resultView;

    static final String PAYLOAD_IDENTIFIER = "org.mudamos.signer.message";
    static final String RESULT_IDENTIFIER = "org.mudamos.signer.message.result";
    static final String MUDAMOS_SIGN_TYPE = "mudamos/sign_message";

    static final int REQUEST_CODE = 33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        resultView = (TextView) findViewById(R.id.result_view);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);

                String message = UUID.randomUUID().toString().substring(0, 20);
                resultView.setText("Mensagem a ser assianada: " + message);

                intent.putExtra(PAYLOAD_IDENTIFIER, message);
                intent.addCategory(PAYLOAD_IDENTIFIER);
                intent.setType(MUDAMOS_SIGN_TYPE);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    log("App Mudamos+ não instalada");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                HashMap<String, Object> result = (HashMap<String, Object>) data.getSerializableExtra(RESULT_IDENTIFIER);
                String message = result.containsKey("message") ? (String) result.get("message") : null;
                String publicKey = result.containsKey("publicKey") ? (String) result.get("publicKey") : null;
                String timestamp = result.containsKey("timestamp") ? (String) result.get("timestamp") : null;
                String signature = result.containsKey("signature") ? (String) result.get("signature") : null;
                Boolean error = result.containsKey("error") ? (Boolean) result.get("error") : true;

                log("Erro?: " + error + "\n\nMensagem: " + message + "\n\nChave pública: " + publicKey + "\n\nTimestamp: " + timestamp + "\n\n\nSignature: " + signature);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                log("Usuário cancelou a ação");
            } else {
                log("Erro inesperado");
            }
        } else {
            log("Requisição inválida");
        }
    }

    private void log(String message) {
        resultView.append("\n\nResultado:\n\n" + message);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
