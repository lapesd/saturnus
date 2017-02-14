package lapesd.saturnus.webInterface.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

public class Init implements EntryPoint {
    private TextBox numberTasks = new TextBox();
    private TextBox numberSegments = new TextBox();
    private TextBox numberDataNodes = new TextBox();
    private TextBox blockSize = new TextBox();
    private TextBox requestSize = new TextBox();
    private TextBox stripeSize = new TextBox();
    private TextBox stripeCount = new TextBox();
    private ListBox fileType = new ListBox();
    private ListBox accessPattern = new ListBox();
    private Button startButton = new Button("Start");

    public void onModuleLoad() {

        startButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {

            }
        });

        numberTasks.setSize("70", "25");
        numberSegments.setSize("70", "25");
        numberDataNodes.setSize("70", "25");
        blockSize.setSize("70", "25");
        requestSize.setSize("70", "25");
        stripeSize.setSize("70", "25");
        stripeCount.setSize("70", "25");
        addItems(fileType, new String[]{"File per Process", "Shared"});
        addItems(accessPattern, new String[]{"Sequential", "Random"});
        RootPanel.get("tasksLabel").add(numberTasks);
        RootPanel.get("segmentsLabel").add(numberSegments);
        RootPanel.get("dataNodesLabel").add(numberDataNodes);
        RootPanel.get("blockLabel").add(blockSize);
        RootPanel.get("requestLabel").add(requestSize);
        RootPanel.get("stripeSizeLabel").add(stripeSize);
        RootPanel.get("stripeCountLabel").add(stripeCount);
        RootPanel.get("fileTypeLabel").add(fileType);
        RootPanel.get("accessPtrnLabel").add(accessPattern);
        RootPanel.get().add(startButton);
    }

    private void addItems(ListBox list, String[] items) {
        for (String item : items) {
            list.addItem(item);
        }
    }
    
}
