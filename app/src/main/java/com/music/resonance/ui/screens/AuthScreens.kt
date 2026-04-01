package com.music.resonance.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import com.music.resonance.R
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

private val AuthBg = Color(0xFF1B1D22)
private val FieldBg = Color(0xFF444444)
private val TealAccent = Color(0xFF23A7A2)
private val ButtonGray = Color(0xFF4A4D54)
private val BottomBarBg = Color(0xFF343438)

data class RegisterFormPayload(
    val name: String,
    val email: String,
    val password: String,
    val isArtistic: Boolean,
    val artisticName: String,
    val description: String
)

@Composable
fun WelcomeAuthScreen(
    onCreateAccount: () -> Unit,
    onLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AuthBg)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ResonanceBranding()
        Spacer(modifier = Modifier.height(56.dp))
        AuthPrimaryButton(text = "Criar Conta", onClick = onCreateAccount)
        Spacer(modifier = Modifier.height(16.dp))
        AuthPrimaryButton(text = "Entrar na Conta", onClick = onLogin)
    }
}

@Composable
fun RegisterAuthScreen(
    onBack: () -> Unit,
    register: suspend (RegisterFormPayload) -> Long?,
    onRegistered: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isArtistic by remember { mutableStateOf(false) }
    var artisticName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val scroll = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AuthBg)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Voltar",
                tint = Color.White,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onBack() }
                    .padding(8.dp)
                    .size(28.dp)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scroll)
                .padding(horizontal = 24.dp)
        ) {
            ResonanceBranding()
            Spacer(modifier = Modifier.height(28.dp))
            AuthLabeledField(label = "Nome", value = name, onValueChange = { name = it })
            Spacer(modifier = Modifier.height(14.dp))
            AuthLabeledField(
                label = "E-mail",
                value = email,
                onValueChange = { email = it },
                keyboardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(14.dp))
            AuthLabeledField(
                label = "Senha",
                value = password,
                onValueChange = { password = it },
                isPassword = true
            )
            Spacer(modifier = Modifier.height(18.dp))
            RowToggle(
                leftLabel = "Uso Pessoal",
                rightLabel = "Uso Artistico",
                isRightSelected = isArtistic,
                onLeft = { isArtistic = false },
                onRight = { isArtistic = true }
            )
            AnimatedVisibility(
                visible = isArtistic,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(14.dp))
                    AuthLabeledField(
                        label = "Nome Artistico",
                        value = artisticName,
                        onValueChange = { artisticName = it }
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    AuthLabeledField(
                        label = "Descrição",
                        value = description,
                        onValueChange = { description = it }
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            errorMessage?.let { msg ->
                Text(
                    text = msg,
                    color = Color(0xFFEF9A9A),
                    fontSize = 13.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(BottomBarBg)
                .clickable(enabled = !loading) {
                    errorMessage = null
                    when {
                        name.isBlank() -> errorMessage = "Informe o nome."
                        email.isBlank() -> errorMessage = "Informe o e-mail."
                        password.length < 4 -> errorMessage = "Senha muito curta."
                        isArtistic && artisticName.isBlank() -> errorMessage =
                            "Informe o nome artistico."

                        isArtistic && description.isBlank() -> errorMessage = "Informe a descricao."
                    }
                    if (errorMessage != null) return@clickable
                    scope.launch {
                        loading = true
                        try {
                            val payload = RegisterFormPayload(
                                name = name.trim(),
                                email = email.trim(),
                                password = password,
                                isArtistic = isArtistic,
                                artisticName = artisticName.trim(),
                                description = description.trim()
                            )
                            val id = register(payload)
                            if (id != null) onRegistered(id)
                            else errorMessage = "Nao foi possivel cadastrar. Tente novamente."
                        } finally {
                            loading = false
                        }
                    }
                }
                .padding(vertical = 18.dp),
            contentAlignment = Alignment.Center
        ) {
            if (loading) {
                CircularProgressIndicator(color = TealAccent, modifier = Modifier.size(28.dp))
            } else {
                Text(
                    text = "OK!",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun LoginAuthScreen(
    onBack: () -> Unit,
    login: suspend (String, String) -> Long?,
    onLoggedIn: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val scroll = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AuthBg)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Voltar",
                tint = Color.White,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onBack() }
                    .padding(8.dp)
                    .size(28.dp)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scroll)
                .padding(horizontal = 24.dp)
        ) {
            ResonanceBranding()
            Spacer(modifier = Modifier.height(36.dp))
            AuthLabeledField(
                label = "E-mail",
                value = email,
                onValueChange = { email = it },
                keyboardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(14.dp))
            AuthLabeledField(
                label = "Senha",
                value = password,
                onValueChange = { password = it },
                isPassword = true
            )
            Spacer(modifier = Modifier.height(20.dp))
            errorMessage?.let { msg ->
                Text(
                    text = msg,
                    color = Color(0xFFEF9A9A),
                    fontSize = 13.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(BottomBarBg)
                .clickable(enabled = !loading) {
                    errorMessage = null
                    when {
                        email.isBlank() -> errorMessage = "Informe o e-mail."
                        password.isEmpty() -> errorMessage = "Informe a senha."
                    }
                    if (errorMessage != null) return@clickable
                    scope.launch {
                        loading = true
                        try {
                            val id = login(email.trim(), password)
                            if (id != null) onLoggedIn(id)
                            else errorMessage = "E-mail ou senha invalidos."
                        } finally {
                            loading = false
                        }
                    }
                }
                .padding(vertical = 18.dp),
            contentAlignment = Alignment.Center
        ) {
            if (loading) {
                CircularProgressIndicator(color = TealAccent, modifier = Modifier.size(28.dp))
            } else {
                Text(
                    text = "Entrar",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
private fun ResonanceBranding() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(120.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(120.dp) // Filled the box size
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Resonance",
            color = Color.White,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun AuthPrimaryButton(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(ButtonGray)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun AuthLabeledField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, color = Color.White, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = label != "Descrição",
            maxLines = if (label == "Descrição") 4 else 1,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = FieldBg,
                unfocusedContainerColor = FieldBg,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = TealAccent
            ),
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@Composable
private fun RowToggle(
    leftLabel: String,
    rightLabel: String,
    isRightSelected: Boolean,
    onLeft: () -> Unit,
    onRight: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ToggleChip(
            text = leftLabel,
            selected = !isRightSelected,
            modifier = Modifier.weight(1f),
            onClick = onLeft
        )
        ToggleChip(
            text = rightLabel,
            selected = isRightSelected,
            modifier = Modifier.weight(1f),
            onClick = onRight
        )
    }
}

@Composable
private fun ToggleChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg = if (selected) TealAccent else Color(0xFF2C2F35)
    val fg = if (selected) Color(0xFFDEFFFF) else Color(0xFFE5E5E5)
    Box(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(bg)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = fg, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
    }
}
