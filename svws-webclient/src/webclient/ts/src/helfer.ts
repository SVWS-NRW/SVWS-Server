import {
	Erzieherart,
	KatalogEintrag,
	LehrerListeEintrag,
	List,
	Nationalitaeten,
	OrtKatalogEintrag,
	OrtsteilKatalogEintrag,
	Verkehrssprache
} from "@svws-nrw/svws-core-ts";

/** Die Sortierfunktion für den Ortskatalog */
export const orte_sort = (a: OrtKatalogEintrag, b: OrtKatalogEintrag): number => {
	if (a.ortsname == null) return -1;
	if (b.ortsname == null) return 1;
	return a.ortsname.localeCompare(String(b.ortsname), "de-DE");
};

/** Der Filter für Ortsteile */
export const orte_filter = (
	items: OrtKatalogEintrag[],
	search: string
): OrtKatalogEintrag[] => {
	// Teilmatch PLZ
	const plzmatch = search.match(/\d+/);
	const plz = plzmatch ? plzmatch[0] : undefined;
	//Teilmatch Ort
	const ortsname = search.replace(/\d+\s*/, "").trim();
	if (!plz && !ortsname) {
		return items;
	}
	if (!plz) {
		return items.filter((item: OrtKatalogEintrag) => {
			if (item && item.ortsname !== null) {
				return item.ortsname
					.toLocaleLowerCase("de-DE")
					.startsWith(ortsname.toLocaleLowerCase("de-DE"));
			}
			return false;
		});
	} else if (!ortsname) {
		return items.filter(item => {
			if (item.plz) {
				return item.plz.includes(plz);
			}
			return false;
		});
	} else {
		return items.filter(item => {
			if (item.plz && item.ortsname) {
				return (
					item.plz.includes(plz) &&
					item.ortsname
						.toLocaleLowerCase("de-DE")
						.startsWith(ortsname.toLocaleLowerCase("de-DE"))
				);
			}
			return false;
		});
	}
};

/** Filter für Staatsangehörigkeiten */
export const staatsangehoerigkeitKatalogEintragFilter = (
	items: Nationalitaeten[],
	search: string
) => {
	return items.filter((i: Nationalitaeten) => {
		if (i.daten.staatsangehoerigkeit) {
			return (
				i.daten.staatsangehoerigkeit
					.toLocaleLowerCase()
					.includes(search.toLocaleLowerCase()) ||
				i.daten.iso3
					.toLocaleLowerCase()
					.includes(search.toLocaleLowerCase())
			);
		}
		return [];
	});
};

/** Sortierfunktion für Staatsangehörigkeiten */
export const staatsangehoerigkeitKatalogEintragSort = (
	a: Nationalitaeten,
	b: Nationalitaeten
) => {
	if (a.daten.staatsangehoerigkeit && b.daten.staatsangehoerigkeit) {
		return a.daten.staatsangehoerigkeit.localeCompare(
			b.daten.staatsangehoerigkeit.toString()
		);
	} else if (a.daten.staatsangehoerigkeit && !b.daten.staatsangehoerigkeit) {
		return -1;
	} else if (!a.daten.staatsangehoerigkeit && !b.daten.staatsangehoerigkeit) {
		return 1;
	}
	return 0;
};

/** Filter für Sprachen */
export const verkehrsspracheKatalogEintragFilter = (items: Verkehrssprache[], search: string) => {
	if (!items.length)
		return [];
	return items.filter((i : Verkehrssprache) => {
		return (
			i.daten.kuerzel.toLocaleLowerCase().includes(search.toLocaleLowerCase()) ||
			i.daten.bezeichnung.toLocaleLowerCase().includes(search.toLocaleLowerCase())
		);
	});
};

/** Sortierfunktion für Sprachen */
export const verkehrsspracheKatalogEintragSort = (a: Verkehrssprache, b: Verkehrssprache) => { 
	return a.daten.bezeichnung.localeCompare(b.daten.bezeichnung.toString()); 
};

/** Filter für Länder */
export const nationalitaetenKatalogEintragFilter = (items: Nationalitaeten[], search: string) => {
	if (!items.length)
		return [];
	return items.filter((i: Nationalitaeten) => {
		return (
			i.daten.bezeichnung.toLocaleLowerCase().includes(search.toLocaleLowerCase()) ||
			i.daten.iso3.toLocaleLowerCase().includes(search.toLocaleLowerCase())
		);
	});
};

/** Sortierfunktion für Länder */
export const nationalitaetenKatalogEintragSort = (a: Nationalitaeten, b: Nationalitaeten) => {
	return a.daten.bezeichnung.localeCompare(b.daten.bezeichnung.toString());
};

export const katalogEintragSort = (
	a: KatalogEintrag | null,
	b: KatalogEintrag | null
) => {
	if (a?.text && b?.text) {
		return a?.text.localeCompare(b.text.toString());
	} else if (a && !b) {
		return -1;
	} else if (!a && !b) {
		return 1;
	}
	return 0;
};

export const katalogEintragFilter = (
	items: KatalogEintrag[] | undefined,
	search: string
) => {
	return items?.filter((i: KatalogEintrag) => {
		if (i.text && i.kuerzel) {
			i.text.toLocaleLowerCase().includes(search.toLocaleLowerCase()) ||
				i.kuerzel
					.toLocaleLowerCase()
					.includes(search.toLocaleLowerCase());
		}
		return false;
	});
};

export const ortsteilSort = (
	a: OrtsteilKatalogEintrag,
	b: OrtsteilKatalogEintrag
) => {
	if (a.ortsteil && b.ortsteil) {
		a.ortsteil.localeCompare(b.ortsteil.toString());
	} else if (a.ortsteil && !b.ortsteil) {
		return -1;
	} else if (!a.ortsteil && !b.ortsteil) {
		return 1;
	}
	return 0;
};

export const ortsteilFilter = (items: OrtsteilKatalogEintrag[], search: string) => {
	let o = [];
	for (const i of items) {
		if (i.ortsteil?.includes(search)) {
			o.push(i);
		}
	}
	return o;
}
export const erzieherArtSort = (a: Erzieherart, b: Erzieherart) => {
	if (a.bezeichnung && b.bezeichnung) {
		a.bezeichnung.localeCompare(b.bezeichnung.toString());
	} else if (a.bezeichnung && !b.bezeichnung) {
		return -1;
	} else if (!a.bezeichnung && !b.bezeichnung) {
		return 1;
	}
	return 0;
};

/** Der Filter für Lehrer mit Kürzelsuche */
export const lehrer_filter = (items: LehrerListeEintrag[], search: string): LehrerListeEintrag[] => {
	const name = search.replace(/\d+\s*/, "").trim();
	if (!name)
		return items;
	return items.filter((item: LehrerListeEintrag) =>
		item.kuerzel
			.toLocaleLowerCase("de-DE")
			.startsWith(name.toLocaleLowerCase("de-DE"))
		|| item.nachname
			.toLocaleLowerCase("de-DE")
			.startsWith(name.toLocaleLowerCase("de-DE"))
		|| item.vorname
			.toLocaleLowerCase("de-DE")
			.startsWith(name.toLocaleLowerCase("de-DE")))
};