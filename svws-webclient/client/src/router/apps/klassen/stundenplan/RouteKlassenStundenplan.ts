import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeKlassen, type RouteKlassen } from "~/router/apps/klassen/RouteKlassen";
import { routeKlassenStundenplanDaten } from "~/router/apps/klassen/stundenplan/RouteKlassenStundenplanDaten";
import { RouteDataKlassenStundenplan } from "~/router/apps/klassen/stundenplan/RouteDataKlassenStundenplan";

import { type StundenplanAuswahlProps } from "@comp";

const SKlassenStundenplan = () => import("~/components/klassen/stundenplan/SKlassenStundenplan.vue");

export class RouteKlassenStundenplan extends RouteNode<RouteDataKlassenStundenplan, RouteKlassen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "klassen.stundenplan", "stundenplan", SKlassenStundenplan, new RouteDataKlassenStundenplan());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
		super.children = [
			routeKlassenStundenplanDaten
		];
		super.defaultChild = routeKlassenStundenplanDaten;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<void | Error | RouteLocationRaw> {
		await routeKlassenStundenplan.data.ladeListe();
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array || to_params.idStundenplan instanceof Array)
			return routeError.getRoute(new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein"));
		// Prüfe, ob eine Klasse ausgewählt ist. Wenn nicht dann wechsele in die Klassen-Route zurück.
		const idKlasse = to_params.id === undefined ? undefined : parseInt(to_params.id);
		if (idKlasse === undefined)
			return routeKlassen.getRoute(undefined);
		// Prüfe, ob diese Route als aktuelle View für die Tab-Bar gesetzt ist
		if (routeKlassen.data.view !== this)
			routeKlassen.data.setView(this, routeKlassen.children);
		// Prüfe, ob diese Route das Ziel ist. Wenn dies der fall ist, dann muss ggf. noch ein Stundenplan geladen werden
		if (to.name === this.name) {
			// Und wähle dann einen Eintrag aus der Stundenplanliste aus, wenn diese nicht leer ist
			if (routeKlassenStundenplan.data.mapStundenplaene.size !== 0) {
				const stundenplan = routeKlassenStundenplan.data.mapStundenplaene.entries().next().value;
				if (stundenplan !== undefined)
					return routeKlassenStundenplanDaten.getRoute(idKlasse, stundenplan.id, 0);
			}
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		let redirect: RouteNode<unknown, any> = (this.selectedChild === undefined) ? this.defaultChild! : this.selectedChild;
		if (redirect.hidden({ id: String(id) }))
			redirect = this.defaultChild!;
		return redirect.getRoute(id);
	}

	public getProps(to: RouteLocationNormalized): StundenplanAuswahlProps {
		return {
			stundenplan: this.data.mapStundenplaene.size === 0 ? undefined : this.data.auswahl,
			mapStundenplaene: this.data.mapStundenplaene,
			gotoStundenplan: this.data.gotoStundenplan,
			gotoWochentyp: this.data.gotoWochentyp,
			gotoKalenderwoche: this.data.gotoKalenderwoche,
			manager: () => this.data.manager,
			wochentyp: () => this.data.wochentyp,
			kalenderwoche: () => this.data.kalenderwoche,
		};
	}

}

export const routeKlassenStundenplan = new RouteKlassenStundenplan();

