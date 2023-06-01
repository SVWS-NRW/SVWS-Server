<template>
	<svws-ui-content-card title="Basisdaten">
		<div class="input-wrapper">
			<svws-ui-text-input placeholder="ID" v-model="id" type="text" readonly />
			<svws-ui-text-input placeholder="Kürzel" v-model="kuerzel" type="text" />
			<div class="col-span-2">
				<svws-ui-text-input placeholder="Beschreibung" v-model="beschreibung" type="text" />
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { ComputedRef, WritableComputedRef } from "vue";
	import type { Aufsichtsbereich } from "@svws-nrw/svws-core";
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

	const id: ComputedRef<number | undefined> = computed(() => props.data.id);

	const kuerzel: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.kuerzel,
		set: (value) => doPatch({ kuerzel: value })
	});

	const beschreibung: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.beschreibung,
		set: (value) => doPatch({ beschreibung: value })
	});

</script>
