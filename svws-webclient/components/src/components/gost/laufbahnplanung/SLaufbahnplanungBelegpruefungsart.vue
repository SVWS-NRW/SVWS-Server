<template>
	<svws-ui-radio-group class="radio--row">
		<svws-ui-radio-option v-model="art" value="ef1" name="ef1" label="EF.1" />
		<svws-ui-radio-option v-model="art" value="gesamt" name="gesamt" label="Gesamt" />
		<svws-ui-radio-option v-model="art" value="auto" name="gesamt" label="Automatisch" v-if="noAuto !== true" />
	</svws-ui-radio-group>
</template>


<script setup lang="ts">

	import { computed, type WritableComputedRef } from 'vue';

	const props = withDefaults(defineProps<{
		modelValue?: 'ef1'|'gesamt'|'auto';
		noAuto?: boolean
	}>(), {
		modelValue: () => 'gesamt'
	});

	const emit = defineEmits<{
		(e: 'update:modelValue', value: 'ef1'|'gesamt'|'auto'): void,
	}>();

	const art: WritableComputedRef<string> = computed({
		get: () => props.modelValue,
		set: (value) => {
			const tmp = value;
			if (tmp === null)
				throw new Error("Unerwarteter Fehler in Komponente: Ungültige Option ausgewählt: " + value);
			emit('update:modelValue', tmp);
		}
	});

</script>
