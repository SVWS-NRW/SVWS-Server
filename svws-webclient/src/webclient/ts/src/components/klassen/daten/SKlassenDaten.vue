<template>
	<div v-if="visible" class="app-container">
		<s-card-klasse-basisdaten/>
	</div>
</template>

<script setup lang="ts">

	import { KlassenListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";

	const props = defineProps<{ id?: number; item?: KlassenListeEintrag, routename: string }>();

	const visible: ComputedRef<boolean> = computed(() => {
		//return this.$app.afaecher.visible; //TODO: richtige Bedingung einpflegen
		return true;
	});

</script>
