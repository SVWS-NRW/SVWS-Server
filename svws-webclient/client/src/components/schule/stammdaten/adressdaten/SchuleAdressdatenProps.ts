import type { SchuleStammdaten } from "@core/asd/data/schule/SchuleStammdaten";
import type { Teilstandort } from "@core/core/data/schule/Teilstandort";
import type { List } from "@core/java/util/List";

export interface SchuleAdressdatenProps {
	patch: (data: Partial<SchuleStammdaten>) => Promise<void>;
	getListTeilstandorte: () => List<Teilstandort>;
	addTeilstandorteintrag: (data: Partial<Teilstandort>) => Promise<void>;
	patchTeilstandorteintrag: (data: Partial<Teilstandort>, adrMerkmal: string) => Promise<void>;
	deleteTeilstandorteintraege: (adrMerkmale: List<string>) => Promise<void>;
}
