<template>
	<svws-ui-content-card
		:title="
			'Kursplanung für den Jahrgang: ' +
				bezeichnung +
				' – ' +
				kursauswahl?.name || 'keine Auswahl'
		"
	>
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
					<div class="flex justify-between">
						<div class="flex gap-4">
							<svws-ui-button
								class="my-4"
								@click="toggle_schienenmodal"
								>Schienen...</svws-ui-button
							>
							<svws-ui-button
								class="my-4"
								@click="toggle_kursmodal"
								>Kurse ...</svws-ui-button
							>
						</div>
					</div>
				</div>
			</div>
		</div>
	</svws-ui-content-card>
	<svws-ui-modal ref="schienenmodal" size="small">
		<template #modalTitle>Verfügbare Schienen</template>
		<template #modalDescription>
			<s-modal-gost-schienen />
		</template>

		<template #modalActions>
			<svws-ui-button type="secondary" @click="toggle_schienenmodal">
				Schließen
			</svws-ui-button>
		</template>
	</svws-ui-modal>
	<svws-ui-modal ref="kursmodal" size="small">
		<template #modalTitle>Kursübersicht</template>
		<template #modalDescription>
			<s-modal-gost-kursliste />
		</template>

		<template #modalActions>
			<svws-ui-button type="secondary" @click="toggle_kursmodal">
				Schließen
			</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
		import {
	GostBlockungListeneintrag,
	GostHalbjahr,
	GostStatistikFachwahl
	} from "@svws-nrw/svws-core-ts";
		   // import { SvwsUiModal } from "@svws-nrw/svws-ui";
		import { computed, ComputedRef, Ref, ref } from "vue";
		import { GOST_CREATE_BLOCKUNG_SYMBOL } from "~/apps/core/LoadingSymbols";

		import { injectMainApp, Main } from "~/apps/Main";

				const main: Main = injectMainApp();
			const app = main.apps.gost

		   // FIXME:
		   // const schienenmodal: Ref<SvwsUiModal> = ref(null);
		   // const kursmodal: Ref<SvwsUiModal> = ref(null);

		   const schienenmodal: Ref<any> = ref(null);
		   const kursmodal: Ref<any> = ref(null);

		   const kursauswahl: ComputedRef<GostBlockungListeneintrag | undefined> = computed(() => {
			return app.blockungsauswahl
				.ausgewaehlt;
		});

		   const bezeichnung: ComputedRef<string | undefined> = computed(() => {
			return app.auswahl.ausgewaehlt?.bezeichnung?.toString();
		});

		   const faecher: ComputedRef<Array<GostStatistikFachwahl>> = computed(() => {
			return (
				app.dataFachwahlen.daten?.toArray(new Array<GostStatistikFachwahl>) ||
				new Array<GostStatistikFachwahl>()
			);
		});

		   const jahrgangsstufen: ComputedRef<GostHalbjahr[]> = computed(() => {
			return GostHalbjahr.values() || [];
		});

		   function toggle_schienenmodal() {
			schienenmodal.value.isOpen ? schienenmodal.value.closeModal() : schienenmodal.value.openModal()
		};

		   function toggle_kursmodal() {
			kursmodal.value.isOpen ? kursmodal.value.closeModal() : kursmodal.value.openModal()
		};
</script>
