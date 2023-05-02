<template>
	<div class="px-7 3xl:px-8 pb-4 bg-light">
		<div class="flex justify-between gap-4 bg-white rounded-lg px-3 pt-4 pb-3 -mx-3">
			<span class="text-headline-sm inline-flex items-center gap-1">
				{{ regel_typ.bezeichnung }}
			</span>
			<svws-ui-toggle v-model="hatRegel" />
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

