import type { Lernplattform } from "@core/core/data/schule/Lernplattform";
import type { List } from "@core/java/util/List";

export interface SchuleDatenaustauschLernplattformenProps {
	lernplattformen: List<Lernplattform>,
	export: (lernplattform: Lernplattform, datenformat: string) => Promise<Blob | null>,
}

