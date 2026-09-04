import type { GostKlausurraum } from "@core/core/data/gost/klausuren/GostKlausurraum";
import type { GostKlausurtermin } from "@core/core/data/gost/klausuren/GostKlausurtermin";
import type { GostKursklausur } from "@core/core/data/gost/klausuren/GostKursklausur";
import type { GostSchuelerklausurtermin } from "@core/core/data/gost/klausuren/GostSchuelerklausurtermin";
import type { StundenplanZeitraster } from "@core/core/data/stundenplan/StundenplanZeitraster";
import type { List } from "@core/java/util/List";

export interface GostNachschreiberDragData {
	type: "nachschreiber";
	items: List<GostSchuelerklausurtermin>;
}

export type GostKlausurplanungDragData = GostKursklausur | GostKlausurtermin | GostSchuelerklausurtermin | GostNachschreiberDragData | undefined;
export type GostKlausurplanungDropZone = GostKlausurtermin | GostKlausurraum | StundenplanZeitraster | undefined;

export function isGostNachschreiberDragData(data: GostKlausurplanungDragData): data is GostNachschreiberDragData {
	return (data !== undefined)
		&& (typeof data === "object")
		&& ((data as { type?: unknown }).type === "nachschreiber");
}
