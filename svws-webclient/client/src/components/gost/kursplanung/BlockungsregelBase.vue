<template>
	<svws-ui-content-card has-background>
		<svws-ui-table :items="regelnFilter" :no-data="false" :columns="cols ? [{key: 'information', label: ' ', fixedWidth: 2}, ...cols, {key: 'entfernen', label: ' ', fixedWidth: 2}] : [{key: 'regel'}, {key: 'entfernen', label: ' ', fixedWidth: 3.25}]" scroll clickable :class="{'mb-10': modelValue?.typ === regelTyp.typ && !disabled}">
			<template #header v-if="cols === undefined">
				<div class="svws-ui-tr" role="row">
					<div class="svws-ui-td col-span-full" role="columnheader">{{ regelTyp.bezeichnung }}</div>
				</div>
			</template>
			<template #rowCustom="{row: r}">
				<div :key="r.id" class="svws-ui-tr" :class="{'svws-clicked': modelValue?.id === r.id, 'bg-red-400': regelverletzung(r)}" role="row" @click="select_regel(r)">
					<svws-ui-tooltip v-if="regelverletzung(r)" autosize>
						<div class="svws-ui-td" role="cell">
							<i-ri-information-line />
						</div>
						<template #content>
							<template v-for="text in getErgebnismanager().regelGetMengeAnVerletzungen(GostKursblockungRegelTyp.fromTyp(r.typ))" :key="text">
								<br>{{ text }}
							</template>
						</template>
					</svws-ui-tooltip>
					<div v-else class="svws-ui-td" role="cell" />
					<slot name="regelRead" :regel="r" />
					<div class="svws-ui-td" role="cell">
						<svws-ui-button type="icon" @click.stop="regelEntfernen(r)" v-if="!disabled" :disabled="apiStatus.pending"><i-ri-delete-bin-line /></svws-ui-button>
					</div>
				</div>
				<span />
			</template>
			<template #footer v-if="modelValue?.typ === regelTyp.typ && !disabled">
				<div class="mt-7 p-3" :class="{'opacity-50': apiStatus.pending}">
					<div class="flex gap-1"><slot name="regelEdit" /></div>
					<div class="flex gap-1 mt-3 justify-end">
						<svws-ui-button type="transparent" @click="abbrechen" :disabled="apiStatus.pending">Abbrechen</svws-ui-button>
						<svws-ui-button @click="regelSpeichern" :disabled="apiStatus.pending">
							<template v-if="modelValue?.id === -1">Regel hinzufügen</template>
							<template v-else>Speichern</template>
						</svws-ui-button>
					</div>
				</div>
			</template>
			<template #actions>
				<svws-ui-button @click="regelHinzufuegen" type="icon" :disabled="modelValue?.typ === regelTyp.typ || apiStatus.pending" v-if="!disabled" class="mr-1"><i-ri-add-line /></svws-ui-button>
			</template>
		</svws-ui-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { ComputedRef} from "vue";
	import { computed } from "vue";
	import type { DataTableColumn } from "@ui";
	import type { ApiStatus } from "~/components/ApiStatus";
	import type { GostBlockungsergebnisManager , GostBlockungRegel } from "@core";
	import { GostKursblockungRegelTyp } from "@core";

	type DataTableColumnSource = DataTableColumn | string

	const props = defineProps<{
		getErgebnismanager: () => GostBlockungsergebnisManager;
		modelValue: GostBlockungRegel | undefined;
		regelTyp: GostKursblockungRegelTyp;
		regeln: Map<GostKursblockungRegelTyp, ComputedRef<GostBlockungRegel[]>>;
		cols?: DataTableColumnSource[];
		disabled: boolean;
		regelEntfernen: (regel: GostBlockungRegel) => Promise<void>;
		regelSpeichern: () => Promise<void>;
		regelHinzufuegen: () => void;
		apiStatus: ApiStatus;
	}>()

	const emit = defineEmits<{
		(e: 'update:modelValue', v: GostBlockungRegel | undefined): void;
	}>()

	function abbrechen() {
		if (props.apiStatus.pending)
			return;
		emit('update:modelValue', undefined);
	}
	function select_regel(r: GostBlockungRegel) {
		emit('update:modelValue', r);
	}

	function regelverletzung(r: GostBlockungRegel) {
		for (const v of props.getErgebnismanager().getErgebnis().bewertung.regelVerletzungen)
			if (v === r.id)
				return true;
		return false;
	}

	const regelnFilter = computed(() => props.regeln.get(props.regelTyp)?.value);

</script>

