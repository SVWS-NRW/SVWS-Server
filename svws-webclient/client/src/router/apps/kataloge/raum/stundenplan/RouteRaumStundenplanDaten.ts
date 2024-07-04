import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeKatalogRaeume } from "~/router/apps/kataloge/raum/RouteKatalogRaeume";
import { routeRaumStundenplan, type RouteRaumStundenplan } from "~/router/apps/kataloge/raum/stundenplan/RouteRaumStundenplan";

import { StundenplanAnsicht } from "@comp";
import type { StundenplanAnsichtProps } from "@comp";
import { routeApp } from "~/router/apps/RouteApp";

export class RouteRaumStundenplanDaten extends RouteNode<any, RouteRaumStundenplan> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.raeume.stundenplan.daten", ":idStundenplan(\\d+)?/:wochentyp(\\d+)?/:kw(\\d+\\.\\d+)?", StundenplanAnsicht);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
		super.children = [];
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array || to_params.idStundenplan instanceof Array
				|| to_params.wochentyp instanceof Array || to_params.kw instanceof Array)
			throw new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const idRaum = to_params.id === undefined ? undefined : parseInt(to_params.id);
		let wochentyp = (to_params.wochentyp === undefined) || (to_params.wochentyp === "") ? undefined : parseInt(to_params.wochentyp);
		let kwjahr = undefined;
		let kw = undefined;
		if (wochentyp === undefined) {
			wochentyp = 0;
		} else if ((to_params.kw !== undefined) && (to_params.kw !== "")) {
			const tmpKW = to_params.kw.split(".");
			if (tmpKW.length !== 2)
				throw new DeveloperNotificationException("Die Angabe der Kalenderwoche muss die Form 'Jahr.KW' haben.");
			kwjahr = parseInt(tmpKW[0]);
			kw = parseInt(tmpKW[1]);
		}
		// Prüfe, ob ein Raum ausgewählt ist. Wenn nicht dann wechsele in die Raum-Route zurück.
		if (idRaum === undefined)
			return routeKatalogRaeume.getRoute(undefined);
			// Prüfe, ob die Stundenplan-ID definiert ist, wenn nicht, dann versuche einen zu laden
		if (to_params.idStundenplan === undefined) {
			if (routeRaumStundenplan.data.mapStundenplaene.size === 0)
				throw new DeveloperNotificationException("Fehler: Kein Stundenplan für die angegebene ID gefunden.");
			return this.getRoute(idRaum, routeRaumStundenplan.data.auswahl.id,
				routeRaumStundenplan.data.wochentyp, routeRaumStundenplan.data.kalenderwoche?.jahr,
				routeRaumStundenplan.data.kalenderwoche?.kw);
		}
		// Lade den Stundenplan ...
		const idStundenplan = parseInt(to_params.idStundenplan);
		await routeRaumStundenplan.data.setEintrag(idRaum, idStundenplan, wochentyp, kwjahr, kw);
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		await routeRaumStundenplan.data.setEintrag(-1, undefined, 0, undefined, undefined);
	}

	public getRoute(id: number, idStundenplan: number, wochentyp: number, kwjahr?: number | undefined, kw?: number | undefined) : RouteLocationRaw {
		const tmpKW = ((kwjahr === undefined) || (kw === undefined)) ? undefined : kwjahr + "." + kw;
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id, idStundenplan, wochentyp, kw: tmpKW }};
	}

	public getProps(to: RouteLocationNormalized): StundenplanAnsichtProps {
		return {
			mode: 'fach',
			modePausenaufsichten: 'aus',
			id: routeRaumStundenplan.data.idRaumStundenplan,
			ignoreEmpty: routeRaumStundenplan.data.ganzerStundenplan,
			manager: () => routeRaumStundenplan.data.manager,
			wochentyp: () => routeRaumStundenplan.data.wochentyp,
			kalenderwoche: () => routeRaumStundenplan.data.kalenderwoche,
		};
	}

}

export const routeRaumStundenplanDaten = new RouteRaumStundenplanDaten();

