<template>
	<div v-if="ladeFehler" class="page p-4">
		<p>Die Leitungsdaten konnten für die Notenübersicht nicht geladen werden.</p>
	</div>
	<div v-else-if="managerNotenuebersicht === null" class="page p-4">
		<p>Die Notenübersicht wird geladen …</p>
	</div>
	<div v-else-if="berechtigt" class="page page-flex-col gap-4 p-4">
		<div v-if="punkteToggleSichtbar" class="flex items-center justify-end gap-2 text-black">
			<span>Noten</span>
			<svws-ui-checkbox v-model="punkteAnsicht" type="toggle">Punkte</svws-ui-checkbox>
		</div>
		<div class="flex flex-wrap items-end justify-between gap-4">
			<div>
				<h1 class="text-headline-lg">Notenübersicht</h1>
				<p class="text-ui-secondary">Fächer und Teilleistungen der Schüler in den Klassen, für die Sie berechtigt sind.</p>
			</div>
			<div class="w-full max-w-lg">
				<svws-ui-select title="Nach Vor- oder Nachname suchen" :model-value="auswahlSchueler" :items="schuelerListe"
					:item-text="schuelerAnzeige" :item-filter="filterSchueler" autocomplete removable
					@update:model-value="value => auswahlSchueler = value ?? null" />
			</div>
		</div>
		<svws-ui-table :items="eintraegeGefiltert" :columns="spalten" :no-data-text="auswahlSchueler ? 'Für diesen Schüler sind keine Teilleistungen vorhanden.' : 'Keine Teilleistungen vorhanden.'" scroll>
			<template #cell="{ value, column, rowData }">
				<template v-if="istNotenspalte(column.key)">
					<div class="w-full text-center">
						<svws-ui-badge v-if="getNotenBadge(column.key, rowData, value) !== null"
							type="light"
							class="!mx-auto !text-base"
							:style="getNotenBadge(column.key, rowData, value) === 'gelb' ? badgeGelbStyle : badgeRotStyle">
							{{ value }}
						</svws-ui-badge>
						<template v-else>{{ value }}</template>
					</div>
				</template>
				<span v-else-if="column.key === 'schueler'" class="whitespace-nowrap">{{ value }}</span>
				<template v-else>{{ value }}</template>
			</template>
		</svws-ui-table>
	</div>
	<div v-else class="page p-4">
		<p>Für diese Übersicht ist eine Klassen- oder Abteilungsleitungsberechtigung erforderlich.</p>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, shallowRef, watch } from 'vue';

	import { ENMLehrerFunktion } from '@core/core/data/enm/ENMLehrerFunktion';
	import type { ENMv2Klasse } from '@core/core/data/enm/v2/ENMv2Klasse';
	import type { ENMv2Schueler } from '@core/core/data/enm/v2/ENMv2Schueler';
	import type { ENMv2Teilleistungsart } from '@core/core/data/enm/v2/ENMv2Teilleistungsart';
	import type { DataTableColumn } from '@ui/types';

	import type { EnmManager } from './EnmManager';
	import { ermittleNotenWarnstufe } from './EnmNotenWarnstufe';

	interface SchuelerEintrag {
		schueler: ENMv2Schueler;
		klasse: ENMv2Klasse;
	}

	interface FachEintrag {
		idKlasse: number;
		idSchueler: number;
		fachSortierung: number;
		klasse: string;
		klassenkuerzel: string;
		schueler: string;
		jahrgang: string;
		fach: string;
		lerngruppe: string;
		note: string;
		noteOriginal: string;
		[key: string]: string | number;
	}

	const props = defineProps<{
		ladeManagerNotenuebersicht: () => Promise<EnmManager>;
		auswahlAnsicht: () => ENMLehrerFunktion;
		auswahlEinzel: () => ENMv2Klasse | null;
		auswahlMehrfach: () => ENMv2Klasse[];
	}>();
	const auswahlSchueler = ref<SchuelerEintrag | null>(null);
	const punkteAnsicht = ref(false);
	const managerNotenuebersicht = shallowRef<EnmManager | null>(null);
	const ladeFehler = ref(false);
	const badgeGelbStyle = {
		backgroundColor: '#FCD34D',
		borderColor: '#FBBF24',
		color: '#000000',
	};
	const badgeRotStyle = {
		backgroundColor: '#EF4444',
		borderColor: '#DC2626',
		color: '#FFFFFF',
	};

	let ladeVersion = 0;
	watch(() => props.auswahlAnsicht(), async () => {
		const aktuelleLadeVersion = ++ladeVersion;
		managerNotenuebersicht.value = null;
		ladeFehler.value = false;
		try {
			const manager = await props.ladeManagerNotenuebersicht();
			if (aktuelleLadeVersion === ladeVersion) {
				managerNotenuebersicht.value = manager;
			}
		} catch {
			if (aktuelleLadeVersion === ladeVersion) {
				ladeFehler.value = true;
			}
		}
	}, { immediate: true });

	const berechtigt = computed(() => {
		const manager = managerNotenuebersicht.value;
		if (manager === null) {
			return false;
		}
		for (const klasse of manager.listKlassenKlassenlehrer) {
			if (istKlasseZugaenglich(klasse, manager)) {
				return true;
			}
		}
		return false;
	});

	const ausgewählteKlassen = computed(() => {
		const mehrfach = props.auswahlMehrfach();
		if (mehrfach.length > 0) {
			return new Set(mehrfach.map(klasse => klasse.id));
		}
		const einzel = props.auswahlEinzel();
		return (einzel === null) ? null : new Set([einzel.id]);
	});
	const punkteToggleSichtbar = computed(() => {
		const manager = managerNotenuebersicht.value;
		if (manager === null) {
			return false;
		}
		for (const klasse of manager.listKlassenKlassenlehrer) {
			if (istKlasseZugaenglich(klasse, manager) && istKlasseAusgewählt(klasse)
				&& !/^(05|06|07|08|09|10)/.test(klasse.kuerzel ?? klasse.kuerzelAnzeige ?? '')) {
				return true;
			}
		}
		return false;
	});

	const schuelerListe = computed((): SchuelerEintrag[] => {
		const manager = managerNotenuebersicht.value;
		if (!berechtigt.value || (manager === null)) {
			return [];
		}
		const result: SchuelerEintrag[] = [];
		for (const klasse of manager.listKlassenKlassenlehrer) {
			if (!istKlasseZugaenglich(klasse, manager) || !istKlasseAusgewählt(klasse)) {
				continue;
			}
			const schueler = manager.mapKlassenSchueler.get(klasse.id);
			if (schueler === null) {
				continue;
			}
			for (const eintrag of schueler) {
				result.push({ schueler: eintrag, klasse });
			}
		}
		return result;
	});

	const teilleistungsarten = computed(() => {
		const manager = managerNotenuebersicht.value;
		if (manager === null) {
			return [];
		}
		const ids = new Set<number>();
		for (const { schueler } of schuelerListe.value) {
			for (const leistung of schueler.leistungsdaten) {
				for (const teilleistung of leistung.teilleistungen) {
					ids.add(teilleistung.artID);
				}
			}
		}
		const result: ENMv2Teilleistungsart[] = [];
		for (const id of ids) {
			const art = manager.mapTeilleistungsarten.get(id);
			if (art !== null) {
				result.push(art);
			}
		}
		return result.sort((a, b) => {
			const sortierung = (a.sortierung ?? 0) - (b.sortierung ?? 0);
			return sortierung !== 0 ? sortierung : (a.bezeichnung ?? '').localeCompare(b.bezeichnung ?? '', 'de');
		});
	});

	const spalten = computed(() => {
		const maxNamenslaenge = schuelerListe.value.reduce((max, eintrag) =>
			Math.max(max, `${eintrag.schueler.nachname ?? ''}, ${eintrag.schueler.vorname ?? ''}`.length), 'Schüler'.length);
		const namensBreite = Math.max(9.5, Math.ceil(((maxNamenslaenge * 0.58) + 3) * 2) / 2);
		const result: DataTableColumn[] = [
			{ key: 'klasse', label: 'Klasse', fixedWidth: 6.5, sortable: true },
			{ key: 'schueler', label: 'Schüler', fixedWidth: namensBreite, sortable: true },
			{ key: 'fach', label: 'Fach', fixedWidth: 5.5, sortable: true },
			{ key: 'lerngruppe', label: 'Lerngruppe', fixedWidth: 9, sortable: true },
		];
		for (const art of teilleistungsarten.value) {
			const label = art.bezeichnung ?? `Teilleistung ${art.id}`;
			const fixedWidth = Math.max(5.5, Math.ceil(((label.length * 0.55) + 3) * 2) / 2);
			result.push({ key: `teilleistung_${art.id}`, label, fixedWidth, align: 'center', sortable: true });
		}
		result.push({ key: 'note', label: 'Note', fixedWidth: 5.5, align: 'center', sortable: true });
		return result;
	});

	const eintraege = computed((): FachEintrag[] => {
		const manager = managerNotenuebersicht.value;
		if (!berechtigt.value || (manager === null)) {
			return [];
		}
		const result: FachEintrag[] = [];
		for (const { schueler, klasse } of schuelerListe.value) {
			const faecher: FachEintrag[] = [];
			for (const leistung of schueler.leistungsdaten) {
				const lerngruppe = manager.mapLerngruppen.get(leistung.lerngruppenID);
				if (lerngruppe === null) {
					continue;
				}
				const fach = manager.mapFaecher.get(lerngruppe.fachID);
				if (fach === null) {
					continue;
				}
				const eintrag: FachEintrag = {
					idKlasse: klasse.id,
					idSchueler: schueler.id,
					fachSortierung: fach.sortierung,
					klasse: klasse.kuerzelAnzeige ?? klasse.kuerzel ?? '',
					klassenkuerzel: klasse.kuerzel ?? klasse.kuerzelAnzeige ?? '',
					schueler: `${schueler.nachname ?? ''}, ${schueler.vorname ?? ''}`,
					jahrgang: getJahrgangKuerzel(klasse, manager),
					fach: fach.kuerzelAnzeige !== '' ? fach.kuerzelAnzeige : fach.kuerzel,
					lerngruppe: lerngruppe.bezeichnung ?? '',
					note: getAnzeigewert(leistung.note, klasse, manager) ?? '-',
					noteOriginal: leistung.note ?? '-',
				};
				for (const art of teilleistungsarten.value) {
					eintrag[`teilleistung_${art.id}`] = '-';
					eintrag[`teilleistung_${art.id}_original`] = '-';
				}
				for (const teilleistung of leistung.teilleistungen) {
					eintrag[`teilleistung_${teilleistung.artID}`] = getAnzeigewert(teilleistung.note, klasse, manager) ?? '-';
					eintrag[`teilleistung_${teilleistung.artID}_original`] = teilleistung.note ?? '-';
				}
				faecher.push(eintrag);
			}
			faecher.sort((a, b) => {
				const sortierung = a.fachSortierung - b.fachSortierung;
				return sortierung !== 0 ? sortierung : a.lerngruppe.localeCompare(b.lerngruppe, 'de');
			});
			result.push(...faecher);
		}
		return result;
	});

	const eintraegeGefiltert = computed(() => {
		if (auswahlSchueler.value === null) {
			return eintraege.value;
		}
		const idSchueler = auswahlSchueler.value.schueler.id;
		const idKlasse = auswahlSchueler.value.klasse.id;
		return eintraege.value.filter(eintrag => (eintrag.idSchueler === idSchueler) && (eintrag.idKlasse === idKlasse));
	});

	function istKlasseZugaenglich(klasse: ENMv2Klasse, manager: EnmManager): boolean {
		if (manager.lehrerFunktion === ENMLehrerFunktion.KLASSENLEITUNG) {
			return manager.klasseIstKlassenleitung(klasse.id);
		}
		if (manager.lehrerFunktion === ENMLehrerFunktion.ABTEILUNGSLEITUNG) {
			return manager.klasseIstAbteilungsleitung(klasse.id);
		}
		return false;
	}

	function istKlasseAusgewählt(klasse: ENMv2Klasse): boolean {
		return (ausgewählteKlassen.value === null) || ausgewählteKlassen.value.has(klasse.id);
	}

	function getJahrgangKuerzel(klasse: ENMv2Klasse, manager: EnmManager): string {
		const jahrgang = (klasse.idJahrgang === null) ? null : manager.mapJahrgaenge.get(klasse.idJahrgang);
		return (jahrgang?.kuerzel ?? jahrgang?.kuerzelAnzeige ?? klasse.kuerzel ?? klasse.kuerzelAnzeige ?? '').toUpperCase();
	}

	function getNotenBadge(spalte: string, zeile: FachEintrag, wert: unknown): 'gelb' | 'orange' | null {
		if (!istNotenspalte(spalte)) {
			return null;
		}
		const notenWert = punkteAnsicht.value && !/^(05|06|07|08|09|10)/.test(zeile.klassenkuerzel)
			? (spalte === 'note' ? zeile.noteOriginal : zeile[`${spalte}_original`])
			: wert;
		const note = typeof notenWert === 'string' || typeof notenWert === 'number' ? String(notenWert) : null;
		const warnstufe = ermittleNotenWarnstufe(zeile.jahrgang, zeile.klassenkuerzel, note);
		return warnstufe === 'rot' ? 'orange' : warnstufe;
	}

	function getAnzeigewert(wert: string | null, klasse: ENMv2Klasse, manager: EnmManager): string | null {
		if ((wert === null) || (punkteAnsicht.value === false) || /^(05|06|07|08|09|10)/.test(klasse.kuerzel ?? klasse.kuerzelAnzeige ?? '')) {
			return wert;
		}
		const kuerzel = wert.trim().replace(/[−–]/g, '-').toLocaleLowerCase();
		for (const note of manager.daten.noten) {
			if ((note.kuerzel ?? '').trim().replace(/[−–]/g, '-').toLocaleLowerCase() === kuerzel) {
				return note.notenpunkte === null ? wert : String(note.notenpunkte);
			}
		}
		return wert;
	}

	function istNotenspalte(spalte: string): boolean {
		return (spalte === 'note') || spalte.startsWith('teilleistung_');
	}

	function schuelerAnzeige(eintrag: SchuelerEintrag): string {
		const name = `${eintrag.schueler.nachname ?? ''}, ${eintrag.schueler.vorname ?? ''}`;
		return `${name} (${eintrag.klasse.kuerzelAnzeige ?? eintrag.klasse.kuerzel ?? ''})`;
	}

	function filterSchueler(eintraege: SchuelerEintrag[], suche: string): SchuelerEintrag[] {
		const begriff = suche.toLocaleLowerCase();
		return eintraege.filter(eintrag => `${eintrag.schueler.nachname ?? ''} ${eintrag.schueler.vorname ?? ''}`
			.toLocaleLowerCase().includes(begriff));
	}

</script>
