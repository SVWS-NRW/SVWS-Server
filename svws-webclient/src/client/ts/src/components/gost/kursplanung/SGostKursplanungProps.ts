import type { GostBlockungsdatenManager, GostBlockungsergebnisManager, GostBlockungRegel, GostBlockungSchiene, GostBlockungKurs, GostBlockungKursLehrer, GostFaecherManager, GostHalbjahr, LehrerListeEintrag, GostStatistikFachwahl, SchuelerListeEintrag } from "@svws-nrw/svws-core";
import type { Config } from "~/components/Config";
import type { GostKursplanungSchuelerFilter } from "./GostKursplanungSchuelerFilter";

export interface GostKursplanungProps {
	getDatenmanager: () => GostBlockungsdatenManager;
	getErgebnismanager: () => GostBlockungsergebnisManager;
	patchRegel: (data: GostBlockungRegel, id: number) => Promise<void>;
	addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
	removeRegel: (id: number) => Promise<GostBlockungRegel | undefined>;
	updateKursSchienenZuordnung: (idKurs: number, idSchieneAlt: number, idSchieneNeu: number) => Promise<boolean>;
	patchSchiene: (data: Partial<GostBlockungSchiene>, id : number) => Promise<void>;
	addSchiene: () => Promise<GostBlockungSchiene | undefined>;
	removeSchiene: (s: GostBlockungSchiene) => Promise<GostBlockungSchiene | undefined>;
	patchKurs: (data: Partial<GostBlockungKurs>, kurs_id: number) => Promise<void>;
	addKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
	removeKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
	combineKurs: (kurs1 : GostBlockungKurs, fach2: GostBlockungKurs) => Promise<void>;
	splitKurs: (kurs: GostBlockungKurs) => Promise<void>;
	addKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<GostBlockungKursLehrer | undefined>;
	removeKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<void>;
	addSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
	removeSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
	ergebnisHochschreiben: () => Promise<void>;
	ergebnisAktivieren: () => Promise<boolean>;
	config: Config;
	hatBlockung: boolean;
	hatErgebnis: boolean;
	schuelerFilter: GostKursplanungSchuelerFilter | undefined;
	faecherManager: GostFaecherManager;
	halbjahr: GostHalbjahr;
	mapLehrer: Map<number, LehrerListeEintrag>;
	mapFachwahlStatistik: Map<number, GostStatistikFachwahl>;
	mapSchueler: Map<number, SchuelerListeEintrag>;
}