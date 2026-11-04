import type { ApiFile } from "@core/api/BaseApi";
import { GostBelegpruefungsArt } from "@core/core/abschluss/gost/GostBelegpruefungsArt";
import type { GostBelegpruefungsErgebnisse } from "@core/core/data/gost/GostBelegpruefungsErgebnisse";
import type { GostSchuelerWiederholung } from "@core/core/data/gost/GostSchuelerWiederholung";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";

import { routeSchuelerSprachen } from "../../schueler/sprachen/RouteSchuelerSprachen";
import { api } from "~/router/Api";
import { routeSchuelerLaufbahnplanung } from "~/router/apps/schueler/laufbahnplanung/RouteSchuelerLaufbahnplanung";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";
import { configStateImpl } from "~/states/ConfigStateImpl";
import { serverStateImpl } from "~/states/ServerStateImpl";


interface RouteStateDataGostLaufbahnfehler extends RouteStateInterface {
	abiturjahr: number;
	listBelegpruefungsErgebnisse: List<GostBelegpruefungsErgebnisse>;
	gostBelegpruefungsArt: GostBelegpruefungsArt;
	mapWiederholungen: Map<number, GostSchuelerWiederholung>;
}

const defaultState = <RouteStateDataGostLaufbahnfehler> {
	abiturjahr: -1,
	listBelegpruefungsErgebnisse: new ArrayList(),
	gostBelegpruefungsArt: GostBelegpruefungsArt.GESAMT,
	mapWiederholungen: new Map<number, GostSchuelerWiederholung>(),
};

export class RouteDataGostLaufbahnfehler extends RouteData<RouteStateDataGostLaufbahnfehler> {

	public constructor() {
		super(defaultState);
	}

	get gostBelegpruefungsArt(): GostBelegpruefungsArt {
		return this._state.value.gostBelegpruefungsArt;
	}

	get abiturjahr(): number {
		return this._state.value.abiturjahr;
	}

	get listBelegpruefungsErgebnisse(): List<GostBelegpruefungsErgebnisse> {
		return this._state.value.listBelegpruefungsErgebnisse;
	}

	get mapWiederholungen(): Map<number, GostSchuelerWiederholung> {
		return this._state.value.mapWiederholungen;
	}

	// Wer im folgenden Jahrgang wiederholt, wird dort geplant und gehört für diesen Jahrgang zu einem anderen
	private istWiederholungAndererJahrgang(idSchueler: number): boolean {
		const wiederholung = this._state.value.mapWiederholungen.get(idSchueler);
		return (wiederholung !== undefined) && (wiederholung.abiturjahr !== null) && (wiederholung.abiturjahr !== this.abiturjahr);
	}

	get filterFehler(): boolean {
		return configStateImpl.config.getValue('gost.laufbahnfehler.filterFehler') === 'true';
	}

	setFilterFehler = async (value: boolean) => {
		await configStateImpl.config.setValue('gost.laufbahnfehler.filterFehler', value ? "true" : "false");
	};

	get filterExterne(): boolean {
		return configStateImpl.config.getValue('gost.laufbahnfehler.filterExterne') === 'true';
	}

	setFilterExterne = async (value: boolean) => {
		await configStateImpl.config.setValue('gost.laufbahnfehler.filterExterne', value ? "true" : "false");
	};

	get filterNurMitFachwahlen(): boolean {
		return configStateImpl.config.getValue('gost.laufbahnfehler.filterNurMitFachwahlen') === 'true';
	}

	setFilterNurMitFachwahlen = async (value: boolean) => {
		await configStateImpl.config.setValue('gost.laufbahnfehler.filterNurMitFachwahlen', value ? "true" : "false");
	};

	get filterNeuaufnahmen(): boolean {
		return configStateImpl.config.getValue('gost.laufbahnfehler.filterNeuaufnahmen') === 'true';
	}

	setFilterNeuaufnahmen = async (value: boolean) => {
		await configStateImpl.config.setValue('gost.laufbahnfehler.filterNeuaufnahmen', value ? "true" : "false");
	};

