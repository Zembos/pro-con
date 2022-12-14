package org.zembos.procon.db.command;

/**
 * Poison pill used to kill consumer when no more commands
 */
public final class DBPoisonPill implements IDBCommand {

    public static final DBPoisonPill POISON_PILL = new DBPoisonPill();

    private DBPoisonPill() {}

    @Override
    public String toString() {
        return "~~~~Poison pill~~~~";
    }
}
