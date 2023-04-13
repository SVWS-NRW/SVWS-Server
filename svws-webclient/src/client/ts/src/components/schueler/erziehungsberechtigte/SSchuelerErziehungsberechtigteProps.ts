import type { List, ErzieherStammdaten, Erzieherart, OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@svws-nrw/svws-core";

export interface SchuelerErziehungsberechtigteProps {
	data: List<ErzieherStammdaten>;
	patch: (data : Partial<ErzieherStammdaten>, id: number) => Promise<void>;
	mapErzieherarten: Map<number, Erzieherart>;
	mapOrte: Map<number, OrtKatalogEintrag>;
	mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
}