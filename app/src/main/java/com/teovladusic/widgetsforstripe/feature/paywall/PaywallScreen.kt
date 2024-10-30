package com.teovladusic.widgetsforstripe.feature.paywall

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.teovladusic.widgetsforstripe.R
import com.teovladusic.widgetsforstripe.core.design_system.components.WFSPrimaryButton
import com.teovladusic.widgetsforstripe.core.design_system.theme.Dimens
import com.teovladusic.widgetsforstripe.core.design_system.theme.NunitoFontFamily
import com.teovladusic.widgetsforstripe.core.design_system.theme.Primary
import com.teovladusic.widgetsforstripe.core.domain.model.paywall.PurchasableProduct
import com.teovladusic.widgetsforstripe.core.util.extension.findActivity

@Destination
@Composable
fun PaywallScreen(viewModel: PaywallViewModel = hiltViewModel()) {
    val productDetailsList by viewModel.productDetailsList.collectAsStateWithLifecycle()
    Content(products = productDetailsList, executeAction = viewModel::executeAction)
}

@Composable
private fun Content(products: List<PurchasableProduct>, executeAction: (PaywallAction) -> Unit) {
    val activity = LocalContext.current.findActivity()

    Scaffold(modifier = Modifier.fillMaxSize(), containerColor = Color.Black) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = Dimens.spacing_m)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height(Dimens.spacing_xxs))
                Header { executeAction(PaywallAction.ClosePaywall) }
                Spacer(Modifier.height(Dimens.spacing_xl))
                BulletPoints()
                Spacer(Modifier.height(Dimens.spacing_m))
                Products(
                    products = products,
                    onClick = { executeAction(PaywallAction.SelectProduct(it)) },
                    onButtonClick = { executeAction(PaywallAction.BuyProduct(activity)) }
                )
            }
        }
    }
}

@Composable
private fun Products(
    products: List<PurchasableProduct>,
    onClick: (String) -> Unit,
    onButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Dimens.spacing_m)
    ) {
        products.forEach { product ->
            ProductItem(product = product, onClick = onClick)
        }
        Spacer(Modifier.height(Dimens.spacing_m))
        WFSPrimaryButton(
            text = if (products.any { it.isSelected }) stringResource(R.string.unlock) else stringResource(
                R.string.try_for_free
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(64.dp),
            onClick = onButtonClick,
            backgroundColor = Color(color = 0xFFC35554),
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                text = "Restore purchases",
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier.clickable { }
            )
            Text(
                text = "  \u2022  Terms",
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier.clickable { }
            )
            Text(
                text = "  \u2022  Privacy Policy",
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier.clickable { }
            )
        }
    }
}

@Composable
private fun ProductItem(product: PurchasableProduct, onClick: (String) -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    val color by animateColorAsState(
        targetValue = if (product.isSelected) Primary else Color.White.copy(alpha = .5f),
        label = "color_anim"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = shape)
            .clickable { onClick(product.id) }
            .border(width = 1.dp, color = color, shape = shape)
            .padding(Dimens.spacing_m)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                if (product.isSelected) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(color = Primary, shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier
                                .size(14.dp)
                                .align(Alignment.Center)
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(color = Color.White.copy(alpha = .5f), shape = CircleShape)
                    )
                }
                Spacer(Modifier.width(Dimens.spacing_m))
                Text(
                    text = product.name,
                    color = Color.White,
                    fontFamily = NunitoFontFamily,
                    fontSize = 17.sp
                )
            }
            product.discountPercent?.let {
                Text(
                    text = "$it% OFF",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = NunitoFontFamily,
                    modifier = Modifier
                        .offset(x = 6.dp, y = (-6).dp)
                        .background(color = Primary, shape = RoundedCornerShape(12.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
        Spacer(Modifier.height(Dimens.spacing_xs))
        Text(text = product.price, color = Color.White, fontSize = 16.sp)
    }
}

@Composable
private fun BulletPoints() {
    BulletPoint(
        title = stringResource(R.string.paywall_bullet_1_title),
        desc = stringResource(R.string.paywall_bullet_1_desc)
    )
    Spacer(Modifier.height(Dimens.spacing_m))
    BulletPoint(
        title = stringResource(R.string.paywall_bullet_2_title),
        desc = stringResource(R.string.paywall_bullet_2_desc)
    )
    Spacer(Modifier.height(Dimens.spacing_m))
    BulletPoint(
        title = stringResource(R.string.paywall_bullet_4_title),
        desc = stringResource(R.string.paywall_bullet_4_desc),
        emoji = "❤️"
    )
}

@Composable
private fun BulletPoint(title: String, desc: String, emoji: String = "✅") {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "$emoji $title",
            color = Color.White,
            fontSize = 18.sp,
            fontFamily = NunitoFontFamily,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = desc,
            color = Color.White,
            fontSize = 14.sp,
            fontFamily = NunitoFontFamily,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
private fun Header(onCloseClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        IconButton(onClick = onCloseClick, modifier = Modifier.offset(x = (-12).dp, y = (-8).dp)) {
            Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = Primary)
        }
        Image(
            painter = painterResource(R.drawable.img_paywall_premium_banner),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(fraction = .55f)
                .align(Alignment.Center),
            contentScale = ContentScale.FillWidth
        )
    }
}

@Preview
@Composable
private fun Preview() {
    Content(
        products = listOf(
            PurchasableProduct(
                id = "1",
                name = stringResource(R.string.monthly),
                price = " 4.99",
                isSelected = false,
                isBought = false,
                discountPercent = null,
            ),
            PurchasableProduct(
                id = "2",
                name = stringResource(R.string.annual),
                price = " 4.99",
                isSelected = false,
                isBought = false,
                discountPercent = 67,
            ),
            PurchasableProduct(
                id = "3",
                name = stringResource(R.string.lifetime),
                price = " 4.99",
                isSelected = true,
                isBought = false,
                discountPercent = null,
            ),
        ),
        executeAction = {}
    )
}