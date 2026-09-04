import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { routeLehrer } from "../lehrer/RouteLehrer";
import { routeSchueler } from "../schueler/RouteSchueler";
import { routeStatistikUebersicht } from "./RouteStatistikUebersicht";
import type { SchuelerListeManager } from "~/states/schueler/SchuelerListeManager";
import { SchuleStammdaten } from "@core/asd/data/schule/SchuleStammdaten";
import { StatistikGesamt } from "@core/asd/data/statistik/StatistikGesamt";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import { LehrerListeManager } from "@ui/ui/manager/lehrer/LehrerListeManager";

interface RouteStateStatistik extends RouteStateInterface {
	schuleStammdaten: SchuleStammdaten;
	statistikGesamt: StatistikGesamt;
	mapLehrer: Map<number, LehrerListeEintrag>;
	mapSchueler: Map<number, SchuelerListeEintrag>;
	managerLehrer: LehrerListeManager;
	managerSchueler: SchuelerListeManager | undefined;
};

const defaultState = <RouteStateStatistik> {
	view: routeStatistikUebersicht,
	schuleStammdaten: new SchuleStammdaten(),
	statistikGesamt: new StatistikGesamt(),
	mapLehrer: new Map<number, LehrerListeEintrag>(),
	mapSchueler: new Map<number, SchuelerListeEintrag>(),
	managerLehrer: new LehrerListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	managerSchueler: undefined,
};

export class RouteDataStatistik extends RouteData<RouteStateStatistik> {

	public constructor() {
		super(defaultState);
	}

	get statistikGesamt() {
		return this._state.value.statistikGesamt;
	}

	get schuleStammdaten() {
		return this._state.value.schuleStammdaten;
	}

	get mapSchueler() {
		return this._state.value.mapSchueler;
	}

	get mapLehrer() {
		return this._state.value.mapLehrer;
	}

	get managerLehrer() {
		return this._state.value.managerLehrer;
	}

	get managerSchueler() {
		if (this._state.value.managerSchueler === undefined) {
			throw new DeveloperNotificationException("Der Manager ist nicht initialisiert");
		}
		return this._state.value.managerSchueler;
	}

	public async ladeDaten() {
		const statistikGesamt = await api.server.getStatistikGesamt(api.schema);
		const schuleStammdaten = await api.server.getSchuleStammdaten(api.schema);
		const listeSchueler = await api.server.getSchuelerAktuell(api.schema);
		const listeLehrer = await api.server.getLehrerFuerAbschnitt(api.schema, schuleStateImpl.abschnitt.id);
		await routeLehrer.data.setSchuljahresabschnitt(schuleStateImpl.abschnitt.id, true);
		const managerLehrer = routeLehrer.data.manager;
		await routeSchueler.data.setSchuljahresabschnitt(schuleStateImpl.abschnitt.id, true);
		const managerSchueler = routeSchueler.data.manager;
		const mapLehrer = new Map<number, LehrerListeEintrag>();
		const mapSchueler = new Map<number, SchuelerListeEintrag>();
		for (const s of listeSchueler) {
			mapSchueler.set(s.id, s);
		}
		for (const l of listeLehrer) {
			mapLehrer.set(l.id, l);
		}
		this.setPatchedState({ statistikGesamt, schuleStammdaten, mapLehrer, mapSchueler, managerLehrer, managerSchueler });
	}

	gotoSchueler = async (eintrag: SchuelerListeEintrag) => {
		await RouteManager.doRoute(routeSchueler.getRoute({ id: eintrag.id }));
	};

	gotoLehrer = async (eintrag: LehrerListeEintrag) => {
		await RouteManager.doRoute(routeLehrer.getRoute({ id: eintrag.id }));
	};

	public updateDatenLehrer = async (id: number) => {
		const managerLehrer = this.managerLehrer;
		const daten = await api.server.getLehrerStammdaten(api.schema, id);
		managerLehrer.setDaten(daten);
		const personaldaten = await api.server.getLehrerPersonaldaten(api.schema, id);
		managerLehrer.setPersonalDaten(personaldaten);
		this.setPatchedState({ managerLehrer });
	};

	public updateDatenSchueler = async (id: number) => {
		const managerSchueler = this.managerSchueler;
		const daten = await api.server.getSchuelerStammdaten(api.schema, id);
		managerSchueler.setDaten(daten);
		this.setPatchedState({ managerSchueler });
	};
}
