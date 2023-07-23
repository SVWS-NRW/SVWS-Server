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
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler.stundenplan.daten", ":idStundenplan(\\d+)?", SSchuelerStundenplanDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
		super.children = [
		];
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		try {
			if (to_params.id instanceof Array || to_params.idStundenplan instanceof Array)
				return routeError.getRoute(new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein"));
			// Prüfe, ob ein schüler ausgewählt ist. Wenn nicht dann wechsele in die Schüler-Route zurück.
			const idSchueler = to_params.id === undefined ? undefined : parseInt(to_params.id);
			if (idSchueler === undefined)
				return routeSchueler.getRoute();
			// Prüfe, ob die Stundenplan-ID definiert ist, wenn nicht, dann versuche einen zu laden
			if (to_params.idStundenplan === undefined) {
				if (routeSchuelerStundenplan.data.mapStundenplaene.size === 0)
					return routeError.getRoute(new Error("Fehler: Kein Stundenplan für die angegebene ID gefunden."));
				return this.getRoute(idSchueler, routeSchuelerStundenplan.data.auswahl.id);
			}
			// Lade den Stundenplan ...
			const idStundenplan = parseInt(to_params.idStundenplan);
			await routeSchuelerStundenplan.data.setEintrag(idSchueler, idStundenplan);
		} catch (e) {
			return routeError.getRoute(e instanceof Error ? e : new Error("Fehler: Kein Stundenplan für die angegebene ID gefunden."));
		}
	}

	public async leave(from: RouteNode<unknown, any>, from_params: RouteParams): Promise<void> {
		await routeSchuelerStundenplan.data.setEintrag(-1, undefined);
	}

	public getRoute(id: number, idStundenplan: number) : RouteLocationRaw {
		return { name: this.name, params: { id, idStundenplan }};
	}

	public getProps(to: RouteLocationNormalized): SchuelerStundenplanDatenProps {
		return {
			manager: () => routeSchuelerStundenplan.data.manager,
		};
	}

}

export const routeSchuelerStundenplanDaten = new RouteSchuelerStundenplanDaten();

