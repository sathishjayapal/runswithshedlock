package me.sathish.runswithshedlock.garmin_run;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import java.time.OffsetDateTime;
import me.sathish.runswithshedlock.runner.Runner;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@EntityListeners(AuditingEntityListener.class)
public class GarminRun {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;

    @Column
    private Integer activityID;

    @Column(columnDefinition = "text")
    private String activityDate;

    @Column(length = 25)
    private String activityType;

    @Column(length = 100)
    private String activityName;

    @Column
    private String activityDescription;

    @Column
    private String elapsedTime;

    @Column
    private String distance;

    @Column
    private String maxHeartRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "runner_id", nullable = false)
    private Runner runner;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Integer getActivityID() {
        return activityID;
    }

    public void setActivityID(final Integer activityID) {
        this.activityID = activityID;
    }

    public String getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(final String activityDate) {
        this.activityDate = activityDate;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(final String activityType) {
        this.activityType = activityType;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(final String activityName) {
        this.activityName = activityName;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(final String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public String getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(final String elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(final String distance) {
        this.distance = distance;
    }

    public String getMaxHeartRate() {
        return maxHeartRate;
    }

    public void setMaxHeartRate(final String maxHeartRate) {
        this.maxHeartRate = maxHeartRate;
    }

    public Runner getRunner() {
        return runner;
    }

    public void setRunner(final Runner runner) {
        this.runner = runner;
    }

    public OffsetDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(final OffsetDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public OffsetDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(final OffsetDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

}
