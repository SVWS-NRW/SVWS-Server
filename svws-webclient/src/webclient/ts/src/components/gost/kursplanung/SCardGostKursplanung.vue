<template>
	<svws-ui-content-card title="Festlegung der Anzahl von Kursen" >
		<div class="flex flex-row">
			<div class="w-full flex-none sm:-mx-6 lg:-mx-8">
				<div class="py-2 align-middle sm:px-6 lg:px-8">
					<div class="overflow-hidden rounded-lg shadow">
						<table class="w-full  border-collapse text-sm">
							<thead class="sticky top-0 bg-slate-100">
								<tr>
									<td class=" border border-[#7f7f7f]/20 ">Kürzel</td>
									<td class=" border border-[#7f7f7f]/20  text-center">Fach</td>
									<td class=" border border-[#7f7f7f]/20 ">LK</td>
									<td class=" border border-[#7f7f7f]/20 ">Kursanzahl</td>
									<td class=" border border-[#7f7f7f]/20 ">GK</td>
									<td class=" border border-[#7f7f7f]/20 ">Kursanzahl</td>
									<td class=" border border-[#7f7f7f]/20 ">ZK</td>
									<td class=" border border-[#7f7f7f]/20 ">Kursanzahl</td>
								</tr>
							</thead>
							<s-fach-kurs
								v-for="(fach, i) in faecher"
								:key="i"
								:fach="fach"
								:halbjahr="
									app.blockungsauswahl.ausgewaehlt
										?.gostHalbjahr || 0
								"
							></s-fach-kurs>
						</table>
					</div>
				</div>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
		import { GostStatistikFachwahl } from "@svws-nrw/svws-core-ts";
		import { computed, ComputedRef } from "vue";
		import { injectMainApp, Main } from "~/apps/Main";

		const main: Main = injectMainApp();
		const app = main.apps.gost

		const faecher: ComputedRef<Array<GostStatistikFachwahl>> = computed(() => {
			return (
				app.dataFachwahlen.daten?.toArray(new Array<GostStatistikFachwahl>) ||
				new Array<GostStatistikFachwahl>()
			);
		});
</script>
