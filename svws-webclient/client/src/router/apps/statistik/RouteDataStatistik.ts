import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";

import { routeLehrer } from "../lehrer/RouteLehrer";
import { routeSchueler } from "../schueler/RouteSchueler";
import type { RouteStateInterface } from "~/router/RouteData";
import { RouteData } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";

import { routeStatistikUebersicht } from "./RouteStatistikUebersicht";


interface RouteStateStatistik extends RouteStateInterface {
	// mapLehrer: Map<number, LehrerListeEintrag>;
	// mapSchueler: Map<number, SchuelerListeEintrag>;
	// managerLehrer: LehrerListeManager;
	// managerSchueler: SchuelerListeManager | undefined;
};

const defaultState = <RouteStateStatistik> {
	view: routeStatistikUebersicht,
	// statistikGesamt: new StatistikGesamt(),
	// mapLehrer: new Map<number, LehrerListeEintrag>(),
	// mapSchueler: new Map<number, SchuelerListeEintrag>(),
	// managerLehrer: new LehrerListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	// managerSchueler: undefined,
};

export class RouteDataStatistik extends RouteData<RouteStateStatistik> {

	public constructor() {
		super(defaultState);
	}

	gotoSchueler = async (eintrag: SchuelerListeEintrag) => {
		await RouteManager.doRoute(routeSchueler.getRoute({ id: eintrag.id }));
	};

	gotoLehrer = async (eintrag: LehrerListeEintrag) => {
		await RouteManager.doRoute(routeLehrer.getRoute({ id: eintrag.id }));
	};


}
