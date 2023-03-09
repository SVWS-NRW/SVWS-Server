<template>
	<div>
		<div class="flex justify-between items-center" :class="{'mb-2' : hatRegel}">
			<h5 class="text-sm font-bold leading-loose pr-4 py-1">{{ regel_typ.bezeichnung }}</h5>
			<svws-ui-checkbox v-model="hatRegel" />
		</div>
		<div v-if="hatRegel">
			<svws-ui-radio-group>
				<svws-ui-radio-option v-model="regel" value="interne" name="interne" label="externe Lehrkräfte nicht beachten" />
				<svws-ui-radio-option v-model="regel" value="externe" name="externe" label="alle Lehrkräfte beachten" />
			</svws-ui-radio-group>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { GostBlockungRegel, GostKursblockungRegelTyp } from "@svws-nrw/svws-core";
	import { computed, WritableComputedRef, shallowRef, ShallowRef, watch } from "vue";

	const props = defineProps<{
		regeln: GostBlockungRegel[];
	}>();

	const emit = defineEmits<{
		(e: 'update:modelValue', v: GostBlockungRegel | undefined): void;
		(e: 'regelSpeichern'): void;
		(e: 'regelEntfernen', v: GostBlockungRegel): void;
	}>()

	const regel_typ = GostKursblockungRegelTyp.LEHRKRAFT_BEACHTEN

	const regel: ShallowRef<'externe'|'interne'> = shallowRef('interne');

	watch(()=>regel.value, (value)=> {
		props.regeln[0]?.parameter.set(0, value === 'interne' ? 0 : 1);
		emit('update:modelValue', props.regeln[0]);

	})

	const hatRegel: WritableComputedRef<boolean> = computed({
		get: () => props.regeln.length === 0 ? false : true,
		set: (val) => {
			if (val === true) {
				const r = new GostBlockungRegel();
				r.typ = regel_typ.typ;
				r.parameter.add(1);
				emit('update:modelValue', r);
				emit('regelSpeichern');
			} else {
				if (props.regeln[0] !== undefined)
					emit('regelEntfernen', props.regeln[0]);
			}
		}
	})
</script>

