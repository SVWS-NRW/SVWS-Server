import { App } from "../BaseApp";

import { DataFach } from "./DataFach";
import { ListFaecher } from "./ListFaecher";

/**
 * Diese Klasse enthält den Fächerspezifischen Teil des SVWS-Client-Applikation.
 * Beim Erstellen der SVWS-Client-Applikation wird ein Objekt dieser Klasse
 * erzeugt und steht unter "this.$app.apps.faecher" allen Vue-Komponenten zur
 * Verfügung. Hierdurch ist eine Kommunikation mit der Open-API-Schnittstelle
 * möglich, ohne das die Fachdaten, etc. über mehrere Komponenten hinweg
 * aktualisiert werden müssen.
 */
export class Faecher extends App {
	public auswahl!: ListFaecher;

	/**
	 * Das Objekt zur Verwaltung der Kommunikation bezüglich der Fachdaten mit dem
	 * SVWS-Server
	 */
	public dataFach!: DataFach;
	/**
	 * Initialisiert die Klasse und holt die relevanten Daten vom Server
	 *
	 * @returns {Promise<void>}
	 */
	public async init(): Promise<void> {
		this.auswahl = new ListFaecher();
		this.dataFach = new DataFach();
		this.auswahl.add_data([this.dataFach]);
	}
}
