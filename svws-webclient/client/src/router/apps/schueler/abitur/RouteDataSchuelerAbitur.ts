import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { routeSchuelerAbiturLeistungsuebersicht } from "~/router/apps/schueler/abitur/RouteSchuelerAbiturLeistungsuebersicht";

interface RouteStateDataSchuelerAbitur extends RouteStateInterface {
	// Daten, die in Abhängigkeit des ausgewählten Schülers geladen werden
	idSchueler: number;
	hatGymOb: boolean;
}

const defaultState = <RouteStateDataSchuelerAbitur> {
	idSchueler: -1,
	hatGymOb: false,
	view: routeSchuelerAbiturLeistungsuebersicht,
};


export class RouteDataSchuelerAbitur extends RouteData<RouteStateDataSchuelerAbitur> {

	public constructor() {
		super(defaultState);
	}

	get idSchueler(): number {
		return this._state.value.idSchueler;
	}

	get hatGymOb(): boolean {
		return this._state.value.hatGymOb;
	}

	/**
	 * Lädt die Informationen zu der übergebenen Schüler-ID
	 *
	 * @param idSchueler   die ID des Schülers
	 * @param force        gibt an, ob das Laden erzwungen werden soll, obwohl die ID bereits geladen ist
	 */
	public async setSchueler(idSchueler: number, force: boolean = false) : Promise<void> {
		if ((!force) && (idSchueler === this._state.value.idSchueler))
			return;
		const listAbschnitte = await api.server.getSchuelerLernabschnittsliste(api.schema, idSchueler);
		let hatGymOb : boolean = false;
		for (const abschnitt of listAbschnitte) {
			if (("EF" === abschnitt.jahrgang) || ("Q1" === abschnitt.jahrgang) || ("Q2" === abschnitt.jahrgang)) {
				hatGymOb = true;
				break;
			}
		}
		let newState = <RouteStateDataSchuelerAbitur>{ idSchueler, hatGymOb, view: this._state.value.view };
		this.setPatchedDefaultState(newState)
	}

}
