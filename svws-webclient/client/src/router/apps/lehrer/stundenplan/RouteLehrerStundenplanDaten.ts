import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { routeLehrerStundenplan, type RouteLehrerStundenplan } from "~/router/apps/lehrer/stundenplan/RouteLehrerStundenplan";

import { StundenplanLehrer } from "@ui";
import type { StundenplanLehrerProps } from "@ui";
import { routeError } from "~/router/error/RouteError";

export class RouteLehrerStundenplanDaten extends RouteNode<any, RouteLehrerStundenplan> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN ], "lehrer.stundenplan.daten", ":idStundenplan(\\d+)?/:wochentyp(\\d+)?/:kw(\\d+\\.\\d+)?", StundenplanLehrer);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
		super.children = [
		];
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { id: idLehrer, wochentyp, idStundenplan } = RouteNode.getIntParams(to_params, ["id", "wochentyp", "idStundenplan"]);
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
			// Prüfe, ob ein Lehrer ausgewählt ist. Wenn nicht dann wechsele in die Lehrer-Route zurück.
			if (idLehrer === undefined)
				return routeLehrer.getRoute();
			// Prüfe, ob die Stundenplan-ID definiert ist, wenn nicht, dann versuche einen zu laden
			if (idStundenplan === undefined) {
				if (routeLehrerStundenplan.data.mapStundenplaene.size === 0)
					throw new DeveloperNotificationException("Fehler: Kein Stundenplan für die angegebene ID gefunden.");
				return this.getRoute();
			}
			// Lade den Stundenplan ...
			await routeLehrerStundenplan.data.setEintrag(idLehrer, idStundenplan, wochentyp ?? 0, kwjahr, kw);
		} catch (e) {
			return routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		await routeLehrerStundenplan.data.setEintrag(-1, undefined, 0, undefined, undefined);
	}

	public getProps(to: RouteLocationNormalized): StundenplanLehrerProps {
		return {
			id: routeLehrer.data.manager.daten().id,
			ignoreEmpty: routeLehrerStundenplan.data.ganzerStundenplan,
			manager: () => routeLehrerStundenplan.data.manager,
			wochentyp: () => routeLehrerStundenplan.data.wochentyp,
			kalenderwoche: () => routeLehrerStundenplan.data.kalenderwoche,
		};
	}

}

export const routeLehrerStundenplanDaten = new RouteLehrerStundenplanDaten();

