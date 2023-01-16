<template>
	<svws-ui-content-card title="Klassenleitungen">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-table :columns="cols" :data="liste" :footer="false" />
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";
	import { LehrerListeEintrag, PersonalTyp } from "@svws-nrw/svws-core-ts";
	import { DataKlasse } from "~/apps/klassen/DataKlasse";
	import { ListLehrer } from "~/apps/lehrer/ListLehrer";
	import type { DataTableColumn } from "@svws-nrw/svws-ui";

	type Lehrer = {
		kuerzel?: string;
		nachname?: string;
		vorname?: string;
		typ?: string;
	}

	const { data, mapLehrer } = defineProps<{
		data: DataKlasse,
		listLehrer: ListLehrer,
		mapLehrer: Map<Number, LehrerListeEintrag>
	}>();

	const liste: ComputedRef<Lehrer[]> = computed(() => {
		if (data.daten?.klassenLeitungen === undefined)
			return [];
		return (data.daten.klassenLeitungen?.toArray() as Number[]).map((id : Number) => {
			const lehrer = mapLehrer.get(id);
			if (lehrer === undefined)
				return {};
			return {
				kuerzel: lehrer.kuerzel.toString(),
				nachname: lehrer.nachname.toString(),
				vorname: lehrer.vorname.toString(),
				typ: PersonalTyp.fromBezeichnung(lehrer.personTyp)?.bezeichnung.toString() || undefined
			};
		});
	});

	const cols: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel", span: 1, sortable: false },
		{ key: "nachname", label: "Nachname", span: 1, sortable: false },
		{ key: "vorname", label: "Vorname", span: 1, sortable: false },
		{ key: "personTyp", label: "Typ", span: 1, sortable: false },
	];

</script>
