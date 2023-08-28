import type { GostKlausurraum, GostKlausurtermin, GostKursklausur, StundenplanZeitraster } from "@core";

export type GostKlausurplanungDragData = GostKursklausur | GostKlausurtermin | undefined;
export type GostKlausurplanungDropZone = GostKlausurtermin | GostKlausurraum | StundenplanZeitraster | undefined;