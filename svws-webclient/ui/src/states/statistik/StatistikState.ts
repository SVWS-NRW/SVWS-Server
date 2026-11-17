import type { InjectionKey } from "vue";

import type { StatistikGesamt } from "@core/asd/data/statistik/StatistikGesamt";
import type { ZusatzerhebungenStatistikGesamt } from "@core/asd/data/statistik/ZusatzerhebungenStatistikGesamt";
import type { ValidatorGesamt } from "@core/asd/validate/ValidatorGesamt";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { AppContext } from "@ui/AppContext";



/**
 * Die Schnittstelle für den Zustand der Statistikprüfung
 */
export interface StatistikState {

	/**
	 * Gibt die Gesamtstatistik zurück
	 *
	 * @returns die Gesamtstatistik
	 */
	get statistikGesamt(): StatistikGesamt;

	get validatorGesamt(): ValidatorGesamt;

	setLehrer: (id: number) => Promise<void>;

	setSchueler: (id: number) => Promise<void>;

	setKurs: (id: number) => Promise<void>;

	setKlasse: (id: number) => Promise<void>;

	get mapSchueler(): Map<number, SchuelerListeEintrag>;

	/**
	 * Patcht die Daten der Zusatzerhebungen für den Schuljahresabschnitt der Statistik und übernimmt
	 * die vom Server zurückgegebenen Daten in die Gesamtstatistik.
	 *
	 * @param data   die zu ändernden Daten
	 *
	 * @returns true, wenn der Patch erfolgreich war
	 */
	patchZusatzerhebungen: (data: Partial<ZusatzerhebungenStatistikGesamt>) => Promise<boolean>;
}

export const StatistikStateKey: InjectionKey<StatistikState> = Symbol('StatistikState');

export function useStatistikState(): StatistikState {
	const state = AppContext.instance.inject(StatistikStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des StatistikState über provide in der main.ts eingebunden");
	}
	return state;
}
