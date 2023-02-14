<template>
	<svws-ui-content-card>
		<div class="overflow-hidden rounded-lg shadow">
			<table class="border-collapse text-sm">
				<thead class="bg-slate-100">
					<tr>
						<td class="border border-[#7f7f7f]/20 text-center" colspan="4" />
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
				<tr v-for="fach in faecher" :key="fach.id">
					<s-row-gost-faecher :fach="fach" :abiturjahr="abiturjahr" :map-leitfaecher="mapLeitfaecher" :patch-fach="patchFach" />
				</tr>
			</table>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed, ComputedRef } from "vue";

	import { Fachgruppe, GostFach, List, Vector, ZulaessigesFach } from "@svws-nrw/svws-core-ts";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";

	const props = defineProps<{
		patchFach: (data: Partial<GostFach>, fach_id: number) => Promise<boolean>;
		abiturjahr: number;
		dataFaecher: DataGostFaecher;
	}>();

	const mapLeitfaecher: ComputedRef<Map<number, GostFach>> = computed(() => {
		const result = new Map<number, GostFach>();
		if (props.dataFaecher.daten === undefined)
			return result;
		for (const fach of props.dataFaecher.daten) {
			const fg = ZulaessigesFach.getByKuerzelASD(fach.kuerzel).getFachgruppe();
			if ((fg !== Fachgruppe.FG_VX) && (fg !== Fachgruppe.FG_PX))
				result.set(fach.id, fach);
		}
		return result;
	});

	const faecher: ComputedRef<List<GostFach>> = computed(() => props.dataFaecher.daten || new Vector<GostFach>());

</script>
