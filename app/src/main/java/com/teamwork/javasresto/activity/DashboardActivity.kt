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
            R.drawable.gudeg,
            "Gudeg",
            "Makanan",
            "Gudeg menjadi makanan khas jawa yang juga sangat terkenal di Indonesia, bahkan dunia. Menu ini sendiri sudah menjadi makanan favorit masyarakat Yogya sejak 1956, lho. Makanan ikonik ini punya tiga jenis gudeg, yaitu Gudeg Basah, Gudeg Maggar dan Gudeg Kering.",
            12000
        )
        list.add(food1)

        val food2 = ProductModels(
            R.drawable.megono,
            "Nasi Megono",
            "Makanan",
            "Nasi Megono adalah makanan khas Jawa khususnya Pekolangan. Makanan ini seringkali ditawarkan sebagai menu sarapan lho, jadi bisa banget kamu menawarkan ini kepada orang-orang yang memang hobi makan nasi di pagi hari sebelum beraktivitas.",
            10000
        )
        list.add(food2)

        val food3 = ProductModels(
            R.drawable.rawon,
            "Rawon",
            "Makanan",
            "Membicarakan tentang makanan khas Jawa, tidak lengkap jika tak memasukkan Rawon ke dalam daftar. Rawon sendiri adalah pilihan kuliner dari Jawa Timur, yang di dalamnya berisi irisan daging sapi empuk, dengan kuah hitam, serta kecambah mentah sebagai topping.",
            10000
        )
        list.add(food3)

        val food4 = ProductModels(
            R.drawable.tahutek,
            "Tahu Tek",
            "Makanan",
            "Sajian yang begitu menggugah selera ini berasal dari Surabaya, Jawa Timur, dengan siraman bumbu yang banyak, lontong, tahu, toge kecil dan kecambah sebagai bahan utamanya. Tahu tek sendiri adalah tahu goreng setengah matang dan lontong yang dipotong kecil, lalu disiram bumbu petis.",
            15000
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
            R.drawable.kopijos,
            "Kopi Joss",
            "Minuman",
            "kopi ini tidak hanya dikenal luas oleh masyarakat Indonesia lho, tetapi mancanegara juga. Kalau di luar negeri, orang-orang biasa menyebutnya The Charcoal Coffee. Penyajian kopi ini cukup nyentrik dan menarik dari kopi lainnya, di mana para penjual Kopi Joss biasanya menggunakan ketel atau teko besar sebagai wadah dan memasaknya menggunakan arang.",
            8000
        )
        list.add(drink1)

        val drink2 = ProductModels(
            R.drawable.birpletok,
            "Bir Pletok",
            "Minuman",
            "Bir Pletok sebetulnya merupakan minuman asli dari tanah Betawi. Namun, minuman satu ini juga lazim ditemukan di sejumlah daerah di Jawa Tengah, sehingga masih bisa disebut minuman khas Jawa Tengah. Walaupun bernama ‘bir’, minuman satu ini tak akan membuat kamu mabuk.",
            8000
        )
        list.add(drink2)

        val drink3 = ProductModels(
            R.drawable.bajigur,
            "Bajigur",
            "Minuman",
            "Minuman ini sangat enak diseruput selagi hangat, serta ditemani sejumlah camilan misalnya pisang atau ubi rebus. Di Jawa Tengah, ada satu tempat yang cocok menyeruput minuman ini. tempat tersebut adalah Warung Bajigur Jae Iwan di daerah Sitanggal, Kecamatan, Larangan, Kabupaten Brebes, Jawa Tengah.",
            5000
        )
        list.add(drink3)

        val drink4 = ProductModels(
            R.drawable.wedanguwuh,
            "Wedang Uwuh",
            "Minuman",
            "Tak hanya di Jawa Tengah, minuman satu ini juga bisa kamu temukan di Jogjakarta. Seperti bir pletok dan bajigur, minuman satu ini juga dibuat dari banyak rempah, serta mampu menghangatkan badan. Bahan-bahan tersebut adalah jahe, cengkeh, kayu manis, kayu secang, pala, kapulaga, gula batu, dan pala.",
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
            R.drawable.bakpia,
            "Bakpia",
            "Dessert",
            "Bakpia merupakan kue khas Yogyakarta yang paling terkenal di Jawa Tengah. Bakpia terbuat dari campuran kacang hijau dan gula yang dibungkus dengan adonan pia lalu dipanggang hingga matang. Bakpia bisa kamu temui hampir di semua toko pusat oleh-oleh khas Yogyakarta. Rasanya tidak terlalu manis dengan tekstur yang cukup empuk.",
            10000
        )
        list.add(dessert1)

        val dessert2 = ProductModels(
            R.drawable.serabi,
            "Surabi Solo",
            "Dessert",
            "Surabi Solo dibuat menggunakan tepung beras. Surabi Solo juga menambahkan santan kelapa encer dalam surabi yang sudah setengah matang sehingga warna pada bagian atasnya terlihat lebih putih. Selain itu, surabi Solo hanya disantap begitu saja karena rasanya lebih gurih. ",
            8000
        )
        list.add(dessert2)

        val dessert3 = ProductModels(
            R.drawable.cenil,
            "Cenil",
            "Dessert",
            "Cenil atau cetil terbuat dari pati ketela pohon yang dibentuk bulat-bulat kecil lalu diberi pewarna sebelum direbus hingga matang. Cenil disajikan dalam mangkuk yang terbuat dari daun pisang beserta taburan kelapa dan gula pasir.",
            10000
        )
        list.add(dessert3)

        val dessert4 = ProductModels(
            R.drawable.jadah,
            "Jadah Manten",
            "Dessert",
            "Jadah manten terbuat dari beras ketan yang diisi dengan suwiran daging ayam atau sapi yang sudah dibumbui dengan aneka rempah-rempah lalu dibungkus dengan dadar gulung dan dijepit dengan menggunakan capitan bambu yang bentuknya seperti sumpit kemudian dibakar di atas arang hingga matang. Jadah manten ini disajikan bersama dengan santan kental yang gurih.",
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
            R.drawable.lumpia,
            "Lumpia",
            "Snack",
            "Lumpia terbuat dari lapisan gandum yang berisi sayuran dan suiran daging ayam atau hewan laut. Lumpia mempunyai 2 jenis yaitu lumpia basah dan lumpia kering yang perbedaannya terletak pada lapisan gandumnya saja.",
            12000
        )
        list.add(snack1)

        val snack2 = ProductModels(
            R.drawable.carabikan,
            "Carabikang",
            "Snack",
            "Kue carabikang atau cara bikan termasuk kue manis-gurih yang mengenyangkan karena dibuat dari tepung beras. Kue basah ini memiliki tekstur agak berserat namun lembut, cocok untuk menemani minum teh di pagi hari.",
            8000
        )
        list.add(snack2)

        val snack3 = ProductModels(
            R.drawable.pukis,
            "Pukis",
            "Snack",
            "Pukis adalah satu snack sedap Jawa yang legendaris. Seperti putu ayu, teksturnya empuk. Rasanya juga manis. Pukis merupakan makanan khas Banyumas yang sekarang telah menyebar ke hampir seluruh wilayah Indonesia.",
            10000
        )
        list.add(snack3)

        val snack4 = ProductModels(
            R.drawable.putu,
            "Putu Ayu",
            "Snack",
            "Putu ayu juga merupakan kudapan Jawa yang harus Anda nikmati. Rasanya manis-gurih dan tidak berat di perut. Bentuknya juga cantik dengan warna-warni yang menawan.",
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