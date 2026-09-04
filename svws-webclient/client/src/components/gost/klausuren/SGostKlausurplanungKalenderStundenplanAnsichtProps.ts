import type { GostKlausurtermin } from "@core/core/data/gost/klausuren/GostKlausurtermin";
import type { GostKursklausur } from "@core/core/data/gost/klausuren/GostKursklausur";
import type { StundenplanKalenderwochenzuordnung } from "@core/core/data/stundenplan/StundenplanKalenderwochenzuordnung";
import type { StundenplanZeitraster } from "@core/core/data/stundenplan/StundenplanZeitraster";
import type { GostHalbjahr } from "@core/core/types/gost/GostHalbjahr";
import type { Wochentag } from "@core/core/types/Wochentag";
import type { List } from "@core/java/util/List";
import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";

export interface SGostKlausurplanungKalenderStundenplanAnsichtProps {
	mode?: 'schueler' | 'lehrer' | 'klasse';
	ignoreEmpty?: boolean;
	id: number;
	abschnittId: number;
	kalenderwoche: (datum?: string) => StundenplanKalenderwochenzuordnung;
	wochentyp: () => number;
	// kalenderwoche: () => StundenplanKalenderwochenzuordnung | undefined;
	useDragAndDrop?: boolean;
	kurseGefiltert: (datum: string, day: Wochentag, stunde: number) => List<GostKursklausur>;
	sumSchreiber: (datum: string, day: Wochentag, stunde: number) => number;
	dragData: () => GostKlausurplanungDragData;
	onDrag: (data: GostKlausurplanungDragData) => void;
	onDrop: (zone: GostKlausurplanungDropZone) => void;
	checkDropZoneZeitraster: (event: DragEvent, zeitraster: StundenplanZeitraster | undefined) => void;
	kursklausurMouseOver: () => GostKursklausur | undefined;
	gotoKalenderdatum: (datum: string | undefined, termin: GostKlausurtermin | undefined) => Promise<void>;
	gotoRaumzeitTermin: (abiturjahr: number, halbjahr: GostHalbjahr, value: number) => Promise<void>;
}
