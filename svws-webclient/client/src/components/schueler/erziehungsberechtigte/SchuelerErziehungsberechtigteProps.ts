import type { List, ErzieherStammdaten, Erzieherart, OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@core";

export interface SchuelerErziehungsberechtigteProps {
	data: () => List<ErzieherStammdaten>;
	patchErzieher: (data: Partial<ErzieherStammdaten>, id: number) => Promise<void>;
	patchErzieherAnPosition: (data: Partial<ErzieherStammdaten>, id: number, pos: number) => Promise<void>;
	addErzieher: (data: Partial<ErzieherStammdaten>, pos: number) => Promise<ErzieherStammdaten>;
	deleteErzieher: (idsEintraege: List<number>) => Promise<void>;
	erzieherartenById: Map<number, Erzieherart>;
	orteById: Map<number, OrtKatalogEintrag>;
	ortsteileById: Map<number, OrtsteilKatalogEintrag>;
}
