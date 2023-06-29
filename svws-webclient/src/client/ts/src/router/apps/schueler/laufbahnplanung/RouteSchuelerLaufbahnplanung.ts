import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeSchueler, type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteDataSchuelerLaufbahnplanung } from "~/router/apps/schueler/laufbahnplanung/RouteDataSchuelerLaufbahnplanung";

import { ConfigElement } from "~/components/Config";
import { SSchuelerLaufbahnplanung } from "@comp";
import type { SchuelerLaufbahnplanungProps } from "@comp";

export class RouteSchuelerLaufbahnplanung extends RouteNode<RouteDataSchuelerLaufbahnplanung, RouteSchueler> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "schueler.laufbahnplanung", "laufbahnplanung", SSchuelerLaufbahnplanung, new RouteDataSchuelerLaufbahnplanung());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Laufbahnplanung";
		super.isHidden = (params?: RouteParams) => {
			if (routeSchueler.data.auswahl === undefined)
				return false;
			const abiturjahr = routeSchueler.data.auswahl?.abiturjahrgang;
			return !(abiturjahr && routeSchueler.data.mapAbiturjahrgaenge.get(abiturjahr));
		}
		api.config.addElements([new ConfigElement("app.gost.belegpruefungsart", "user", "gesamt")]);
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (this.parent === undefined)
			throw new Error("Fehler: Die Route ist ungültig - Parent ist nicht definiert");
		if (to_params.id === undefined) {
			await this.data.ladeDaten();
		} else {
			const id = parseInt(to_params.id);
			try {
				await this.data.ladeDaten(this.parent.data.mapSchueler.get(id));
			} catch(error) {
				return routeSchueler.getRoute(id);
			}
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id }};
	}

	public getProps(to: RouteLocationNormalized): SchuelerLaufbahnplanungProps {
		return {
			setWahl: this.data.setWahl,
			setGostBelegpruefungsArt: this.data.setGostBelegpruefungsArt,
			getPdfWahlbogen: this.data.getPdfWahlbogen,
			exportLaufbahnplanung: this.data.exportLaufbahnplanung,
			importLaufbahnplanung: this.data.importLaufbahnplanung,
			schueler: this.data.auswahl,
			gostJahrgangsdaten: this.data.gostJahrgangsdaten,
			gostLaufbahnBeratungsdaten: () => this.data.gostLaufbahnBeratungsdaten,
			patchBeratungsdaten: this.data.patchBeratungsdaten,
			gostBelegpruefungsArt: () => this.data.gostBelegpruefungsArt,
			gostBelegpruefungErgebnis: this.data.gostBelegpruefungErgebnis,
			abiturdatenManager: this.data.abiturdatenManager,
			faechermanager: this.data.faechermanager,
			mapFachkombinationen: this.data.mapFachkombinationen,
			mapLehrer: this.data.mapLehrer,
			id: this.data.id,
			zwischenspeicher: this.data.zwischenspeicher,
			saveLaufbahnplanung: this.data.saveLaufbahnplanung,
			restoreLaufbahnplanung: this.data.restoreLaufbahnplanung,
		};
	}

}

export const routeSchuelerLaufbahnplanung = new RouteSchuelerLaufbahnplanung();

