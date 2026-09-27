package com.example

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Undo
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkOutline
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceContainer
import com.example.ui.theme.DarkSurfaceContainerHigh
import com.example.ui.theme.TurquoiseGlow
import com.example.ui.theme.TurquoiseOnPrimary
import com.example.ui.theme.TurquoisePrimary
import com.example.ui.theme.TurquoiseSecondary
import com.example.ui.theme.TurquoiseTertiary
import com.example.ui.theme.TurquoiseTrack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HydrationScreen(
  viewModel: HydrationViewModel,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  Scaffold(
    modifier = modifier.fillMaxSize(),
    containerColor = DarkBackground,
    topBar = {
      CenterAlignedTopAppBar(
        title = {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(
              imageVector = Icons.Filled.WaterDrop,
              contentDescription = "Goutte d'eau",
              tint = TurquoisePrimary,
              modifier = Modifier.size(24.dp)
            )
            Text(
              text = "Hydratation",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
              ),
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        },
        actions = {
          IconButton(
            onClick = { viewModel.requestReset() },
            modifier = Modifier.testTag("top_bar_reset_button")
          ) {
            Icon(
              imageVector = Icons.Filled.Refresh,
              contentDescription = "Réinitialiser",
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
          containerColor = DarkBackground
        )
      )
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 8.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

      // Target Header Label
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = DarkSurfaceContainer,
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkOutline.copy(alpha = 0.5f))
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = null,
            tint = TurquoiseSecondary,
            modifier = Modifier.size(16.dp)
          )
          Text(
            text = "Objectif quotidien : 2,0 Litres (2000 ml)",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      // Circular Progress Indicator Container
      HydrationProgressCircle(
        currentMl = uiState.currentIntakeMl,
        targetMl = uiState.targetMl,
        progress = uiState.progress,
        isGoalReached = uiState.isGoalReached,
        modifier = Modifier.testTag("hydration_progress_circle")
      )

      // Stats Summary Row
      StatsRow(
        currentMl = uiState.currentIntakeMl,
        targetMl = uiState.targetMl,
        glassesCount = uiState.glassesCount,
        remainingMl = uiState.remainingMl
      )

      // Main Action Buttons
      ActionButtonsSection(
        onAddWater = { viewModel.addWater(250) },
        onReset = { viewModel.requestReset() },
        isGoalReached = uiState.isGoalReached
      )

      // Recent History & Undo
      if (uiState.history.isNotEmpty()) {
        IntakeHistoryCard(
          history = uiState.history,
          onUndo = { viewModel.undoLastEntry() }
        )
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }

  // Reset Confirmation Dialog
  if (uiState.showResetConfirmation) {
    AlertDialog(
      onDismissRequest = { viewModel.dismissResetConfirmation() },
      icon = {
        Icon(
          imageVector = Icons.Filled.Refresh,
          contentDescription = null,
          tint = TurquoisePrimary
        )
      },
      title = {
        Text(
          text = "Réinitialiser la journée ?",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface
        )
      },
      text = {
        Text(
          text = "Voulez-vous remettre votre consommation à 0 ml ? L'historique d'aujourd'hui sera effacé.",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      },
      confirmButton = {
        Button(
          onClick = { viewModel.confirmReset() },
          colors = ButtonDefaults.buttonColors(
            containerColor = TurquoisePrimary,
            contentColor = TurquoiseOnPrimary
          ),
          modifier = Modifier.testTag("confirm_reset_button")
        ) {
          Text("Réinitialiser", fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(
          onClick = { viewModel.dismissResetConfirmation() },
          modifier = Modifier.testTag("cancel_reset_button")
        ) {
          Text("Annuler", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
      },
      containerColor = DarkSurfaceContainerHigh
    )
  }
}

@Composable
fun HydrationProgressCircle(
  currentMl: Int,
  targetMl: Int,
  progress: Float,
  isGoalReached: Boolean,
  modifier: Modifier = Modifier
) {
  val animatedProgress by animateFloatAsState(
    targetValue = progress,
    animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
    label = "progressAnimation"
  )

  val circleBorderGlow by animateColorAsState(
    targetValue = if (isGoalReached) TurquoisePrimary else TurquoiseGlow,
    label = "glowAnimation"
  )

  Box(
    contentAlignment = Alignment.Center,
    modifier = modifier
      .size(270.dp)
      .padding(8.dp)
  ) {
    // Canvas for Arc and Background Track
    Canvas(modifier = Modifier.fillMaxSize()) {
      val strokeWidth = 22.dp.toPx()
      val arcSize = size.minDimension - strokeWidth
      val topLeftOffset = Offset(strokeWidth / 2f, strokeWidth / 2f)

      // Background Full Track Circle
      drawArc(
        color = TurquoiseTrack,
        startAngle = -90f,
        sweepAngle = 360f,
        useCenter = false,
        topLeft = topLeftOffset,
        size = Size(arcSize, arcSize),
        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
      )

      // Gradient Progress Sweep
      val brush = Brush.sweepGradient(
        listOf(
          TurquoiseTertiary,
          TurquoiseSecondary,
          TurquoisePrimary,
          TurquoisePrimary
        )
      )

      val sweepAngle = (animatedProgress * 360f).coerceIn(0f, 360f)
      if (sweepAngle > 0f) {
        drawArc(
          brush = brush,
          startAngle = -90f,
          sweepAngle = sweepAngle,
          useCenter = false,
          topLeft = topLeftOffset,
          size = Size(arcSize, arcSize),
          style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
      }
    }

    // Inner Content Display
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier.padding(24.dp)
    ) {
      // Icon or Status Badge
      if (isGoalReached) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp),
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(TurquoisePrimary.copy(alpha = 0.15f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = TurquoisePrimary,
            modifier = Modifier.size(16.dp)
          )
          Text(
            text = "Objectif atteint !",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = TurquoisePrimary
          )
        }
      } else {
        Icon(
          imageVector = Icons.Filled.WaterDrop,
          contentDescription = null,
          tint = TurquoisePrimary,
          modifier = Modifier.size(28.dp)
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      // Current Volume Big Display
      Text(
        text = if (currentMl >= 1000) {
          String.format(java.util.Locale.US, "%.2f L", currentMl / 1000f)
        } else {
          "$currentMl ml"
        },
        style = MaterialTheme.typography.displayMedium.copy(
          fontWeight = FontWeight.ExtraBold,
          letterSpacing = (-1).sp
        ),
        color = MaterialTheme.colorScheme.onSurface
      )

      // Target Label
      Text(
        text = "sur 2 000 ml",
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(6.dp))

      // Percentage Pill
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = TurquoisePrimary.copy(alpha = 0.12f),
        border = androidx.compose.foundation.BorderStroke(1.dp, TurquoisePrimary.copy(alpha = 0.3f))
      ) {
        val pct = ((currentMl.toFloat() / targetMl.toFloat()) * 100).toInt()
        Text(
          text = "$pct %",
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = TurquoisePrimary
        )
      }
    }
  }
}

@Composable
fun StatsRow(
  currentMl: Int,
  targetMl: Int,
  glassesCount: Int,
  remainingMl: Int,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    // Glasses card
    Card(
      modifier = Modifier.weight(1f),
      colors = CardDefaults.cardColors(containerColor = DarkSurfaceContainer),
      shape = RoundedCornerShape(16.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, DarkOutline.copy(alpha = 0.4f))
    ) {
      Column(
        modifier = Modifier.padding(14.dp),
        horizontalAlignment = Alignment.Start
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(
            imageVector = Icons.Filled.LocalDrink,
            contentDescription = null,
            tint = TurquoiseSecondary,
            modifier = Modifier.size(16.dp)
          )
          Text(
            text = "Verres",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "$glassesCount / 8",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = "250 ml / verre",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
      }
    }

    // Remaining card
    Card(
      modifier = Modifier.weight(1f),
      colors = CardDefaults.cardColors(containerColor = DarkSurfaceContainer),
      shape = RoundedCornerShape(16.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, DarkOutline.copy(alpha = 0.4f))
    ) {
      Column(
        modifier = Modifier.padding(14.dp),
        horizontalAlignment = Alignment.Start
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(
            imageVector = Icons.Filled.WaterDrop,
            contentDescription = null,
            tint = TurquoisePrimary,
            modifier = Modifier.size(16.dp)
          )
          Text(
            text = "Restant",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = if (remainingMl <= 0) "Complet !" else "$remainingMl ml",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = if (remainingMl <= 0) TurquoisePrimary else MaterialTheme.colorScheme.onSurface
          )
        )
        Text(
          text = if (remainingMl <= 0) "Objectif 2L fait" else "pour atteindre 2L",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
      }
    }
  }
}

@Composable
fun ActionButtonsSection(
  onAddWater: () -> Unit,
  onReset: () -> Unit,
  isGoalReached: Boolean,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    // Prominent Primary Button: + 250 ml
    Button(
      onClick = onAddWater,
      modifier = Modifier
        .fillMaxWidth()
        .height(62.dp)
        .shadow(elevation = 8.dp, shape = RoundedCornerShape(20.dp), ambientColor = TurquoisePrimary, spotColor = TurquoisePrimary)
        .testTag("add_water_button"),
      shape = RoundedCornerShape(20.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = TurquoisePrimary,
        contentColor = TurquoiseOnPrimary
      ),
      contentPadding = PaddingValues(horizontal = 24.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
      ) {
        Icon(
          imageVector = Icons.Filled.Add,
          contentDescription = null,
          modifier = Modifier.size(26.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Ajouter 250 ml",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 0.3.sp
          )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
          imageVector = Icons.Filled.LocalDrink,
          contentDescription = null,
          modifier = Modifier.size(20.dp)
        )
      }
    }

    // Reset Button
    OutlinedButton(
      onClick = onReset,
      modifier = Modifier
        .fillMaxWidth()
        .height(52.dp)
        .testTag("reset_button"),
      shape = RoundedCornerShape(16.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, DarkOutline),
      colors = ButtonDefaults.outlinedButtonColors(
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
      )
    ) {
      Icon(
        imageVector = Icons.Filled.Refresh,
        contentDescription = null,
        modifier = Modifier.size(18.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "Réinitialiser la journée",
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
      )
    }
  }
}

@Composable
fun IntakeHistoryCard(
  history: List<HydrationRecord>,
  onUndo: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = DarkSurfaceContainer),
    border = androidx.compose.foundation.BorderStroke(1.dp, DarkOutline.copy(alpha = 0.35f))
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(
            imageVector = Icons.Outlined.History,
            contentDescription = null,
            tint = TurquoiseSecondary,
            modifier = Modifier.size(18.dp)
          )
          Text(
            text = "Prises récentes (${history.size})",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        TextButton(
          onClick = onUndo,
          contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
          modifier = Modifier.testTag("undo_button")
        ) {
          Icon(
            imageVector = Icons.Outlined.Undo,
            contentDescription = "Annuler",
            tint = TurquoiseSecondary,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "Annuler",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = TurquoiseSecondary
          )
        }
      }

      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(history) { entry ->
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = DarkSurfaceContainerHigh,
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkOutline.copy(alpha = 0.3f))
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(
                imageVector = Icons.Filled.WaterDrop,
                contentDescription = null,
                tint = TurquoisePrimary,
                modifier = Modifier.size(14.dp)
              )
              Text(
                text = "+${entry.amountMl} ml",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = entry.timeFormatted,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }
    }
  }
}
