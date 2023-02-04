import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.primo2.PostInfo
import com.example.primo2.activity.MainActivity
import com.example.primo2.screen.Post
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore

import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DatePlanViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    var datePlanName:String? = null
    set(value){
        savedStateHandle.set("datePlanName",value)
    }

    init{
        savedStateHandle.get<String>("datePlanName")?.run{
            datePlanName = this
        }
    }
}