<template>
	<svws-ui-content-card title="Basisdaten">
		<div class="input-wrapper">
			<svws-ui-text-input placeholder="ID" v-model="id" type="text" readonly />
			<svws-ui-text-input placeholder="Kürzel" v-model="kuerzel" type="text" />
			<svws-ui-text-input placeholder="Größe" v-model="groesse" type="number" />
			<div class="col-span-2">
				<svws-ui-text-input placeholder="Beschreibung" v-model="beschreibung" type="text" />
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { ComputedRef, WritableComputedRef } from "vue";
	import type { Raum } from "@svws-nrw/svws-core";
	import { computed } from "vue";


	const props = defineProps<{
		data: Raum
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<Raum>): void;
	}>()

	function doPatch(data: Partial<Raum>) {
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
	const groesse: WritableComputedRef<number | undefined> = computed({
		get: () => props.data.groesse,
		set: (value) => doPatch({ groesse: value })
	});

</script>
