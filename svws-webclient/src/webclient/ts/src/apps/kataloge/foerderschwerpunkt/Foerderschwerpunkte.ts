import { App } from "../../BaseApp";

import { DataFoerderschwerpunkt } from "./DataFoerderschwerpunkt";
import { ListFoerderschwerpunkte } from "./ListFoerderschwerpunkte";

/**
 * Diese Klasse enthält den Teil der SVWS-Client-Applikation für die Förderschwerpunkte.
 * Beim Erstellen der SVWS-Client-Applikation wird ein Objekt dieser Klasse
 * erzeugt und steht unter "this.$app.apps.kataloge.foerderschwerpunkte" allen Vue-Komponenten zur
 * Verfügung. Hierdurch ist eine Kommunikation mit der Open-API-Schnittstelle
 * möglich, ohne das die Förderschwerpunktdaten, etc. über mehrere Komponenten hinweg
 * aktualisiert werden müssen.
 */
export class Foerderschwerpunkte extends App {

	public auswahl!: ListFoerderschwerpunkte;

	/**
	 * Das Objekt zur Verwaltung der Kommunikation bezüglich der Förderschwerpunktdaten mit dem
	 * SVWS-Server
	 */
	public dataFoerderschwerpunkt!: DataFoerderschwerpunkt;

	/**
	 * Initialisiert die Klasse und holt die relevanten Daten vom Server
	 *
	 * @returns {Promise<void>}
	 */
	public async init(): Promise<void> {
		this.auswahl = new ListFoerderschwerpunkte();
		this.dataFoerderschwerpunkt = new DataFoerderschwerpunkt();
		this.auswahl.add_data([this.dataFoerderschwerpunkt]);
	}

}
