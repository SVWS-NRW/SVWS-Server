<template>
	<div class="pl-5 pr-3 bg-light rounded-lg mb-3">
		<div class="flex gap-4 px-3 pt-4 pb-3 -mx-3">
			<svws-ui-checkbox type="toggle" v-model="hatRegel" :disabled="disabled">
				{{ regel_typ.bezeichnung }}
			</svws-ui-checkbox>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { GostBlockungRegel, GostKursblockungRegelTyp } from "@core";
	import { computed } from "vue";

	const props = defineProps<{
		regeln: GostBlockungRegel[];
		disabled: boolean;
	}>();

	const emit = defineEmits<{
		(e: 'update:modelValue', v: GostBlockungRegel | undefined): void;
		(e: 'regelSpeichern'): void;
		(e: 'regelEntfernen', v: GostBlockungRegel): void;
	}>()

	const regel_typ = GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN

	const hatRegel = computed<boolean>({
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

