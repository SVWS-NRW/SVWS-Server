import type { Erzieherart, KatalogEintrag, LehrerListeEintrag, Nationalitaeten, OrtKatalogEintrag, OrtsteilKatalogEintrag, Verkehrssprache, CoreTypeData,
	SchulenKatalogEintrag, SchulEintrag } from "@core";
import { Schulform } from "@core";


/** Die Sortierfunktion für den Ortskatalog */
export function orte_sort(a: OrtKatalogEintrag, b: OrtKatalogEintrag): number {
	if (a.ortsname === null)
		return -1;
	if (b.ortsname === null)
		return 1;
	return a.ortsname.localeCompare(b.ortsname, "de-DE");
}

/** Der Filter für Ortsteile */
export function orte_filter(items: OrtKatalogEintrag[], search: string): OrtKatalogEintrag[] {
	// Teilmatch PLZ
	const plzmatch = /\d+/.exec(search);
	const plz = plzmatch ? plzmatch[0] : "";
	//Teilmatch Ort
	const ortsname = search.replace(/\d+\s*/, "").trim();
	if ((plz.length === 0) && (ortsname.length === 0))
		return items;
	if (plz.length === 0)
		return items.filter((item: OrtKatalogEintrag) => {
			if (item.ortsname !== null)
				return item.ortsname.toLocaleLowerCase("de-DE").startsWith(ortsname.toLocaleLowerCase("de-DE"));
			return false;
		});
	else if (ortsname.length === 0)
		return items.filter(item => {
			if (item.plz !== null)
				return item.plz.includes(plz);
			return false;
		});
	else
		return items.filter(item => {
			if ((item.plz !== null) && (item.ortsname !== null))
				return (item.plz.includes(plz) && item.ortsname.toLocaleLowerCase("de-DE").startsWith(ortsname.toLocaleLowerCase("de-DE")));
			return false;
		});
}

/** Filter für Staatsangehörigkeiten */
export function staatsangehoerigkeitKatalogEintragFilter(items: Iterable<Nationalitaeten>, search: string) {
	const list = [];
	for (const i of items)
		if (i.historie().getLast().staatsangehoerigkeit.toLocaleLowerCase().includes(search.toLocaleLowerCase())
			|| i.historie().getLast().iso3.toLocaleLowerCase().includes(search.toLocaleLowerCase()))
			list.push(i);
	return list;
}

/** Sortierfunktion für Staatsangehörigkeiten */
export function staatsangehoerigkeitKatalogEintragSort(a: Nationalitaeten, b: Nationalitaeten) {
	const va = a.historie().getLast().staatsangehoerigkeit;
	const vb = b.historie().getLast().staatsangehoerigkeit;
	if ((va.length > 0) && (vb.length > 0))
		return va.localeCompare(vb);
	else if ((va.length > 0) && (vb.length === 0))
		return -1;
	else if ((va.length === 0) && (vb.length === 0))
		return 1;
	return 0;
}

/** Filter für Sprachen */
export function verkehrsspracheKatalogEintragFilter(items: Iterable<Verkehrssprache>, search: string) {
	const list = [];
	for (const i of items)
		if (i.daten.kuerzel.toLocaleLowerCase().includes(search.toLocaleLowerCase())
			|| i.daten.bezeichnung.toLocaleLowerCase().includes(search.toLocaleLowerCase()))
			list.push(i);
	return list;
}

/** Sortierfunktion für Sprachen */
export function verkehrsspracheKatalogEintragSort(a: Verkehrssprache, b: Verkehrssprache) {
	return a.daten.bezeichnung.localeCompare(b.daten.bezeichnung);
}

/** Filter für Länder */
export function nationalitaetenKatalogEintragFilter(items: Iterable<Nationalitaeten>, search: string) {
	const list = [];
	for (const i of items)
		if (i.historie().getLast().bezeichnung.toLocaleLowerCase().includes(search.toLocaleLowerCase())
			|| i.historie().getLast().iso3.toLocaleLowerCase().includes(search.toLocaleLowerCase()))
			list.push(i);
	return list;
}

/** Sortierfunktion für Länder */
export function nationalitaetenKatalogEintragSort(a: Nationalitaeten, b: Nationalitaeten) {
	return a.historie().getLast().bezeichnung.localeCompare(b.historie().getLast().bezeichnung);
}

export function katalogEintragSort(a: KatalogEintrag | null, b: KatalogEintrag | null) {
	if ((typeof a?.text === 'string') && (typeof b?.text === 'string'))
		return a.text.localeCompare(b.text);
	else if (a && !b)
		return -1;
	else if (!a && !b)
		return 1;
	return 0;
}

