<template>
	<BlockungsregelBase v-model="regel" @update:model-value="e => emit('update:modelValue', e)" :regel-typ="regel_typ" :regeln="regeln" :get-ergebnismanager="getErgebnismanager"
		@regel-hinzugefuegen="regel_hinzufuegen" @regel-speichern="emit('regelSpeichern')" @regel-entfernen="e=>emit('regelEntfernen', e)" :disabled="disabled" :cols="cols">
		<template #regelRead="{regel: r}">
			<div class="svws-ui-td" role="cell">
				{{ GostKursart.fromID(r.parameter.get(0)).beschreibung }}
			</div>
			<div class="svws-ui-td" role="cell">
				{{ r.parameter.get(1) }}
			</div>
			<div class="svws-ui-td" role="cell">
				{{ r.parameter.get(2) }}
			</div>
		</template>
		<template #regelEdit>
			<parameter-kursart v-model="kursart" label="Nur diese Kursart" />
			<parameter-schiene v-model="start" :schienen="schienen" label="von Schiene" />
			<parameter-schiene v-model="ende" :schienen="schienen" label="bis Schiene" />
		</template>
	</BlockungsregelBase>
</template>

<script setup lang="ts">

	import type { GostBlockungRegel, GostBlockungSchiene, GostBlockungsergebnisManager} from "@core";
	import type { WritableComputedRef } from "vue";
	import {useRegelParameterKursart, useRegelParameterSchiene} from '../composables';
	import { GostKursart, GostKursblockungRegelTyp } from "@core";
	import {computed} from "vue";
	import type {DataTableColumn} from "@ui";

	const props = defineProps<{
		getErgebnismanager: () => GostBlockungsergebnisManager;
		modelValue: GostBlockungRegel | undefined;
		schienen: Iterable<GostBlockungSchiene>;
		regeln: GostBlockungRegel[];
		disabled: boolean;
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

	const cols: DataTableColumn[] = [
		{key: 'kursart', label: 'Kursart allein in Schienen', span: 2},
		{key: 'von', label: 'von'},
		{key: 'bis', label: 'bis'},
	]

</script>
