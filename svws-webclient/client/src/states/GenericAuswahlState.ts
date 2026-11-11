import type { List } from "@core/java/util/List";
import type { ListeMitAuswahl } from "@ui/ui/manager/ListeMitAuswahl";

import type { GenericAuswahlNavigator } from "./GenericAuswahlNavigator";
import type { GenericDatenState } from "./GenericDatenState";
import type { GenericGruppenprozessState } from "./GenericGruppenprozessState";

/**
 * Ein Callback, der bei einer neuen Auswahl aufgerufen wird, damit ein State seine Daten zu der Auswahl laden kann.
 * Der Callback arbeitet in zwei Phasen: Er lädt die Daten (z.B. über die API) und liefert eine Funktion zurück, welche
 * die geladenen Daten übernimmt. Der Auswahl-State ruft die Funktionen erst auf, wenn alle Callbacks erfolgreich geladen
 * haben. So wird bei einem Fehler nichts übernommen und es entstehen keine Zwischenzustände.
 *
 * @param idAuswahl   die ID des gewählten Eintrags oder null, wenn die Auswahl entfernt wurde
 *
 * @returns eine Promise mit der Funktion, welche die geladenen Daten übernimmt
 */
export type GenericAuswahlSelectCallback = (idAuswahl: number | null) => Promise<() => void>;

/**
 * Ein Callback, der bei einer Änderung der Mehrfachauswahl aufgerufen wird, damit ein State seine Daten zu der
 * Mehrfachauswahl laden kann. Der Callback arbeitet wie {@link GenericAuswahlSelectCallback} in zwei Phasen.
 *
 * @param idsAuswahl   die IDs der Einträge der Mehrfachauswahl, die Liste ist leer, wenn keine Mehrfachauswahl besteht
 *
 * @returns eine Promise mit der Funktion, welche die geladenen Daten übernimmt
 */
export type GenericAuswahlMultiSelectCallback = (idsAuswahl: List<number>) => Promise<() => void>;

/**
 * Die Schnittstelle von generischen States für Auswahllisten.
 *
 * @typeParam TAuswahl   der Typ der Einträge der Auswahlliste
 * @typeParam TDaten     der Typ der Stammdaten zu einem Eintrag der Auswahlliste
 */
export interface GenericAuswahlState<TAuswahl extends { id: number }, TDaten> {

	/**
	 * Gibt zurück, ob der State aktuell mit einer gültigen initialisiert und verfügbar ist.
	 */
	get isAvailable(): boolean;

	/**
	 * Gibt den State für die Stammdaten der Auswahl zurück.
	 */
	get stammdaten(): GenericDatenState<TDaten>;

	/**
	 * Gibt den State für die Stammdaten der Mehrfachauswahl (Gruppenprozesse) zurück. Die Mehrfachauswahl selbst
	 * liegt in diesem Auswahl-State, der State hält nur die dazu geladenen Stammdaten.
	 *
	 * @returns der State für die Stammdaten der Mehrfachauswahl
	 */
	get gruppenprozesse(): GenericGruppenprozessState<TDaten>;

	/**
	 * Gibt den Navigator zurück, der die aktive Ansicht hält und die Ansicht und die Auswahl wechselt.
	 */
	get navigator(): GenericAuswahlNavigator;

	/**
	 * Gibt die ID des Eintrags der Einzelauswahl zurück oder null, falls kein Eintrag gewählt ist.
	 */
	get idSelected(): number | null;

	/**
	 * Gibt den Eintrag der Auswahlliste zurück, der in der Einzelauswahl gewählt ist.
	 *
	 * @throws DeveloperNotificationException wenn kein Eintrag gewählt ist
	 */
	get selected(): TAuswahl;

	/**
	 * Gibt die Auswahlliste mit der Einzel- und der Mehrfachauswahl zurück.
	 *
	 * @throws DeveloperNotificationException wenn der State aktuell nicht initialisiert ist
	 */
	get liste(): ListeMitAuswahl<number, TAuswahl>;

	/**
	 * Gibt die ID des aktuell gewählten Schuljahresabschnittes zurück
	 *
	 * @returns die ID des aktuell gewählten Schuljahresabschnittes
	 */
	get idSchuljahresabschnitt(): number;

	/**
	 * Initialisiert den State für den ausgewählten Schuljahresabschnitt. Es werden die Daten für diesen Abschnitt geladen.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 * @param force                    gibt an, ob das Laden der Daten auch erzwungen werden soll, wenn die ID des
	 *                                 Schuljahresabschnittes bereits gesetzt ist (z.B. beim Betreten einer Route)
	 *
	 * @returns ein Promise mit der ID der Auswahl oder null
	 */
	init(idSchuljahresabschnitt: number, force: boolean): Promise<number | null>;

