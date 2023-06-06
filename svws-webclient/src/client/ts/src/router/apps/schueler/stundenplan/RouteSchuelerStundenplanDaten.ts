import type { StundenplanListeEintrag } from "@core";
import { BenutzerKompetenz, SchuelerStundenplanManager, Schulform } from "@core";
import { shallowRef } from "vue";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { SchuelerStundenplanAuswahlProps } from "~/components/schueler/stundenplan/SSchuelerStundenplanAuswahlProps";
import type { SchuelerStundenplanDatenProps } from "~/components/schueler/stundenplan/SSchuelerStundenplanDatenProps";
import { api } from "~/router/Api";
import { routeSchueler } from "~/router/apps/RouteSchueler";
import type { RouteSchuelerStundenplan } from "~/router/apps/schueler/RouteSchuelerStundenplan";
import { routeApp } from "~/router/RouteApp";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

interface RouteStateSchuelerDataStundenplan {
	auswahl: StundenplanListeEintrag | undefined;
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	manager: SchuelerStundenplanManager | undefined;
}

export class RouteDataSchuelerStundenplan {

	private static _defaultState: RouteStateSchuelerDataStundenplan = {
		auswahl: undefined,
		mapStundenplaene: new Map(),
		manager: undefined,
	}

	private _state = shallowRef(RouteDataSchuelerStundenplan._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateSchuelerDataStundenplan>) {
		this._state.value = Object.assign({ ... RouteDataSchuelerStundenplan._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateSchuelerDataStundenplan>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	get auswahl(): StundenplanListeEintrag {
		if (this._state.value.auswahl === undefined)
			throw new Error("Unerwarteter Fehler: Stundenplaneintrag nicht festgelegt, es können keine Informationen zum Stundenplan abgerufen oder eingegeben werden.");
		return this._state.value.auswahl;
	}

	get manager(): SchuelerStundenplanManager | undefined {
		return this._state.value.manager;
	}

	get mapStundenplaene(): Map<number, StundenplanListeEintrag> {
		return this._state.value.mapStundenplaene;
	}

	public async ladeListe() {
		const listStundenplaene = await api.server.getStundenplanlisteFuerAbschnitt(api.schema, routeApp.data.aktAbschnitt.value.id);
		const mapStundenplaene = new Map<number, StundenplanListeEintrag>();
		const auswahl = listStundenplaene.size() > 0 ? listStundenplaene.get(0) : undefined;
		for (const l of listStundenplaene)
			mapStundenplaene.set(l.id, l);
		this.setPatchedDefaultState({ auswahl, mapStundenplaene });
	}

	public async setEintrag(idSchueler: number, idStundenplan?: number) {
		if (((idStundenplan === undefined) && (this.manager === undefined)) ||
		((this.manager !== undefined) && (this.manager.getSchuelerID() === idSchueler) && (this.manager.getStundenplanID() === idStundenplan)))
			return;
		const auswahl = this.mapStundenplaene.get(idStundenplan || -1);
		if (auswahl !== undefined) {
			const daten = await api.server.getSchuelerStundenplan(api.schema, auswahl.id, idSchueler);
			const manager = new SchuelerStundenplanManager(daten);
			this.setPatchedState({auswahl, manager});
		} else this.setPatchedState({auswahl: undefined, manager: undefined})
	}

	public gotoStundenplan = async (value: StundenplanListeEintrag | undefined) => {
		await RouteManager.doRoute({ name: routeSchuelerStundenplanDaten.name, params: { id: routeSchueler.data.stammdaten.id, idStundenplan: value?.id } });
	}

}

const SSchuelerStundenplanDaten = () => import("~/components/schueler/stundenplan/SSchuelerStundenplanDaten.vue");
const SSchuelerStundenplanAuswahl = () => import("~/components/schueler/stundenplan/SSchuelerStundenplanAuswahl.vue")

export class RouteSchuelerStundenplanDaten extends RouteNode<RouteDataSchuelerStundenplan, RouteSchuelerStundenplan> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler.stundenplan.daten", ":idStundenplan(\\d+)?", SSchuelerStundenplanDaten, new RouteDataSchuelerStundenplan());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
		super.setView("stundenplanauswahl", SSchuelerStundenplanAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
		];
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		await this.data.ladeListe();
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<any> {
		if (to_params.id instanceof Array || to_params.idStundenplan instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const idSchueler = to_params.id === undefined ? undefined : parseInt(to_params.id);
		if (idSchueler === undefined)
			return routeSchueler.getRoute();
		if (to_params.idStundenplan === undefined) {
			if (this.data.mapStundenplaene.size === 0)
				return false;
			return { name: this.name, params: { id: to_params.id, idStundenplan: this.data.auswahl.id }};
		}
		const idStundenplan = parseInt(to_params.idStundenplan);
		try {
			await this.data.setEintrag(idSchueler, idStundenplan);
		} catch (e) {
			console.log("Kein Stundenplan für diesen Schüler gefunden.")
			return routeSchueler.getRoute(idSchueler);
		}
	}

	public getRoute(id: number, idStundenplan: number) : RouteLocationRaw {
		return { name: this.name, params: { id, idStundenplan }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): SchuelerStundenplanAuswahlProps {
		return {
			stundenplan: this.data.auswahl,
			mapStundenplaene: this.data.mapStundenplaene,
			gotoStundenplan: this.data.gotoStundenplan,
		};
	}

	public getProps(to: RouteLocationNormalized): SchuelerStundenplanDatenProps {
		return {
			manager: this.data.manager,
		};
	}

}

export const routeSchuelerStundenplanDaten = new RouteSchuelerStundenplanDaten();

