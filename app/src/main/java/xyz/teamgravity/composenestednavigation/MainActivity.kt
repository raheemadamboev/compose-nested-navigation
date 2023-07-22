package xyz.teamgravity.composenestednavigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import xyz.teamgravity.composenestednavigation.ui.theme.ComposeNestedNavigationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeNestedNavigationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val controller = rememberNavController()

                    NavHost(
                        navController = controller,
                        startDestination = "auth"
                    ) {
                        navigation(
                            route = "auth",
                            startDestination = "login"
                        ) {
                            composable(route = "login") { entry ->
                                val viewmodel = entry.sharedViewModel<AuthViewModel>(controller)
                                GenericScreen(
                                    text = "Login",
                                    onClick = {
                                        controller.navigate(
                                            route = "profile",
                                            builder = {
                                                popUpTo(
                                                    route = "auth",
                                                    popUpToBuilder = {
                                                        inclusive = true
                                                    }
                                                )
                                            }
                                        )
                                    }
                                )
                            }
                            composable(route = "register") { entry ->
                                val viewmodel = entry.sharedViewModel<AuthViewModel>(controller)
                                GenericScreen(
                                    text = "Register",
                                    onClick = {

                                    }
                                )
                            }
                            composable(route = "forgot_password") { entry ->
                                val viewmodel = entry.sharedViewModel<AuthViewModel>(controller)
                                GenericScreen(
                                    text = "Forgot Password",
                                    onClick = {
                                        controller.navigate("about")
                                    }
                                )
                            }
                        }
                        navigation(
                            route = "profile",
                            startDestination = "settings"
                        ) {
                            composable(route = "settings") {
                                GenericScreen(
                                    text = "Settings",
                                    onClick = {
                                        controller.navigate("forgot_password")
                                    }
                                )
                            }
                            composable(route = "goals") {
                                GenericScreen(
                                    text = "Goals",
                                    onClick = {

                                    }
                                )
                            }
                        }
                        composable(route = "about") {
                            GenericScreen(
                                text = "About",
                                onClick = {

                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GenericScreen(
    text: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = text)
        Button(onClick = onClick) {
            Text(text = "Click me")
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(controller: NavController): T {
    val route = destination.parent?.route ?: return viewModel()
    val entry = remember(this) { controller.getBackStackEntry(route) }
    return viewModel(viewModelStoreOwner = entry)
}

class AuthViewModel : ViewModel()