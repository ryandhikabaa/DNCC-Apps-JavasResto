package com.teamwork.javasresto.activity

import Config
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.teamwork.javasresto.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private  lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            private fun doNothing() {}
            override fun onClick(v: View) {
                onBackPressed()
            }
        })

        val bundle = intent.extras
        if (bundle != null) {
            binding.toolbar.setTitle(bundle.getString("name"))
            binding.ivDetail.setImageResource(bundle.getInt("image"))
            binding.etHarga.setText("Rp " + bundle.getString("price"))
            binding.etKategori.setText(bundle.getString("kat"))
            binding.etDeskripsi.setText(bundle.getString("ket"))
        }

        binding.tvBook.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Mohon Maaf")
                .setContentText("Mohon maaf keranjang pesanan dalam pengembangan\nPemesanan dapat dipesan langsung melalui WhatsApp dengan klik lanjutkan")
                .setConfirmText("Lanjutkan")
                .setConfirmClickListener { sDialog ->
                    val sp = getSharedPreferences(Config::SHARED_PREF_NAME.toString(), MODE_PRIVATE)
                    val sEmail = sp.getString(Config::LOGIN_EMAIL_SHARED_PREF.toString(), "")
                    val sName = sp.getString(Config::LOGIN_NAME_SHARED_PREF.toString(), "")
                    val mobileNumber = 85740151761
                    val message = "Pemesan :\nNama : " + sName + "\nEmail : " + sEmail + "\nDaftar Pesanan (isi menu yang anda pilih) :\n"

                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+"+62"+mobileNumber + "&text="+message))
                    startActivity(intent)
                    sDialog.dismissWithAnimation()
                }
                .setCancelText("Batal")
                .setCancelClickListener { sDialog ->
                    sDialog.dismissWithAnimation()
                }
                .show()



        }

    }


}