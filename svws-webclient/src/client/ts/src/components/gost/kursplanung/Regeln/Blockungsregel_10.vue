<template>
	<div>
		<div class="flex justify-between items-center" :class="{'mb-2' : hatRegel}">
			<h5 class="text-sm font-bold leading-loose pr-4 py-1">{{ regel_typ.bezeichnung }}</h5>
			<svws-ui-checkbox v-model="hatRegel" />
		</div>
	</div>
</template>

<script setup lang="ts">
	import { GostBlockungRegel, GostKursblockungRegelTyp } from "@svws-nrw/svws-core";
	import type { WritableComputedRef } from "vue";
	import { computed } from "vue";

	const props = defineProps<{
		regeln: GostBlockungRegel[];
	}>();
	console.log(props.regeln)

	const emit = defineEmits<{
		(e: 'update:modelValue', v: GostBlockungRegel | undefined): void;
		(e: 'regelSpeichern'): void;
		(e: 'regelEntfernen', v: GostBlockungRegel): void;
	}>()

	const regel_typ = GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN

	const hatRegel: WritableComputedRef<boolean> = computed({
		get: () => props.regeln.length === 0 ? false : true,
		set: (val) => {
			if (val === true) {
				const r = new GostBlockungRegel();
				r.typ = regel_typ.typ;
				emit('update:modelValue', r);
				emit('regelSpeichern');
			} else {
				if (props.regeln[0] !== undefined)
					emit('regelEntfernen', props.regeln[0]);
			}
		}
	})
</script>

