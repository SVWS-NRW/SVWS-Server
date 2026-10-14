import type { InjectionKey } from "vue";

import type { ENMLehrerFunktion } from "@core/core/data/enm/ENMLehrerFunktion";
import type { ENMv2Klasse } from "@core/core/data/enm/v2/ENMv2Klasse";
import type { ENMv2Leistung } from "@core/core/data/enm/v2/ENMv2Leistung";
import type { ENMv2LeistungBemerkungen } from "@core/core/data/enm/v2/ENMv2LeistungBemerkungen";
import type { ENMv2Lernabschnitt } from "@core/core/data/enm/v2/ENMv2Lernabschnitt";
import type { ENMv2SchuelerAnkreuzkompetenz } from "@core/core/data/enm/v2/ENMv2SchuelerAnkreuzkompetenz";
import type { ENMv2Teilleistung } from "@core/core/data/enm/v2/ENMv2Teilleistung";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import type { List } from "@core/java/util/List";
import { AppContext } from "@ui/AppContext";
import type { EnmLerngruppenAuswahlEintrag, EnmManager } from "@ui/components/enm/EnmManager";

/**
 * Die Schnittstelle für den Zustand des Notenmoduls
 */
export interface NotenmodulState {

	ladeDaten(full?: boolean): Promise<void>;
	reset(): void;
	get auswahlAnsicht(): ENMLehrerFunktion;
	setAnsicht(funktion: ENMLehrerFunktion): Promise<void>;
	get optionsAuswahlAnsicht(): Set<ENMLehrerFunktion>;
	get idsLehrer(): List<number>;
	get manager(): EnmManager;
	get auswahlLerngruppe(): EnmLerngruppenAuswahlEintrag | null;
	setAuswahlLerngruppe: (value: EnmLerngruppenAuswahlEintrag | null) => void;
	get auswahlLerngruppen(): Array<EnmLerngruppenAuswahlEintrag>;
	setAuswahlLerngruppen: (value: Array<EnmLerngruppenAuswahlEintrag>) => void;
	get auswahlKlasse(): ENMv2Klasse | null;
	setAuswahlKlasse: (value: ENMv2Klasse | null) => void;
	get auswahlKlassen(): Array<ENMv2Klasse>;
	setAuswahlKlassen: (value: Array<ENMv2Klasse>) => void;
	get auswahlKlassenNurMehrfachauswahl(): Array<ENMv2Klasse>;
	get auswahlLerngruppenNurMehrfachauswahl(): Array<EnmLerngruppenAuswahlEintrag>;
	patchLeistung: (data: ENMv2Leistung, patch: Partial<ENMv2Leistung>) => Promise<void>;
	patchTeilleistung: (data: ENMv2Teilleistung, patch: Partial<ENMv2Teilleistung>) => Promise<void>;
	patchBemerkungen: (id: number, data: ENMv2LeistungBemerkungen, patch: Partial<ENMv2LeistungBemerkungen>) => Promise<void>;
	patchLernabschnitt: (data: ENMv2Lernabschnitt, patch: Partial<ENMv2Lernabschnitt>) => Promise<void>;
	patchAnkreuzkompetenz: (data: ENMv2SchuelerAnkreuzkompetenz, patch: Partial<ENMv2SchuelerAnkreuzkompetenz>) => Promise<void>;
	get hideAnkreuzkompetenzen(): boolean;
}

export const NotenmodulStateKey: InjectionKey<NotenmodulState> = Symbol('NotenmodulState');

export function useNotenmodulState(): NotenmodulState {
	const state = AppContext.instance.inject(NotenmodulStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des NotenmodulState über provide in der main.ts eingebunden");
	}
	return state;
}
