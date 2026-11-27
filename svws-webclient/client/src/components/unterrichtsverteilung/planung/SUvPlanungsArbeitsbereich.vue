<template>
	<section class="flex min-h-0 min-w-80 flex-col overflow-hidden rounded-lg border border-ui-25 bg-ui">
		<div class="uv-deaktivierbar flex shrink-0 items-center gap-3 border-b border-ui-10 p-3" :inert="navigationGesperrt">
			<span class="text-xs font-semibold text-ui-secondary">{{ seite }}</span>
			<svws-ui-select class="min-w-40 flex-1" headless title="Inhalt des Arbeitsbereichs" :items="uvBereichArten" :disabled="navigationGesperrt"
				:item-text="item => item.text" :model-value="bereichAuswahl" @update:model-value="item => item && emit('update:art', item.value)" />
			<span v-if="istListenAnsicht" class="text-xs text-ui-secondary">{{ anzahl }} {{ art === 'klassen' ? 'Klassen' : 'Einträge' }}</span>
		</div>
		<div v-if="art === 'lehrer' && mitgliedschaftBearbeiten" class="flex shrink-0 items-center justify-between gap-2 border-b border-ui-10 p-3">
			<span class="text-sm font-semibold">Lehrkräfte im Planungsabschnitt</span>
			<svws-ui-button type="secondary"
				:disabled="mitgliedschaftPending"
				@click="props.mitgliedschaftBearbeitungBeenden()">
				Bearbeiten beenden
			</svws-ui-button>
		</div>
		<div v-else-if="istListenAnsicht" class="shrink-0 border-b border-ui-10 p-3">
			<svws-ui-text-input v-if="art !== 'schueler'" v-model="suche" type="search" placeholder="Suchen" removable />
			<svws-ui-button v-if="art === 'lehrer' && state.hatKompetenzAendern"
				type="secondary"
				class="mt-2"
				@click="props.mitgliedschaftBearbeitungStarten()">
				Lehrkräfte hinzufügen/entfernen
			</svws-ui-button>
			<div v-if="art === 'lerngruppen'" class="mt-2 grid grid-cols-2 gap-2 xl:grid-cols-4">
				<svws-ui-select v-model="filterLerngruppenJahrgang" :items="lerngruppenJahrgaenge" :item-text="item => item" title="Jahrgang" removable />
				<svws-ui-select v-model="filterLerngruppenFach" :items="lerngruppenFaecher" :item-text="item => item" title="Fach" removable />
				<svws-ui-select v-model="filterLerngruppenKlasseKurs"
					:items="lerngruppenKlassenKurse"
					:item-text="item => item.text"
					title="Klasse / Kursunterricht"
					removable />
				<svws-ui-select v-model="filterLerngruppenStatus" :items="lerngruppenStatus" :item-text="item => item.text" title="Status" removable />
			</div>
			<!-- Abschnitt und Ansicht begrenzen die Lebensdauer laufender Listenaktionen. -->
			<s-uv-planungs-listen-aktionen :key="`${plan?.id}-${art}`"
				:art
				:selected-ids
				:auswahl-aufheben="() => selectedIds = []"
				:erstellen="props.erstellen" />
			<div v-if="art === 'lerngruppen' && ausgewaehlterLehrer !== undefined" class="mt-2 flex items-center justify-between gap-2 text-xs">
				<span class="min-w-0 truncate font-semibold">{{ nurLehrerEinsaetze ? `Einsätze von ${ausgewaehlterLehrer.kuerzel}` : `Alle Lerngruppen · ${ausgewaehlterLehrer.kuerzel} ausgewählt` }}</span>
				<div class="flex shrink-0 items-center gap-1">
					<svws-ui-button type="secondary" @click="nurLehrerEinsaetze = !nurLehrerEinsaetze">{{ nurLehrerEinsaetze ? 'Alle anzeigen' : 'Nur Einsätze' }}</svws-ui-button>
					<svws-ui-button type="icon"
						title="Lehrkraftauswahl aufheben"
						@click="props.lehrerAuswaehlen(null)">
						<span class="icon i-ri-close-line" />
					</svws-ui-button>
				</div>
			</div>
			<div v-if="art === 'lehrer' && ausgewaehlterLehrer !== undefined" class="mt-2 flex items-center justify-between gap-2 text-xs">
				<span class="min-w-0 truncate font-semibold">{{ ausgewaehlterLehrer.kuerzel }} · {{ (lehrerEinsatz?.anzahl ?? 0) }} Lerngruppen · {{ formatStunden(lehrerEinsatz?.stunden ?? 0) }} h</span>
				<svws-ui-button type="secondary" @click="props.lehrerAuswaehlen(null)">Auswahl aufheben</svws-ui-button>
			</div>
			<div v-if="art === 'lehrer' && ausgewaehlteLerngruppe !== undefined" class="mt-2 flex items-center justify-between gap-2 text-xs">
				<span class="flex min-w-0 items-center gap-1 text-ui-secondary"><span class="truncate">{{ zugeordneteLehrer.length === 0 ? 'Für' : `Zugeordnet: ${zugeordneteLehrer.join(', ')} ·` }} {{ presenter.lerngruppeBezeichnung(ausgewaehlteLerngruppe) }}</span><s-uv-fach-badge :id-fach="presenter.lerngruppeFachId(ausgewaehlteLerngruppe)" /></span>
				<svws-ui-button type="secondary" @click="nurGeeignete = !nurGeeignete">{{ nurGeeignete ? 'Alle anzeigen' : 'Nur geeignete' }}</svws-ui-button>
			</div>
			<s-uv-lerngruppe-lehrer-zuordnungen v-if="art === 'lehrer' && ausgewaehlteLerngruppe !== undefined"
				:id-lerngruppe="ausgewaehlteLerngruppe.id"
				:bearbeiten="props.zuordnungBearbeiten" />
		</div>
		<div v-if="art === 'details'" class="flex min-h-0 flex-1 flex-col overflow-auto"><slot name="details"><p class="p-4 text-sm text-ui-secondary">Eintrag in der anderen Ansicht auswählen.</p></slot></div>
		<s-uv-planungs-matrix v-else-if="art === 'matrix'"
			class="min-h-0 flex-1"
			:selected-id="selectedLerngruppeId"
			:selected-lehrer-id
			:auswahl
			:sortier-auswahl
			:auswahl-manager
			:auswaehlen="id => props.auswaehlen('lerngruppen', id)"
			:details-anzeigen="id => props.detailsAnzeigen('lerngruppen', id)"
			:erstellen="props.erstellen" />
		<s-uv-planungs-regeln v-else-if="art === 'regeln'"
			:auswahl
			:sortier-auswahl
			:auswahl-manager
			:auswaehlen="id => props.auswaehlen('regeln', id)"
			:details-anzeigen="id => props.detailsAnzeigen('regeln', id)"
			:erstellen="() => props.erstellen('regeln')" />
		<s-uv-planungs-statistik v-else-if="art === 'statistik' && statistik !== undefined" :daten="statistik" />
		<s-uv-planungs-klassenbrett v-else-if="art === 'klassen'"
			:stufen="klassenDaten?.stufen ?? []"
			:selected-ids
			:waehlbare-ids
			:auswahl
			:auswahl-manager
			:klasse-auswaehlen="id => props.auswaehlen('klassen', id)"
			:details-anzeigen="props.detailsAnzeigen"
			:klassen-auswahl-umschalten="auswahlUmschalten" />
		<s-uv-planungs-lerngruppen v-else-if="art === 'lerngruppen' && lerngruppenDaten !== undefined"
			:daten="lerngruppenDaten"
			:auswahl-manager
			:auswahl
			:selected-ids
			:selectable="hatMehrfachauswahl"
			:auswaehlen="id => props.auswaehlen('lerngruppen', id)"
			:auswahl-umschalten
			:details-anzeigen="id => props.detailsAnzeigen('lerngruppen', id)" />
		<div v-else-if="art === 'schueler'" class="min-h-0 flex-1 overflow-auto p-3">
			<s-uv-planungs-schueler-tabelle v-model:selected="selectedSchueler"
				:items="schueler"
				:selectable="hatMehrfachauswahl"
				:auswahl-status="id => auswahlStatus('schueler', id)"
				:relevant-ids="schuelerDaten?.relevanteIds"
				:relevanz-sortieren="sortierAuswahl !== null"
				filterbar
				@visible-count="schuelerAnzahl = $event"
				:schueler-oeffnen="id => props.auswaehlen('schueler', id)"
				:klasse-oeffnen="id => props.auswaehlen('klassen', id)"
				:details-anzeigen="id => props.detailsAnzeigen('schueler', id)">
				<template #no-data>Keine Schüler für diese Auswahl vorhanden.</template>
			</s-uv-planungs-schueler-tabelle>
		</div>
		<s-uv-planungs-lehrer v-else-if="art === 'lehrer'"
			:eintraege="lehrerDaten?.eintraege ?? []"
			:gesamt-anzahl="lehrerDaten?.gesamtAnzahl ?? 0"
			:suche
			:auswahl
			:auswahl-manager
			:hat-kompetenz-aendern="state.hatKompetenzAendern"
			:mitgliedschaft-bearbeiten
			:mitgliedschaft-pending
			:verwendete-lehrer-ids
			:auswaehlen="id => props.lehrerAuswaehlen(selectedLehrerId === id ? null : id)"
			:details-anzeigen="id => props.detailsAnzeigen('lehrer', id)"
			:lehrer-zuordnen="props.lehrerZuordnen"
			:mitgliedschaft-entfernen="props.mitgliedschaftEntfernen" />
		<div v-else-if="art === 'schienen'" class="min-h-0 flex-1 overflow-auto p-3">
			<div class="flex flex-col gap-2">
				<div v-for="eintrag in schienen"
					:key="`${art}-${eintrag.id}`"
					class="group relative flex min-w-0 items-center gap-2 rounded border border-ui-25 bg-ui p-2 hover:border-ui-brand"
					:class="uvAuswahlKlasse(auswahlStatus(art, eintrag.id))">
					<input v-if="hatMehrfachauswahl && waehlbar(eintrag.id)"
						type="checkbox"
						class="shrink-0"
						:checked="selectedIds.includes(eintrag.id)"
						:aria-label="`${eintrag.text} für Sammelaktion auswählen`"
						@change="auswahlUmschalten(eintrag.id)">
					<button type="button"
						class="flex min-w-0 flex-1 items-center gap-2 text-left focus-visible:outline-2 focus-visible:outline-ui-brand"
						:title="`${eintrag.text} · ${eintrag.zusatz}`"
						@click="props.auswaehlen('schienen', eintrag.id)">
						<span class="min-w-0 flex-1"><span class="block truncate font-semibold">{{ eintrag.text }}</span><span class="block truncate text-xs text-ui-secondary">{{ eintrag.zusatz }}</span></span>
					</button>
					<s-uv-details-oeffnen v-if="auswahlStatus(art, eintrag.id) === 'direkt'" :oeffnen="() => props.detailsAnzeigen(art, eintrag.id)" />
				</div>
			</div>
			<p v-if="schienen.length === 0" class="p-2 text-ui-secondary">{{ suche ? 'Keine Treffer.' : 'Keine Einträge vorhanden.' }}</p>
		</div>
	</section>
