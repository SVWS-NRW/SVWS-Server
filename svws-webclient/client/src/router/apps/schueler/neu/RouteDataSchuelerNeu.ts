import type { RouteStateInterface } from "~/router/RouteData";
import { RouteData } from "~/router/RouteData";
import type { KlassenDaten, List } from "@core";
import { DeveloperNotificationException } from "@core";
import { api } from "~/router/Api";
import { routeApp } from "~/router/apps/RouteApp";
import { RouteManager } from "~/router/RouteManager";
import { routeSchuelerSchnelleingabe } from "~/router/apps/schueler/neu/RouteSchuelerSchnelleingabe";
import { SchuelerNeuManager } from "@ui";


interface RouteStateDataSchuelerNeu extends RouteStateInterface {
	manager: SchuelerNeuManager | undefined;
}

const defaultState = <RouteStateDataSchuelerNeu> {
	manager: undefined,
};

export class RouteDataSchuelerNeu extends RouteData<RouteStateDataSchuelerNeu> {

	public constructor() {
		super(defaultState);
	}

	public async ladeDaten() {
		const manager = await this.createManager();
		this.setPatchedState({ manager });
	}

	private async createManager() {
		const kindergaertenById = routeApp.cache.kataloge.kindergaertenById;
		const einschulungsartenById = routeApp.cache.kataloge.einschulungsartenById;
		const klassenFuerAbschnitt = await this.getKlassenFuerAbschnitt();
		const jahrgaengeById = routeApp.cache.kataloge.jahrgaengeById;
		const religionenById = routeApp.cache.kataloge.religionenById;
		const schuljahresabschnitte = api.schuleStammdaten.abschnitte;
		const aktuellerAbschnitt = routeApp.data.aktAbschnitt.value;

		return new SchuelerNeuManager(kindergaertenById, einschulungsartenById, jahrgaengeById, religionenById, schuljahresabschnitte, klassenFuerAbschnitt,
			aktuellerAbschnitt);
	}

	private async getKlassenFuerAbschnitt(): Promise<Map<number, List<KlassenDaten>>> {
		const klassenByIdAbschnitt = new Map();
		const idAktuellerAbschnitt = routeApp.data.aktAbschnitt.value.id;
		const klassenAktuellerAbschnitt = await api.server.getKlassenFuerAbschnitt(api.schema, idAktuellerAbschnitt);
		klassenByIdAbschnitt.set(idAktuellerAbschnitt, klassenAktuellerAbschnitt);

		const idFolgeabschnitt = routeApp.data.aktAbschnitt.value.idFolgeAbschnitt;
		if (idFolgeabschnitt !== null) {
			const klassenFolgeAbschnitt = await api.server.getKlassenFuerAbschnitt(api.schema, idFolgeabschnitt);
			klassenByIdAbschnitt.set(idFolgeabschnitt, klassenFolgeAbschnitt);
		}
		return klassenByIdAbschnitt;
	};

	get manager(): SchuelerNeuManager {
		if (this._state.value.manager === undefined) {
			throw new DeveloperNotificationException("Unerwarteter Fehler: SchuelerNeuManager nicht initialisiert");
		}
		return this._state.value.manager;
	}

	goToSchnelleingabe = async (idSchueler: number): Promise<void> => {
		await RouteManager.doRoute(routeSchuelerSchnelleingabe.getRoute({ id: idSchueler }));
	};

}
