package com.example.profeska

//To co na zielono jest to se możesz ziemieniać a te databaseDate.subSequence..
//to ci podpisałem które jest które to możesz sobie zmieniać kolejność jak chcesz ;)

fun displayFormatDate(databaseDate: String): String {

    return "${databaseDate.subSequence(0, 4)}" + //Rok
            "/${databaseDate.subSequence(4, 6)}" + //Miesiac
            "/${databaseDate.subSequence(6, 8)}" //Dzien

}

fun displayFormatTime(databaseDate: String):String{
    return "${databaseDate.subSequence(8, 10)}:" + // Godzina
            "${databaseDate.subSequence(10, 12)}" //Minuty
}

fun displayDateAndTime(databaseDate: String):String{
    return "${displayFormatDate(databaseDate)} ${displayFormatTime(databaseDate)}"
}