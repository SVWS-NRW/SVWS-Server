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
					<a href="https://www.svws.nrw.de/faq/impressum"> <svws-ui-button type="secondary"> Impressum </svws-ui-button> </a>
					<a href="#"> <svws-ui-button type="secondary"> Datenschutz </svws-ui-button> </a>
				</div>
				<div><span>{{ version }}</span> <span v-if="version.includes('SNAPSHOT')">{{ githash.substring(0, 8) }}</span></div>
			</div>
		</div>
	</div>

	<svws-ui-tab-bar :tab-manager="() => tabManager">
		<Teleport defer to=".svws-sub-nav-target">
			<svws-ui-sub-nav>
				<svws-ui-button type="transparent" @click="export_laufbahnplanung"><span class="icon-sm i-ri-upload-2-line" />Exportieren</svws-ui-button>
				<svws-ui-button type="transparent" @click="showModalImport = true"><span class="icon-sm i-ri-download-2-line" /> Importieren…</svws-ui-button>
				<s-laufbahnplanung-import-modal :show="showModalImport" :import-laufbahnplanung="import_laufbahnplanung" @update:show="val => showModalImport = val" />
				<svws-ui-button :type="zwischenspeicher === undefined ? 'transparent' : 'error'" @click="saveLaufbahnplanung">Planung merken</svws-ui-button>
				<svws-ui-button type="danger" @click="restoreLaufbahnplanung" v-if="zwischenspeicher !== undefined">Planung wiederherstellen</svws-ui-button>
				<svws-ui-button :type="manager.modus === 'normal' ? 'transparent' : 'danger'" @click="manager.switchModus()">
					<span class="icon-sm i-ri-loop-right-line" /> Modus: <span>{{ manager.modus }}</span>
				</svws-ui-button>
				<s-modal-laufbahnplanung-kurswahlen-loeschen schueler-ansicht :gost-jahrgangsdaten :reset-fachwahlen />
				<svws-ui-button type="transparent" @click="manager.switchFaecherAnzeigen()"> {{ "Fächer anzeigen: " + manager.getTextFaecherAnzeigen() }} </svws-ui-button>
			</svws-ui-sub-nav>
		</Teleport>

		<div v-if="schueler.abiturjahrgang !== null" class="page page-flex-row">
			<div class="grow overflow-y-auto overflow-x-hidden min-w-fit">
				<s-laufbahnplanung-card-planung :manager :abiturdaten-manager :gost-jahrgangsdaten :set-wahl :goto-kursblockung="async () => {}" belegung-hat-immer-noten />
			</div>
			<div class="w-2/5 3xl:w-1/2 min-w-[36rem] overflow-y-auto overflow-x-hidden">
				<div class="flex flex-col gap-16">
					<s-laufbahnplanung-card-status :abiturdaten-manager :fehlerliste="() => gostBelegpruefungErgebnis().fehlercodes" :gost-belegpruefungs-art :set-gost-belegpruefungs-art />
				</div>
			</div>
		</div>
		<div v-else class="page page-flex-row">Die Laufbahnplanung hat kein gültiges Abiturjahr, bitte prüfen Sie die importierte Datei.</div>
	</svws-ui-tab-bar>
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

	const manager = computed<LaufbahnplanungUiManager>(() => new LaufbahnplanungUiManager(props.abiturdatenManager, props.config, () => props.gostJahrgangsdaten));

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

	async function import_laufbahnplanung(formData: FormData) {
		await props.importLaufbahnplanung(formData);
	}

</script>
