package fr.iut.kotlin.androidproject.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.iut.kotlin.androidproject.R
import fr.iut.kotlin.androidproject.fragment.FirstFragment
import fr.iut.kotlin.androidproject.fragment.SecondFragment
import fr.iut.kotlin.androidproject.fragment.ThirdFragment


class FragmentTestActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_test)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val firstFragment= FirstFragment()
        val secondFragment= SecondFragment()
        val thirdFragment= ThirdFragment()

        setCurrentFragment(firstFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.location ->setCurrentFragment(firstFragment)
                R.id.map ->setCurrentFragment(secondFragment)
                R.id.settings ->setCurrentFragment(thirdFragment)

            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }

}