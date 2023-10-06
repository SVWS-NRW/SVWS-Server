import type { GostBlockungKurs, GostBlockungKursLehrer, GostBlockungRegel, GostBlockungsdatenManager, GostBlockungsergebnisKurs, GostBlockungsergebnisManager, GostBlockungsergebnisSchiene, GostFaecherManager, GostKursart, GostStatistikFachwahl, LehrerListeEintrag } from "@core";
import type { Config } from "~/components/Config";
import type { GostKursplanungSchuelerFilter } from "../GostKursplanungSchuelerFilter";

export type SGostKursplanungKursansichtDragData = { kurs: GostBlockungKurs; schiene: GostBlockungsergebnisSchiene; fachId: number; } | undefined;

export interface SGostKursplanungKursansichtFachwahlProps {
	getDatenmanager: () => GostBlockungsdatenManager;
	getKursauswahl: () => Set<number>,
	getErgebnismanager: () => GostBlockungsergebnisManager;
	addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
	removeRegel: (id: number) => Promise<GostBlockungRegel | undefined>;
	updateKursSchienenZuordnung: (idKurs: number, idSchieneAlt: number, idSchieneNeu: number) => Promise<boolean>;
	patchKurs: (data: Partial<GostBlockungKurs>, kurs_id: number) => Promise<void>;
	addKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
	removeKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
	combineKurs: (kurs1 : GostBlockungKurs, fach2: GostBlockungKurs | GostBlockungsergebnisKurs | undefined | null) => Promise<void>;
	splitKurs: (kurs: GostBlockungKurs) => Promise<void>;
	addKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<GostBlockungKursLehrer | undefined>;
	removeKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<void>;
	addSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
	removeSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
	config: Config;
	hatErgebnis: boolean;
	schuelerFilter: GostKursplanungSchuelerFilter | undefined;
	fachwahlen: GostStatistikFachwahl;
	faecherManager: GostFaecherManager;
	fachwahlenAnzahl: number;
	kursart: GostKursart;
	mapLehrer: Map<number, LehrerListeEintrag>;
	allowRegeln: boolean;
	dragDataKursSchiene: () => SGostKursplanungKursansichtDragData;
	onDragKursSchiene: (data: SGostKursplanungKursansichtDragData) => void;
	onDropKursSchiene: (zone: SGostKursplanungKursansichtDragData) => Promise<void>;
}