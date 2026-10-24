package model;

import java.util.Arrays;

public class Prona {
    private int id;
    private String emri;
    private String adresa;
    private double cmimi;
    private String statusi; // "NË SHITJE", "ME QIRA", "E ZËNË"
    private byte[] foto;
    private String pershkrimi;
    private int nrDhoma;
    private double siperfaqe;

    public Prona() {
    }

    public Prona(int id, String emri, String adresa, double cmimi, String statusi, byte[] foto, String pershkrimi, int nrDhoma, double siperfaqe) {
        this.id = id;
        this.emri = emri;
        this.adresa = adresa;
        this.cmimi = cmimi;
        this.statusi = statusi;
        this.foto = foto;
        this.pershkrimi = pershkrimi;
        this.nrDhoma = nrDhoma;
        this.siperfaqe = siperfaqe;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmri() {
        return emri;
    }

    public void setEmri(String emri) {
        this.emri = emri;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public double getCmimi() {
        return cmimi;
    }

    public void setCmimi(double cmimi) {
        this.cmimi = cmimi;
    }

    public String getStatusi() {
        return statusi;
    }

    public void setStatusi(String statusi) {
        this.statusi = statusi;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getPershkrimi() {
        return pershkrimi;
    }

    public void setPershkrimi(String pershkrimi) {
        this.pershkrimi = pershkrimi;
    }

    public int getNrDhoma() {
        return nrDhoma;
    }

    public void setNrDhoma(int nrDhoma) {
        this.nrDhoma = nrDhoma;
    }

    public double getSiperfaqe() {
        return siperfaqe;
    }

    public void setSiperfaqe(double siperfaqe) {
        this.siperfaqe = siperfaqe;
    }

    @Override
    public String toString() {
        return "Prona{" +
                "id=" + id +
                ", emri='" + emri + '\'' +
                ", adresa='" + adresa + '\'' +
                ", cmimi=" + cmimi +
                ", statusi='" + statusi + '\'' +
                ", foto=" + Arrays.toString(foto) +
                ", pershkrimi='" + pershkrimi + '\'' +
                ", nrDhoma=" + nrDhoma +
                ", siperfaqe=" + siperfaqe +
                '}';
    }
}