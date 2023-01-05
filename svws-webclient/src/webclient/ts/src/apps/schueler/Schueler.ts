import { App } from "../BaseApp";
import { ListSchueler } from "./ListSchueler";
import { DataSchuelerLaufbahnplanung } from "./DataSchuelerLaufbahnplanung";
import { mainApp } from "../Main";
import { ListSchuelerBetriebsdaten } from "./ListSchuelerBetriebsdaten";
import { DataBetriebsstammdaten } from "./DataBetriebsstammdaten";

/**
 * Diese Klasse enthält den Schülerspezifischen Teil des
 * SVWS-Client-Applikation. Beim Erstellen der SVWS-Client-Applikation wird ein
 * Objekt dieser Klasse erzeugt und steht unter "this.$app.appSchueler" allen
 * Vue-Komponenten zur Verfügung. Hierdurch ist eine Kommunikation mit der
 * Open-API-Schnittstelle möglich, ohne das die Stammdaten, etc. über mehrere
 * Komponenten hinweg aktualisiert werden müssen.
 */

export class Schueler extends App {
	/** Informationen zum allgemeinen Status dieser Teilapplikation */
	public auswahl!: ListSchueler;

	/**
	 * Initialisiert die OpenAPI mit der aktuellen Konfiguration
	 *
	 * @returns {Promise<void>}
	 */
	public async init(): Promise<void> {
		this.auswahl = new ListSchueler();
	}
}
