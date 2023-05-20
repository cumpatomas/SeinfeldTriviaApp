package com.cumpatomas.seinfeldrecords.ui.homefragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cumpatomas.seinfeldrecords.core.ex.addSpaces
import com.cumpatomas.seinfeldrecords.domain.GetRandomScript
import com.cumpatomas.seinfeldrecords.domain.ScrapScripts
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeFragmentViewModel : ViewModel() {
    private val scraping = ScrapScripts()
    private val script = GetRandomScript()
    private val _list = MutableStateFlow<List<String>>(emptyList())
    val list = _list.asStateFlow()
    private var urls = emptyList<String>()
    private val _titlesList = MutableStateFlow<MutableList<String>>(mutableListOf())
    val titlesList = _titlesList.asStateFlow()

    init {

        viewModelScope.launch(IO) {

            launch {
                urls = scraping.invoke()
            }.join()
            launch {
                val randomLines = script.invoke(urls.shuffled().random())
                    .ifEmpty { script.invoke(urls.shuffled().random()) }
                _titlesList.value =
                    urls.map { it.substringAfterLast("/").substringBefore(".") }.toMutableList()
                val tempList = mutableListOf<String>()
                val tempList2 = _titlesList.value.toMutableList()
                for (title in tempList2) {
                    tempList += title.addSpaces()
                }
                _titlesList.value = tempList
                println("titulos")
                println(titlesList)

                for (i in randomLines) {
                    _list.value += i
                }
            }.join()
        }

    }

    suspend fun getNewScript() {
        println("button!")
        _list.value = emptyList()
        viewModelScope.launch() {
            launch(IO) {
                val randomLines = script.invoke(urls.shuffled().random())
                    .ifEmpty { script.invoke(urls.shuffled().random()) }

                for (i in randomLines) {
                    _list.value += i
                }
            }.join()
            println(_list.value)
        }
    }
}