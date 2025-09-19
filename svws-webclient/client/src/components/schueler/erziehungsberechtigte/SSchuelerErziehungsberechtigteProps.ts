import type { List, ErzieherStammdaten, Erzieherart, OrtKatalogEintrag, OrtsteilKatalogEintrag, BenutzerKompetenz, Schuljahresabschnitt } from "@core";

export interface SchuelerErziehungsberechtigteProps {
	data: () => List<ErzieherStammdaten>;
	patchErzieher: (data : Partial<ErzieherStammdaten>, id: number) => Promise<void>;
	patchErzieherAnPosition: (data : Partial<ErzieherStammdaten>, id: number, pos: number) => Promise<void>;
	addErzieher: (data: Partial<ErzieherStammdaten>, pos: number) => Promise<ErzieherStammdaten>;
	deleteErzieher: (idsEintraege: List<number>) => Promise<void>;
	mapErzieherarten: Map<number, Erzieherart>;
	mapOrte: Map<number, OrtKatalogEintrag>;
	mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	aktAbschnitt: Schuljahresabschnitt;
}
