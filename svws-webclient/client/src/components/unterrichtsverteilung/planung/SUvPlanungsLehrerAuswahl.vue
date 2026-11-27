<template>
	<section class="flex h-full min-h-0 flex-col overflow-hidden rounded-lg border border-ui-brand bg-ui shadow-lg">
		<div class="flex shrink-0 items-center justify-between gap-2 border-b border-ui-25 p-3">
			<div class="min-w-0"><h3 class="font-semibold">UV-Lehrkräfte hinzufügen</h3><p class="text-xs text-ui-secondary">{{ daten.anzahl }} im Planungsabschnitt · {{ daten.kandidaten.length }} verfügbar</p></div>
			<svws-ui-button type="secondary" :disabled="pending" @click="props.schliessen()">Bearbeiten beenden</svws-ui-button>
		</div>
		<div class="shrink-0 border-b border-ui-10 p-3"><svws-ui-text-input v-model="suche" type="search" placeholder="Lehrkräfte suchen" removable /></div>
		<div class="flex shrink-0 flex-wrap items-center justify-between gap-2 border-b border-ui-10 px-3 py-2">
			<label class="flex items-center gap-2 text-sm"><input type="checkbox" :checked="alleSichtbarenSelektiert" :disabled="pending || sichtbar.length === 0" @change="alleSichtbarenUmschalten">Alle auswählen</label>
			<svws-ui-button v-if="selectedIds.length > 0"
				type="secondary"
				:disabled="pending"
				@click="auswahlHinzufuegen">
				Selektierte hinzufügen ({{ selectedIds.length }})
			</svws-ui-button>
		</div>
		<p class="px-3 pt-2 text-xs text-ui-secondary">Per Plus oder Drag-and-drop hinzufügen. Lehrkräfte aus dem Planungsabschnitt hier ablegen, um sie zu entfernen. Änderungen werden sofort gespeichert.</p>
		<div class="min-h-0 flex-1 overflow-auto p-3">
			<div v-if="sichtbar.length === 0" class="text-sm text-ui-secondary">Keine weiteren Lehrkräfte für diesen Planungsabschnitt verfügbar.</div>
			<div class="grid grid-cols-[repeat(auto-fill,minmax(13rem,1fr))] content-start gap-3">
				<s-uv-planungs-lehrer-kachel v-for="eintrag in sichtbar"
					:key="eintrag.id"
					modus="abschnitt-hinzufuegen"
					:ausgewaehlt="selectedIds.includes(eintrag.id)"
					:kuerzel="eintrag.zusatz"
					:name="eintrag.text"
					:faecher="manager?.lehrerFaecher(eintrag.id) ?? []"
					:pending
					:auswahl-umschalten="() => auswahlUmschalten(eintrag.id)"
					:zuordnen="() => props.hinzufuegen([eintrag.id])"
					@drag-start="event => dragStart(event, eintrag.id)" />
			</div>
		</div>
	</section>
</template>

<script setup lang="ts">
	import { computed, ref, watch } from 'vue';

	import SUvPlanungsLehrerKachel from './SUvPlanungsLehrerKachel.vue';
	import { uvDragDatenSchreiben } from './UvDragDaten';
	import { useUvPlanungsManager } from './UvPlanungsKontext';

	const props = defineProps<{
		pending: boolean;
		schliessen: () => void;
		hinzufuegen: (ids: number[]) => void;
	}>();

	const suche = ref('');
	const selectedIds = ref<number[]>([]);
	const manager = useUvPlanungsManager();
	const daten = computed(() => manager.value?.lehrerPlanungsabschnittAuswahl() ?? { kandidaten: [], verwendet: new Set<number>(), anzahl: 0 });
	const sichtbar = computed(() => daten.value.kandidaten.filter(item => `${item.zusatz} ${item.text}`.toLocaleLowerCase('de').includes(suche.value.trim().toLocaleLowerCase('de'))));
	const alleSichtbarenSelektiert = computed(() => sichtbar.value.length > 0 && sichtbar.value.every(item => selectedIds.value.includes(item.id)));
	watch(suche, () => {
		selectedIds.value = [];
	});
	watch(() => daten.value.kandidaten, kandidaten => {
		const ids = new Set(kandidaten.map(item => item.id));
		selectedIds.value = selectedIds.value.filter(id => ids.has(id));
	});

	function auswahlUmschalten(id: number): void {
		selectedIds.value = selectedIds.value.includes(id) ? selectedIds.value.filter(item => item !== id) : [...selectedIds.value, id];
	}

	function alleSichtbarenUmschalten(): void {
		selectedIds.value = alleSichtbarenSelektiert.value ? [] : sichtbar.value.map(item => item.id);
	}

	function auswahlHinzufuegen(): void {
		if (selectedIds.value.length > 0) {
			props.hinzufuegen([...selectedIds.value]);
		}
	}

	function dragStart(event: DragEvent, id: number): void {
		if (event.dataTransfer === null) {
			return;
		}
		uvDragDatenSchreiben(event.dataTransfer, { typ: 'lehrer-mitgliedschaft', aktion: 'hinzufuegen', id });
	}
</script>
