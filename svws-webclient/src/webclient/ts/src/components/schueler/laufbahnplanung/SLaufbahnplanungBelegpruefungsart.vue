<template>
	<div class="flex flex-row m-2">
		<div class="mr-2"> Art: </div>
		<div class="mr-2"> <svws-ui-radio-option v-model="art" :value="GostBelegpruefungsArt.EF1.kuerzel" name="ef1" label="EF.1" /> </div>
		<div class="mr-2"> <svws-ui-radio-option v-model="art" :value="GostBelegpruefungsArt.GESAMT.kuerzel" name="gesamt" label="Gesamt" /> </div>
	</div>
</template>


<script setup lang="ts">

	import { GostBelegpruefungsArt } from '@svws-nrw/svws-core-ts';
	import { computed, WritableComputedRef } from 'vue';

	const props = withDefaults(defineProps<{
		modelValue?: GostBelegpruefungsArt;
	}>(), {
		modelValue: () => GostBelegpruefungsArt.GESAMT
	});

	const emit = defineEmits<{
		(e: 'update:modelValue', value: GostBelegpruefungsArt): void,
	}>();

	const art: WritableComputedRef<string> = computed({
		get: () => props.modelValue.kuerzel,
		set: (value) => {
			const tmp = GostBelegpruefungsArt.fromKuerzel(value);
			if (tmp === null)
				throw new Error("Unerwarteter Fehler in Komponente: Ungültige Option ausgewählt: " + value);
			emit('update:modelValue', tmp);
		}
	});

</script>
