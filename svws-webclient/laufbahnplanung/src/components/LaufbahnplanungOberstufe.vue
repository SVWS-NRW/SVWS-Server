<template>
	<div class="page--header">
		<div class="flex flex-row w-full">
			<div class="grow">
				<h2 class="text-headline"> {{ schueler.nachname }}, {{ schueler.vorname }} </h2>
				Jahrgang {{ gostJahrgangsdaten.jahrgang }} ({{ gostJahrgangsdaten.bezeichnung }})
			</div>
			<div class="flex flex-col gap-2">
				<div class="flex gap-3">
					<svws-ui-modal-hilfe> <hilfe-laufbahnplanung /> </svws-ui-modal-hilfe>
					<svws-ui-button type="danger" @click="handleClick"><span class="icon-sm i-ri-logout-circle-line" /> Abmelden </svws-ui-button>
				</div>
				<div><span>{{ version }}</span> <span v-if="version.includes('SNAPSHOT')">{{ githash.substring(0, 8) }}</span></div>
			</div>
		</div>
	</div>

	<svws-ui-tab-bar :tab-manager="() => tabManager">
		<Teleport defer to=".svws-sub-nav-target">
			<svws-ui-sub-nav>
				<svws-ui-button type="transparent" @click="export_laufbahnplanung"><span class="icon-sm i-ri-upload-2-line" /> Speichern</svws-ui-button>
				<svws-ui-button type="transparent" @click="showModalImport = true"><span class="icon-sm i-ri-download-2-line" /> Öffnen</svws-ui-button>
				<s-laufbahnplanung-import-modal :show="showModalImport" :import-laufbahnplanung="import_laufbahnplanung" @update:show="val => showModalImport = val" />
				<svws-ui-button :type="zwischenspeicher === undefined ? 'transparent' : 'error'" @click="saveLaufbahnplanung">Planung merken</svws-ui-button>
				<svws-ui-button type="danger" @click="restoreLaufbahnplanung" v-if="zwischenspeicher !== undefined">Planung wiederherstellen</svws-ui-button>
				<svws-ui-button :type="manager.modus === 'normal' ? 'transparent' : 'danger'" @click="manager.switchModus()">
					<span class="icon-sm i-ri-loop-right-line" /> Modus: <span>{{ manager.modus }}</span>
				</svws-ui-button>
				<s-modal-laufbahnplanung-kurswahlen-loeschen schueler-ansicht keine-vorlage :gost-jahrgangsdaten :reset-fachwahlen />
				<svws-ui-button type="transparent" @click="manager.switchFaecherAnzeigen()"> {{ "Fächer anzeigen: " + manager.getTextFaecherAnzeigen() }} </svws-ui-button>
			</svws-ui-sub-nav>
		</Teleport>

		<div v-if="schueler.abiturjahrgang !== null" class="page page-flex-row">
			<div class="grow overflow-y-auto overflow-x-hidden min-w-fit">
				<s-laufbahnplanung-card-planung :manager :abiturdaten-manager :gost-jahrgangsdaten :goto-kursblockung="async () => {}" />
			</div>
			<div class="w-2/5 3xl:w-1/2 min-w-[36rem] overflow-y-auto overflow-x-hidden">
				<div class="flex flex-col gap-16">
					<s-laufbahnplanung-card-status :abiturdaten-manager :fehlerliste="() => gostBelegpruefungErgebnis().fehlercodes" :gost-belegpruefungs-art :set-gost-belegpruefungs-art />
				</div>
			</div>
		</div>
		<div v-else class="page page-flex-row">Die Laufbahnplanung hat kein gültiges Abiturjahr, bitte prüfen Sie die importierte Datei.</div>
	</svws-ui-tab-bar>
	<!-- Modal zum Speichern der Laufbandatei -->
	<svws-ui-modal :show>
		<template #modalTitle>Laufbahnplanung speichern</template>
		<template #modalContent>
			Möchten Sie die Laufbahnplanungsdatei vor der Abmeldung speichern?
			<div class="mt-7 flex flex-row gap-4 justify end">
				<svws-ui-button type="secondary" @click="exitLaufbahnplanung">Nein</svws-ui-button>
				<svws-ui-button @click="exit_laufbahnplanung">Speichern</svws-ui-button>
			</div>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { LaufbahnplanungOberstufeProps } from "./LaufbahnplanungOberstufeProps";
	import { version } from '../../version';
	import { githash } from '../../githash';
	import { TabManager } from "@ui/ui/nav/TabManager";
	import type { TabData } from "@ui/ui/nav/TabData";
	import { LaufbahnplanungUiManager } from "@ui/components/gost/laufbahnplanung/LaufbahnplanungUiManager";

	const props = defineProps<LaufbahnplanungOberstufeProps>();

	const show = ref<boolean>(false);

	const manager = computed<LaufbahnplanungUiManager>(() =>
		new LaufbahnplanungUiManager(props.serverMode, props.abiturdatenManager, props.config, () => props.gostJahrgangsdaten, props.setWahl,
			{ faecherZeigen: "app.schueler.laufbahnplanung.faecher.anzeigen", modus: "app.schueler.laufbahnplanung.modus" }));

	const tabManager = new TabManager([], <TabData>{}, async (value: TabData) => {});

	const showModalImport = ref<boolean>(false);

	async function export_laufbahnplanung() {
		const { data, name } = await props.exportLaufbahnplanung();
		const link = document.createElement("a");
		link.href = URL.createObjectURL(data);
		link.download = name;
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}

	async function exit_laufbahnplanung() {
		await export_laufbahnplanung();
		await props.exitLaufbahnplanung();
	}

	async function import_laufbahnplanung(formData: FormData) {
		await props.importLaufbahnplanung(formData);
	}

	async function handleClick() {
		return props.dirty() ? (show.value = true) : await props.exitLaufbahnplanung();
	}

</script>
