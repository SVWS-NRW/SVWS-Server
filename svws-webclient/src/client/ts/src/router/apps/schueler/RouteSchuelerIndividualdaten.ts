import type { FoerderschwerpunktEintrag, KatalogEintrag, ReligionEintrag } from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { SchuelerIndividualdatenProps } from "~/components/schueler/individualdaten/SSchuelerIndividualdatenProps";
import { api } from "~/router/Api";
import { routeApp } from "~/router/RouteApp";
import { RouteNode } from "~/router/RouteNode";
import type { RouteSchueler} from "../RouteSchueler";
import { routeSchueler } from "../RouteSchueler";
import { shallowRef } from "vue";

const SSchuelerIndividualdaten = () => import("~/components/schueler/individualdaten/SSchuelerIndividualdaten.vue");

interface RouteStateDataSchuelerIndividualdaten {
	mapFahrschuelerarten: Map<number, KatalogEintrag>;
	mapFoerderschwerpunkte: Map<number, FoerderschwerpunktEintrag>;
	mapHaltestellen: Map<number, KatalogEintrag>;
	mapReligionen: Map<number, ReligionEintrag>;
}
export class RouteDataSchuelerIndividualdaten {

	private static _defaultState: RouteStateDataSchuelerIndividualdaten = {
		mapFahrschuelerarten: new Map(),
		mapFoerderschwerpunkte: new Map(),
		mapHaltestellen: new Map(),
		mapReligionen: new Map(),
	}

	private _state = shallowRef(RouteDataSchuelerIndividualdaten._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateDataSchuelerIndividualdaten>) {
		this._state.value = Object.assign({ ... RouteDataSchuelerIndividualdaten._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateDataSchuelerIndividualdaten>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	get mapFahrschuelerarten(): Map<number, KatalogEintrag> {
		return this._state.value.mapFahrschuelerarten;
	}

	get mapFoerderschwerpunkte(): Map<number, FoerderschwerpunktEintrag> {
		return this._state.value.mapFoerderschwerpunkte;
	}

	get mapHaltestellen(): Map<number, KatalogEintrag> {
		return this._state.value.mapHaltestellen;
	}

	get mapReligionen(): Map<number, ReligionEintrag> {
		return this._state.value.mapReligionen;
	}

	public async ladeListe() {
		// Lade den Katalog der Fahrschülerarten
		const fahrschuelerarten = await api.server.getSchuelerFahrschuelerarten(api.schema)
		const mapFahrschuelerarten = new Map();
		for (const fa of fahrschuelerarten)
			mapFahrschuelerarten.set(fa.id, fa);
		// Lade den Katalog der Förderschwerpunkte
		const foerderschwerpunkte = await api.server.getSchuelerFoerderschwerpunkte(api.schema);
		const mapFoerderschwerpunkte = new Map();
		for (const fs of foerderschwerpunkte)
			mapFoerderschwerpunkte.set(fs.id, fs);
		// Lade den Katalog der Haltestellen
		const haltestellen = await api.server.getHaltestellen(api.schema);
		const mapHaltestellen = new Map();
		for (const h of haltestellen)
			mapHaltestellen.set(h.id, h);
		// Lade den Katalog der Religionen
		const religionen = await api.server.getReligionen(api.schema)
		const mapReligionen = new Map();
		for (const r of religionen)
			mapReligionen.set(r.id, r);
		this.setPatchedDefaultState({ mapFahrschuelerarten, mapFoerderschwerpunkte, mapHaltestellen, mapReligionen })
	}

}

export class RouteSchuelerIndividualdaten extends RouteNode<RouteDataSchuelerIndividualdaten, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler.daten", "daten", SSchuelerIndividualdaten, new RouteDataSchuelerIndividualdaten());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Individualdaten";
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		await this.data.ladeListe();
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): SchuelerIndividualdatenProps {
		return {
			patch: routeSchueler.data.patch,
			data: ()=> routeSchueler.data.stammdaten,
			mapOrte: routeApp.data.mapOrte,
			mapOrtsteile: routeApp.data.mapOrtsteile,
			mapFahrschuelerarten: this.data.mapFahrschuelerarten,
			mapFoerderschwerpunkte: this.data.mapFoerderschwerpunkte,
			mapHaltestellen: this.data.mapHaltestellen,
			mapReligionen: this.data.mapReligionen
		};
	}

}

export const routeSchuelerIndividualdaten = new RouteSchuelerIndividualdaten();

