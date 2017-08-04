package com.nokia.oss.sdm.tools.dumi.gui.controller;

import com.nokia.oss.sdm.tools.dumi.DumiInspector;
import com.nokia.oss.sdm.tools.dumi.context.ApplicationContext;
import com.nokia.oss.sdm.tools.dumi.context.Options;
import com.nokia.oss.sdm.tools.dumi.gui.model.LogEntry;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;

/**
 * Created by x36zhao on 2017/8/3.
 */
public class DumiGuiController
{
    private DirectoryChooser directoryChooser = new DirectoryChooser();

    @FXML
    private TextField scanFolder;

    @FXML
    private TableView<LogEntry> logViewList;

    @FXML
    private TableColumn<LogEntry, String> timestampColumn;

    @FXML
    private TableColumn<LogEntry, String> levelColumn;

    @FXML
    private TableColumn<LogEntry, String> textColumn;

    private DumiInspector inspector;

    @FXML
    private Button stopButton;

    @FXML
    private Button startButton;

    @FXML
    private void initialize()
    {
        System.out.print("initialized");
        timestampColumn.setCellValueFactory(cellData -> cellData.getValue().getTimestamp());
        levelColumn.setCellValueFactory(cellData -> cellData.getValue().getLevel());
        textColumn.setCellValueFactory(cellData -> cellData.getValue().getText());

        /*logViewList.getItems().addListener((ListChangeListener<LogEntry>) (c -> {
            c.next();
            final int size = logViewList.getItems().size();
            if (size > 0)
            {
                logViewList.scrollTo(size - 1);
            }
        }));*/
    }

    public void logEvent (LogEntry logEntry)
    {
        if (logEntry != null)
        {
//            if (logViewList.getItems().size() > 200)
//            {
//                logViewList.getItems().clear();
//            }
            logViewList.getItems().add(logEntry);
        }
    }

    public void stop ()
    {
        inspector.stop();
    }

    @FXML
    public void onBrowserFolderButtonClick(ActionEvent event)
    {
        Node node = (Node) event.getSource();
        File selctedFolder = directoryChooser.showDialog(node.getScene().getWindow());
        if (selctedFolder != null)
        {
            scanFolder.setText(selctedFolder.getPath());
        }
    }

    @FXML
    public void onButtonStop(ActionEvent event)
    {
        stop();

        Node button = (Node) event.getSource();
        button.setDisable(true);

        startButton.setDisable(false);
    }

    @FXML
    public void onButtonStart(ActionEvent event)
    {
        String folder = scanFolder.getText();
        Options options = ApplicationContext.getInstance().getOptions();
        options.setScanFolder(folder);

        if (logViewList.getItems() != null)
        {
            logViewList.getItems().clear();
        }

        inspector = new DumiInspector();
        new Thread(new Runnable()
        {
            @Override
            public void run ()
            {
                inspector.inspect(options.getScanFolder(), options.getUserDictionary());
            }
        }).start();

        Node button = (Node) event.getSource();
        button.setDisable(true);

        stopButton.setDisable(false);
    }
}
