package com.talia.multilanguagedemogroup1


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
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


/*
 * LanguageScreen
 *
 * This Composable is responsible for DISPLAYING the interface.
 *
 * Notice that it does not:
 *
 * create an ML Kit Translator
 *
 * download language models
 *
 * change Android locales
 *
 * Those responsibilities are handled elsewhere.
 *
 * This keeps our UI separate from our language logic.
 */
@Composable
fun LanguageScreen(

    /*
     * Allows MainActivity to provide padding from Scaffold.
     */
    modifier: Modifier = Modifier,


    /*
     * Contains all the text currently displayed
     * by the interface.
     */
    uiState: LanguageUiState,


    /*
     * Human-readable name of the current language.
     */
    currentLanguage: String,


    /*
     * true while ML Kit is preparing or translating.
     */
    isLoading: Boolean,


    /*
     * null when there is no error.
     *
     * Contains a message if translation fails.
     */
    errorMessage: String?,


    /*
     * These functions are supplied by MainActivity.
     *
     * LanguageScreen does not need to know HOW each
     * language is implemented.
     */
    onEnglishClick: () -> Unit,

    onAfrikaansClick: () -> Unit,

    onZuluClick: () -> Unit,

    onSystemLanguageClick: () -> Unit
) {


    Column(

        modifier =
            modifier
                .fillMaxSize()
                .padding(24.dp),

        verticalArrangement =
            Arrangement.Center,

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {


        /*
         * MAIN SCREEN TITLE
         */
        Text(

            text =
                uiState.screenTitle,

            style =
                MaterialTheme
                    .typography
                    .headlineMedium
        )


        Spacer(
            modifier =
                Modifier.height(20.dp)
        )


        /*
         * WELCOME MESSAGE
         */
        Text(

            text =
                uiState.welcomeMessage,

            style =
                MaterialTheme
                    .typography
                    .titleLarge
        )


        Spacer(
            modifier =
                Modifier.height(12.dp)
        )


        /*
         * SCREEN DESCRIPTION
         */
        Text(
            text =
                uiState.description
        )


        Spacer(
            modifier =
                Modifier.height(24.dp)
        )


        /*
         * LANGUAGE SELECTION HEADING
         */
        Text(

            text =
                uiState.chooseLanguage,

            style =
                MaterialTheme
                    .typography
                    .titleMedium
        )


        Spacer(
            modifier =
                Modifier.height(16.dp)
        )


        /*
         * ENGLISH BUTTON
         *
         * MainActivity will handle the actual
         * language change.
         */
        Button(

            onClick =
                onEnglishClick,

            /*
             * Disable the button while ML Kit is busy.
             */
            enabled =
                !isLoading,

            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text(
                text =
                    uiState.englishLabel
            )
        }


        Spacer(
            modifier =
                Modifier.height(8.dp)
        )


        /*
         * AFRIKAANS BUTTON
         *
         * This ultimately starts the ML Kit
         * automatic translation process.
         */
        Button(

            onClick =
                onAfrikaansClick,

            enabled =
                !isLoading,

            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text(
                text =
                    uiState.afrikaansLabel
            )
        }


        Spacer(
            modifier =
                Modifier.height(8.dp)
        )


        /*
         * ISIZULU BUTTON
         *
         * This ultimately changes Android's locale
         * to "zu".
         */
        Button(

            onClick =
                onZuluClick,

            enabled =
                !isLoading,

            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text(
                text =
                    uiState.zuluLabel
            )
        }


        Spacer(
            modifier =
                Modifier.height(8.dp)
        )


        /*
         * DEVICE LANGUAGE BUTTON
         *
         * Removes the application's forced locale
         * and allows Android to follow the system.
         */
        Button(

            onClick =
                onSystemLanguageClick,

            enabled =
                !isLoading,

            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text(
                text =
                    uiState.systemLanguageLabel
            )
        }


        Spacer(
            modifier =
                Modifier.height(24.dp)
        )


        /*
         * CURRENT LANGUAGE
         *
         * The label itself may be translated.
         *
         * Example:
         *
         * English:
         * Current Language: English
         *
         * Afrikaans:
         * [translated label]&#58; Afrikaans
         */
        Text(

            text =
                "${uiState.currentLanguageLabel}: $currentLanguage",

            style =
                MaterialTheme
                    .typography
                    .bodyLarge
        )


        /*
         * Only show this section while ML Kit is
         * preparing or translating.
         */
        if (isLoading) {


            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )


            CircularProgressIndicator()


            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )


            /*
             * For this simple demo we retrieve the loading
             * message from the default resource.
             *
             * You could later add this to LanguageUiState
             * if you want it to change dynamically as well.
             */
            Text(
                text =
                    "Preparing translation..."
            )
        }


        /*
         * ?.let means:
         *
         * only execute this block if errorMessage
         * is NOT null.
         */
        errorMessage?.let { message ->


            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )


            Text(

                text =
                    message,

                color =
                    MaterialTheme
                        .colorScheme
                        .error
            )
        }
    }
}