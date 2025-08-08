package com.meenbeese.chronos.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import androidx.compose.foundation.layout.size

@Composable
fun ClockPageView(
    fragments: List<@Composable () -> Unit>,
    backgroundPainter: Painter,
    pageIndicatorVisible: Boolean,
    modifier: Modifier = Modifier
) {
    val backgroundDimConstant = 0.15f
    val pagerState = rememberPagerState { fragments.size }

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Image(
            painter = backgroundPainter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Black.copy(alpha = backgroundDimConstant))
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.matchParentSize()
        ) { page ->
            fragments[page]()
        }
        val currentMonth = remember { LocalDate.now().monthValue }
        val monthPainter = getMonthImagePainter(currentMonth)

        Image(
            painter = monthPainter,
            contentDescription = "Ảnh theo tháng",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp)
                .size(100.dp)
        )

        if (pageIndicatorVisible) {
            val currentPage = pagerState.currentPage
            val pageOffset = pagerState.currentPageOffsetFraction

            PageIndicatorView(
                currentPage = currentPage,
                pageOffset = pageOffset,
                pageCount = fragments.size,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 48.dp)
            )
        }
    }
}

// Đặt ở đầu hoặc cuối file, ngoài ClockPageView
@Composable
fun getMonthImagePainter(month: Int): Painter {
    val context = LocalContext.current
    val resId = remember(month) {
        context.resources.getIdentifier("thang$month", "drawable", context.packageName)
    }
    return painterResource(id = resId)
}

