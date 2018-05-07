package com.example.gcwien.gui;

import com.example.gcwien.command.IssueCmd;
import com.example.gcwien.command.RedeemCmd;
import com.example.gcwien.query.DataQuery;
import com.example.gcwien.query.GiftCardSummary;
import com.example.gcwien.query.SizeQuery;
import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.ErrorEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.responsetypes.ResponseTypes;

import java.util.stream.Stream;

@SpringUI
public class GiftCardGUI extends UI {

    private final CommandGateway commandGateway;
    private final DataProvider<GiftCardSummary, Void> dataProvider;

    public GiftCardGUI(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.dataProvider = dataProvider(queryGateway);
    }

    private DataProvider<GiftCardSummary, Void> dataProvider(QueryGateway queryGateway) {
        return new AbstractBackEndDataProvider<GiftCardSummary, Void>() {
            @Override
            protected Stream<GiftCardSummary> fetchFromBackEnd(Query<GiftCardSummary, Void> query) {
                return queryGateway
                        .query(
                                new DataQuery(query.getOffset(), query.getLimit()),
                                ResponseTypes.multipleInstancesOf(GiftCardSummary.class))
                        .join()
                        .stream();
            }

            @Override
            protected int sizeInBackEnd(Query<GiftCardSummary, Void> query) {
                return queryGateway
                        .query(new SizeQuery(),
                                ResponseTypes.instanceOf(Integer.class))
                        .join();
            }
        };
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        HorizontalLayout commands = new HorizontalLayout();
        commands.setSizeFull();
        commands.addComponents(issuePanel(), redeemPanel());

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();;
        layout.addComponents(commands, summaryGrid());

        setContent(layout);

        setErrorHandler(new DefaultErrorHandler() {
            @Override
            public void error(com.vaadin.server.ErrorEvent event) {
                Throwable cause = event.getThrowable();
                while(cause.getCause() != null) cause = cause.getCause();
                Notification.show("Error", cause.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        });

    }

    private Panel issuePanel() {
        TextField id = new TextField("Id");
        TextField value = new TextField("value");
        Button submit = new Button("submit");

        submit.addClickListener(evt -> {
           String idStr = id.getValue();
           int val = Integer.parseInt(value.getValue());
           commandGateway.sendAndWait(new IssueCmd(idStr, val));
            Notification.show("Success", Notification.Type.HUMANIZED_MESSAGE)
                    .addCloseListener(closeEvt -> dataProvider.refreshAll());
        });

        FormLayout form = new FormLayout();
        form.setMargin(true);
        form.addComponents(id, value, submit);

        Panel panel = new Panel("Issue");
        panel.setContent(form);
        return panel;
    }

    private Panel redeemPanel() {
        TextField id = new TextField("Id");
        TextField value = new TextField("value");
        Button submit = new Button("submit");

        submit.addClickListener(evt -> {
            String idStr = id.getValue();
            int val = Integer.parseInt(value.getValue());
            commandGateway.sendAndWait(new RedeemCmd(idStr, val));
            Notification.show("Success", Notification.Type.HUMANIZED_MESSAGE)
                    .addCloseListener(closeEvt -> dataProvider.refreshAll());

        });

        FormLayout form = new FormLayout();
        form.setMargin(true);
        form.addComponents(id, value, submit);

        Panel panel = new Panel("Redeem");
        panel.setContent(form);
        return panel;
    }

    private Grid<GiftCardSummary> summaryGrid() {
        Grid<GiftCardSummary> grid = new Grid<>();

        grid.addColumn(GiftCardSummary::getId).setCaption("ID");
        grid.addColumn(GiftCardSummary::getInitialBalance).setCaption("Initial balance");
        grid.addColumn(GiftCardSummary::getIssueAt).setCaption("Issued At");
        grid.addColumn(GiftCardSummary::getCurrentBalance).setCaption("Current balance");

        grid.setDataProvider(dataProvider);
        grid.setSizeFull();

        return grid;
    }

}
