package com.example.filetransfer.main

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.filetransfer.BuildConfig
import com.example.filetransfer.R
import com.example.filetransfer.adapter.ApkAdapter
import com.example.filetransfer.adapter.ApkDataClass
import com.example.filetransfer.tools.Constant
import com.example.filetransfer.tools.Tools
import java.io.File
import java.util.*


class ApkFragment : Fragment() {

    private lateinit var apkArrayList: ArrayList<String>
    private lateinit var idRecyclerView: RecyclerView
    private lateinit var fragmentView: View
    private lateinit var apkAdapter: ApkAdapter
    private lateinit var idFabOptions: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apkArrayList = ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_apk, container, false)
        idRecyclerView = fragmentView.findViewById(R.id.id_rv_apk)
        idFabOptions = fragmentView.findViewById(R.id.id_fab_options)
        apkAdapter = ApkAdapter(requireContext(), idRecyclerView)

        checkForClickListeners()

        fabButtonClickedListener()

        getListOfApps()
        return fragmentView
    }

    private fun fabButtonClickedListener() {
        idFabOptions.setOnClickListener {
            Tools.popUpWindow(requireContext(), fragmentView) {
                val share = it.findViewById<TextView>(R.id.id_iv_share)
                share.setOnClickListener { shareToOtherApps() }
            }
        }
    }

    private fun shareToOtherApps() {
        if (apkArrayList.isNotEmpty()) {
//            val intent = Intent()
//            intent.type = "application"
//            intent.action = Intent.ACTION_SEND
//            val uri = requireContext().getExternalFilesDirs(apkArrayList[0])
//            Tools.debugMessage(uri.toString())
////            val uri = FileProvider.getUriForFile(
////                requireContext(),
////                BuildConfig.APPLICATION_ID + ".provider",
////                File(apkArrayList[0])
////            )
//            intent.putExtra(
//                Intent.EXTRA_STREAM,
//                uri
//            )
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//            requireContext().startActivity(Intent.createChooser(intent, "Share APK"))


            val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            val uriList: ArrayList<Uri> = ArrayList()
            apkArrayList.forEach { path ->
                val file = File(path)
                val uri = FileProvider.getUriForFile(
                    requireContext(),
                    Constant.APPLICATION_AUTHORITY,
                    file
                )
                uriList.add(uri)
                intent.setDataAndType(uri, "application/vnd.android.package-archive")
            }
            intent.putExtra(Intent.EXTRA_STREAM, uriList)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(Intent.createChooser(intent, "Share APK"))
        } else {
            Tools.showToast(requireContext(), "You did not select anything")
        }
    }

    private fun checkForClickListeners() {
        apkAdapter.clickListener(object : ApkAdapter.OnClickListener {
            override fun onItemClick(position: Int, view: View) {
                val apkicon = view.findViewById<ImageView>(R.id.id_iv_apk_icon)
                val apkname = view.findViewById<TextView>(R.id.id_tv_apk_name)
                val apklocation = view.findViewById<TextView>(R.id.id_tv_apk_path)
                val apklayout = view.findViewById<CardView>(R.id.id_card_apk)
                apklayout.setCardBackgroundColor(Color.GRAY)
                if (!apkArrayList.contains(apklocation.text)){
                    apkArrayList.add(apklocation.text.toString())
                }
            }
            override fun onItemLongClick(position: Int, view: View) {
            }
        })
    }

    private fun getListOfApps(): ArrayList<ApkDataClass> {
        val packageManager = context?.applicationContext?.packageManager
        //get list of apks
        val packages = packageManager?.getInstalledApplications(PackageManager.GET_META_DATA)
        packages?.forEach { packageInfo ->
            val name = packageInfo.packageName
            val icon = packageManager.getApplicationIcon(packageInfo)
            val apkPath = packageInfo.sourceDir
            val apkSize = File(apkPath).length()
            Tools.debugMessage(name)
            apkAdapter.addToAdapter(
                ApkDataClass(
                    name = name.split(".").last(),
                    icon = icon,
                    apk_path = apkPath,
                    apk_size = apkSize
                )
            )
        }
        apkAdapter.getArrayList().sortWith(Comparator { apk1, apk2 ->
            return@Comparator apk1.name.lowercase().compareTo(apk2.name.lowercase())
        })
        return apkAdapter.getArrayList()
    }
}