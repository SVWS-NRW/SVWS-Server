import type { Schuljahresabschnitt } from "@svws-nrw/svws-core-ts";

/**
 * Die für Konfigurationsdaten zur Verfügung gestellte Schnittstelle,
 * nimmt per API Schlüssel mit Daten entgegen.
 * Auf Benutzerebene und auf globaler Ebene.
 *
 * der Typ UserConfigKeys enthält Optionen, die im Client verwendet werden.
 */
export type UserConfigKeys = {
	'gost.kursansicht.sortierung'?: 'kursart'|'fach_id';
	'app.akt_abschnitt'?: Schuljahresabschnitt | undefined;
}
