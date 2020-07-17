package bzh.strawberry.survie.loterie.data;

import java.sql.Timestamp;
import java.util.UUID;

/*
 * This file (Ticket) is part of a project Survie.
 * It was created on 17/07/2020 19:14 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class Ticket {

    private final UUID player;
    private Timestamp date;
    private int number1;
    private int number2;
    private int number3;

    public Ticket(UUID player, Timestamp date, int number1, int number2, int number3) {
        this.player = player;
        this.date = date;
        this.number1 = number1;
        this.number2 = number2;
        this.number3 = number3;
    }

    public UUID getPlayer() {
        return player;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public int getNumber1() {
        return number1;
    }

    public int getNumber2() {
        return number2;
    }

    public void setNumber1(int number1) {
        this.number1 = number1;
    }

    public void setNumber2(int number2) {
        this.number2 = number2;
    }

    public int getNumber3() {
        return number3;
    }

    public void setNumber3(int number3) {
        this.number3 = number3;
    }
}
