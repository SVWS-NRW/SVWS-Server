import { RouteNode } from "~/router/RouteNode";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import type { RouteLocation, RouteLocationNormalized, RouteParams } from "vue-router";
import { api } from "~/router/Api";
import { type RouteSchuelerLernabschnitte } from "./RouteSchuelerLernabschnitte";
import type { SchuelerLernabschnittFoerderempfehlungenProps } from "~/components/schueler/lernabschnitte/foerderempfehlungen/SchuelerLernabschnittFoerderempfehlungenProps";
import { RouteDataSchuelerLernabschnittFoerderempfehlungen } from "./RouteDataSchuelerLernabschnittFoerderempfehlungen";

const SchuelerLernabschnittFoerderempfehlungen = () => import("~/components/schueler/lernabschnitte/foerderempfehlungen/SchuelerLernabschnittFoerderempfehlungen.vue");

class RouteSchuelerLernabschnittFoerderempfehlungen extends RouteNode<RouteDataSchuelerLernabschnittFoerderempfehlungen, RouteSchuelerLernabschnitte> {
	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN], "schueler.lernabschnitt.foerderempfehlungen", "foerderempfehlungen", SchuelerLernabschnittFoerderempfehlungen, new RouteDataSchuelerLernabschnittFoerderempfehlungen);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Förderempfehlungen";
		super.children = [];
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams): Promise<void | Error | RouteLocation> {
		await this.data.ladeFoerderempfehlungen();
	}

	public getProps(to: RouteLocationNormalized): SchuelerLernabschnittFoerderempfehlungenProps {
		return {
			benutzerKompetenzen: api.benutzerKompetenzen,
			foerderempfehlungen: () => this.data.listFoerderempfehlungen,
			add: this.data.addFoerderempfehlung,
			patch: this.data.patchFoerderempfehlung,
			delete: this.data.deleteFoerderempfehlungen,
		};
	}
}

export const routeSchuelerLernabschnittFoerderempfehlungen = new RouteSchuelerLernabschnittFoerderempfehlungen();