</template>

<script setup lang="ts">
	import { computed, ref, watch } from 'vue';

	import type { UvPlanungsabschnittSchueler } from '@core/core/data/uv/UvPlanungsabschnittSchueler';
	import { useUvState } from '@ui/states/UvState';

	import SUvFachBadge from '../SUvFachBadge.vue';
	import { createUvPresenter, formatUvStunden as formatStunden } from '../UvPresenter';

	import SUvDetailsOeffnen from './SUvDetailsOeffnen.vue';
	import SUvLerngruppeLehrerZuordnungen from './SUvLerngruppeLehrerZuordnungen.vue';
	import SUvPlanungsKlassenbrett from './SUvPlanungsKlassenbrett.vue';
	import SUvPlanungsLehrer from './SUvPlanungsLehrer.vue';
	import SUvPlanungsLerngruppen from './SUvPlanungsLerngruppen.vue';
	import SUvPlanungsListenAktionen from './SUvPlanungsListenAktionen.vue';
	import SUvPlanungsMatrix from './SUvPlanungsMatrix.vue';
	import SUvPlanungsRegeln from './SUvPlanungsRegeln.vue';
	import SUvPlanungsSchuelerTabelle from './SUvPlanungsSchuelerTabelle.vue';
	import SUvPlanungsStatistik from './SUvPlanungsStatistik.vue';
	import { uvAuswahlKlasse } from './UvAuswahlDarstellung';
	import { istUvPlanungsAuswahlArt, type UvAuswahlStatus, type UvBereichArt, uvBereichArten, type UvPlanungsAuswahl } from './UvPlanung';
	import type { UvPlanungsManager } from './UvPlanungsManager';

	const props = defineProps<{
		seite: string;
		navigationGesperrt: boolean;
		art: UvBereichArt;
		selectedLerngruppeId: number | null;
		selectedLehrerId: number | null;
		auswahl: UvPlanungsAuswahl | null;
		sortierAuswahl: UvPlanungsAuswahl | null;
		auswahlManager: UvPlanungsManager | null;
		mitgliedschaftBearbeiten: boolean;
		mitgliedschaftPending: boolean;
		verwendeteLehrerIds: Set<number>;
		lehrerAuswaehlen: (id: number | null) => void;
		auswaehlen: (art: UvBereichArt, id: number) => void;
		detailsAnzeigen: (art: UvBereichArt, id: number) => void;
		lehrerZuordnen: (id: number) => void;
		zuordnungBearbeiten: (id: number) => void;
		erstellen: (art: 'lerngruppen' | 'klassen' | 'schienen' | 'regeln') => void;
		mitgliedschaftBearbeitungStarten: () => void;
		mitgliedschaftBearbeitungBeenden: () => void;
		mitgliedschaftEntfernen: (id: number) => void;
	}>();
	const emit = defineEmits<{
		'update:art': [art: UvBereichArt];
	}>();
	const state = useUvState();
	const presenter = computed(() => createUvPresenter(state.uvManager));
	// Suchwerte und Filter des jeweiligen Arbeitsbereichs

	const suche = ref('');
	const nurGeeignete = ref(false);
	const nurLehrerEinsaetze = ref(false);
	const selectedIds = ref<number[]>([]);
	const filterLerngruppenJahrgang = ref<string | null>(null);
	const filterLerngruppenFach = ref<string | null>(null);
	const filterLerngruppenKlasseKurs = ref<{ id: string; text: string } | null>(null);
	const filterLerngruppenStatus = ref<{ id: string; text: string } | null>(null);
	const lerngruppenStatus = [
		{ id: 'offen', text: 'Ohne Lehrkraft' },
		{ id: 'ohneSchueler', text: 'Ohne Schüler' },
		{ id: 'vollstaendig', text: 'Vollständig' },
	];
	const bereichAuswahl = computed(() => uvBereichArten.find(item => item.value === props.art));
	const plan = computed(() => state.planungsabschnitt);
	const istListenAnsicht = computed(() => istUvPlanungsAuswahlArt(props.art) && props.art !== 'regeln');
	// Daten für die aktuell angezeigte Ansicht

	const lerngruppenDaten = computed(() => props.art !== 'lerngruppen' ? undefined : props.auswahlManager?.lerngruppenAnsicht({
		suche: suche.value, auswahl: props.sortierAuswahl, idLehrer: props.selectedLehrerId,
		nurLehrerEinsaetze: nurLehrerEinsaetze.value, jahrgang: filterLerngruppenJahrgang.value, fach: filterLerngruppenFach.value,
		klasseKurs: filterLerngruppenKlasseKurs.value?.id ?? null, status: filterLerngruppenStatus.value?.id ?? null,
	}));
	const lehrerDaten = computed(() => props.art !== 'lehrer' ? undefined : props.auswahlManager?.lehrerAnsicht({
		idLehrer: props.selectedLehrerId, suche: suche.value, auswahl: props.sortierAuswahl, idLerngruppe: props.mitgliedschaftBearbeiten ? null : props.selectedLerngruppeId,
		nurGeeignete: !props.mitgliedschaftBearbeiten && nurGeeignete.value,
	}));
	const statistik = computed(() => props.art !== 'statistik' ? undefined : props.auswahlManager?.statistik());
	const schuelerDaten = computed(() => props.art !== 'schueler' ? undefined : props.auswahlManager?.schuelerAnsicht(props.auswahl));
	const schueler = computed(() => schuelerDaten.value?.schueler ?? []);
	const schuelerAnzahl = ref(0);
	const klassenDaten = computed(() => props.art !== 'klassen' ? undefined : props.auswahlManager?.klassenAnsicht(suche.value, props.sortierAuswahl));
	const schienen = computed(() => props.art !== 'schienen' ? [] : props.auswahlManager?.schienenAnsicht(suche.value, props.sortierAuswahl) ?? []);
	const lerngruppenJahrgaenge = computed(() => lerngruppenDaten.value?.jahrgaenge ?? []);
	const lerngruppenFaecher = computed(() => lerngruppenDaten.value?.faecher ?? []);
	const lerngruppenKlassenKurse = computed(() => lerngruppenDaten.value?.klassenKurse ?? []);
	const ausgewaehlteLerngruppe = computed(() => props.selectedLerngruppeId === null ? undefined : state.uvManager.lerngruppeGetByIdOrNull(props.selectedLerngruppeId) ?? undefined);
	const ausgewaehlterLehrer = computed(() => props.selectedLehrerId === null ? undefined : state.uvManager.lehrerGetByIdOrNull(props.selectedLehrerId) ?? undefined);
	const lehrerEinsatz = computed(() => props.selectedLehrerId === null ? undefined : props.auswahlManager?.lehrerEinsatzZusammenfassung(props.selectedLehrerId));
	const zugeordneteLehrer = computed(() => lehrerDaten.value?.zugeordneteLehrer ?? []);
	// Sammelauswahl und Aktionszustand

	const hatMehrfachauswahl = computed(() => state.hatKompetenzAendern && ['lerngruppen', 'klassen', 'schueler', 'schienen'].includes(props.art));
	const waehlbareIds = computed(() => hatMehrfachauswahl.value ? props.auswahlManager?.sammelauswahlIds(props.art) ?? new Set<number>() : new Set<number>());
	const selectedSchueler = computed({
		get: () => schueler.value.filter(item => selectedIds.value.includes(item.idSchueler)),
		set: (items: UvPlanungsabschnittSchueler[]) => {
			selectedIds.value = items.map(item => item.idSchueler);
		},
	});
	const anzahl = computed(() => {
		switch (props.art) {
			case 'lehrer': {
				return lehrerDaten.value?.eintraege.length ?? 0;
			}
			case 'lerngruppen': {
				return lerngruppenDaten.value?.anzahl ?? 0;
			}
			case 'klassen': {
				return klassenDaten.value?.anzahl ?? 0;
			}
			case 'schueler': {
				return schuelerAnzahl.value;
			}
			default: {
				return schienen.value.length;
			}
		}
	});
	watch(waehlbareIds, ids => {
		selectedIds.value = selectedIds.value.filter(id => ids.has(id));
	});

	watch(() => props.art, () => {
		suche.value = '';
		selectedIds.value = [];
		nurGeeignete.value = false;
		nurLehrerEinsaetze.value = false;
		filterLerngruppenJahrgang.value = null;
		filterLerngruppenFach.value = null;
		filterLerngruppenKlasseKurs.value = null;
		filterLerngruppenStatus.value = null;
	});
	watch(() => props.mitgliedschaftBearbeiten, aktiv => {
		if (aktiv) {
			suche.value = '';
		}
	});
	watch(() => props.selectedLerngruppeId, () => {
		nurGeeignete.value = false;
	});
	watch(() => props.selectedLehrerId, () => {
		nurLehrerEinsaetze.value = false;
	});
	watch(() => plan.value?.id, () => {
		selectedIds.value = [];
	});

	function waehlbar(id: number): boolean {
		return waehlbareIds.value.has(id);
	}

	function auswahlStatus(art: UvBereichArt, id: number): UvAuswahlStatus {
		if (props.auswahlManager === null || !istUvPlanungsAuswahlArt(art)) {
			return 'neutral';
		}
		return props.auswahlManager.auswahlStatus(props.auswahl, art, id);
	}

	function auswahlUmschalten(id: number): void {
		if (!waehlbar(id)) {
			return;
		}
		selectedIds.value = selectedIds.value.includes(id) ? selectedIds.value.filter(item => item !== id) : [...selectedIds.value, id];
	}


</script>
