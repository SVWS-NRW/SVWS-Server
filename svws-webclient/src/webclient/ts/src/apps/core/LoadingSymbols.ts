/**
 * Erstellen aller benötigten Symbols für die Ladeprozesse in der Anwendung.
 */

/** Ladeprozess zum initialen Laden der Anwendung */
export const MAIN_LOADING_SYMBOL = Symbol('initialisierung');
/** Ladeprozess für das serverseitige Erstellen neuer Blockungen */
export const GOST_CREATE_BLOCKUNG_SYMBOL = Symbol('gost-blockung-erstellen');
