<template>
	<div class="page page-flex-row">
		<div class="grow w-full h-full overflow-hidden">
			<enm-klassenleitung-uebersicht :manager :patch-bemerkungen :patch-lernabschnitt :columns-visible :set-columns-visible @hauptgruppe="erlaubteHauptgruppe = $event" />
		</div>
		<div v-if="floskelEditorVisible" class="min-w-196 max-w-196 h-full overflow-hidden">
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
