import type { FaecherListeEintrag, LehrerListeEintrag, SchuelerLeistungsdaten, SchuelerLernabschnittListeEintrag, SchuelerLernabschnittsdaten } from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { shallowRef } from "vue";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { SchuelerLeistungenAuswahlProps } from "~/components/schueler/leistungsdaten/SSchuelerLeistungenAuswahlProps";
import type { SchuelerLeistungenDatenProps } from "~/components/schueler/leistungsdaten/SSchuelerLeistungenDatenProps";
import { api } from "~/router/Api";
import type { RouteSchuelerLeistungen } from "~/router/apps/schueler/RouteSchuelerLeistungen";
import { routeSchuelerLeistungen } from "~/router/apps/schueler/RouteSchuelerLeistungen";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

interface RouteStateSchuelerLeistungenDaten {
	auswahl: SchuelerLernabschnittListeEintrag | undefined;
	daten: SchuelerLernabschnittsdaten | undefined;
	mapFaecher: Map<number, FaecherListeEintrag>;
	mapLehrer: Map<number, LehrerListeEintrag>;
}

export class RouteDataSchuelerLeistungenDaten {

	private static _defaultState: RouteStateSchuelerLeistungenDaten = {
		auswahl: undefined,
		daten: undefined,
		mapFaecher: new Map(),
		mapLehrer: new Map(),
	}

	private _state = shallowRef(RouteDataSchuelerLeistungenDaten._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateSchuelerLeistungenDaten>) {
		this._state.value = Object.assign({ ... RouteDataSchuelerLeistungenDaten._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateSchuelerLeistungenDaten>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	get auswahl(): SchuelerLernabschnittListeEintrag {
		if (this._state.value.auswahl === undefined)
			throw new Error("Unerwarteter Fehler: Lernabschnittseintrag nicht festgelegt, es können keine Informationen zu den Leistungsdaten abgerufen oder eingegeben werden.");
		return this._state.value.auswahl;
	}

	get daten(): SchuelerLernabschnittsdaten {
		if (this._state.value.daten === undefined)
			throw new Error("Unerwarteter Fehler: Leistungsdaten nicht initialisiert");
		return this._state.value.daten;
	}
	get mapFaecher(): Map<number, FaecherListeEintrag> {
		return this._state.value.mapFaecher;
	}

	get mapLehrer(): Map<number, LehrerListeEintrag> {
		return this._state.value.mapLehrer;
	}

	public async ladeListe() {
		const listFaecher = await	api.server.getFaecher(api.schema);
		const mapFaecher = new Map<number, FaecherListeEintrag>();
		for (const f of listFaecher)
			mapFaecher.set(f.id, f);
		const listLehrer = await api.server.getLehrer(api.schema);
		const mapLehrer = new Map<number, LehrerListeEintrag>();
		for (const l of listLehrer)
			mapLehrer.set(l.id, l);
		this.setPatchedDefaultState({ mapFaecher, mapLehrer });
	}

	public async setEintrag(lernabschnitt: SchuelerLernabschnittListeEintrag | undefined) {
		if (lernabschnitt === undefined)
			return;
		const daten = await api.server.getSchuelerLernabschnittsdatenByID(api.schema, lernabschnitt.id);
		this.setPatchedState({ auswahl: lernabschnitt, daten });
	}

	gotoLernabschnitt = async (value: SchuelerLernabschnittListeEintrag | undefined) => {
		await RouteManager.doRoute({ name: routeSchuelerLeistungenDaten.name, params: { id: value?.schuelerID, abschnitt: value?.schuljahresabschnitt, wechselNr: value?.wechselNr || undefined } });
	}

	patchLeistung = async (data : Partial<SchuelerLeistungsdaten>, id : number) => {
		// TODO await api.server.patchSchuelerLeistungsdaten(data, api.schema, id);
	}

}

const SSchuelerLeistungenDaten = () => import("~/components/schueler/leistungsdaten/SSchuelerLeistungenDaten.vue");
const SSchuelerLeistungenAuswahl = () => import("~/components/schueler/leistungsdaten/SSchuelerLeistungenAuswahl.vue")

export class RouteSchuelerLeistungenDaten extends RouteNode<RouteDataSchuelerLeistungenDaten, RouteSchuelerLeistungen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler.leistungen.daten", ":abschnitt(\\d+)?/:wechselNr(\\d+)?", SSchuelerLeistungenDaten, new RouteDataSchuelerLeistungenDaten());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Leistungsdaten";
		super.setView("lernabschnittauswahl", SSchuelerLeistungenAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
		];
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		await this.data.ladeListe();
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array || to_params.abschnitt instanceof Array || to_params.wechselNr instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (to_params.id === undefined)
			return false;
		const id = parseInt(to_params.id);
		if (to_params.abschnitt === undefined) {
			return routeSchuelerLeistungen.getRoute(id);
		} else {
			const abschnitt = parseInt(to_params.abschnitt);
			const wechselNr = (to_params.wechselNr === undefined) || (to_params.wechselNr === "") ? null : parseInt(to_params.wechselNr);
			const eintrag = routeSchuelerLeistungen.data.getEntry(abschnitt, wechselNr);
			await this.data.setEintrag(eintrag);
		}
	}

	public getRoute(id: number, abschnitt: number | undefined, wechselNr: number | undefined) : RouteLocationRaw {
		return { name: this.name, params: { id: id, abschnitt: abschnitt, wechselNr: wechselNr }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): SchuelerLeistungenAuswahlProps {
		return {
			lernabschnitt: this.data.auswahl,
			lernabschnitte: routeSchuelerLeistungen.data.listAbschnitte,
			gotoLernabschnitt: this.data.gotoLernabschnitt
		};
	}

	public getProps(to: RouteLocationNormalized): SchuelerLeistungenDatenProps {
		return {
			data: this.data.daten,
			mapFaecher: this.data.mapFaecher,
			mapLehrer: this.data.mapLehrer,
			patchLeistung: this.data.patchLeistung
		};
	}

}

export const routeSchuelerLeistungenDaten = new RouteSchuelerLeistungenDaten();

