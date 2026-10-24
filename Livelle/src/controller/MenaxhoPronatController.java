package controller;

import dao.PronaDAO;
import model.Prona;
import java.util.List;

public class MenaxhoPronatController {
    private PronaDAO pronaDAO;

    public MenaxhoPronatController() {
        pronaDAO = new PronaDAO();
    }

    public void addProna(Prona prona) {
        pronaDAO.addProna(prona);
    }

    public void updateProna(Prona prona) {
        pronaDAO.updateProna(prona);
    }

    public void deleteProna(int id) {
        pronaDAO.deleteProna(id);
    }

    public List<Prona> getAllPronat() {
        return pronaDAO.getAllPronat();
    }

    public Prona getPronaById(int id) {
        return pronaDAO.getPronaById(id);
    }

    public List<Prona> searchProna(String keyword) {
        return pronaDAO.searchProna(keyword);
    }
}