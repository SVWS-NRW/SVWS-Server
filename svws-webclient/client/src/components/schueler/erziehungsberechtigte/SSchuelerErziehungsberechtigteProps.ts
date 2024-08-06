import type { List, ErzieherStammdaten, Erzieherart, OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@core";

export interface SchuelerErziehungsberechtigteProps {
	data: () => List<ErzieherStammdaten>;
	patch: (data : Partial<ErzieherStammdaten>, id: number) => Promise<void>;
	add: () => Promise<ErzieherStammdaten>;
	mapErzieherarten: Map<number, Erzieherart>;
	mapOrte: Map<number, OrtKatalogEintrag>;
	mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
}