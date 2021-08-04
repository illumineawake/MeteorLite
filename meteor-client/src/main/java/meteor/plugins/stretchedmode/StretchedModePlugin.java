package meteor.plugins.stretchedmode;

import static org.sponge.util.Logger.ANSI_CYAN;
import static org.sponge.util.Logger.ANSI_YELLOW;

import java.io.IOException;
import java.util.Objects;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javax.inject.Inject;
import meteor.MeteorLite;
import meteor.input.MouseManager;
import meteor.plugins.Plugin;
import meteor.plugins.PluginDescriptor;
import meteor.ui.TranslateMouseListener;
import meteor.ui.TranslateMouseWheelListener;

@PluginDescriptor(
    name = "Stretched Mode",
    description = "Stretches the game in fixed and resizable modes.",
    tags = {"resize", "ui", "interface", "stretch", "scaling", "fixed"}
)
public class StretchedModePlugin extends Plugin {

  public static boolean integerScalingEnabled = false;
  public static boolean keepAspectRatio = false;
  public static boolean isStretchedFast = false;
  public static int scalingFactor = 100;
  public String name = ANSI_CYAN + "StretchedModePlugin" + ANSI_YELLOW;

  @Inject
  private MouseManager mouseManager;

  @Inject
  private TranslateMouseListener mouseListener;

  @Inject
  private TranslateMouseWheelListener mouseWheelListener;

  {
    logger.name = name;
  }

  @Override
  public void showConfig() {
    Parent configRoot = null;
    try {
      configRoot = FXMLLoader.load(Objects.requireNonNull(ClassLoader.getSystemClassLoader()
          .getResource("meteor/plugins/stretchedmode/config.fxml")));
    } catch (IOException e) {
      e.printStackTrace();
    }
    MeteorLite.updateRightPanel(configRoot);
  }

  @Override
  public void startup() {
    mouseManager.registerMouseListener(0, mouseListener);
    mouseManager.registerMouseWheelListener(0, mouseWheelListener);
    client.setGameDrawingMode(2);
    client.setStretchedEnabled(true);
    updateConfig();
  }

  @Override
  public void shutdown() {
    mouseManager.unregisterMouseListener(mouseListener);
    mouseManager.unregisterMouseWheelListener(mouseWheelListener);
    client.setStretchedEnabled(false);
    updateConfig();
  }

  public void updateConfig() {
    client.setStretchedIntegerScaling(integerScalingEnabled);
    client.setStretchedKeepAspectRatio(keepAspectRatio);
    client.setStretchedFast(isStretchedFast);
    client.setScalingFactor(scalingFactor);
    client.invalidateStretching(true);
  }
}
