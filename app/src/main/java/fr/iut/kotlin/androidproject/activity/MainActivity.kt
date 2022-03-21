package fr.iut.kotlin.androidproject.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.iut.kotlin.androidproject.R
import fr.iut.kotlin.androidproject.fragment.MapFragment
import fr.iut.kotlin.androidproject.fragment.SettingsFragment
import fr.iut.kotlin.androidproject.fragment.WeatherFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bnv = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val fm: FragmentManager = supportFragmentManager

        val weatherFragment = WeatherFragment()
        val mapFragment = MapFragment()
        val settingsFragment = SettingsFragment()

        var active: Fragment = weatherFragment

        fm.beginTransaction().add(R.id.flFragment, settingsFragment, "Settings").hide(settingsFragment).commit();
        fm.beginTransaction().add(R.id.flFragment, mapFragment, "Map").hide(mapFragment).commit();
        fm.beginTransaction().add(R.id.flFragment, weatherFragment, "Weather").commit();

        bnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.location -> {
                    fm.beginTransaction().hide(active).show(weatherFragment).commit()
                    active = weatherFragment
                    it.isChecked = true
                    true
                }
                R.id.map -> {
                    fm.beginTransaction().hide(active).show(mapFragment).commit()
                    active = mapFragment
                    it.isChecked = true
                    true
                }
                R.id.settings -> {
                    fm.beginTransaction().hide(active).show(settingsFragment).commit()
                    active = settingsFragment
                    it.isChecked = true
                    true
                }
            }
            false
        }
    }

}