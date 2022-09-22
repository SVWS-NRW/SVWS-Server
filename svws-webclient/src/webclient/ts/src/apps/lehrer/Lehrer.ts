import { App } from "../BaseApp";

import { DataLehrerStammdaten } from "./DataLehrerStammdaten";
import { DataLehrerPersonaldaten } from "./DataLehrerPersonaldaten";
import { ListLehrer } from "./ListLehrer";

/** Diese Klasse verwaltet die Lehrer-App */
export class Lehrer extends App {
	/** Informationen zum allgemeinen Status dieser Teilapplikation */
	public auswahl!: ListLehrer;

	/**
	 * Das Objekt zur Verwaltung der Kommunikation bezüglich der Stammdaten mit
	 * dem SVWS-Server
	 */
	public stammdaten!: DataLehrerStammdaten;

	/**
	 * Das Objekt zur Verwaltung der Kommunikation bezüglich der Personaldaten mit
	 * dem SVWS-Server
	 */
	public personaldaten!: DataLehrerPersonaldaten;

	/**
	 * Initialisiert die OpenAPI mit der aktuellen Konfiguration
	 *
	 * @returns {Promise<void>}
	 */
	public async init(): Promise<void> {
		this.auswahl = new ListLehrer();

		this.stammdaten = new DataLehrerStammdaten();
		this.personaldaten = new DataLehrerPersonaldaten();
		this.auswahl.add_data([this.stammdaten, this.personaldaten]);
	}
}
