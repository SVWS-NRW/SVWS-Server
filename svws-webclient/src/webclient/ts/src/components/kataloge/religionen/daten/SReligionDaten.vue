<template>
	<div v-if="visible" class="app-container">
		<s-card-religion-daten />
	</div>
</template>

<script setup lang="ts">

	import { ReligionEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";

	const props = defineProps<{ id?: number; item?: ReligionEintrag, routename: string }>();

	const visible: ComputedRef<boolean> = computed(() => {
		// TODO: richtige Bedingung einpflegen
		return true;
	});

</script>