package model;

public class Klient {
    private int id;
    private String emer;
    private String email;
    private String telefon;
    private String tipi; // "Bleres" or "Qiramarres"
    private String interesime;

    public Klient() {
    }

    public Klient(int id, String emer, String email, String telefon, String tipi, 
    		      String interesime) {
        this.id = id;
        this.emer = emer;
        this.email = email;
        this.telefon = telefon;
        this.tipi = tipi;
        this.interesime = interesime;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmer() {
        return emer;
    }

    public void setEmer(String emer) {
        this.emer = emer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getTipi() {
        return tipi;
    }

    public void setTipi(String tipi) {
        this.tipi = tipi;
    }

    public String getInteresime() {
        return interesime;
    }

    public void setInteresime(String interesime) {
        this.interesime = interesime;
    }

    @Override
    public String toString() {
        return "Klient{" +
                "id=" + id +
                ", emer='" + emer + '\'' +
                ", email='" + email + '\'' +
                ", telefon='" + telefon + '\'' +
                ", tipi='" + tipi + '\'' +
                ", interesime='" + interesime + '\'' +
                '}';
    }
}
