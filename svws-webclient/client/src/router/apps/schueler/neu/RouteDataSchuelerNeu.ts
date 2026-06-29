import type { RouteStateInterface } from "~/router/RouteData";
import { RouteData } from "~/router/RouteData";
import type { KlassenListeEintrag, List } from "@core";
import { DeveloperNotificationException } from "@core";
import { api } from "~/router/Api";
import { routeApp } from "~/router/apps/RouteApp";
import { RouteManager } from "~/router/RouteManager";
import { routeSchuelerSchnelleingabe } from "~/router/apps/schueler/neu/RouteSchuelerSchnelleingabe";
import { SchuelerNeuManager } from "@ui";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";


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
		const klassenFuerAbschnitt = await this.getKlassenBySchuljahresabschnitt();
		const jahrgaengeById = routeApp.cache.kataloge.jahrgaengeById;
		const religionenById = routeApp.cache.kataloge.religionenById;

		return new SchuelerNeuManager(kindergaertenById, einschulungsartenById, jahrgaengeById, religionenById, abschnittStateImpl.alle, klassenFuerAbschnitt,
			abschnittStateImpl.auswahl);
	}

	private async getKlassenBySchuljahresabschnitt(): Promise<Map<number, List<KlassenListeEintrag>>> {
		const klassenByIdAbschnitt = new Map();
		const idAktuellerAbschnitt = abschnittStateImpl.auswahl.id;
		const klassenAktuellerAbschnitt = await api.server.getListKlassenListeEintragBySchuljahresabschnitt(api.schema, idAktuellerAbschnitt);
		klassenByIdAbschnitt.set(idAktuellerAbschnitt, klassenAktuellerAbschnitt);

		const idFolgeabschnitt = abschnittStateImpl.auswahl.idFolgeAbschnitt;
		if (idFolgeabschnitt !== null) {
			const klassenFolgeAbschnitt = await api.server.getListKlassenListeEintragBySchuljahresabschnitt(api.schema, idFolgeabschnitt);
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
