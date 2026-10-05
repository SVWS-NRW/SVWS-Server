import type { SchulwechselAbgang } from "@core/core/data/schule/SchulwechselAbgang";
import { ArrayList } from "@core/java/util/ArrayList";
import { HashMap } from "@core/java/util/HashMap";
import type { JavaMap } from "@core/java/util/JavaMap";
import type { List } from "@core/java/util/List";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";


interface RouteStateSchulwechselAbgang extends RouteStateInterface {
	abgaengeAuswahl: JavaMap<number, SchulwechselAbgang>;
	abgaenge: List<SchulwechselAbgang>;
}

const defaultState = {
	abgaengeAuswahl: new HashMap<number, SchulwechselAbgang>(),
	abgaenge: new ArrayList<SchulwechselAbgang>(),
};

export class RouteDataSchulwechselAbgaenge extends RouteData<RouteStateSchulwechselAbgang> {

	public constructor() {
		super(defaultState);
	}

	public async ladeDaten() {
		const abgaenge: List<SchulwechselAbgang> = await api.server.getSchulwechselAbgaenge(api.schema);
		this.setPatchedState({ abgaenge });
	}

	get abgaengeAuswahl(): JavaMap<number, SchulwechselAbgang> {
		return this._state.value.abgaengeAuswahl;
	}

	get abgaenge(): List<SchulwechselAbgang> {
		return this._state.value.abgaenge;
	}
}
