import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeKlassen } from "~/router/apps/klassen/RouteKlassen";
import { routeKlassenStundenplan, type RouteKlassenStundenplan } from "~/router/apps/klassen/stundenplan/RouteKlassenStundenplan";

import { StundenplanKlasse } from "@comp";
import { type StundenplanKlasseProps } from "@comp";
import { routeApp } from "../../RouteApp";
import { routeError } from "~/router/error/RouteError";

export class RouteKlassenStundenplanDaten extends RouteNode<any, RouteKlassenStundenplan> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN ], "klassen.stundenplan.daten", ":idStundenplan(\\d+)?/:wochentyp(\\d+)?/:kw(\\d+\\.\\d+)?", StundenplanKlasse);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
		super.children = [
		];
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { id, wochentyp, idStundenplan } = RouteNode.getIntParams(to_params, ["id", "wochentyp", "idStundenplan"]);
			const { kw: kwString } = RouteNode.getStringParams(to_params, ["kw"]);
			let kwjahr = undefined;
			let kw = undefined;
			if ((kwString !== "") && (kwString !== undefined) && (wochentyp !== undefined)) {
				const tmpKW = kwString.split(".");
				if (tmpKW.length !== 2)
					throw new DeveloperNotificationException("Die Angabe der Kalenderwoche muss die Form 'Jahr.KW' haben.");
				kwjahr = parseInt(tmpKW[0]);
				kw = parseInt(tmpKW[1]);
			}
			// Prüfe, ob eine Klasse ausgewählt ist. Wenn nicht dann wechsele in die Klassen-Route zurück.
			if (id === undefined)
				return routeKlassen.getRoute(undefined);
			// Prüfe, ob die Stundenplan-ID definiert ist, wenn nicht, dann versuche einen zu laden
			if (idStundenplan === undefined) {
				if (routeKlassenStundenplan.data.mapStundenplaene.size === 0)
					throw new DeveloperNotificationException("Fehler: Kein Stundenplan für die angegebene ID gefunden.");
				return this.getRoute(id, routeKlassenStundenplan.data.auswahl.id,
					routeKlassenStundenplan.data.wochentyp, routeKlassenStundenplan.data.kalenderwoche?.jahr,
					routeKlassenStundenplan.data.kalenderwoche?.kw);
			}
			// Lade den Stundenplan ...
			await routeKlassenStundenplan.data.setEintrag(id, idStundenplan, wochentyp ?? 0, kwjahr, kw);
		} catch (e) {
			return routeError.getRoute(e as DeveloperNotificationException);
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		await routeKlassenStundenplan.data.setEintrag(-1, undefined, 0, undefined, undefined);
	}

	public getRoute(id: number, idStundenplan: number | undefined, wochentyp: number, kwjahr?: number, kw?: number) : RouteLocationRaw {
		if (idStundenplan === undefined) {
			if (routeKlassenStundenplan.data.hatAuswahl)
				return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id, idStundenplan: routeKlassenStundenplan.data.auswahl.id }};
			return { name: routeKlassenStundenplan.name, params : { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id } };
		}
		const tmpKW = ((kwjahr === undefined) || (kw === undefined)) ? undefined : kwjahr + "." + kw;
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id, idStundenplan, wochentyp, kw: tmpKW }};
	}

	public getProps(to: RouteLocationNormalized): StundenplanKlasseProps {
		return {
			id: routeKlassen.data.klassenListeManager.daten().id,
			ignoreEmpty: routeKlassenStundenplan.data.ganzerStundenplan,
			manager: () => routeKlassenStundenplan.data.manager,
			wochentyp: () => routeKlassenStundenplan.data.wochentyp,
			kalenderwoche: () => routeKlassenStundenplan.data.kalenderwoche,
		};
	}

}

export const routeKlassenStundenplanDaten = new RouteKlassenStundenplanDaten();

