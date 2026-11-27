<template>
	<section class="flex h-full min-h-0 flex-col overflow-hidden rounded-lg border border-ui-brand bg-ui shadow-lg">
		<div class="flex shrink-0 items-center justify-between gap-2 border-b border-ui-25 p-3">
			<div class="min-w-0">
				<h3 class="font-semibold">{{ titel }} zuordnen</h3>
				<p class="text-xs text-ui-secondary">{{ zugeordnet }} zugeordnet · {{ daten.verfuegbareIds.size }} verfügbar</p>
			</div>
			<svws-ui-button type="secondary" :disabled="bedienungGesperrt" @click="props.schliessen()">Bearbeiten beenden</svws-ui-button>
		</div>
		<div v-if="bearbeitung.art !== 'gruppe-schueler'" class="grid shrink-0 grid-cols-1 gap-2 border-b border-ui-10 p-3">
			<svws-ui-text-input v-model="suche" type="search" :placeholder="`${titel} suchen`" removable />
			<svws-ui-checkbox v-if="bearbeitung.art === 'lerngruppe-lehrer'"
				v-model="nurGeeignete"
				:disabled="bedienungGesperrt"
				type="toggle">
				Nur passende Lehrkräfte
			</svws-ui-checkbox>
		</div>
		<svws-ui-notification v-if="fehler" type="error" class="m-3">{{ fehler }}</svws-ui-notification>
		<div class="flex shrink-0 items-center justify-between gap-2 px-3 pt-2">
			<p class="text-xs text-ui-secondary">Per Plus oder Drag-and-drop hinzufügen. Änderungen werden sofort gespeichert.</p>
			<svws-ui-button v-if="!istLehrerZuordnung && auswahlIds.length > 0"
				type="secondary"
				:disabled="bedienungGesperrt"
				@click="auswahlHinzufuegen">
				{{ auswahlIds.length }} ausgewählte hinzufügen
			</svws-ui-button>
		</div>
		<p v-if="bearbeitung.art === 'gruppe-schueler'" class="px-3 pt-1 text-xs text-ui-secondary">Schüler aus der Gruppe hier ablegen, um sie zu entfernen.</p>
		<div v-if="bearbeitung.art === 'gruppe-schueler'" class="min-h-0 flex-1 overflow-auto p-3">
			<s-uv-planungs-schueler-tabelle v-model:selected="ausgewaehlteSchueler"
				:items="verfuegbareSchueler"
				selectable
				draggable
				filterbar
				:disabled="bedienungGesperrt"
				@drag-start="ziehenStarten"
				:hinzufuegen="eintragHinzufuegen" />
		</div>
		<div v-else-if="istLehrerZuordnung" class="min-h-0 flex-1 overflow-auto p-3">
			<div v-if="lehrerTreffer.length === 0" class="text-sm text-ui-secondary">Keine weiteren passenden Lehrkräfte.</div>
			<div class="grid grid-cols-[repeat(auto-fill,minmax(13rem,1fr))] content-start gap-3">
				<s-uv-planungs-lehrer-kachel v-for="eintrag in lehrerTreffer" :key="eintrag.id" :id-lehrer="eintrag.id"
					:kuerzel="eintrag.text" :name="eintrag.name" :faecher="eintrag.faecher"
					:verplant="eintrag.verplant" :anrechnung="eintrag.anrechnung" :soll="eintrag.soll" :rest="eintrag.reststunden"
					:auswahl-status="planungsManager?.auswahlStatus(zielAuswahl, 'lehrer', eintrag.id) ?? 'neutral'"
					modus="zuordnung-auswahl" :pending="bedienungGesperrt"
					:zuordnen="() => eintragHinzufuegen(eintrag.id)" @drag-start="event => ziehenStarten(event, eintrag.id)" />
			</div>
		</div>
		<div v-else class="min-h-0 flex-1 overflow-auto p-3">
			<div v-if="sichtbar.length === 0" class="text-sm text-ui-secondary">Keine weiteren passenden Einträge.</div>
			<div v-for="eintrag in sichtbar"
				:key="eintrag.id"
				class="mb-1 flex items-center gap-2 rounded border border-ui-25 bg-ui px-2 py-1.5 hover:border-ui-brand"
				:draggable="!bedienungGesperrt"
				@dragstart="ziehenStarten($event, eintrag.id)">
				<input type="checkbox"
					class="shrink-0"
					:checked="auswahlIds.includes(eintrag.id)"
					:disabled="bedienungGesperrt"
					:aria-label="`${eintrag.text} auswählen`"
					@change="auswahlUmschalten(eintrag.id)">
				<span v-if="!bedienungGesperrt" class="icon i-ri-draggable text-ui-secondary" aria-hidden="true" />
				<div class="min-w-0 flex-1">
					<p class="truncate text-sm font-semibold">{{ eintrag.text }}</p>
					<p v-if="eintrag.zusatz" class="truncate text-xs text-ui-secondary">{{ eintrag.zusatz }}</p>
				</div>
				<svws-ui-button type="icon"
					:disabled="bedienungGesperrt"
					:title="`${eintrag.text} hinzufügen`"
					@click="eintragHinzufuegen(eintrag.id)">
					<span class="icon i-ri-add-line" />
				</svws-ui-button>
			</div>
			<svws-ui-button v-if="treffer.length > sichtbar.length"
				type="secondary"
				:disabled="bedienungGesperrt"
				@click="sichtbareAnzahl += 100">
				Weitere anzeigen
			</svws-ui-button>
		</div>
	</section>
