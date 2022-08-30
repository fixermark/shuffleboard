package com.fixermark.soundwidget.widget;

import java.io.File;
import java.util.List;

import com.google.common.collect.ImmutableList;

import edu.wpi.first.shuffleboard.api.prefs.Group;
import edu.wpi.first.shuffleboard.api.prefs.Setting;
import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

@Description(name = "Triggered Sound", dataTypes = Boolean.class, summary = "Plays a sound when the monitored boolean goes from false to true")
@ParametrizedController("SoundWidget.fxml")
public final class SoundWidget extends SimpleAnnotatedWidget<Boolean> {

  /**
   * The root content to display. We do a simple light grey / dark grey box to
   * indicate true / false status and white if there's no data.
   */
  @FXML
  private Pane root;

  /** Holder for an error panel; this gets attached to root if we need to display error text */
  private Text errorMessage;

  /** Path to the sound file to play */
  private final StringProperty soundPath = new SimpleStringProperty(this, "soundPath", "");

  /**
   * Media player to play the loaded sound, or null if the sound was not
   * successfully loaded
   */
  private MediaPlayer player;

  /**
   * Called by the JavaFX framework to initialize the widget
   */
  @FXML
  private void initialize() {
    root.backgroundProperty().bind(
        Bindings.createObjectBinding(
            () -> createSolidColorBackground(getColor()),
            dataProperty()));
    dataProperty().addListener((newValue) -> checkSoundPlay());
    soundPath.addListener((newValue) -> loadSoundFile());
    loadSoundFile();
  }

  /**
   * Report the settings for this widget
   * @return List of the widget's setting groups
   */
  @Override
  public List<Group> getSettings() {
    return ImmutableList.of(
        Group.of("Sound",
            Setting.of("Source file", "Path to audio file to use", soundPath, String.class)));
  }

  /**
   * Attempt to load the sound file and build a MediaPlayer, or set error message if we cannot load
   */
  private void loadSoundFile() {
    player = null;
    try {
      File f = new File(soundPath.getValue());
      if (!f.exists()) {
        setErrorMessage("File " + soundPath.getValue() + " does not exist");
        return;
      }
      if (f.isDirectory()) {
        setErrorMessage(soundPath.getValue() + " is a directory");
        return;
      }
      Media sound = new Media("file://" + soundPath.getValue());
      player = new MediaPlayer(sound);
      setErrorMessage(null);
    } catch (MediaException e) {
      System.err.print(e);
      setErrorMessage(e.getMessage());
    }
  }

  /**
   * Set error message, or clear error message by passing a null string
   * @param msg Message to set, or null to clear currently-set message
   */
  private void setErrorMessage(String msg) {
    if (msg == null) {
      if (errorMessage != null) {
        root.getChildren().remove(errorMessage);
        errorMessage = null;
        return;
      }
    } else {
      if (errorMessage == null) {
        errorMessage = new Text();
        errorMessage.setStyle("color: red");
        root.getChildren().add(errorMessage);
      }
      errorMessage.setText(msg);
    }
  }

  /**
   * Build a sold-color background to attach as a property
   * @param color Color to use
   * @return The built background
   */
  private Background createSolidColorBackground(Color color) {
    return new Background(new BackgroundFill(color, null, null));
  }

  /**
   * Choose the color to use based on current widget state
   * 
   * @return Color to use
   */
  private Color getColor() {
    final Boolean data = getData();
    if (data == null) {
      return Color.WHITE;
    }

    if (data.booleanValue()) {
      return Color.LIGHTGRAY;
    } else {
      return Color.DARKGREY;
    }
  }

  /**
   * Check to see if we should play the sound and recycle the sound if we should.
   * 
   * We play if the boolean value is true and the player is available.
   */
  private void checkSoundPlay() {
    final Boolean data = getData();

    if (data.booleanValue() && player != null) {
      player.stop();
      player.play();
    }
  }

  /**
   * Accessor for sound path
   * 
   * @return Sound path
   */
  public String getSoundPath() {
    return soundPath.getValue();
  }

  /**
   * Accessor to set the sound path
   * @param path New value for path
   */
  public void setSoundPath(String path) {
    soundPath.setValue(path);
  }

  /**
   * Accessor to the entire sound path property
   * 
   * @return The sound path property
   */
  public StringProperty soundPathProperty() {
    return this.soundPath;
  }

  @Override
  public Pane getView() {
    return root;
  }
}
