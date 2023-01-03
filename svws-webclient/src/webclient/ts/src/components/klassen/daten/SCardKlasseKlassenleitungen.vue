<template>
	<svws-ui-content-card title="Klassenleitungen">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-table :columns="cols" :data="liste" :footer="false"/>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, ref } from "vue";
	import { LehrerListeEintrag, PersonalTyp } from "@svws-nrw/svws-core-ts";
	import { DataKlasse } from "~/apps/klassen/DataKlasse";
	import { ListLehrer } from "~/apps/lehrer/ListLehrer";

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

	const liste: ComputedRef<Lehrer[]> =
		computed(() => {
			if (!data.daten?.klassenLeitungen) return [];
			let res: Lehrer[] = [];
			for (const l of data.daten.klassenLeitungen)
				if (l)
					res.push({
						kuerzel: kuerzel(l),
						nachname: nachname(l),
						vorname: vorname(l),
						typ: personTyp(l)
					})
			return res;
		});

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
