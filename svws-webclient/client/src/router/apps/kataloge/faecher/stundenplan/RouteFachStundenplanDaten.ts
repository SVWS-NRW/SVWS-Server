import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeKatalogFaecher } from "~/router/apps/kataloge/faecher/RouteKatalogFaecher";
import { routeFachStundenplan, type RouteFachStundenplan } from "~/router/apps/kataloge/faecher/stundenplan/RouteFachStundenplan";

import { StundenplanAnsichtFach } from "@comp";
import type { StundenplanAnsichtFachProps } from "@comp";
import { routeApp } from "~/router/apps/RouteApp";

export class RouteFachStundenplanDaten extends RouteNode<any, RouteFachStundenplan> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.faecher.stundenplan.daten", ":idStundenplan(\\d+)?/:wochentyp(\\d+)?/:kw(\\d+\\.\\d+)?", StundenplanAnsichtFach);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
		super.children = [];
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array || to_params.idStundenplan instanceof Array
				|| to_params.wochentyp instanceof Array || to_params.kw instanceof Array)
			throw new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const idFach = to_params.id === undefined ? undefined : parseInt(to_params.id);
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
		// Prüfe, ob ein Lehrer ausgewählt ist. Wenn nicht dann wechsele in die Lehrer-Route zurück.
		if (idFach === undefined)
			return routeKatalogFaecher.getRoute(undefined);
			// Prüfe, ob die Stundenplan-ID definiert ist, wenn nicht, dann versuche einen zu laden
		if (to_params.idStundenplan === undefined) {
			if (routeFachStundenplan.data.mapStundenplaene.size === 0)
				throw new DeveloperNotificationException("Fehler: Kein Stundenplan für die angegebene ID gefunden.");
			return this.getRoute(idFach, routeFachStundenplan.data.auswahl.id,
				routeFachStundenplan.data.wochentyp, routeFachStundenplan.data.kalenderwoche?.jahr,
				routeFachStundenplan.data.kalenderwoche?.kw);
		}
		// Lade den Stundenplan ...
		const idStundenplan = parseInt(to_params.idStundenplan);
		await routeFachStundenplan.data.setEintrag(idFach, idStundenplan, wochentyp, kwjahr, kw);
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		await routeFachStundenplan.data.setEintrag(-1, undefined, 0, undefined, undefined);
	}

	public getRoute(id: number, idStundenplan: number, wochentyp: number, kwjahr?: number | undefined, kw?: number | undefined) : RouteLocationRaw {
		const tmpKW = ((kwjahr === undefined) || (kw === undefined)) ? undefined : kwjahr + "." + kw;
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id, idStundenplan, wochentyp, kw: tmpKW }};
	}

	public getProps(to: RouteLocationNormalized): StundenplanAnsichtFachProps {
		return {
			modePausenaufsichten: 'aus',
			id: routeKatalogFaecher.data.fachListeManager.daten().id,
			ignoreEmpty: routeFachStundenplan.data.ganzerStundenplan,
			manager: () => routeFachStundenplan.data.manager,
			wochentyp: () => routeFachStundenplan.data.wochentyp,
			kalenderwoche: () => routeFachStundenplan.data.kalenderwoche,
		};
	}

}

export const routeFachStundenplanDaten = new RouteFachStundenplanDaten();

