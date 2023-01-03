<template>
	<svws-ui-content-card title="Klassenleitungen">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-table :columns="cols" :data="liste" :footer="false">
					<template #cell-kuerzel="{row}">
						{{ kuerzel(row) }}
					</template>
					<template #cell-nachname="{row}">
						{{ nachname(row) }}
					</template>
					<template #cell-vorname="{row}">
						{{ vorname(row) }}
					</template>
					<template #cell-personTyp="{row}">
						{{ personTyp(row) }}
					</template>
				</svws-ui-table>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, ref } from "vue";
	import { LehrerListeEintrag, PersonalTyp } from "@svws-nrw/svws-core-ts";
	import { DataKlasse } from "~/apps/klassen/DataKlasse";

	const { data, mapLehrer } = defineProps<{ 
		data: DataKlasse,
		mapLehrer: Map<Number, LehrerListeEintrag>
	}>();

	const liste: ComputedRef<Number[]> = computed(() => data.daten?.klassenLeitungen?.toArray() as Number[] || []);

	const cols = ref([
		{ key: "kuerzel", label: "Kürzel", span: "1", sortable: false },
		{ key: "nachname", label: "Nachname", span: "1", sortable: false },
		{ key: "vorname", label: "Vorname", span: "1", sortable: false },
		{ key: "personTyp", label: "Typ", span: "1", sortable: false },
	]);

	const kuerzel = (id: Number): string | undefined  => mapLehrer.get(id)?.kuerzel.toString();
	const nachname = (id: Number): string | undefined  => mapLehrer.get(id)?.nachname.toString();
	const vorname = (id: Number): string | undefined  => mapLehrer.get(id)?.vorname.toString();
	const personTyp = (id: Number): string | undefined  => {
		const personTyp = mapLehrer.get(id)?.personTyp;
		return personTyp === undefined ? undefined : PersonalTyp.fromBezeichnung(personTyp)?.bezeichnung.toString();
	}

</script>
