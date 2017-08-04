package com.nokia.oss.sdm.tools.dumi.gui;

import com.nokia.oss.sdm.tools.dumi.DumiInspector;
import com.nokia.oss.sdm.tools.dumi.context.ApplicationContext;
import com.nokia.oss.sdm.tools.dumi.context.Constants;
import com.nokia.oss.sdm.tools.dumi.context.Options;
import com.nokia.oss.sdm.tools.dumi.gui.controller.DumiGuiController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.awt.*;

public class DumiGui extends Application
{
    private static final String WIN_TITLE = "Do You Mean It";
    private static final String WIN_VER = "1.0";
    private static final ApplicationContext context = ApplicationContext.getInstance();
    private static DumiGui instance;
    private static DumiGuiController controller;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx/dumi_ui.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle(getWinTitle());
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setResizable(false);
        primaryStage.show();

        Dimension d= Toolkit.getDefaultToolkit().getScreenSize();
        primaryStage.setX(d.width/2-(primaryStage.getWidth()/2));
        primaryStage.setY(d.height/2-(primaryStage.getHeight()/2));

       /*primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle (WindowEvent we) {
                getController().stop();
                System.out.println("Stage is closing");
            }
        });
        */

        instance = this;
        controller = loader.getController();
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
        builder.append(context.getProperty(Constants.propToolTitle, WIN_TITLE));
        builder.append(" (v");
        builder.append(context.getProperty(Constants.propToolVer, WIN_VER));
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
            new DumiInspector().inspect(options.getScanFolder(), options.getUserDictionary());
        }
        else
        {
            System.out.println("Invalid options");
            System.exit(1);
        }

        //launch(args);
    }
}
