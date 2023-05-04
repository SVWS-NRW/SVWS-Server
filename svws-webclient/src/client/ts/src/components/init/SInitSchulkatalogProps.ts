import type { List, SchulenKatalogEintrag } from "@svws-nrw/svws-core";

export interface InitSchulkatalogProps {
	listSchulkatalog: List<SchulenKatalogEintrag>;
	initSchule: (schule: SchulenKatalogEintrag) => Promise<void>;
}