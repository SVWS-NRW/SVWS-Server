import type { List } from "@core/java/util/List";
import type { AuswahlManager } from "@ui/ui/manager/AuswahlManager";
import type { ViewType } from "@ui/ui/nav/ViewType";

/**
 * Die Schnittstelle von generischen States für Auswahllisten
 */
export interface GenericAuswahlState<TAuswahlManager extends AuswahlManager<number, TAuswahl, TDaten>, TAuswahl = any, TDaten = any> {

	/**
	 * Gibt zurück, ob der State aktuell mit einer gültigen initialisiert und verfügbar ist.
	 */
	get isAvailable(): boolean;

	/**
	 * Gibt den Auswahl-Manager zurück.
	 *
	 * @throws DeveloperNotificationException wenn der State aktuell nicht initialisiert ist
	 */
	get manager(): TAuswahlManager;

	/**
	 * Führt ein Reset des States durch, indem der State in den Default-State zurück gesetzt wird
	 */
	reset(): void;

	/**
	 * Führt einen Patch auf den aktuellen Eintrag mit den übergebenen Daten aus.
	 *
	 * @param data   die Daten für den Patch
	 */
	patch(data: Partial<TDaten>): Promise<boolean>;

	/**
	 * Führt eine Löschoperation mit der aktuellen Auswahl aus.
	 */
	delete(): Promise<[boolean, List<string | null>]>;

	/**
	 * Setzt den Filter neu und lädt den ersten Eintrag aus der gefilterten Liste
	 */
	setFilter(): Promise<void>;

	/**
	 * Gibt die aktuelle Art der aktuellen View zurück (Default, Hinzufügen oder Gruppenprozess, Schnelleingabe).
	 */
	get activeViewType(): ViewType;

	/**
	 * Lädt die Default-Ansicht - also nicht die Ansicht für das Hinzufügen oder die Gruppenprozesse.
	 *
	 * @param id   die zu setzende ID oder null
	 */
	gotoDefaultView(id?: number | null): Promise<void>;

	/**
	 * Lädt die Gruppenprozess-Ansicht
	 *
	 * @param navigate   gibt an, ob ein Routing durchgeführt werden soll oder nur die View im State gesetzt werden soll
	 */
	gotoGruppenprozessView(navigate: boolean): Promise<void>;

	/**
	 * Lädt die Ansicht für das Hinzufügen von Daten
	 *
	 * @param navigate   gibt an, ob ein Routing durchgeführt werden soll oder nur die View im State gesetzt werden soll
	 */
	gotoHinzufuegenView(navigate: boolean): Promise<void>;

	/**
	 * Lädt die Ansicht für die Schnelleingabe von Daten
	 *
	 * @param navigate   gibt an, ob ein Routing durchgeführt werden soll oder nur die View im State gesetzt werden soll
	 * @param id         die ID des Schülers, zu dessen Schnelleingabe navigiert werden soll
	 */
	gotoSchnelleingabeView(navigate: boolean, id?: number | null): Promise<void>;

}
