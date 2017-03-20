package com.example.labdesenvolvimento.integracaoomdb;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

public class FilmeActivity extends Activity {

    ImageView imageFilme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filme);
        imageFilme = (ImageView) findViewById(R.id.imageFilme);
        try {
            String jsonOmdb = getIntent().getStringExtra("jsonOmdb");
            Filme filme = Util.convertJSONtoFilme(jsonOmdb);
            new DownloadImageAsync().execute(new URL(filme.getLinkPoster()));
            ((TextView)findViewById(R.id.txtTitulo)).setText(filme.getTitulo());
            ((TextView)findViewById(R.id.txtDiretor)).setText(filme.getDiretor());
            ((TextView)findViewById(R.id.txtDuracao)).setText(filme.getDuracao());
            ((TextView)findViewById(R.id.txtAno)).setText(filme.getAno());
            ((TextView)findViewById(R.id.txtGenero)).setText(filme.getGenero());
            ((TextView)findViewById(R.id.txtSinopse)).setText(filme.getSinopse());
        } catch (Exception e){
            Toast.makeText(getApplicationContext(),"Erro ao carregar filme",Toast.LENGTH_SHORT).show();
        }
    }

    public void voltar(View v){
        finish();
    }

    private class DownloadImageAsync extends AsyncTask<URL,Integer,Bitmap> {

        @Override
        protected Bitmap doInBackground(URL... params) {
            URL url = params[0];

            Bitmap bitMap = null;

            try {
                bitMap = Util.downloadImage(url);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(),"Erro ao carregar imagem",Toast.LENGTH_SHORT).show();
            }

            return bitMap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap != null){
                imageFilme.setImageBitmap(bitmap);
                imageFilme.setVisibility(View.VISIBLE);
            }
        }
    }
}
