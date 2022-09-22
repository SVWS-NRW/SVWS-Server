import { App } from "../BaseApp";

import { DataKlasse } from "./DataKlasse";
import { ListKlassen } from "./ListKlassen";

/**
 * Diese Klasse enthält den Klassenspezifischen Teil des
 * SVWS-Client-Applikation. Beim Erstellen der SVWS-Client-Applikation wird ein
 * Objekt dieser Klasse erzeugt und steht unter `this.$app.apps.klassen` allen
 * Vue-Komponenten zur Verfügung. Hierdurch ist eine Kommunikation mit der
 * Open-API-Schnittstelle möglich, ohne das die Klassendaten, etc. über mehrere
 * Komponenten hinweg aktualisiert werden müssen.
 */
export class Klassen extends App {
	/** Informationen zum allgemeinen Status dieser Teilapplikation */
	public auswahl!: ListKlassen;

	/**
	 * Das Objekt zur Verwaltung der Kommunikation bezüglich der Klassendaten mit
	 * dem SVWS-Server
	 */
	public klassendaten!: DataKlasse;

	/**
	 * Initialisiert die Klasse und holt die relevanten Daten vom Server
	 *
	 * @returns {Promise<void>}
	 */
	public async init(): Promise<void> {
		this.auswahl = new ListKlassen();
		this.klassendaten = new DataKlasse();
		this.auswahl.add_data([this.klassendaten]);
	}
}
