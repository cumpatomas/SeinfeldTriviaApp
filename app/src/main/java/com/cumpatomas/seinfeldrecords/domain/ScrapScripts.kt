package com.cumpatomas.seinfeldrecords.domain

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class ScrapScripts {

    private val blackList = listOf(
        "https://www.seinfeldscripts.com/TheMasseuse.html",
        "https://www.seinfeldscripts.com/TheDeal.htm",
        "https://www.seinfeldscripts.com/TheNote.html",
    "https://www.seinfeldscripts.com/TheCafe.html",
    "https://www.seinfeldscripts.com/TheNoseJob.html",
    "https://www.seinfeldscripts.com/TheAlternateSide.htm",
    "https://www.seinfeldscripts.com/TheSuicide.html",
    "https://www.seinfeldscripts.com/TheFixUp.html",
    "https://www.seinfeldscripts.com/TheBoyfriend1.htm",
    "https://www.seinfeldscripts.com/TheBoyfriend2.htm",
    "https://www.seinfeldscripts.com/TheLimo.html",
    "https://www.seinfeldscripts.com/TheGoodSamaritan.html",
    "https://www.seinfeldscripts.com/TheParkingSpace.html",
    "https://www.seinfeldscripts.com/TheTrip1.htm",
   "https://www.seinfeldscripts.com/TheTrip2.htm",
    "https://www.seinfeldscripts.com/ThePitch.htm",
    "https://www.seinfeldscripts.com/TheWallet.html",
    "https://www.seinfeldscripts.com/TheWatch.html",
    "https://www.seinfeldscripts.com/TheOpera.html,",
    "https://www.seinfeldscripts.com/TheAirport.htm",
    "https://www.seinfeldscripts.com/TheVisa.html",
    "https://www.seinfeldscripts.com/TheShoes.html",
    "https://www.seinfeldscripts.com/TheImplant.html",
    "https://www.seinfeldscripts.com/TheHandicapSpot.html",
    "https://www.seinfeldscripts.com/ThePilot.html",
    "https://www.seinfeldscripts.com/ThePilot2.html",
    "https://www.seinfeldscripts.com/TheGlasses.html",
    "https://www.seinfeldscripts.com/TheNonFatYogurt.html",
    "https://www.seinfeldscripts.com/TheMasseuse.html",
    "https://www.seinfeldscripts.com/TheCigarStoreIndian.htm",
    "https://www.seinfeldscripts.com/TheStand-In.html",
    "https://www.seinfeldscripts.com/ThePledgeDrive.html",
    "https://www.seinfeldscripts.com/TheCouch.html",
        "https://www.seinfeldscripts.com/TheSoup.html",
        "https://www.seinfeldscripts.com/TheMomAndPopStore.html",
    "https://www.seinfeldscripts.com/TheSecretary.html",
    "https://www.seinfeldscripts.com/TheLabelMaker.html",
    "https://www.seinfeldscripts.com/TheScofflaw.htm",
   "https://www.seinfeldscripts.com/Highlights-of-100-1.html",
        "https://www.seinfeldscripts.com/Highlights-of-100-2.html",
    "https://www.seinfeldscripts.com/seinfeld-scripts.html",
    "https://www.seinfeldscripts.com/TheDoorman.html",
   "https://www.seinfeldscripts.com/TheFacePainter.html",
    "https://www.seinfeldscripts.com/ThePoolGuy.html",
    "https://www.seinfeldscripts.com/TheCaddy.html",
    "https://www.seinfeldscripts.com/TheCadillac1.html",
    "https://www.seinfeldscripts.com/TheCadillac2.html",
    "https://www.seinfeldscripts.com/TheFriarsClub.html",
    "https://www.seinfeldscripts.com/TheBottleDeposit2.html",
    "https://www.seinfeldscripts.com/TheFoundation.html",
    "https://www.seinfeldscripts.com/TheSoulMate.html",
    "https://www.seinfeldscripts.com/TheFatigues.html",
    "https://www.seinfeldscripts.com/TheChecks.html",
    "https://www.seinfeldscripts.com/TheMoney.html",
    "https://www.seinfeldscripts.com/TheComeback.html",
    "https://www.seinfeldscripts.com/TheVanBurenBoys.htm",
    "https://www.seinfeldscripts.com/ThePothole.htm",
    "https://www.seinfeldscripts.com/TheEnglishPatient.html",
    "https://www.seinfeldscripts.com/TheMillennium.html",
    "https://www.seinfeldscripts.com/TheSummerofGeorge.htm",
    "https://www.seinfeldscripts.com/TheJunkMail.htm",
    "https://www.seinfeldscripts.com/TheMervGriffinShow.htm",
    "https://www.seinfeldscripts.com/TheApology.htm",
    "https://www.seinfeldscripts.com/TheStrongbox.htm",
    "https://www.seinfeldscripts.com/TheWizard.htm",
    "https://www.seinfeldscripts.com/TheBurning.html",
    "https://www.seinfeldscripts.com/The-Clip-Show-1.html",
    "https://www.seinfeldscripts.com/The-Clip-Show-2.html",
        "https://www.seinfeldscripts.com/TheSeinfeldChronicles.htm",
        "https://www.seinfeldscripts.com/TheSuzie.htm",
        "https://www.seinfeldscripts.com/ThePostponement.html",
        "https://www.seinfeldscripts.com/TheStranded.html",
        "https://www.seinfeldscripts.com/TheOpera.html",
        "https://www.seinfeldscripts.com/TheTrip1.htm",
        "https://www.seinfeldscripts.com/TheTrip2.htm"
    )
    suspend operator fun invoke(): MutableList<String> {

        val outputList = mutableListOf<String>()
        coroutineScope {
            launch {
                val url = Jsoup.connect("https://www.seinfeldscripts.com/seinfeld-scripts.html")
                    .get();
                val table = url.getElementsByAttributeStarting("href")

                for (i in 15..table.lastIndex - 16) {
                    if (table[i].toString().contains(".htm")) {
                        outputList.add(
                            "https://www.seinfeldscripts.com/" + table[i].toString()
                                .substringAfter("\"").substringBefore("\"").trim()
                        )

                    }
                }
            }
        }
        /*        println("lista filtrada:")
        println(outputList.filter { it !in blackList }.toMutableList())*/
        return outputList.filter { it !in blackList }.toMutableList()

    }

}