export function katalogEintragFilter(items: Iterable<KatalogEintrag> | undefined, search: string) {
	const list: KatalogEintrag[] = [];
	if (items === undefined)
		return list;
	for (const i of items)
		if ((i.text !== null) && (i.kuerzel !== null)
			&& (i.text.toLocaleLowerCase().includes(search.toLocaleLowerCase())
				|| i.kuerzel
					.toLocaleLowerCase()
					.includes(search.toLocaleLowerCase())))
			list.push(i);
	return list;
}

export function ortsteilSort(a: OrtsteilKatalogEintrag, b: OrtsteilKatalogEintrag) {
	if ((a.ortsteil !== null) && (b.ortsteil !== null))
		a.ortsteil.localeCompare(b.ortsteil);
	else if ((a.ortsteil !== null) && (b.ortsteil === null))
		return -1;
	else if ((a.ortsteil === null) && (b.ortsteil === null))
		return 1;
	return 0;
}

export function ortsteilFilter(items: Iterable<OrtsteilKatalogEintrag>, search: string) {
	const o = [];
	for (const i of items)
		if ((i.ortsteil !== null) && i.ortsteil.includes(search))
			o.push(i);
	return o;
}

export function erzieherArtSort(a: Erzieherart, b: Erzieherart) {
	if ((a.bezeichnung !== null) && (b.bezeichnung !== null))
		a.bezeichnung.localeCompare(b.bezeichnung);
	else if ((a.bezeichnung !== null) && (b.bezeichnung === null))
		return -1;
	else if ((a.bezeichnung === null) && (b.bezeichnung === null))
		return 1;
	return 0;
}

/** Der Filter für Lehrer mit Kürzelsuche */
export function lehrer_filter(items: Iterable<LehrerListeEintrag>, search: string): LehrerListeEintrag[] {
	const name = search.replace(/\d+\s*/, "").trim();
	if (name.length === 0)
		return Array.isArray(items) ? items : [...items];
	const list = [];
	for (const i of items)
		if (i.kuerzel.toLocaleLowerCase("de-DE").startsWith(name.toLocaleLowerCase("de-DE"))
			|| i.nachname.toLocaleLowerCase("de-DE").startsWith(name.toLocaleLowerCase("de-DE"))
			|| i.vorname.toLocaleLowerCase("de-DE").startsWith(name.toLocaleLowerCase("de-DE")))
			list.push(i);
	return list;
}

/** Der Filter für SchulenKatalogEinträge */
export function filterSchulenKatalogEintraege(items: SchulenKatalogEintrag[], search: string) : SchulenKatalogEintrag[] {
	const searchLower = search.toLocaleLowerCase()
	const list = [];
	for (const i of items)
		if ((i.SchulNr.includes(searchLower))
				|| ((i.KurzBez !== null) && i.KurzBez.toLowerCase().includes(searchLower))
				|| ((i.ABez1 !== null) && i.ABez1.toLowerCase().includes(searchLower))
				|| ((i.Ort !== null) && i.Ort.toLowerCase().includes(searchLower)))
			list.push(i);
	return list;
}

/** Die Filter-Methode der Schuleeinträge */
export function filterSchulenEintraege(items: SchulEintrag[], search: string) : SchulEintrag[] {
	const searchLower = search.toLowerCase()
	const list = [];
	for (const i of items) {
		const schulform = Schulform.data().getEintragByID(i.idSchulform ?? -1);
		if (((schulform !== null) && schulform.text.toLowerCase().includes(searchLower))
				||((i.schulnummerStatistik !== null) && i.schulnummerStatistik.includes(searchLower))
				|| ((i.schulnummerStatistik !== null) && i.schulnummerStatistik.includes(searchLower))
				|| ((i.kurzbezeichnung !== null) && i.kurzbezeichnung.toLowerCase().includes(searchLower))
				|| i.name.toLowerCase().includes(searchLower) || ((i.ort !== null) && i.ort.toLowerCase().includes(searchLower))
				|| ((i.kuerzel !== null) && i.kuerzel.toLowerCase().includes(searchLower))
				|| ((i.plz !== null) && i.plz.toLowerCase().includes(searchLower)))
			list.push(i);
	}
	return list;
}

/** Filter für CoreType */
export function coreTypeDataFilter(items: CoreTypeData[], search: string): CoreTypeData[] {
	const searchLower = search.trim().toLowerCase();
	return items.filter(item => item.schluessel.toLowerCase().includes(searchLower) || item.text.toLowerCase().includes(searchLower)
			|| item.kuerzel.toLowerCase().includes(searchLower));
}

/** Formatiert ein Datum in das Format 01.01.2020 */
export function formatDate(dateString: string | null): string {
	if (dateString === null)
		return "";
	return new Date(dateString).toLocaleDateString("de-DE");
}
