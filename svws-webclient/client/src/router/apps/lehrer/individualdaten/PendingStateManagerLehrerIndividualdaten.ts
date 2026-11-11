import type { LehrerStammdaten } from "@core/asd/data/lehrer/LehrerStammdaten";
import type { JavaMap } from "@core/java/util/JavaMap";
import type { ListeMitAuswahl } from "@ui/ui/manager/ListeMitAuswahl";
import { PendingStateManager } from "@ui/ui/wrapper/PendingStateManager";

/**
 * Die Klasse `PendingStateManagerLehrerIndividualdaten` erweitert den `PendingStateManager`
 * und dient der Verwaltung von individuell bezogenen Lehrerdaten innerhalb eines PendingState.
 *
 */
export class PendingStateManagerLehrerIndividualdaten extends PendingStateManager<LehrerStammdaten> {

	/**
	 * Konstruktor, der einen neuen PendingState für LehrerStammdaten erstellt.
	 *
	 * @param idFieldName Der Name des Attributs, welches Änderungen hält.
	 * @param liste Funktion, die die Auswahlliste mit der Mehrfachauswahl bereitstellt.
	 * @param listeDaten Funktion, die die geladenen Daten der Mehrfachauswahl bereitstellt.
	 */
	public constructor(idFieldName: any, liste: () => ListeMitAuswahl<number, any>, listeDaten: () => JavaMap<number, LehrerStammdaten>) {
		super(idFieldName, liste, listeDaten);
		this.initializeAttributeDisplayMappers();
	}

	/**
	 * Initialisiert die Mapper für die Attributanzeige und ordnet
	 * verschiedenen Attributen spezifische Darstellungslogiken zu.
	 */
	private initializeAttributeDisplayMappers() {

	}

}
