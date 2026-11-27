<template>
	<svws-ui-content-card :title="title ?? 'Schüler'">
		<template #actions v-if="state.hatKompetenzAendern">
			<svws-ui-button v-if="bearbeitung === null" type="secondary" @click="props.bearbeiten(gruppe.id)">Bearbeiten</svws-ui-button>
		</template>
		<p class="mb-2 text-xs text-ui-secondary">Die Zuordnung gilt für alle Klassen, Kurse und Lerngruppen mit dieser Schülergruppe.</p>
		<s-uv-planungs-schueler-tabelle :selected="[]" :items="mitglieder" :gruppe :selectable="false" :navigation-disabled="bearbeitung !== null"
			:removable="kannEntfernen" :draggable="kannEntfernen" :disabled="zuordnungPending"
			:schueler-oeffnen="gotoSchueler" :klasse-oeffnen="gotoKlasse" :entfernen @drag-start="dragStart">
			<template #no-data>Keine Schüler für diese Auswahl vorhanden.</template>
		</s-uv-planungs-schueler-tabelle>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed } from 'vue';

	import type { UvSchuelergruppe } from '@core/core/data/uv/UvSchuelergruppe';
	import { useUvState } from '@ui/states/UvState';

	import SUvPlanungsSchuelerTabelle from '../planung/SUvPlanungsSchuelerTabelle.vue';
	import { uvDragDatenSchreiben } from '../planung/UvDragDaten';
	import type { UvZuordnungBearbeitung } from '../planung/UvPlanung';

	const props = defineProps<{
		gruppe: UvSchuelergruppe;
		title?: string;
		bearbeitung: UvZuordnungBearbeitung | null;
		zuordnungPending: boolean;
		bearbeiten: (idGruppe: number) => void;
		schuelerEntfernen: (idGruppe: number, idSchueler: number) => Promise<void>;
		gotoSchueler: (id: number) => void;
		gotoKlasse: (id: number) => void;
	}>();

	const state = useUvState();
	const kannEntfernen = computed(() => state.hatKompetenzAendern
		&& props.bearbeitung?.art === 'gruppe-schueler' && props.bearbeitung.idZiel === props.gruppe.id);
	const mitglieder = computed(() => [...state.uvManager.planungsabschnittSchuelerGetMengeBySchuelergruppe(props.gruppe)]);

	function dragStart(event: DragEvent, idSchueler: number): void {
		if (!kannEntfernen.value || props.zuordnungPending || event.dataTransfer === null) {
			event.preventDefault();
			return;
		}
		uvDragDatenSchreiben(event.dataTransfer, {
			typ: 'zuordnung', aktion: 'entfernen', art: 'gruppe-schueler', idZiel: props.gruppe.id, id: idSchueler,
		});
	}

	function entfernen(idSchueler: number): void {
		if (kannEntfernen.value && !props.zuordnungPending) {
			void props.schuelerEntfernen(props.gruppe.id, idSchueler);
		}
	}
</script>
