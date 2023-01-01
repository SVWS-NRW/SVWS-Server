import { FoerderschwerpunktEintrag, KatalogEintrag, List, Vector } from "@svws-nrw/svws-core-ts";
import { App } from "../BaseApp";
import { ListSchueler } from "./ListSchueler";
import { DataSchuelerLaufbahnplanung } from "./DataSchuelerLaufbahnplanung";
import { ListAbschnitte } from "./ListAbschnitte";
import { DataSchuelerAbschnittsdaten } from "./DataSchuelerAbschnittsdaten";
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
	 * Das Objekt zur Verwaltung der Kommunikation bezüglich der
	 * Schülerlaufbahndaren des Schülers der gymnasialen Oberstufe mit dem SVWS-Server
	 */
	public dataGostLaufbahndaten: DataSchuelerLaufbahnplanung | undefined =
		undefined;

	public listAbschnitte!: ListAbschnitte;
	public dataSchuelerAbschnittsdaten!: DataSchuelerAbschnittsdaten;

	/** Liste der Schülerbetriebe */
	public listSchuelerbetriebe : ListSchuelerBetriebsdaten | undefined = undefined;
	/** Objekt für Betriebsstammdaten */
	public betriebsStammdaten!: DataBetriebsstammdaten;

	/**
	 * Initialisiert die OpenAPI mit der aktuellen Konfiguration
	 *
	 * @returns {Promise<void>}
	 */
	public async init(): Promise<void> {
		this.listAbschnitte = new ListAbschnitte();
		this.dataSchuelerAbschnittsdaten = new DataSchuelerAbschnittsdaten();
		this.listAbschnitte.add_data(this.dataSchuelerAbschnittsdaten);

		this.auswahl = new ListSchueler(this.listAbschnitte);
		if (mainApp.config.hasGost) {
			this.dataGostLaufbahndaten = new DataSchuelerLaufbahnplanung();
			this.auswahl.add_data(this.dataGostLaufbahndaten);
		}
		this.listSchuelerbetriebe  = new ListSchuelerBetriebsdaten();
		this.betriebsStammdaten = new DataBetriebsstammdaten();
		this.listSchuelerbetriebe.add_data(this.betriebsStammdaten);
	}
}
