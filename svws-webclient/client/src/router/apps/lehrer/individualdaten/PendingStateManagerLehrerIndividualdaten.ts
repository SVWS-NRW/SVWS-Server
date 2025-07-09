import { PendingStateManager } from "@ui";
import type { AuswahlManager, LehrerStammdaten } from "@core";


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
	 * @param auswahlManager Funktion, die einen AuswahlManager bereitstellt.
	 */
	public constructor(idFieldName: any, auswahlManager: () => AuswahlManager<any, any, LehrerStammdaten>) {
		super(idFieldName, auswahlManager);
		this.initializeAttributeDisplayMappers();
	}

	/**
	 * Initialisiert die Mapper für die Attributanzeige und ordnet
	 * verschiedenen Attributen spezifische Darstellungslogiken zu.
	 */
	private initializeAttributeDisplayMappers() {

	}

}
