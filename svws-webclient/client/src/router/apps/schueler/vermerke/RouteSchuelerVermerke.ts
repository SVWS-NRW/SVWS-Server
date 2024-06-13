import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeSchueler, type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteDataSchuelerVermerke } from "~/router/apps/schueler/vermerke/RouteDataSchuelerVermerke";
import type { SchuelerVermerkeProps } from "~/components/schueler/vermerke/SSchuelerVermerkeProps";
import { routeApp } from "../../RouteApp";


const SSchuelerVermerke = () => import("~/components/schueler/vermerke/SSchuelerVermerke.vue");

export class RouteSchuelerVermerke extends RouteNode<RouteDataSchuelerVermerke, RouteSchueler> {

	public constructor() {
		super(Schulform.values().filter(f=>!f.equals(Schulform.G)), [ BenutzerKompetenz.KEINE ], "schueler.vermerke", "vermerke", SSchuelerVermerke, new RouteDataSchuelerVermerke());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Vermerke";
		this.isHidden = (params?: RouteParams) => {
			if ((params === undefined) || (params.id instanceof Array))
				return routeError.getRoute(new DeveloperNotificationException("Fehler: Die Parameter der Route sind nicht gültig gesetzt."));
			return routeSchueler.data.schuelerListeManager.hasDaten() ? false : routeSchueler.getRoute(parseInt(params.id));
		};
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array)
			return routeError.getRoute(new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein"));
		if (this.parent === undefined)
			return routeError.getRoute(new DeveloperNotificationException("Fehler: Die Route ist ungültig - Parent ist nicht definiert"));
		if (to_params.id === undefined) {
			await this.data.ladeDaten(null);
		} else {
			const id = parseInt(<string>to_params.id);
			try {
				await this.data.ladeDaten(routeSchueler.data.schuelerListeManager.liste.get(id));
			} catch(error) {
				return routeSchueler.getRoute(id);
			}
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getProps(to: RouteLocationNormalized): SchuelerVermerkeProps {
		return {
			data: this.data.data,
			mapVermerkArten: this.data.mapVermerkArten,
			patch: this.data.patch,
			create: this.data.create,
			deleteVermerk: this.data.deleteVermerk
			}
		};

}

export const routeSchuelerVermerke = new RouteSchuelerVermerke();

