package com.example.gcwien.command;

import lombok.Value;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

@Value
public class RedeemCmd {

    @TargetAggregateIdentifier String id;
    int value;

}
