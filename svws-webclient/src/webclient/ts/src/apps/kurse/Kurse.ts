import { App } from "../BaseApp";

import { DataKurs } from "./DataKurs";
import { ListKurse } from "./ListKurse";

/**
 * Diese Klasse enthält den Kursspezifischen Teil des SVWS-Client-Applikation.
 * Beim Erstellen der SVWS-Client-Applikation wird ein Objekt dieser Klasse
 * erzeugt und steht unter "this.$app.appKurse" allen Vue-Komponenten zur
 * Verfügung. Hierdurch ist eine Kommunikation mit der Open-API-Schnittstelle
 * möglich, ohne das die Kursdaten, etc. über mehrere Komponenten hinweg
 * aktualisiert werden müssen.
 */
export class Kurse extends App {
	/** Informationen zum allgemeinen Status dieser Teilapplikation */
	public auswahl!: ListKurse;

	/**
	 * Das Objekt zur Verwaltung der Kommunikation bezüglich der Kursdaten mit dem
	 * SVWS-Server
	 */
	public kursdaten!: DataKurs;

	/**
	 * Initialisiert die Klasse und holt die relevanten Daten vom Server
	 *
	 * @returns {Promise<void>}
	 */
	public async init(): Promise<void> {
		this.auswahl = new ListKurse();
		this.kursdaten = new DataKurs();
		this.auswahl.add_data([this.kursdaten]);
	}
}
