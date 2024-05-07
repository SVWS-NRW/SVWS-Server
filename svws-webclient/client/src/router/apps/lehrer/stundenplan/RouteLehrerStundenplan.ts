import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeLehrer, type RouteLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { routeLehrerStundenplanDaten } from "~/router/apps/lehrer/stundenplan/RouteLehrerStundenplanDaten";
import { RouteDataLehrerStundenplan } from "~/router/apps/lehrer/stundenplan/RouteDataLehrerStundenplan";

import type { StundenplanAuswahlProps } from "@comp";
import { ConfigElement } from "~/components/Config";
import { api } from "~/router/Api";
import { routeApp } from "../../RouteApp";

const SLehrerStundenplan = () => import("~/components/lehrer/stundenplan/SLehrerStundenplan.vue");

export class RouteLehrerStundenplan extends RouteNode<RouteDataLehrerStundenplan, RouteLehrer> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "lehrer.stundenplan", "stundenplan", SLehrerStundenplan, new RouteDataLehrerStundenplan());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
		super.children = [
			routeLehrerStundenplanDaten
		];
		super.defaultChild = routeLehrerStundenplanDaten;
		api.config.addElements([
			new ConfigElement("lehrer.stundenplan.ganzerStundenplan", "user", "true"),
		]);
	}

	public async enter(to: RouteNode<any, any>, to_params: RouteParams): Promise<void | Error | RouteLocationRaw> {
		await routeLehrerStundenplan.data.ladeListe();
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array || to_params.idStundenplan instanceof Array)
			return routeError.getRoute(new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein"));
		// Prüfe, ob ein Lehrer ausgewählt ist. Wenn nicht dann wechsele in die Lehrer-Route zurück.
		const idLehrer = to_params.id === undefined ? undefined : parseInt(to_params.id);
		if (idLehrer === undefined)
			return routeLehrer.getRoute(undefined);
		// Prüfe, ob diese Route das Ziel ist. Wenn dies der fall ist, dann muss ggf. noch ein Stundenplan geladen werden
		if (to.name === this.name) {
			// Und wähle dann einen Eintrag aus der Stundenplanliste aus, wenn diese nicht leer ist
			if (routeLehrerStundenplan.data.mapStundenplaene.size !== 0) {
				const stundenplan = routeLehrerStundenplan.data.mapStundenplaene.entries().next().value;
				if (stundenplan !== undefined)
					return routeLehrerStundenplanDaten.getRoute(idLehrer, stundenplan.id, 0);
			}
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: id, wochentyp: 0 }};
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
			ganzerStundenplan: () => this.data.ganzerStundenplan,
			setGanzerStundenplan: this.data.setGanzerStundenplan,
		};
	}

}

export const routeLehrerStundenplan = new RouteLehrerStundenplan();

