import type { SchuelerFoerderempfehlung } from "@core/asd/data/schueler/SchuelerFoerderempfehlung";
import type { List } from "@core/java/util/List";

export interface SchuelerLernabschnittFoerderempfehlungenProps {
	foerderempfehlungen: () => List<SchuelerFoerderempfehlung>;
	add: (data: Partial<SchuelerFoerderempfehlung>) => Promise<void>;
	patch: (data: Partial<SchuelerFoerderempfehlung>, guid: string) => Promise<boolean>;
	delete: (guIDs: List<string>) => Promise<void>;
}
