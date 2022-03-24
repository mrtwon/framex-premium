package com.mrtwon.framex_premium.screen.dialogDonat

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.mrtwon.framex_premium.R

class DialogDonate: DialogFragment() {
    private lateinit var clipBoardManager: ClipboardManager
    private val transparencyProperty = 0.5f
    private val defaultTransparency = 1f


    private lateinit var btnLinkQiwi: LinearLayout
    private lateinit var btnDonateQiwi: ImageButton
    private lateinit var btnDonateSber: ImageButton
    private lateinit var btnDonateVisa: ImageButton
    private lateinit var layoutVisa: LinearLayout
    private lateinit var layoutQiwi: LinearLayout
    private lateinit var layoutSber: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        clipBoardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.fragment_donate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btnLinkQiwi = view.findViewById(R.id.action_pay_with_qiwi_link)
        layoutQiwi = view.findViewById(R.id.layout_action_pay_qiwi)
        layoutSber = view.findViewById(R.id.layout_action_pay_sber)
        layoutVisa = view.findViewById(R.id.layout_action_pay_visa)
        btnDonateQiwi = view.findViewById(R.id.donat_with_qiwi)
        btnDonateSber = view.findViewById(R.id.donat_with_sber)
        btnDonateVisa = view.findViewById(R.id.donat_with_visa)
        btnDonateVisa.setOnClickListener{ btn ->
            onClickDonate(btn)
            hidePayLayout()
            layoutVisa.visibility = View.VISIBLE
            copyToClip(PaymentMethod.Visa())
        }
        btnDonateSber.setOnClickListener{ btn ->
            onClickDonate(btn)
            hidePayLayout()
            layoutSber.visibility = View.VISIBLE
            copyToClip(PaymentMethod.Sber())
        }
        btnDonateQiwi.setOnClickListener{ btn ->
            onClickDonate(btn)
            hidePayLayout()
            layoutQiwi.visibility = View.VISIBLE
            copyToClip(PaymentMethod.Qiwi())
        }
        btnLinkQiwi.setOnClickListener{
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://qiwi.com/n/MRTWON")
            })
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun onClickDonate(v: View){
        btnTransparency()
        v.alpha = defaultTransparency
    }

    private fun btnTransparency(){
        btnDonateVisa.alpha = transparencyProperty
        btnDonateQiwi.alpha = transparencyProperty
        btnDonateSber.alpha = transparencyProperty
    }
    private fun hidePayLayout(){
        layoutVisa.visibility = View.GONE
        layoutSber.visibility = View.GONE
        layoutQiwi.visibility = View.GONE
    }

    private fun copyToClip(method: PaymentMethod){
        val clipData = ClipData.newPlainText("Requisites", method.data)
        clipBoardManager.setPrimaryClip(clipData)
        Toast.makeText(requireContext(), "Скопированно", Toast.LENGTH_SHORT).show()
    }

    sealed class PaymentMethod(val data: String){
        class Qiwi(mData: String = "+79643852014") : PaymentMethod(mData)
        class Sber(mData: String = "+79643852014") : PaymentMethod(mData)
        class Visa(mData: String = "4817 7602 1210 4008") : PaymentMethod(mData)
    }
}