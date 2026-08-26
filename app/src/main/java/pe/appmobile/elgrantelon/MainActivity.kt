package pe.appmobile.elgrantelon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pe.appmobile.elgrantelon.ui.navigation.ElGranTelonNavHost
import pe.appmobile.elgrantelon.ui.theme.ElGranTelonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ElGranTelonTheme {
                ElGranTelonNavHost()
            }
        }
    }
}
