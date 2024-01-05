import { type WritableComputedRef } from "vue";
import type { GostBlockungsdatenManager, GostBlockungsergebnisManager, GostBlockungRegel, GostBlockungSchiene, GostBlockungKurs, GostBlockungKursLehrer, GostFaecherManager, GostHalbjahr, LehrerListeEintrag, GostStatistikFachwahl, SchuelerListeEintrag, GostBlockungsergebnisKurs, GostJahrgangsdaten, ApiFile, JavaSet, List } from "@core";
import type { GostKursplanungSchuelerFilter } from "./GostKursplanungSchuelerFilter";
import type { Config } from "~/components/Config";

export interface GostKursplanungProps {
	getDatenmanager: () => GostBlockungsdatenManager;
	getKursauswahl: () => Set<number>,
	getErgebnismanager: () => GostBlockungsergebnisManager;
	addBlockung: () => Promise<void>;
	restoreBlockung: () => Promise<void>;
	patchRegel: (data: GostBlockungRegel, id: number) => Promise<void>;
	addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
	removeRegel: (id: number) => Promise<GostBlockungRegel | undefined>;
	addRegeln: (regeln: List<GostBlockungRegel>) => Promise<void>;
	removeRegeln: (regeln: List<GostBlockungRegel>) => Promise<void>;
	updateRegeln: (typ : string) => Promise<void>;
	updateKursSchienenZuordnung: (idKurs: number, idSchieneAlt: number, idSchieneNeu: number) => Promise<boolean>;
	patchSchiene: (data: Partial<GostBlockungSchiene>, id : number) => Promise<void>;
	addSchiene: () => Promise<GostBlockungSchiene | undefined>;
	removeSchiene: (s: GostBlockungSchiene) => Promise<GostBlockungSchiene | undefined>;
	patchKurs: (data: Partial<GostBlockungKurs>, kurs_id: number) => Promise<void>;
	addKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
	removeKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
	combineKurs: (kurs1 : GostBlockungKurs, fach2: GostBlockungKurs | GostBlockungsergebnisKurs | undefined | null) => Promise<void>;
	splitKurs: (kurs: GostBlockungKurs) => Promise<void>;
	addKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<GostBlockungKursLehrer | undefined>;
	removeKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<void>;
	addSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
	removeSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
	ergebnisAbleiten: () => Promise<void>;
	ergebnisHochschreiben: () => Promise<void>;
	ergebnisAktivieren: () => Promise<boolean>;
	ergebnisSynchronisieren: () => Promise<void>;
	getPDF: (title: string) => Promise<ApiFile>;
	jahrgangsdaten: () => GostJahrgangsdaten;
	kurssortierung: WritableComputedRef<'fach' | 'kursart'>;
	existiertSchuljahresabschnitt: boolean;
	config: Config;
	hatBlockung: boolean;
	hatErgebnis: boolean;
	schuelerFilter: () => GostKursplanungSchuelerFilter | undefined;
	faecherManager: GostFaecherManager;
	halbjahr: GostHalbjahr;
	mapLehrer: Map<number, LehrerListeEintrag>;
	mapFachwahlStatistik: () => Map<number, GostStatistikFachwahl>;
	mapSchueler: Map<number, SchuelerListeEintrag>;
	removeKursSchuelerZuordnung: (id: number, kurs: number) => Promise<boolean>;
}