	protected async updateList(abiturjahr: number, gostBelegpruefungsArt: GostBelegpruefungsArt) {
		if (abiturjahr < 1) {
			throw new DeveloperNotificationException(`Fehlerhafte Übergabe des Abiturjahrs: ${abiturjahr}`);
		}
		const listBelegpruefungsErgebnisse = (gostBelegpruefungsArt === GostBelegpruefungsArt.GESAMT)
			? await api.server.getGostAbiturjahrgangBelegpruefungsergebnisseGesamt(api.schema, abiturjahr)
			: await api.server.getGostAbiturjahrgangBelegpruefungsergebnisseEF1(api.schema, abiturjahr);
		// Wiederholungen gibt es zunächst nur im Servermode DEV, sonst lehnt der Server die Abfrage ab
		const mapWiederholungen = new Map<number, GostSchuelerWiederholung>();
		const listWiederholungen = serverStateImpl.hasDev ? await api.server.getGostSchuelerWiederholungen(api.schema) : [];
		for (const wiederholung of listWiederholungen) {
			mapWiederholungen.set(wiederholung.idSchueler, wiederholung);
		}
		this.setPatchedState({ listBelegpruefungsErgebnisse, gostBelegpruefungsArt, abiturjahr, mapWiederholungen });
	}

	public async setAbiturjahr(abiturjahr: number) {
		await this.updateList(abiturjahr, this._state.value.gostBelegpruefungsArt);
	}

	setGostBelegpruefungsArt = async (gostBelegpruefungsArt: GostBelegpruefungsArt) => {
		if (gostBelegpruefungsArt === this.gostBelegpruefungsArt) {
			return;
		}
		await this.updateList(this.abiturjahr, gostBelegpruefungsArt);
		this.setPatchedState({ gostBelegpruefungsArt });
	};

	gotoLaufbahnplanung = async (idSchueler: number) =>
		await RouteManager.doRoute(routeSchuelerLaufbahnplanung.getRoute({ id: idSchueler }));

	gotoSprachenfolge = async (idSchueler: number) =>
		await RouteManager.doRoute(routeSchuelerSprachen.getRoute({ id: idSchueler }));

	importLaufbahnplanung = async (data: FormData): Promise<void> => {
		api.status.start();
		await api.server.importGostSchuelerLaufbahnplanungen(data, api.schema);
		await this.setAbiturjahr(this.abiturjahr);
		api.status.stop();
	};

	exportLaufbahnplanung = async (schueler: List<number>): Promise<ApiFile> => {
		api.status.start();
		const res = await api.server.exportGostSchuelerLaufbahnplanungen(schueler, api.schema);
		api.status.stop();
		return res;
	};

	resetFachwahlenAlle = async (ergebnisse: Iterable<GostBelegpruefungsErgebnisse>) => {
		if ([...ergebnisse].length === this.listBelegpruefungsErgebnisse.size()) {
			await api.server.resetGostAbiturjahrgangSchuelerFachwahlen(api.schema, this.abiturjahr);
		} else {
			for (const ergebnis of ergebnisse) {
				// Wer im folgenden Jahrgang wiederholt, wird dort geplant. Diese Planung bleibt bei Aktionen im bisherigen Jahrgang unverändert.
				if (!this.istWiederholungAndererJahrgang(ergebnis.schueler.id)) {
					await api.server.resetGostSchuelerFachwahlen(api.schema, ergebnis.schueler.id);
				}
			}
		}
		await this.setAbiturjahr(this.abiturjahr);
	};

	loeschenFachwahlenSelected = async (ergebnisse: Iterable<GostBelegpruefungsErgebnisse>) => {
		const idsSchueler = new ArrayList<number>();
		for (const ergebnis of ergebnisse) {
			// Wer im folgenden Jahrgang wiederholt, wird dort geplant. Diese Planung bleibt bei Aktionen im bisherigen Jahrgang unverändert.
			if (!this.istWiederholungAndererJahrgang(ergebnis.schueler.id)) {
				idsSchueler.add(ergebnis.schueler.id);
			}
		}
		if (!idsSchueler.isEmpty()) {
			await api.server.deleteGostSchuelerFachwahlenMultiple(idsSchueler, api.schema);
		}
		await this.setAbiturjahr(this.abiturjahr);
	};

}
