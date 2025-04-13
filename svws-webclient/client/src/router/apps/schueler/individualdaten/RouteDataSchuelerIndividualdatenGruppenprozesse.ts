import type { FoerderschwerpunktEintrag, KatalogEintrag, ReligionEintrag, SchulEintrag } from "@core";

import { RouteData, type RouteStateInterface } from "~/router/RouteData";


interface RouteStateDataSchuelerIndividualdaten extends RouteStateInterface {
	mapFahrschuelerarten: Map<number, KatalogEintrag>;
	mapFoerderschwerpunkte: Map<number, FoerderschwerpunktEintrag>;
	mapHaltestellen: Map<number, KatalogEintrag>;
	mapReligionen: Map<number, ReligionEintrag>;
	mapSchulen: Map<string, SchulEintrag>;
}

const defaultState = <RouteStateDataSchuelerIndividualdaten> {
	mapFahrschuelerarten: new Map(),
	mapFoerderschwerpunkte: new Map(),
	mapHaltestellen: new Map(),
	mapReligionen: new Map(),
	mapSchulen: new Map<string, SchulEintrag>(),
};


export class RouteDataSchuelerIndividualdatenGruppenprozesse extends RouteData<RouteStateDataSchuelerIndividualdaten> {

	public constructor() {
		super(defaultState);
	}


}

