package dao;

import model.Vizite;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViziteDAO {
    private Connection connection;

    public ViziteDAO() {
        connection = DBConnection.getConnection();
    }

    public void addVizite(Vizite vizite) {
        try {
            String query = "INSERT INTO vizite (prona_id, klient_id, agjent_id, data_ora, statusi, shenime) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, vizite.getPronaId());
            ps.setInt(2, vizite.getKlientId());
            ps.setInt(3, vizite.getAgjentId());
            ps.setTimestamp(4, new Timestamp(vizite.getDataOra().getTime()));
            ps.setString(5, vizite.getStatusi());
            ps.setString(6, vizite.getShenime());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateVizite(Vizite vizite) {
        try {
            String query = "UPDATE vizite SET prona_id=?, klient_id=?, agjent_id=?, data_ora=?, statusi=?, shenime=? WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, vizite.getPronaId());
            ps.setInt(2, vizite.getKlientId());
            ps.setInt(3, vizite.getAgjentId());
            ps.setTimestamp(4, new Timestamp(vizite.getDataOra().getTime()));
            ps.setString(5, vizite.getStatusi());
            ps.setString(6, vizite.getShenime());
            ps.setInt(7, vizite.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteVizite(int id) {
        try {
            String query = "DELETE FROM vizite WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Vizite> getAllVizite() {
        List<Vizite> vizite = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM vizite");
            while (rs.next()) {
                Vizite vizita = new Vizite();
                vizita.setId(rs.getInt("id"));
                vizita.setPronaId(rs.getInt("prona_id"));
                vizita.setKlientId(rs.getInt("klient_id"));
                vizita.setAgjentId(rs.getInt("agjent_id"));
                vizita.setDataOra(rs.getTimestamp("data_ora"));
                vizita.setStatusi(rs.getString("statusi"));
                vizita.setShenime(rs.getString("shenime"));
                vizite.add(vizita);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vizite;
    }

    public Vizite getViziteById(int id) {
        Vizite vizite = new Vizite();
        try {
            String query = "SELECT * FROM vizite WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                vizite.setId(rs.getInt("id"));
                vizite.setPronaId(rs.getInt("prona_id"));
                vizite.setKlientId(rs.getInt("klient_id"));
                vizite.setAgjentId(rs.getInt("agjent_id"));
                vizite.setDataOra(rs.getTimestamp("data_ora"));
                vizite.setStatusi(rs.getString("statusi"));
                vizite.setShenime(rs.getString("shenime"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vizite;
    }

    public List<Vizite> getViziteByAgjentId(int agjentId) {
        List<Vizite> vizite = new ArrayList<>();
        try {
            String query = "SELECT * FROM vizite WHERE agjent_id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, agjentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vizite vizita = new Vizite();
                vizita.setId(rs.getInt("id"));
                vizita.setPronaId(rs.getInt("prona_id"));
                vizita.setKlientId(rs.getInt("klient_id"));
                vizita.setAgjentId(rs.getInt("agjent_id"));
                vizita.setDataOra(rs.getTimestamp("data_ora"));
                vizita.setStatusi(rs.getString("statusi"));
                vizita.setShenime(rs.getString("shenime"));
                vizite.add(vizita);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vizite;
    }
}