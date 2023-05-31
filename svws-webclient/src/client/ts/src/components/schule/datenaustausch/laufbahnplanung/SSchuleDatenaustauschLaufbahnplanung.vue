<template>
	<div class="page--content">
		<s-card-schule-datenaustausch-laufbahnplanung :set-gost-lupo-import-m-d-b-fuer-jahrgang="setGostLupoImportMDBFuerJahrgang" />
	</div>
</template>

<script setup lang="ts">

	const props = defineProps<{
		setGostLupoImportMDBFuerJahrgang: (formData: FormData) => Promise<boolean>;
	}>();

</script>
