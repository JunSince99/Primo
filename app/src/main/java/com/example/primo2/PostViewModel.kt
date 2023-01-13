import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.primo2.PostInfo
import com.example.primo2.activity.MainActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore

import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {
    private val _postState = MutableStateFlow(ArrayList<PostInfo>())
    val postState: StateFlow<ArrayList<PostInfo>> = _postState.asStateFlow()
    var postList2: ArrayList<PostInfo> = arrayListOf()
    var isUpdate:Boolean = false
    fun updatePostInformation() {
        postList2.clear()
        val db = Firebase.firestore
        var count:Int = 0
            db.collection("posts").orderBy("postDate", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { documents ->

                    for (pDocument in documents) {
                        postList2.add(
                            PostInfo(
                                pDocument.id,
                                pDocument.getString("title"),
                                pDocument.data["contents"] as ArrayList<String?>,
                                pDocument.data["format"] as ArrayList<String?>,
                                pDocument.get("comments").toString(),
                                pDocument.getString("writer"),
                                pDocument.getString("writerID"),
                                pDocument.getString("postDate"),
                                pDocument.data["like"] as HashMap<String, Boolean>,
                            )
                        )
                    }
                    _postState.update { currentState ->
                        isUpdate = false
                        postList2
                    }
                }
                .addOnFailureListener { exception ->
                }
        }

}