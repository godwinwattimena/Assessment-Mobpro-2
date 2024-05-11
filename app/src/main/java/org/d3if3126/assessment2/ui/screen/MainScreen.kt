package org.d3if3126.assessment2.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3126.assessment2.R
import org.d3if3126.assessment2.database.DanusDb
import org.d3if3126.assessment2.model.Danus
import org.d3if3126.assessment2.navigation.Screen
import org.d3if3126.assessment2.ui.theme.Assessment2Theme
import org.d3if3126.assessment2.util.SettingsDataStore
import org.d3if3126.assessment2.util.ViewModelFactory


val warnaCyan = Color(0xFF3DC7D3)
val warnaPutih = Color(0xFFFFFFFF)
val warnaGray = Color(0xFFCCCCCC)
val warnaBlack = Color(0xFF000000)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {

    val dataStore = SettingsDataStore(LocalContext.current)
    val showList by dataStore.layoutFlow.collectAsState(true)



    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name), fontSize = 26.sp, fontWeight = FontWeight(600))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = warnaCyan,
                    titleContentColor = warnaPutih
                ),
                actions = {
                    IconButton(onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            dataStore.saveLayout(!showList)
                        }
                    }) {
                        Icon(
                            painter = painterResource(
                                if (showList) R.drawable.baseline_grid_view_24
                                else R.drawable.baseline_view_list_24
                            ),
                            contentDescription = stringResource(
                                if (showList) R.string.grid
                                else R.string.list
                            ),
                            tint = warnaPutih
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.FormBaru.route)
                },
                containerColor = warnaCyan
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.tambah_danus),
                    tint = warnaPutih
                )
            }
        }
    ) { padding ->
        ScreenContent(showList, Modifier.padding(padding), navController)
    }

}

@Composable
fun ScreenContent(showList: Boolean, modifier: Modifier, navController: NavHostController) {
    val context = LocalContext.current
    val db = DanusDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: MainViewModel = viewModel(factory = factory)
    val data by viewModel.data.collectAsState()

    if (data.isEmpty()){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.list_kosong),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
    else {
        if (showList) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 86.dp, start = 20.dp)
            ) {
                Text(
                    "Danus Tel-U :",
                    fontSize = 26.sp,
                    color = warnaCyan,
                    fontWeight = FontWeight(600))
            }
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(top = 56.dp),
                contentPadding = PaddingValues(bottom = 84.dp)
            ) {
                items(data) {
                    ListItem(danus = it) {
                        navController.navigate(Screen.FormUbah.withId(it.id))
                    }
                }
            }
        }
        else {
            LazyVerticalStaggeredGrid(
                modifier = modifier.fillMaxSize(),
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 84.dp)
            ) {
                items(data) {
                    GridItem(danus = it) {
                        navController.navigate(Screen.FormUbah.withId(it.id))
                    }
                }
            }
        }
    }
}

@Composable
fun ListItem(danus: Danus, onClick: () -> Unit) {

    Column (
        modifier = Modifier
            .padding(16.dp)
            .shadow(16.dp)
    ){
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .background(color = warnaCyan, RoundedCornerShape(10.dp, 10.dp))
        ){
            Row (
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Column {
                    Text(
                        modifier = Modifier
                            .padding(bottom = 6.dp),
                        text = danus.namaToko,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 30.sp,
                        color = warnaPutih
                    )
                    Text(
                        text = danus.namaBarang,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = warnaPutih
                    )
                }

                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_forward_24),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp),
                    tint = warnaPutih
                )

                Column (
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = danus.jenisBarang,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = warnaPutih
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = warnaGray, RoundedCornerShape(0.dp, 0.dp, 10.dp, 10.dp)),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Row {
                    Text(
                        "Price : ",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = warnaBlack
                    )
                    Text(
                        text = danus.hargaBarang,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.ExtraBold,
                        color = warnaBlack
                    )
                }
                Text(
                    text = danus.tanggal,
                    fontWeight = FontWeight(1000),
                    fontSize = 12.sp,
                    color = warnaBlack
                )
            }
        }
    }
}

@Composable
fun GridItem(danus: Danus, onClick: () -> Unit) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = warnaCyan,
        )
    ) {
        Column (
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Column (
                modifier = Modifier
                    .background(color = warnaPutih, RoundedCornerShape(10.dp))
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = danus.namaToko,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = warnaBlack
                )
            }
            Text(
                text = danus.namaBarang,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight(1000),
                color = warnaPutih
            )
            Row {
                Text(
                    "Rp : ",
                    fontWeight = FontWeight.SemiBold,
                    color = warnaPutih
                )
                Text(
                    text = danus.hargaBarang,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold,
                    color = warnaPutih
                )
            }
            Text(
                text = danus.jenisBarang,
                fontWeight = FontWeight.SemiBold,
                color = warnaPutih
            )
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ){
                Text(
                    text = danus.tanggal,
                    fontWeight = FontWeight.SemiBold,
                    color = warnaPutih
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ScreenPreview() {
    Assessment2Theme {
        MainScreen(rememberNavController())
    }
}