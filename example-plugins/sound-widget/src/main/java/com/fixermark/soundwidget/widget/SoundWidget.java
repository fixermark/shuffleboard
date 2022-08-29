package com.fixermark.soundwidget.widget;

import java.io.File;

import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

@Description(
    name = "Triggered Sound",
    dataTypes = Boolean.class,
    summary = "Plays a sound when the monitored boolean goes from false to true"
)
@ParametrizedController("SoundWidget.fxml")
public final class SoundWidget extends SimpleAnnotatedWidget<Boolean> {

  @FXML
  private Pane root;

  private MediaPlayer player;

  @FXML
  private void initialize() {
    root.backgroundProperty().bind(
      Bindings.createObjectBinding(
        () -> createSolidColorBackground(getColor()), 
    dataProperty()));
    dataProperty().addListener((newValue) -> checkSoundPlay());
    Media sound = new Media("file:///home/mtomczak/alert.wav");
    player = new MediaPlayer(sound);
  }

  private Background createSolidColorBackground(Color color) {
    return new Background(new BackgroundFill(color, null, null));
  }

  private Color getColor() {
    final Boolean data = getData();
    if (data == null) {
      return Color.BLACK;
    }

    if (data.booleanValue()) {
      return Color.LAWNGREEN;
    } else {
      return Color.DARKRED;
    }
  }

  private void checkSoundPlay() {
    final Boolean data = getData();

    if (data.booleanValue()) {
      player.stop();
      player.play();
    }
  }

  @Override
  public Pane getView() {
    return root;
  }
}
