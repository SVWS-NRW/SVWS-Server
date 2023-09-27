<template>
	<div class="page--content page--content--full h-full">
		<div class="grid grid-cols-1 gap-16">
			<s-card-schule-datenaustausch-laufbahnplanung :set-gost-lupo-import-m-d-b-fuer-jahrgang="setGostLupoImportMDBFuerJahrgang" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { type SimpleOperationResponse } from '@core';

	const props = defineProps<{
		setGostLupoImportMDBFuerJahrgang: (formData: FormData, mode: 'none' | 'schueler' | 'all') => Promise<SimpleOperationResponse>;
	}>();

</script>
