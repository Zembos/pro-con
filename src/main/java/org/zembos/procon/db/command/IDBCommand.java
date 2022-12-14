package org.zembos.procon.db.command;

/**
 * Marker interface used to mark possible types for processing
 */
public sealed interface IDBCommand permits DBCommand, DBPoisonPill {

}
