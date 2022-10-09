import { App } from "../../../BaseApp";
import { DataBenutzer } from "./DataBenutzer";
import { ListBenutzer } from "./ListBenutzer";



/**
 * Diese Klasse enthält den Teil der SVWS-Client-Applikation für die Benutzer.
 * Beim Erstellen der SVWS-Client-Applikation wird ein Objekt dieser Klasse
 * erzeugt und steht unter "this.$app.apps.kataloge.benutzer" allen Vue-Komponenten zur
 * Verfügung. Hierdurch ist eine Kommunikation mit der Open-API-Schnittstelle
 * möglich, ohne das die Benutzerdaten, etc. über mehrere Komponenten hinweg
 * aktualisiert werden müssen.
 */
export class Benutzer extends App {

	public auswahl!: ListBenutzer;

	/**
	 * Das Objekt zur Verwaltung der Kommunikation bezüglich der Benutzerdaten mit dem
	 * SVWS-Server
	 */
	public dataBenutzer!: DataBenutzer;

	/**
	 * Initialisiert die Klasse und holt die relevanten Daten vom Server
	 *
	 * @returns {Promise<void>}
	 */
	public async init(): Promise<void> {
		this.auswahl = new ListBenutzer;
		this.dataBenutzer = new DataBenutzer();
		this.auswahl.add_data([this.dataBenutzer]);
	}

}