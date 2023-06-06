<template>
	<BlockungsregelBase v-model="regel" @update:model-value="e => emit('update:modelValue', e)" :regel-typ="regel_typ" :regeln="regeln"
		@regel-hinzugefuegen="regel_hinzufuegen" @regel-speichern="emit('regelSpeichern')" @regel-entfernen="e=>emit('regelEntfernen', e)">
		<template #beschreibung="{ regel: r }">
			{{ GostKursart.fromID(r.parameter.get(0)).beschreibung }} alleine in Schiene {{ r.parameter.get(1) }} bis {{ r.parameter.get(2) }}
		</template>
		Nur
		<parameter-kursart v-model="kursart" />
		von
		<parameter-schiene v-model="start" :schienen="schienen" />
		bis
		<parameter-schiene v-model="ende" :schienen="schienen" />
	</BlockungsregelBase>
</template>

<script setup lang="ts">

	import type { GostBlockungRegel, GostBlockungSchiene} from "@core";
	import { GostKursart, GostKursblockungRegelTyp } from "@core";
	import type { WritableComputedRef } from "vue";
	import { computed } from "vue";
	import { useRegelParameterKursart, useRegelParameterSchiene } from '../composables';

	const props = defineProps<{
		modelValue: GostBlockungRegel | undefined;
		schienen: GostBlockungSchiene[];
		regeln: GostBlockungRegel[];
	}>();

	const emit = defineEmits<{
		(e: 'update:modelValue', v: GostBlockungRegel | undefined): void;
		(e: 'regelSpeichern'): void;
		(e: 'regelEntfernen', v: GostBlockungRegel): void;
	}>()

	const regel: WritableComputedRef<GostBlockungRegel | undefined> = computed({
		get: () => props.modelValue,
		set: (value) => emit('update:modelValue', value)
	});

	const regel_typ = GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS

	const kursart = useRegelParameterKursart(regel, 0)
	const start = useRegelParameterSchiene(props.schienen, regel, 1)
	const ende = useRegelParameterSchiene(props.schienen, regel, 2)

	const regel_hinzufuegen = (r: GostBlockungRegel) => {
		r.parameter.add(1);
		r.parameter.add(1);
		r.parameter.add(1);
		regel.value = r;
	}

</script>
