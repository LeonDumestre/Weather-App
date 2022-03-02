package fr.iut.kotlin.androidproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var URL_OPENDATA = "https://public.opendatasoft.com/explore/dataset/arome-0025-enriched/information/?disjunctive.commune&disjunctive.code_commune"
    private var URL_TIME_TEXT = "http://worldtimeapi.org/api/timezone/Europe/paris"
    private var URL_METEO_TEXT = "https://public.opendatasoft.com/api/records/1.0/search/?dataset=arome-0025-enriched&q=&facet=commune&facet=code_commune"
    private lateinit var tvDate : TextView
    private lateinit var btDate : Button
    private lateinit var btHtml : Button
    private lateinit var btGoogle : Button
    private lateinit var btMeteo : Button
    private lateinit var webView : WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btDate = findViewById(R.id.btDate)
        tvDate = findViewById(R.id.tvDate)
        btHtml = findViewById(R.id.btTextHtml)
        btGoogle = findViewById(R.id.btGoogle)
        btMeteo = findViewById(R.id.btMeteo)
        webView = findViewById(R.id.webView)

        btDate.setOnClickListener(this)
        btHtml.setOnClickListener(this)
        btGoogle.setOnClickListener(this)
        btMeteo.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btDate -> {
                HttpConnectServerAsyncTask().execute(URL_TIME_TEXT, tvDate)
            }

            R.id.btTextHtml -> {
                val strHtml = "<html><body><strong> <em>Ceci est un texte au format HTML </em> </strong></br>qui s'affiche très simplement</body></html>";
                webView.loadData(strHtml , "text/html; charset=utf-8", "UTF-8")
            }

            R.id.btGoogle -> {
                webView.loadUrl(URL_OPENDATA)
                webView.webViewClient = WebViewClient()
            }

            R.id.btMeteo -> {
                webView.loadUrl(URL_METEO_TEXT)
            }
        }
    }


}