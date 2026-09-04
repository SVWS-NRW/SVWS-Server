import type { SchuelerLernplattform } from "@core/core/data/schueler/SchuelerLernplattform";
import type { Lernplattform } from "@core/core/data/schule/Lernplattform";
import type { List } from "@core/java/util/List";
import type { ApiStatus } from "~/components/ApiStatus";

export interface SchuelerLernplattformenProps {
	schuelerLernplattformen: () => List<SchuelerLernplattform>;
	mapLernplattformen: Map<number, Lernplattform>;
	patch: (data: Partial<SchuelerLernplattform>, idLernplattform: number) => Promise<boolean>;
	apiStatus: ApiStatus;
}
