import type { ErzieherStammdaten } from "@core/core/data/erzieher/ErzieherStammdaten";
import type { List } from "@core/java/util/List";

export interface SchuelerErziehungsberechtigteProps {
	data: () => List<ErzieherStammdaten>;
	patchErzieher: (data: Partial<ErzieherStammdaten>, id: number) => Promise<boolean>;
	patchErzieherAnPosition: (data: Partial<ErzieherStammdaten>, id: number, pos: number) => Promise<boolean>;
	addErzieher: (data: Partial<ErzieherStammdaten>, pos: number) => Promise<ErzieherStammdaten>;
	deleteErzieher: (idsEintraege: List<number>) => Promise<boolean>;
}
