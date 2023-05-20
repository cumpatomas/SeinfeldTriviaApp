package com.cumpatomas.seinfeldrecords.data

import com.cumpatomas.seinfeldrecords.data.model.SeinfeldChar

class CharListProvider {

    companion object {
        val charList = listOf<SeinfeldChar>(
            SeinfeldChar(
                "Jerry Seinfeld",
                "Main Character",
                "He's Jerry!",
                "https://cdn.forbes.com.mx/2013/07/seinfeld.jpg"

            ),
            SeinfeldChar(
                "George Costanza",
                "Greedy, stocky, bald",
                "Jerry's friend",
                "https://static.wikia.nocookie.net/seinfeld/images/7/76/George-costanza.jpg/revision/latest?cb=20110406222711"

            ),
            SeinfeldChar(
                "Elaine Benes",
                "Single, demanding, big-head",
                "Jerry's ex girlfriend",
                "https://s3.amazonaws.com/arc-wordpress-client-uploads/infobae-wp/wp-content/uploads/2019/07/08165544/elaine-benes-copy.jpg"

            ),
            SeinfeldChar(
                "Cosmo Kramer",
                "Single, intruder, unemployed ",
                "Jerry's neighbour",
                "data:image/jpeg;base64,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"

            ),

SeinfeldChar(
                "Frank Costanza",
                "Vigorous, foot odour suffer",
                "George's father",
                "https://www.latercera.com/resizer/ulznbr1hmsFVK6YmRJHfnPvQotI=/900x600/smart/arc-anglerfish-arc2-prod-copesa.s3.amazonaws.com/public/BFC7RJS3UNDRZCZTKUECYI4EJM.webp"

            ),
SeinfeldChar(
                "Estelle Costanza",
                "High pitch voice, never smile",
                "George's mother",
                "https://preview.redd.it/k2zzmpkvkqy41.png?auto=webp&s=70471cd9aa0f00b801751fb046659c3ab46641c6"

            ),
SeinfeldChar(
                "Newman ",
                "Mailman, broccoli hater",
                "Jerry's enemy",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSFe1qZl4cokgLv0g1UdnNvm96T2hKMIGlrkA&usqp=CAU"

            ),
SeinfeldChar(
                "David Puddy",
                "Christian, face painter",
                "Elaine's boyfriend",
                "https://1.bp.blogspot.com/-eIwaDUtmlNk/WYfmat3KcYI/AAAAAAAAYyQ/9PsDQh2k_mMaTRgP5g5pEv8Fs5ej0_ZtgCLcBGAs/s1600/puddy.jpg"

            ),
SeinfeldChar(
                "Susan Ross",
                "Demanding, converted",
                "George's fiancee",
                "https://static.tvmaze.com/uploads/images/original_untouched/315/789362.jpg"

            ),

            )
    }

}