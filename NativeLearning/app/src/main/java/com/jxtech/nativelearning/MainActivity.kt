package com.jxtech.nativelearning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.jxtech.nativelearning.databinding.ActivityMainBinding
import com.jxtech.nativelib.NativeLib

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Example of a call to a native method
        binding.sampleText.text = stringFromJNI() + "\n" + NativeLib().stringFromJNI()
    }

    /**
     * A native method that is implemented by the 'nativelearning' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'nativelearning' library on application startup.
        init {
            System.loadLibrary("nativelearning")
        }
    }
}