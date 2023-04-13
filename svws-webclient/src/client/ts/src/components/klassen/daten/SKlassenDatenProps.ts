import type { KlassenDaten, LehrerListeEintrag, JahrgangsListeEintrag, Schueler} from "@svws-nrw/svws-core";
import { List } from "@svws-nrw/svws-core";

export interface KlassenDatenProps {
	patch: (data : Partial<KlassenDaten>) => Promise<void>;
	data: KlassenDaten,
	mapLehrer: Map<number, LehrerListeEintrag>,
	mapJahrgaenge: Map<number, JahrgangsListeEintrag>,
	gotoSchueler: (eintrag: Schueler) => Promise<void>,
}