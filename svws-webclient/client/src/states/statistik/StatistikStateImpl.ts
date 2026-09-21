
import { computed } from "vue";

import { StatistikGesamt } from "@core/asd/data/statistik/StatistikGesamt";
import { ValidatorGesamt } from "@core/asd/validate/ValidatorGesamt";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import type { StatistikState } from "@ui/states/statistik/StatistikState";
import type { LehrerListeManager } from "@ui/ui/manager/lehrer/LehrerListeManager";
import { StateManager } from "@ui/ui/StateManager";

import { klassenAuswahlStateImpl } from "../klassen/KlassenAuswahlStateImpl";
import type { KlassenListeManager } from "../klassen/KlassenListeManager";
import { kurseAuswahlStateImpl } from "../kurse/KurseAuswahlStateImpl";
import type { KursListeManager } from "../kurse/KursListeManager";
import { lehrerAuswahlStateImpl } from "../lehrer/LehrerAuswahlStateImpl";
import { schuelerAuswahlStateImpl } from "../schueler/SchuelerAuswahlStateImpl";
import type { SchuelerListeManager } from "../schueler/SchuelerListeManager";
import { schuleStateImpl } from "../SchuleStateImpl";
import { api } from "~/router/Api";


interface StatistikReactiveState {
	statistikGesamt: StatistikGesamt;
	mapLehrer: Map<number, LehrerListeEintrag>;
	mapSchueler: Map<number, SchuelerListeEintrag>;
	lehrerListeManager: LehrerListeManager | undefined;
	schuelerListeManager: SchuelerListeManager | undefined;
	kursListeManager: KursListeManager | undefined;
	klassenListeManager: KlassenListeManager | undefined;
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
			lehrerListeManager: undefined,
			schuelerListeManager: undefined,
			kursListeManager: undefined,
			klassenListeManager: undefined,
		});
	}

	public async init(): Promise<void> {
		const statistikGesamt = await api.server.getStatistikGesamt(api.schema);
		const listeSchueler = await api.server.getSchuelerAuswahllisteFuerAbschnitt(api.schema, schuleStateImpl.abschnitt.id);
		const listeLehrer = await api.server.getLehrerFuerAbschnitt(api.schema, schuleStateImpl.abschnitt.id);

		// Lehrer-State
		await lehrerAuswahlStateImpl.init(schuleStateImpl.abschnitt.id, true);
		const lehrerListeManager = lehrerAuswahlStateImpl.manager;

		// Schüler-State
		await schuelerAuswahlStateImpl.init(schuleStateImpl.abschnitt.id, true);
		const schuelerListeManager = schuelerAuswahlStateImpl.manager;

		// Kurse-State
		await kurseAuswahlStateImpl.init(schuleStateImpl.abschnitt.id, true);
		const kursListeManager = kurseAuswahlStateImpl.manager;

		// Klassen-State
		await klassenAuswahlStateImpl.init(schuleStateImpl.abschnitt.id, true);
		const klassenListeManager = klassenAuswahlStateImpl.manager;

		const mapLehrer = new Map<number, LehrerListeEintrag>();
		const mapSchueler = new Map<number, SchuelerListeEintrag>();
		for (const s of listeSchueler.schueler) {
			mapSchueler.set(s.id, s);
		}
		for (const l of listeLehrer) {
			mapLehrer.set(l.id, l);
		}

		this.setPatchedState({ mapLehrer, mapSchueler, lehrerListeManager, schuelerListeManager, kursListeManager, klassenListeManager, statistikGesamt });
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

	public get lehrerListeManager(): LehrerListeManager {
		if (this.state.lehrerListeManager === undefined) {
			throw new DeveloperNotificationException("Der Manager wurde noch nicht initialisiert, es besteht keine Verbindung zum Server");
		}
		return this.state.lehrerListeManager;
	}

	public get kursListeManager(): KursListeManager {
		if (this.state.kursListeManager === undefined) {
			throw new DeveloperNotificationException("Der Manager wurde noch nicht initialisiert, es besteht keine Verbindung zum Server");
		}
		return this.state.kursListeManager;
	}

	public get klassenListeManager(): KlassenListeManager {
		if (this.state.klassenListeManager === undefined) {
			throw new DeveloperNotificationException("Der Manager wurde noch nicht initialisiert, es besteht keine Verbindung zum Server");
		}
		return this.state.klassenListeManager;
	}

	public get schuelerListeManager(): SchuelerListeManager {
		if (this.state.schuelerListeManager === undefined) {
			throw new DeveloperNotificationException("Der Manager wurde noch nicht initialisiert, es besteht keine Verbindung zum Server");
		}
		return this.state.schuelerListeManager;
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
		const lehrerListeManager = this.lehrerListeManager;
		const daten = await api.server.getLehrerStammdaten(api.schema, id);
		lehrerListeManager.setDaten(daten);
		const personaldaten = await api.server.getLehrerPersonaldaten(api.schema, id);
		lehrerListeManager.setPersonalDaten(personaldaten);
		this.setPatchedState({ lehrerListeManager });
	};

	public setSchueler = async (id: number) => {
		const schuelerListeManager = this.schuelerListeManager;
		const daten = await api.server.getSchuelerStammdaten(api.schema, id);
		schuelerListeManager.setDaten(daten);
		this.setPatchedState({ schuelerListeManager });
		schuelerAuswahlStateImpl.commit();
	};

	public setKurs = async (id: number) => {
		const kursListeManager = this.kursListeManager;
		const daten = await api.server.getKurs(api.schema, id);
		kursListeManager.setDaten(daten);
		this.setPatchedState({ kursListeManager });
	};

	public setKlasse = async (id: number) => {
		const klassenListeManager = this.klassenListeManager;
		const daten = await api.server.getKlasse(api.schema, id);
		klassenListeManager.setDaten(daten);
		this.setPatchedState({ klassenListeManager });
	};
}

export const statistikStateImpl = new StatistikStateImpl();
