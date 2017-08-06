package com.nokia.oss.sdm.tools.dumi.gui;

import com.nokia.oss.sdm.tools.dumi.inspector.InspectionProcessor;
import com.nokia.oss.sdm.tools.dumi.context.ApplicationContext;
import com.nokia.oss.sdm.tools.dumi.context.Constants;
import com.nokia.oss.sdm.tools.dumi.context.Options;
import com.nokia.oss.sdm.tools.dumi.gui.controller.DumiGuiController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.awt.*;

public class DumiGui extends Application
{
    private Logger LOGGER = Logger.getLogger(DumiGui.class);
    private static final ApplicationContext context = ApplicationContext.getInstance();

    private static int WIN_WIDTH = 800;
    private static int WIN_HEIGHT = 600;

    private static DumiGui instance;
    private static DumiGuiController controller;

    private static String LAYOUT_FILE = "/res/fx/dumi_ui.fxml";

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(LAYOUT_FILE));
        initialize(primaryStage, loader.load());

        instance = this;
        controller = loader.getController();
    }

    private void centerOnScreen(Stage stage)
    {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        stage.setX(d.width/2 - (stage.getWidth()/2));
        stage.setY(d.height/2 - (stage.getHeight()/2));
    }

    private void initialize(Stage stage, Parent root)
    {
        stage.setTitle(getWinTitle());
        stage.setScene(new Scene(root, WIN_WIDTH, WIN_HEIGHT));
        stage.setResizable(false);

        try
        {
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/res/icon.png")));
        }
        catch (Exception e)
        {
            LOGGER.warn("Failed to load icon image", e);
        }

        stage.show();
        centerOnScreen(stage);
    }

    public static DumiGui getApplication()
    {
        return instance;
    }

    public static DumiGuiController getController()
    {
        return controller;
    }

    private String getWinTitle ()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(context.getProperty(Constants.propToolTitle, Constants.DEFAULT_TOOL_TITLE));
        builder.append(" (v");
        builder.append(context.getProperty(Constants.propToolVer, Constants.DEFAULT_TOOL_VERSION));
        builder.append(")");
        return builder.toString();
    }

    public static void main(String[] args)
    {
        System.out.println(args);
        launch(args);

        Options options = Options.from(args);
        if (options.isValid())
        {
            ApplicationContext.getInstance().setOptions(options);
            new InspectionProcessor().inspect(options.getScanFolder(), options.getUserDictionary());
        }
        else
        {
            System.out.println("Invalid options");
            System.exit(1);
        }
    }
}
