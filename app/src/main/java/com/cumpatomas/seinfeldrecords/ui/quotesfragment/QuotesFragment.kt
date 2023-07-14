package com.cumpatomas.seinfeldrecords.ui.quotesfragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.cumpatomas.seinfeldrecords.R
import com.cumpatomas.seinfeldrecords.core.ex.addSpaces
import com.robinhood.ticker.TickerUtils
import com.robinhood.ticker.TickerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuotesFragment : Fragment() {
    private lateinit var tickerView: TickerView

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter", "MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.quotes_fragment, container, false)
        tickerView = view.findViewById<TickerView>(R.id.quizPointsTickerView)

        view.findViewById<ComposeView>(R.id.composeView).setContent {}

        return view
    }

    @SuppressLint(
        "UnsafeRepeatOnLifecycleDetector", "CoroutineCreationDuringComposition",
        "StateFlowValueCalledInComposition",
    )
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ComposeView>(R.id.composeView).setContent {
            /** Marquee configuration*/
            val txtMarquee: TextView = view.findViewById(R.id.tvChapterTitle)
            txtMarquee.isSelected = true
            txtMarquee.isSingleLine = true

            val lottieCorrect = view.findViewById<View>(R.id.lottieWellDone)
            val lottieWrong = view.findViewById<View>(R.id.lottieNope)
            val viewModel: QuotesFragmentViewModel by viewModels()
            val list = viewModel.quotesListViewModel.collectAsState()
            val loading = viewModel.isLoading.collectAsState()
            val quotesListSize = rememberSaveable {
                mutableStateOf<Int?>(0)
            }

            initCollectors(viewModel)



            Surface(
                modifier = Modifier.fillMaxSize(), color = colorResource(R.color.custom_blue)
            ) {
                LongPressDraggable(modifier = Modifier.fillMaxSize()) {
                    QuotesLazyColumn(list.value, loading, viewModel, txtMarquee)
                    PersonListContainer(viewModel, quotesListSize, lottieCorrect, lottieWrong)
                }
            }
        }
    }

    private fun initCollectors(viewModel: QuotesFragmentViewModel) {
        lifecycleScope.launch {
            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.getUserPoints()
                    launch() {
                        viewModel.userPoints.collectLatest {
                            tickerView.setCharacterLists(TickerUtils.provideNumberList())
                            tickerView.text = it.toString()
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition", "SetTextI18n")
@Composable
private fun QuotesLazyColumn(
    quotesList: List<QuoteItem>,
    loading: State<Boolean>,
    viewModel: QuotesFragmentViewModel,
    txtMarquee: TextView
) {
    if (loading.value) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .padding(top = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = colorResource(id = R.color.primaryColor),
                modifier = Modifier.size(40.dp),
                strokeWidth = 5.dp
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .padding(bottom = 8.dp),
//            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Hold and drag the line to it's author",
                    color = colorResource(id = R.color.primaryColor),
                    fontFamily = FontFamily(Font(R.font.type_font)),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(items = quotesList.take(3)) { quote ->
                if (!quote.isAnswered.value) QuoteItemCard(quoteItem = quote)
            }
        }
        txtMarquee.text = "Chapter: " + viewModel.link.value
            .substringAfterLast('/')
            .dropLast(4).addSpaces() + "     " +
                "Chapter: " + viewModel.link.value.substringAfterLast('/').dropLast(4).addSpaces() +
        "     " +
                "Chapter: " + viewModel.link.value.substringAfterLast('/').dropLast(4).addSpaces()
    }
}

@Composable
fun BoxScope.PersonListContainer(
    viewModel: QuotesFragmentViewModel,
    quotesListSize: MutableState<Int?>,
    lottieCorrect: View,
    lottieWrong: View
) {
    LazyRow(
        modifier = Modifier
            .fillMaxHeight(0.3f)
            .fillMaxWidth()
            .background(
                colorResource(id = R.color.transparent),
                shape = RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp)
            )
            .padding(vertical = 10.dp, horizontal = 0.dp)
            .align(Alignment.BottomCenter),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
//        columns = GridCells.Fixed(count = 2)
    ) {
        items(items = author) { person ->
            PersonCard(person, viewModel, quotesListSize, lottieCorrect, lottieWrong)
        }
    }
}

@Composable
fun PersonCard(
    person: Person,
    viewModel: QuotesFragmentViewModel,
    quotesListSize: MutableState<Int?>,
    lottieCorrect: View,
    lottieWrong: View
) {
    val coroutineScope = rememberCoroutineScope()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        DropTarget<QuoteItem>(
            modifier = Modifier
                .padding(0.dp),
        ) { isInBound, authorItem ->
            val bgColor = if (isInBound) {
                colorResource(id = R.color.primaryColor)
            } else {
                Color.White
            }
            authorItem?.let {
                if (isInBound) {
                    if (it.author == person.name) {
                        println("Right!!")
                        viewModel.setPoints(1)
                        coroutineScope.launch {
                            lottieCorrect.isVisible = true
                            delay(3000)
                            lottieCorrect.isGone = true
                        }
                    } else {
                        println("wrong!")
                        viewModel.setPoints(-1)
                        coroutineScope.launch {
                            lottieWrong.isVisible = true
                            delay(3000)
                            lottieWrong.isGone = true
                        }
                    }
                    it.isAnswered.value = true
                    quotesListSize.value = quotesListSize.value?.plus(1)
                    if (quotesListSize.value == 3) {
                        quotesListSize.value = 0
                        viewModel.updateList()
                    }
                    println(quotesListSize.value)
                }
            }

            Column(
                modifier = Modifier
                    .shadow(elevation = 4.dp, shape = CircleShape)
                    .background(
                        bgColor,
                        CircleShape
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = person.profile),
                    contentDescription = null,
                    modifier = Modifier.size(75.dp),
                    contentScale = ContentScale.FillHeight
                )
            }
        }
        Text(
            text = person.name,
            fontSize = 16.sp,
            color = colorResource(id = R.color.white),
            fontFamily = FontFamily(Font(R.font.type_font))
//        fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun QuoteItemCard(quoteItem: QuoteItem) {
    Card(
        elevation = 10.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(10.dp),
        modifier = modifier()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(10.dp)
        ) {
            DragTarget(modifier = Modifier, dataToDrop = quoteItem) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = quoteItem.quote,
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.type_font)),
                        color = Color.DarkGray,
                        fontStyle = Italic,
                        textAlign = TextAlign.Center,
                    )
                }
            }
            Spacer(modifier = Modifier.width(20.dp))
        }
    }
}

@Composable
private fun modifier() = Modifier
    .padding(4.dp)
    .fillMaxWidth()

