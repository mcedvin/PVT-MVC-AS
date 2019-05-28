package com.example.edvin.app.models;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

public class Report implements Serializable {

    private Long id;

    private Station station;
    private Date finalEndDate;

    private UserAccount userAccount;

    private Collection<MaterialSchedule> materialSchedules;

    private CleaningSchedule cleaningSchedule;

    public Report() {

    }

    public Report(Station station, UserAccount userAccount, Date finalEndDate, Collection<MaterialSchedule> materialSchedules, CleaningSchedule cleaningSchedule) {
        this.station = station;
        this.userAccount = userAccount;
        this.finalEndDate = finalEndDate;
        this.materialSchedules = materialSchedules;
        this.cleaningSchedule = cleaningSchedule;
    }


    public Report(Station station, UserAccount userAccount, Collection<MaterialSchedule> materialSchedules, CleaningSchedule cleaningSchedule) {
        this.station = station;
        this.userAccount = userAccount;
        this.finalEndDate = finalEndDate;
        this.materialSchedules = materialSchedules;
        this.cleaningSchedule = cleaningSchedule;

        finalEndDate = calculateEndDate(materialSchedules, cleaningSchedule);

    }


    private Date calculateEndDate(Collection<MaterialSchedule> materialSchedules, CleaningSchedule cleaningSchedule) {

        Date latestDate = cleaningSchedule.getDate();

        for (MaterialSchedule ms : materialSchedules) {
            Date materialScheduleDate = ms.getDate();
            if (materialScheduleDate.after(latestDate)) {
                latestDate = materialScheduleDate;
            }
        }

        return latestDate;

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

    public Collection<MaterialSchedule> getMaterialSchedules() {
        return materialSchedules;
    }

    public void setMaterialSchedules(Collection<MaterialSchedule> materialSchedules) {
        this.materialSchedules = materialSchedules;
    }

    public CleaningSchedule getCleaningSchedule() {
        return cleaningSchedule;
    }

    public void setCleaningSchedule(CleaningSchedule cleaningSchedule) {
        this.cleaningSchedule = cleaningSchedule;
    }


}