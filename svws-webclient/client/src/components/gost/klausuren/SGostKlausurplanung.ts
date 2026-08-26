import type { GostKlausurraum, GostKlausurtermin, GostKursklausur, List, StundenplanZeitraster, GostSchuelerklausurtermin } from "@core";

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
