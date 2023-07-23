import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeKlassen } from "~/router/apps/klassen/RouteKlassen";
import { routeKlassenStundenplan, type RouteKlassenStundenplan } from "~/router/apps/klassen/stundenplan/RouteKlassenStundenplan";

import type { KlassenStundenplanDatenProps } from "~/components/klassen/stundenplan/SKlassenStundenplanDatenProps";


const SKlassenStundenplanDaten = () => import("~/components/klassen/stundenplan/SKlassenStundenplanDaten.vue");

export class RouteKlassenStundenplanDaten extends RouteNode<unknown, RouteKlassenStundenplan> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "klassen.stundenplan.daten", ":idStundenplan(\\d+)?", SKlassenStundenplanDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
		super.children = [
		];
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		try {
			if (to_params.id instanceof Array || to_params.idStundenplan instanceof Array)
				throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
			// Prüfe, ob eine Klasse ausgewählt ist. Wenn nicht dann wechsele in die Klassen-Route zurück.
			const idKlasse = to_params.id === undefined ? undefined : parseInt(to_params.id);
			if (idKlasse === undefined)
				return routeKlassen.getRoute(undefined);
			// Prüfe, ob die Stundenplan-ID definiert ist, wenn nicht, dann versuche einen zu laden
			if (to_params.idStundenplan === undefined) {
				if (routeKlassenStundenplan.data.mapStundenplaene.size === 0)
					throw new Error("Fehler: Kein Stundenplan für die angegebene ID gefunden.");
				return this.getRoute(idKlasse, routeKlassenStundenplan.data.auswahl.id);
			}
			// Lade den Stundenplan ...
			const idStundenplan = parseInt(to_params.idStundenplan);
			await routeKlassenStundenplan.data.setEintrag(idKlasse, idStundenplan);
		} catch (e) {
			return routeError.getRoute(e instanceof Error ? e : new Error("Fehler: Kein Stundenplan für die angegebene ID gefunden."));
		}
	}

	public async leave(from: RouteNode<unknown, any>, from_params: RouteParams): Promise<void> {
		await routeKlassenStundenplan.data.setEintrag(-1, undefined);
	}

	public getRoute(id: number, idStundenplan: number) : RouteLocationRaw {
		return { name: this.name, params: { id, idStundenplan }};
	}

	public getProps(to: RouteLocationNormalized): KlassenStundenplanDatenProps {
		return {
			manager: () => routeKlassenStundenplan.data.manager,
		};
	}

}

export const routeKlassenStundenplanDaten = new RouteKlassenStundenplanDaten();

