<template>
	<div class="page--content page--content--full h-full">
		<div class="grid grid-cols-1 gap-16">
			<s-card-schule-datenaustausch-laufbahnplanung :set-gost-lupo-import-m-d-b-fuer-jahrgang="setGostLupoImportMDBFuerJahrgang" />
		</div>
	</div>
	<Teleport to=".svws-schule-datenauschtausch-header-target" v-if="isMounted">
		<span class="inline-block mr-3">Import aus LuPO</span>
		<br>
		<span class="opacity-50"><i-ri-download2-line /></span>
	</Teleport>
</template>

<script setup lang="ts">

	import { ref, onMounted } from 'vue';
	import { type SimpleOperationResponse } from '@core';

	const props = defineProps<{
		setGostLupoImportMDBFuerJahrgang: (formData: FormData, mode: 'none' | 'schueler' | 'all') => Promise<SimpleOperationResponse>;
	}>();

	const isMounted = ref(false);
	onMounted(() => isMounted.value = true);

</script>
