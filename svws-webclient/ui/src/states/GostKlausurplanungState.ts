import type { InjectionKey } from "vue";
import type {
	GostJahrgangsdaten,
	GostKlausurraum,
	GostKlausurraumRich,
	GostKlausurterminblockungDaten,
	GostNachschreibterminblockungKonfiguration,
	GostSchuelerklausur,
	List,
	Schuljahresabschnitt,
} from "../../../core/src";
import type { GostKlausurvorgabe } from "../../../core/src/core/data/gost/klausuren/GostKlausurvorgabe";
import type { GostKursklausur } from "../../../core/src/core/data/gost/klausuren/GostKursklausur";
import type { GostKlausurtermin } from "../../../core/src/core/data/gost/klausuren/GostKlausurtermin";
import type { GostSchuelerklausurtermin } from "../../../core/src/core/data/gost/klausuren/GostSchuelerklausurtermin";
import type { GostKlausurenKlausurdaten } from "../../../core/src/core/data/gost/klausuren/GostKlausurenKlausurdaten";
import type { GostHalbjahr } from "../../../core/src/core/types/gost/GostHalbjahr";
import type { GostKlausurplanManager } from "../../../core/src/core/utils/gost/klausuren/GostKlausurplanManager";
import { DeveloperNotificationException } from "../../../core/src/core/exceptions/DeveloperNotificationException";
import { AppContext } from "../AppContext";

export const CONFIG_KEY_GOST_KLAUSURPLAN_PREFIX = "gost.klausuren.";

export interface GostKlausurplanungState {

	get abiturjahr(): number;
	get abschnitt(): Schuljahresabschnitt | undefined;
	get abschnittOrException(): Schuljahresabschnitt;
	get jahrgangsdaten(): GostJahrgangsdaten;
	get halbjahr(): GostHalbjahr;
	get manager(): GostKlausurplanManager;
	get zeigeAlleJahrgaenge(): boolean;
	get kalenderdatum(): string | undefined;
	get kalenderdatumOrException(): string;
	get selectedTermin(): GostKlausurtermin | undefined;
	get quartal(): 0 | 1 | 2;
	get kwWarnLimit(): number;
	get kwErrorLimit(): number;

	setAbiturjahr(abiturjahr: number | undefined): Promise<boolean>;
	setHalbjahr(halbjahr: GostHalbjahr, hjChanged: boolean): Promise<boolean>;
	reloadFehlendData(): Promise<void>;

	getConfigValue(key: string): string;
	getConfigNumberValue(key: string): number;
	setConfigValue(key: string, value: string | number): Promise<void>;
	setZeigeAlleJahrgaenge(value: boolean): void;
	setRaumTermin(termin: GostKlausurtermin | null): void;
	setKalenderdatum(value: string | undefined): void;
	setSelectedTermin(value: GostKlausurtermin | undefined): void;
	setQuartal(value: 0 | 1 | 2): void;
	setKwWarnLimit(value: number | null): void;
	setKwErrorLimit(value: number | null): void;

	erzeugeKlausurtermin(quartal: number, ht: boolean): Promise<GostKlausurtermin>;
	loescheKlausurtermine(termine: List<GostKlausurtermin>): Promise<void>;
	loescheKursklausuren(klausuren: List<GostKursklausur> | GostKursklausur[]): Promise<void>;
	erzeugeSchuelerklausuren(klausuren: List<Partial<GostSchuelerklausur>>): Promise<void>;
	loescheSchuelerklausuren(klausuren: List<GostSchuelerklausur>): Promise<void>;
	patchKlausur(klausur: GostKursklausur | GostSchuelerklausur | GostSchuelerklausurtermin, patch: Partial<GostKursklausur | GostSchuelerklausur | GostSchuelerklausurtermin>): Promise<void>;
	patchSchuelerklausurtermine(klausuren: List<GostSchuelerklausurtermin>, patch: Partial<GostSchuelerklausurtermin>): Promise<void>;
	erzeugeDefaultKlausurvorgaben(quartal: number): Promise<void>;
	erzeugeKlausurvorgabe(vorgabe: Partial<GostKlausurvorgabe>): Promise<void>;
	patchKlausurvorgabe(vorgabe: Partial<GostKlausurvorgabe>, id: number): Promise<void>;
	patchKlausurvorgaben(vorgaben: List<Partial<GostKlausurvorgabe>>): Promise<void>;
	loescheKlausurvorgaben(vorgaben: List<GostKlausurvorgabe>): Promise<void>;
	erzeugeKursklausurenAusVorgaben(quartal: number): Promise<GostKlausurenKlausurdaten>;
	patchKlausurtermin(id: number, termin: Partial<GostKlausurtermin>): Promise<void>;
	erzeugeVorgabenAusVorlage(quartal: number): Promise<void>;
	createKlausurraum(raum: Partial<GostKlausurraum>): Promise<void>;
	loescheKlausurraum(id: number): Promise<boolean>;
	patchKlausurraum(id: number, raum: Partial<GostKlausurraum>): Promise<boolean>;
	setzeRaumZuSchuelerklausuren(rRaeume: List<GostKlausurraumRich>, deleteFromRaeume: boolean): Promise<void>;
	blockenKursklausuren(blockungDaten: GostKlausurterminblockungDaten): Promise<void>;
	blockenNachschreiber(config: GostNachschreibterminblockungKonfiguration): Promise<void>;
	createSchuelerklausurtermin(skt: Partial<GostSchuelerklausurtermin>): Promise<void>;

}

export const GostKlausurplanungStateKey: InjectionKey<GostKlausurplanungState> = Symbol('GostKlausurplanungState');

export function useGostKlausurplanungState(): GostKlausurplanungState {
	const state = AppContext.instance.inject(GostKlausurplanungStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurde keine Instanz des GostKlausurplanungState über provide in der main.ts eingebunden");
	}
	return state;
}
