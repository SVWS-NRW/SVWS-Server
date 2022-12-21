import { App } from "../BaseApp";

import { DataLehrerStammdaten } from "./DataLehrerStammdaten";
import { DataLehrerPersonaldaten } from "./DataLehrerPersonaldaten";
import { ListLehrer } from "./ListLehrer";

/** Diese Klasse verwaltet die Lehrer-App */
export class Lehrer extends App {

	/** Informationen zum allgemeinen Status dieser Teilapplikation */
	public auswahl!: ListLehrer;

	/**
	 * Initialisiert die OpenAPI mit der aktuellen Konfiguration
	 *
	 * @returns {Promise<void>}
	 */
	public async init(): Promise<void> {
		this.auswahl = new ListLehrer();
	}

}
