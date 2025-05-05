<template>
	<div>
		<svws-ui-button :disabled="selected.length === 0" @click="toggle_modal" size="small" type="transparent" class="hover--danger subNavigationFocusField">
			<span class="icon-sm i-ri-delete-bin-line" />
			Alle ausgewählten Schüler-Fachwahlen löschen
		</svws-ui-button>
		<svws-ui-modal v-model:show="show" size="medium" type="danger">
			<template #modalTitle>
				Die Fachwahlen aller ausgewählten Schüler im Abiturjahrgang löschen
			</template>
			<template #modalDescription>
				<div class="flex gap-1 mb-2">
					Sollen für alle ausgewählten Schüler des Abiturjahrgangs die Fachwahlen gelöscht werden?
				</div>
			</template>
			<template #modalActions>
				<svws-ui-button @click="toggle_modal" type="secondary">Abbrechen</svws-ui-button>
				<svws-ui-button @click="loeschen_fachwahlen_selected" type="danger">Ja</svws-ui-button>
			</template>
		</svws-ui-modal>
	</div>
</template>

<script setup lang="ts">

	import { ref } from 'vue';
	import type { GostBelegpruefungsErgebnisse } from '../../../../../core/src/core/data/gost/GostBelegpruefungsErgebnisse';

	const props = defineProps<{
		selected: GostBelegpruefungsErgebnisse[];
		loeschenFachwahlenSelected: (ergebnisse: Iterable<GostBelegpruefungsErgebnisse>) => Promise<void>;
		resetAuswahl: () => void;
	}>();

	const show = ref<boolean>(false);

	function toggle_modal() {
		show.value = !show.value;
	}

	async function loeschen_fachwahlen_selected() {
		show.value = false;
		await props.loeschenFachwahlenSelected(props.selected);
		props.resetAuswahl();
	}

</script>
