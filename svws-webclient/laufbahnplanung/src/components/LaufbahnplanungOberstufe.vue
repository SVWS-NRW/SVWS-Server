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
				<s-laufbahnplanung-import-modal :show="showModalImport" :import-laufbahnplanung="import_laufbahnplanung" />
				<svws-ui-button :type="zwischenspeicher === undefined ? 'transparent' : 'error'" @click="saveLaufbahnplanung">Planung merken</svws-ui-button>
				<svws-ui-button type="danger" @click="restoreLaufbahnplanung" v-if="zwischenspeicher !== undefined">Planung wiederherstellen</svws-ui-button>
				<svws-ui-button :type="modus === 'normal' ? 'transparent' : 'danger'" @click="switchModus">
					<span class="icon-sm i-ri-loop-right-line" /> Modus: <span>{{ modus }}</span>
				</svws-ui-button>
				<s-modal-laufbahnplanung-kurswahlen-loeschen schueler-ansicht :gost-jahrgangsdaten :reset-fachwahlen />
				<svws-ui-button type="transparent" @click="switchFaecherAnzeigen()"> {{ "Fächer anzeigen: " + textFaecherAnzeigen() }} </svws-ui-button>
			</svws-ui-sub-nav>
		</Teleport>

		<div v-if="schueler.abiturjahrgang !== null" class="page--content page--content--full page--content--laufbahnplanung">
			<div class="flex-grow overflow-y-auto overflow-x-hidden min-w-fit">
				<s-laufbahnplanung-card-planung :abiturdaten-manager :modus :gost-jahrgangsdaten :set-wahl :goto-kursblockung="async () => {}" :faecher-anzeigen belegung-hat-immer-noten />
			</div>
			<div class="w-2/5 3xl:w-1/2 min-w-[36rem] overflow-y-auto overflow-x-hidden">
				<div class="flex flex-col gap-16">
					<s-laufbahnplanung-card-status :abiturdaten-manager :fehlerliste="() => gostBelegpruefungErgebnis().fehlercodes" :gost-belegpruefungs-art :set-gost-belegpruefungs-art />
				</div>
			</div>
		</div>
		<div v-else class="page--content page--content--full">Die Laufbahnplanung hat kein gültiges Abiturjahr, bitte prüfen Sie die importierte Datei.</div>
	</svws-ui-tab-bar>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { LaufbahnplanungOberstufeProps } from "./LaufbahnplanungOberstufeProps";
	import { version } from '../../version';
	import { githash } from '../../githash';
	import { TabManager } from "@ui/ui/nav/TabManager";
	import type { TabData } from "@ui/ui/nav/TabData";

	const props = defineProps<LaufbahnplanungOberstufeProps>();

	const tabManager = new TabManager([], <TabData>{}, async (value: TabData) => {});

	const showModalImport = ref<boolean>(false);

	const faecherAnzeigen = ref<'alle'|'nur_waehlbare'|'nur_gewaehlt'>('alle');

	const hatFaecherNichtWaehlbar = computed<boolean>(() => {
		for (const fach of props.abiturdatenManager().faecher().faecher())
			if (!(fach.istMoeglichEF1 || fach.istMoeglichEF2 || fach.istMoeglichQ11 || fach.istMoeglichQ12 || fach.istMoeglichQ21 || fach.istMoeglichQ22))
				return true;
		return false;
	});

	function switchFaecherAnzeigen() {
		switch (faecherAnzeigen.value) {
			case 'alle':
				faecherAnzeigen.value = hatFaecherNichtWaehlbar.value ? 'nur_waehlbare' : 'nur_gewaehlt';
				break;
			case 'nur_waehlbare':
				faecherAnzeigen.value = 'nur_gewaehlt';
				break;
			case 'nur_gewaehlt':
				faecherAnzeigen.value = 'alle';
				break;
		}
	}

	function textFaecherAnzeigen() {
		switch (faecherAnzeigen.value) {
			case 'alle':
				return "Alle";
			case 'nur_waehlbare':
				return "Nur wählbare"
			case 'nur_gewaehlt':
				return "Nur gewählte"
		}
	}

	async function switchModus() {
		// wenn EF1 und EF2 bereits festgelegt sind, macht der Hochschreibemodus
		// keinen Sinn mehr und wird deaktiviert.
		const festgelegt = props.gostJahrgangsdaten.istBlockungFestgelegt
		if (festgelegt[0] && festgelegt[1]) {
			switch (props.modus) {
				case 'normal':
					await props.setModus('manuell');
					break;
				case 'manuell':
					await props.setModus('normal');
					break;
				case 'hochschreiben':
					await props.setModus('normal');
					break;
			}
		} else {
			switch (props.modus) {
				case 'normal':
					await props.setModus('hochschreiben');
					break;
				case 'hochschreiben':
					await props.setModus('manuell');
					break;
				case 'manuell':
					await props.setModus('normal');
					break;
			}
		}
	}

	async function export_laufbahnplanung() {
		const { data, name } = await props.exportLaufbahnplanung();
		const link = document.createElement("a");
		console.log(data, name)
		link.href = URL.createObjectURL(data);
		link.download = name;
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}

	async function import_laufbahnplanung(formData: FormData) {
		const result = await props.importLaufbahnplanung(formData);
		return (result === null);
	}

</script>


<style lang="postcss" scoped>

	.page--content--laufbahnplanung {
		@apply gap-x-8 2xl:gap-x-12 relative overflow-y-hidden h-full;
		@apply px-4 lg:px-6 3xl:px-8 4xl:px-12 pt-8 pb-16;
	}

</style>
