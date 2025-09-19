<template>
	<svws-ui-content-card has-background>
		<svws-ui-table :items="aufgeklappt ? regeln : []" :no-data="false" :columns="cols" scroll clickable>
			<template #header(information)>
				<div v-if="regeln.size()" @click.stop="aufgeklappt = !aufgeklappt">
					<span v-if="aufgeklappt" class="icon i-ri-arrow-down-s-line cursor-pointer" />
					<span v-else class="icon i-ri-arrow-right-s-line cursor-pointer" />
				</div>
			</template>
			<template #[`header(${props.columns.at(0)?.key})`]="{ column }">
				<span v-if="!aufgeklappt && (regeln.size() > 0)" class="rounded-md bg-ui-brand p-1 text-ui-onbrand cursor-pointer inline-block">{{ regeln.size() }}</span>
				<span class="line-clamp-1 break-all leading-tight">{{ column.label }}</span>
			</template>
			<template #header(entfernen)>
				<svws-ui-button @click="regelHinzufuegen" type="icon" :disabled="modelValue?.typ === regelTyp.typ || apiStatus.pending" v-if="!disabled" class="mr-1">
					<span class="icon i-ri-add-line" />
				</svws-ui-button>
			</template>
			<template #rowCustom="{row: r}">
				<div :key="r.id" class="svws-ui-tr" role="row" @click="select_regel(r)" :style="gridTemplateColumns"
					:class="{'svws-clicked': modelValue?.id === r.id, 'bg-ui-danger text-ui-ondanger hover:text-ui-ondanger-hover': verletzungen.get(r.id)}">
					<svws-ui-tooltip v-if="verletzungen.get(r.id)" autosize>
						<div class="svws-ui-td" role="cell">
							<span class="icon-ui-ondanger icon i-ri-information-line" />
						</div>
						<template #content> {{ verletzungen.get(r.id) }} </template>
					</svws-ui-tooltip>
					<div v-else class="svws-ui-td" role="cell" />
					<slot name="regelRead" :regel="r" />
					<div class="svws-ui-td" role="cell">
						<svws-ui-button type="icon" @click.stop="regelEntfernen(r)" v-if="!disabled" :disabled="apiStatus.pending">
							<span class="icon i-ri-delete-bin-line" />
						</svws-ui-button>
					</div>
				</div>
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
		</svws-ui-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { DataTableColumn } from "@ui";
	import type { ApiStatus } from "~/components/ApiStatus";
	import type { GostBlockungsergebnisManager, GostBlockungsdatenManager, List } from "@core";
	import { GostBlockungRegel, ArrayList, GostKursblockungRegelTyp } from "@core";

	const props = defineProps<{
		getErgebnismanager: () => GostBlockungsergebnisManager;
		getDatenmanager: () => GostBlockungsdatenManager;
		modelValue: GostBlockungRegel | undefined;
		regelTyp: GostKursblockungRegelTyp;
		columns: DataTableColumn[];
		disabled: boolean;
		regelEntfernen: (regel: GostBlockungRegel) => Promise<void>;
		regelSpeichern: () => Promise<void>;
		regelHinzufuegen: () => void;
		apiStatus: ApiStatus;
		nurRegelverletzungen: boolean;
	}>()

	const emit = defineEmits<{
		(e: 'update:modelValue', v: GostBlockungRegel | undefined): void;
	}>()

	// eslint-Regel wird verletzt. props.regelTyp wird aber nicht geändert, es gibt für jede Regel eine Komponente
	// eslint-disable-next-line vue/no-setup-props-reactivity-loss
	const aufgeklappt = ref<boolean>(![GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE, GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE].includes(props.regelTyp));

	const verletzungen = computed(() => props.getErgebnismanager().regelGetMap_regelID_to_verletzungString());

	const regeln = computed(() => {
		const list: List<GostBlockungRegel> = new ArrayList();
		if (props.nurRegelverletzungen) {
			for (const r of props.getDatenmanager().regelGetListeOfTyp(props.regelTyp))
				if (verletzungen.value.get(r.id) !== null)
					list.add(r);
			return list;
		}
		list.addAll(props.getDatenmanager().regelGetListeOfTyp(props.regelTyp));
		return list;
	});

	function abbrechen() {
		if (props.apiStatus.pending)
			return;
		emit('update:modelValue', undefined);
	}

	function select_regel(r: GostBlockungRegel) {
		const regel = GostBlockungRegel.transpilerFromJSON(GostBlockungRegel.transpilerToJSON(r));
		emit('update:modelValue', regel);
	}

	const cols = computed(() => [ { key: 'information', label: ' ', fixedWidth: 2 }, ...props.columns, { key: 'entfernen', label: ' ', fixedWidth: 3 } ]);

	const gridTemplateColumns = computed<string>(() =>
		"grid-template-columns: " + cols.value.map(column => {
			const fixedWidth = Number(column.fixedWidth ?? 0);
			const minWidth = Number(column.minWidth ?? 0);
			const span = column.span ?? 1;
			const min = (fixedWidth > 0) ? (fixedWidth + 'rem') : (minWidth > 0 ? (minWidth + 'rem') : '4rem');
			const max = (fixedWidth > 0) ? (fixedWidth + 'rem') : span + 'fr';
			return `minmax(${min}, ${max})`
		}).join(' '));

</script>

