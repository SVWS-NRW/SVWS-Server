
import type {
	GostFaecherManager,
	GostJahrgangsdaten,
	GostKlausurtermin,
	GostKursklausurManager,
	KursManager,
	LehrerListeEintrag,
	StundenplanManager,
} from "@core";
import type { WritableComputedRef } from "vue";

export interface GostKlausurplanungKalenderProps {
	jahrgangsdaten: GostJahrgangsdaten;
	kursklausurmanager: () => GostKursklausurManager;
	faecherManager: GostFaecherManager;
	mapLehrer: Map<number, LehrerListeEintrag>;
	kursmanager: KursManager;
	patchKlausurtermin: (id: number, termin: Partial<GostKlausurtermin>) => Promise<boolean>;
	stundenplanmanager: StundenplanManager;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
}
