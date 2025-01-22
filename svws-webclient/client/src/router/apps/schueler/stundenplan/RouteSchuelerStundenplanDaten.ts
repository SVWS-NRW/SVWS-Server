import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeSchuelerStundenplan, type RouteSchuelerStundenplan } from "~/router/apps/schueler/stundenplan/RouteSchuelerStundenplan";

import { StundenplanSchueler } from "@ui";
import type { StundenplanSchuelerProps } from "@ui";
import { routeApp } from "../../RouteApp";
import { routeError } from "~/router/error/RouteError";

export class RouteSchuelerStundenplanDaten extends RouteNode<any, RouteSchuelerStundenplan> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN ], "schueler.stundenplan.daten", ":idStundenplan(\\d+)?/:wochentyp(\\d+)?/:kw(\\d+\\.\\d+)?", StundenplanSchueler);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
		super.children = [
		];
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { id: idSchueler, idStundenplan, wochentyp } = RouteNode.getIntParams(to_params, ["id", "idStundenplan", "wochentyp"]);
			const { kw: kwString } = RouteNode.getStringParams(to_params, ["kw"]);
			let kwjahr = undefined;
			let kw = undefined;
			if ((kwString !== undefined) && (kwString !== "") && (wochentyp === undefined)) {
				const tmpKW = kwString.split(".");
				if (tmpKW.length !== 2)
					throw new DeveloperNotificationException("Die Angabe der Kalenderwoche muss die Form 'Jahr.KW' haben.");
				kwjahr = parseInt(tmpKW[0]);
				kw = parseInt(tmpKW[1]);
			}
			// Prüfe, ob ein schüler ausgewählt ist. Wenn nicht dann wechsele in die Schüler-Route zurück.
			if (idSchueler === undefined)
				return routeSchueler.getRoute();
			// Prüfe, ob die Stundenplan-ID definiert ist, wenn nicht, dann versuche einen zu laden
			if (idStundenplan === undefined) {
				if (routeSchuelerStundenplan.data.mapStundenplaene.size === 0)
					throw new DeveloperNotificationException("Fehler: Kein Stundenplan für die angegebene ID gefunden.");
				return this.getRoute();
			}
			// Lade den Stundenplan ...
			await routeSchuelerStundenplan.data.setEintrag(idSchueler, idStundenplan, wochentyp ?? 0, kwjahr, kw);
		} catch (e) {
			return routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		await routeSchuelerStundenplan.data.setEintrag(-1, undefined, 0, undefined, undefined);
	}

	public getProps(to: RouteLocationNormalized): StundenplanSchuelerProps {
		return {
			id: routeSchueler.data.schuelerListeManager.daten().id,
			ignoreEmpty: routeSchuelerStundenplan.data.ganzerStundenplan,
			manager: () => routeSchuelerStundenplan.data.manager,
			wochentyp: () => routeSchuelerStundenplan.data.wochentyp,
			kalenderwoche: () => routeSchuelerStundenplan.data.kalenderwoche,
		};
	}

}

export const routeSchuelerStundenplanDaten = new RouteSchuelerStundenplanDaten();

