import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { routeLehrerStundenplan, type RouteLehrerStundenplan } from "~/router/apps/lehrer/stundenplan/RouteLehrerStundenplan";

import type { LehrerStundenplanDatenProps } from "~/components/lehrer/stundenplan/SLehrerStundenplanDatenProps";


const SLehrerStundenplanDaten = () => import("~/components/lehrer/stundenplan/SLehrerStundenplanDaten.vue");

export class RouteLehrerStundenplanDaten extends RouteNode<unknown, RouteLehrerStundenplan> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "lehrer.stundenplan.daten", ":idStundenplan(\\d+)?", SLehrerStundenplanDaten);
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
			// Prüfe, ob ein Lehrer ausgewählt ist. Wenn nicht dann wechsele in die Lehrer-Route zurück.
			const idLehrer = to_params.id === undefined ? undefined : parseInt(to_params.id);
			if (idLehrer === undefined)
				return routeLehrer.getRoute(undefined);
			// Prüfe, ob die Stundenplan-ID definiert ist, wenn nicht, dann versuche einen zu laden
			if (to_params.idStundenplan === undefined) {
				if (routeLehrerStundenplan.data.mapStundenplaene.size === 0)
					throw new Error("Fehler: Kein Stundenplan für die angegebene ID gefunden.");
				return this.getRoute(idLehrer, routeLehrerStundenplan.data.auswahl.id);
			}
			// Lade den Stundenplan ...
			const idStundenplan = parseInt(to_params.idStundenplan);
			await routeLehrerStundenplan.data.setEintrag(idLehrer, idStundenplan);
		} catch (e) {
			return routeError.getRoute(e instanceof Error ? e : new Error("Fehler: Kein Stundenplan für die angegebene ID gefunden."));
		}
	}

	public async leave(from: RouteNode<unknown, any>, from_params: RouteParams): Promise<void> {
		await routeLehrerStundenplan.data.setEintrag(-1, undefined);
	}

	public getRoute(id: number, idStundenplan: number) : RouteLocationRaw {
		return { name: this.name, params: { id, idStundenplan }};
	}

	public getProps(to: RouteLocationNormalized): LehrerStundenplanDatenProps {
		return {
			manager: () => routeLehrerStundenplan.data.manager,
		};
	}

}

export const routeLehrerStundenplanDaten = new RouteLehrerStundenplanDaten();

