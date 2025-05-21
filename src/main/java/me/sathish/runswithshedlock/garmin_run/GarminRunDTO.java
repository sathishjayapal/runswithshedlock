package me.sathish.runswithshedlock.garmin_run;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class GarminRunDTO {

    private Long id;

    private Integer activityID;

    private String activityDate;

    @Size(max = 25)
    private String activityType;

    @Size(max = 100)
    private String activityName;

    @Size(max = 255)
    private String activityDescription;

    @Size(max = 255)
    private String elapsedTime;

    @Size(max = 255)
    private String distance;

    @Size(max = 255)
    private String maxHeartRate;

    @NotNull
    private Long runner;

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

    public Long getRunner() {
        return runner;
    }

    public void setRunner(final Long runner) {
        this.runner = runner;
    }

}
