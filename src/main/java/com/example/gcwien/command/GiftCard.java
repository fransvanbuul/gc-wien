package com.example.gcwien.command;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.context.annotation.Profile;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
@Profile("command")
public class GiftCard {

    @AggregateIdentifier
    private String id;
    private int balance;

    public GiftCard() {
    }

    @CommandHandler
    public GiftCard(IssueCmd cmd) {
        if(cmd.getValue() <= 0)
            throw new IllegalArgumentException("value <= 0");
        apply(new IssuedEvt(cmd.getId(), cmd.getValue()));
    }

    @CommandHandler
    public void handle(RedeemCmd cmd) {
        if(cmd.getValue() <= 0)
            throw new IllegalArgumentException("value <= 0");
        if(cmd.getValue() > balance)
            throw new IllegalArgumentException("value > balance");
        apply(new RedeemedEvt(cmd.getId(), cmd.getValue()));
    }

    @EventSourcingHandler
    public void on(IssuedEvt evt) {
        this.id = evt.getId();
        this.balance = evt.getValue();
    }

    @EventSourcingHandler
    public void on(RedeemedEvt evt) {
        this.balance -= evt.getValue();
    }

}
