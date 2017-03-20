package com.example.labdesenvolvimento.integracaoomdb;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {

    Button btnBusca;
    String jsonOmdb;

    @Override
    protected void onRestart() {
        super.onRestart();
        btnBusca.setEnabled(true);
        btnBusca.setText("Buscar");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnBusca = (Button)findViewById(R.id.btnBusca);
    }

    private class OmdbRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            try {
                String titulo = params[0];
                String ano = params[1];
                URL url = new URL("http://www.omdbapi.com/?t=" +
                        titulo.concat(!ano.equals("")? "&y=" + ano : "")
                            + "&plot=full");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                String result = Util.webToString(urlConnection.getInputStream());

                return result;
            } catch (Exception e) {
                Log.e("Error", "Error ", e);
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            jsonOmdb = s;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            btnBusca.setEnabled(false);
            btnBusca.setText("Buscando...");
        }
    }

    public void buscar(View v){
        TextView textTitulo = (TextView) findViewById(R.id.textTitulo);
        TextView textAno = (TextView) findViewById(R.id.textAno);

        //Valida titulo nulo
        if(textTitulo.getText().toString().equals("")){
            Toast.makeText(this,"Titulo é obrigatório",Toast.LENGTH_SHORT).show();
            btnBusca.setEnabled(true);
            btnBusca.setText("Buscar");
            return;
        }

        OmdbRequest omdb  = new OmdbRequest();
        try {
            omdb.execute(textTitulo.getText().toString(), textAno.getText().toString());

            //Valida resposta do servidor
            if(Util.isValidResponse(jsonOmdb)){
                Toast.makeText(this,"Nenhum filme encontrado!",Toast.LENGTH_SHORT).show();
                btnBusca.setEnabled(true);
                btnBusca.setText("Buscar");
                return;
            }
            Intent tela = new Intent(this,FilmeActivity.class);
            tela.putExtra("jsonOmdb",jsonOmdb);
            startActivity(tela);
            jsonOmdb = "";
        }catch (Exception e){
            Toast.makeText(this,"Falha ao buscar filme!",Toast.LENGTH_SHORT).show();
            btnBusca.setEnabled(true);
            btnBusca.setText("Buscar");
        }
    }

}
