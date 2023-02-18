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
	import { KlassenDaten, LehrerListeEintrag, PersonalTyp } from "@svws-nrw/svws-core-ts";
	import type { DataTableColumn } from "@svws-nrw/svws-ui";

	type Lehrer = {
		kuerzel?: string;
		nachname?: string;
		vorname?: string;
		typ?: string;
	}

	const props = defineProps<{
		data: KlassenDaten,
		mapLehrer: Map<number, LehrerListeEintrag>
	}>();

	const liste: ComputedRef<Lehrer[]> = computed(() => {
		if (props.data.klassenLeitungen === undefined)
			return [];
		return (props.data.klassenLeitungen?.toArray() as number[]).map((id) => {
			const lehrer = props.mapLehrer.get(id);
			if (lehrer === undefined)
				return {};
			return {
				kuerzel: lehrer.kuerzel,
				nachname: lehrer.nachname,
				vorname: lehrer.vorname,
				typ: PersonalTyp.fromBezeichnung(lehrer.personTyp)?.bezeichnung ?? undefined
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
