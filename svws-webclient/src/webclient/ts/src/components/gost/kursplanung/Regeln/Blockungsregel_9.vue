<template>
	<div>
		<div class="flex justify-between items-center" :class="{'mb-2' : hatRegel}">
			<h5 class="text-sm font-bold leading-loose pr-4 py-1">{{ regel_typ.bezeichnung }}</h5>
			<svws-ui-checkbox v-model="hatRegel" />
		</div>
		<div v-if="hatRegel">
			<svws-ui-radio-group>
				<svws-ui-radio-option v-model="regel" :value="false" name="interne" label="externe Lehrkräfte nicht beachten" />
				<svws-ui-radio-option v-model="regel" :value="true" name="externe" label="alle Lehrkräfte beachten" />
			</svws-ui-radio-group>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { GostBlockungRegel, GostKursblockungRegelTyp } from "@svws-nrw/svws-core-ts";
	import { computed, Ref, ref, WritableComputedRef } from "vue";

	const props = defineProps<{
		modelValue: GostBlockungRegel | undefined;
		regeln: GostBlockungRegel[];
	}>();


	const emit = defineEmits<{
		(e: 'update:modelValue', v: GostBlockungRegel | undefined): void;
		(e: 'regelSpeichern'): void;
		(e: 'regelEntfernen', v: GostBlockungRegel): void;
	}>()

	const regel_typ = GostKursblockungRegelTyp.LEHRKRAFT_BEACHTEN

	const extern: Ref<boolean> = ref(true);

	const regel: WritableComputedRef<boolean> = computed({
		get: () => props.modelValue?.parameter.get(0) === 0 ? false : true,
		set: (value) => {
			props.modelValue?.parameter.set(0, value ? 1 : 0);
			emit('update:modelValue', props.modelValue);
		}
	});

	const hatRegel: WritableComputedRef<boolean> = computed({
		get: () => props.regeln.length === 1 ? true : false,
		set: (val) => {
			if (val === true) {
				const r = new GostBlockungRegel();
				r.typ = regel_typ.typ;
				r.parameter.add(extern.value ? 1 : 0);
				emit('update:modelValue', r);
				emit('regelSpeichern');
			} else {
				if (props.regeln[0] !== undefined)
					emit('regelEntfernen', props.regeln[0])
			}
		}
	})

</script>

