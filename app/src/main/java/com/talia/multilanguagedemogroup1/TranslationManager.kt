package com.talia.multilanguagedemogroup1

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions

/*
this class is ONLY responsible for logic of the auto translation

source - english
target - afrikaans
- zulu = fallback on strings.xml
 */

class TranslationManager {

    //create a translator to configure langs
    private val translatorOptions =
        TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.AFRIKAANS)
            .build()

    //create ml kit translator
    private val translator: Translator =
        Translation.getClient(
            translatorOptions
        )

    /*for every language that should be supported i would need to add a model
    model =
    - success callabck run immediately

    if we need to download model
    - ml kit will download model
    - success callback
     */
    fun prepareAfrikaansModel(
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ){
        val conditions =
            DownloadConditions
                .Builder()
                .build()

        translator
            .downloadModelIfNeeded( conditions )
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(
                    exception
                )
            }
    }

    /*
    translate 1 english string to afrikaans

    ml kit works async

    - req translation
    - ml kit process text
    - callback will be delayed
     */
    fun translate(
        text: String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        translator
            .translate(
                text
            )
            .addOnSuccessListener { translatedText ->
                onSuccess(
                    translatedText
                )
            }
            .addOnFailureListener { exception ->
                onFailure(
                    exception
                )
            }
    }

    //releasing unnecessary resources by translator
    fun close(){
        translator.close()
    }

}