import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeSchuleFaecher } from "~/router/apps/schule/faecher/RouteSchuleFaecher";
import { routeFachStundenplan, type RouteFachStundenplan } from "~/router/apps/schule/faecher/stundenplan/RouteFachStundenplan";

import { StundenplanFach } from "@comp";
import type { StundenplanFachProps } from "@comp";
import { routeError } from "~/router/error/RouteError";

export class RouteFachStundenplanDaten extends RouteNode<any, RouteFachStundenplan> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN ], "schule.faecher.stundenplan.daten", ":idStundenplan(\\d+)?/:wochentyp(\\d+)?/:kw(\\d+\\.\\d+)?", StundenplanFach);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
		super.children = [];
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { id: idFach, wochentyp, idStundenplan } = RouteNode.getIntParams(to_params, ["id", "wochentyp", "idStundenplan"]);
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
			// Prüfe, ob ein Lehrer ausgewählt ist. Wenn nicht dann wechsele in die Fächer-Route zurück.
			if (idFach === undefined)
				return routeSchuleFaecher.getRoute();
				// Prüfe, ob die Stundenplan-ID definiert ist, wenn nicht, dann versuche einen zu laden
			if (idStundenplan === undefined) {
				if (routeFachStundenplan.data.mapStundenplaene.size === 0)
					throw new DeveloperNotificationException("Fehler: Kein Stundenplan für die angegebene ID gefunden.");
				return this.getRoute();
			}
			// Lade den Stundenplan ...
			await routeFachStundenplan.data.setEintrag(idFach, idStundenplan, wochentyp ?? 0, kwjahr, kw);
		} catch (error) {
			return routeError.getErrorRoute(error as DeveloperNotificationException);
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		await routeFachStundenplan.data.setEintrag(-1, undefined, 0, undefined, undefined);
	}

	public getProps(to: RouteLocationNormalized): StundenplanFachProps {
		return {
			modePausenaufsichten: 'aus',
			id: routeSchuleFaecher.data.fachListeManager.daten().id,
			ignoreEmpty: routeFachStundenplan.data.ganzerStundenplan,
			manager: () => routeFachStundenplan.data.manager,
			wochentyp: () => routeFachStundenplan.data.wochentyp,
			kalenderwoche: () => routeFachStundenplan.data.kalenderwoche,
		};
	}

}

export const routeFachStundenplanDaten = new RouteFachStundenplanDaten();

