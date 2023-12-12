import type { SchulEintrag } from "@core";

export interface SchuleDatenProps {
	patch: (data : Partial<SchulEintrag>) => Promise<void>;
	mapKatalogeintraege: Map<number, SchulEintrag>;
}