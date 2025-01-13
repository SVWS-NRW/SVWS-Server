<template>
	<div class="page--content">
		<div class="enm-leistung-uebersicht-bereich">
			<enm-leistungen-uebersicht :manager :patch-leistung :columns-visible :set-columns-visible />
		</div>
		<div class="enm-floskel-editor-bereich">
			<enm-floskeleditor :manager :patch-leistung="doPatchLeistung" :erlaubte-hauptgruppen :bemerkung />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed } from 'vue';
	import type { EnmLeistungenProps } from './EnmLeistungenProps';

	const props = defineProps<EnmLeistungenProps>();

	const erlaubteHauptgruppen = new Set<'FACH'|'ALLG'>(['FACH', 'ALLG']);

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

	const bemerkung = computed(() => {
		return props.manager.auswahlLeistung.leistung?.fachbezogeneBemerkungen ?? null;
	})
</script>

<style lang="postcss" scoped>

	.page--content {
		@apply flex flex-row gap-6;
		@apply h-full w-full overflow-hidden;
		@apply max-w-full;
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