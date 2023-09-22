<template>
	<div class="page--content page--content--full">
		<s-card-schule-datenaustausch-laufbahnplanung :set-gost-lupo-import-m-d-b-fuer-jahrgang="setGostLupoImportMDBFuerJahrgang" />
	</div>
</template>

<script setup lang="ts">

	import { type SimpleOperationResponse } from '@core';

	const props = defineProps<{
		setGostLupoImportMDBFuerJahrgang: (formData: FormData) => Promise<SimpleOperationResponse>;
	}>();

</script>
