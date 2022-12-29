package com.example.infits

import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import java.time.LocalDateTime
import java.time.OffsetDateTime

class HealthConnectKt {
    suspend fun readStepsByTimeRange(healthConnectClient: HealthConnectClient) {
        val end: LocalDateTime = LocalDateTime.now()
        val start: LocalDateTime = end.minusYears(1)
        val response =
            healthConnectClient.readRecords(
                ReadRecordsRequest(
                    HeartRateRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(start.toInstant(OffsetDateTime.now().offset), end.toInstant(
                        OffsetDateTime.now().offset))
                )
            )

        for (stepRecord in response.records) {
            println(stepRecord.toString())
        }
    }
}