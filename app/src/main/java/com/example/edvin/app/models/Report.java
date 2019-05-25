package com.example.edvin.app.models;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;


public class Report implements Serializable {

    private Long id;

    private Station station;

    private UserAccount userAccount;
    private Date finalEndDate;

    private Collection<MaterialSchedule> materialSchedules;
    private CleaningSchedule cleaningSchedule;


    public Report() {

    }

    public Report(Station station, UserAccount userAccount, Collection<MaterialSchedule> materialSchedules,
                  CleaningSchedule cleaningSchedule) {

        this.station = station;
        this.userAccount = userAccount;
        this.materialSchedules = materialSchedules;
        this.cleaningSchedule = cleaningSchedule;

        calculateEndDate(materialSchedules, cleaningSchedule);

    }


    private void calculateEndDate(Collection<MaterialSchedule> materialSchedules,
                                  CleaningSchedule cleaningSchedule) {

        Date latestDate = cleaningSchedule.getDate();

        for (MaterialSchedule ms : materialSchedules) {
            Date materialScheduleDate = ms.getDate();
            if (materialScheduleDate.after(latestDate)) {
                latestDate = materialScheduleDate;
            }
        }

        finalEndDate = latestDate;

    }


    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public Date getFinalEndDate() {
        return finalEndDate;
    }

    public void setFinalEndDate(Date finalEndDate) {
        this.finalEndDate = finalEndDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private boolean isActive() {
        return finalEndDate.after(new Date());
    }

}