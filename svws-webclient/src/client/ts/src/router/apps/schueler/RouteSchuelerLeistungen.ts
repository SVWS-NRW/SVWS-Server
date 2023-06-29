import { shallowRef } from "vue";
import type { RouteLocationRaw, RouteParams } from "vue-router";

import type { List, SchuelerLernabschnittListeEintrag} from "@core";
import { BenutzerKompetenz, Schulform, ArrayList, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";

import { routeApp } from "~/router/apps/RouteApp";
import type { RouteSchueler } from "~/router/apps/RouteSchueler";
import { routeSchuelerLeistungenDaten } from "~/router/apps/schueler/leistungsdaten/RouteSchuelerLeistungenDaten";

interface RouteStateDataSchuelerLeistungen {
	idSchueler: number | undefined;
	listAbschnitte: List<SchuelerLernabschnittListeEintrag>;
}

export class RouteDataSchuelerLeistungen {

	private static _defaultState: RouteStateDataSchuelerLeistungen = {
		idSchueler: undefined,
		listAbschnitte: new ArrayList<SchuelerLernabschnittListeEintrag>(),
	}

	private _state = shallowRef(RouteDataSchuelerLeistungen._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateDataSchuelerLeistungen>) {
		this._state.value = Object.assign({ ... RouteDataSchuelerLeistungen._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateDataSchuelerLeistungen>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	get idSchueler(): number | undefined {
		return this._state.value.idSchueler;
	}

	get listAbschnitte(): List<SchuelerLernabschnittListeEintrag> {
		return this._state.value.listAbschnitte;
	}

	public async ladeListe(idSchueler: number) {
		const listAbschnitte = await api.server.getSchuelerLernabschnittsliste(api.schema, idSchueler);
		this.setPatchedState({ idSchueler, listAbschnitte })
	}

	public getEntry(idSchuljahresabschnitt: number, wechselNr: number | null) : SchuelerLernabschnittListeEintrag | undefined {
		for (const current of this.listAbschnitte)
			if ((current.schuljahresabschnitt === idSchuljahresabschnitt) && (current.wechselNr === wechselNr))
				return current;
		return undefined;
	}

	public getEntryDefault() : SchuelerLernabschnittListeEintrag | undefined {
		const entry = this.getEntry(routeApp.data.aktAbschnitt.value.id, null);
		if (entry !== undefined)
			return entry;
		if (this.listAbschnitte.size() > 0)
			return this.listAbschnitte.get(0);
		return undefined;
	}

}

const SSchuelerLeistungen = () => import("~/components/schueler/leistungsdaten/SSchuelerLeistungen.vue");

export class RouteSchuelerLeistungen extends RouteNode<RouteDataSchuelerLeistungen, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler.leistungen", "leistungsdaten", SSchuelerLeistungen, new RouteDataSchuelerLeistungen());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getNoProps(route);
		super.text = "Leistungsdaten";
		super.children = [
			routeSchuelerLeistungenDaten
		];
		super.defaultChild = routeSchuelerLeistungenDaten;
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array || to_params.abschnitt instanceof Array || to_params.wechselNr instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (to_params.id === undefined)
			return false;
		const id = parseInt(to_params.id);
		if ((this.data.idSchueler !== id) || (to_params.abschnitt === undefined)) {
			await this.data.ladeListe(id);
			if (this.data.listAbschnitte.size()<=0)
				return false;
			if (to_params.abschnitt !== undefined) {
				const abschnitt = parseInt(to_params.abschnitt);
				const wechselNr = (to_params.wechselNr === undefined) || (to_params.wechselNr === "") ? null : parseInt(to_params.wechselNr);
				const lernabschnitt = this.data.getEntry(abschnitt, wechselNr);
				if (lernabschnitt !== undefined)
					return routeSchuelerLeistungenDaten.getRoute(id, lernabschnitt.schuljahresabschnitt, lernabschnitt.wechselNr === null ? undefined : lernabschnitt.wechselNr);
				if (wechselNr !== null) {
					const lernabschnitt = this.data.getEntry(abschnitt, null);
					if (lernabschnitt !== undefined)
						return routeSchuelerLeistungenDaten.getRoute(id, lernabschnitt.schuljahresabschnitt, lernabschnitt.wechselNr === null ? undefined : lernabschnitt.wechselNr);
				}
			}
			const lernabschnitt = this.data.getEntryDefault();
			if (lernabschnitt === undefined)
				return false;
			return routeSchuelerLeistungenDaten.getRoute(id, lernabschnitt.schuljahresabschnitt, lernabschnitt.wechselNr === null ? undefined : lernabschnitt.wechselNr);
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

}

export const routeSchuelerLeistungen = new RouteSchuelerLeistungen();

