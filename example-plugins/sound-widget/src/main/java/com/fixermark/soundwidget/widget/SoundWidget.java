package com.fixermark.soundwidget.widget;

import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
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

  @FXML
  private void initialize() {
    root.backgroundProperty().bind(
      Bindings.createObjectBinding(
        () -> createSolidColorBackground(getColor()), 
    dataProperty()));
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

  @Override
  public Pane getView() {
    return root;
  }
}
