import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.primo2.PostInfo
import com.example.primo2.activity.MainActivity
import com.google.firebase.firestore.ktx.firestore

import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PostViewModel : ViewModel() {

    private val _postState = MutableStateFlow(ArrayList<PostInfo>())
    val postState: StateFlow<ArrayList<PostInfo>> = _postState.asStateFlow()

    var test:PostInfo = PostInfo()

    fun updatePostInformation() {
        var postList2: ArrayList<PostInfo> = arrayListOf()
        val db = Firebase.firestore
        var count:Int = 0
        db.collection("posts")
            .get()
            .addOnSuccessListener { documents ->
                for (pDocument in documents) {
                    postList2.add(
                        PostInfo(
                            pDocument.getString("title"),
                            pDocument.data["contents"] as ArrayList<String?>,
                            pDocument.data["format"] as ArrayList<String?>,
                            pDocument.get("comments").toString(),
                            pDocument.getString("writer"),
                            pDocument.getString("postDate")
                        )
                    )
                }
                _postState.update { currentState ->
                    postList2
                }
            }
            .addOnFailureListener { exception ->
            }
    }
}