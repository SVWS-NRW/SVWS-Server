<template>
	<div class="page gap-6 overflow-y-auto">
		<svws-ui-table :items="abgaenge()" :columns>
			<template #header>
				<tr class="svws-ui-tr grid-cols-7">
					<th id="col_auswahl" class="svws-ui-td svws-align-left col-span-1" aria-label="Alle auswählen">
						<svws-ui-checkbox :model-value="abgaenge().size() === auswahl.size" :indeterminate="someSelected" @update:model-value="selectAll" />
					</th>
					<th id="col_id_schueler" class="svws-ui-td svws-align-left col-span-2" aria-label="ID Schüler"> ID Schüler </th>
					<th id="col_status" class="svws-ui-td svws-align-left col-span-2" aria-label="status"> Status </th>
					<th id="col_last_modified" class="svws-ui-td svws-align-left col-span-2" aria-label="Letzte Änderung"> Letzte Änderung </th>
				</tr>
			</template>
			<template #rowCustom="{ row: abgang }">
				<tr class="svws-ui-tr grid-cols-7">
					<td class="svws-ui-td svws-align-left cursor-pointer col-span-1">
						<svws-ui-checkbox :model-value="auswahl.has(abgang)" @update:model-value="auswahl.has(abgang) ? auswahl.delete(abgang) : auswahl.add(abgang)" />
					</td>
					<td class="svws-ui-td svws-align-left col-span-2">
						<span> {{ abgang.idSchueler }} </span>
					</td>
					<td class="svws-ui-td svws-align-left col-span-2">
						<span> {{ getStatusBezeichnung(abgang.idStatus) }} </span>
					</td>
					<td class="svws-ui-td svws-align-left col-span-2">
						<span> {{ abgang.lastModified }} </span>
					</td>
				</tr>
			</template>
			<template #actions>
				<svws-ui-button :disabled="!auswahl.size">Datensätze senden</svws-ui-button>
			</template>
		</svws-ui-table>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";

	import type { SchulwechselAbgang } from "@core/core/data/schule/SchulwechselAbgang";
	import { StatusSchulwechselAbgang } from "@core/core/types/schule/StatusSchulwechselAbgang";

	import type { SchulwechselAbgaengeProps } from "~/components/schule/datenaustausch/schulwechsel/SchulwechselAbgaengeProps";

	const props = defineProps<SchulwechselAbgaengeProps>();

	const columns = [
		{ key: 'auswahl', label: 'Auswahl', span: 1 },
		{ key: 'idSchueler', label: 'ID Schüler', span: 2 },
		{ key: 'status', label: 'Status', span: 2 },
		{ key: 'lastModified', label: 'Letzte Änderung', span: 2 },
	];

	const auswahl = ref<Set<SchulwechselAbgang>>(new Set<SchulwechselAbgang>());

	const someSelected = computed(() => {
		return auswahl.value.size >= 1 && auswahl.value.size < props.abgaenge().size();
	});

	function getStatusBezeichnung(idStatus: number): string | undefined {
		return StatusSchulwechselAbgang.getByIdOrNull(idStatus)?.toString();
	}

	function selectAll() {
		const allSelected = (props.abgaenge().size() === auswahl.value.size);
		if (allSelected) {
			auswahl.value.clear();
		} else {
			for (const leistung of props.abgaenge()) {
				auswahl.value.add(leistung);
			}
		}
	}

</script>
