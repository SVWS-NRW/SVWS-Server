import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";

import { DeveloperNotificationException} from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { type RouteLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { routeLehrerStundenplanDaten } from "~/router/apps/lehrer/stundenplan/RouteLehrerStundenplanDaten";
import { RouteDataLehrerStundenplan } from "~/router/apps/lehrer/stundenplan/RouteDataLehrerStundenplan";

import type { StundenplanAuswahlProps } from "@ui";
import { ConfigElement } from "~/components/Config";
import { api } from "~/router/Api";

const SLehrerStundenplan = () => import("~/components/lehrer/stundenplan/SLehrerStundenplan.vue");

export class RouteLehrerStundenplan extends RouteNode<RouteDataLehrerStundenplan, RouteLehrer> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN ], "lehrer.stundenplan", "stundenplan", SLehrerStundenplan, new RouteDataLehrerStundenplan());
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

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { idSchuljahresabschnitt, id } = RouteNode.getIntParams(to_params, ["idSchuljahresabschnitt", "id"]);
			if (idSchuljahresabschnitt === undefined)
				throw new DeveloperNotificationException("Beim Aufruf der Route ist kein gültiger Schuljahresabschnitt gesetzt.");
			if (id === undefined)
				return;

			if (isEntering || (idSchuljahresabschnitt !== routeLehrerStundenplan.data.idSchuljahresabschnitt))
				await routeLehrerStundenplan.data.ladeListe();

			if ((to.name === routeLehrerStundenplanDaten.name) && (routeLehrerStundenplan.data.mapStundenplaene.size === 0))
				return routeLehrerStundenplan.getRoute({ id });

			// Prüfe, ob diese Route das Ziel ist. Wenn dies der fall ist, dann muss ggf. noch ein Stundenplan geladen werden
			if (to.name === this.name) {
				// Und wähle dann einen Eintrag aus der Stundenplanliste aus, wenn diese nicht leer ist
				if (routeLehrerStundenplan.data.mapStundenplaene.size !== 0) {
					const [stundenplan] = routeLehrerStundenplan.data.mapStundenplaene.values();
					return routeLehrerStundenplanDaten.getRoute({ id, idStundenplan: stundenplan.id, wochentyp: 0 });
				}
			}
		} catch (e) {
			return routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public addRouteParamsFromState() : RouteParamsRawGeneric {
		return {
			idStundenplan: (this.data.hasAuswahl === true) ? this.data.auswahl.id : undefined,
			wochentyp: this.data.wochentyp,
			kw: (this.data.kalenderwoche === undefined) ? undefined : this.data.kalenderwoche.jahr + "." + this.data.kalenderwoche.kw,
		};
	}

	public getProps(to: RouteLocationNormalized): StundenplanAuswahlProps {
		return {
			stundenplan: ((this.data.mapStundenplaene.size === 0) || (this.data.hasAuswahl === false)) ? undefined : this.data.auswahl,
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

