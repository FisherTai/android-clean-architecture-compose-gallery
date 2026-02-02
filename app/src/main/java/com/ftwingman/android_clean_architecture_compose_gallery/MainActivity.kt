package com.ftwingman.android_clean_architecture_compose_gallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ftwingman.android_clean_architecture_compose_gallery.domain.model.Photo
import com.ftwingman.android_clean_architecture_compose_gallery.domain.model.User
import com.ftwingman.android_clean_architecture_compose_gallery.presentation.navigation.Route
import com.ftwingman.android_clean_architecture_compose_gallery.presentation.photo_detail.PhotoDetailScreen
import com.ftwingman.android_clean_architecture_compose_gallery.presentation.photo_list.PhotoListScreen
import com.ftwingman.android_clean_architecture_compose_gallery.presentation.photo_list.components.PhotoItem
import com.ftwingman.android_clean_architecture_compose_gallery.ui.theme.AndroidcleanarchitecturecomposegalleryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalSharedTransitionApi::class, androidx.compose.material3.ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val systemInDarkTheme = isSystemInDarkTheme()
            var isDarkTheme by remember { mutableStateOf(systemInDarkTheme) }

            AndroidcleanarchitecturecomposegalleryTheme(darkTheme = isDarkTheme) {
                val navController = rememberNavController()
                val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Text(
                                    text = "Infinite Muse",
                                    style = MaterialTheme.typography.titleLarge
                                )
                            },
                            actions = {
                                IconButton(onClick = { isDarkTheme = !isDarkTheme }) {
                                    Icon(
                                        imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                        contentDescription = "Toggle Theme"
                                    )
                                }
                            },
                            scrollBehavior = scrollBehavior
                        )
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        SharedTransitionLayout {
                            NavHost(
                                navController = navController,
                                startDestination = Route.PhotoList
                            ) {
                                composable<Route.PhotoList> {
                                    PhotoListScreen(
                                        viewModel = hiltViewModel(),
                                        onPhotoClick = { photo ->
                                            navController.navigate(Route.PhotoDetail(photo.id, photo.thumbnailUrl))
                                        },
                                        animatedVisibilityScope = this,
                                        sharedTransitionScope = this@SharedTransitionLayout
                                    )
                                }
                                composable<Route.PhotoDetail> {
                                    PhotoDetailScreen(
                                        viewModel = hiltViewModel(),
                                        onBackClick = {
                                            navController.popBackStack()
                                        },
                                        animatedVisibilityScope = this,
                                        sharedTransitionScope = this@SharedTransitionLayout
                                    )
                                }
                            }
                        }
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
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PhotoItemPreview() {
    AndroidcleanarchitecturecomposegalleryTheme {
        PhotoItem(
            photo = Photo(
                id = "1",
                width = 100,
                height = 100,
                url = "https://pic.616pic.com/ys_bnew_img/00/20/31/PfCAgYoVAA.jpg",
                thumbnailUrl = "https://pic.616pic.com/ys_bnew_img/00/20/31/PfCAgYoVAA.jpg",
                color = "#E0E0E0",
                blurHash = null,
                description = "A beautiful sunset",
                author = User(
                    id = "u1",
                    username = "johndoe",
                    name = "John Doe",
                    profileImage = "https://pic.616pic.com/ys_bnew_img/00/20/31/PfCAgYoVAA.jpg"
                ),
                exif = null
            ),
            onPhotoClick = {},
            modifier = Modifier.width(200.dp)
        )
    }
}