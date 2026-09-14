package com.talia.multilanguagedemogroup1


import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat


/*
 * AppLanguageManager
 *
 * This object is responsible for changing Android's
 * application locale.
 *
 * We use Android's locale system for:
 *
 * English
 * -> "en"
 *
 * isiZulu
 * -> "zu"
 *
 * We do NOT use it for Afrikaans in this demo because
 * Afrikaans is being translated dynamically using ML Kit.
 */
object AppLanguageManager {


    /*
     * changeLanguage()
     *
     * Receives an Android language tag.
     *
     * Examples:
     *
     * "en" -> English
     *
     * "zu" -> isiZulu
     */
    fun changeLanguage(
        languageTag: String
    ) {

        /*
         * Convert the String language tag into a locale list
         * that Android understands.
         */
        val appLocale =
            LocaleListCompat.forLanguageTags(
                languageTag
            )


        /*
         * Tell AppCompat that this application should use
         * the selected locale.
         *
         * Android may recreate MainActivity after this.
         *
         * That is normal because Android needs to reload
         * language-specific resources.
         */
        AppCompatDelegate.setApplicationLocales(
            appLocale
        )
    }


    /*
     * useDeviceLanguage()
     *
     * Passing an empty LocaleList tells AppCompat:
     *
     * "Do not force an application-specific language."
     *
     * Android will then use the device/system language.
     */
    fun useDeviceLanguage() {

        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.getEmptyLocaleList()
        )
    }
}