import type { BenutzerKompetenz, List, SchuleStammdaten, ServerMode, Teilstandort } from "@core";

export interface SchuleAdressdatenProps {
	schule: () => SchuleStammdaten;
	patch: (data: Partial<SchuleStammdaten>) => Promise<void>;
	getListTeilstandorte: () => List<Teilstandort>;
	addTeilstandorteintrag: (data: Partial<Teilstandort>) => Promise<void>;
	patchTeilstandorteintrag: (data: Partial<Teilstandort>, adrMerkmal: string) => Promise<void>;
	deleteTeilstandorteintraege: (adrMerkmale: List<string>) => Promise<void>;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	benutzerIstAdmin: boolean;
}
