<template>
	<BlockungsregelBase v-model="regel" @update:model-value="e => emit('update:modelValue', e)" :regel-typ="regel_typ" :regeln="regeln"
		@regel-hinzugefuegen="regel_hinzufuegen" @regel-speichern="emit('regelSpeichern')" @regel-entfernen="e=>emit('regelEntfernen', e)" :disabled="disabled">
		<template #beschreibung="{ regel: r }">
			{{ GostKursart.fromID(r.parameter.get(0)).beschreibung }}, von Schiene {{ r.parameter.get(1) }} bis {{ r.parameter.get(2) }} gesperrt
		</template>
		Sperre
		<parameter-kursart v-model="kursart" />
		von
		<parameter-schiene v-model="start" :schienen="schienen" />
		bis
		<parameter-schiene v-model="ende" :schienen="schienen" />
	</BlockungsregelBase>
</template>

<script setup lang="ts">
	import type { GostBlockungRegel, GostBlockungSchiene} from "@core";
	import type { WritableComputedRef } from "vue";
	import { useRegelParameterKursart, useRegelParameterSchiene } from '../composables';
	import { GostKursart, GostKursblockungRegelTyp } from "@core";
	import { computed } from "vue";

	const props = defineProps<{
		modelValue: GostBlockungRegel | undefined;
		schienen: Iterable<GostBlockungSchiene>;
		regeln: GostBlockungRegel[];
		disabled: boolean;
	}>();

	const emit = defineEmits<{
		(e: 'update:modelValue', v: GostBlockungRegel | undefined): void;
		(e: 'regelSpeichern'): void;
		(e: 'regelEntfernen', v: GostBlockungRegel): void;
	}>();

	const regel: WritableComputedRef<GostBlockungRegel | undefined> = computed({
		get: () => props.modelValue,
		set: (value) => emit('update:modelValue', value)
	});

	const regel_typ = GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS;

	const kursart = useRegelParameterKursart(regel, 0)
	// eslint-disable-next-line vue/no-setup-props-destructure
	const start = useRegelParameterSchiene(props.schienen, regel, 1)
	// eslint-disable-next-line vue/no-setup-props-destructure
	const ende = useRegelParameterSchiene(props.schienen, regel, 2)

	const regel_hinzufuegen = (r: GostBlockungRegel) => {
		r.parameter.add(1);
		r.parameter.add(1);
		r.parameter.add(1);
		regel.value = r;
	}

</script>

