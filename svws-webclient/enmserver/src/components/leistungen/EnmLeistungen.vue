<template>
	<div class="page page-flex-row">
		<div class="grow w-full h-full overflow-hidden">
			<enm-leistungen-uebersicht :manager :patch-leistung :columns-visible :set-columns-visible />
		</div>
		<div v-if="columnsVisible().get('Bemerkung')" class="min-w-196 max-w-196 h-full overflow-hidden">
			<enm-floskeleditor :manager :patch="doPatchLeistung" erlaubte-hauptgruppe="FACH" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { watchEffect } from 'vue';
	import type { EnmLeistungenProps } from './EnmLeistungenProps';

	const props = defineProps<EnmLeistungenProps>();

	watchEffect(() => {
		if (props.manager.lerngruppenAuswahlGetLeistungOrNull(props.manager.auswahlLeistung.leistung?.id ?? null) !== null)
			return;
		props.manager.auswahlLeistungNaechste();
	})

	async function doPatchLeistung(fachbezogeneBemerkungen: string|null) {
		if (props.manager.auswahlLeistung.leistung === null)
			return;
		const id = props.manager.auswahlLeistung.leistung.id
		const patch = { id, fachbezogeneBemerkungen };
		const success = await props.patchLeistung(patch);
		if (success)
			Object.assign(props.manager.auswahlLeistung.leistung, patch);
		props.manager.update();
	}

</script>