</template>

<script setup lang="ts">
	import { computed, onUnmounted, ref, watch } from 'vue';

	import type { UvPlanungsabschnittSchueler } from '@core/core/data/uv/UvPlanungsabschnittSchueler';

	import SUvPlanungsLehrerKachel from './SUvPlanungsLehrerKachel.vue';
	import SUvPlanungsSchuelerTabelle from './SUvPlanungsSchuelerTabelle.vue';
	import { uvDragDatenSchreiben } from './UvDragDaten';
	import { type UvPlanungsAuswahl, type UvZuordnungBearbeitung, uvZuordnungTitel } from './UvPlanung';
	import { useUvPlanungsManager } from './UvPlanungsKontext';
	import type { UvZuordnungsAuswahl } from './UvPlanungsManager';

	const props = defineProps<{
		bearbeitung: UvZuordnungBearbeitung;
		hinzufuegen: (id: number) => Promise<void>;
		schliessen: () => void;
		aenderungAktiv: boolean;
	}>();

	const planungsManager = useUvPlanungsManager();
	const suche = ref('');
	const nurGeeignete = ref(false);
	const speichert = ref(false);
	const auswahlIds = ref<number[]>([]);
	const sichtbareAnzahl = ref(100);
	const fehler = ref('');
	let eingebunden = true;

	const bedienungGesperrt = computed(() => speichert.value || props.aenderungAktiv);
	const titel = computed(() => uvZuordnungTitel[props.bearbeitung.art]);
	const istLehrerZuordnung = computed(() => props.bearbeitung.art === 'lerngruppe-lehrer' || props.bearbeitung.art === 'klasse-lehrer');
	const zielAuswahl = computed<UvPlanungsAuswahl>(() => ({
		art: props.bearbeitung.art === 'klasse-lehrer' ? 'klassen' : 'lerngruppen',
		id: props.bearbeitung.idZiel,
	}));
	// Kandidaten und bereits zugeordnete Einträge

	const daten = computed<UvZuordnungsAuswahl>(() => planungsManager.value?.zuordnungsAuswahl(props.bearbeitung, nurGeeignete.value)
		?? { eintraege: [], lehrer: [], schueler: [], zugeordnet: 0, verfuegbareIds: new Set<number>() });
	const zugeordnet = computed(() => daten.value.zugeordnet);
	const verfuegbareSchueler = computed(() => daten.value.schueler);
	const treffer = computed(() => planungsManager.value?.kandidatenSuchen(daten.value.eintraege, suche.value) ?? []);
	const sichtbar = computed(() => treffer.value.slice(0, sichtbareAnzahl.value));
	const lehrerTreffer = computed(() => planungsManager.value?.lehrerKandidatenSuchen(daten.value.lehrer, suche.value) ?? []);

	const ausgewaehlteSchueler = computed({
		get: () => verfuegbareSchueler.value.filter(item => auswahlIds.value.includes(item.idSchueler)),
		set: (items: UvPlanungsabschnittSchueler[]) => {
			if (!bedienungGesperrt.value) {
				auswahlIds.value = items.map(item => item.idSchueler);
			}
		},
	});

	watch(() => daten.value.verfuegbareIds, ids => {
		auswahlIds.value = auswahlIds.value.filter(id => ids.has(id));
	});

	watch([suche, nurGeeignete], () => {
		sichtbareAnzahl.value = 100;
	});

	onUnmounted(() => {
		eingebunden = false;
	});

	function istEingebunden(): boolean {
		return eingebunden;
	}

	function ziehenStarten(event: DragEvent, id: number): void {
		if (bedienungGesperrt.value || !daten.value.verfuegbareIds.has(id) || event.dataTransfer === null) {
			event.preventDefault();
			return;
		}
		uvDragDatenSchreiben(event.dataTransfer, { typ: 'zuordnung', aktion: 'hinzufuegen', art: props.bearbeitung.art, idZiel: props.bearbeitung.idZiel, id });
	}

	function auswahlUmschalten(id: number): void {
		if (bedienungGesperrt.value || !daten.value.verfuegbareIds.has(id)) {
			return;
		}
		auswahlIds.value = auswahlIds.value.includes(id) ? auswahlIds.value.filter(item => item !== id) : [...auswahlIds.value, id];
	}

	function auswahlHinzufuegen(): Promise<void> {
		return zuordnungenHinzufuegen([...auswahlIds.value]);
	}

	function eintragHinzufuegen(id: number): Promise<void> {
		return zuordnungenHinzufuegen([id]);
	}

	async function zuordnungenHinzufuegen(ids: number[]): Promise<void> {
		if (!istEingebunden() || bedienungGesperrt.value) {
			return;
		}
		speichert.value = true;
		fehler.value = '';
		try {
			for (const id of ids) {
				if (!istEingebunden()) {
					return;
				}
				if (!daten.value.verfuegbareIds.has(id)) {
					continue;
				}
				await props.hinzufuegen(id);
				if (!istEingebunden()) {
					return;
				}
				auswahlIds.value = auswahlIds.value.filter(item => item !== id);
			}
		} catch (error) {
			if (istEingebunden()) {
				fehler.value = error instanceof Error ? error.message : 'Die Zuordnung konnte nicht gespeichert werden.';
			}
		} finally {
			if (istEingebunden()) {
				speichert.value = false;
			}
		}
	}
</script>
