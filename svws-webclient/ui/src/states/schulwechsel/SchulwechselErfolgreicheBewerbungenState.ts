import type { InjectionKey } from "vue";

import type { SchulwechselErfolgreicheBewerbung } from "@core/core/data/schule/SchulwechselErfolgreicheBewerbung";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import type { List } from "@core/java/util/List";
import { AppContext } from "@ui/AppContext";

/**
 * Die Schnittstelle für die Anzeige der erfolgreichen Bewerbungen (Schulwechsel) und ihrer API-Methoden
 */
export interface SchulwechselErfolgreicheBewerbungenState {
	/** Die erfolgreichen Bewerbungen, die in der Datenbank vorliegen */
	get erfolgreicheBewerbungen(): List<SchulwechselErfolgreicheBewerbung>;

	/**
	 * Die Anzahl der erfolgreichen Bewerbungen beim Remote-Server, die noch nicht in der Datenbank vorliegen.
	 * Der Wert ist null, solange er unbekannt ist, d. h. solange die Abfrage läuft oder wenn sie fehlgeschlagen ist.
	 */
	get newBewerbungenCount(): number | null;

	/** Gibt an, ob gerade die Anzahl der neuen erfolgreichen Bewerbungen beim Remote-Server abgefragt wird */
	get isPreviewRunning(): boolean;

	/** Lädt die erfolgreichen Bewerbungen aus der Datenbank */
	updateErfolgreicheBewerbungen(): Promise<void>;

	/**
	 * Fragt beim Remote-Server die Anzahl der noch nicht importierten erfolgreichen Bewerbungen ab.
	 * Schlägt die Abfrage fehl, wird eine Fehlermeldung als Notification angezeigt, statt den Fehler zu werfen.
	 */
	updateNewBewerbungenCount(): Promise<void>;

	/**
	 * Importiert die neuen erfolgreichen Bewerbungen vom Remote-Server und aktualisiert danach die Daten.
	 * Schlägt der Import fehl, wird eine Fehlermeldung als Notification angezeigt, statt den Fehler zu werfen.
	 * Die Anzahl der importierten Datensätze liefert der Server nicht. Sie ergibt sich aus der Differenz der Listengröße
	 * vor und nach dem Import.
	 *
	 * @returns die Anzahl der neu angelegten Datensätze (0, wenn keine neuen vorlagen) oder null, wenn der Import
	 *          fehlgeschlagen ist oder nach dem Import die Daten nicht neu geladen werden konnten (die Fehler sind dann gemeldet)
	 */
	sync(): Promise<number | null>;
}

export const SchulwechselErfolgreicheBewerbungenStateKey: InjectionKey<SchulwechselErfolgreicheBewerbungenState> = Symbol('SchulwechselErfolgreicheBewerbungenState');

export function useSchulwechselErfolgreicheBewerbungenState(): SchulwechselErfolgreicheBewerbungenState {
	const state = AppContext.instance.inject(SchulwechselErfolgreicheBewerbungenStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurde keine Instanz des SchulwechselErfolgreicheBewerbungenState über provide in der main.ts eingebunden");
	}
	return state;
}
