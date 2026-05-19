import type { BenutzerKompetenz, List, SchuleStammdaten, Teilstandort } from "@core";

export interface SchuleAdressdatenProps {
	patch: (data: Partial<SchuleStammdaten>) => Promise<void>;
	getListTeilstandorte: () => List<Teilstandort>;
	addTeilstandorteintrag: (data: Partial<Teilstandort>) => Promise<void>;
	patchTeilstandorteintrag: (data: Partial<Teilstandort>, adrMerkmal: string) => Promise<void>;
	deleteTeilstandorteintraege: (adrMerkmale: List<string>) => Promise<void>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	benutzerIstAdmin: boolean;
}
