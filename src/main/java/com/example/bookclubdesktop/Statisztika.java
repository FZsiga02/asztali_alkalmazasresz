package com.example.bookclubdesktop;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Statisztika {

    private static List<Member> members;
    public static void run() {
        try {
            readMembersFromDatabase();
            System.out.printf("Kitiltott tagok száma: %d\n", countBannedMembers());
            System.out.printf("%s a tagok között 18 évnél fiatalabb személy. \n", isYoungerThan18Present()? "Van" : "Nincs");
            Member oldest = getOldest();
            System.out.printf("Legidősebb klubtag: %s (%s)\n", oldest.getName(), oldest.getBirth_date());
            printMemberCount();
            String name = readNameFromConsole();
            Member member = getMember(name);
            if (member == null) {
                System.out.println("Nincs ilyen nevű tagja a klubnak");
            } else {
                System.out.printf("A megadott személy %s", member.isBanned()? "ki van tiltva": "nincs kitiltva");
            }
        } catch (SQLException e) {
            System.out.println("Nem sikerült kapcsolódni adatbázishoz, az alkalmazás leáll");
            System.out.println(e.getMessage());
        }
    }

    private static Member getMember(String name) {
        int index = 0;
        while (index < members.size() && members.get(index).getName().equals(name)) {
            index++;
        }
        if (index < members.size()) {
            return  members.get(index);
        }
        return null;
    }

    private static String readNameFromConsole() {
        System.out.print("Adjon meg egy nevet: ");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    private static void printMemberCount() {
        Map<String, Integer> genderCounts = new HashMap<>();
        for (Member member: members) {
            genderCounts.putIfAbsent(member.getGenderDisplay(), 0);
            int count = genderCounts.get(member.getGenderDisplay());
            genderCounts.put(member.getGenderDisplay(), count + 1);
        }
        System.out.println("Tagok száma:");
        for (Map.Entry<String, Integer> gender: genderCounts.entrySet()) {
            System.out.printf("\t%s: %d", gender.getKey(), gender.getValue());
        }
    }

    private static Member getOldest() {
        Member oldest = members.get(0);
        for (int i = 1; i < members.size(); i++) {
            if (oldest.getBirth_date().isAfter(members.get(i).getBirth_date())) {
                oldest = members.get(i);
            }
        }
        return oldest;
    }

    private static boolean isYoungerThan18Present() {
        int index = 0;
        while (index < members.size() && !(Period.between(members.get(index).getBirth_date(), LocalDate.now()).getYears() < 18)) {
            index++;
        }
        return index < members.size();
    }

    private static int countBannedMembers() {
        int count = 0;
        for (Member member: members) {
            if (member.isBanned()) {
                count++;
            }
        }
        return count;
    }

    private static void readMembersFromDatabase() throws SQLException {
        DBHelper db = new DBHelper();
        members = db.readMembers();
    }
}
