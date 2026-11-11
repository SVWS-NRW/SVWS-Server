import type { JavaMap } from "@core/java/util/JavaMap";
import type { List } from "@core/java/util/List";
import type { PendingStateManager } from "@ui/ui/wrapper/PendingStateManager";

/**
 * Die Schnittstelle von generischen States für die Stammdaten der Mehrfachauswahl (Gruppenprozesse) zu einer
 * Auswahlliste. Die Mehrfachauswahl selbst liegt im Auswahl-State. Der State hängt sich dort an, hält die Stammdaten
 * der Einträge der Mehrfachauswahl, lädt diese nach und führt Patches auf mehreren Datensätzen aus.
 *
 * @typeParam TDaten   der Typ der Stammdaten
 */
export interface GenericGruppenprozessState<TDaten> {

	/**
	 * Gibt die Stammdaten der Mehrfachauswahl zugeordnet zu deren ID zurück.
	 */
	get listeDaten(): JavaMap<number, TDaten>;

	/**
	 * Lädt die Stammdaten der übergebenen Mehrfachauswahl, ohne sie zu übernehmen. Es werden nur die Stammdaten
	 * nachgeladen, die noch nicht vorliegen. Stammdaten von Einträgen, die nicht mehr ausgewählt sind, werden bei der
	 * Übernahme entfernt.
	 *
	 * @param idsAuswahl   die IDs der Einträge der Mehrfachauswahl
	 *
	 * @returns eine Promise mit der Funktion, welche die geladenen Daten übernimmt
	 */
	loadForMultiSelection(idsAuswahl: List<number>): Promise<() => void>;

	/**
	 * Führt die Patches des übergebenen Pending-State-Managers auf den Stammdaten der Mehrfachauswahl aus.
	 *
	 * @param pendingStateManager   der Pending-State-Manager mit den Änderungen
	 */
	patchMultiple(pendingStateManager: PendingStateManager<any>): Promise<void>;

	/**
	 * Setzt den State in den Default-State zurück.
	 */
	reset(): void;

}
