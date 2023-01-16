<template>
	<svws-ui-content-card :title="title">
		<div class="inline-block py-2 align-middle sm:px-6 lg:px-8">
			<div class="overflow-hidden rounded-lg shadow">
				<table class="border-collapse text-sm">
					<thead class="bg-slate-100">
						<tr>
							<td class="border border-[#7f7f7f]/20 text-center" colspan="4"> Fach/Kursart => Fach/Kursart </td>
							<td class="border border-[#7f7f7f]/20 text-center" colspan="6"> Gültig in </td>
							<td class="border border-[#7f7f7f]/20 text-center" rowspan="2" />
						</tr>
						<tr>
							<td class="border border-[#7f7f7f]/20 text-center">Fach</td>
							<td class="border border-[#7f7f7f]/20 text-center">Kursart</td>
							<td class="border border-[#7f7f7f]/20 text-center">Fach</td>
							<td class="border border-[#7f7f7f]/20 text-center">Kursart</td>
							<td class="border border-[#7f7f7f]/20 text-center">EF.1</td>
							<td class="border border-[#7f7f7f]/20 text-center">EF.2</td>
							<td class="border border-[#7f7f7f]/20 text-center">Q1.1</td>
							<td class="border border-[#7f7f7f]/20 text-center">Q1.2</td>
							<td class="border border-[#7f7f7f]/20 text-center">Q2.1</td>
							<td class="border border-[#7f7f7f]/20 text-center">Q2.2</td>
						</tr>
					</thead>
					<tr v-for="row in rows" :key="row.id">
						<s-row-gost-fachkombination :kombination="row" :data-faecher="dataFaecher" :data-fachkombinationen="dataFachkombinationen" />
					</tr>
				</table>
				<svws-ui-button class="pl-2 pt-2" @click="add_kurskombi">Hinzufügen</svws-ui-button>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";
	import { List, Vector, GostJahrgangFachkombination, GostLaufbahnplanungFachkombinationTyp } from "@svws-nrw/svws-core-ts";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";
	import { DataGostFachkombinationen } from "~/apps/gost/DataGostFachkombinationen";

	const { typ, dataFaecher, dataFachkombinationen } = defineProps<{
		typ: GostLaufbahnplanungFachkombinationTyp;
		dataFaecher: DataGostFaecher;
		dataFachkombinationen: DataGostFachkombinationen;
	}>();

	const title: ComputedRef<string> = computed(() => {
		switch(typ) {
			case GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH:
				return "Regeln für geforderte Fachkombinationen";
			case GostLaufbahnplanungFachkombinationTyp.VERBOTEN:
				return "Regeln für unzulässige Fachkombinationen";
			default:
				return "Fehler: Typ für die Fachkombination unbekannt";
		}
	});

	const rows: ComputedRef<List<GostJahrgangFachkombination>> = computed(() => {
		const result = new Vector<GostJahrgangFachkombination>();
		if (dataFachkombinationen.daten)
			for (const kombi of dataFachkombinationen.daten)
				if (GostLaufbahnplanungFachkombinationTyp.fromValue(kombi.typ) === typ)
					result.add(kombi);
		return result;
	});

	const add_kurskombi = () => {
		dataFachkombinationen.add(typ);
	}

</script>
