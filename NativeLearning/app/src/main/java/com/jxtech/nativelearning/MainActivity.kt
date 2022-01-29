package com.jxtech.nativelearning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.jxtech.nativelearning.databinding.ActivityMainBinding
import com.jxtech.nativelib.NativeLib

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val primitiveTypes = PrimitiveTypes()
    private val fieldsAndMethods = FieldsAndMethods();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Example of a call to a native method
        binding.sampleText.text = stringFromJNI() + "\n" +
                NativeLib().stringFromJNI() + "\n" +
                callPlus() + "\n" +
                callSumOfFloatArray() + "\n\n" +
                callChangeName()

        binding.btnExceptions.setOnClickListener {
            Exceptions.run()
        }
    }

    private fun callChangeName(): String {
        return fieldsAndMethods.callChangeName()
    }

    private fun callSumOfFloatArray(): String {
        val array = arrayOf(1.3f, 2.8f, 3.3f).toFloatArray()
        val sum = primitiveTypes.sumOfFloatArray(array = array)
        return "sum of array is $sum"
    }

    private fun callPlus() =
        "10 + 4 = ${primitiveTypes.plus(10, 4)}"

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