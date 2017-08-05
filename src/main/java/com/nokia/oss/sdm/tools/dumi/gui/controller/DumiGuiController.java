package com.nokia.oss.sdm.tools.dumi.gui.controller;

import com.nokia.oss.sdm.tools.dumi.inspector.InspectionProcessor;
import com.nokia.oss.sdm.tools.dumi.context.ApplicationContext;
import com.nokia.oss.sdm.tools.dumi.context.Options;
import com.nokia.oss.sdm.tools.dumi.gui.model.LogEntry;
import com.nokia.oss.sdm.tools.dumi.inspector.rule.FilterRule;
import com.nokia.oss.sdm.tools.dumi.inspector.rule.FilterText;
import com.nokia.oss.sdm.tools.dumi.inspector.rule.PlainTextFilterRule;
import com.nokia.oss.sdm.tools.dumi.inspector.rule.RegexPatternFilterRule;
import com.nokia.oss.sdm.tools.dumi.inspector.splitter.PlainTextSplitter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

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

    private InspectionProcessor inspector;

    @FXML
    private Button stopButton;

    @FXML
    private Button startButton;

    @FXML
    private Button browserButton;

    @FXML
    private TextField threshold;

    @FXML
    private TableView<FilterText> tblIgnoredWords;

    @FXML
    private TableColumn<FilterText, String> wordColumn;

    @FXML
    private TableColumn<FilterText, String> wordRemarkColumn;

    @FXML
    private Button ignoredWordsAdd;

    @FXML
    private Button ignoredWordsDel;

    @FXML
    private TableView<FilterText> tblRegexPatterns;

    @FXML
    private TableColumn<FilterText, String> patternColumn;

    @FXML
    private TableColumn<FilterText, String> descColumn;

    @FXML
    private Button regexPatternAdd;

    @FXML
    private Button regexPatternDel;

    private final TextField textInput = new TextField();
    private final TextField remarkInput = new TextField();

    @FXML
    private void initialize()
    {
        timestampColumn.setCellValueFactory(cellData -> cellData.getValue().getTimestamp());
        levelColumn.setCellValueFactory(cellData -> cellData.getValue().getLevel());
        textColumn.setCellValueFactory(cellData -> cellData.getValue().getText());

        wordColumn.setCellValueFactory(cellData -> cellData.getValue().getTextProperty());
        wordRemarkColumn.setCellValueFactory(cellData -> cellData.getValue().getRemarkProperty());
        patternColumn.setCellValueFactory(cellData -> cellData.getValue().getTextProperty());
        descColumn.setCellValueFactory(cellData -> cellData.getValue().getRemarkProperty());

        List<FilterRule> filterRules = ApplicationContext.getInstance().getFilterRules();
        for (FilterRule filterRule : filterRules)
        {
            TableView<FilterText> tableView = filterRule instanceof PlainTextFilterRule ? tblIgnoredWords : tblRegexPatterns;
            for (FilterText filterText : filterRule.getAcceptedPhrases())
            {
                tableView.getItems().add(filterText);
            }
        }

        /*logViewList.getItems().addListener((ListChangeListener<LogEntry>) (c -> {
            c.next();
            final int size = logViewList.getItems().size();
            if (size > 0)
            {
                logViewList.scrollTo(size - 1);
            }
        }));*/
    }

    private Action showDialog(String title, String masthead, String message, DIALOG_TYPE type)
    {
        return showDialog(title, masthead, message, type, null);
    }

    private Action showDialog(String title, String masthead, String message, DIALOG_TYPE type, Throwable throwable)
    {
        Action response = null;
        Object owner = scanFolder.getScene().getWindow();
        Dialogs dialog = Dialogs.create()
                .owner(owner)
                .title(title)
                .masthead(masthead)
                .message(message);

        switch (type)
        {
            case INFO:
                dialog.showInformation();
                break;
            case ERROR:
                dialog.showError();
                break;
            case WARNING:
                dialog.showWarning();
                break;
            case CONFIRM:
                response = dialog.actions(Dialog.ACTION_YES, Dialog.ACTION_NO).showConfirm();
                break;
            case EXCEPTION:
                dialog.showException(throwable);
                break;
            default:
                dialog.showInformation();
                break;
        }
        return response;
    }

    private boolean validateInput ()
    {
        String folder = scanFolder.getText();
        if (folder == null && "".equals(folder.trim()))
        {
            showDialog("Illegal Argument", null, "Scanning folder must not be empty", DIALOG_TYPE.ERROR);
            return false;
        }

        if (!new File(folder).exists())
        {
            showDialog("Illegal Argument", null, "Scanning folder does not exit", DIALOG_TYPE.ERROR);
            return false;
        }

        return true;
    }

    public void logEvent (LogEntry logEntry)
    {
        if (logEntry != null)
        {
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
        enableFormComponents(true);
    }

    @FXML
    public void onIgnoredWordAdd (ActionEvent event)
    {
        showAddDialog(tblIgnoredWords, PlainTextSplitter.class);
    }

    private void showAddDialog (TableView<FilterText> tableView, Class filterRuleClass)
    {
        createFormDialog("Filter Form Dialog", "Add new filter rule", new Consumer<ActionEvent>()
        {
            @Override
            public void accept (ActionEvent event)
            {
                if (textInput.getText() == null || "".equals(textInput.getText().trim()))
                {
                    showDialog("Illegal Form Input", null, "Text must not be empty", DIALOG_TYPE.ERROR);
                    textInput.requestFocus();
                    return;
                }

                FilterText filterText = new FilterText(textInput.getText(), remarkInput.getText());
                tableView.getItems().add(filterText);
                tableView.scrollTo(tableView.getItems().size());

                List<FilterRule> filterRules = ApplicationContext.getInstance().getFilterRules();
                for (FilterRule filterRule : filterRules)
                {
                    if (filterRule.getClass() == filterRuleClass)
                    {
                        filterRule.addRule(filterText.getText(), filterText.getRemark());
                    }
                }

                Dialog d = (Dialog) event.getSource();
                d.hide();

                textInput.clear();
                remarkInput.clear();
            }
        });
    }

    private void showDeleteDialog (TableView<FilterText> tableView, Class filterRuleClass)
    {
        Action action = showDialog("Delete Dialog", null, "Are you sure to delete the item?", DIALOG_TYPE.CONFIRM);
        if (action == Dialog.ACTION_YES)
        {
            removeItem(tableView, filterRuleClass);
        }
    }

    @FXML
    public void onIgnoredWordDelete (ActionEvent event)
    {
        showDeleteDialog(tblIgnoredWords, PlainTextFilterRule.class);
    }

    @FXML
    public void onPatterAdd (ActionEvent event)
    {
        showAddDialog(tblRegexPatterns, RegexPatternFilterRule.class);
    }

    @FXML
    public void onPatternDelete (ActionEvent event)
    {
        showDeleteDialog(tblRegexPatterns, RegexPatternFilterRule.class);
    }

    private void createFormDialog (String title, String masthead, Consumer<ActionEvent> actionEvent)
    {
        textInput.clear();
        remarkInput.clear();
        textInput.setMinWidth(470);

        Dialog dlg = new Dialog(scanFolder.getScene().getWindow(), title);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));
        dlg.setContent(grid);
        grid.add(new Label("Text:"), 0, 0);
        grid.add(textInput, 1, 0);
        grid.add(new Label("Remark:"), 0, 1);
        grid.add(remarkInput, 1, 1);
        dlg.setMasthead(masthead); //"Create new filter rule");
        dlg.getActions().addAll(new Action("OK", actionEvent), new Action("Cancel", new Consumer<ActionEvent>() {
            @Override
            public void accept (ActionEvent event)
            {
                textInput.clear();
                remarkInput.clear();
                Dialog d = (Dialog) event.getSource();
                d.hide();
            }
        }));

        dlg.setResizable(false);
        dlg.show();
    }

    private void removeItem (TableView<FilterText> tableView, Class filterRuleClass)
    {
        int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex > -1)
        {
            FilterText filterText = tableView.getSelectionModel().getSelectedItem();
            List<FilterRule> filterRules = ApplicationContext.getInstance().getFilterRules();
            for (FilterRule filterRule : filterRules)
            {
                if (filterRule.getClass() == filterRuleClass)
                {
                    filterRule.removeRule(filterText.getText());
                }
            }

            tableView.getItems().remove(selectedIndex);
        }
    }

    @FXML
    public void onButtonStart(ActionEvent event)
    {
        if (validateInput())
        {
            String folder = scanFolder.getText();
            Options options = ApplicationContext.getInstance().getOptions();
            options.setScanFolder(folder);

            if (logViewList.getItems() != null)
            {
                logViewList.getItems().clear();
            }

            inspector = new InspectionProcessor();
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
            enableFormComponents(false);
        }
    }

    private void enableFormComponents(boolean disable)
    {
        scanFolder.setDisable(disable);
        browserButton.setDisable(disable);
        threshold.setDisable(disable);
    }

    private enum DIALOG_TYPE
    {
        INFO,
        WARNING,
        ERROR,
        CONFIRM,
        EXCEPTION
    }
}
