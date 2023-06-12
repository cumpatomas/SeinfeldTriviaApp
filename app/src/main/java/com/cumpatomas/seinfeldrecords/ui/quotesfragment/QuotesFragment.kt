package com.cumpatomas.seinfeldrecords.ui.quotesfragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.cumpatomas.seinfeldrecords.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuotesFragment : Fragment() {

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter", "MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.quotes_fragment, container, false)

        view.findViewById<ComposeView>(R.id.composeView).setContent {
        }

        return view
    }

    @SuppressLint(
        "UnsafeRepeatOnLifecycleDetector", "CoroutineCreationDuringComposition",

        )
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ComposeView>(R.id.composeView).setContent {
            val viewModel: QuotesFragmentViewModel by viewModels()

            val list = viewModel.quotesListViewModel.collectAsState()

            val quotesListSize = remember {
                mutableStateOf(0)
            }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color(R.color.custom_blue)
            ) {

                LongPressDraggable(modifier = Modifier.fillMaxSize()) {
                    QuotesLazyColumn(list.value, viewModel, quotesListSize)
                    PersonListContainer(viewModel, quotesListSize)
                }
            }
        }
    }
}

@Composable
private fun QuotesLazyColumn(
    quotesList: List<QuoteItem>,
    viewModel: QuotesFragmentViewModel,
    quotesListSize: MutableState<Int>
) {
    quotesListSize.value = quotesList.count { !it.isAnswered.value }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(items = quotesList.distinct()) { quote ->

            if (!quote.isAnswered.value)
                QuoteItemCard(quoteItem = quote)
        }
if (quotesListSize.value == 0) viewModel.updateList()

    }
}

@Composable
fun BoxScope.PersonListContainer(
    viewModel: QuotesFragmentViewModel,
    quotesListSize: MutableState<Int>
) {
    LazyVerticalGrid(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .background(
                Color.LightGray,
                shape = RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp)
            )
            .padding(vertical = 10.dp, horizontal = 4.dp)
            .align(Alignment.BottomCenter),
        columns = GridCells.Fixed(count = 2)
    ) {

        items(items = author) { person ->
            PersonCard(person, viewModel, quotesListSize)
        }
    }

}

@Composable
fun PersonCard(
    person: Person,
    viewModel: QuotesFragmentViewModel,
    quotesListSize: MutableState<Int>
) {
    val quoteItems = remember {
        mutableStateMapOf<Int, QuoteItem>()
    }

    DropTarget<QuoteItem>(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth(0.5f)
    ) { isInBound, authorItem ->
        val bgColor = if (isInBound) {
            Color.Yellow
        } else {
            Color.White
        }
        authorItem?.let {
            if (isInBound) {
                if (it.author == person.name) {

                    println("Right!!")
                } else {

                    println("wrong!")
                }
                it.isAnswered.value = true
                quotesListSize.value --
                println(quotesListSize.value)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
                .background(
                    bgColor,
                    RoundedCornerShape(16.dp)
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = person.profile), contentDescription = null,
                modifier = Modifier
                    .size(100.dp),

                contentScale = ContentScale.FillHeight
            )


            /*            Text(
                            text = person.name,
                            fontSize = 18.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )*/

        }
    }
}

@Composable
fun QuoteItemCard(quoteItem: QuoteItem) {

    Card(
        elevation = 10.dp, backgroundColor = Color.White,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
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
                        textAlign = TextAlign.Center,
                    )

                }
            }
            Spacer(modifier = Modifier.width(20.dp))

        }
    }
}

