<template>
	<svws-ui-select v-model="selected" :items="kurse" :item-text="text" />
</template>

<script setup lang="ts">

	import type { GostBlockungKurs, GostFach} from "@core";
	import type { WritableComputedRef } from "vue";
	import { GostKursart } from "@core";
	import { computed } from "vue";

	const props = defineProps<{
		modelValue: GostBlockungKurs;
		mapFaecher: Map<number, GostFach>;
		kurse: Iterable<GostBlockungKurs>;
	}>();

	const emit = defineEmits(['update:modelValue'])
	const selected: WritableComputedRef<GostBlockungKurs> = computed({
		get: () => props.modelValue,
		set: (value: GostBlockungKurs) => emit('update:modelValue', value)
	});

	function text(i: GostBlockungKurs) {
		return `${props.mapFaecher.get(i.fach_id)?.kuerzel}-${i.kursart > 0 ? GostKursart.fromID(i.kursart) : 'kursart-fehlt' }${i.nummer}${i.suffix ? "-"+i.suffix:""}`
	}

</script>
