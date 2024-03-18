<template>
	<svws-ui-content-card has-background>
		<svws-ui-table :items="regeln" :no-data="false" :columns="[{key: 'information', label: ' ', fixedWidth: 2}, ...columns, {key: 'entfernen', label: ' ', fixedWidth: 3}]" scroll clickable>
			<template #header(entfernen)>
				<svws-ui-button @click="regelHinzufuegen" type="icon" :disabled="modelValue?.typ === regelTyp.typ || apiStatus.pending" v-if="!disabled" class="mr-1">
					<span class="icon i-ri-add-line" />
				</svws-ui-button>
			</template>
			<template #rowCustom="{row: r}">
				<div :key="r.id" class="svws-ui-tr" :class="{'svws-clicked': modelValue?.id === r.id, 'bg-error': verletzungen.contains(r)}" role="row" @click="select_regel(r)">
					<svws-ui-tooltip v-if="verletzungen.contains(r)" autosize>
						<div class="svws-ui-td" role="cell">
							<span class="icon i-ri-information-line" />
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
						<svws-ui-button type="icon" @click.stop="regelEntfernen(r)" v-if="!disabled" :disabled="apiStatus.pending">
							<span class="icon i-ri-delete-bin-line" />
						</svws-ui-button>
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
		</svws-ui-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { DataTableColumn } from "@ui";
	import type { ApiStatus } from "~/components/ApiStatus";
	import type { GostBlockungsergebnisManager, GostBlockungRegel, GostBlockungsdatenManager, List } from "@core";
	import { ArrayList, GostKursblockungRegelTyp } from "@core";

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

	const verletzungen = computed(() => props.getErgebnismanager().getErgebnis().bewertung.regelVerletzungen);

	const regeln = computed(()=> {
		if (props.nurRegelverletzungen) {
			const list: List<GostBlockungRegel> = new ArrayList();
			for (const r of props.getDatenmanager().regelGetListeOfTyp(props.regelTyp))
				if (verletzungen.value.contains(r.id))
					list.add(r);
			return list;
		}
		return props.getDatenmanager().regelGetListeOfTyp(props.regelTyp);
	});

	function abbrechen() {
		if (props.apiStatus.pending)
			return;
		emit('update:modelValue', undefined);
	}

	function select_regel(r: GostBlockungRegel) {
		emit('update:modelValue', r);
	}

</script>

