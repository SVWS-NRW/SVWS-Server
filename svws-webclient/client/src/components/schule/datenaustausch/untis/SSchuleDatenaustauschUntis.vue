<template>
	<div class="page--content page--content--full">
		<s-card-schule-datenaustausch-untis :set-untis-import-g-p-u="setUntisImportGPU" :abschnitte="abschnitte" :akt-abschnitt="aktAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
	</div>
</template>

<script setup lang="ts">
	import type { SchuleDatenaustauschUntisProps } from './SSchuleDatenaustauschUntisProps';

	const props = defineProps<SchuleDatenaustauschUntisProps>();

</script>
