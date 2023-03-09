<template>
	<svws-ui-multi-select v-model="selected" :items="kurse" :item-text="text" />
</template>

<script setup lang="ts">

	import { GostBlockungKurs, GostFach, GostKursart } from "@svws-nrw/svws-core";
	import { computed, WritableComputedRef } from "vue";

	const props = defineProps<{
		modelValue: GostBlockungKurs;
		mapFaecher: Map<number, GostFach>;
		kurse: GostBlockungKurs[];
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
