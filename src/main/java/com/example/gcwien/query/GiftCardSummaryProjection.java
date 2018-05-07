package com.example.gcwien.query;

import com.example.gcwien.command.IssuedEvt;
import com.example.gcwien.command.RedeemedEvt;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.EventHandlingConfiguration;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.Timestamp;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.List;

@Component
@Profile("query")
@RequiredArgsConstructor
public class GiftCardSummaryProjection {

    private final EntityManager entityManager;

    @EventHandler
    public void on(IssuedEvt evt, @Timestamp Instant timestamp) {
        entityManager.persist(new GiftCardSummary(
                evt.getId(),
                evt.getValue(),
                timestamp,
                evt.getValue()
        ));
    }

    @EventHandler
    public void on(RedeemedEvt evt) {
        entityManager.find(GiftCardSummary.class, evt.getId())
                .redeem(evt.getValue());
    }

    @QueryHandler
    public Integer handle(SizeQuery query) {
        return entityManager.createQuery(
                "SELECT COUNT (c) FROM GiftCardSummary c", Long.class
        ).getSingleResult().intValue();
    }

    @QueryHandler
    public List<GiftCardSummary> handle(DataQuery query) {
        return entityManager.createQuery(
                "SELECT c FROM GiftCardSummary c ORDER BY c.id", GiftCardSummary.class
        ).getResultList();
    }

    @Autowired
    public void configure(EventHandlingConfiguration config) {
        config.registerTrackingProcessor(getClass().getPackage().getName());
    }

}
