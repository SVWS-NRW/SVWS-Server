import type { SchuelerVermerke } from "@core/core/data/schueler/SchuelerVermerke";
import type { VermerkartEintrag } from "@core/core/data/schule/VermerkartEintrag";
import type { List } from "@core/java/util/List";
import type { ApiStatus } from "~/components/ApiStatus";

export interface SchuelerVermerkeProps {
	schuelerVermerke: () => List<SchuelerVermerke>;
	mapVermerkArten: Map<number, VermerkartEintrag>;
	patch: (data: Partial<SchuelerVermerke>, idVermerk: number) => Promise<boolean>;
	add: () => Promise<void>;
	remove: (idVermerk: number) => Promise<void>;
	apiStatus: ApiStatus;
	autofocus: boolean;
	filterNurSichtbare: boolean,
	setFilterNurSichtbare: (value: boolean) => Promise<void>,
}
