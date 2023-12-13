<template>
	<div class="page--content page--content--full">
		<s-card-schule-datenaustausch-untis :set-untis-import-g-p-u="setUntisImportGPU" :abschnitte="abschnitte" :akt-abschnitt="aktAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
	</div>
	<Teleport to=".svws-schule-datenauschtausch-header-target" v-if="isMounted">
		<span class="inline-block mr-3">Import aus Untis</span>
		<br>
		<span class="opacity-50"><i-ri-download2-line /></span>
	</Teleport>
</template>

<script setup lang="ts">

	import { ref, onMounted } from 'vue';
	import type { SchuleDatenaustauschUntisProps } from './SSchuleDatenaustauschUntisProps';

	const props = defineProps<SchuleDatenaustauschUntisProps>();

	const isMounted = ref(false);
	onMounted(() => isMounted.value = true);

</script>
