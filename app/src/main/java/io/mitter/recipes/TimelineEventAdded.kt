package io.mitter.recipes

import io.mitter.models.mardle.messaging.TimelineEvent

data class TimelineEventAdded(
    val messageId: String,
    val timelineEvent: TimelineEvent
)
