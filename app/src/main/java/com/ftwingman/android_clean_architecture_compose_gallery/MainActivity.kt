package com.ftwingman.android_clean_architecture_compose_gallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ftwingman.android_clean_architecture_compose_gallery.domain.model.Photo
import com.ftwingman.android_clean_architecture_compose_gallery.domain.model.User
import com.ftwingman.android_clean_architecture_compose_gallery.presentation.photo_list.PhotoListScreen
import com.ftwingman.android_clean_architecture_compose_gallery.presentation.photo_list.components.PhotoItem
import com.ftwingman.android_clean_architecture_compose_gallery.ui.theme.AndroidcleanarchitecturecomposegalleryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidcleanarchitecturecomposegalleryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        PhotoListScreen(viewModel = hiltViewModel())
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun PhotoItemPreview() {
    AndroidcleanarchitecturecomposegalleryTheme {
        PhotoItem(
            photo = Photo(
                id = "1",
                width = 100,
                height = 100,
                url = "https://pic.616pic.com/ys_bnew_img/00/20/31/PfCAgYoVAA.jpg",
                blurHash = null,
                description = "A beautiful sunset",
                author = User(
                    id = "u1",
                    username = "johndoe",
                    name = "John Doe",
                    profileImage = "https://pic.616pic.com/ys_bnew_img/00/20/31/PfCAgYoVAA.jpg"
                )
            ),
            modifier = Modifier.width(200.dp)
        )
    }
}