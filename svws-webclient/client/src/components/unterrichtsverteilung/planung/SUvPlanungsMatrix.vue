<template>
	<div class="flex min-h-0 flex-col">
		<div class="flex shrink-0 flex-col gap-2 border-b border-ui-10 p-2">
			<div class="grid grid-cols-2 gap-2">
				<svws-ui-select class="min-w-0" headless title="Matrixbereich" :items="bereiche" :item-text="item => item.text"
					:model-value="bereichAuswahl" @update:model-value="item => item && (bereich = item.value)" />
				<svws-ui-select class="min-w-0" headless title="Jahrgang" :items="jahrgangOptionen" :item-text="item => item.text"
					:model-value="jahrgangAuswahl" @update:model-value="item => jahrgang = item?.value ?? null" />
				<div class="col-span-2 flex flex-wrap items-center gap-2">
					<svws-ui-button type="secondary"
						class="whitespace-nowrap"
						:title="nurOffene ? 'Alle Lerngruppen anzeigen' : 'Nur Lerngruppen ohne Lehrkraft anzeigen'"
						@click="nurOffene = !nurOffene">
						{{ nurOffene ? 'Alle' : 'Offene' }}
					</svws-ui-button>
					<svws-ui-button v-if="selectedLehrerId !== null" type="secondary" class="whitespace-nowrap" @click="nurEinsaetze = !nurEinsaetze">
						{{ nurEinsaetze ? 'Alle Lerngruppen' : 'Nur Einsätze' }}
					</svws-ui-button>
					<span class="ml-auto whitespace-nowrap text-xs text-ui-secondary">{{ sichtbareLerngruppen.length }} Lerngruppen · {{ offeneAnzahl }} offen</span>
				</div>
			</div>
			<svws-ui-text-input v-model="fachSuche" type="search" placeholder="Fach suchen" removable />
			<s-uv-planungs-listen-aktionen :key="state.planungsabschnitt?.id" art="matrix" :selected-ids="[]" :erstellen="props.erstellen" />
		</div>
		<div class="flex shrink-0 items-center gap-3 border-b border-ui-10 px-3 py-1 text-xs text-ui-secondary">
			<span><span class="mr-1 inline-block h-2 w-2 rounded-sm bg-ui-warning" />Offen</span>
			<span><span class="mr-1 inline-block h-2 w-2 rounded-sm bg-ui-success" />Mit Lehrkraft</span>
			<span v-if="selectedId !== null && !auswahlVerborgen">Ausgewählte Lerngruppe ist hervorgehoben</span>
		</div>
		<div v-if="selectedLehrerId !== null" class="flex shrink-0 items-center justify-between gap-2 border-b border-ui-10 bg-ui-selected px-3 py-2 text-xs">
			<span class="truncate">{{ ausgewaehlterLehrer?.kuerzel ?? 'Lehrkraft' }}: {{ einsatzIds.size }} Lerngruppen · {{ sichtbareEinsaetze }} in dieser Ansicht</span>
			<svws-ui-button v-if="sichtbareEinsaetze === 0 && einsatzIds.size > 0"
				type="secondary"
				class="shrink-0"
				@click="einsaetzeAnzeigen">
				Einsätze anzeigen
			</svws-ui-button>
		</div>
		<div v-if="auswahlVerborgen" class="flex shrink-0 items-center justify-between gap-2 border-b border-ui-10 bg-ui-selected px-3 py-2 text-xs">
			<span class="truncate">{{ ausgewaehlteLerngruppe === undefined ? 'Die ausgewählte Lerngruppe ist nicht mehr vorhanden.' : `${presenter.lerngruppeBezeichnung(ausgewaehlteLerngruppe)} · ${presenter.lerngruppeFach(ausgewaehlteLerngruppe)} ist durch die Filter ausgeblendet.` }}</span>
			<svws-ui-button v-if="ausgewaehlteLerngruppe !== undefined" type="secondary" class="shrink-0" @click="auswahlAnzeigen">Anzeigen</svws-ui-button>
		</div>
		<div ref="scrollbereich" class="min-h-0 flex-1 overflow-auto">
			<table v-if="sichtbareLerngruppen.length > 0" class="min-w-max border-separate border-spacing-0 text-xs">
				<thead class="sticky top-0 z-10 bg-ui-neutral">
					<tr>
						<th scope="col" class="sticky left-0 z-20 min-w-20 border-b border-r border-ui-25 bg-ui-neutral p-2 text-left">Klasse / Jahrgang</th>
						<th v-for="fach in faecher" :key="fach.kuerzel" scope="col" class="min-w-20 border-b border-r border-ui-25 p-2 text-center font-semibold">
							<s-uv-fach-badge :id-fach="fach.id" :kuerzel="fach.kuerzel" />
						</th>
					</tr>
				</thead>
				<tbody>
					<tr v-for="zeile in zeilen" :key="zeile.id">
						<th scope="row" class="sticky left-0 z-10 border-b border-r border-ui-25 bg-ui-neutral p-2 text-left font-semibold"
							:class="zeile.jahrgangWechsel ? 'border-t-2 border-t-ui-50' : ''">
							<span class="block">{{ zeile.text }}</span>
							<span class="block text-xs font-normal text-ui-secondary">{{ zeile.offen }} offen</span>
						</th>
						<td v-for="fach in faecher" :key="fach.kuerzel" class="min-w-20 border-b border-r border-ui-25 p-1 align-top"
							:class="zeile.jahrgangWechsel ? 'border-t-2 border-t-ui-50' : ''">
							<div v-for="lerngruppe in zeile.zellen.get(fach.kuerzel) ?? []" :key="lerngruppe.id" class="group relative mb-1 last:mb-0">
								<button type="button"
									:data-uv-lerngruppe-id="lerngruppe.id"
									class="flex w-full min-w-18 flex-col rounded border px-1.5 py-1 text-left hover:shadow-sm focus-visible:outline-2 focus-visible:outline-ui-brand"
									:class="uvAuswahlMatrixKlasse(auswahlManager?.auswahlStatus(auswahl, 'lerngruppen', lerngruppe.id) ?? 'neutral', istOffen(lerngruppe))"
									:title="`${presenter.lerngruppeBezeichnung(lerngruppe)} · ${lerngruppe.wochenstunden} Wochenstunden · ${lehrerText(lerngruppe)}`"
									@click="props.auswaehlen(lerngruppe.id)">
									<span v-if="lerngruppe.idKurs !== null" class="truncate font-semibold">{{ presenter.lerngruppeKurzbezeichnung(lerngruppe) }}</span>
									<span class="flex w-full items-center justify-between gap-1">
										<span class="truncate font-semibold">{{ lehrerText(lerngruppe) }}</span>
										<span class="shrink-0 text-xs text-ui-secondary">{{ lerngruppe.wochenstunden }} h</span>
									</span>
								</button>
								<s-uv-details-oeffnen v-if="auswahlManager?.auswahlStatus(auswahl, 'lerngruppen', lerngruppe.id) === 'direkt'"
									variante="kompakt"
									:oeffnen="() => props.detailsAnzeigen(lerngruppe.id)" />
							</div>
						</td>
					</tr>
				</tbody>
			</table>
			<p v-if="sichtbareLerngruppen.length === 0" class="p-4 text-ui-secondary">{{ nurOffene ? 'Keine offenen Lerngruppen für diese Auswahl.' : 'Keine Lerngruppen für diese Auswahl.' }}</p>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { computed, nextTick, ref, shallowRef, watch } from 'vue';

	import type { UvLerngruppe } from '@core/core/data/uv/UvLerngruppe';
	import { useUvState } from '@ui/states/UvState';

	import SUvFachBadge from '../SUvFachBadge.vue';
	import { createUvPresenter } from '../UvPresenter';

	import SUvDetailsOeffnen from './SUvDetailsOeffnen.vue';
	import SUvPlanungsListenAktionen from './SUvPlanungsListenAktionen.vue';
	import { uvAuswahlMatrixKlasse } from './UvAuswahlDarstellung';
	import type { UvPlanungsAuswahl } from './UvPlanung';
	import type { UvPlanungsManager } from './UvPlanungsManager';

	const props = defineProps<{
		selectedId: number | null;
		selectedLehrerId: number | null;
		auswahl: UvPlanungsAuswahl | null;
		sortierAuswahl: UvPlanungsAuswahl | null;
		auswahlManager: UvPlanungsManager | null;
		auswaehlen: (id: number) => void;
		detailsAnzeigen: (id: number) => void;
		erstellen: (art: 'lerngruppen' | 'klassen' | 'schienen') => void;
	}>();

	const state = useUvState();
	const presenter = computed(() => createUvPresenter(state.uvManager));
	const bereiche = [
		{ value: 'alle', text: 'Alle Lerngruppen' },
		{ value: 'klassen', text: 'Klassenunterricht' },
		{ value: 'kurse', text: 'Kursunterricht' },
	] as const;

	// Matrixfilter und Scrollposition

	const bereich = ref<'alle' | 'klassen' | 'kurse'>('alle');
	const jahrgang = ref<string | null>(null);
	const fachSuche = ref('');
	const nurOffene = ref(false);
	const nurEinsaetze = ref(false);
	const scrollbereich = ref<HTMLDivElement | null>(null);
	const sortierKontext = shallowRef<UvPlanungsAuswahl | null>(null);
	let scrollAuftrag = 0;

	const bereichAuswahl = computed(() => bereiche.find(item => item.value === bereich.value));

	const lerngruppen = computed(() => {
		const plan = state.planungsabschnitt;
		return plan === null || plan.id === -1 ? [] : [...state.uvManager.lerngruppeGetMengeByPlanungsabschnitt(plan)];
	});
	const ausgewaehlterLehrer = computed(() => props.selectedLehrerId === null ? undefined : state.uvManager.lehrerGetByIdOrNull(props.selectedLehrerId) ?? undefined);
	// Gefilterte Matrix und daraus abgeleitete Anzeige

	const matrixDaten = computed(() => props.auswahlManager?.matrixAnsicht({
		bereich: bereich.value,
		jahrgang: jahrgang.value,
		fachSuche: fachSuche.value,
		nurOffene: nurOffene.value,
		nurEinsaetze: nurEinsaetze.value,
		idLehrer: props.selectedLehrerId,
	}, sortierKontext.value));
	const matrixLerngruppen = computed(() => matrixDaten.value?.bereichLerngruppen ?? []);
	const einsatzIds = computed(() => matrixDaten.value?.einsatzIds ?? new Set<number>());
	const sichtbareLerngruppen = computed(() => matrixDaten.value?.sichtbareLerngruppen ?? []);
	const sichtbareEinsaetze = computed(() => matrixDaten.value?.sichtbareEinsaetze ?? 0);
	const offeneAnzahl = computed(() => matrixDaten.value?.offeneAnzahl ?? 0);
	const faecher = computed(() => matrixDaten.value?.faecher ?? []);
	const zeilen = computed(() => matrixDaten.value?.zeilen ?? []);

	const jahrgangOptionen = computed(() => [
		{ value: null as string | null, text: 'Alle Jahrgänge' },
		...(matrixDaten.value?.jahrgaenge ?? []).map(value => ({ value, text: value })),
	]);
	const jahrgangAuswahl = computed(() => jahrgangOptionen.value.find(item => item.value === jahrgang.value));

	const ausgewaehlteLerngruppe = computed(() => lerngruppen.value.find(item => item.id === props.selectedId));
	const auswahlVerborgen = computed(() => props.selectedId !== null && !sichtbareLerngruppen.value.some(item => item.id === props.selectedId));

	// Eine Auswahl in der Matrix behält die zuletzt von außen bestimmte Sortierung bei.
	watch([() => props.sortierAuswahl, () => props.auswahl], ([sortierung, auswahl]) => {
		if (sortierung !== null || auswahl === null) {
			sortierKontext.value = sortierung;
		}
	}, { immediate: true, flush: 'sync' });

	watch(jahrgangOptionen, optionen => {
		if (jahrgang.value !== null && !optionen.some(item => item.value === jahrgang.value)) {
			jahrgang.value = null;
		}
	});

	watch(() => props.selectedLehrerId, id => {
		nurEinsaetze.value = false;
		if (id !== null) {
			einsaetzeAnzeigen();
		}
	});

	watch(() => props.sortierAuswahl === null ? null : `${props.sortierAuswahl.art}-${props.sortierAuswahl.id}`, async schluessel => {
		const auftrag = ++scrollAuftrag;
		if (schluessel === null) {
			return;
		}
		await nextTick();
		if (auftrag === scrollAuftrag) {
			scrollbereich.value?.scrollTo({ left: 0, top: 0, behavior: 'auto' });
		}
	}, { flush: 'post' });

	watch([() => props.selectedId, auswahlVerborgen], ([id, verborgen]) => {
		if (props.sortierAuswahl !== null) {
			return;
		}
		if (id === null || verborgen) {
			scrollAuftrag += 1;
			return;
		}
		void zeigeAuswahlImMatrixbereich(id);
	}, { immediate: true, flush: 'post' });

	async function zeigeAuswahlImMatrixbereich(id: number): Promise<void> {
		const auftrag = ++scrollAuftrag;
		await nextTick();
		if (auftrag !== scrollAuftrag || props.selectedId !== id) {
			return;
		}
		const container = scrollbereich.value;
		const kachel = container?.querySelector<HTMLElement>(`[data-uv-lerngruppe-id="${id}"]`);
		if (container === null || kachel === undefined || kachel === null) {
			return;
		}
		const rahmen = container.getBoundingClientRect();
		const ziel = kachel.getBoundingClientRect();
		const kopfhoehe = container.querySelector('thead')?.getBoundingClientRect().height ?? 0;
		const spaltenbreite = container.querySelector('thead th')?.getBoundingClientRect().width ?? 0;
		const links = rahmen.left + spaltenbreite + 8;
		const rechts = rahmen.right - 8;
		const oben = rahmen.top + kopfhoehe + 8;
		const unten = rahmen.bottom - 8;
		const dx = ziel.left < links ? ziel.left - links : Math.max(0, ziel.right - rechts);
		const dy = ziel.top < oben ? ziel.top - oben : Math.max(0, ziel.bottom - unten);
		if (dx !== 0 || dy !== 0) {
			container.scrollBy({ left: dx, top: dy, behavior: 'smooth' });
		}
	}

	function filterZuruecksetzen(): void {
		fachSuche.value = '';
		nurOffene.value = false;
		jahrgang.value = null;
		nurEinsaetze.value = false;
	}

	function einsaetzeAnzeigen(): void {
		if (props.selectedLehrerId === null) {
			return;
		}
		filterZuruecksetzen();
		if (einsatzIds.value.size > 0 && !matrixLerngruppen.value.some(item => einsatzIds.value.has(item.id))) {
			bereich.value = 'alle';
		}
	}

	function auswahlAnzeigen(): void {
		const lerngruppe = ausgewaehlteLerngruppe.value;
		if (lerngruppe === undefined) {
			return;
		}
		filterZuruecksetzen();
		bereich.value = 'alle';
		jahrgang.value = props.auswahlManager?.lerngruppeJahrgang(lerngruppe, 'Weitere Klassen', 'Weitere Kurse') ?? null;
		void zeigeAuswahlImMatrixbereich(lerngruppe.id);
	}

	function istOffen(lerngruppe: UvLerngruppe): boolean {
		return props.auswahlManager?.lerngruppeIstOffen(lerngruppe.id) ?? true;
	}

	function lehrerText(lerngruppe: UvLerngruppe): string {
		const text = props.auswahlManager?.lerngruppeLehrerText(lerngruppe.id);
		return text === undefined || text === '' ? 'Offen' : text;
	}
</script>
