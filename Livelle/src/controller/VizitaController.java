package controller;

import dao.ViziteDAO;
import model.Vizite;
import java.util.List;

public class VizitaController {
    private ViziteDAO viziteDAO;

    public VizitaController() {
        viziteDAO = new ViziteDAO();
    }

    public void addVizite(Vizite vizite) {
        viziteDAO.addVizite(vizite);
    }

    public void updateVizite(Vizite vizite) {
        viziteDAO.updateVizite(vizite);
    }

    public void deleteVizite(int id) {
        viziteDAO.deleteVizite(id);
    }

    public List<Vizite> getAllVizite() {
        return viziteDAO.getAllVizite();
    }

    public Vizite getViziteById(int id) {
        return viziteDAO.getViziteById(id);
    }

    public List<Vizite> getViziteByAgjentId(int agjentId) {
        return viziteDAO.getViziteByAgjentId(agjentId);
    }
}