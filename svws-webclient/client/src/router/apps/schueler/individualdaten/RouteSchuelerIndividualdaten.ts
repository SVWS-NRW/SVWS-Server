import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "~/router/apps/RouteApp";
import { routeSchueler, type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteDataSchuelerIndividualdaten } from "~/router/apps/schueler/individualdaten/RouteDataSchuelerIndividualdaten";
import type { SchuelerIndividualdatenProps } from "~/components/schueler/individualdaten/SchuelerIndividualdatenProps";
import { wiedervorlageStateImpl } from "~/states/wiedervorlage/WiedervorlageStateImpl";

const SSchuelerIndividualdaten = () => import("~/components/schueler/individualdaten/SchuelerIndividualdaten.vue");


export class RouteSchuelerIndividualdaten extends RouteNode<RouteDataSchuelerIndividualdaten, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KEINE], "schueler.daten", "daten", SSchuelerIndividualdaten, new RouteDataSchuelerIndividualdaten());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Individualdaten";
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		// initialize used states
		await Promise.all([
			wiedervorlageStateImpl.init(),
		]);

		if (isEntering) {
			await this.data.ladeListe();
		}
	}

	public getProps(to: RouteLocationNormalized): SchuelerIndividualdatenProps {
		return {
			patch: routeSchueler.data.patch,
			schuelerListeManager: () => routeSchueler.data.manager,
			fahrschuelerartenById: routeApp.cache.kataloge.fahrschuelerartenById,
			foerderschwerpunkteById: routeApp.cache.kataloge.foerderschwerpunkteById,
			haltestellenById: routeApp.cache.kataloge.haltestellenById,
			religionenById: routeApp.cache.kataloge.religionenById,
			mapTelefonArten: routeApp.cache.kataloge.telefonartenById,
			getListSchuelerTelefoneintraege: () => routeSchueler.data.getListSchuelerTelefoneintraege,
			addSchuelerTelefoneintrag: routeSchueler.data.addSchuelerTelefoneintrag,
			patchSchuelerTelefoneintrag: routeSchueler.data.patchSchuelerTelefoneintrag,
			deleteSchuelerTelefoneintrage: routeSchueler.data.deleteSchuelerTelefoneintrage,
			mapSchulen: this.data.mapSchulen,
			autofocus: routeSchueler.data.autofocus,
			zeigeAlles: true,
		};
	}

}

export const routeSchuelerIndividualdaten = new RouteSchuelerIndividualdaten();

