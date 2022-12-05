<template>
	<svws-ui-content-card title="Benutzer">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-text-input v-model="anzeigename" type="text" placeholder="Login-Name" />
				<svws-ui-text-input v-model="name" type="text" placeholder="Name" />
			</div>
		</div>
	</svws-ui-content-card>
	<s-card-benutzer-gruppen-liste > </s-card-benutzer-gruppen-liste>
</template>

<script setup lang="ts">
	import { BenutzergruppeListeEintrag, BenutzerManager } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, Ref, ref, WritableComputedRef } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";
	import { useAuswahlViaRoute } from "~/router/auswahlViaRoute";

	const main: Main = injectMainApp();
	const app = main.apps.benutzer;
	const manager: ComputedRef<BenutzerManager | undefined> = computed(() => {
		return app.dataBenutzer.manager;
	})

	const anzeigename: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return manager.value?.getAnzeigename().valueOf();
		},
		async set(val: string | undefined) {
			if ((val === undefined) || (val === "") || (val === manager.value?.getAnzeigename().valueOf()))
				return;
			app.dataBenutzer.setAnzeigename(val);
		}
	});

	const name: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return manager.value?.getAnmeldename().valueOf();
		},
		async set(val: string | undefined) {
			if ((val === undefined) || (val === "") || (val === manager.value?.getAnmeldename().valueOf()))
				return;
			app.dataBenutzer.setAnmeldename(val);
		}
	});


	// Auswahlliste der Benutzergruppen

	const benutzergruppen_cols = [
		{ key: "id", label: "ID", sortable: true },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true }
	];

	const benutzergruppen_suche: Ref<string> = ref("");

	const benutzergruppen_rows: ComputedRef<BenutzergruppeListeEintrag[] | undefined> = computed(() => {
		return main.apps.benutzergruppe.auswahl.liste;
	});

	const benutzergruppen_rows_gefiltert: ComputedRef<BenutzergruppeListeEintrag[] | undefined> = computed(() => {
		if (benutzergruppen_rows.value === undefined) 
			return undefined;
		const rowsValue: BenutzergruppeListeEintrag[] = benutzergruppen_rows.value;
		return (benutzergruppen_suche.value) 
			? rowsValue.filter((e: BenutzergruppeListeEintrag) => e.bezeichnung.toLocaleLowerCase().includes(benutzergruppen_suche.value.toLocaleLowerCase()))
			: rowsValue;
	});

	const benutzergruppen_ausgewaehlt = useAuswahlViaRoute('benutzergruppe');
	const benutzergruppen_selection = ref([]);

</script>
