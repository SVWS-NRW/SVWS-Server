import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeSchuelerStundenplan, type RouteSchuelerStundenplan } from "~/router/apps/schueler/stundenplan/RouteSchuelerStundenplan";

import type { SchuelerStundenplanDatenProps } from "~/components/schueler/stundenplan/SSchuelerStundenplanDatenProps";


const SSchuelerStundenplanDaten = () => import("~/components/schueler/stundenplan/SSchuelerStundenplanDaten.vue");

export class RouteSchuelerStundenplanDaten extends RouteNode<unknown, RouteSchuelerStundenplan> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler.stundenplan.daten", ":idStundenplan(\\d+)?/:wochentyp(\\d+)?/:kw(\\d+\\.\\d+)?", SSchuelerStundenplanDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
		super.children = [
		];
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		try {
			if (to_params.id instanceof Array || to_params.idStundenplan instanceof Array
				|| to_params.wochentyp instanceof Array || to_params.kw instanceof Array)
				throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
			const idSchueler = to_params.id === undefined ? undefined : parseInt(to_params.id);
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
			// Prüfe, ob ein schüler ausgewählt ist. Wenn nicht dann wechsele in die Schüler-Route zurück.
			if (idSchueler === undefined)
				return routeSchueler.getRoute();
			// Prüfe, ob die Stundenplan-ID definiert ist, wenn nicht, dann versuche einen zu laden
			if (to_params.idStundenplan === undefined) {
				if (routeSchuelerStundenplan.data.mapStundenplaene.size === 0)
					return routeError.getRoute(new Error("Fehler: Kein Stundenplan für die angegebene ID gefunden."));
				return this.getRoute(idSchueler, routeSchuelerStundenplan.data.auswahl.id,
					routeSchuelerStundenplan.data.wochentyp, routeSchuelerStundenplan.data.kalenderwoche?.jahr,
					routeSchuelerStundenplan.data.kalenderwoche?.kw);
			}
			// Lade den Stundenplan ...
			const idStundenplan = parseInt(to_params.idStundenplan);
			await routeSchuelerStundenplan.data.setEintrag(idSchueler, idStundenplan, wochentyp, kwjahr, kw);
			// TODO Prüfe, ob Änderungen stattgefunden haben und passe die Route ggf. an (return ...)
		} catch (e) {
			return routeError.getRoute(e instanceof Error ? e : new Error("Fehler: Kein Stundenplan für die angegebene ID gefunden."));
		}
	}

	public async leave(from: RouteNode<unknown, any>, from_params: RouteParams): Promise<void> {
		await routeSchuelerStundenplan.data.setEintrag(-1, undefined, 0, undefined, undefined);
	}

	public getRoute(id: number, idStundenplan: number, wochentyp: number, kwjahr?: number | undefined, kw?: number | undefined) : RouteLocationRaw {
		const tmpKW = ((kwjahr === undefined) || (kw === undefined)) ? undefined : kwjahr + "." + kw;
		return { name: this.name, params: { id, idStundenplan, wochentyp, kw: tmpKW }};
	}

	public getProps(to: RouteLocationNormalized): SchuelerStundenplanDatenProps {
		return {
			manager: () => routeSchuelerStundenplan.data.manager,
		};
	}

}

export const routeSchuelerStundenplanDaten = new RouteSchuelerStundenplanDaten();

