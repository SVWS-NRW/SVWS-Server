<template>
	<svws-ui-content-card title="Blockungen">
		<div class="flex flex-row">
			<div class="w-full flex-none sm:-mx-6 lg:-mx-8">
				<div class="py-2 align-middle sm:px-6 lg:px-8">
					<div class="rounded-lg shadow">
						<table class="w-full  border-collapse text-sm">
							<!-- Wenn sticky angewendet wird, verschwinden die  border border-[#7f7f7f]/20 s...  -->
							<thead class="sticky top-0  bg-slate-100">
								<tr>
									<td class="border border-[#7f7f7f]/20 " colspan="3">
										Schülerzahl
									</td>
									<!-- Schülerzahlen -->
									<td
										v-for="s in schienen"
										:key="s.id"
										class="border border-[#7f7f7f]/20  text-center"
									>
										{{ getAnzahlSchuelerSchiene(s.id) }}
									</td>
								</tr>
								<tr>
									<td class="border border-[#7f7f7f]/20" colspan="3">Umwahlen</td>
									<!-- Umwahlen -->
									<td
										v-for="s in schienen"
										:key="s.id"
										class="border border-[#7f7f7f]/20 text-center"
									>
										Y
									</td>
								</tr>
								<tr>
									<td class="border border-[#7f7f7f]/20" colspan="3">
										Kollisionen
									</td>
									<!-- Kollisionen -->
									<td
										v-for="s in schienen"
										:key="s.id"
										class="border border-[#7f7f7f]/20 text-center"
									>
										{{ getAnzahlKollisionenSchiene(s.id) }}
									</td>
								</tr>
								<tr>
									<td class="border border-[#7f7f7f]/20 text-center">Kurs</td>
									<td class="border border-[#7f7f7f]/20 text-center">Diff</td>
									<!--Schienen-->
									<td
										v-for="s in schienen"
										:key="s.id"
										class="border border-[#7f7f7f]/20 text-center"
									>
										{{ s.bezeichnung }}
										<i-ri-lock-line
											v-if="false"
											class="inline-block text-red-700"
										/>
										<i-ri-lock-unlock-line
											v-else
											class="inline-block"
										/>
									</td>
								</tr>
								<tr></tr>
							</thead>
							<tbody>
								<s-kurs-blockung
									v-for="(
										[kurs, belegung], index
									) in blockungsergebnisse"
									:key="index"
									:kurs="kurs"
									:belegung="belegung"
								></s-kurs-blockung>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import {
		GostBlockungKurs,
		GostBlockungSchiene,
		GostBlockungsergebnisKurs,
		GostBlockungsergebnisManager,
		GostBlockungsergebnisSchiene,
		Vector
	} from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";


	const main: Main = injectMainApp();
	const app = main.apps.gost

	const bezeichnung: ComputedRef<string | undefined> = computed(() => {
		return app.auswahl.ausgewaehlt?.bezeichnung?.toString();
	});

	const kurse: ComputedRef<Vector<GostBlockungKurs>> = computed(() => {
		return (
			app.dataKursblockung.daten?.kurse ||
			new Vector<GostBlockungKurs>()
		);
	});

	const schienen: ComputedRef<GostBlockungSchiene[]> = computed(() => {
		const result = app.dataKursblockung.daten?.schienen || new Vector<GostBlockungSchiene>();
		return result.toArray(new Array<GostBlockungSchiene>);
	});

	const manager: ComputedRef<GostBlockungsergebnisManager | undefined> = computed(() => {
		return app.dataKursblockungsergebnis.manager
	});

	const blockungsergebnisse: ComputedRef<Map<
		GostBlockungKurs,
		GostBlockungsergebnisKurs[]
	>> = computed(() => {
		const map = new Map();
		if (!app.dataKursblockung.daten) return map;
		const schienen = Array.from(app.dataKursblockung.daten.schienen)
		for (const k of kurse.value) {
			let zuordnung: GostBlockungsergebnisKurs | undefined = undefined
			try {
				zuordnung = manager.value?.getKursSchuelerZuordnung(
					k.id
				);
					} catch (e) {zuordnung = new GostBlockungsergebnisKurs()}
			map.set(
				k,
				schienen.map(s =>
					s.id === zuordnung?.schienenID
						? zuordnung
						: undefined
				)
			);
		}
		return map;
	});

	function getAnzahlSchuelerSchiene(idSchiene: number): number {
		return manager.value?.getAnzahlSchuelerSchiene(idSchiene) || 0;
	};

	function getAnzahlKollisionenSchiene(idSchiene: number): number {
		return manager.value?.getAnzahlKollisionenSchiene(idSchiene) || 0;
	};
</script>
