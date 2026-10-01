import type { SchuelerFoerderempfehlung } from "@core/asd/data/schueler/SchuelerFoerderempfehlung";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { List } from "@core/java/util/List";

export interface SchuelerLernabschnittFoerderempfehlungenProps {
	foerderempfehlungen: () => List<SchuelerFoerderempfehlung>;
	lehrer: () => List<LehrerListeEintrag>,
	add: (data: Partial<SchuelerFoerderempfehlung>) => Promise<void>;
	patch: (data: Partial<SchuelerFoerderempfehlung>, guid: string) => Promise<boolean>;
	delete: (guIDs: List<string>) => Promise<void>;
}
