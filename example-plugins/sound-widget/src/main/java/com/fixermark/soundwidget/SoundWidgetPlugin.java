package com.fixermark.soundwidget;

import edu.wpi.first.shuffleboard.api.plugin.Description;
import edu.wpi.first.shuffleboard.api.plugin.Plugin;
import edu.wpi.first.shuffleboard.api.widget.ComponentType;
import edu.wpi.first.shuffleboard.api.widget.WidgetType;

import java.util.List;

import com.fixermark.soundwidget.widget.SoundWidget;

/**
 * An example plugin that provides a custom data type (a 2D point) and a simple widget for viewing such data.
 */
@Description(
    group = "com.fixermark",
    name = "SoundWidget",
    version = "2019.1.1",
    summary = "A boolean widget that plays a sound when the state transitions from false to true"
)
public final class SoundWidgetPlugin extends Plugin {

  @Override
  public List<ComponentType> getComponents() {
    return List.of(
        WidgetType.forAnnotatedWidget(SoundWidget.class)
    );
  }
}
