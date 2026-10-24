package model;

import java.util.Date;

public class Vizite {
    private int id;
    private int pronaId;
    private int klientId;
    private int agjentId;
    private Date dataOra;
    private String statusi; // "PLANIFIKUAR", "KRYER", "ANULUAR"
    private String shenime;

    public Vizite() {
    }

    public Vizite(int id, int pronaId, int klientId, int agjentId, Date dataOra, String statusi, String shenime) {
        this.id = id;
        this.pronaId = pronaId;
        this.klientId = klientId;
        this.agjentId = agjentId;
        this.dataOra = dataOra;
        this.statusi = statusi;
        this.shenime = shenime;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPronaId() {
        return pronaId;
    }

    public void setPronaId(int pronaId) {
        this.pronaId = pronaId;
    }

    public int getKlientId() {
        return klientId;
    }

    public void setKlientId(int klientId) {
        this.klientId = klientId;
    }

    public int getAgjentId() {
        return agjentId;
    }

    public void setAgjentId(int agjentId) {
        this.agjentId = agjentId;
    }

    public Date getDataOra() {
        return dataOra;
    }

    public void setDataOra(Date dataOra) {
        this.dataOra = dataOra;
    }

    public String getStatusi() {
        return statusi;
    }

    public void setStatusi(String statusi) {
        this.statusi = statusi;
    }

    public String getShenime() {
        return shenime;
    }

    public void setShenime(String shenime) {
        this.shenime = shenime;
    }

    @Override
    public String toString() {
        return "Vizite{" +
                "id=" + id +
                ", pronaId=" + pronaId +
                ", klientId=" + klientId +
                ", agjentId=" + agjentId +
                ", dataOra=" + dataOra +
                ", statusi='" + statusi + '\'' +
                ", shenime='" + shenime + '\'' +
                '}';
    }
}