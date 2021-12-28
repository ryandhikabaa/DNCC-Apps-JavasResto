package com.teamwork.javasresto.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.teamwork.javasresto.databinding.ActivityDetailBinding
import android.content.pm.PackageInfo

class DetailActivity : AppCompatActivity() {

    private  lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        if (bundle != null) {
            binding.ivDetail.setImageResource(bundle.getInt("image"))
            binding.tvName.setText(bundle.getString("name"))
            binding.tvKategoriProduct.setText(bundle.getString("kat"))
            binding.tvDeskripsiProduct.setText(bundle.getString("ket"))
            binding.tvhargaProduct.setText("Rp " + bundle.getString("price"))
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.cvPesan.setOnClickListener {

            val mobileNumber = 85291412003
            val message = "Isi pesan"

            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+"+62"+mobileNumber + "&text="+message))
            startActivity(intent)

        }

    }


}