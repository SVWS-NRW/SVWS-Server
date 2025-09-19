import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { SchuelerKAoAProps } from "~/components/schueler/kaoa/SSchuelerKaoaProps";
import { BenutzerKompetenz, DeveloperNotificationException, SchuelerStatus, Schulform, ServerMode } from "@core";
import { RouteDataSchuelerKAoA } from "~/router/apps/schueler/kaoa/RouteDataSchuelerKAoA";
import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeSchueler, type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { api } from "~/router/Api";


const SSchuelerKaoa = () => import("~/components/schueler/kaoa/SSchuelerKaoa.vue");

export class RouteSchuelerKAoA extends RouteNode<RouteDataSchuelerKAoA, RouteSchueler> {

	public constructor() {
		super(Schulform.values().filter(f => ![Schulform.G, Schulform.FW, Schulform.HI, Schulform.KS].includes(f)), [BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_KAOA_DATEN_AENDERN], "schueler.kaoa", "kaoa", SSchuelerKaoa, new RouteDataSchuelerKAoA());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "KAoA";
		this.isHidden = (params?: RouteParams) => this.checkHidden(params);
	}

	protected checkHidden(params: RouteParams = {}) {
		try {
			const { id } = RouteNode.getIntParams(params, ["id"]);
			const auswahl = routeSchueler.data.manager.auswahl();
			const schuljahr = routeSchueler.data.manager.getSchuljahr();
			if (!routeSchueler.data.manager.hasDaten()
				|| (auswahl.status === SchuelerStatus.EXTERN.daten(schuljahr)?.id)
				|| (auswahl.status === SchuelerStatus.EHEMALIGE.daten(schuljahr)?.id))
				return routeSchueler.getRouteDefaultChild({ id });
			return false;
		} catch (e) {
			return routeError.getSimpleErrorRoute(e as DeveloperNotificationException);
		}
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
					await this.data.ladeDaten(routeSchueler.data.manager.liste.get(id));
				} catch(error) {
					// TODO: Routing zum Schüler zurück führt zu einer Endlosschleife... return routeSchueler.getRoute({ id });
				}
			}
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public getProps(to: RouteLocationNormalized): SchuelerKAoAProps {
		return {
			schuelerKaoaManager: () => this.data.schuelerKaoaManager,
			auswahl: () => this.data.auswahl,
			add: this.data.add,
			patch: this.data.patch,
			delete: this.data.delete,
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}

}

export const routeSchuelerKAoA = new RouteSchuelerKAoA();

