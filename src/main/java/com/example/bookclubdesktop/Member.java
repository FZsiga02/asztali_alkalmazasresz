package com.example.bookclubdesktop;

import java.time.LocalDate;

public class Member {
    private int id;
    private String name;
    private String gender;
    private LocalDate birth_date;
    private boolean banned;

    public Member(int id, String name, String gender, LocalDate birth_date, boolean banned) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birth_date = birth_date;
        this.banned = banned;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getGenderDisplay() {
        if (this.gender == null) {
            return "Ismeretlen";
        }
        switch (this.gender) {
            case "F":
                return "Nő";
            case "M":
                return "Férfi";
            default:
                return "Ismeretlen";
        }
    }

    public LocalDate getBirth_date() {
        return birth_date;
    }

    public boolean isBanned() {
        return banned;
    }

    public  String getBannedDisplay() {
        return banned ? "X" : "";
    }
}
