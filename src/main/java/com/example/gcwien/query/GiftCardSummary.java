package com.example.gcwien.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiftCardSummary {

    @Id
    String id;
    int initialBalance;
    Instant issueAt;
    int currentBalance;


    void redeem(int value) {
        currentBalance -= value;
    }

}
