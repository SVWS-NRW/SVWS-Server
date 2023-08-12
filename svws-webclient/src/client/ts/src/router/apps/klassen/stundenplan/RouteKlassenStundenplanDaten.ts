import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeKlassen } from "~/router/apps/klassen/RouteKlassen";
import { routeKlassenStundenplan, type RouteKlassenStundenplan } from "~/router/apps/klassen/stundenplan/RouteKlassenStundenplan";

import { StundenplanAnsichtPausenzeiten } from "@comp";
import { type StundenplanAnsichtProps } from "@comp";

export class RouteKlassenStundenplanDaten extends RouteNode<unknown, RouteKlassenStundenplan> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "klassen.stundenplan.daten", ":idStundenplan(\\d+)?/:wochentyp(\\d+)?/:kw(\\d+\\.\\d+)?", StundenplanAnsichtPausenzeiten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
		super.children = [
		];
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array || to_params.idStundenplan instanceof Array
				|| to_params.wochentyp instanceof Array || to_params.kw instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const idKlasse = to_params.id === undefined ? undefined : parseInt(to_params.id);
		let wochentyp = (to_params.wochentyp === undefined) || (to_params.wochentyp === "") ? undefined : parseInt(to_params.wochentyp);
		let kwjahr = undefined;
		let kw = undefined;
		if (wochentyp === undefined) {
			wochentyp = 0;
		} else if ((to_params.kw !== undefined) && (to_params.kw !== "")) {
			const tmpKW = to_params.kw.split(".");
			if (tmpKW.length !== 2)
				throw new Error("Die Angabe der Kalenderwoche muss die Form 'Jahr.KW' haben.");
			kwjahr = parseInt(tmpKW[0]);
			kw = parseInt(tmpKW[1]);
		}
		// Prüfe, ob eine Klasse ausgewählt ist. Wenn nicht dann wechsele in die Klassen-Route zurück.
		if (idKlasse === undefined)
			return routeKlassen.getRoute(undefined);
			// Prüfe, ob die Stundenplan-ID definiert ist, wenn nicht, dann versuche einen zu laden
		if (to_params.idStundenplan === undefined) {
			if (routeKlassenStundenplan.data.mapStundenplaene.size === 0)
				throw new Error("Fehler: Kein Stundenplan für die angegebene ID gefunden.");
			return this.getRoute(idKlasse, routeKlassenStundenplan.data.auswahl.id,
				routeKlassenStundenplan.data.wochentyp, routeKlassenStundenplan.data.kalenderwoche?.jahr,
				routeKlassenStundenplan.data.kalenderwoche?.kw);
		}
		// Lade den Stundenplan ...
		const idStundenplan = parseInt(to_params.idStundenplan);
		await routeKlassenStundenplan.data.setEintrag(idKlasse, idStundenplan, wochentyp, kwjahr, kw);
	}

	public async leave(from: RouteNode<unknown, any>, from_params: RouteParams): Promise<void> {
		await routeKlassenStundenplan.data.setEintrag(-1, undefined, 0, undefined, undefined);
	}

	public getRoute(id: number, idStundenplan: number | undefined, wochentyp: number, kwjahr?: number | undefined, kw?: number | undefined) : RouteLocationRaw {
		if (idStundenplan === undefined) {
			if (routeKlassenStundenplan.data.hatAuswahl)
				return { name: this.name, params: { id, idStundenplan: routeKlassenStundenplan.data.auswahl.id }};
			return { name: routeKlassenStundenplan.name, params : { id } };
		}
		const tmpKW = ((kwjahr === undefined) || (kw === undefined)) ? undefined : kwjahr + "." + kw;
		return { name: this.name, params: { id, idStundenplan, wochentyp, kw: tmpKW }};
	}

	public getProps(to: RouteLocationNormalized): StundenplanAnsichtProps {
		return {
			mode: 'klasse',
			manager: () => routeKlassenStundenplan.data.manager,
			wochentyp: () => routeKlassenStundenplan.data.wochentyp,
			kalenderwoche: () => routeKlassenStundenplan.data.kalenderwoche,
		};
	}

}

export const routeKlassenStundenplanDaten = new RouteKlassenStundenplanDaten();

