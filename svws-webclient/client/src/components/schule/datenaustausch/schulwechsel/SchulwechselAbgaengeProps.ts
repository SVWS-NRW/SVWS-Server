import type { SchulwechselAbgang } from "@core/core/data/schule/SchulwechselAbgang.ts";
import type { JavaMap } from "@core/java/util/JavaMap.ts";
import type { List } from "@core/java/util/List.ts";


export interface SchulwechselAbgaengeProps {
	abgaenge: () => List<SchulwechselAbgang>;
	abgaengeAuswahl: () => JavaMap<number, SchulwechselAbgang>;
}