	/**
	 * Hinterlegt einen Callback, der bei einer neuen Auswahl aufgerufen wird (auch beim Entfernen der Auswahl und
	 * beim Wechsel des Schuljahresabschnittes). Damit werden andere States über Änderungen der Auswahl benachrichtigt
	 * und können dazu ihre Daten nachladen.
	 *
	 * @param callback   der Callback
	 *
	 * @returns eine Funktion, welche den Callback wieder entfernt
	 */
	onSelect(callback: GenericAuswahlSelectCallback): () => void;

	/**
	 * Entfernt einen mit {@link onSelect} hinterlegten Callback. Ist der Callback nicht hinterlegt, so geschieht nichts.
	 *
	 * @param callback   der Callback
	 */
	removeOnSelect(callback: GenericAuswahlSelectCallback): void;

	/**
	 * Hinterlegt einen Callback, der bei einer Änderung der Mehrfachauswahl aufgerufen wird (auch beim Wechsel in die
	 * Ansicht der Gruppenprozesse und beim Wechsel des Schuljahresabschnittes, bei dem die Mehrfachauswahl verworfen wird).
	 *
	 * @param callback   der Callback
	 *
	 * @returns eine Funktion, welche den Callback wieder entfernt
	 */
	onMultiSelect(callback: GenericAuswahlMultiSelectCallback): () => void;

	/**
	 * Entfernt einen mit {@link onMultiSelect} hinterlegten Callback. Ist der Callback nicht hinterlegt, so geschieht nichts.
	 *
	 * @param callback   der Callback
	 */
	removeOnMultiSelect(callback: GenericAuswahlMultiSelectCallback): void;

	/**
	 * Setzt die Mehrfachauswahl und ruft danach die mit {@link onMultiSelect} hinterlegten Callbacks auf. Einträge, die
	 * nicht in der Auswahlliste enthalten sind, werden ignoriert.
	 *
	 * @param items   die Einträge der Mehrfachauswahl
	 */
	setMultiSelection(items: Iterable<TAuswahl>): Promise<void>;

	/**
	 * Gibt den Eintrag der Auswahlliste mit der ID zurück.
	 *
	 * @param id   die ID des Eintrags
	 *
	 * @returns der Eintrag oder null, falls er nicht in der Auswahlliste enthalten ist
	 */
	getEntry(id: number): TAuswahl | null;

	/**
	 * Passt den Eintrag der Auswahlliste mit der ID lokal an, z.B. wenn sich ein in der Liste angezeigtes Feld
	 * (wie der Nachname) in den Stammdaten geändert hat. Es wird nichts am Server geändert. Danach wird die
	 * Reaktivität getriggert und der Filter aktualisiert.
	 *
	 * @param id       die ID des Eintrags
	 * @param update   die Funktion, welche den Eintrag anpasst
	 *
	 * @throws DeveloperNotificationException   wenn der Eintrag nicht in der Auswahlliste enthalten ist
	 */
	updateEntry(id: number, update: (entry: TAuswahl) => void): void;

	/**
	 * Führt ein Commit auf dem State aus, d.h. die Reaktivität wird getriggert.
	 */
	commit(): void;

	/**
	 * Führt ein Reset des States durch, indem der State in den Default-State zurück gesetzt wird
	 */
	reset(): void;

	/**
	 * Führt eine Löschoperation mit der aktuellen Auswahl aus.
	 */
	delete(): Promise<[boolean, List<string | null>]>;

	/**
	 * Setzt den Filter neu und lädt den ersten Eintrag aus der gefilterten Liste
	 */
	setFilter(): Promise<void>;

}

/**
 * Die schmale Schnittstelle eines Auswahl-States, die States zu der Auswahl (z.B. die Stammdaten) benötigen, um sich
 * an die Auswahl anzuhängen und Einträge der Auswahlliste anzupassen.
 *
 * @typeParam TAuswahl   der Typ der Einträge der Auswahlliste
 */
export type GenericAuswahlHost<TAuswahl extends { id: number }> = Pick<GenericAuswahlState<TAuswahl, unknown>, "onSelect" | "getEntry" | "updateEntry">;

/**
 * Die schmale Schnittstelle eines Auswahl-States, die States zu der Mehrfachauswahl (z.B. die Stammdaten der
 * Gruppenprozesse) benötigen, um sich an die Mehrfachauswahl anzuhängen.
 *
 * @typeParam TAuswahl   der Typ der Einträge der Auswahlliste
 */
export type GenericAuswahlMultiHost<TAuswahl extends { id: number }> = Pick<GenericAuswahlState<TAuswahl, unknown>, "onMultiSelect" | "getEntry">;

