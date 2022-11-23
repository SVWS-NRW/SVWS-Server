<template>
	<svws-ui-secondary-menu>
		<template #headline
			><div>
				<i-ri-arrow-left-line
					class="inline-block cursor-pointer"
					@click="router.push({ name: name })"
				/>
				Religionenauswahl
			</div>
		</template>
		<template #header> </template>
		<template #content>
			<div class="container">
				<svws-ui-table
					v-model="selected"
					:columns="cols"
					:data="rows"
					:footer="true"
				>
					<template #footer>
						<button
							@click="modalAdd.openModal()"
							class="flex h-10 w-10 items-center justify-center"
						>
							<svws-ui-icon><i-ri-add-line /></svws-ui-icon>
						</button>
					</template>
				</svws-ui-table>
			</div>
		</template>
	</svws-ui-secondary-menu>

	<svws-ui-modal ref="modalAdd" size="medium">
		<template #modalTitle>Religion Hinzufügen</template>

		<template #modalContent>
			<div class="input-wrapper">
				<svws-ui-multi-select
					v-model="reli_neu.kuerzel"
					title="Statistikkürzel"
					:items="inputKatalogReligionenStatistik"
					:item-text="(i: Religion) => i.daten.kuerzel"
					required
				/>
				<svws-ui-text-input
					v-model="reli_neu.kuerzel"
					type="text"
					placeholder="Kürzel"
				/>
				<svws-ui-text-input
					v-model="reli_neu.text"
					type="text"
					placeholder="Bezeichnung"
				/>
				<svws-ui-text-input
					v-model="reli_neu.textZeugnis"
					type="text"
					placeholder="Zeugnisbezeichnung"
				/>
			</div>
		</template>

		<template #modalActions>
			<svws-ui-button
				v-if="reli_neu.kuerzel || reli_neu.textZeugnis || reli_neu.text"
				type="secondary"
				@click="deleteEntries()"
			>
				Felder Leeren
			</svws-ui-button>
			<svws-ui-button type="secondary" @click="modalAdd.closeModal">
				Abbrechen
			</svws-ui-button>
			<svws-ui-button type="primary" @click="saveEntries()">
				Speichern
			</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import { Religion, ReligionEintrag } from "@svws-nrw/svws-core-ts";
	import {
		computed,
		ComputedRef,
		reactive,
		ref,
		Ref,
		WritableComputedRef
	} from "vue";
	import { App } from "~/apps/BaseApp";
	import { router } from "~/router";
	import { injectMainApp, Main } from "~/apps/Main";

	const name = ref("kataloge");
	const none_selected: Ref<ReligionEintrag> = ref({
		id: -1,
		text: "",
		textZeugnis: "",
		kuerzel: "",
		istSichtbar: false,
		istAenderbar: false
	} as unknown as ReligionEintrag);

	const cols = ref([
	{
			key: "kuerzel",
			label: "Kuerzel",
			sortable: true,
			defaultSort: "asc"
		},
		{
			key: "text",
			label: "Bezeichnung",
			sortable: true
		}
	]);

	const main: Main = injectMainApp();
	const app = main.apps.religionen;
	const modalAdd = ref();

	const rows: ComputedRef<ReligionEintrag[] | undefined> = computed(() => {
		return app.auswahl.liste;
	});

	const selected: WritableComputedRef<ReligionEintrag | undefined> = computed(
		{
			get(): ReligionEintrag {
				if (!app.auswahl.ausgewaehlt) return none_selected.value;
				return app.auswahl.ausgewaehlt;
			},
			set(value: ReligionEintrag) {
				if (app) {
					app.auswahl.ausgewaehlt = value;
				}
			}
		}
	);

	/**
	 * Modalfenster-Neu-Anpsrechpartner
	 */
	const reli_neu: ReligionEintrag = reactive(new ReligionEintrag());

	const inputKatalogReligionenStatistik: ComputedRef<Religion[] | undefined> =
		computed(() => {
			return Religion.values();
		});

	async function saveEntries() {
		//TODO  Den Attributswert von reli_neu.id von 0 auf null setzen.
		if (reli_neu.kuerzel) {
			await App.api.createReligion(
				reli_neu,
				App.schema,
				reli_neu.kuerzel?.valueOf()
			);
			modalAdd.value.closeModal();
			app.auswahl.update_list();
		} else {
			alert("Kürzel darf nicht leer sein");
		}
		deleteEntries();
	}
	function deleteEntries() {
		reli_neu.kuerzel = null;
		reli_neu.text = null;
		reli_neu.textZeugnis = null;
	}
</script>
