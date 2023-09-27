<template>
	<svws-ui-content-card has-background>
		<svws-ui-table :items="[]" :no-data="false" :columns="cols ? [...cols, {key: 'entfernen', label: ' ', fixedWidth: 2}] : [{key: 'regel'}, {key: 'entfernen', label: ' ', fixedWidth: 3.25}]" scroll clickable :class="{'mb-10': modelValue?.typ === regelTyp.typ && !disabled}">
			<template #header v-if="cols === undefined">
				<div class="svws-ui-tr" role="row">
					<div class="svws-ui-td col-span-full" role="columnheader">{{ regelTyp.bezeichnung }}</div>
				</div>
			</template>
			<template #body>
				<div v-for="r in regeln" :key="r.id" class="svws-ui-tr" :class="{'svws-clicked': modelValue?.id === r.id}" role="row" @click="select_regel(r)">
					<slot name="regelRead" :regel="r" />
					<div class="svws-ui-td" role="cell">
						<svws-ui-button type="icon" @click.stop="regel_entfernen(r)" v-if="!disabled" :disabled="pending"><i-ri-delete-bin-line /></svws-ui-button>
					</div>
				</div>
				<span />
			</template>
			<template #footer v-if="modelValue?.typ === regelTyp.typ && !disabled">
				<div class="mt-7 p-3" :class="{'opacity-50': pending}">
					<div class="flex gap-1"><slot name="regelEdit" /></div>
					<div class="flex gap-1 mt-3 justify-end">
						<svws-ui-button type="transparent" @click="emit('update:modelValue', undefined)" :disabled="pending">Abbrechen</svws-ui-button>
						<svws-ui-button @click="speichern" :disabled="pending">
							<template v-if="modelValue?.id === -1">Regel hinzufügen</template>
							<template v-else>Speichern</template>
						</svws-ui-button>
					</div>
				</div>
			</template>
			<template #actions>
				<svws-ui-button @click="regel_hinzufuegen" type="icon" :disabled="modelValue?.typ === regelTyp.typ || pending" v-if="!disabled" class="mr-1"><i-ri-add-line /></svws-ui-button>
			</template>
		</svws-ui-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { GostKursblockungRegelTyp } from "@core";
	import { GostBlockungRegel } from "@core";
	import type {DataTableColumn} from "@ui";
	import {api} from "~/router/Api";
	import {computed, type ComputedRef} from "vue";

	type DataTableColumnSource = DataTableColumn | string

	const props = defineProps<{
		modelValue: GostBlockungRegel | undefined;
		regelTyp: GostKursblockungRegelTyp;
		regeln: GostBlockungRegel[];
		cols?: DataTableColumnSource[];
		disabled: boolean;
	}>()

	const emit = defineEmits<{
		(e: 'update:modelValue', v: GostBlockungRegel | undefined): void;
		(e: 'regelHinzugefuegen', v: GostBlockungRegel): void;
		(e: 'regelSpeichern'): void;
		(e: 'regelEntfernen', v: GostBlockungRegel): void;
	}>()

	const pending: ComputedRef<boolean> = computed(() => api.status.pending);

	function select_regel(r: GostBlockungRegel) {
		emit('update:modelValue', r);
	}

	const regel_hinzufuegen = () => {
		const r = new GostBlockungRegel()
		r.typ = props.regelTyp.typ
		emit('regelHinzugefuegen', r)
	}

	const speichern = async () => {
		if (!props.modelValue)
			return;
		emit('regelSpeichern');
	}

	const regel_entfernen = async (r: GostBlockungRegel) => {
		emit('regelEntfernen', r)
	}

</script>

