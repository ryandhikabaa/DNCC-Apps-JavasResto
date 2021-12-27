package com.teamwork.javasresto.activity

import Config
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.teamwork.javasresto.R
import com.teamwork.javasresto.adapter.ProductAdapter
import com.teamwork.javasresto.databinding.ActivityDashboardBinding
import com.teamwork.javasresto.models.ProductModels
import java.text.SimpleDateFormat
import java.util.*


class DashboardActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityDashboardBinding
    private lateinit var sharedPreferences: SharedPreferences
    var mGoogleSignInClient: GoogleSignInClient? = null
//    var sName = ""
//    var sEmail = ""
//    var SPhoto = ""
//    var sToken = ""
    val list = ArrayList<ProductModels>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataSet()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val sp = getSharedPreferences(Config::SHARED_PREF_NAME.toString(), MODE_PRIVATE)
        val sToken = sp.getString(Config::LOGIN_TOKEN_SHARED_PREF.toString(), "")
        val sEmail = sp.getString(Config::LOGIN_EMAIL_SHARED_PREF.toString(), "")
        val sName = sp.getString(Config::LOGIN_NAME_SHARED_PREF.toString(), "")
        val SPhoto = sp.getString(Config::LOGIN_PICT_SHARED_PREF.toString(), "")

        binding.tvName.setText(sName)
        binding.tvEmail.setText(sEmail)
        Glide
            .with(this)
            .load(SPhoto)
            .centerCrop()
            .placeholder(R.drawable.placeholder)
            .into(binding.ivProfile)
        binding.divLogout.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Logout")
                .setConfirmText("Ya")
                .setConfirmClickListener { sDialog ->
                    signOut()
                    sDialog.dismissWithAnimation()
                }
                .setCancelButton(
                    "Tidak"
                ) { sDialog -> sDialog.dismissWithAnimation() }
                .show()
        }

    }

    private fun signOut() {
        mGoogleSignInClient?.signOut()
            ?.addOnCompleteListener(this, OnCompleteListener<Void?> {
                Toast.makeText(
                    this,
                    "Anda telah logout dari aplikasi.\n" +
                            "Untuk mengakses beberapa fitur, Anda harus login terlebih dahulu",
                    Toast.LENGTH_SHORT
                ).show()
                sharedPreferences = getSharedPreferences(Config::SHARED_PREF_NAME.toString(), Context.MODE_PRIVATE)
                sharedPreferences.edit()
                    .putString(Config::LOGIN_TOKEN_SHARED_PREF.toString(), "")
                    .putString(Config::LOGIN_NAME_SHARED_PREF.toString(), "")
                    .putString(Config::LOGIN_EMAIL_SHARED_PREF.toString(), "")
                    .putString(Config::LOGIN_PICT_SHARED_PREF.toString(), "")
                    .apply()
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                finish()
            })
    }

    @SuppressLint("SetTextI18n")
    private fun greeting() {
        val calendar = Calendar.getInstance()
        val timeOfDay = calendar[Calendar.HOUR_OF_DAY]
        if (timeOfDay >= 0 && timeOfDay < 12) {
            binding.tvGreeting.setText("Halo, Selamat Pagi")
        } else if (timeOfDay >= 12 && timeOfDay < 15) {
            binding.tvGreeting.setText("Halo, Selamat Siang")
        } else if (timeOfDay >= 15 && timeOfDay < 18) {
            binding.tvGreeting.setText("Halo, Selamat Sore")
        } else if (timeOfDay >= 18 && timeOfDay < 24) {
            binding.tvGreeting.setText("Halo, Selamat Malam")
        }
    }

    private fun dataSet(){
        binding.tvDateToday.setText(
            SimpleDateFormat("EEE, dd-MM-yyyy", Locale.getDefault()).format(
                Date()
            ))
        greeting()
        onCategoryFood()

        binding.DivMakanan.setOnClickListener {
            onCategoryFood()
        }
        binding.DivMinuman.setOnClickListener {
            onCategoryDrink()
        }
        binding.DivDessert.setOnClickListener {
            onCategoryDessert()
        }
        binding.DivSnack.setOnClickListener {
            onCategorySnack()
        }
        binding.DivSoon1.setOnClickListener {
            soonFitur()
        }
        binding.DivSoon2.setOnClickListener {
            soonFitur()
        }
    }

    private fun onCategoryFood(){
        Glide.with(this).load(R.drawable.category_food).into(binding.ivMakanan)
        Glide.with(this).load(R.drawable.off_category_drink).into(binding.ivMinuman)
        Glide.with(this).load(R.drawable.off_category_dessert).into(binding.ivDessert)
        Glide.with(this).load(R.drawable.off_category_snack).into(binding.ivSnack)
        Glide.with(this).load(R.drawable.off_category_soon).into(binding.ivSoon1)
        Glide.with(this).load(R.drawable.off_category_soon).into(binding.ivSoon2)
        binding.tvResultCategory.setText("List Makanan")
        binding.rvMakanan.isVisible = true
        binding.rvMinuman.isVisible = false
        binding.rvDessert.isVisible = false
        binding.rvSnack.isVisible = false

        list.clear()

        val food1 = ProductModels(
            R.drawable.off_category_food,
            "Ayam Goreng Mentega",
            "Makanan",
            "Makanan",
            12000
        )
        list.add(food1)

        val food2 = ProductModels(
            R.drawable.off_category_food,
            "Nasi Goreng Oriental",
            "Makanan",
            "Makanan",
            8000
        )
        list.add(food2)

        val food3 = ProductModels(
            R.drawable.off_category_food,
            "Kwetiau Goreng Ayam",
            "Makanan",
            "Makanan",
            10000
        )
        list.add(food3)

        val food4 = ProductModels(
            R.drawable.off_category_food,
            "Sushi Roll Crispy",
            "Makanan",
            "Makanan",
            17000
        )
        list.add(food4)

        binding.rvMakanan.adapter = ProductAdapter(list)
        binding.rvMakanan.layoutManager = GridLayoutManager(this, 2)

    }

    private fun onCategoryDrink(){
        Glide.with(this).load(R.drawable.off_category_food).into(binding.ivMakanan)
        Glide.with(this).load(R.drawable.category_drink).into(binding.ivMinuman)
        Glide.with(this).load(R.drawable.off_category_dessert).into(binding.ivDessert)
        Glide.with(this).load(R.drawable.off_category_snack).into(binding.ivSnack)
        Glide.with(this).load(R.drawable.off_category_soon).into(binding.ivSoon1)
        Glide.with(this).load(R.drawable.off_category_soon).into(binding.ivSoon2)
        binding.tvResultCategory.setText("List Minuman")
        binding.rvMakanan.isVisible = false
        binding.rvMinuman.isVisible = true
        binding.rvDessert.isVisible = false
        binding.rvSnack.isVisible = false

        list.clear()

        val drink1 = ProductModels(
            R.drawable.off_category_drink,
            "Jus",
            "Minuman",
            "Minuman",
            8000
        )
        list.add(drink1)

        val drink2 = ProductModels(
            R.drawable.off_category_drink,
            "Coffe",
            "Minuman",
            "Minuman",
            8000
        )
        list.add(drink2)

        val drink3 = ProductModels(
            R.drawable.off_category_drink,
            "Softdrink",
            "Minuman",
            "Minuman",
            5000
        )
        list.add(drink3)

        val drink4 = ProductModels(
            R.drawable.off_category_drink,
            "Air Mineral",
            "Minuman",
            "Minuman",
            3000
        )
        list.add(drink4)

        binding.rvMinuman.adapter = ProductAdapter(list)
        binding.rvMinuman.layoutManager = GridLayoutManager(this, 2)

    }

    private fun onCategoryDessert(){
        Glide.with(this).load(R.drawable.off_category_food).into(binding.ivMakanan)
        Glide.with(this).load(R.drawable.off_category_drink).into(binding.ivMinuman)
        Glide.with(this).load(R.drawable.category_dessert).into(binding.ivDessert)
        Glide.with(this).load(R.drawable.off_category_snack).into(binding.ivSnack)
        Glide.with(this).load(R.drawable.off_category_soon).into(binding.ivSoon1)
        Glide.with(this).load(R.drawable.off_category_soon).into(binding.ivSoon2)
        binding.tvResultCategory.setText("List Dessert")
        binding.rvMakanan.isVisible = false
        binding.rvMinuman.isVisible = false
        binding.rvDessert.isVisible = true
        binding.rvSnack.isVisible = false

        list.clear()

        val dessert1 = ProductModels(
            R.drawable.off_category_dessert,
            "Puding",
            "Dessert",
            "Dessert",
            10000
        )
        list.add(dessert1)

        val dessert2 = ProductModels(
            R.drawable.off_category_dessert,
            "Es Krim",
            "Dessert",
            "Dessert",
            8000
        )
        list.add(dessert2)

        val dessert3 = ProductModels(
            R.drawable.off_category_dessert,
            "Pai",
            "Dessert",
            "Dessert",
            10000
        )
        list.add(dessert3)

        val dessert4 = ProductModels(
            R.drawable.off_category_dessert,
            "Cake",
            "Dessert",
            "Dessert",
            10000
        )
        list.add(dessert4)

        binding.rvDessert.adapter = ProductAdapter(list)
        binding.rvDessert.layoutManager = GridLayoutManager(this, 2)

    }

    private fun onCategorySnack(){
        Glide.with(this).load(R.drawable.off_category_food).into(binding.ivMakanan)
        Glide.with(this).load(R.drawable.off_category_drink).into(binding.ivMinuman)
        Glide.with(this).load(R.drawable.off_category_dessert).into(binding.ivDessert)
        Glide.with(this).load(R.drawable.category_snack).into(binding.ivSnack)
        Glide.with(this).load(R.drawable.off_category_soon).into(binding.ivSoon1)
        Glide.with(this).load(R.drawable.off_category_soon).into(binding.ivSoon2)
        binding.tvResultCategory.setText("List Snack")
        binding.rvMakanan.isVisible = false
        binding.rvMinuman.isVisible = false
        binding.rvDessert.isVisible = false
        binding.rvSnack.isVisible = true

        list.clear()

        val snack1 = ProductModels(
            R.drawable.off_category_snack,
            "Biskuit",
            "Snack",
            "Snack",
            12000
        )
        list.add(snack1)

        val snack2 = ProductModels(
            R.drawable.off_category_snack,
            "Muffin",
            "Snack",
            "Snack",
            8000
        )
        list.add(snack2)

        val snack3 = ProductModels(
            R.drawable.off_category_snack,
            "Donat",
            "Snack",
            "Snack",
            10000
        )
        list.add(snack3)

        val snack4 = ProductModels(
            R.drawable.off_category_snack,
            "Popcorn",
            "Snack",
            "Snack",
            10000
        )
        list.add(snack4)

        binding.rvSnack.adapter = ProductAdapter(list)
        binding.rvSnack.layoutManager = GridLayoutManager(this, 2)

    }

    private fun soonFitur(){
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Soon Category")
            .setContentText("Category belum tersedia\nStay tune in JavasResto!")
            .setConfirmText("Ya")
            .setConfirmClickListener { sDialog ->
                sDialog.dismissWithAnimation()
            }
            .show()
    }

}