package com.example.retrofit

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofit.RetrofitInstance
import com.example.retrofit.TodoAdapter
import com.example.retrofit.databinding.ActivityMainBinding
import retrofit2.HttpException
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private lateinit var todoAdapter: TodoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        lifecycleScope.launchWhenCreated {
            binding.progressBar.isVisible=true
            val response = try {
                RetrofitInstance.api.getTodos()
            }catch (e:IOException){
                Log.e("MainActivity", "IOException,u may not have internet connection" )
                binding.progressBar.isVisible=false
                return@launchWhenCreated
            }catch (e:HttpException){
                Log.e("MainActivity", "HttpException,unexpected response" )
                binding.progressBar.isVisible=false
                return@launchWhenCreated
            }
            if (response.isSuccessful &&response.body()!=null){
                todoAdapter.todos=response.body()!!
            }else{
                Log.e(TAG, "Response not successful" )
            }
            binding.progressBar.isVisible=false
        }

    }
    private fun setupRecyclerView()=binding.rvTodos.apply {
        todoAdapter= TodoAdapter()
        adapter= todoAdapter
        layoutManager=LinearLayoutManager(this@MainActivity)
    }
}


