
import type {
	GostFaecherManager,
	GostJahrgangsdaten,
	GostKlausurraumManager,
	GostKlausurtermin,
	GostKursklausurManager,
	KursManager,
	LehrerListeEintrag,
	SchuelerListeEintrag,
	StundenplanManager,
} from "@core";
import type { WritableComputedRef } from "vue";

export interface GostKlausurplanungDetailAnsichtProps {
	jahrgangsdaten: GostJahrgangsdaten;
	kursklausurmanager: () => GostKursklausurManager;
	erzeugeKlausurraummanager: (termin: GostKlausurtermin) => Promise<GostKlausurraumManager>;
	faecherManager: GostFaecherManager;
	mapLehrer: Map<number, LehrerListeEintrag>;
	mapSchueler: Map<number, SchuelerListeEintrag>;
	kursmanager: KursManager;
	stundenplanmanager: StundenplanManager;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
}
