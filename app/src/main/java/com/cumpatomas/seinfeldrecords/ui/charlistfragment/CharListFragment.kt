package com.cumpatomas.seinfeldrecords.ui.charlistfragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieDrawable
import com.cumpatomas.seinfeldrecords.R
import com.cumpatomas.seinfeldrecords.data.model.CharGestures
import com.cumpatomas.seinfeldrecords.data.model.SeinfeldChar
import com.cumpatomas.seinfeldrecords.databinding.CharListFragmentBinding
import com.robinhood.ticker.TickerUtils
import com.robinhood.ticker.TickerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharListFragment : Fragment() {
    private val viewmodel: CharListFragmentViewModel by viewModels()
    private val adapter = CharListFragmentAdapter()
    private var _binding: CharListFragmentBinding? = null
    private val binding get() = _binding!!
    private var gesturesList = emptyList<CharGestures>()
    private var animation = mutableStateOf(false)
    private lateinit var tickerView: TickerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CharListFragmentBinding.inflate(layoutInflater)
        tickerView = binding.pointsTickerViewCharList
        return binding.root
    }

    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val compose = view.findViewById<ComposeView>(R.id.composeViewHome)
        compose.setContent {
            val animateShape = remember { androidx.compose.animation.core.Animatable(8f) }
            val animateRotation = remember { androidx.compose.animation.core.Animatable(0f) }
            val initialColor = colorResource(id = R.color.secondaryColorDark)
            val targetColor = Color(0xFFede0dc)
            val animateColor = remember { Animatable(initialColor) }

            AnimatedVisibility(
                visible = animation.value,
                enter = slideInVertically(
                    animationSpec = tween(1000),
                    initialOffsetY = {
                        -it
                    }
                ),
                exit = slideOutVertically(
                    animationSpec = tween(1000),
                    targetOffsetY = {
                        it
                    }
                )
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .clickable { }
                        .alpha(0.5f)
                        .background(Color.Black),
                    horizontalAlignment = CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                }
                Column(
                    horizontalAlignment = CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Box(
                        contentAlignment = Center,
                    ) {
                        Text(
                            text = "You've got",
                            fontSize = 35.sp,
                            color = colorResource(id = R.color.white),
                            fontFamily = FontFamily(Font(R.font.type_font)),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        contentAlignment = Center,
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(colorResource(id = R.color.secondaryColorDark))
                            .border(
                                width = Dp(animateShape.value),
                                color = animateColor.asState().value,
                                shape = CircleShape,
                            )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.circle_yellow),
                            contentDescription = "user points",
                            Modifier
                                .size(100.dp),
                        )

                        Column(
                            horizontalAlignment = CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.rotate(animateRotation.value)
                        ) {
                            val text = viewmodel.userPoints.value.toString()
                            Text(
                                text = viewmodel.userPoints.value.toString(),
                                fontSize = 22.sp,
                                color = colorResource(id = R.color.secondaryColorDark),
                                fontFamily = FontFamily(Font(R.font.type_font)),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = when (text) {
                                    "1" -> "point"
                                    else ->"points!"
                                },
                            fontSize = 18.sp,
                            color = colorResource(id = R.color.secondaryColorDark),
                            fontFamily = FontFamily(Font(R.font.type_font))
                            )


                        }


                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = when (viewmodel.userPoints.value) {
                            in 0..3 -> "You're a disgrace\n to the uniform"
                            in 3..5 -> "Your mind is as barren\n as the surface of the moon"
                            in 5..10 -> "And you wanted\n to be my latex salesman"
                            in 10..15 -> "I’m speechless\n I’m without speech"
                            in 15..20 -> "Yama hama, it's fright night!"
                            in 20..30 -> "Alright, high-five!"
                            in 30..40 -> "you're like a phoenix\n rising from Arizona!"
                            else ->"You're no longer\n pre-occupied with sex\n so your mind is\n able to focus!"
                        },
                        textAlign = TextAlign.Center,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.white),
                        fontFamily = FontFamily(Font(R.font.type_font))
                    )
                }
            }

            LaunchedEffect(animateShape) {
                animateShape.animateTo(
                    targetValue = 2f,
                    animationSpec = repeatable(
                        animation = tween(
                            durationMillis = 1000,
                            easing = LinearEasing,
                            delayMillis = 300
                        ),
                        repeatMode = RepeatMode.Reverse,
                        iterations = 3
                    )
                )
            }

            LaunchedEffect(animateColor) {
                animateColor.animateTo(
                    targetValue = targetColor,
                    animationSpec = repeatable(
                        animation = tween(
                            durationMillis = 2000,
                            easing = LinearEasing,
                            delayMillis = 500
                        ),
                        repeatMode = RepeatMode.Reverse,
                        iterations = 3
                    )
                )
            }

            LaunchedEffect(animateRotation) {
                animateRotation.animateTo(
                    targetValue = 360f,
                    animationSpec = repeatable(
                        animation = tween(
                            durationMillis = 1000,
                            easing = LinearEasing,
                            delayMillis = 1300
                        ),
                        repeatMode = RepeatMode.Reverse,
                        iterations = 1
                    )
                )
            }
        }
        compose.bringToFront()

        binding.tvLoading.isGone = true
        initListeners()
        initCollectors()
        initRecyclerView()
        setLottieAnimation()
    }

    private fun setLottieAnimation() {
        binding.tvLoading.setAnimation(R.raw.loading_yellow_white)
        binding.tvLoading.repeatMode = LottieDrawable.RESTART
        binding.tvLoading.bringToFront()
    }

    private fun initListeners() {
        adapter.onItemClickListener = {
            if (!it.completed) {
                val action: NavDirections =
                    CharListFragmentDirections.actionCharListFragmentToCharGesturesFragment(it.shortName)
                findNavController().navigate(action)
            } else {
            }
        }
    }

    private fun initCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.getUserPoints()

                launch {
                    viewmodel.animationIn.collectLatest { state ->
                        animation.value = state
                    }
                }
                launch {
                    viewmodel.userPoints.collectLatest { points ->
                        println("PUNTOS: $points")

                        tickerView.setCharacterLists(TickerUtils.provideNumberList())
                        tickerView.text = points.toString()
                    }
                }
                launch {
                    viewmodel.pointsCircle.collectLatest { state ->
                        binding.clBottomBarContainer.isVisible = state
                    }
                }
                launch {
                    viewmodel.gesturesList.collectLatest { gestures ->
                        gesturesList = gestures
                        adapter.charGestures = gestures
                    }
                }

                launch {
                    viewmodel.viewState.collectLatest { state ->
                        updateUiState(state)
                    }
                }

                launch {
                    viewmodel.charList.collectLatest { list ->
                        val new = list.toMutableList()
                        for (i in new) {
                            for (g in gesturesList.filter { it.char == i.shortName })
                                i.completed = g.completed
                        }
                        updateList(new)
                    }
                }
            }
        }
    }

    private fun updateList(list: List<SeinfeldChar>) {
        val newList = list.toMutableList()
        for (i in newList) {
            for (g in gesturesList.filter { it.char == i.shortName })
                i.completed = g.completed
        }
        adapter.setList(newList)
    }

    private fun initRecyclerView() {
        val recyclerView =
            binding.rvRecyclerFragment3// encontramos el Recycler del Main LAYOUT xml
        recyclerView.layoutManager =
            LinearLayoutManager(context) // si cambiamos el Manager aqui podriamos hacer listados de GRID u otro tipo! Investigar!
        recyclerView.adapter = this.adapter
    }

    private fun updateUiState(state: CharListViewState) {
        binding.tvLoading.isVisible = state.loading
    }
}