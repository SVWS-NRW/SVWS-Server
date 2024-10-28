import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeSchueler, type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteDataSchuelerKAoA } from "~/router/apps/schueler/kaoa/RouteDataSchuelerKAoA";

import type { SchuelerKAoAProps } from "~/components/schueler/kaoa/SSchuelerKaoaProps";


const SSchuelerKaoa = () => import("~/components/schueler/kaoa/SSchuelerKaoa.vue");

export class RouteSchuelerKAoA extends RouteNode<RouteDataSchuelerKAoA, RouteSchueler> {

	public constructor() {
		super(Schulform.values().filter(f => !f.equals(Schulform.G)), [ BenutzerKompetenz.KEINE ], "schueler.kaoa", "kaoa", SSchuelerKaoa, new RouteDataSchuelerKAoA());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "KAoA";
		this.isHidden = (params?: RouteParams) => {
			if ((params === undefined) || (params.id instanceof Array))
				return routeError.getErrorRoute(new DeveloperNotificationException("Fehler: Die Parameter der Route sind nicht gültig gesetzt."));
			return routeSchueler.data.schuelerListeManager.hasDaten() ? false : routeSchueler.getRoute({ id: parseInt(params.id) });
		};
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { id } = RouteNode.getIntParams(to_params, ["id"]);
			if (this.parent === undefined)
				throw new DeveloperNotificationException("Fehler: Die Route ist ungültig - Parent ist nicht definiert");
			if (id === undefined) {
				await this.data.ladeDaten(null);
			} else {
				try {
					await this.data.ladeDaten(routeSchueler.data.schuelerListeManager.liste.get(id));
				} catch(error) {
					// TODO: Routing zum Schüler zurück führt zu einer Endlosschleife... return routeSchueler.getRoute({ id });
				}
			}
		} catch (e) {
			return routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public getProps(to: RouteLocationNormalized): SchuelerKAoAProps {
		return {
			schuelerKaoaManager: () => this.data.schuelerKaoaManager,
			auswahl: () => this.data.auswahl,
			add: this.data.add,
			patch: this.data.patch,
			delete: this.data.delete
		};
	}

}

export const routeSchuelerKAoA = new RouteSchuelerKAoA();

