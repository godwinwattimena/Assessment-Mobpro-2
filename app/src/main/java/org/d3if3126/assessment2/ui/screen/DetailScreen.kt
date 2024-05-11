package org.d3if3126.assessment2.ui.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3126.assessment2.R
import org.d3if3126.assessment2.database.DanusDb
import org.d3if3126.assessment2.ui.theme.Assessment2Theme
import org.d3if3126.assessment2.util.ViewModelFactory

const val KEY_ID_DANUS = "idDanus"

val items = listOf("Makanan", "Minuman")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id: Long? = null) {
    val context = LocalContext.current
    val db = DanusDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var namaToko by remember { mutableStateOf("") }
    var namaBarang by remember { mutableStateOf("") }
    var hargaBarang by remember { mutableStateOf("") }
    var jenisBarang by remember { mutableStateOf(items[0]) }

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        if (id == null) return@LaunchedEffect
        val data = viewModel.getDanus(id) ?: return@LaunchedEffect
        namaToko = data.namaToko
        namaBarang = data.namaBarang
        hargaBarang = data.hargaBarang
        jenisBarang = data.jenisBarang
    }
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            modifier = Modifier
                                .size(70.dp,70.dp),
                            painter = painterResource(id = R.drawable.baseline_navigate_before_24),
                            contentDescription = stringResource(
                                id = R.string.kembali
                            ),
                            tint = warnaPutih
                        )
                    }
                },
                title = {
                    if (id == null)
                        Text(text = stringResource(id = R.string.tambah_danus), fontSize = 26.sp, fontWeight = FontWeight(600))
                    else
                        Text(text = stringResource(id = R.string.edit_danus), fontSize = 26.sp, fontWeight = FontWeight(600))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = warnaCyan,
                    titleContentColor = warnaPutih
                ),
                actions = {
                    IconButton(onClick = {
                        if (namaToko == "" || namaBarang == "" || hargaBarang == "" || jenisBarang == "") {
                            Toast.makeText(context, R.string.invalid, Toast.LENGTH_SHORT).show()
                            return@IconButton
                        }
                        if (id == null) {
                            viewModel.insert(namaToko, namaBarang, hargaBarang, jenisBarang)
                        } else {
                            viewModel.update(id, namaToko, namaBarang, hargaBarang, jenisBarang)
                        }
                        navController.popBackStack()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_create_24),
                            contentDescription = stringResource(
                                id = R.string.simpan
                            ),
                            tint = warnaPutih
                        )
                    }
                    if (id != null) {
                        DeleteAction { showDialog = true }
                        DisplayAlertDialog(
                            openDialog = showDialog,
                            onDismissRequest = { showDialog = false }) {
                            showDialog = false
                            viewModel.delete(id)
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    ) { padding ->
        FormDanus(
            modifier = Modifier.padding(padding),
            namaToko = namaToko,
            onNameShopChange = { namaToko = it },
            namaBarang = namaBarang,
            onNameItemsChange = { namaBarang = it },
            hargaBarang = hargaBarang,
            onPriceChange = { hargaBarang = it },
            jenisBarang = jenisBarang,
            onTypeChange = { jenisBarang = it }
        )
    }
}

@Composable
fun DeleteAction(delete: () -> Unit) {
    var expanded by remember {
        mutableStateOf(false)
    }
    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(id = R.string.lainnya),
            tint = warnaPutih
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.hapus_danus)) },
                onClick = {
                    expanded = false
                    delete()
                },
            )
        }
    }
}


