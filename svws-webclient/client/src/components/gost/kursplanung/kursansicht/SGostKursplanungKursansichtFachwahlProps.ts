import type { ComputedRef } from "vue";
import type { GostKursplanungSchuelerFilter } from "../GostKursplanungSchuelerFilter";
import type { GostBlockungsergebnisSchiene, GostBlockungKurs, GostBlockungKursLehrer, GostBlockungRegelUpdate, GostBlockungSchiene, GostBlockungsdatenManager, GostBlockungsergebnisKurs, GostBlockungsergebnisManager, GostFaecherManager, GostKursart, GostStatistikFachwahl, JavaSet, LehrerListeEintrag } from "@core";
import type { ApiStatus } from "~/components/ApiStatus";

export interface SGostKursplanungKursansichtFachwahlProps {
	getDatenmanager: () => GostBlockungsdatenManager;
	getKursauswahl: () => JavaSet<number>,
	getErgebnismanager: () => GostBlockungsergebnisManager;
	regelnUpdate: (update: GostBlockungRegelUpdate) => Promise<void>;
	updateKursSchienenZuordnung: (idKurs: number, idSchieneAlt: number, idSchieneNeu: number) => Promise<boolean>;
	patchKurs: (data: Partial<GostBlockungKurs>, kurs_id: number) => Promise<void>;
	addKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
	removeKurse: (ids: Iterable<number>) => Promise<void>;
	combineKurs: (kurs1 : GostBlockungKurs, fach2: GostBlockungKurs | GostBlockungsergebnisKurs | undefined | null) => Promise<void>;
	splitKurs: (kurs: GostBlockungKurs) => Promise<void>;
	addKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<GostBlockungKursLehrer | undefined>;
	removeKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<void>;
	addSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
	removeSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
	hatErgebnis: boolean;
	schuelerFilter: () => GostKursplanungSchuelerFilter;
	fachwahlen: GostStatistikFachwahl;
	faecherManager: GostFaecherManager;
	fachwahlenAnzahl: number;
	kursart: GostKursart;
	mapLehrer: Map<number, LehrerListeEintrag>;
	allowRegeln: boolean;
	apiStatus: ApiStatus;
	kursdetailAnzeigen: number | undefined;
	setKursdetailAnzeigen: (value: number|undefined) => void;
	// Drag and Drop
	setDrag: {
    (kurs: GostBlockungKurs): void;
    (schiene: GostBlockungsergebnisSchiene): void;
    (kurs: GostBlockungKurs, schiene: GostBlockungsergebnisSchiene): void;
	};
	setDragOver: (kurs: GostBlockungKurs, schiene: GostBlockungsergebnisSchiene) => void;
	setDrop: {
    (kurs: GostBlockungKurs): void;
    (schiene: GostBlockungsergebnisSchiene): void;
    (kurs: GostBlockungKurs, schiene: GostBlockungsergebnisSchiene): void;
	};
	resetDrag: () => void;
	resetDrop: () => void;
	highlightKursVerschieben: (kurs: GostBlockungKurs) => ComputedRef<boolean>;
	highlightRechteck: (kurs: GostBlockungKurs, schiene: GostBlockungsergebnisSchiene) => ComputedRef<boolean>;
	highlightRechteckDrop: (kurs: GostBlockungKurs, schiene: GostBlockungsergebnisSchiene) => ComputedRef<boolean>;
	highlightKursAufAnderenKurs: (kurs: GostBlockungKurs, schiene: GostBlockungsergebnisSchiene) => ComputedRef<boolean>;
	isDragging: boolean;
	showTooltip: {kursID: number, schieneID: number};
	kurseUndSchienenInRechteck: [JavaSet<number>, JavaSet<number>, JavaSet<number>|null] | null;
}