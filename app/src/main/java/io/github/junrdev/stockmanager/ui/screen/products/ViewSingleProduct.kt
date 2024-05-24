package io.github.junrdev.stockmanager.ui.screen.products

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import io.github.junrdev.stockmanager.Constants
import io.github.junrdev.stockmanager.R
import io.github.junrdev.stockmanager.adapter.AddProductCategoryRecyclerAdapter
import io.github.junrdev.stockmanager.data.local.ProductRepository
import io.github.junrdev.stockmanager.model.CategoryItem
import io.github.junrdev.stockmanager.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewSingleProduct : Fragment() {

    lateinit var productname: TextInputEditText
    lateinit var productPrice: TextInputEditText
    lateinit var stock: TextInputEditText
    lateinit var deleteProduct: CardView
    lateinit var saveProduct: CardView
    lateinit var editImage: CardView
    lateinit var productCategoryRecycler: RecyclerView
    lateinit var selectedImage: ImageView
    lateinit var productCategoryRecyclerAdapter: AddProductCategoryRecyclerAdapter
    lateinit var saveedittext: TextView
    lateinit var product: Product


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_single_product, container, false)

        view.apply {
            productname = findViewById(R.id.productname)
            productPrice = findViewById(R.id.productprice)
            stock = findViewById(R.id.productstock)
            editImage = findViewById(R.id.addImage)
            saveProduct = findViewById(R.id.saveProduct)
            deleteProduct = findViewById(R.id.deleteProduct)
            productCategoryRecycler = findViewById(R.id.productCategoryRecycler)
            selectedImage = findViewById(R.id.selectedImage)
            deleteProduct = findViewById(R.id.deleteProduct)
            saveedittext = findViewById(R.id.saveedittext)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val productId = arguments?.getLong("pid")
        Log.d(TAG, "onViewCreated: Product id $productId")
        val productRepository = ProductRepository(requireContext())

//        productId?.let {
        if (productId != null) {
            CoroutineScope(Dispatchers.IO).launch {
                product = productRepository.getProductById(productId)

                withContext(Dispatchers.Main) {
                    productname.setText(product.productName)
                    productPrice.setText("${product.price}")
                    stock.setText("${product.stockCount}")
                    Glide.with(requireContext())
                        .load(product.image)
                        .into(selectedImage)
                    productCategoryRecyclerAdapter =
                        AddProductCategoryRecyclerAdapter(categories = product.category.map {
                            CategoryItem(it)
                        }.toMutableList())
                }
            }
            deleteProduct.setOnClickListener {
                // show warning dialog
                val dialog = AlertDialog.Builder(requireContext())
                    .apply {
                        setTitle("Alert")
                        setMessage("You are about to delete $productId from your stock.")

                        setPositiveButton("Continue") { dialog, which ->
                            CoroutineScope(Dispatchers.IO).launch {
                                productRepository.deleteProduct(
                                    product = productRepository.getProductById(
                                        productId!!
                                    )
                                )

                                withContext(Dispatchers.Main) {
                                    requireActivity().supportFragmentManager.popBackStack()
                                }
                            }
                        }
                        setNegativeButton("Cancel") { dialog, which ->
                            dialog.dismiss()
                        }

                        setIcon(requireContext().getDrawable(R.drawable.round_info_24))
                    }

                dialog.create().show()
            }
        }

        editImage.setOnClickListener {
            val pickNewPhoto = Intent(Intent.ACTION_PICK)
                .apply {
                    type = "image/**"
                }
            startActivityForResult(pickNewPhoto, Constants.FILE_PICK_CODE)

        }
        saveProduct.setOnClickListener {

            if (saveedittext.text.toString().equals("Edit")) {
                saveedittext.setText("Save")
            } else {

                product.apply {
                    productName = productname.text.toString()
                    stockCount = stock.text.toString().toLong()
                    price = productPrice.text.toString().toDouble()
                }

                CoroutineScope(Dispatchers.IO).launch {
                    productRepository.updateProduct(product)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Product updated.", Toast.LENGTH_SHORT)
                            .show()
                        delay(1000)
                        requireActivity().finish()
                    }
                }
            }
        }

        val pname = arguments?.getString("pname")
        if (pname != null) {
            CoroutineScope(Dispatchers.IO).launch {
                val product = productRepository.getProductByName(pname)

                withContext(Dispatchers.Main) {
                    productname.setText(product.productName)
                    productPrice.setText("${product.price}")
                    stock.setText("${product.stockCount}")
                    Glide.with(requireContext())
                        .load(product.image)
                        .into(selectedImage)
                    productCategoryRecyclerAdapter =
                        AddProductCategoryRecyclerAdapter(categories = product.category.map {
                            CategoryItem(it)
                        }.toMutableList())
                }

            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == -1 && requestCode == Constants.FILE_PICK_CODE) {

            val newImage = data?.data!!

            Glide.with(requireContext())
                .load(newImage)
                .into(selectedImage)

        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        requireActivity().menuInflater.inflate(R.menu.viewproductmenu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.editproduct -> {
                Log.d(TAG, "onOptionsItemSelected: edit product")
            }
        }

        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val TAG = "ViewSingleProduct"
    }

}