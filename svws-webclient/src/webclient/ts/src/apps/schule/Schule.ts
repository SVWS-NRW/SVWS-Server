import {
	Schulform,
	Schulgliederung,
	Schuljahresabschnitt,
} from "@svws-nrw/svws-core-ts";
import { App } from "../BaseApp";

import { DataSchuleStammdaten } from "./DataSchuleStammdaten";
import { ListSchule } from "./ListSchule";

/** Diese Klasse versorgt den Client mit den Schuldaten */
export class Schule extends App {
	/** Informationen zum Status dieser Teil-App */
	public auswahl!: ListSchule;
	/** Die Stammdaten der Schule */
	public schuleStammdaten!: DataSchuleStammdaten;
	/** Die ermittelte Schulform */
	public schulform!: Schulform | null;
	/** Die an der Schule verfügbaren Schulgliederungen */
	public schulgliederungen: Array<Schulgliederung> = [];

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
		this.schulform = !this.schuleStammdaten.daten.schulform
			? null
			: Schulform.getByKuerzel(this.schuleStammdaten.daten.schulform);
		this.schulgliederungen = !this.schulform
			? []
			: Schulgliederung.get(this.schulform).toArray(
					Array<Schulgliederung>()
			  );
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
