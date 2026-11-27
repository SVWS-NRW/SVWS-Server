<template>
	<section class="flex h-full min-h-0 flex-col overflow-hidden rounded-lg border border-ui-brand bg-ui shadow-lg">
		<div class="flex shrink-0 items-center justify-between gap-2 border-b border-ui-25 p-3">
			<div class="min-w-0"><h3 class="font-semibold">Schüler zuordnen</h3><p class="text-xs text-ui-secondary">{{ items.length }} verfügbar</p></div>
			<svws-ui-button type="secondary" @click="props.schliessen()">Bearbeiten beenden</svws-ui-button>
		</div>
		<div class="flex shrink-0 items-center justify-between gap-2 px-3 pt-3">
			<p class="text-xs text-ui-secondary">Per Plus oder Drag-and-drop hinzufügen. Die Zuordnung wird beim Erstellen gespeichert.</p>
			<svws-ui-button v-if="selected.length > 0"
				type="secondary"
				@click="auswahlHinzufuegen">
				{{ selected.length }} ausgewählte hinzufügen
			</svws-ui-button>
		</div>
		<p class="px-3 pt-1 text-xs text-ui-secondary">Neu hinzugefügte Schüler hier ablegen, um sie wieder zu entfernen.</p>
		<div class="min-h-0 flex-1 overflow-auto p-3">
			<s-uv-planungs-schueler-tabelle v-model:selected="selected" :items selectable draggable filterbar :navigation-disabled="true"
				:hinzufuegen="props.hinzufuegen" @drag-start="dragStartHinzufuegen">
				<template #no-data>Keine weiteren Schüler verfügbar.</template>
			</s-uv-planungs-schueler-tabelle>
		</div>
	</section>
</template>

<script setup lang="ts">
	import { ref } from 'vue';

	import type { UvPlanungsabschnittSchueler } from '@core/core/data/uv/UvPlanungsabschnittSchueler';

	import SUvPlanungsSchuelerTabelle from './SUvPlanungsSchuelerTabelle.vue';
	import { uvDragDatenSchreiben } from './UvDragDaten';

	const props = defineProps<{
		items: UvPlanungsabschnittSchueler[];
		schliessen: () => void;
		hinzufuegen: (id: number) => void;
	}>();

	const selected = ref<UvPlanungsabschnittSchueler[]>([]);

	function auswahlHinzufuegen(): void {
		for (const schueler of selected.value) {
			if (props.items.some(item => item.idSchueler === schueler.idSchueler)) {
				props.hinzufuegen(schueler.idSchueler);
			}
		}
		selected.value = [];
	}

	function dragStartHinzufuegen(event: DragEvent, idSchueler: number): void {
		if (event.dataTransfer === null) {
			return;
		}
		uvDragDatenSchreiben(event.dataTransfer, { typ: 'lerngruppe-entwurf', aktion: 'hinzufuegen', id: idSchueler });
	}
</script>
