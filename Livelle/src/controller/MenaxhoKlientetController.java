package controller;

import dao.KlientDAO;
import model.Klient;
import java.util.List;

public class MenaxhoKlientetController {
    private KlientDAO klientDAO;

    public MenaxhoKlientetController() {
        klientDAO = new KlientDAO();
    }

    public void addKlient(Klient klient) {
        klientDAO.addKlient(klient);
    }

    public void updateKlient(Klient klient) {
        klientDAO.updateKlient(klient);
    }

    public void deleteKlient(int id) {
        klientDAO.deleteKlient(id);
    }

    public List<Klient> getAllKlientet() {
        return klientDAO.getAllKlientet();
    }

    public Klient getKlientById(int id) {
        return klientDAO.getKlientById(id);
    }

    public List<Klient> searchKlient(String keyword) {
        return klientDAO.searchKlient(keyword);
    }
}