package com.talia.multilanguagedemogroup1


import android.os.Bundle
import android.util.Log

import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.appcompat.app.AppCompatActivity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.talia.multilanguagedemogroup1.ui.theme.MultiLanguageDemoGroup1Theme


class MainActivity : AppCompatActivity() {


    /*
     * TAG
     *
     * This is used when writing messages to Logcat.
     *
     * In Logcat we can search for:
     *
     * LanguageDemo
     *
     * to follow what is happening during translation.
     */
    companion object {

        private const val TAG =
            "LanguageDemo"
    }


    /*
     * Create our TranslationManager.
     *
     * This class handles:
     *
     * English
     * ->
     * Afrikaans
     *
     * using Google ML Kit.
     */
    private val translationManager =
        TranslationManager()


    /*
     * uiState contains all the text currently displayed
     * by our screen.
     *
     * Initially the Strings are empty.
     *
     * We populate them from strings.xml inside onCreate().
     */
    private var uiState by mutableStateOf(

        LanguageUiState(

            screenTitle = "",

            welcomeMessage = "",

            description = "",

            chooseLanguage = "",

            englishLabel = "",

            afrikaansLabel = "",

            zuluLabel = "",

            currentLanguageLabel = "",

            systemLanguageLabel = ""
        )
    )


    /*
     * Stores the name of the language currently being
     * displayed.
     *
     * Examples:
     *
     * English
     * Afrikaans
     * isiZulu
     */
    private var currentLanguage by
    mutableStateOf("English")


    /*
     * Keeps track of whether ML Kit is currently:
     *
     * downloading/preparing a model
     *
     * OR
     *
     * translating text.
     *
     * Compose observes this value.
     */
    private var isLoading by
    mutableStateOf(false)


    /*
     * Stores an error message if something goes wrong.
     *
     * null means:
     *
     * there is currently no error.
     */
    private var errorMessage by
    mutableStateOf<String?>(null)


    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()


        /*
         * Load the Strings for Android's CURRENT locale.
         *
         * For example:
         *
         * English locale
         * -> values/strings.xml
         *
         * isiZulu locale
         * -> values-zu/strings.xml
         */
        loadCurrentResourceStrings()


