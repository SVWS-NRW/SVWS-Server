
import { computed } from "vue";

import { StatistikGesamt } from "@core/asd/data/statistik/StatistikGesamt";
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


interface StatistikReactiveState {
	statistikGesamt: StatistikGesamt;
	mapLehrer: Map<number, LehrerListeEintrag>;
	mapSchueler: Map<number, SchuelerListeEintrag>;
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
		for (const s of listeSchueler.schueler) {
			mapSchueler.set(s.id, s);
		}
		for (const l of listeLehrer) {
			mapLehrer.set(l.id, l);
		}

		this.setPatchedState({ mapLehrer, mapSchueler, statistikGesamt });
	}

	public get statistikGesamt(): StatistikGesamt {
		return this.state.statistikGesamt;
	}

	public get mapLehrer(): Map<number, LehrerListeEintrag> {
		return this.state.mapLehrer;
	}

	public get mapSchueler(): Map<number, SchuelerListeEintrag> {
		return this.state.mapSchueler;
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
		const daten = await api.server.getLehrerStammdaten(api.schema, id);
		lehrerAuswahlStateImpl.setDatenDirekt(daten);
		await lehrerAuswahlStateImpl.loadPersonaldaten();
	};

	public setSchueler = async (id: number) => {
		const daten = await api.server.getSchuelerStammdaten(api.schema, id);
		schuelerAuswahlStateImpl.setDatenDirekt(daten);
		schuelerAuswahlStateImpl.commit();
	};

	public setKurs = async (id: number) => {
		const daten = await api.server.getKurs(api.schema, id);
		kurseAuswahlStateImpl.setDatenDirekt(daten);
	};

	public setKlasse = async (id: number) => {
		const daten = await api.server.getKlasse(api.schema, id);
		klassenAuswahlStateImpl.setDatenDirekt(daten);
	};
}

export const statistikStateImpl = new StatistikStateImpl();
