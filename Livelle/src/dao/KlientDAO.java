package dao;

import model.Klient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KlientDAO {
    private Connection connection;

    public KlientDAO() {
        connection = DBConnection.getConnection();
    }

    public void addKlient(Klient klient) {
        try {
            String query = "INSERT INTO klient (emer, email, telefon, tipi, interesime) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, klient.getEmer());
            ps.setString(2, klient.getEmail());
            ps.setString(3, klient.getTelefon());
            ps.setString(4, klient.getTipi());
            ps.setString(5, klient.getInteresime());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateKlient(Klient klient) {
        try {
            String query = "UPDATE klient SET emer=?, email=?, telefon=?, tipi=?, interesime=? WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, klient.getEmer());
            ps.setString(2, klient.getEmail());
            ps.setString(3, klient.getTelefon());
            ps.setString(4, klient.getTipi());
            ps.setString(5, klient.getInteresime());
            ps.setInt(6, klient.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteKlient(int id) {
        try {
            String query = "DELETE FROM klient WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Klient> getAllKlientet() {
        List<Klient> klientet = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM klient");
            while (rs.next()) {
                Klient klient = new Klient();
                klient.setId(rs.getInt("id"));
                klient.setEmer(rs.getString("emer"));
                klient.setEmail(rs.getString("email"));
                klient.setTelefon(rs.getString("telefon"));
                klient.setTipi(rs.getString("tipi"));
                klient.setInteresime(rs.getString("interesime"));
                klientet.add(klient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return klientet;
    }

    public Klient getKlientById(int id) {
        Klient klient = new Klient();
        try {
            String query = "SELECT * FROM klient WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                klient.setId(rs.getInt("id"));
                klient.setEmer(rs.getString("emer"));
                klient.setEmail(rs.getString("email"));
                klient.setTelefon(rs.getString("telefon"));
                klient.setTipi(rs.getString("tipi"));
                klient.setInteresime(rs.getString("interesime"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return klient;
    }

    public List<Klient> searchKlient(String keyword) {
        List<Klient> klientet = new ArrayList<>();
        try {
            String query = "SELECT * FROM klient WHERE emer LIKE ? OR email LIKE ? OR telefon LIKE ? OR tipi LIKE ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");
            ps.setString(4, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Klient klient = new Klient();
                klient.setId(rs.getInt("id"));
                klient.setEmer(rs.getString("emer"));
                klient.setEmail(rs.getString("email"));
                klient.setTelefon(rs.getString("telefon"));
                klient.setTipi(rs.getString("tipi"));
                klient.setInteresime(rs.getString("interesime"));
                klientet.add(klient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return klientet;
    }
}