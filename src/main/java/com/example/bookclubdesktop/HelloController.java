package com.example.bookclubdesktop;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class HelloController {

    @FXML
    private Button bannButton;
    @FXML
    private TableView<Member> members;
    @FXML
    private TableColumn<Member, String> name;
    @FXML
    private TableColumn<Member, String> gender;
    @FXML
    private TableColumn<Member, LocalDate> birth_date;
    @FXML
    private TableColumn<Member, String> banned;

    private DBHelper db;

    public void initialize() {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        gender.setCellValueFactory(new PropertyValueFactory<>("genderDisplay"));
        birth_date.setCellValueFactory(new PropertyValueFactory<>("birth_date"));
        banned.setCellValueFactory(new PropertyValueFactory<>("bannedDisplay"));
        try {
            db = new DBHelper();
            refreshTable();
        } catch (SQLException e) {
            Platform.runLater(() -> {
                alert(Alert.AlertType.ERROR, "Nem sikerült kapcsolódni az adatbázishoz", e.getMessage());
                Platform.exit();
            });
        }
    }

    private void refreshTable() throws SQLException {
        members.getItems().clear();
        members.getItems().addAll(db.readMembers());
    }

    private Optional<ButtonType> alert(Alert.AlertType alertType, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert.showAndWait();
    }

    @FXML
    public void bannClick(ActionEvent actionEvent) {
        Member selected = members.getSelectionModel().getSelectedItem();
        if (selected == null) {
            alert(Alert.AlertType.WARNING, "Tiltás módosításához előbb válasszon ki klubtagot", "");
            return;
        }
        String headerText = selected.isBanned() ? "Biztos szeretné visszavonni a kiválasztott klubtag tiltását?" : "Biztos szeretné kitiltani a kiválasztott klubtagot?";
        Optional<ButtonType> confirm = alert(Alert.AlertType.CONFIRMATION, headerText, "");
        if (confirm.isPresent() && confirm.get().equals(ButtonType.OK)) {
            changeBanned(selected);
        }
    }

    private void changeBanned(Member selected) {
        try {
            if (db.changedBanned(selected)) {
                alert(Alert.AlertType.INFORMATION, "Sikeres módosítás", "");
            } else {
                alert(Alert.AlertType.WARNING, "Sikertelen módosítás", "");
            }
            refreshTable();
        } catch (SQLException e) {
            alert(Alert.AlertType.ERROR, "Hiba történt a módosítás során", e.getMessage());
        }
    }
}