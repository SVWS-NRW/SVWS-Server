<template>
	<svws-ui-header>
		<span class="inline-block mr-3">Import aus LuPO</span>
		<br>
		<span class="opacity-50"><span class="i-ri-download-2-line icon-xl inline-block" /></span>
	</svws-ui-header>
	<div class="svws-ui-page w-full">
		<div class="svws-ui-tab-content">
			<div class="page--content page--content--full h-full">
				<div class="grid grid-cols-1 gap-16 h-full w-full">
					<s-card-schule-datenaustausch-laufbahnplanung :set-gost-lupo-import-m-d-b-fuer-jahrgang="setGostLupoImportMDBFuerJahrgang" />
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { type SimpleOperationResponse } from '@core';

	const props = defineProps<{
		setGostLupoImportMDBFuerJahrgang: (formData: FormData, mode: 'none' | 'schueler' | 'all') => Promise<SimpleOperationResponse>;
	}>();

</script>
