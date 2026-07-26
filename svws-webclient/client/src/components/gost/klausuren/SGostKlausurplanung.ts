import type { GostKlausurraum, GostKlausurtermin, GostKursklausur, StundenplanZeitraster, GostSchuelerklausurtermin } from "@core";

export type GostKlausurplanungDragData = GostKursklausur | GostKlausurtermin | GostSchuelerklausurtermin | undefined;
export type GostKlausurplanungDropZone = GostKlausurtermin | GostKlausurraum | StundenplanZeitraster | undefined;