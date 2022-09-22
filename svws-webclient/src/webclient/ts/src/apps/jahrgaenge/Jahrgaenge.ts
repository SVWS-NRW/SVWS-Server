import { App } from "../BaseApp";

import { DataJahrgang } from "./DataJahrgang";
import { ListJahrgaenge } from "./ListJahrgaenge";

/**
 * Diese Klasse enthält den Jahrgangsspezifischen Teil des
 * SVWS-Client-Applikation. Beim Erstellen der SVWS-Client-Applikation wird ein
 * Objekt dieser Klasse erzeugt und steht unter "this.$app.appJahrgaenge" allen
 * Vue-Komponenten zur Verfügung. Hierdurch ist eine Kommunikation mit der
 * Open-API-Schnittstelle möglich, ohne das die Jahrgangsdaten, etc. über
 * mehrere Komponenten hinweg aktualisiert werden müssen.
 */
export class Jahrgaenge extends App {
	/** Informationen zum allgemeinen Status dieser Teilapplikation */
	public auswahl!: ListJahrgaenge;

	/**
	 * Das Objekt zur Verwaltung der Kommunikation bezüglich der Jahrgangsdaten
	 * mit dem SVWS-Server
	 */
	public jahrgangsdaten!: DataJahrgang;

	/**
	 * Initialisiert die Klasse und holt die relevanten Daten vom Server
	 *
	 * @returns {Promise<void>}
	 */
	public async init(): Promise<void> {
		this.auswahl = new ListJahrgaenge();
		this.jahrgangsdaten = new DataJahrgang();
		this.auswahl.add_data([this.jahrgangsdaten]);
	}
}
