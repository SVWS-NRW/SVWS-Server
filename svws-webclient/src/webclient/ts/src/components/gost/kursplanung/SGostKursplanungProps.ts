import { GostBlockungsdatenManager, GostBlockungsergebnisManager, GostBlockungRegel, GostBlockungSchiene, GostBlockungKurs, GostBlockungKursLehrer, GostFaecherManager, GostHalbjahr, LehrerListeEintrag, GostStatistikFachwahl, SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
import { Config } from "~/components/Config";
import { GostKursplanungSchuelerFilter } from "./GostKursplanungSchuelerFilter";

export interface GostKursplanungProps {
	getDatenmanager: () => GostBlockungsdatenManager;
	getErgebnismanager: () => GostBlockungsergebnisManager;
	patchRegel: (data: Partial<GostBlockungRegel>, id: number) => Promise<void>;
	addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
	removeRegel: (id: number) => Promise<GostBlockungRegel | undefined>;
	updateKursSchienenZuordnung: (idKurs: number, idSchieneAlt: number, idSchieneNeu: number) => Promise<boolean>;
	patchSchiene: (data: Partial<GostBlockungSchiene>, id : number) => Promise<void>;
	addSchiene: () => Promise<GostBlockungSchiene | undefined>;
	removeSchiene: (s: GostBlockungSchiene) => Promise<GostBlockungSchiene | undefined>;
	patchKurs: (data: Partial<GostBlockungKurs>, kurs_id: number) => Promise<void>;
	addKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
	removeKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
	addKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<GostBlockungKursLehrer | undefined>;
	removeKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<void>;
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