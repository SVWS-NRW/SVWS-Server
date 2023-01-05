import { Schuljahresabschnitt } from "@svws-nrw/svws-core-ts";
import { App } from "../BaseApp";

import { DataSchuleStammdaten } from "./DataSchuleStammdaten";
import { ListSchule } from "./ListSchule";

/** Diese Klasse versorgt den Client mit den Schuldaten */
export class Schule extends App {
	/** Informationen zum Status dieser Teil-App */
	public auswahl!: ListSchule;
	/** Die Stammdaten der Schule */
	public schuleStammdaten!: DataSchuleStammdaten;

	/**
	 * Initialisiert die OpenAPI mit der aktuellen Konfiguration
	 *
	 * @returns {Promise<void>}
	 */
	public async init(): Promise<void> {
		this.auswahl = new ListSchule();
		this.schuleStammdaten = new DataSchuleStammdaten();
		//select(true) wird übergeben, weil es keine Liste gibt, aus der etwas ausgewählt werden kann...
		await this.schuleStammdaten.select(true);
		if (!this.schuleStammdaten.daten) return;
		const id = this.schuleStammdaten.daten.idSchuljahresabschnitt;
		const a = this.schuleStammdaten.daten.abschnitte
			.toArray(new Array<Schuljahresabschnitt>())
			.find(e => e.id === id);
		if (a) App.akt_abschnitt = a;
	}

	/** Leer, da keine Liste vorhanden ist */
	public async update_liste(): Promise<void> {
		void undefined;
	}
}
