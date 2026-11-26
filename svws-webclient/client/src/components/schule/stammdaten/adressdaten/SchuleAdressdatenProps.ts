import type { SchuleStammdaten } from "@core/asd/data/schule/SchuleStammdaten";
import type { Schulleitung } from "@core/asd/data/schule/Schulleitung";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { Teilstandort } from "@core/core/data/schule/Teilstandort";
import type { List } from "@core/java/util/List";

export interface SchuleAdressdatenProps {
	patch: (data: Partial<SchuleStammdaten>) => Promise<boolean>;
	getListTeilstandorte: () => List<Teilstandort>;
	addTeilstandorteintrag: (data: Partial<Teilstandort>) => Promise<void>;
	patchTeilstandorteintrag: (data: Partial<Teilstandort>, adrMerkmal: string) => Promise<boolean>;
	deleteTeilstandorteintraege: (adrMerkmale: List<string>) => Promise<void>;
	getListLeitungsfunktionen: () => List<Schulleitung>;
	getListLehrer: () => List<LehrerListeEintrag>;
	gotoLehrer: (idLehrer: number) => Promise<void>;
}
