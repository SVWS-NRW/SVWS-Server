<template>
	<svws-ui-content-card :title="`Übersicht über die Blockung ${allow_regeln ? '/ Festlegung von Regeln':''}`">
		<div class="flex flex-row">
			<div class="w-full flex-none sm:-mx-6 lg:-mx-8">
				<div class="py-2 align-middle sm:px-6 lg:px-8">
					<div class="rounded-lg shadow">
						<table class="w-full  border-collapse text-sm table-auto">
							<!-- Wenn sticky angewendet wird, verschwinden die  border border-[#7f7f7f]/20 s...  -->
							<thead class="sticky top-0 bg-slate-100">
								<tr>
									<td colspan="3">
										Schiene
									</td>
									<td
										v-for="s in schienen"
										:key="s.id"
										class="border border-[#7f7f7f]/20 text-center"
									>
										<div class="flex gap-1">
											<template v-if="s === edit_schienenname">
												<svws-ui-text-input
													v-model="s.bezeichnung"
													focus headless
													style="width: 6rem"
													@blur="edit_schienenname=undefined"
													@keyup.enter="edit_schienenname=undefined"
													@input="patch_schiene(s)"
												/>
											</template>
											<template v-else>
												<span class="px-3 underline decoration-dashed underline-offset-2 cursor-text" @click="edit_schienenname = s">{{s.nummer}}</span>
											</template>
											<svws-ui-icon v-if="allow_del_schiene(s)" class="text-red-500 cursor-pointer" @click="del_schiene(s)"><i-ri-delete-bin-2-line/></svws-ui-icon>
										</div>
									</td>
									<template v-if="allow_regeln">
										<td class="bg-[#329cd5] rounded-l-none rounded-lg border-none cursor-pointer" rowspan="4" @click="add_schiene">
											<div class="px-2" >+</div></td><td rowspan="4" class="bg-white">
										</td>
									</template>
								</tr>
								<tr>
									<td class="border border-[#7f7f7f]/20" colspan="3">
										Schülerzahl
									</td>
									<!-- Schülerzahlen -->
									<td
										v-for="s in schienen"
										:key="s.id"
										class="border border-[#7f7f7f]/20 text-center"
									>
										{{ getAnzahlSchuelerSchiene(s.id) }}
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
									<td class="border border-[#7f7f7f]/20 text-center cursor-pointer" @click="sort_by = sort_by === 'kursart'? 'fach_id':'kursart'">
										<div class="flex gap-1">Kurs<svws-ui-icon><i-ri-arrow-up-down-line /></svws-ui-icon></div></td>
									<td class="border border-[#7f7f7f]/20 text-center">Koop</td>
									<td class="border border-[#7f7f7f]/20 text-center">Diff</td>
									<!--Schienen-->
									<s-drag-schiene
										v-if="allow_regeln"
										v-for="s in schienen"
										:key="s.id"
										:schiene="s"
									/>
									<td v-else :colspan="schienen.size()" class="text-center">Regeln können nicht in Ergebnissen erstellt werden</td>
								</tr>
							</thead>
							<tbody>
								<s-kurs-blockung
									v-for="kurs in sorted_kurse"
									:key="kurs.id"
									:kurs="kurs"
								></s-kurs-blockung>
								<template v-if="allow_regeln">
									<tr>
										<td class="border border-[#7f7f7f]/20 text-center bg-white" :colspan="schienen.size()+4">Fachwahlen ohne Kurse</td>
										<td class="bg-white"></td>
									</tr>
									<s-fach-kurs
										v-for="fach in faecher"
										:key="fach.id"
										:fach="fach"
										:halbjahr="app.blockungsauswahl.ausgewaehlt?.gostHalbjahr || 0"
									/>
								</template>
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
		GostBlockungsergebnisManager,
		GostStatistikFachwahl,
		List,
		Vector
	} from "@svws-nrw/svws-core-ts";
	import { App } from "~/apps/BaseApp";
	import { computed, ComputedRef, ref, Ref, WritableComputedRef } from "vue";

	import { injectMainApp, Main, mainApp } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.gost
	
	const edit_schienenname: Ref<GostBlockungSchiene|undefined> = ref()

	const sort_by: WritableComputedRef<UserConfigKeys['gost.kursansicht.sortierung']> =
		computed({
			get(): UserConfigKeys['gost.kursansicht.sortierung'] {
				return main.config.user_config.get('gost.kursansicht.sortierung')
					|| 'kursart'
			},
			set(value: UserConfigKeys['gost.kursansicht.sortierung']) {
				if (value === undefined)
					value = 'kursart'
				App.api.setClientConfigUserKey(value, App.schema, 'SVWS-Client', 'gost.kursansicht.sortierung')
				mainApp.config.user_config.set('gost.kursansicht.sortierung', value)
			}
		});
	
	const faecher: ComputedRef<List<GostStatistikFachwahl>> =
		computed(() => app.dataFachwahlen.daten || new Vector<GostStatistikFachwahl>());

	const sorted_kurse: ComputedRef<List<GostBlockungKurs>> =
		computed(() => {
			if (app.dataKursblockung.datenmanager === undefined)
				return new Vector<GostBlockungKurs>();
			if (sort_by.value === 'kursart')
				return app.dataKursblockung.datenmanager.getKursmengeSortiertNachKursartFachNummer()
			else return app.dataKursblockung.datenmanager.getKursmengeSortiertNachFachKursartNummer()
		})

	const schienen: ComputedRef<List<GostBlockungSchiene>> =
		computed(() => app.dataKursblockung.datenmanager?.getMengeOfSchienen() || new Vector<GostBlockungSchiene>());

	const manager: ComputedRef<GostBlockungsergebnisManager | undefined> =
		computed(() => app.dataKursblockung.ergebnismanager);

	const allow_regeln: ComputedRef<boolean> =
		computed(()=> app.blockungsergebnisauswahl.liste.length === 1)

	function getAnzahlSchuelerSchiene(idSchiene: number): number {
		return manager.value?.getOfSchieneAnzahlSchueler(idSchiene) || 0;
	};

	function allow_del_schiene(schiene: GostBlockungSchiene): boolean {
		return app.dataKursblockung.datenmanager?.removeSchieneAllowed(schiene.id)
						&& manager.value?.getOfSchieneRemoveAllowed(schiene.id)
			? true
			:false
	}

	function getAnzahlKollisionenSchiene(idSchiene: number): number {
		return manager.value?.getOfSchieneAnzahlSchuelerMitKollisionen(idSchiene) || 0;
	};

	async function patch_schiene(schiene: GostBlockungSchiene) {
		await app.dataKursblockung.patch_schiene(schiene);
	}

	async function add_schiene() {
		await app.dataKursblockung.add_blockung_schiene()
	}

	async function del_schiene(s: GostBlockungSchiene) {
		await app.dataKursblockung.del_blockung_schiene(s)
	}
</script>
