package controller;

import dao.AgjentDAO;
import model.Agjent;

public class AuthController {
    private AgjentDAO agjentDAO;

    public AuthController() {
        agjentDAO = new AgjentDAO();
    }

    public Agjent login(String username, String password) {
        return agjentDAO.login(username, password);
    }
}