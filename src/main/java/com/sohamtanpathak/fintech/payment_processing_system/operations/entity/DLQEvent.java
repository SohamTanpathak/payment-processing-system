package com.sohamtanpathak.fintech.payment_processing_system.operations.entity;

import com.sohamtanpathak.fintech.payment_processing_system.common.entity.BaseEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * After all the retries, if the event is not processed, the even is sent to DLQ(Dead Lettered Queue)
 *
 */

@Entity
@Table(name = "dlq_event")
public class DLQEvent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID merchantId;

    @OneToOne(fetch = FetchType.LAZY) //if one webhook event is put inside dlq, then that dlq will have only webhook event

    private WebhookEvent webhookEventId;

    @Column(length = 1000)
    private String finalError;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> payload;

    private LocalDateTime movedAt; //when the event was moved to dlq

    private LocalDateTime replayedAt; //when this event was reused/replayed on the request of merchant

}
