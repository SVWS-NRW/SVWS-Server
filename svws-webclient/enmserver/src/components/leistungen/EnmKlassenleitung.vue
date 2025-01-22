<template>
	<div class="page--content">
		<div class="enm-leistung-uebersicht-bereich">
			<enm-klassenleitung-uebersicht :manager :patch-bemerkungen :patch-lernabschnitt :columns-visible :set-columns-visible @hauptgruppe="erlaubteHauptgruppe = $event" />
		</div>
		<div v-if="floskelEditorVisible" class="enm-floskel-editor-bereich">
			<enm-floskeleditor :manager :patch="doPatchBemerkungen" :erlaubte-hauptgruppe />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watchEffect } from 'vue';
	import type { BemerkungenHauptgruppe } from './EnmManager';
	import type { EnmKlassenleitungProps } from './EnmKlassenleitungProps';

	const props = defineProps<EnmKlassenleitungProps>();

	const erlaubteHauptgruppe = ref<BemerkungenHauptgruppe>('ZB');

	const floskelEditorVisible = computed(() => {
		const cols = props.columnsVisible();
		return (cols.get('AUE') ?? false)
			|| (cols.get('ASV') ?? false)
			|| (cols.get('ZB') ?? false);
	})

	watchEffect(() => {
		if (props.manager.klassenAuswahlGetSchueler().contains(props.manager.auswahlSchueler))
			return;
		props.manager.auswahlSchuelerNaechster();
	})

	async function doPatchBemerkungen(bemerkung: string|null) {
		const schueler = props.manager.auswahlSchueler ?? null;
		if (schueler === null)
			return;
		switch (erlaubteHauptgruppe.value) {
			case 'ASV':
				schueler.bemerkungen.ASV = bemerkung;
				break;
			case 'AUE':
				schueler.bemerkungen.AUE = bemerkung;
				break;
			case 'ZB':
				schueler.bemerkungen.ZB = bemerkung;
				break;
			default:
				return;
		}
		await props.patchBemerkungen(schueler.id, schueler.bemerkungen);
		props.manager.update();
	}

</script>

<style lang="postcss" scoped>

.page--content {
		@apply flex flex-row gap-6;
		@apply h-full w-full overflow-hidden;
		@apply max-w-full pb-4;
	}

	.enm-leistung-uebersicht-bereich {
		@apply w-full h-full overflow-hidden;
		@apply flex-grow;
	}

	.enm-floskel-editor-bereich {
		@apply h-full overflow-hidden;
		@apply max-w-196;
	}

</style>