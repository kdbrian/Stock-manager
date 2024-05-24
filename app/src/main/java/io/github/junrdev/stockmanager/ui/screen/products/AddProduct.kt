package io.github.junrdev.stockmanager.ui.screen.products

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import io.github.junrdev.stockmanager.Constants
import io.github.junrdev.stockmanager.MainActivity
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

class AddProduct : Fragment() {

    private lateinit var imageUri: Uri
    lateinit var selectedImage: ImageView
    lateinit var productRepository: ProductRepository
    private val categories: MutableList<CategoryItem> = mutableListOf()

    lateinit var productname: TextInputEditText
    lateinit var price: TextInputEditText
    lateinit var stock: TextInputEditText
    lateinit var addCategory: CardView
    lateinit var saveProduct: CardView
    lateinit var addImage: CardView
    lateinit var productCategoryRecycler: RecyclerView
    lateinit var addProductCategoryRecyclerAdapter: AddProductCategoryRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_product, container, false)

        view.apply {
            productname = findViewById(R.id.productname)
            price = findViewById(R.id.productprice)
            stock = findViewById(R.id.productstock)
            addCategory = findViewById(R.id.addcategory)
            addImage = findViewById(R.id.addImage)
            saveProduct = findViewById(R.id.saveProduct)
            productCategoryRecycler = findViewById(R.id.productCategoryRecycler)
            selectedImage = findViewById(R.id.selectedImage)

        }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addProductCategoryRecyclerAdapter = AddProductCategoryRecyclerAdapter(categories)
        productCategoryRecycler.adapter = addProductCategoryRecyclerAdapter
        productRepository = ProductRepository(requireContext())
        addCategory.setOnClickListener {
            showCategoryAdder()
        }

        addImage.setOnClickListener {
            selectImage()
        }

        saveProduct.setOnClickListener {
            saveProductItem()
        }
    }

    private fun saveProductItem() {
        val itemcats = categories.map { it -> it.text }.joinToString("-")
        Log.d(TAG, "saveProductItem: $itemcats")
        val product = Product(
            productName = productname.text.toString(),
            category = categories.map { it.text }.toList(),
            image = imageUri.toString(),
            stockCount = stock.text.toString().toLong(),
            price = price.text.toString().toDouble()
        )

        productRepository.insertProduct(product) {
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.Main) {
                    Snackbar.make(requireView(), "Product added", Snackbar.LENGTH_SHORT)
                        .apply {
                            //set other items
                        }
                        .show()
                    delay(2000)
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finish()
                }
            }
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/**"
        startActivityForResult(intent, Constants.FILE_PICK_CODE)
    }

    private fun showCategoryAdder() {
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(R.layout.addcategoryitem)

        dialog.apply {
            val catName: TextInputEditText = findViewById(R.id.categoryName)!!
            val saveBtn: CardView = findViewById(R.id.saveCategory)!!

            saveBtn.setOnClickListener {
                if (catName.text?.isNotEmpty()!!) {
                    if (categories.filter { it.text == catName?.text.toString() }.isEmpty()) {
                        categories.add(CategoryItem(catName?.text.toString()))
                        addProductCategoryRecyclerAdapter.notifyItemInserted(categories.size)
                    }
                    dialog.dismiss()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Provide a valid category name.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

        }

        dialog.setOnDismissListener(DialogInterface::dismiss)
        dialog.show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == -1 && requestCode == Constants.FILE_PICK_CODE && data != null) {
            imageUri = data.data!!
            Glide.with(requireContext())
                .load(imageUri)
                .into(selectedImage)
        }
    }

    companion object {
        private const val TAG = "AddProduct"
    }

}