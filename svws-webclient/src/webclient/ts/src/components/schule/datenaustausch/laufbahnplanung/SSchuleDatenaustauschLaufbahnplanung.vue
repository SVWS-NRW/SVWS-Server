<template>
	<div v-if="visible" class="app-container">
		<s-card-schule-datenaustausch-laufbahnplanung :set-gost-lupo-import-m-d-b-fuer-jahrgang="setGostLupoImportMDBFuerJahrgang" />
	</div>
</template>

<script setup lang="ts">
	import { computed, ComputedRef } from "vue";

	const props = defineProps<{
		setGostLupoImportMDBFuerJahrgang: (formData: FormData) => Promise<boolean>;
	}>();

	const visible: ComputedRef<boolean> = computed(() => {
		//return this.$app.lehrerStammdaten.visible; //TODO: richtige Bedingung einpflegen
		return true;
	});
</script>