        setContent {

            MultiLanguageDemoGroup1Theme {

                Scaffold(
                    modifier =
                        Modifier.fillMaxSize()
                ) { innerPadding ->


                    /*
                     * Display our language screen.
                     *
                     * MainActivity manages the behaviour.
                     *
                     * LanguageScreen manages what the user sees.
                     */
                    LanguageScreen(

                        modifier =
                            Modifier.padding(
                                innerPadding
                            ),

                        uiState =
                            uiState,

                        currentLanguage =
                            currentLanguage,

                        isLoading =
                            isLoading,

                        errorMessage =
                            errorMessage,


                        /*
                         * ENGLISH BUTTON
                         *
                         * English uses Android's normal
                         * resource system.
                         */
                        onEnglishClick = {

                            Log.d(
                                TAG,
                                "English selected."
                            )


                            AppLanguageManager
                                .changeLanguage(
                                    "en"
                                )
                        },


                        /*
                         * AFRIKAANS BUTTON
                         *
                         * Afrikaans uses ML Kit.
                         */
                        onAfrikaansClick = {

                            Log.d(
                                TAG,
                                "Afrikaans selected."
                            )


                            translateToAfrikaans()
                        },


                        /*
                         * ISIZULU BUTTON
                         *
                         * isiZulu is not supported by the
                         * ML Kit Translation model used here.
                         *
                         * We therefore use Android's normal
                         * resource localisation.
                         */
                        onZuluClick = {

                            Log.d(
                                TAG,
                                "isiZulu selected."
                            )


                            AppLanguageManager
                                .changeLanguage(
                                    "zu"
                                )
                        },


                        /*
                         * DEVICE LANGUAGE BUTTON
                         *
                         * Remove the application's forced
                         * locale.
                         *
                         * Android can then follow the
                         * device language.
                         */
                        onSystemLanguageClick = {

                            Log.d(
                                TAG,
                                "Device language selected."
                            )


                            AppLanguageManager
                                .useDeviceLanguage()
                        }
                    )
                }
            }
        }
    }


    /*
     * loadCurrentResourceStrings()
     *
     * Retrieve all the Strings from Android's currently
     * active resource set.
     *
     * Android automatically decides which strings.xml
     * file should be used.
     */
    private fun loadCurrentResourceStrings() {

        uiState =
            LanguageUiState(

                screenTitle =
                    getString(
                        R.string.screen_title
                    ),

                welcomeMessage =
                    getString(
                        R.string.welcome_message
                    ),

                description =
                    getString(
                        R.string.description
                    ),

                chooseLanguage =
                    getString(
                        R.string.choose_language
                    ),

                englishLabel =
                    getString(
                        R.string.english
                    ),

                afrikaansLabel =
                    getString(
                        R.string.afrikaans
                    ),

                zuluLabel =
                    getString(
                        R.string.zulu
                    ),

                currentLanguageLabel =
                    getString(
                        R.string.current_language
                    ),

                systemLanguageLabel =
                    getString(
                        R.string.use_system_language
                    )
            )


        /*
         * Find the language currently being used by
         * Android resources.
         *
         * Examples:
         *
         * en
         * zu
         */
        val languageCode =
            resources
                .configuration
                .locales[0]
                .language


        /*
         * Convert the language code into a readable
         * language name for the interface.
         */
        currentLanguage =
            when (languageCode) {

                "zu" ->
                    "isiZulu"

                "af" ->
                    "Afrikaans"

                else ->
                    "English"
            }


        /*
         * Clear any previous error.
         */
        errorMessage =
            null
    }


    /*
     * translateToAfrikaans()
     *
     * This begins the ML Kit translation process.
     *
     * IMPORTANT:
     *
     * Our TranslationManager is configured as:
     *
     * English
     * ->
     * Afrikaans
     *
     * Therefore the source text supplied to ML Kit
     * must be English.
     */
    private fun translateToAfrikaans() {

        /*
         * Do not start another translation while one
         * is already running.
         */
        if (isLoading) {

            return
        }


        isLoading =
            true

        errorMessage =
            null


        /*
         * Determine the locale Android is currently using.
         */
        val currentLocale =
            resources
                .configuration
                .locales[0]
                .language


        /*
         * If we are currently using isiZulu resources,
         * our getString() calls would return isiZulu.
         *
         * We cannot send isiZulu text to a Translator
         * configured as:
         *
         * English -> Afrikaans
         *
         * Therefore return to English first.
         */
        if (currentLocale != "en") {

            Log.d(
                TAG,
                "Returning to English resources before Afrikaans translation."
            )


            /*
             * Changing the locale may recreate this Activity.
             *
             * For this simple classroom demo, after returning
             * to English the user can press Afrikaans again.
             *
             * A more advanced implementation could remember
             * that Afrikaans was requested and automatically
             * continue after recreation.
             */
            isLoading =
                false


            AppLanguageManager
                .changeLanguage(
                    "en"
                )


            return
        }


        Log.d(
            TAG,
            "Preparing Afrikaans translation model."
        )


        /*
         * Ask TranslationManager to make sure the
         * Afrikaans model is available.
         */
        translationManager
            .prepareAfrikaansModel(

                /*
                 * This block runs when the model is ready.
                 */
                onSuccess = {

                    Log.d(
                        TAG,
                        "Afrikaans translation model is ready."
                    )


                    /*
                     * We can now begin translating our
                     * English interface.
                     */
                    translateScreenStrings()
                },


                /*
                 * This block runs if the model cannot
                 * be prepared/downloaded.
                 */
                onFailure = { exception ->

                    isLoading =
                        false


                    errorMessage =
                        getString(
                            R.string.translation_failed
                        )


                    Log.e(
                        TAG,
                        "Unable to prepare Afrikaans model.",
                        exception
                    )
                }
            )
    }


    /*
     * translateScreenStrings()
     *
     * Collect the English Strings used by our screen and
     * send them to ML Kit.
     */
    private fun translateScreenStrings() {


        /*
         * Create a List containing all the English
         * user-facing Strings that we want ML Kit to
         * automatically translate.
         *
         * The order is important because we will use
         * these positions again when creating the
         * translated LanguageUiState.
         */
        val sourceStrings =
            listOf(

                getString(
                    R.string.screen_title
                ),

                getString(
                    R.string.welcome_message
                ),

                getString(
                    R.string.description
                ),

                getString(
                    R.string.choose_language
                ),

                getString(
                    R.string.english
                ),

                getString(
                    R.string.afrikaans
                ),

                getString(
                    R.string.zulu
                ),

                getString(
                    R.string.current_language
                ),

                getString(
                    R.string.use_system_language
                )
            )


        /*
         * Create an empty list that has the SAME size
         * as our English source list.
         *
         * Each completed Afrikaans translation will be
         * placed into its corresponding position.
         */
        val translatedStrings =
            MutableList(
                sourceStrings.size
            ) {
                ""
            }


        /*
         * Keep track of how many translation operations
         * have successfully completed.
         */
        var completedTranslations =
            0

        /*
         * Loop through every English String.
         *
         * forEachIndexed gives us:
         *
         * index
         * -> position in the List
         *
         * sourceText
         * -> actual English String
         */
        sourceStrings
            .forEachIndexed { index, sourceText ->


                /*
                 * Ask ML Kit to translate this String.
                 */
                translationManager
                    .translate(

                        text =
                            sourceText,


                        /*
                         * Runs when this particular String
                         * has been successfully translated.
                         */
                        onSuccess = { translatedText ->


                            /*
                             * Store the translated result in
                             * exactly the same position as its
                             * English source.
                             */
                            translatedStrings[index] =
                                translatedText


                            /*
                             * Record that another translation
                             * has completed.
                             */
                            completedTranslations++


                            Log.d(
                                TAG,
                                "Translated: $sourceText -> $translatedText"
                            )


                            /*
                             * We only update the entire interface
                             * once ALL Strings have finished.
                             *
                             * This prevents a screen that is
                             * temporarily half English and half
                             * Afrikaans.
                             */
                            if (
                                completedTranslations ==
                                sourceStrings.size
                            ) {

                                applyAfrikaansTranslations(
                                    translatedStrings
                                )
                            }
                        },


                        /*
                         * Runs if this particular translation
                         * fails.
                         */
                        onFailure = { exception ->


                            isLoading =
                                false


                            errorMessage =
                                getString(
                                    R.string.translation_failed
                                )


                            Log.e(
                                TAG,
                                "Translation failed for: $sourceText",
                                exception
                            )
                        }
                    )
            }
    }


    /*
     * applyAfrikaansTranslations()
     *
     * All translations have now completed.
     *
     * Convert the translated List back into our
     * LanguageUiState.
     */
    private fun applyAfrikaansTranslations(
        translations: List<String>
    ) {


        /*
         * The indexes correspond to the order used in
         * sourceStrings.
         *
         * 0 -> screen title
         * 1 -> welcome message
         * 2 -> description
         * 3 -> choose language
         * 4 -> English
         * 5 -> Afrikaans
         * 6 -> isiZulu
         * 7 -> current language
         * 8 -> use device language
         */
        uiState =
            LanguageUiState(

                screenTitle =
                    translations[0],

                welcomeMessage =
                    translations[1],

                description =
                    translations[2],

                chooseLanguage =
                    translations[3],

                englishLabel =
                    translations[4],

                afrikaansLabel =
                    translations[5],

                zuluLabel =
                    translations[6],

                currentLanguageLabel =
                    translations[7],

                systemLanguageLabel =
                    translations[8]
            )


        /*
         * Update the readable current-language value.
         */
        currentLanguage =
            "Afrikaans"


        /*
         * Translation has finished.
         *
         * Hide the loading indicator and re-enable
         * language buttons.
         */
        isLoading =
            false


        Log.d(
            TAG,
            "Complete interface translated to Afrikaans."
        )
    }


    override fun onDestroy() {

        super.onDestroy()


        /*
         * Release ML Kit resources when this Activity
         * is destroyed.
         */
        translationManager.close()
    }
}