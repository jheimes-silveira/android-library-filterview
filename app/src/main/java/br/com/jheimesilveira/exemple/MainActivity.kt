package br.com.jheimesilveira.exemple

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initButtons()
    }

    private fun initButtons() {
        btnCidade.setOnClickListener {
            startActivity(Intent(this@MainActivity, ActFilterCidade::class.java))
        }

        btnEstaticoMock.setOnClickListener {
            startActivity(Intent(this@MainActivity, ActEstaticoMock::class.java))
        }
    }
}