@Composable
fun FormDanus(
    modifier: Modifier,
    namaToko: String, onNameShopChange: (String) -> Unit,
    namaBarang: String, onNameItemsChange: (String) -> Unit,
    hargaBarang: String, onPriceChange: (String) -> Unit,
    jenisBarang: String, onTypeChange: (String) -> Unit
) {
    val hargaError by rememberSaveable { mutableStateOf(false) }


    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 86.dp, start = 16.dp, bottom = 56.dp)
    ){
        Text("Danus Tel-U :", fontSize = 26.sp, color = warnaCyan, fontWeight = FontWeight(600))
    }

    Column (
        modifier = Modifier
            .padding(top = 56.dp)
    ){
        Column(
            modifier = modifier
                .fillMaxHeight(0.95f)
                .padding(26.dp)
                .background(color = warnaCyan, RoundedCornerShape(10.dp)),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ){
                Column (
                    modifier = Modifier
                        .padding(top = 8.dp)
                ){
                    Text(
                        "Nama Toko :",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = warnaPutih,
                        modifier = Modifier
                            .padding(start = 26.dp)
                    )
                    Spacer(modifier = Modifier.padding(top = 16.dp))
                    OutlinedTextField(
                        value = namaToko,
                        onValueChange = { onNameShopChange(it) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_store_24),
                                contentDescription = null,
                                tint = warnaBlack
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.Next
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = warnaPutih,
                            unfocusedIndicatorColor = warnaPutih,
                            focusedContainerColor = warnaPutih,
                            unfocusedContainerColor = warnaPutih,
                            focusedTextColor = warnaBlack,
                            unfocusedTextColor = warnaBlack
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                    )
                }
                Column (
                    modifier = Modifier
                        .padding(top = 36.dp)
                ){
                    Text(
                        "Nama Barang :",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = warnaPutih,
                        modifier = Modifier
                            .padding(start = 26.dp)
                    )
                    Spacer(modifier = Modifier.padding(top = 16.dp))
                    OutlinedTextField(
                        value = namaBarang,
                        onValueChange = { onNameItemsChange(it) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_storage_24),
                                contentDescription = null,
                                tint = warnaBlack
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.Next
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = warnaPutih,
                            unfocusedIndicatorColor = warnaPutih,
                            focusedContainerColor = warnaPutih,
                            unfocusedContainerColor = warnaPutih,
                            focusedTextColor = warnaBlack,
                            unfocusedTextColor = warnaBlack
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                    )
                }
                Column (
                    modifier = Modifier
                        .padding(top = 36.dp)
                ){
                    Text(
                        "Harga Barang :",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = warnaPutih,
                        modifier = Modifier
                            .padding(start = 26.dp)
                    )
                    Spacer(modifier = Modifier.padding(top = 16.dp))
                    OutlinedTextField(
                        value = hargaBarang,
                        onValueChange = { onPriceChange(it) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_attach_money_24),
                                contentDescription = null,
                                tint = warnaBlack
                            )
                        },
                        isError = hargaError,
                        trailingIcon = {
                            IconPicker(
                                hargaError,
                                "Rp"
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = warnaPutih,
                            unfocusedIndicatorColor = warnaPutih,
                            focusedContainerColor = warnaPutih,
                            unfocusedContainerColor = warnaPutih,
                            focusedTextColor = warnaBlack,
                            unfocusedTextColor = warnaBlack
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                    )
                }
                Column (
                    modifier = Modifier
                        .padding(top = 36.dp)
                ){
                    Text(
                        "Jenis Barang :",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = warnaPutih,
                        modifier = Modifier
                            .padding(start = 26.dp)
                    )
                    Spacer(modifier = Modifier.padding(top = 16.dp))
                    Column(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(color = warnaPutih)
                    ) {
                        items.forEach { text ->
                            RadioOptions(
                                label = text,
                                isSelected = jenisBarang == text,
                                modifier = Modifier
                                    .selectable(
                                        selected = jenisBarang == text,
                                        onClick = { onTypeChange(text) },
                                        role = Role.RadioButton
                                    )
                                    .padding(8.dp), onTypeChange
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun IconPicker(isError: Boolean, unit: String) {
    if (isError) {
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    }else {
        Text(
            text = unit,
            fontWeight = FontWeight.SemiBold,
            color = warnaBlack
        )
    }
}

@Composable
fun RadioOptions(
    label: String,
    isSelected: Boolean,
    modifier: Modifier,
    onTypeChange: (String) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Column (
            modifier = Modifier
                .padding(start = 8.dp)
        ){
            Icon(
                painter = painterResource(id = R.drawable.baseline_fastfood_24),
                contentDescription = null,
                tint = warnaBlack
            )
        }
        RadioButton(
            selected = isSelected,
            onClick = { onTypeChange(label) },
            colors = RadioButtonDefaults.colors(
                selectedColor = warnaCyan
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(8.dp),
            color = warnaBlack
        )
    }
}


@Composable
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun DetailScreenPreview() {
    Assessment2Theme {
        DetailScreen(rememberNavController())
    }
}