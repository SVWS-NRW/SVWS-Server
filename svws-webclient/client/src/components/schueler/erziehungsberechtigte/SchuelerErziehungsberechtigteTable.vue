<template>
	<svws-ui-table class="contentFocusField"
		:items="sortedData"
		:columns
		:no-data="data().size() === 0"
		clickable
		:clicked="erzieher"
		@update:clicked="value => emit('update:erzieher',value)"
		:model-value="selectedErz"
		@update:model-value="value => emit('update:selectedErz', value)"
		:selectable="hatKompetenzUpdate"
		focus-first-element>
		<template #header(erhaeltAnschreiben)>
			<svws-ui-tooltip>
				<span class="icon i-ri-mail-send-line" />
				<template #content>Erhält Anschreiben</template>
			</svws-ui-tooltip>
		</template>
		<template #cell(idErzieherArt)="{ value }">
			{{ erzieherartenById.get(value)?.bezeichnung ?? '' }}
		</template>
		<template #cell(name)="{ rowData }">
			{{ rowData.vorname }} {{ rowData.nachname }}
		</template>
		<template #cell(eMail)="{ value: eMail }">
			{{ eMail ? eMail : '—' }}
		</template>
		<template #cell(adresse)="{ rowData }">
			{{ strasse(rowData) }}{{ rowData.wohnortID && orteById?.get(rowData.wohnortID) ? `, ${orteById.get(rowData.wohnortID)?.plz} ${orteById?.get(rowData.wohnortID)?.ortsname}` : '' }}
		</template>
		<template #cell(erhaeltAnschreiben)="{ value: erhaeltAnschreiben }">
			{{ erhaeltAnschreiben ? '&check;' : '&times;' }}
		</template>
		<template #cell(actions)="{ rowData }">
			<svws-ui-button v-if="isSuffix1(rowData.id) && !hasSuffix2(rowData.id) && hatKompetenzUpdate" @click.stop="emit('openModalForPos2', rowData)"> + </svws-ui-button>
		</template>
		<template #actions v-if="hatKompetenzUpdate">
			<svws-ui-button @click="emit('deleteErzieher')" type="trash" :disabled="selectedErz.length === 0" />
			<svws-ui-button @click="emit('addModal')" type="icon" title="Erziehungsberechtigten hinzufügen">
				<span class="icon i-ri-add-line" />
			</svws-ui-button>
		</template>
	</svws-ui-table>
</template>

<script setup lang="ts">
	import type { Erzieherart } from "@core/core/data/erzieher/Erzieherart";
	import type { ErzieherStammdaten } from "@core/core/data/erzieher/ErzieherStammdaten";
	import { AdressenUtils } from "@core/core/utils/AdressenUtils";
	import type { List } from "@core/java/util/List";
	import { useOrteState } from "@ui/states/kataloge/OrteState";
	import type { DataTableColumn } from "@ui/types";
	import { computed } from "vue";

	const props = defineProps<{
		data: () => List<ErzieherStammdaten>;
		erzieherartenById: Map<number, Erzieherart>;
		hatKompetenzUpdate: boolean;
		erzieher: ErzieherStammdaten | undefined;
		selectedErz: ErzieherStammdaten[];
	}>();

	const emit = defineEmits<{
		'update:erzieher': [value: ErzieherStammdaten | undefined];
		'update:selectedErz': [value: ErzieherStammdaten[]];
		'openModalForPos2': [item: ErzieherStammdaten];
		'deleteErzieher': [];
		'addModal': [];
	}>();

	const orteState = useOrteState();

	const orteById = computed(() => orteState.orte.byId);
	const erzieherList = computed(() => Array.from(props.data()));
	const suffix2Ids = computed(() => new Set(erzieherList.value.map(e => e.id)));

	const sortedData = computed(() => [...erzieherList.value].sort((a, b) => {
		const ersteErzId = Math.floor(a.id / 10);
		const zweiteErzId = Math.floor(b.id / 10);
		if (ersteErzId !== zweiteErzId) {
			return ersteErzId - zweiteErzId;
		}
		return a.id - b.id;
	}));

	const columns: DataTableColumn[] = [
		{ key: "idErzieherArt", label: "Art" },
		{ key: "name", label: "Name" },
		{ key: "eMail", label: "E-Mail" },
		{ key: "adresse", label: "Adresse" },
		{ key: "erhaeltAnschreiben", label: "Anschreiben", tooltip: "Erhält Anschreiben", fixedWidth: 3, align: "center" },
		{ key: "actions", label: "2. Person", tooltip: "Weiteres Elternteil hinzufügen", fixedWidth: 10, align: "center" },
	];

	function strasse(erzieher: ErzieherStammdaten) {
		return AdressenUtils.combineStrasse(erzieher.strassenname ?? "", erzieher.hausnummer ?? "", erzieher.hausnummerZusatz ?? "");
	}

	function isSuffix1(id: number): boolean {
		return id % 10 === 1;
	}

	function hasSuffix2(id: number): boolean {
		return suffix2Ids.value.has(id + 1);
	}
</script>
