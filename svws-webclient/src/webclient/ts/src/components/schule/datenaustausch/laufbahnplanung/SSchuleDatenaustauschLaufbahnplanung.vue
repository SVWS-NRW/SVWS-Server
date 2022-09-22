<template>
	<div v-if="visible" class="app-container">
		<s-card-schule-datenaustausch-laufbahnplanung />
	</div>
</template>

<script setup lang="ts">
	import { computed, ComputedRef } from "vue";

	const visible: ComputedRef<boolean> = computed(() => {
		//return this.$app.lehrerStammdaten.visible; //TODO: richtige Bedingung einpflegen
		return true;
	});
</script>
