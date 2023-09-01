<template>
	<svws-ui-content-card title="Allgemein">
		<div class="input-wrapper">
			<svws-ui-text-input placeholder="ID" v-model="id" type="text" readonly />
			<svws-ui-text-input placeholder="Kürzel" :model-value="data.kuerzel" @blur="kuerzel=>doPatch({kuerzel})" type="text" />
			<div class="col-span-2">
				<svws-ui-text-input placeholder="Beschreibung" :model-value="data.beschreibung" @blur="beschreibung=>doPatch({beschreibung})" type="text" />
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { Aufsichtsbereich } from "@core";
	import { computed } from "vue";

	const props = defineProps<{
		data: Aufsichtsbereich
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<Aufsichtsbereich>): void;
	}>()

	function doPatch(data: Partial<Aufsichtsbereich>) {
		emit('patch', data);
	}

	const id = computed<number | undefined>(() => props.data.id);

</script>
