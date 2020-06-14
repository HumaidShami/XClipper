package com.kpstv.xclipper.ui.activities

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.kpstv.xclipper.App.DARK_THEME
import com.kpstv.xclipper.App.TUTORIAL_PREF
import com.kpstv.xclipper.R
import com.kpstv.xclipper.data.localized.ToolbarState
import com.kpstv.xclipper.data.provider.PreferenceProvider
import com.kpstv.xclipper.extensions.utils.ThemeUtils
import com.kpstv.xclipper.ui.viewmodels.MainViewModel
import com.kpstv.xclipper.ui.viewmodels.MainViewModelFactory
import kotlinx.android.synthetic.main.fragment_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class Main : AppCompatActivity(), KodeinAware {

    private val TAG = javaClass.simpleName

    override val kodein by kodein()
    private val viewModelFactory by instance<MainViewModelFactory>()
    private val preferenceProvider by instance<PreferenceProvider>()

    var isDarkTheme = true

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isDarkTheme = DARK_THEME

        ThemeUtils.setTheme(this)

        mainViewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)

        val navOptions =
            NavOptions.Builder().setPopUpTo(R.id.fragment_greet, true).build()

        if (preferenceProvider.getBooleanKey(TUTORIAL_PREF, false)) {
            navController.navigate(R.id.fragment_home, null, navOptions)
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        /*

         // TODO: Move this SAW to a valid place.
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !android.provider.Settings.canDrawOverlays(
                 this
             )
         ) {
             // Show alert dialog to the user saying a separate permission is needed
             // Launch the settings activity if the user prefers
             val myIntent =
                 Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
             startActivity(myIntent)
         }

         */
    }

    /**
     * Check if current theme has changed...
     */
    override fun onResume() {
        super.onResume()
        if (DARK_THEME != isDarkTheme) {
            val previousIntent = intent
            finish()
            startActivity(previousIntent);
        }
    }

    override fun onBackPressed() {
        when {
            mainViewModel.stateManager.isMultiSelectionStateActive() -> mainViewModel.stateManager.setToolbarState(
                ToolbarState.NormalViewState
            )
            searchView?.onBackPressed() == true -> return
            else -> super.onBackPressed()
        }
    }
}