
import { computed } from "vue";

import type { SchuelerStatistikGesamt } from "@core/asd/data/statistik/SchuelerStatistikGesamt";
import { StatistikGesamt } from "@core/asd/data/statistik/StatistikGesamt";
import type { ZusatzerhebungenStatistikGesamt } from "@core/asd/data/statistik/ZusatzerhebungenStatistikGesamt";
import { ValidatorGesamt } from "@core/asd/validate/ValidatorGesamt";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import type { StatistikState } from "@ui/states/statistik/StatistikState";
import { StateManager } from "@ui/ui/StateManager";

import { klassenAuswahlStateImpl } from "../klassen/KlassenAuswahlStateImpl";
import { kurseAuswahlStateImpl } from "../kurse/KurseAuswahlStateImpl";
import { lehrerAuswahlStateImpl } from "../lehrer/LehrerAuswahlStateImpl";
import { schuelerAuswahlStateImpl } from "../schueler/SchuelerAuswahlStateImpl";
import { schuleStateImpl } from "../SchuleStateImpl";
import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";


interface StatistikReactiveState {
	statistikGesamt: StatistikGesamt;
	mapLehrer: Map<number, LehrerListeEintrag>;
	mapSchueler: Map<number, SchuelerListeEintrag>;
	mapStatistikSchueler: Map<number, SchuelerStatistikGesamt>
}

/**
 * Die Schnittstelle für den Zustand des Servers
 */
export class StatistikStateImpl extends StateManager<StatistikReactiveState> implements StatistikState {

	public constructor() {
		super({
			statistikGesamt: new StatistikGesamt(),
			mapLehrer: new Map<number, LehrerListeEintrag>(),
			mapSchueler: new Map<number, SchuelerListeEintrag>(),
			mapStatistikSchueler: new Map<number, SchuelerStatistikGesamt>(),
		});
	}

	public async init(): Promise<void> {
		const statistikGesamt = await api.server.getStatistikGesamt(api.schema);
		const listeSchueler = await api.server.getSchuelerAuswahllisteFuerAbschnitt(api.schema, schuleStateImpl.abschnitt.id);
		const listeLehrer = await api.server.getLehrerFuerAbschnitt(api.schema, schuleStateImpl.abschnitt.id);

		// Lehrer-State
		await lehrerAuswahlStateImpl.init(schuleStateImpl.abschnitt.id, true);

		// Schüler-State
		await schuelerAuswahlStateImpl.init(schuleStateImpl.abschnitt.id, true);

		// Kurse-State
		await kurseAuswahlStateImpl.init(schuleStateImpl.abschnitt.id, true);

		// Klassen-State
		await klassenAuswahlStateImpl.init(schuleStateImpl.abschnitt.id, true);

		const mapLehrer = new Map<number, LehrerListeEintrag>();
		const mapSchueler = new Map<number, SchuelerListeEintrag>();
		const mapStatistikSchueler = new Map<number, SchuelerStatistikGesamt>();
		for (const s of listeSchueler.schueler) {
			mapSchueler.set(s.id, s);
		}
		for (const l of listeLehrer) {
			mapLehrer.set(l.id, l);
		}
		for (const s of statistikGesamt.schueler) {
			mapStatistikSchueler.set(s.id, s);
		}

		this.setPatchedState({ mapLehrer, mapSchueler, statistikGesamt, mapStatistikSchueler });
	}

	public reload = () => this.init();

	public get statistikGesamt(): StatistikGesamt {
		return this.state.statistikGesamt;
	}

	public get mapLehrer(): Map<number, LehrerListeEintrag> {
		return this.state.mapLehrer;
	}

	public get mapSchueler(): Map<number, SchuelerListeEintrag> {
		return this.state.mapSchueler;
	}

	public get mapSchuelerStatistik(): Map<number, SchuelerStatistikGesamt> {
		return this.state.mapStatistikSchueler;
	}

	private readonly _validatorGesamt = computed<ValidatorGesamt>(() => {
		const v = new ValidatorGesamt({ get: () => this.statistikGesamt }, schuleStateImpl.validatorKontext);
		v.run();
		return v;
	});

	public get validatorGesamt(): ValidatorGesamt {
		return this._validatorGesamt.value;
	}

	public setLehrer = async (id: number) => {
		const eintrag = lehrerAuswahlStateImpl.getEntry(id);
		await lehrerAuswahlStateImpl.setDaten(eintrag);
	};

	public setSchueler = async (id: number) => {
		const eintrag = schuelerAuswahlStateImpl.getEntry(id);
		await schuelerAuswahlStateImpl.setDaten(eintrag);
	};

	public setKurs = async (id: number) => {
		const eintrag = kurseAuswahlStateImpl.getEntry(id);
		await kurseAuswahlStateImpl.setDaten(eintrag);
	};

	public setKlasse = async (id: number) => {
		const eintrag = klassenAuswahlStateImpl.getEntry(id);
		await klassenAuswahlStateImpl.setDaten(eintrag);
	};

	/**
	 * Patcht die Daten der Zusatzerhebungen für den Schuljahresabschnitt der Statistik und übernimmt
	 * die vom Server zurückgegebenen Daten in die Gesamtstatistik.
	 *
	 * @param data   die zu ändernden Daten
	 *
	 * @returns true, wenn der Patch erfolgreich war
	 */
	public patchZusatzerhebungen = async (data: Partial<ZusatzerhebungenStatistikGesamt>): Promise<boolean> => {
		const idSchuljahresabschnitt = this.statistikGesamt.zusatzerhebungen.idSchuljahresabschnitt;
		this.statistikGesamt.zusatzerhebungen = await api.server.patchStatistikZusatzerhebungen(data, api.schema, idSchuljahresabschnitt);
		this.commit();
		return true;
	};

	public gotoTab = async (name: string) => {
		await RouteManager.doRoute({ name });
	};
}

export const statistikStateImpl = new StatistikStateImpl();
