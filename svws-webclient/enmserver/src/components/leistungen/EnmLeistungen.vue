<template>
	<div class="page--content">
		<div class="enm-leistung-uebersicht-bereich">
			<enm-leistungen-uebersicht :manager :patch-leistung :columns-visible :set-columns-visible />
		</div>
		<div class="enm-floskel-editor-bereich">
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

<style lang="postcss" scoped>

	.page--content {
		@apply flex flex-row gap-6;
		@apply h-full w-full overflow-hidden;
		@apply max-w-full pb-4;
	}

	.enm-leistung-uebersicht-bereich {
		@apply w-full h-full overflow-hidden;
		@apply flex-grow;
		@apply max-w-[100rem];
	}

	.enm-floskel-editor-bereich {
		@apply w-full h-full overflow-hidden;
		@apply max-w-196;
	}

</style>