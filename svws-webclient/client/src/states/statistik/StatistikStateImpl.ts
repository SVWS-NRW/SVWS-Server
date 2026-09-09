
import { computed } from "vue";

import { StatistikGesamt } from "@core/asd/data/statistik/StatistikGesamt";
import { ValidatorGesamt } from "@core/asd/validate/ValidatorGesamt";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { StatistikState } from "@ui/states/statistik/StatistikState";
import { LehrerListeManager } from "@ui/ui/manager/lehrer/LehrerListeManager";
import { StateManager } from "@ui/ui/StateManager";

import { abschnittStateImpl } from "../AbschnittStateImpl";
import { SchuelerListeManager } from "../schueler/SchuelerListeManager";
import { schuleStateImpl } from "../SchuleStateImpl";
import { api } from "~/router/Api";


interface StatistikReactiveState {
	statistikGesamt: StatistikGesamt;
	mapLehrer: Map<number, LehrerListeEintrag>;
	mapSchueler: Map<number, SchuelerListeEintrag>;
	lehrerListeManager: LehrerListeManager;
	schuelerListeManager: SchuelerListeManager | undefined;
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
			lehrerListeManager: new LehrerListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
			schuelerListeManager: undefined, // new SchuelerListeManager(Schulform.BK, new SchuelerListe(), new ArrayList(), new ArrayList(), -1),
		});
	}

	public async init(): Promise<void> {
		const statistikGesamt = await api.server.getStatistikGesamt(api.schema);
		const listeSchueler = await api.server.getSchuelerAuswahllisteFuerAbschnitt(api.schema, schuleStateImpl.abschnitt.id);
		const listeLehrer = await api.server.getLehrerFuerAbschnitt(api.schema, schuleStateImpl.abschnitt.id);
		const lehrerListeManager = new LehrerListeManager(schuleStateImpl.abschnitt.id, schuleStateImpl.abschnitt.id, abschnittStateImpl.alle, schuleStateImpl.schulform, listeLehrer);
		const schuelerListeManager = new SchuelerListeManager(schuleStateImpl.schulform, listeSchueler, listeLehrer, abschnittStateImpl.alle, schuleStateImpl.abschnitt.id);
		const mapLehrer = new Map<number, LehrerListeEintrag>();
		const mapSchueler = new Map<number, SchuelerListeEintrag>();
		for (const s of listeSchueler.schueler) {
			mapSchueler.set(s.id, s);
		}
		for (const l of listeLehrer) {
			mapLehrer.set(l.id, l);
		}

		this.setPatchedState({ mapLehrer, mapSchueler, lehrerListeManager, schuelerListeManager, statistikGesamt });
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
		return this.state.lehrerListeManager;
	}

	public get schuelerListeManager(): SchuelerListeManager {
		if (this.state.schuelerListeManager === undefined) {
			throw new DeveloperNotificationException("Der Manager wurde noch nicht initialisiert, es besteht keine Verbindung zum Server");
		}
		return this.state.schuelerListeManager;
	}

	private _validatorGesamt = computed<ValidatorGesamt>(() => {
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
	};
}

export const statistikStateImpl = new StatistikStateImpl();
