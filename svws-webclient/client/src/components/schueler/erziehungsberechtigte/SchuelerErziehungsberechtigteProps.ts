import type { List, ErzieherStammdaten, Erzieherart } from "@core";

export interface SchuelerErziehungsberechtigteProps {
	data: () => List<ErzieherStammdaten>;
	patchErzieher: (data: Partial<ErzieherStammdaten>, id: number) => Promise<void>;
	patchErzieherAnPosition: (data: Partial<ErzieherStammdaten>, id: number, pos: number) => Promise<void>;
	addErzieher: (data: Partial<ErzieherStammdaten>, pos: number) => Promise<ErzieherStammdaten>;
	deleteErzieher: (idsEintraege: List<number>) => Promise<void>;
	erzieherartenById: Map<number, Erzieherart>;
}
