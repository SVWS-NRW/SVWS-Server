<template>
	<svws-ui-content-card>
		<div class="overflow-hidden rounded-lg shadow">
			<table class="border-collapse text-sm">
				<thead class="bg-slate-100">
					<tr>
						<td class="border border-[#7f7f7f]/20 text-center" colspan="4"></td>
						<td class="border border-[#7f7f7f]/20 text-center" colspan="2">
							Leitfächer
						</td>
						<td class="border border-[#7f7f7f]/20 text-center" colspan="6">
							Wählbar
						</td>
						<td class="border border-[#7f7f7f]/20 text-center" colspan="2">
							im Abitur
						</td>
					</tr>
					<tr>
						<td class="w-20 border border-[#7f7f7f]/20 px-2 text-center">Kürzel</td>
						<td class="w-60 border border-[#7f7f7f]/20 text-center">Fach</td>
						<td class="border border-[#7f7f7f]/20 text-center">Neu</td>
						<td class="border border-[#7f7f7f]/20 text-center">WStd.</td>
						<td class="border border-[#7f7f7f]/20 text-center">1.</td>
						<td class="border border-[#7f7f7f]/20 text-center">2.</td>
						<td class="border border-[#7f7f7f]/20 text-center">EF.1</td>
						<td class="border border-[#7f7f7f]/20 text-center">EF.2</td>
						<td class="border border-[#7f7f7f]/20 text-center">Q1.1</td>
						<td class="border border-[#7f7f7f]/20 text-center">Q1.2</td>
						<td class="border border-[#7f7f7f]/20 text-center">Q2.1</td>
						<td class="border border-[#7f7f7f]/20 text-center">Q2.2</td>
						<td class="border border-[#7f7f7f]/20 text-center">GK</td>
						<td class="border border-[#7f7f7f]/20 text-center">LK</td>
					</tr>
				</thead>
				<tr v-for="row in rows" :key="row.id">
					<s-row-gost-faecher :fach="row" :data-faecher="dataFaecher"></s-row-gost-faecher>
				</tr>
			</table>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed, ComputedRef } from "vue";

	import { GostFach, List, Vector } from "@svws-nrw/svws-core-ts";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";
	import { routeGost } from "~/router/apps/RouteGost";

	const { dataFaecher } = defineProps<{ 
		dataFaecher: DataGostFaecher;
	}>();

	const rows: ComputedRef<List<GostFach>> = computed(() => dataFaecher.daten || new Vector<GostFach>());

</script>
