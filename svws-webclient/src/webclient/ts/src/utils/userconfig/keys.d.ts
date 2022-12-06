/**
 * Die für Konfigurationsdaten zur Verfügung gestellte Schnittstelle,
 * nimmt per API Schlüssel mit Daten entgegen.
 * Auf Benutzerebene und auf globaler Ebene.
 * 
 * der Typ UserConfigKeys enthält Optionen, die im Client verwendet werden.
 */
type UserConfigKeys = {
	'gost.kursansicht.sortierung'?: 'kursart'|'fach_id';
}