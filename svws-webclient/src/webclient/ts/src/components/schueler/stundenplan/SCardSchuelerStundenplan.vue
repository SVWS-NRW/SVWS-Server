<template>
	<svws-ui-content-card title="Stundenplan">
		<svws-ui-multi-select
			v-model="selected_stundenplan"
			:items="liste"
			:item-text="(i: StundenplanListeEintrag)=>i.bezeichnung"
			/>
	</svws-ui-content-card>

	<div class="flex flex-row gap-4">
		<div class="w-full flex-none sm:-mx-6 lg:-mx-8">
			<div class="w-full inline-block py-2 align-middle sm:px-6 lg:px-8">
					<div v-for="wochentyp in stundenplan?.getWochentypen()" class="w-full rounded-lg shadow">
						<p class="text-lg"></p>
						<table class="w-full table-auto border-collapse mb-5">
							<thead><tr><th class="text-center border-2 border-collapse">Woche {{wochentyp}}</th><th class="text-center border-2 border-collapse">Montag</th><th class="text-center border-2 border-collapse">Dienstag</th><th class="text-center border-2 border-collapse">Mittwoch</th><th class="text-center border-2 border-collapse">Donnerstag</th><th class="text-center border-2 border-collapse">Freitag</th></tr></thead>
							<tbody>
                            <tr v-for="stunde in stundenplan?.getMaxStunde()">
								<th class="text-center border-2 border-collapse">{{stunde}}</th>
								<td class="text-center border-2 border-collapse" v-for="wochentag in stundenplan?.getMaxWochentag()">
									<table class="w-full">
        								<tr>
											<s-unterricht v-for="unterricht in stundenplan?.getUnterrichtByWocheZeitrasterId(wochentyp, stundenplan?.getZeitrasterByWochentagStunde(wochentag, stunde).id, true)" :key="unterricht.idUnterricht" :unterricht="unterricht">
            								</s-unterricht>
										</tr>
									</table>
								</td>
							</tr>
						</tbody>
                        </table>
                    </div>
				</div>
			</div>
        </div>

</template>

<script setup lang="ts">
	import { computed, ComputedRef, Ref, ref, watch, WritableComputedRef } from "vue";

	import { SchuelerStundenplan, SchuelerStundenplanManager, SchuelerStundenplanUnterricht, StundenplanListeEintrag, ZulaessigesFach } from "@svws-nrw/svws-core-ts";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.schueler;
	
	const liste: WritableComputedRef<StundenplanListeEintrag[]> = computed(() => {
			return app.listStundenplaene.liste
	});

	const stundenplan: ComputedRef<SchuelerStundenplanManager | undefined> = computed(()=>{
		let ssm = typeof app.dataStundenplan.daten === "undefined" ? undefined : new SchuelerStundenplanManager(app.dataStundenplan.daten)
		return ssm
	})

	const selected_stundenplan: Ref<StundenplanListeEintrag|undefined> = ref(liste.value[0])
	watch(selected_stundenplan, async (neu) => {
		app.listStundenplaene.ausgewaehlt = neu
	}, {immediate: true})
</script>
