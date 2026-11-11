<template>
	<div v-if="ladeFehler" class="page p-4">
		<p>Die Daten der Klassenleitung konnten für die Notenübersicht nicht geladen werden.</p>
	</div>
	<div v-else-if="managerNotenuebersicht === null" class="page p-4">
		<p>Die Notenübersicht wird geladen …</p>
	</div>
	<div v-else-if="berechtigt" class="page page-flex-col gap-4 p-4">
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
		<svws-ui-table :items="eintraegeGefiltert" :columns="spalten" :no-data-text="auswahlSchueler ? 'Für diesen Schüler sind keine Teilleistungen vorhanden.' : 'Keine Teilleistungen vorhanden.'" scroll />
	</div>
	<div v-else class="page p-4">
		<p>Für diese Übersicht ist eine Klassenleitungsberechtigung erforderlich.</p>
	</div>
</template>

<script setup lang="ts">

	import { computed, onMounted, ref, shallowRef } from 'vue';

	import type { ENMv2Klasse } from '@core/core/data/enm/v2/ENMv2Klasse';
	import type { ENMv2Schueler } from '@core/core/data/enm/v2/ENMv2Schueler';
	import type { ENMv2Teilleistungsart } from '@core/core/data/enm/v2/ENMv2Teilleistungsart';

	import type { EnmManager } from './EnmManager';

	interface SchuelerEintrag {
		schueler: ENMv2Schueler;
		klasse: ENMv2Klasse;
	}

	interface FachEintrag {
		idKlasse: number;
		idSchueler: number;
		fachSortierung: number;
		klasse: string;
		schueler: string;
		fach: string;
		lerngruppe: string;
		[key: string]: string | number;
	}

	const props = defineProps<{ ladeManagerKlassenleitung: () => Promise<EnmManager> }>();
	const auswahlSchueler = ref<SchuelerEintrag | null>(null);
	const managerNotenuebersicht = shallowRef<EnmManager | null>(null);
	const ladeFehler = ref(false);

	onMounted(async () => {
		try {
			managerNotenuebersicht.value = await props.ladeManagerKlassenleitung();
		} catch {
			ladeFehler.value = true;
		}
	});

	const berechtigt = computed(() => {
		const manager = managerNotenuebersicht.value;
		if (manager === null) {
			return false;
		}
		for (const klasse of manager.listKlassenKlassenlehrer) {
			if (manager.klasseIstKlassenleitung(klasse.id)) {
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
			if (!istKlasseZugaenglich(klasse, manager)) {
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
		return result.sort((a, b) => ((a.sortierung ?? 0) - (b.sortierung ?? 0)) || (a.bezeichnung ?? '').localeCompare(b.bezeichnung ?? '', 'de'));
	});

	const spalten = computed(() => {
		const result = [
			{ key: 'klasse', label: 'Klasse', fixedWidth: 4, sortable: true },
			{ key: 'schueler', label: 'Schüler', fixedWidth: 16, sortable: true },
			{ key: 'fach', label: 'Fach', fixedWidth: 4, sortable: true },
			{ key: 'lerngruppe', label: 'Lerngruppe', fixedWidth: 6, sortable: true },
		];
		for (const art of teilleistungsarten.value) {
			result.push({ key: `teilleistung_${art.id}`, label: art.bezeichnung ?? `Teilleistung ${art.id}`, fixedWidth: 4, align: 'center', sortable: true });
		}
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
					schueler: `${schueler.nachname ?? ''}, ${schueler.vorname ?? ''}`,
					fach: fach.kuerzelAnzeige || fach.kuerzel,
					lerngruppe: lerngruppe.bezeichnung ?? '',
				};
				for (const art of teilleistungsarten.value) {
					eintrag[`teilleistung_${art.id}`] = '—';
				}
				for (const teilleistung of leistung.teilleistungen) {
					eintrag[`teilleistung_${teilleistung.artID}`] = teilleistung.note ?? '—';
				}
				faecher.push(eintrag);
			}
			faecher.sort((a, b) => (a.fachSortierung - b.fachSortierung) || a.lerngruppe.localeCompare(b.lerngruppe, 'de'));
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
		return manager.klasseIstKlassenleitung(klasse.id);
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
