import type { LehrerFunktion } from "@core/asd/data/lehrer/LehrerFunktion";
import type { List } from "@core/java/util/List";

import { api } from "~/router/Api";

import type { LehrerPersonaldatenState } from "./LehrerPersonaldatenState";

/**
 * Die Operationen für die schulinternen Funktionen im Personalabschnitt des aktuellen Schuljahresabschnittes
 */
export class LehrerFunktionenOperationen {

	private readonly _personaldaten: LehrerPersonaldatenState;

	/**
	 * Erzeugt die Operationen.
	 *
	 * @param personaldaten   der State mit den Personaldaten
	 */
	public constructor(personaldaten: LehrerPersonaldatenState) {
		this._personaldaten = personaldaten;
	}

	/**
	 * Fügt Funktionen hinzu.
	 *
	 * @param data   die Daten der Funktionen
	 */
	public async add(data: List<Partial<LehrerFunktion>>): Promise<void> {
		const abschnitt = this._personaldaten.abschnittAktuellOrException();
		abschnitt.funktionen.addAll(await api.server.createLehrerFunktionen(data, api.schema));
		this._personaldaten.commit();
	}

	/**
	 * Entfernt eine Funktion.
	 *
	 * @param data   die Funktion
	 */
	public async remove(data: LehrerFunktion): Promise<void> {
		const abschnitt = this._personaldaten.abschnittAktuellOrException();
		await api.server.deleteLehrerFunktion(api.schema, data.id);
		abschnitt.funktionen.remove(data);
		this._personaldaten.commit();
	}

}
